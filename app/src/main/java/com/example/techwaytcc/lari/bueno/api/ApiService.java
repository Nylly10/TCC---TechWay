package com.example.techwaytcc.lari.bueno.api;

import com.example.techwaytcc.lari.bueno.model.LoginRequest;
import com.example.techwaytcc.lari.bueno.model.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("techway/api/login.php")
    Call<LoginResponse> login(
            @Body LoginRequest request
    );
}