package com.example.movieapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.repositories.MovieRepository;

import java.util.List;

public class MovieListViewModel extends ViewModel {
    //Class is used for ViewModel
    private MovieRepository movieRepository;

    //Constructor
    public MovieListViewModel()
    {
        movieRepository = MovieRepository.getInstance();
    }

    public LiveData<List<MovieModel>> getMovies()
    {
        return movieRepository.getMovies();
    }

    public LiveData<List<MovieModel>> getPopular()
    {
        return movieRepository.getPopular();
    }

    //3. Calling method in ViewModel

    public void searchMovieApi(String query, int pageNumber) {
        movieRepository.searchMovieApi(query, pageNumber);
    }

    public void searchMoviePopular(int pageNumber) {
        movieRepository.searchMoviePopular(pageNumber);
    }

    public void searchNextPage(){
        movieRepository.searchNextPage();
    }
}
