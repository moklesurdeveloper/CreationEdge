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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.model.UserDataResponse;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetNewPasswordFragment extends DialogFragment implements View.OnClickListener {

    private Context mContext;
    private View view;
    private TextInputEditText ed_old,ed_new,ed_retype;
    private Button btn_Save;
    private ProgressDialog pd;
    private Method method=new Method();
    private String id,oldassword;


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
        view=inflater.inflate(R.layout.fragment_set_new_password,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialization all views
        initAll(view);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id",null);
        oldassword=sharedPreferences.getString("password",null);


    }

    private void initAll(View view) {
        pd=new ProgressDialog(mContext);
        pd.setCancelable(false);
        pd.setMessage("Saving.....");
        ed_old=view.findViewById(R.id.input_old_password);
        ed_new=view.findViewById(R.id.input_new_password);
        ed_retype=view.findViewById(R.id.input_new_retype_password);
        btn_Save=view.findViewById(R.id.btn_save);

        btn_Save.setOnClickListener(this);
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
        if (id==R.id.btn_save){
            String Old = ed_old.getText().toString().trim();
            String Password = ed_new.getText().toString().trim();
            String Confirm_Password = ed_retype.getText().toString().trim();

            if (Old.length() == 0) {
                ed_old.setError("Please fill out this field");
            } else {
                if (Old.equals(oldassword)){
                    if (Password.length() < 6) {
                        ed_new.setError("Please fill out this field");
                    } else if (Confirm_Password.length() < 6) {
                        ed_retype.setError("Please fill out this field");
                    } else {
                        // todo after click save button
                        if (Password.equals(Confirm_Password)) {
                            pd.show();
                            savenewPassword(Confirm_Password);
                        } else {
                            ed_retype.setError("Please fill out this field");
                            method.showToastMessage("Don't match password and confirm password", 3, mContext);
                        }
                    }
                }else{
                    method.showToastMessage("Don't match current password! retype current password and try again!", 3, mContext);
                }


            }
            // todo after click save button
        }

    }
    public void savenewPassword(String newpassword){

        Call<UserDataResponse> call= ApiClient.getPostServices().editepassword(id, Common.CONSUMER_KEY,Common.CONSUMER_SECRET,newpassword);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()){
                    pd.dismiss();
                    method.showToastMessage("Password Changed Successfully.", 1, mContext);
                    getDialog().dismiss();
                }else{
                    pd.dismiss();
                   method.showToastMessage("Something went wrong please try again.",2,mContext);
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                pd.dismiss();
                method.showToastMessage("Failed please try again..",3,mContext);
            }
        });
    }
}
