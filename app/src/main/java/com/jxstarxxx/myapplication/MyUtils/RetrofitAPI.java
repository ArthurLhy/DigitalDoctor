package com.jxstarxxx.myapplication.MyUtils;

import com.jxstarxxx.myapplication.DTO.CaseTrackerDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitAPI {

    static final String REQUIRE_URL = "https://corona.lmao.ninja/v2/";

    @GET("countries")
    Call<List<CaseTrackerDTO>> getCountryData();
}
