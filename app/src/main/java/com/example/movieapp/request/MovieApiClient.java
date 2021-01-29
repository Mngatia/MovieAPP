package com.example.movieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.AppExecutors;
import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.utils.Credentials;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    //LiveData for search
    private MutableLiveData<List<MovieModel>> mMovies;
    private static MovieApiClient instance;

    //Making global Runnable request
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    //Live Data for popular movies
    private MutableLiveData<List<MovieModel>> mMoviesPopular;

    //Making popular runnable
    private RetrieveMoviesRunnablePopular retrieveMoviesRunnablePopular;



    public static MovieApiClient getInstance() {
        if (instance == null){
            instance = new MovieApiClient();
        }
        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
        mMoviesPopular = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies(){
        return mMovies;
    }
    public LiveData<List<MovieModel>> getMoviesPopular(){
        return mMoviesPopular;
    }

    //1. The method will be called through classes
    public void searchMoviesApi(String query, int pageNumber){

        if (retrieveMoviesRunnable != null){
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query, pageNumber);

        final Future myHandler = AppExecutors.getInstance().mNetworkIO().submit(retrieveMoviesRunnable);
        AppExecutors.getInstance().mNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancel the retrofit call
                myHandler.cancel(true);

            }
        }, 3000, TimeUnit.MILLISECONDS);

    }

    public void searchMoviesPopular(int pageNumber){

        if (retrieveMoviesRunnablePopular != null){
            retrieveMoviesRunnablePopular = null;
        }

        retrieveMoviesRunnablePopular = new RetrieveMoviesRunnablePopular(pageNumber);

        final Future myHandler2 = AppExecutors.getInstance().mNetworkIO().submit(retrieveMoviesRunnablePopular);
        AppExecutors.getInstance().mNetworkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancel the retrofit call
                myHandler2.cancel(true);

            }
        }, 1000, TimeUnit.MILLISECONDS);

    }

    //Retrieve data from RestAPI by runnable class
    //we have 2 queries: ID, Search Queries
    private class RetrieveMoviesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber){
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting response objects
            try {
                Response response = getMovies(query, pageNumber).execute();
                if (cancelRequest){
                    return;
                }
                if (response.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());
                    if (pageNumber == 1){
                        //sending data to live data
                        //PostValue - used or background thread
                        //setValue - not for background thread
                        mMovies.postValue(list);
                    }

                    }else {
                        String error = response.errorBody().string();
                        Log.v("Tag", "Error" +error);
                        mMovies.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }


        }
        //search method/query
        private Call<MovieSearchResponse> getMovies(String query, int pageNumber){
            return Services.getMovieApi().searchMovie(
                    Credentials.API_KEY,
                    query,
                    String.valueOf(pageNumber)
            );
        }
        private void CancelRequest(){
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }

    private class RetrieveMoviesRunnablePopular implements Runnable{

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnablePopular(int pageNumber){
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            //Getting response objects
            try {
                Response response2 = getPopular(pageNumber).execute();
                if (cancelRequest){
                    return;
                }
                if (response2.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response2.body()).getMovies());
                    if (pageNumber == 1){
                        //sending data to live data
                        //PostValue - used or background thread
                        //setValue - not for background thread
                        mMoviesPopular.postValue(list);
                    }

                }else {
                    String error = response2.errorBody().string();
                    Log.v("Tag", "Error" +error);
                    mMoviesPopular.postValue(null);
                }

            } catch (IOException e) {
                e.printStackTrace();
                mMoviesPopular.postValue(null);
            }


        }
        //search method/query
        private Call<MovieSearchResponse> getPopular(int pageNumber){
            return Services.getMovieApi().getPopular(
                    Credentials.API_KEY,
                    pageNumber
            );
        }
        private void CancelRequest(){
            Log.v("Tag", "Cancelling Search Request");
            cancelRequest = true;
        }
    }
}
