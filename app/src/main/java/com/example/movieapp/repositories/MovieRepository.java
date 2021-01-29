package com.example.movieapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.request.MovieApiClient;

import java.util.List;
import java.util.Queue;

import retrofit2.http.Query;

public class MovieRepository {
    //Class - acts as repository

    private static MovieRepository instance;

    //LiveData
    private final MovieApiClient movieApiClient;

    private String mQuery;
    private int mPageNumber;


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

    public LiveData<List<MovieModel>> getPopular()
    {
        return movieApiClient.getMoviesPopular();
    }

    //2. Calling the method in repository

    public void searchMovieApi(String query, int pageNumber) {
        mQuery = query;
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesApi(query, pageNumber);
    }

    public void searchMoviePopular(int pageNumber) {
        mPageNumber = pageNumber;
        movieApiClient.searchMoviesPopular(pageNumber);
    }

    public void searchNextPage(){
        searchMovieApi(mQuery, mPageNumber+1);
    }
}
