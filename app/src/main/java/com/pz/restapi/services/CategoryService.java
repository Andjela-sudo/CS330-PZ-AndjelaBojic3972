package com.pz.restapi.services;

import com.pz.restapi.models.Category;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CategoryService {
    @GET("category/")
    Call<List<Category>> getCategories();

    @GET("category/{id}")
    Call<Category> getCategoryById(@Path("id") Long id);

    @POST("category")
    Call<Category> addCategory(@Body Category category, @Header("Authorization") String authToken);

    @DELETE("category/{id}")
    Call<ResponseBody> deleteCategory(@Path("id") Long id, @Header("Authorization") String authToken);
}
