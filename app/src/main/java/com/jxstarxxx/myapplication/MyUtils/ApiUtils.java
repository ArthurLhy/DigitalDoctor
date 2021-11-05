package com.jxstarxxx.myapplication.MyUtils;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {
    private static Retrofit retrofit = null;

    public static RetrofitAPI getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(RetrofitAPI.REQUIRE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        Log.i("retrofit API", retrofit.baseUrl().toString());
        return retrofit.create(RetrofitAPI.class);
    }
}
