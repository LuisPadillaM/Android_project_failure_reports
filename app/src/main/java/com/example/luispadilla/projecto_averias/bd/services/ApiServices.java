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
    static OkHttpClient httpClient = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
        @Override
        public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
            Request original = chain.request();

            // Request customization: add request headers
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

    public static FailureService getFailureServices() {
        if(failureSingleton == null) {
            failureSingleton = jsonPlaceHolderInstance.create(FailureService.class);
        }
        return failureSingleton;
    }
}
