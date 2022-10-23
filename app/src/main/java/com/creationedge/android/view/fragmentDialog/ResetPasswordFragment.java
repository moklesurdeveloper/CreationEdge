package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.creationedge.android.model.CodeResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordFragment extends DialogFragment implements View.OnClickListener {

    private Context mContext;
    private View view;
    private TextInputEditText email;
    private Button confirm;
    private ProgressDialog pd;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(),getTheme()){
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
        view=inflater.inflate(R.layout.reset_password,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialization all views
        initAll(view);

    }

    private void initAll(View view) {

        email=view.findViewById(R.id.input_email);
        confirm=view.findViewById(R.id.btn_confirm);
        pd=new ProgressDialog(mContext);
        pd.setCancelable(false);
        pd.setMessage("Sending OTP Code...");
        confirm.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog=getDialog();
        if (dialog!=null){
            int width=ViewGroup.LayoutParams.MATCH_PARENT;
            int height=ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width,height);
        }
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if (id==R.id.btn_confirm){
            if (!TextUtils.isEmpty(email.getText().toString())){
                pd.show();
                sendOTPCode(email.getText().toString());

            }else{
                email.setError("Please fill out this field!");
            }

        }
    }

    private void sendOTPCode(String email) {
        Call<CodeResponse> call= ApiClient.getForgotPasswordService().sendCode(email);
        call.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                if (response.isSuccessful()){
                    pd.dismiss();

                    CodeResponse codeResponse=response.body();
                    Common.EMAIL=email;
                    ReSetForgotPassword reSetForgotPassword = new ReSetForgotPassword();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    reSetForgotPassword.show(ft, "Tag");
                    getDialog().dismiss();
                }else {

                    pd.dismiss();
                    Toast.makeText(mContext, "Failed! Try again.", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(mContext, "Failed! Try again.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
