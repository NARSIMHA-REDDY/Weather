package com.example.weather;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GetDataService {

    @GET("{id}")
    Call<CitiesWeather> getCitiesWeatherCall(@Path("id") int id);
}
