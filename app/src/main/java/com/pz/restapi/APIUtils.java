package com.pz.restapi;

import com.pz.restapi.services.AuthService;
import com.pz.restapi.services.CategoryService;
import com.pz.restapi.services.ListService;
import com.pz.restapi.services.MaterialService;
import com.pz.restapi.services.UserService;

public class APIUtils {
    private APIUtils(){
    };

    public static final String API_URL = "http://192.168.43.223:8080/api/";

    public static UserService getUserService(){
        return RetrofitClient.getClient(API_URL).create(UserService.class);
    }

    public static AuthService getAuthService(){
        return RetrofitClient.getClient(API_URL).create(AuthService.class);
    }

    public static CategoryService getCategoryService(){
        return RetrofitClient.getClient(API_URL).create(CategoryService.class);
    }

    public static MaterialService getMaterialService(){
        return RetrofitClient.getClient(API_URL).create(MaterialService.class);
    }

    public static ListService getListService(){
        return RetrofitClient.getClient(API_URL).create(ListService.class);
    }
}
