package com.pz.restapi.services;

import com.pz.restapi.models.JwtToken;
import com.pz.restapi.models.LogInRequest;
import com.pz.restapi.models.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthService {

    @POST("auth/signin")
    Call<JwtToken> signInUser(@Body LogInRequest logInRequest);

    @POST("auth/signup")
    Call<ResponseBody> signUpUser(@Body User user);
}
