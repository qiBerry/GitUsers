package com.example.gitusers;

import java.util.ArrayList;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;


public interface GitInterface {
    @GET("/users")
    public Call<ArrayList<User>> getUsersSinceId(@Query("since") int since);
    @GET("/users/{login}")
    public Call<User> getUserData(@Path("login") String login);
}
