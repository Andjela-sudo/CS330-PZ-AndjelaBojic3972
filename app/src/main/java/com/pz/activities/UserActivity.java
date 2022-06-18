package com.pz.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.pz.R;
import com.pz.adapters.UserAdapter;
import com.pz.restapi.APIUtils;
import com.pz.restapi.models.User;
import com.pz.restapi.services.UserService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserActivity extends AppCompatActivity {
    Button btnGetUsersList;
    ListView listView;

    UserService userService;
    List<User> list = new ArrayList<User>();

    EditText edtUId;
    EditText edtUsername;
    Button btnDel;
    TextView txtUId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_activity);

        btnGetUsersList = (Button) findViewById(R.id.btnGetUsersList);
        listView = (ListView) findViewById(R.id.listView);
        userService = APIUtils.getUserService();

        btnGetUsersList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get users list
                getUsersList();
            }
        });


        userService = APIUtils.getUserService();

//        Bundle extras = getIntent().getExtras();
//        final String userId = extras.getString("user_id");
//        String userName = extras.getString("user_name");
//
//        edtUId.setText(userId);
//        edtUsername.setText(userName);
//
//        if(userId != null && userId.trim().length() > 0 ){
//            edtUId.setFocusable(false);
//        } else {
//            txtUId.setVisibility(View.INVISIBLE);
//            edtUId.setVisibility(View.INVISIBLE);
//            btnDel.setVisibility(View.INVISIBLE);
//        }


    }

    public void getUsersList(){
        Call<List<User>> call = userService.getUsers();
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    list = response.body();
                    listView.setAdapter(new UserAdapter(UserActivity.this, R.layout.list_user, list));
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}