package com.example.movieapp.utils;

import com.example.movieapp.models.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieApi {
    //Search for movies
    //https://api.themoviedb.org/3/search/movie?api_key={api_key}&query=Jack+Reacher
    //http://api.themoviedb.org/3/movie/popular?api_key=e4989174a6558954c59d7b26abaf074c&page=1
    //movie_id = 550 is for The Response Fight Club movie
    //movie_id = 343611 for Jack Reacher

    @GET("3/search/movie")
    Call<MovieSearchResponse> searchMovie(
            @Query("api_key") String key,
            @Query("query") String query,
            @Query("page") String page
    );

    //Get popular movie
    //http://api.themoviedb.org/3/movie/popular  ?api_key=e4989174a6558954c59d7b26abaf074c&page=1
    @GET("/3/movie/popular")
    Call<MovieSearchResponse> getPopular(
            @Query("api_key") String key,
            @Query("page") int page
    );

    //Search with Id   https://api.themoviedb.org/3/movie/550?api_key=e4989174a6558954c59d7b26abaf074c
    @GET("3/movie/{movie_id}?")
    Call<MovieModel> getMovie(
            @Path("movie_id") int movie_id,
            @Query("api_key") String api_key
    );
}
