package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Details extends AppCompatActivity {

    public static Movies movies;
    public static Intent intent;
    public static TextView title,year,rate,overview;
    public static ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment,new PlaceHolderFragment())
                    .commit();
        }

    }



    public static class PlaceHolderFragment extends Fragment{
        public PlaceHolderFragment(){}


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
            View rootView=inflater.inflate(R.layout.details_fragment,container,false);
            WindowManager windowManager= (WindowManager) rootView.getContext().getSystemService(Context.WINDOW_SERVICE);
            Display display = windowManager.getDefaultDisplay();
            initComponents(rootView);
            setValues(rootView);
            return rootView;
        }

        public void initComponents (View rootView){
            movies = new Movies();
            Details.intent = getActivity().getIntent();
            int id = intent.getIntExtra("movie_id",0);
            int position = intent.getIntExtra("movie_position",0);
            movies = MainActivity.moviesArrayList.get(position);
            title = (TextView) rootView.findViewById(R.id.movies_title);
            year = (TextView) rootView.findViewById(R.id.movies_year);
            rate = (TextView) rootView.findViewById(R.id.movies_rating);
            image = (ImageView) rootView.findViewById(R.id.movies_images);
            overview = (TextView) rootView.findViewById(R.id.movies_overview);
        }

        public static void setValues(View rootView){
            title.setText(movies.getOriginal_title());
            year.setText(movies.getRelease_date().substring(0,4));
            title.setVisibility(View.VISIBLE);
            rate.setText(movies.getVote_average()+"/10");
            overview.setText(movies.getOverview());

            String movie_images_url;
            if(movies.getImagePath()== APIHelper.image_not_found){
                movie_images_url= APIHelper.image_not_found;

            }
            else {
                movie_images_url = APIHelper.image_url + APIHelper.image_size + "/" + movies.getImagePath();
            }

            Picasso.with(rootView.getContext()).load(movie_images_url).into(image);
            image.setVisibility(View.VISIBLE);

        }
    }
}
