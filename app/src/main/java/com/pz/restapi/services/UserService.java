package com.pz.restapi.services;

import com.pz.restapi.models.Material;
import com.pz.restapi.models.User;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface UserService {
    @GET("users")
    Call<List<User>> getUsers();

    @GET("users/me")
    Call<User> getCurrentUser(@Header("Authorization") String authToken);

    @GET("users/{user_id}/lists")
    Call<List<com.pz.restapi.models.List>> getListsCreatedBy(@Path("user_id") String userId);

    @POST("users")
    Call<User> addUser(@Body User user, @Header("Authorization") String authToken);

    @DELETE("users/{username}")
    Call<ResponseBody> deleteUser(@Path("username") String username, @Header("Authorization") String authToken);

}
