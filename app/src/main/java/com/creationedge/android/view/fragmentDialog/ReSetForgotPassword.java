package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.model.CodeResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReSetForgotPassword extends DialogFragment implements View.OnClickListener {

    private Context mContext;
    private View view;
    private TextInputEditText ed_code, ed_new, ed_retype;
    private Button btn_Save;
    private Method method = new Method();
    private String email;
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
        view = inflater.inflate(R.layout.fragment_reset_forgot_password, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialization all views
        initAll(view);
        email= Common.EMAIL;

    }

    private void initAll(View view) {
        ed_code = view.findViewById(R.id.input_verify_code);
        ed_new = view.findViewById(R.id.input_new_password);
        ed_retype = view.findViewById(R.id.input_new_retype_password);
        btn_Save = view.findViewById(R.id.btn_save);
        btn_Save.setOnClickListener(this);
        pd=new ProgressDialog(mContext);
        pd.setCancelable(false);
        pd.setMessage("Saving your new Password....");
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
        if (id == R.id.btn_save) {
            String Code = ed_code.getText().toString().trim();
            String Password = ed_new.getText().toString().trim();
            String Confirm_Password = ed_retype.getText().toString().trim();

            if (Code.length() == 0) {
                ed_code.setError("Please fill out this field");
            } else {
                if (Password.length() < 6) {
                    ed_new.setError("Please fill out this field");
                } else if (Confirm_Password.length() < 6) {
                    ed_retype.setError("Please fill out this field");
                } else {
                    // todo after click save button
                    if (Password.equals(Confirm_Password)) {
                        pd.show();
                        savenewPassword(email, Password,Code);
                    } else {
                        ed_retype.setError("Please fill out this field");
                        method.showToastMessage("Don't match password and confirm password", 3, mContext);
                    }
                }
            }
        }
    }

    private void savenewPassword( String email,String password,String code) {
        Call<CodeResponse> call= ApiClient.getForgotPasswordService().setNewPassword(email,password,code);
        call.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if (response.isSuccessful()){
                    pd.dismiss();
                    getDialog().dismiss();
                }else{
                    pd.dismiss();
                    Toast.makeText(mContext, "Something went wrong tray again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(mContext, "Failed! Please Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
