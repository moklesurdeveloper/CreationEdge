package com.creationedge.android.view.activitys;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.model.LogInResponse;
import com.creationedge.android.model.LoginRequest;
import com.creationedge.android.view.fragmentDialog.ResetPasswordFragment;
import com.creationedge.android.view.fragmentDialog.SignUpFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputEditText edEmail, edPassword;
    private TextView txtForgotPassword, txtSignUp;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // initialization all view
        initAll();
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Logging Your Account.");


    }

    private void initAll() {
        edEmail = findViewById(R.id.input_email);
        edPassword = findViewById(R.id.input_password);
        txtForgotPassword = findViewById(R.id.link_forgotPassword);
        txtSignUp = findViewById(R.id.link_signUp);
        txtForgotPassword.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            // todo after click login button
            String Email = Objects.requireNonNull(edEmail.getText()).toString().trim();
            String Password = Objects.requireNonNull(edPassword.getText()).toString().trim();
            if (Email.length() == 0 && Password.length() == 0) {
                edEmail.setError("Please fill out this field");
                edPassword.setError("Please fill out this field");
            } else {
                if (Email.length() == 0) {
                    edEmail.setError("Please Enter your account Email");
                } else if (Password.length() < 6) {
                    edPassword.setError("Please Enter your password");
                } else {
                    pd.show();
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setUsername(Email);
                    loginRequest.setPassword(Password);
                    Call<LogInResponse> call = ApiClient.getPostServiceDev().login(loginRequest);
                    call.enqueue(new Callback<LogInResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<LogInResponse> call, @NonNull Response<LogInResponse> response) {
                            if (response.isSuccessful()) {
                                LogInResponse logInResponse = response.body();
                                // Save UserLoginToken
                                SharedPreferences preferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user_token", logInResponse.getToken());
                                editor.putString("email", logInResponse.getUserEmail());
                                editor.putString("id", logInResponse.getUserId().toString());
                                editor.putString("name", logInResponse.getFirstName() + " " + logInResponse.getLastName());
                                editor.putString("photo", logInResponse.getAvatar());
                                editor.putString("password", edPassword.getText().toString());

                                editor.apply();
                                pd.dismiss();

                                // Intent HomeActivity when login successful
                                finish();
                            } else {
                                pd.dismiss();
                                Toast.makeText(LoginActivity.this, "not success", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<LogInResponse> call, @NonNull Throwable t) {
                            pd.dismiss();
                            Toast.makeText(LoginActivity.this, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }


        }
        if (id == R.id.link_forgotPassword) {
            // todo after click forgot password
            ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            resetPasswordFragment.show(ft, "Tag");
        }
        if (id == R.id.link_signUp) {
            // todo after click don't have account
            SignUpFragment purchaseHistoryFragment = new SignUpFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            purchaseHistoryFragment.show(ft, "Tag");
        }
    }

    @Override
    public void onBackPressed() {
        if (Common.logingCheck) {
            Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
            intent.putExtra("info", "home");
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        } else {
            finish();
        }
    }

}