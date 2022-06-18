package com.pz.restapi.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LogInRequest {
    @SerializedName("usernameOrEmail")
    @Expose
    private String usernameOrEmail;

    @SerializedName("password")
    @Expose
    private String password;

    public String getUsernameOrEmail() {
        return usernameOrEmail;
    }

    public void setUsernameOrEmail(String usernameOrEmail) {
        this.usernameOrEmail = usernameOrEmail;
    }

    public String getPassword() {
        return password;
    }

    public LogInRequest() {
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LogInRequest(String usernameOrEmail, String password) {
        this.usernameOrEmail = usernameOrEmail;
        this.password = password;
    }
}
