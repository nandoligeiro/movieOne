package com.ligeirostudio.movieone.retrofit;

import android.support.annotation.NonNull;
import android.util.Log;

import com.github.leonardoxh.livedatacalladapter.LiveDataCallAdapterFactory;
import com.github.leonardoxh.livedatacalladapter.LiveDataResponseBodyConverterFactory;
import com.ligeirostudio.movieone.BuildConfig;
import com.ligeirostudio.movieone.Constants;

import java.io.IOException;
import java.util.Date;


import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RequesterApi {
    private ApiService api;

    public RequesterApi() {

        OkHttpClient okClient = new OkHttpClient.Builder()
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("tic", String.valueOf(System.currentTimeMillis()));
                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(@NonNull Chain chain) throws IOException {

                        Request original = chain.request();
                        HttpUrl originalHttpUrl = original.url();

                        HttpUrl url = originalHttpUrl.newBuilder()
                                .addQueryParameter("api_key", BuildConfig.API_KEY)
                                .build();

                        Request.Builder requestBuilder = original.newBuilder()
                                .url(url);

                        Request request = requestBuilder.build();

                        Response response = chain.proceed(request);

                        String name = response.request().url().encodedPath();
                        Date tic = new Date(Long.parseLong(response.request().header("tic")));
                        Long time = System.currentTimeMillis() - tic.getTime();
                        Log.e("Track Timing Event", name + " - " + time + " milliseconds");

                        return response;
                    }
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okClient)
                .addCallAdapterFactory(LiveDataCallAdapterFactory.create())
                .addConverterFactory(LiveDataResponseBodyConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(ApiService.class);
    }

    public ApiService getApi() {
        return api;
    }

}
