package com.example.movieapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.request.MovieApiClient;

import java.util.List;

public class MovieRepository {
    //Class - acts as repository

    private static MovieRepository instance;

    //LiveData
    private MovieApiClient movieApiClient;

    public static MovieRepository getInstance(){
        if (instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }
    private MovieRepository(){

        movieApiClient  = MovieApiClient.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies()
    {
        return movieApiClient.getMovies();
    }

    //2. Calling the method in repository

    public void searchMovieApi(String query, int pageNumber) {
        movieApiClient.searchMoviesApi(query, pageNumber);
    }
}
