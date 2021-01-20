package com.example.movieapp.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.models.MovieModel;

import java.util.List;

public class MovieRepository {
    //Class - acts as repository

    private static MovieRepository instance;

    //LiveData
    private MutableLiveData<List<MovieModel>> mMovies;

    public static MovieRepository getInstance(){
        if (instance == null){
            instance = new MovieRepository();
        }
        return instance;
    }
    private MovieRepository(){
        mMovies  = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }
}
