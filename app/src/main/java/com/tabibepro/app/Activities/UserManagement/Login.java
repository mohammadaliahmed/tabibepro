package com.tabibepro.app.Activities.UserManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.tabibepro.app.Activities.MainActivity;
import com.tabibepro.app.Activities.ResetPassword;
import com.tabibepro.app.Models.UserModel;
import com.tabibepro.app.NetworkManager.AppConfig;
import com.tabibepro.app.NetworkManager.UserClient;
import com.tabibepro.app.NetworkResponses.LoginResponse;
import com.tabibepro.app.R;
import com.tabibepro.app.Utils.CommonUtils;
import com.tabibepro.app.Utils.PrefManager;
import com.tabibepro.app.Utils.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Login extends AppCompatActivity {

    EditText email, password;
    Button login;

    RelativeLayout wholeLayout;

    Button signup;
    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        password = findViewById(R.id.password);
        forgot = findViewById(R.id.forgot);
        email = findViewById(R.id.email);
        signup = findViewById(R.id.signup);
        login = findViewById(R.id.login);
        wholeLayout = findViewById(R.id.wholeLayout);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(Login.this,MainActivity.class));
                if (email.getText().length() == 0) {
                    email.setError("Entrez votre e-mail");
                } else if (!email.getText().toString().contains("@")) {
                    email.setError("Entrez une adresse email valide");
                } else if (password.getText().length() == 0) {
                    password.setError("Entrer le mot de passe");
                } else {

                    loginUser();
                }
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, ResetPassword.class));
            }
        });


    }


    private void loginUser() {
        wholeLayout.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call call = getResponse.loginUser(AppConfig.API_USERNAME, AppConfig.API_PASSWORD, email.getText().toString(), password.getText().toString());
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                wholeLayout.setVisibility(View.GONE);
                if (response.code() == 401) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtils.showToast(jObjError.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 404) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        CommonUtils.showToast(jObjError.getString("message"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else if (response.code() == 200) {
                    if (response.body() != null && response.body().getData() != null) {
                        if (response.body().getData().size() > 0) {
                            CommonUtils.showToast(response.body().getMessage());
                            UserModel model = response.body().getData().get(0);
                            if (model != null) {
                                wholeLayout.setVisibility(View.GONE);
                                SharedPrefs.setUserModel(model);
                                launchHomeScreen();
                            }

                        } else {
                            wholeLayout.setVisibility(View.GONE);
                            CommonUtils.showToast("E-mail ou mot de passe incorrect");
                        }
                    }
                }

            }


            @Override
            public void onFailure(Call call, Throwable t) {
                CommonUtils.showToast(t.getMessage());
                wholeLayout.setVisibility(View.GONE);

            }
        });
    }

    private void launchHomeScreen() {
        startActivity(new Intent(Login.this, MainActivity.class));
        finish();
    }
}
