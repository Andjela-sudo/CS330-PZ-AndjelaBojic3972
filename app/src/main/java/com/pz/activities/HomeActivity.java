package com.pz.activities;

import com.pz.R;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pz.adapters.ListsListAdapter;
import com.pz.restapi.APIUtils;
import com.pz.restapi.models.Item;
import com.pz.restapi.models.Material;
import com.pz.restapi.models.User;
import com.pz.restapi.services.ListService;
import com.pz.restapi.services.UserService;
import com.yarolegovich.lovelydialog.LovelyCustomDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    UserService userService;
    ListService listService;
    User currentUser;
    List<com.pz.restapi.models.List> lists = new ArrayList<>();
    String authToken;
    private EditText created_list_name;
    Dialog lovelyCustomDialog;

    //BottomAppBar bottomAppBar;
    FloatingActionButton fab_main;
    RecyclerView recyclerView;


    ListsListAdapter listsAdapter;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setEnterTransition(null);

        fab_main = findViewById(R.id.fab_main);
        fab_main.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("------ + ------");
                LayoutInflater inflater = LayoutInflater.from(HomeActivity.this);
                final View view1 = inflater.inflate(R.layout.popup_add_list, null);
                created_list_name = view1.findViewById(R.id.name_list_popup);


                lovelyCustomDialog = new LovelyCustomDialog(HomeActivity.this)
                        .setView(view1)
                        .setTopColorRes(R.color.theme_light)
                        .setIcon(R.drawable.ic_plus)
                        .setTitle("Add list")
                        .setListener(R.id.list_add_btn_popup, new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onClick(View view) {
                                final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                                progressDialog.setMessage("Creating item..");
                                progressDialog.show();

                                String createdlistName = created_list_name.getText().toString();
                                com.pz.restapi.models.List newList = new com.pz.restapi.models.List(createdlistName,currentUser,null);

                                if (!created_list_name.getText().toString().trim().equalsIgnoreCase("")) {
                                    Call<com.pz.restapi.models.List> call = listService.addList(newList, authToken);
                                    call.enqueue(new Callback<com.pz.restapi.models.List>() {
                                        @Override
                                        public void onResponse(Call<com.pz.restapi.models.List> call, Response<com.pz.restapi.models.List> response) {
                                            if (response.isSuccessful()) {
                                                com.pz.restapi.models.List createdList = response.body();
                                                System.out.println("----list created ----");
                                                System.out.println(createdList);
                                                progressDialog.dismiss();
                                                lovelyCustomDialog.dismiss();
                                                getLists();
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<com.pz.restapi.models.List> call, Throwable t) {
                                            Log.e("ERROR: ", t.getMessage());
                                            final ProgressDialog progressDialog = new ProgressDialog(HomeActivity.this);
                                            progressDialog.setMessage("Creating item..");
                                            progressDialog.show();
                                        }
                                    });
                                }


                            }
                        })
                        .setListener(R.id.list_cancel_btn_popup, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                lovelyCustomDialog.dismiss();
                            }
                        })
                        .show();
            }
        });
        recyclerView = findViewById(R.id.recyclerView_main);

        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(staggeredGridLayoutManager);


        authToken = getAuthToken(savedInstanceState);

        userService = APIUtils.getUserService();
        listService = APIUtils.getListService();

        getCurrentUser();

        getLists();

    }

    public String getAuthToken(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                return null;
            } else {
                return extras.getString("jwt");
            }
        } else {
            return (String) savedInstanceState.getSerializable("jwt");
        }
    }

    public void getCurrentUser() {

        Call<User> call = userService.getCurrentUser(authToken);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    currentUser = response.body();
                    System.out.println(currentUser);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    public void showLists(List<com.pz.restapi.models.List> lists) {

        RecyclerView.Adapter mAdapter = new ListsListAdapter(HomeActivity.this, lists);
        recyclerView.setAdapter(mAdapter);
    }

    public void getLists() {

        Call<List<com.pz.restapi.models.List>> call = listService.getMyLists(authToken);
        call.enqueue(new Callback<List<com.pz.restapi.models.List>>() {
            @Override
            public void onResponse(Call<List<com.pz.restapi.models.List>> call, Response<List<com.pz.restapi.models.List>> response) {
                if (response.isSuccessful()) {
                    lists = response.body();
                    showLists(lists);
                }
            }

            @Override
            public void onFailure(Call<List<com.pz.restapi.models.List>> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("RESUME");
        getLists();
    }

}