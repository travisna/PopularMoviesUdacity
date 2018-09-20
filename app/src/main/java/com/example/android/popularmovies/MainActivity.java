package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    static public ArrayList<Movies> moviesArrayList;
    static public ArrayList<String> imagesArrayList;
    static public  ImageAdapter imageAdapter;
    static GridView gridView;
    public static Toast toast;
    static public String lastSortOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment, new MoviesFragment())
                    .commit();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();

        switch (id){
            case R.id.sort_popular:new FetchMovies().execute("popular",null);break;
            case R.id.sort_rating:new FetchMovies().execute("top_rated",null);break;

        }

        return super.onOptionsItemSelected(item);
    }


    public static class MoviesFragment extends Fragment{

        public MoviesFragment(){}


        @Override
        public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            View rootView = inflater.inflate(R.layout.main_fragment,container,false);
            setHasOptionsMenu(true);

            gridView=(GridView) rootView.findViewById(R.id.images_grid);
            int or = getResources().getConfiguration().orientation;
            gridView.setNumColumns(or == Configuration.ORIENTATION_LANDSCAPE ? 3 : 2);
            gridView.setAdapter(imageAdapter);
            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(getActivity(),Details.class);
                    intent.putExtra("movie_id",moviesArrayList.get(position).getId());
                    intent.putExtra("movie_position",position);

                    startActivity(intent);
                }
            });


            return rootView;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            outState.putParcelableArrayList("movies",MainActivity.moviesArrayList);
            outState.putStringArrayList("images",MainActivity.imagesArrayList);

            super.onSaveInstanceState(outState);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            if(savedInstanceState!=null && savedInstanceState.containsKey("movies")){
                moviesArrayList= savedInstanceState.getParcelableArrayList("movies");
                imagesArrayList= savedInstanceState.getStringArrayList("images");
            }
            else{
                moviesArrayList= new ArrayList<Movies>();
                imagesArrayList = new ArrayList<String>();
                imageAdapter = new ImageAdapter(getActivity());
                updateMovies();
            }

            super.onCreate(savedInstanceState);
        }

        @Override
        public void onResume() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortCriteria = sharedPreferences.getString("sorting_criteria","popular");

            if(lastSortOrder!=null && !sortCriteria.equals(lastSortOrder)){
                moviesArrayList= new ArrayList<Movies>();
                imagesArrayList = new ArrayList<String>();
                updateMovies();
            }
            lastSortOrder = sortCriteria;
            super.onResume();
        }

        public void updateMovies(){
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortCriteria = sharedPreferences.getString(getString(R.string.pref_sorting_criteria_key),"popular");
            new FetchMovies().execute(sortCriteria,null);
        }

    }

}
