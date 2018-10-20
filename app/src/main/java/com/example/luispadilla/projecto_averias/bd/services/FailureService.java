package com.example.luispadilla.projecto_averias.bd.services;

import com.example.luispadilla.projecto_averias.bd.Failure;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;



public interface FailureService {
    @GET("averias")
    Call<List<Failure>> getAllFailures();

    @GET("averias/{id}")
    Call<Failure> getFailureById(@Path("id") String id);

    @POST("averias")
    Call<Failure> createFailure(@Body Failure newFailure);

    @POST("averias/{id}")
    Call<Failure> updateFailure(@Path("id") String id, @Body Failure currentFailure);

    @DELETE("averias/{id}")
    Call<Failure> deleteFailure(@Path("id") String id);
}
