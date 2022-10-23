package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.model.LogInResponse;
import com.creationedge.android.model.LoginRequest;
import com.creationedge.android.view.activitys.HomeActivity;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignInFragment extends DialogFragment implements View.OnClickListener {

    private Context mContext;
    private View view;
    private TextInputEditText edEmail, edPassword;
    private Button btnSignIn, google, facebook;
    private TextView txtForgotPassword, txtSignUp;
    private ProgressDialog pd;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                getDialog().dismiss();
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_dialog_sign_in, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialization all view
        initAll(view);
        pd=new ProgressDialog(mContext);
        pd.setCancelable(false);
        pd.setMessage("Logging Your Account.");


    }

    private void initAll(View view) {
        edEmail = view.findViewById(R.id.input_email);
        edPassword = view.findViewById(R.id.input_password);
        btnSignIn = view.findViewById(R.id.btn_login);
        txtForgotPassword = view.findViewById(R.id.link_forgotPassword);
        txtSignUp = view.findViewById(R.id.link_signUp);

        btnSignIn.setOnClickListener(this);
        txtForgotPassword.setOnClickListener(this);
        txtSignUp.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_login) {
            // todo after click login button
            String Email = edEmail.getText().toString().trim();
            String Password = edPassword.getText().toString().trim();
            if (Email.length() == 0 && Password.length() == 0) {
                edEmail.setError("Please fill out this field");
                edPassword.setError("Please fill out this field");
            } else {
                if (Email.length() == 0) {
                    edEmail.setError("Please fill out this field");
                } else if (Password.length() < 6) {
                    edPassword.setError("Please fill out this field");
                } else {
                    pd.show();
                    LoginRequest loginRequest = new LoginRequest();
                    loginRequest.setUsername(Email);
                    loginRequest.setPassword(Password);
                    Call<LogInResponse> call = ApiClient.getPostServiceDev().login(loginRequest);
                    call.enqueue(new Callback<LogInResponse>() {
                        @Override
                        public void onResponse(Call<LogInResponse> call, Response<LogInResponse> response) {
                            if (response.isSuccessful()) {
                                LogInResponse logInResponse = response.body();
                                // Save UserLoginToken
                                SharedPreferences preferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("user_token", logInResponse.getToken());
                                editor.putString("email", logInResponse.getUserEmail());
                                editor.putString("id",logInResponse.getUserId().toString());
                                editor.putString("name",logInResponse.getFirstName()+" "+logInResponse.getLastName());
                                editor.putString("photo",logInResponse.getAvatar());
                                editor.putString("password",edPassword.getText().toString());

                                editor.apply();
                                pd.dismiss();

                                // Intent HomeActivity when login successful
                                getDialog().dismiss();
                            } else {
                                pd.dismiss();
                                Toast.makeText(mContext, "not success", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<LogInResponse> call, Throwable t) {
                            pd.dismiss();
                            Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }


        }
        if (id == R.id.link_forgotPassword) {
            // todo after click forgot password
            ResetPasswordFragment resetPasswordFragment = new ResetPasswordFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            resetPasswordFragment.show(ft, "Tag");
        }
        if (id == R.id.link_signUp) {
            // todo after click don't have account
            SignUpFragment purchaseHistoryFragment = new SignUpFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            purchaseHistoryFragment.show(ft, "Tag");
        }
    }
}
