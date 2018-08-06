package com.udacity.getfit.network;


import com.udacity.getfit.dao.FitnessData;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by HARSHAD on  06/08/2018.
 */
public interface RetrofitAPIInterface {

    @GET("fitness")
    Call<List<FitnessData>> getFitnessData();
}
