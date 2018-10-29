package com.example.luispadilla.projecto_averias.bd.services;

import com.example.luispadilla.projecto_averias.utils.Constants;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiServices {

    private static FailureService failureSingleton;
    private static ImgurService imgurSingleton;
    static OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("x-api-key", Constants.failureAPIKey)
                    .header("Accept", "application/json")
                    .build();
            return chain.proceed(request);
        }
    }).build();

    private static final Retrofit jsonPlaceHolderInstance = new Retrofit.Builder()
            .baseUrl(Constants.failureAPI)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();


    static OkHttpClient httpImgClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
            Request original = chain.request();
            Request request = original.newBuilder()
                    .header("Authorization:", Constants.imgurAPIKey)
                    .build();
            return chain.proceed(request);
        }
    }).build();

    private static final Retrofit imgurInstance = new Retrofit.Builder()
            .baseUrl(Constants.imgurAPI)
            .client(httpImgClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static FailureService getFailureServices() {
        if(failureSingleton == null) {
            failureSingleton = jsonPlaceHolderInstance.create(FailureService.class);
        }
        return failureSingleton;
    }

    public static ImgurService getImgurServices() {
        if(imgurSingleton == null) {
            imgurSingleton = imgurInstance.create(ImgurService.class);
        }
        return imgurSingleton;
    }
}
