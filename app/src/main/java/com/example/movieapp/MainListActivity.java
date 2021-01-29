package com.example.movieapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.example.movieapp.adapter.MovieRecyclerView;
import com.example.movieapp.adapter.OnMovieListener;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.request.Services;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.utils.Credentials;
import com.example.movieapp.utils.MovieApi;
import com.example.movieapp.viewmodels.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainListActivity extends AppCompatActivity implements OnMovieListener {
    //(Before run the app, add the network security config)

    //RecyclerView
    private RecyclerView recyclerView;
    private MovieRecyclerView movieRecyclerViewAdapter;

    //ViewModel
    private MovieListViewModel movieListViewModel;

    boolean isPopulary = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //SearchView
        SetupSearchView();

        recyclerView = findViewById(R.id.recyclerView);
        movieListViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        ConfigureRecyclerView();
        ObserveAnyChange();
        ObservePopularyMovies();

        //Get data for popular movies
        movieListViewModel.searchMoviePopular(1);
    }

    private void ObservePopularyMovies() {
        movieListViewModel.getPopular().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                //Observing for any data change
                if (movieModels != null){
                    for (MovieModel movieModel: movieModels){
                        //Getting the data in log
                        Log.v("Tag", "onChanged: "+movieModel.getTitle());
                        movieRecyclerViewAdapter.setmMovies(movieModels);
                    }
                }

            }
        });

    }

    //Observing any data change
    private void ObserveAnyChange(){
        movieListViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {
                //Observing for any data change
                if (movieModels != null){
                    for (MovieModel movieModel: movieModels){
                        //Getting the data in log
                        Log.v("Tag", "onChanged: "+movieModel.getTitle());
                        movieRecyclerViewAdapter.setmMovies(movieModels);
                    }
                }

            }
        });
    }

    // 4. Call method in MainActivity
//    private void searchMovieApi(String query, int pageNumber){
//        movieListViewModel.searchMovieApi(query, pageNumber);
//    }
// 5 - Initializing recyclerView and adding data to it
    private void ConfigureRecyclerView(){
        //Live Data cannot be passed via the constructor
        movieRecyclerViewAdapter = new MovieRecyclerView( this);

        recyclerView.setAdapter(movieRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));



        //RecyclerView pagination. Loading next page of api response
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)){
                    //Display in search results on the next page api
                    movieListViewModel.searchNextPage();
                }
            }
        });
    }

    @Override
    public void onMovieClick(int position) {

      //  Toast.makeText(this, "The position " +position, Toast.LENGTH_SHORT).show();
        //We need the id of the movie in order to get all its details

        Intent intent = new Intent(this, MovieDetails.class);
        intent.putExtra("movie", movieRecyclerViewAdapter.getSelectedMovie(position));
        startActivity(intent);

    }

    @Override
    public void onCategoryClick(String category) {

    }

//Get data from SearchView and query the api to get the results(movies)
    private void SetupSearchView() {
        final SearchView searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                movieListViewModel.searchMovieApi(
                        //Search String got from SearchView
                        query,
                        1
                );
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isPopulary = false;
            }
        });
    }
}


