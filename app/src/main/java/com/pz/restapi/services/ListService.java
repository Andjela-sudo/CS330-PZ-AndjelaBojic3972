package com.pz.restapi.services;

import com.pz.restapi.models.Item;
import com.pz.restapi.models.Material;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ListService {
    @GET("list")
    Call<List<com.pz.restapi.models.List>> getLists();

    @GET("list/me")
    Call<List<com.pz.restapi.models.List>> getMyLists(@Header("Authorization") String authToken);

    @GET("list/{id}")
    Call<com.pz.restapi.models.List> getListById(@Path("id") Long id);

    @GET("list/user/{id}")
    Call<List<com.pz.restapi.models.List>> getListsByUserId(@Path("id") Long id);

    @POST("list")
    Call<com.pz.restapi.models.List> addList(@Body com.pz.restapi.models.List list, @Header("Authorization") String authToken);

    @DELETE("list/{id}")
    Call<ResponseBody> deleteList(@Path("id") Long id, @Header("Authorization") String authToken);

    @GET("list/item/{item_id}")
    Call<Item> getListItem(@Path("item_id") Long itemId);

    @POST("list/item/{material_id}")
    Call<Item> addListItem(@Body Item item, @Path("material_id") Long materialId, @Header("Authorization") String authToken);

    @DELETE("list/item/{item_id}")
    Call<ResponseBody> deleteListItem(@Path("item_id") Long itemId, @Header("Authorization") String authToken);

    @PUT("list/item/{id}")
    Call<Item> updateListItem(@Body Item newItem, @Path("id") Long id, @Header("Authorization") String authToken);

}
