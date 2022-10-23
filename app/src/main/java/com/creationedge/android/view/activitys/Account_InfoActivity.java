package com.creationedge.android.view.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.model.Billing;
import com.creationedge.android.model.Shipping;
import com.creationedge.android.model.UserDataResponse;
import com.google.android.gms.common.api.Api;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Account_InfoActivity extends AppCompatActivity implements View.OnClickListener {
    private Method method = new Method();
    private CircleImageView profileImage;
    private FloatingActionButton img_image_change;
    private EditText userName,et_lname, userEmail;
    private RelativeLayout shipping_layout, billing_layout;
    private Button btn_save;
    private ImageView btn_back;
    private boolean isEditAble = true;
    private boolean isEdit = true;
    private Context mContext;
    private String id;
    private ProgressDialog pd;
    private UserDataResponse userDataResponse;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account__info);
        // initialization all views
        initalization();
        pd.show();
        // getting sharedPreferences Data
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);


        //getting UserCurrent Data
        getUserCurrentData(id);


    }

    private void getUserCurrentData(String id) {
        Call<UserDataResponse> call = ApiClient.getPostServices().getUserData(id, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    userDataResponse = response.body();
                    // setProfile Data
                    Picasso.get().load(userDataResponse.getAvatarUrl()).fit().into(profileImage);
                    userName.setText(userDataResponse.getFirstName());
                    et_lname.setText(userDataResponse.getLastName());
                    userEmail.setText(userDataResponse.getEmail());
                    SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor prefsEditr = sharedPreferences.edit();
                    prefsEditr.putString("name", userDataResponse.getFirstName() +" "+ userDataResponse.getLastName());
                    prefsEditr.putString("photo", userDataResponse.getAvatarUrl());
                    prefsEditr.apply();


                } else {
                    pd.dismiss();
                    Toast.makeText(Account_InfoActivity.this, "not success try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(Account_InfoActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initalization() {
        profileImage = findViewById(R.id.img_profile);
        img_image_change = findViewById(R.id.fb_btn_image);
        userName = findViewById(R.id.et_name);
        et_lname = findViewById(R.id.et_lname);
        userEmail = findViewById(R.id.et_email);
        shipping_layout = findViewById(R.id.shipping_layout);
        billing_layout = findViewById(R.id.billing_layout);
        btn_save = findViewById(R.id.btn_edit_save);
        btn_back=findViewById(R.id.img_backbutton);
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setMessage("Getting Data....");
        mContext = this;

        img_image_change.setOnClickListener(this);
        shipping_layout.setOnClickListener(this);
        billing_layout.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        btn_back.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.shipping_layout) {
           showingShippingBottomDialog(userDataResponse.getShipping());
        }
        if (id == R.id.billing_layout) {

           showingBillingBottomDialog();
        }
        if (id == R.id.fb_btn_image) {

        }
        if (id==R.id.img_backbutton){
            finish();
        }
        if (id == R.id.btn_edit_save) {
            if (btn_save.getText().toString().equals("Edit")) {
                //todo when able to edit
                btn_save.setText("Save");
                userName.setEnabled(true);
                userName.setCursorVisible(true);
                userName.requestFocus();
                et_lname.setEnabled(true);
                userEmail.setEnabled(true);
                img_image_change.setVisibility(View.VISIBLE);
                shipping_layout.setVisibility(View.GONE);
                billing_layout.setVisibility(View.GONE);


            } else {
                // todo when able to save
                userName.setEnabled(false);
                et_lname.setEnabled(false);
                userEmail.setEnabled(false);
                img_image_change.setVisibility(View.GONE);
                shipping_layout.setVisibility(View.VISIBLE);
                billing_layout.setVisibility(View.VISIBLE);
                saveProfileinfo();

            }
        }

    }

    private void saveProfileinfo() {
        String uName = userName.getText().toString();
        String uLname = et_lname.getText().toString();
        String uEmail = userEmail.getText().toString();
        if (uName.length() == 0) {
            userName.setError("Please fill out this field");
        } else if (uLname.length() == 0) {
            et_lname.setError("Please fill out this field");
        } else if (uEmail.length() == 0) {
            userEmail.setError("Please fill out this field");
        } else {
            pd.setMessage("Saving info...");
            pd.show();
            UserDataResponse user = new UserDataResponse();
            user.setFirstName(uName);
            user.setLastName(uLname);
            user.setEmail(uEmail);

            Call<UserDataResponse> call = ApiClient.getPostServices().editProfile(id, Common.CONSUMER_KEY, Common.CONSUMER_SECRET, user);
            call.enqueue(new Callback<UserDataResponse>() {
                @Override
                public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                    if (response.isSuccessful()) {
                        UserDataResponse userDataResponse = response.body();
                        pd.dismiss();
                        method.showToastMessage("Data Saved.",1,Account_InfoActivity.this);
                        btn_save.setText("Edit");
                        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
                        SharedPreferences.Editor prefsEditr = sharedPreferences.edit();
                        prefsEditr.putString("name", userDataResponse.getFirstName() +" "+ userDataResponse.getLastName());
                        prefsEditr.putString("photo", userDataResponse.getAvatarUrl());
                        prefsEditr.apply();
                    } else {
                        btn_save.setText("Edit");
                        method.showToastMessage("Something went wrong! Please try again",0,Account_InfoActivity.this);
                        pd.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<UserDataResponse> call, Throwable t) {
                    btn_save.setText("Edit");
                    method.showToastMessage("Something went wrong! Please try again",0,Account_InfoActivity.this);
                    pd.dismiss();
                }
            });
            }

    }

/*    private void showingShippingBottomDialog(Shipping shipping) {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.row_shipping_address_bottom_dialog_layout);
        bottomSheetDialog.setCancelable(true);

        // declared view
        ImageView close;
        Button save;
        EditText name, address, address2, state, city, postalCode, country;

        // initialization view
        name = bottomSheetDialog.findViewById(R.id.et_b_name);
        address = bottomSheetDialog.findViewById(R.id.et_b_address);
        state = bottomSheetDialog.findViewById(R.id.et_b_state);
        city = bottomSheetDialog.findViewById(R.id.et_b_city);
        postalCode = bottomSheetDialog.findViewById(R.id.et_b_postalcode);
        country = bottomSheetDialog.findViewById(R.id.et_b_country);
        save = bottomSheetDialog.findViewById(R.id.bnt_edit);
        close = bottomSheetDialog.findViewById(R.id.img_close);


        // set info
        name.setText(shipping.getFirstName());
        address.setText(shipping.getAddress1());
        state.setText(shipping.getState());
        city.setText(shipping.getCity());
        postalCode.setText(shipping.getPostcode());
        country.setText(shipping.getCountry());

        // close dialog
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        // edit or save info
        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (isEdit) {

                    isEdit = false;
                    save.setText("Save");
                    name.setEnabled(true);
                    name.requestFocus();
                    address.setEnabled(true);
                    state.setEnabled(true);
                    city.setEnabled(true);
                    postalCode.setEnabled(true);
                    country.setEnabled(true);

                } else {
                    // save request shipping address
                  //  saveShippingAddress(name.getText().toString(),address.getText().toString(),address2.getText().toString(),state.getText().toString(),city.getText().toString(),postalCode.getText().toString(),country.getText().toString());


                }
            }
        });

        bottomSheetDialog.show();
    }

    private void showingBillingBottomDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.row_billing_address_bottom_dialog_layout);
        bottomSheetDialog.setCancelable(true);

        // declared view
        ImageView close;
        Button save;
        EditText name, address, address2, state, city, postalCode, country;

        // initialization view
        name = bottomSheetDialog.findViewById(R.id.et_b_name);
        address = bottomSheetDialog.findViewById(R.id.et_b_address);
     //   address2 = bottomSheetDialog.findViewById(R.id.et_b_address_2);
        state = bottomSheetDialog.findViewById(R.id.et_b_state);
        city = bottomSheetDialog.findViewById(R.id.et_b_city);
        postalCode = bottomSheetDialog.findViewById(R.id.et_b_postalcode);
        country = bottomSheetDialog.findViewById(R.id.et_b_country);
        save = bottomSheetDialog.findViewById(R.id.btn_edit);
        close = bottomSheetDialog.findViewById(R.id.img_close);

        // getting billing info
        Billing billing = userDataResponse.getBilling();

        // setup billing info
        name.setText(billing.getFirstName());
        address.setText(billing.getAddress1());
        address2.setText(billing.getAddress2());
        state.setText(billing.getState());
        city.setText(billing.getCity());
        postalCode.setText(billing.getPostcode());
        country.setText(billing.getCountry());

        // close dialog
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });

        // edit or save info
        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                if (isEdit) {

                    isEdit = false;
                    save.setText("Save");
                    name.setEnabled(true);
                    name.requestFocus();
                    address.setEnabled(true);
                    address2.setEnabled(true);
                    state.setEnabled(true);
                    city.setEnabled(true);
                    postalCode.setEnabled(true);
                    country.setEnabled(true);

                } else {

                    isEdit = true;
                    save.setText("Edit");
                    name.setEnabled(false);
                    address.setEnabled(false);
                    address2.setEnabled(false);
                    state.setEnabled(false);
                    city.setEnabled(false);
                    postalCode.setEnabled(false);
                    country.setEnabled(false);
                    // save request billing address
                    saveBillingAddress(name.getText().toString(),address.getText().toString(),address2.getText().toString(),state.getText().toString(),city.getText().toString(),postalCode.getText().toString(),country.getText().toString());


                }
            }
        });

        bottomSheetDialog.show();
    }

    private void saveShippingAddress(String name, String address, String address2, String state,String city, String postalcode,String country)
    {

        pd.setMessage("Saving Billing Address...");
        pd.show();
        UserDataResponse user=new UserDataResponse();
        Shipping bill=new Shipping();
        bill.setFirstName(name);
        bill.setAddress1(address);
        bill.setAddress2(address2);
        bill.setState(state);
        bill.setCity(city);
        bill.setPostcode(postalcode);
        bill.setCountry(country);
        user.setShipping(bill);
        Call<UserDataResponse> call=ApiClient.getPostServices().editProfile(id,Common.CONSUMER_KEY,Common.CONSUMER_SECRET,user);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(Account_InfoActivity.this, "Saved your new data!", Toast.LENGTH_SHORT).show();
                }else{
                    pd.dismiss();
                    Toast.makeText(Account_InfoActivity.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(Account_InfoActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveBillingAddress(String name, String address, String address2, String state,String city,
                                    String postalcode,String country) {
        pd.setMessage("Saving Billing Address...");
        pd.show();
        UserDataResponse user=new UserDataResponse();
        Billing bill=new Billing();
        bill.setFirstName(name);
        bill.setAddress1(address);
        bill.setAddress2(address2);
        bill.setState(state);
        bill.setCity(city);
        bill.setPostcode(postalcode);
        bill.setCountry(country);
        user.setBilling(bill);
        Call<UserDataResponse> call=ApiClient.getPostServices().editProfile(id,Common.CONSUMER_KEY,Common.CONSUMER_SECRET,user);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()){
                    pd.dismiss();
                    Toast.makeText(Account_InfoActivity.this, "Saved your new data!", Toast.LENGTH_SHORT).show();
                }else{
                    pd.dismiss();
                    Toast.makeText(Account_InfoActivity.this, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(Account_InfoActivity.this, "failed", Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    private void showingShippingBottomDialog(Shipping shipping)
    {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.row_shipping_address_bottom_dialog_layout);
        bottomSheetDialog.setCancelable(true);

        // declared view
        ImageView close;
        Button save;
        EditText name,lname, address, state, city, postalCode, country;

        // initialization view
        name = bottomSheetDialog.findViewById(R.id.et_b_name);
        lname = bottomSheetDialog.findViewById(R.id.et_lname);
        address = bottomSheetDialog.findViewById(R.id.et_b_address);
        state = bottomSheetDialog.findViewById(R.id.et_b_state);
        city = bottomSheetDialog.findViewById(R.id.et_b_city);
        postalCode = bottomSheetDialog.findViewById(R.id.et_b_postalcode);
        country = bottomSheetDialog.findViewById(R.id.et_b_country);
        save = bottomSheetDialog.findViewById(R.id.bnt_edit);
        close = bottomSheetDialog.findViewById(R.id.img_close);
        // set info
        name.setText(shipping.getFirstName());
        lname.setText(shipping.getLastName());
        address.setText(shipping.getAddress1());
        state.setText(shipping.getState());
        city.setText(shipping.getCity());
        postalCode.setText(shipping.getPostcode());
        country.setText(shipping.getCountry());
        // close dialog
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        // edit or save info
        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String btn_txt = save.getText().toString().trim();
                if(btn_txt.equals("Edit")){
                    save.setText("Save");
                    name.setEnabled(true);
                    name.requestFocus();
                    lname.setEnabled(true);
                    address.setEnabled(true);
                    state.setEnabled(true);
                    city.setEnabled(true);
                    postalCode.setEnabled(true);
                    country.setEnabled(true);
                }else {
                    bottomSheetDialog.dismiss();
                    save.setText("Edit");
                    name.setEnabled(false);
                    lname.setEnabled(false);
                    address.setEnabled(false);
                    state.setEnabled(false);
                    city.setEnabled(false);
                    postalCode.setEnabled(false);
                    country.setEnabled(false);
                    pd.setMessage("Saving Shipping Address...");
                    pd.show();
                    // save request shipping address
                    saveShippingAddress(name.getText().toString(),lname.getText().toString(),address.getText().toString(),state.getText().toString(),
                            city.getText().toString(),postalCode.getText().toString(),country.getText().toString());
                }
            }
        });

        bottomSheetDialog.show();
    }

    private void saveShippingAddress(String name, String lname,String address, String state,String city, String postalcode,String country)
    {
        UserDataResponse user=new UserDataResponse();
        Shipping bill=new Shipping();
        bill.setFirstName(name);
        bill.setLastName(lname);
        bill.setAddress1(address);
        bill.setAddress2(address);
        bill.setState(state);
        bill.setCity(city);
        bill.setPostcode(postalcode);
        bill.setCountry(country);
        user.setShipping(bill);
        Call<UserDataResponse> call=ApiClient.getPostServices().editProfile(id,Common.CONSUMER_KEY,Common.CONSUMER_SECRET,user);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()){
                    //get Current user data
                    getUserCurrentData(id);

                    pd.dismiss();
                    method.showToastMessage("Change shipping address",1,Account_InfoActivity.this);

                }else{
                    pd.dismiss();
                    method.showToastMessage("Something went wrong! Please try again",3,Account_InfoActivity.this);
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                pd.dismiss();
                method.showToastMessage("Something went wrong! Please try again",3,Account_InfoActivity.this);
            }
        });
    }

    private void showingBillingBottomDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(mContext, R.style.BottomSheetDialogTheme);
        bottomSheetDialog.setContentView(R.layout.row_billing_address_bottom_dialog_layout);
        bottomSheetDialog.setCancelable(true);

        // declared view
        ImageView close;
        Button save;
        EditText name,lname, phone, email, address, state, city, postalCode, country;

        // initialization view
        name = bottomSheetDialog.findViewById(R.id.et_b_name);
        lname = bottomSheetDialog.findViewById(R.id.et_lname);
        phone = bottomSheetDialog.findViewById(R.id.et_phone);
        email = bottomSheetDialog.findViewById(R.id.et_email);
        address = bottomSheetDialog.findViewById(R.id.et_b_address);
        state = bottomSheetDialog.findViewById(R.id.et_b_state);
        city = bottomSheetDialog.findViewById(R.id.et_b_city);
        postalCode = bottomSheetDialog.findViewById(R.id.et_b_postalcode);
        country = bottomSheetDialog.findViewById(R.id.et_b_country);
        save = bottomSheetDialog.findViewById(R.id.btn_edit);
        close = bottomSheetDialog.findViewById(R.id.img_close);

        // getting billing info
        Billing billing = userDataResponse.getBilling();

        // setup billing info
        name.setText(billing.getFirstName());
        lname.setText(billing.getLastName());
        phone.setText(billing.getPhone());
        email.setText(billing.getEmail());
        address.setText(billing.getAddress1());
        state.setText(billing.getState());
        city.setText(billing.getCity());
        postalCode.setText(billing.getPostcode());
        country.setText(billing.getCountry());

        // close dialog
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });
        // edit or save info
        save.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String btn_txt = save.getText().toString().trim();
                if(btn_txt.equals("Edit")){
                    save.setText("Save");
                    name.setEnabled(true);
                    name.requestFocus();
                    phone.setEnabled(true);
                    email.setEnabled(true);
                    address.setEnabled(true);
                    state.setEnabled(true);
                    city.setEnabled(true);
                    postalCode.setEnabled(true);
                    country.setEnabled(true);
                }else {
                    bottomSheetDialog.dismiss();
                    save.setText("Edit");
                    name.setEnabled(false);
                    phone.setEnabled(false);
                    email.setEnabled(false);
                    address.setEnabled(false);
                    state.setEnabled(false);
                    city.setEnabled(false);
                    postalCode.setEnabled(false);
                    country.setEnabled(false);
                    pd.setMessage("Saving Billing Address...");
                    pd.show();
                    // save request billing address
                    saveBillingAddress(name.getText().toString(),lname.getText().toString(),phone.getText().toString(),email.getText().toString(),address.getText().toString(),state.getText().toString(),city.getText().toString(),postalCode.getText().toString(),country.getText().toString());

                }
            }
        });

        bottomSheetDialog.show();
    }

    private void saveBillingAddress(String name,String lname, String phone, String email,String address, String state,String city,
                                    String postalcode,String country) {
        UserDataResponse user=new UserDataResponse();
        Billing bill=new Billing();
        bill.setFirstName(name);
        bill.setLastName(lname);
        bill.setPhone(phone);
        bill.setEmail(email);
        bill.setAddress1(address);
        bill.setAddress2(address);
        bill.setState(state);
        bill.setCity(city);
        bill.setPostcode(postalcode);
        bill.setCountry(country);
        user.setBilling(bill);
        Call<UserDataResponse> call=ApiClient.getPostServices().editProfile(id,Common.CONSUMER_KEY,Common.CONSUMER_SECRET,user);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()){
                    pd.dismiss();
                    method.showToastMessage("Change your billing address",1,Account_InfoActivity.this);
                }else{
                    pd.dismiss();
                    method.showToastMessage("Something went wrong! Please try again",3,Account_InfoActivity.this);
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                method.showToastMessage("Something went wrong! Please try again",3,Account_InfoActivity.this);
                pd.dismiss();
            }

        });

    }
}