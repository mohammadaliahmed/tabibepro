package com.tabibepro.app.Activities.UserManagement;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tabibepro.app.Activities.MainActivity;
import com.tabibepro.app.Models.UserModel;
import com.tabibepro.app.NetworkManager.AppConfig;
import com.tabibepro.app.NetworkManager.UserClient;
import com.tabibepro.app.NetworkResponses.LoginResponse;
import com.tabibepro.app.R;
import com.tabibepro.app.Utils.CommonUtils;
import com.tabibepro.app.Utils.SharedPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.appcompat.app.AppCompatActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Register extends AppCompatActivity {

    EditText firstName, lastName, email, password;
    Button login;

    RelativeLayout wholeLayout;
    String gender;
    RadioButton male, female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        password = findViewById(R.id.password);
        email = findViewById(R.id.email);
        login = findViewById(R.id.login);
        wholeLayout = findViewById(R.id.wholeLayout);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);


        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gender = "Male";
            }
        });
        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                gender = "Female";
            }
        });


        firstName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    /* Write your logic here that will be executed when user taps next button */
                    lastName.requestFocus();

                    handled = true;
                }

                return handled;
            }
        });
        lastName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    /* Write your logic here that will be executed when user taps next button */
                    email.requestFocus();

                    handled = true;
                }

                return handled;
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(Login.this,MainActivity.class));
                if (firstName.getText().length() == 0) {
                    firstName.setError("Enter first name");
                } else if (lastName.getText().length() == 0) {
                    lastName.setError("Enter lastname");
                } else if (email.getText().length() == 0) {
                    email.setError("Enter email");
                } else if (!email.getText().toString().contains("@")) {
                    email.setError("Enter email");
                } else if (password.getText().length() == 0) {
                    password.setError("Enter password");
                } else if (password.getText().length() < 8) {
                    password.setError("Enter 8 characters atleast");
                } else if (!password_Validation(password.getText().toString())) {
                    CommonUtils.showToast(getResources().getString(R.string.password_valid_error));
                } else if (gender == null) {
                    CommonUtils.showToast("Please select gender");
                } else {

                    registerUser();
                }
            }
        });


    }

    public boolean password_Validation(String password) {


        Pattern letter = Pattern.compile("[a-zA-z]");
        Pattern digit = Pattern.compile("[0-9]");
        Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
        //Pattern eight = Pattern.compile (".{8}");


        Matcher hasLetter = letter.matcher(password);
        Matcher hasDigit = digit.matcher(password);
        Matcher hasSpecial = special.matcher(password);

        if (hasLetter.find() && hasDigit.find() && hasSpecial.find()) {
            return true;
        } else {
            return false;
        }


    }


    private void registerUser() {
        wholeLayout.setVisibility(View.VISIBLE);
        UserClient getResponse = AppConfig.getRetrofit().create(UserClient.class);
        Call call = getResponse.registerUser(AppConfig.API_USERNAME, AppConfig.API_PASSWORD,
                firstName.getText().toString(),
                lastName.getText().toString(),
                gender,
                email.getText().toString(),
                password.getText().toString()
        );
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
                    if (response.body() != null) {
                        CommonUtils.showToast(response.body().getMessage());
//                        UserModel model = new UserModel();
//                        model.setFirstName(firstName.getText().toString());
//                        model.setLastName(lastName.getText().toString());
//                        model.setEmailAddress(email.getText().toString());
//                        model.setGender(gender);
//                        SharedPrefs.setUserModel(model);
//                        launchHomeScreen();
//                        loginUser();
                        Intent i = new Intent(Register.this, Login.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

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
                            CommonUtils.showToast("Wrong email or password");
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
        CommonUtils.showToast("Signup Successful");
        startActivity(new Intent(Register.this, MainActivity.class));
        finish();
    }
}
