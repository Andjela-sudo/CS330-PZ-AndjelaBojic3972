package com.pz.activities;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.pz.R;
import com.pz.restapi.APIUtils;
import com.pz.restapi.models.JwtToken;
import com.pz.restapi.models.LogInRequest;
import com.pz.restapi.models.User;
import com.pz.restapi.services.AuthService;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textView;
    int count = 0;

    AuthService authService;
    JwtToken jwt;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.signin_layout);
        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView);

        authService = APIUtils.getAuthService();
    }

    public void switchToSignin(View view) {
        setContentView(R.layout.signin_layout);
    }

    public void switchToSignup(View view) {
        setContentView(R.layout.signup_layout);
    }

    public void onSignInClick(View view) {
        EditText usernameOrEmail = (EditText) findViewById(R.id.signInEmailField);
        EditText password = (EditText) findViewById(R.id.signInPasswordField);

        LogInRequest logInRequest = new LogInRequest(usernameOrEmail.getText().toString(),password.getText().toString());
        Call<JwtToken> call = authService.signInUser(logInRequest);

        call.enqueue(new Callback<JwtToken>() {
            @Override
            public void onResponse(Call<JwtToken> call, Response<JwtToken> response) {
                if(response.isSuccessful()){
                    jwt = response.body();

                    SharedPreferences sharedPref = getSharedPreferences("AuthPreferences",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("authToken", jwt.getTokenType()+ " "+ jwt.getAccessToken());
                    editor.apply();

                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("jwt", jwt.getTokenType()+ " "+ jwt.getAccessToken());
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Wrong login data, try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<JwtToken> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onSignUpClick(View view) {
        EditText firstName = (EditText) findViewById(R.id.signUpFirstNameField);
        EditText lastName = (EditText) findViewById(R.id.signUpLastNameField);
        EditText email = (EditText) findViewById(R.id.signUpEmailField);
        EditText webSite = (EditText) findViewById(R.id.signUpSiteField);
        EditText phone = (EditText) findViewById(R.id.signUpPhoneField);
        EditText username = (EditText) findViewById(R.id.signUpUsernameField);
        EditText password = (EditText) findViewById(R.id.signUpPasswordField);

        User newUser =new User(firstName.getText().toString(),lastName.getText().toString(),username.getText().toString(),password.getText().toString(),email.getText().toString(),phone.getText().toString(), webSite.getText().toString());
        Call<ResponseBody> call = authService.signUpUser(newUser);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "Wrong signup data, try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ERROR: ", t.getMessage());
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}