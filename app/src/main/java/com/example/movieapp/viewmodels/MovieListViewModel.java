package com.example.movieapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.models.MovieModel;

import java.util.List;

public class MovieListViewModel extends ViewModel {
    //Class is used for ViewModel

    //LiveData
    private MutableLiveData<List<MovieModel>> mMovies = new MutableLiveData<>();

    //Constructor
    public MovieListViewModel() {
    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }
}
