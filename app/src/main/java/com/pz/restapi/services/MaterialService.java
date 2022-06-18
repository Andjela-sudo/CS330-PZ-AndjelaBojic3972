package com.pz.restapi.services;

import com.pz.restapi.models.Category;
import com.pz.restapi.models.Material;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface MaterialService {
    @GET("material/")
    Call<List<Material>> getMaterials();

    @GET("material/{id}")
    Call<Material> getMaterialById(@Path("id") Long id);

    @GET("material/category/{id}")
    Call<List<Material>> getMaterialsByCategoryId(@Path("id") Long id);

    @POST("material")
    Call<Material> addMaterial(@Body Material material, @Header("Authorization") String authToken);

    @DELETE("material/{id}")
    Call<ResponseBody> deleteMaterial(@Path("id") Long id, @Header("Authorization") String authToken);
}
