package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.model.RegisterRequest;
import com.creationedge.android.model.UserResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.ConfirmPassword;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Password;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpFragment extends DialogFragment implements View.OnClickListener, Validator.ValidationListener {
    private Context mContext;
    private View view;
    private Button btnSignUp;

    @NotEmpty
    private TextInputEditText edName;
    @NotEmpty
    private TextInputEditText edLastName;
    @NotEmpty
    private TextInputEditText edEmail;
    @NotEmpty
    @Password
    @Length(min = 6, max = 21)
    private TextInputEditText edPassword;
    @NotEmpty
    @ConfirmPassword
    private TextInputEditText edCPassword;

    private Method method = new Method();
    private ProgressDialog pd;

    private Validator validator;

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
        view = inflater.inflate(R.layout.fragment_dialog_sign_up, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialization all views
        initAll(view);
        pd = new ProgressDialog(mContext);
        pd.setCancelable(false);
        validator = new Validator(this);
        validator.setValidationListener(this);
    }

    private void initAll(View view) {
        btnSignUp = view.findViewById(R.id.btn_signUp);
        edName = view.findViewById(R.id.input_name);
        edLastName = view.findViewById(R.id.input_last_name);
        edEmail = view.findViewById(R.id.input_email);
        edPassword = view.findViewById(R.id.input_password);
        edCPassword = view.findViewById(R.id.input_confirm_password);

        btnSignUp.setOnClickListener(this);
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
        if (id == R.id.btn_signUp) {
            validator.validate();
        }
    }

    @Override
    public void onValidationSucceeded() {
        pd.show();

        String Name = edName.getText().toString().trim();
        String Last_Name = edLastName.getText().toString().trim();
        String Email = edEmail.getText().toString().trim();
        String Password = edPassword.getText().toString().trim();
        String Confirm_Password = edCPassword.getText().toString().trim();

        RegisterRequest request = new RegisterRequest();
        request.setUsername(Name + Last_Name);
        request.setFirst_name(Name);
        request.setLast_name(Last_Name);
        request.setEmail(Email);
        request.setPassword(Password);

        Call<UserResponse> callRegister = ApiClient.getPostServices().register(Common.CONSUMER_KEY, Common.CONSUMER_SECRET, request);
        callRegister.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    method.showToastMessage("Successfully Created a Account!", 1, mContext);
                    getDialog().dismiss();
                } else {
                    pd.dismiss();
                    method.showToastMessage("Something went wrong!", 3, mContext);
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                pd.dismiss();
                method.showToastMessage("Error: " + t.getLocalizedMessage(), 3, mContext);
            }
        });

    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(mContext);
            if (view instanceof TextInputEditText) {
                ((TextInputEditText) view).setError(message);
            } else {
                method.showToastMessage(message, 1, mContext);
            }
        }
    }
}
