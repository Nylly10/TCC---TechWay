package com.example.techwaytcc.lari.bueno.api;

import com.example.techwaytcc.lari.bueno.model.LoginRequest;
import com.example.techwaytcc.lari.bueno.model.LoginResponse;
import com.example.techwaytcc.lari.bueno.model.CadastroRequest;
import com.example.techwaytcc.lari.bueno.model.CadastroResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import okhttp3.ResponseBody;

public interface ApiService {

    @Headers("Content-Type: application/json")
    @POST("techway/api/login.php")
    Call<LoginResponse> login(@Body LoginRequest request);

    @Headers("Content-Type: application/json")
    @POST("techway/api/cadastrar.php")
    Call<ResponseBody> cadastrar(@Body CadastroRequest request);
}