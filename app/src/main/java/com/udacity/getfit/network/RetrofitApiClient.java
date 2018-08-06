package com.udacity.getfit.network;

import com.udacity.getfit.utils.AppConstants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by HARSHAD on 06/08/2018.
 */

public class RetrofitApiClient {

        static Retrofit retrofit = null;

        public static Retrofit getClient(){
            retrofit = new Retrofit.Builder()
                    .baseUrl(AppConstants.GET_FIT_URI)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            return retrofit;
        }

}
