package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.ItemClickListener;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.model.Billing;
import com.creationedge.android.model.CartDBModel;
import com.creationedge.android.model.CartRequest;
import com.creationedge.android.model.CheckoutRequest;
import com.creationedge.android.model.CheckoutResponse;
import com.creationedge.android.model.Shipping;
import com.creationedge.android.model.ShippingLine;
import com.creationedge.android.model.ShippingMethodCost;
import com.creationedge.android.model.ShippingMethodResponse;
import com.creationedge.android.model.ShippingMethodTitle;
import com.creationedge.android.model.UserDataResponse;
import com.creationedge.android.view.activitys.HomeActivity;
import com.google.gson.internal.$Gson$Preconditions;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OfflineCheckoutFragment extends DialogFragment implements View.OnClickListener {

    private EditText shippingfname, shippinglname, shippingcity, shippingpostalcode, shippingaddress,
            billingfname, billinglname, billingphone, billingemail, billingcountry, billingcity, billingpostalcode, billingaddress;
    private TextView btn_confirm, sub_total_amount, shipping_fee, total_amount;
    private RelativeLayout differentShipping;
    private EditText et_order_note, et_payment_number, et_transaction_id;
    private CheckBox checkBox;
    private ProgressBar item_progress_bar;
    private Toolbar toolbar;
    private String user_token, id;

    private boolean shipEdCheck = false;
    private String shippingTitle = " ", shippingCost = "0.0";
    private AppCompatSpinner sp_payment_method;
    private LinearLayout layout_payment, checkout_button;
    private String selectPaymentMethod;
    private int i;
    private View view;
    public Context mContext;
    private Method method = new Method();
    private ProgressDialog pd;
    private List<CartRequest> cartRequestList;
    Shipping sip = new Shipping();
    private String sfName, slName, sfCity, sfPostal, sfAddress, bfName, blName, bPhone, bEmail, bCountry, bCity, bPostal, bAddress;
    Shipping shipping;

    private UserDataResponse userDataResponse;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_offline_checkout, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        user_token = sharedPreferences.getString("user_token", null);
        id = sharedPreferences.getString("id", null);


        //int alll
        init(view);

        if (!TextUtils.isEmpty(id)) {
            getUserCurrentData(id);
        }

        pd.dismiss();

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    if (TextUtils.isEmpty(id)) {
                        sfName = shippingfname.getText().toString().trim();
                        slName = shippinglname.getText().toString().trim();
                        sfCity = shippingcity.getText().toString().trim();
                        sfPostal = shippingpostalcode.getText().toString().trim();
                        sfAddress = shippingaddress.getText().toString().trim();

                        differentShipping.setVisibility(View.VISIBLE);
                        sip.setFirstName(sfName);
                        sip.setLastName(slName);
                        sip.setAddress1(sfAddress);
                        sip.setCity(sfCity);
                        sip.setPostcode(sfPostal);
                    } else {
                        shippingfname.setText(shipping.getFirstName());
                        shippinglname.setText(shipping.getFirstName());
                        shippingaddress.setText(shipping.getFirstName());
                        shippingcity.setText(shipping.getFirstName());
                        shippingpostalcode.setText(shipping.getFirstName());
                    }


                } else {
                    differentShipping.setVisibility(View.GONE);
                }

            }
        });

        for (int i = 0; i < Common.cartDBModelList.size(); i++) {
            CartRequest cartRequest = new CartRequest();
            cartRequest.setProduct_id(String.valueOf(Common.cartDBModelList.get(i).getProductId()));
            cartRequest.setQuantity(String.valueOf(Common.cartDBModelList.get(i).getQuantity()));
            cartRequestList.add(cartRequest);
        }

        CharSequence[] entries = getResources().getTextArray(R.array.array_shiptopickup);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(sp_payment_method.getContext(), android.R.layout.simple_spinner_item, entries);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        sp_payment_method.setAdapter(adapter);
        sp_payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectPaymentMethod = parent.getItemAtPosition(position).toString();
                i = sp_payment_method.getSelectedItemPosition();
                if (i == 2 || i == 3 || i == 4) {
                    layout_payment.setVisibility(View.VISIBLE);
                } else {
                    layout_payment.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // set data
        sub_total_amount.setText("৳" + String.valueOf(Common.TOTAL_PRICE));
        getshippingpaymentlist();

    }

    private void init(View view) {
        shippingfname = view.findViewById(R.id.et_shipping_fname);
        shippinglname = view.findViewById(R.id.et_shipping_lname);
        shippingcity = view.findViewById(R.id.et_shipping_city);
        shippingpostalcode = view.findViewById(R.id.et_shipping_postal);
        shippingaddress = view.findViewById(R.id.et_shipping_address);

        billingfname = view.findViewById(R.id.et_billing_fname);
        billinglname = view.findViewById(R.id.et_billing_lname);
        billingphone = view.findViewById(R.id.et_billing_phone);
        billingemail = view.findViewById(R.id.et_billing_email);
        billingcity = view.findViewById(R.id.et_billing_city);
        billingpostalcode = view.findViewById(R.id.et_billing_postal);
        billingaddress = view.findViewById(R.id.et_billing_address);
        billingcountry = view.findViewById(R.id.et_billing_country);


        checkBox = view.findViewById(R.id.cb_differant);
        et_order_note = view.findViewById(R.id.et_order_note);
        sp_payment_method = view.findViewById(R.id.sp_shipping);
        layout_payment = view.findViewById(R.id.layout_payment);
        et_payment_number = view.findViewById(R.id.et_payment_number);
        et_transaction_id = view.findViewById(R.id.et_transaction_id);
        sub_total_amount = view.findViewById(R.id.sub_total_amount);
        shipping_fee = view.findViewById(R.id.shipping_fee);
        total_amount = view.findViewById(R.id.total_amount);
        btn_confirm = view.findViewById(R.id.tv_confirm_order_btn);
        checkout_button = view.findViewById(R.id.checkout_button);
        item_progress_bar = view.findViewById(R.id.item_progress_bar);
        differentShipping = view.findViewById(R.id.shipping_different_layout);

        cartRequestList = new ArrayList<>();

        // set onclick
        btn_confirm.setOnClickListener(this);

        //set toolbar and back
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(getString(R.string.confirm_order));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
        pd = new ProgressDialog(mContext);
        pd.setCancelable(false);
        pd.setMessage("Loading....");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_confirm_order_btn:

                String orderNote = et_order_note.getText().toString().trim();
                String payNumber = et_payment_number.getText().toString().trim();
                String transId = et_transaction_id.getText().toString().trim();
                String payvia = " ";

                bfName = billingfname.getText().toString().trim();
                blName = billinglname.getText().toString().trim();
                bPhone = billingphone.getText().toString().trim();
                bEmail = billingemail.getText().toString().trim();
                bCountry = billingcountry.getText().toString().trim();
                bCity = billingcity.getText().toString().trim();
                bPostal = billingpostalcode.getText().toString().trim();
                bAddress = billingaddress.getText().toString().trim();


                if (TextUtils.isEmpty(bfName) || TextUtils.isEmpty(blName) ||TextUtils.isEmpty(bEmail) ||TextUtils.isEmpty(bPhone) ||TextUtils.isEmpty(bCountry) ||TextUtils.isEmpty(bCity) ||TextUtils.isEmpty(bPostal) ||TextUtils.isEmpty(bAddress) ){

                    method.showToastMessage("input your information",2,mContext);

                }else {

                    if (selectPaymentMethod.equals("Select payment method")) {
                        method.showToastMessage("Please select your payment method", 0, mContext);
                    } else {
                        if (i == 2 || i == 3 || i == 4) {
                            if (payNumber.length() == 0) {
                                method.showToastMessage("Please enter payment number", 0, mContext);
                            } else if (transId.length() == 0) {
                                method.showToastMessage("Please enter transaction id", 0, mContext);
                            } else {
                                if (i == 2) {
                                    payvia = "pay via your bkash mobile banking";
                                    pd.show();
                                    confirmOrder(orderNote, selectPaymentMethod, payvia, payNumber, transId);
                                }
                                if (i == 3) {
                                    payvia = "pay via your rocket mobile banking";
                                    pd.show();
                                    confirmOrder(orderNote, selectPaymentMethod, payvia, payNumber, transId);
                                }

                                if (i == 5) {
                                    payvia = "pay via your nagad mobile banking";
                                    pd.show();
                                    confirmOrder(orderNote, selectPaymentMethod, payvia, payNumber, transId);
                                }
                            }
                        } else if (i == 1) {
                            payvia = "pay with cash upon delivery";
                            pd.show();
                            confirmOrder(orderNote, selectPaymentMethod, payvia, " ", " ");
                        }
                    }

                }



        }

    }

    private void confirmOrder(String orderNote, String paymentMethod, String payvia, String number, String transId) {


        CheckoutRequest checkoutRequest = new CheckoutRequest();
        //get billing data
/*        Billing billing = userDataResponse.getBilling();
        //Get Shipping data
        Shipping shipping = userDataResponse.getShipping();*/
        //Create object
        Billing bill = new Billing();

        List<ShippingLine> shippingLineList = new ArrayList<>();
        ShippingLine shippingLine = new ShippingLine();

        checkoutRequest.setPaymentMethod(paymentMethod);
        checkoutRequest.setPaymentMethodTitle(payvia);
        checkoutRequest.setTransaction_id(transId);
        checkoutRequest.setNumber(number);
        checkoutRequest.setCustomer_note(orderNote);
        checkoutRequest.setSetPaid(false);
        checkoutRequest.setStatus("on-hold");
        if (!TextUtils.isEmpty(id)) {
            checkoutRequest.setCustomer_id(id);
        }

        //set billing data
        bill.setFirstName(bfName);
        bill.setLastName(blName);
        bill.setPhone(bPhone);
        bill.setEmail(bEmail);
        bill.setAddress1(bAddress);
        bill.setCity(bCity);
        bill.setPostcode(bPostal);
        bill.setCountry(bCountry);

        checkoutRequest.setBilling(bill);
        if (checkBox.isChecked()) {
            checkoutRequest.setShipping(sip);

        }

        //Set cart item data
        checkoutRequest.setLineItems(cartRequestList);
        //Set shipping Line data
        shippingLine.setMethodId("flat_rate");
        shippingLine.setMethodTitle(shippingTitle);
        shippingLine.setTotal(shippingCost);
        shippingLineList.add(shippingLine);

        checkoutRequest.setShippingLines(shippingLineList);

        Call<CheckoutResponse> checkoutResponseCall = ApiClient.getPostServices().createOrder(Common.CONSUMER_KEY, Common.CONSUMER_SECRET, checkoutRequest);
        checkoutResponseCall.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                if (response.isSuccessful()) {
                    CheckoutResponse checkoutResponse = response.body();

                    method.showToastMessage("Thank you. Your order has been received.", 1, mContext);
                    pd.dismiss();
                    getActivity().finish();
                    startActivity(new Intent(mContext, HomeActivity.class));


                    //cart all data clear
/*                    Call<String> cartClearCall = ApiClient.getPostServicesCoCart().clearDataCart("Bearer "+user_token);
                    cartClearCall.enqueue(new Callback<String>() {
                        @Override
                        public void onResponse(Call<String> call, Response<String> response) {
                            if(response.isSuccessful()){
                                method.showToastMessage("Thank you. Your order has been received.",1,mContext);
                                pd.dismiss();
                                getActivity().finish();
                                startActivity(new Intent(mContext, HomeActivity.class));
                            } else {
                                method.showToastMessage("Please cart clear first.",0,mContext);
                                pd.dismiss();
                                getActivity().finish();
                                startActivity(new Intent(mContext, HomeActivity.class));

                            }

                        }

                        @Override
                        public void onFailure(Call<String> call, Throwable t) {
                            method.showToastMessage("Please cart clear first.",0,mContext);
                            pd.dismiss();
                            getActivity().finish();
                            startActivity(new Intent(mContext, HomeActivity.class));
                        }
                    });*/

                } else {
                    method.showToastMessage("Please try again.", 0, mContext);
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                method.showToastMessage("Please try again.", 0, mContext);
                pd.dismiss();
            }
        });
    }

    public void getshippingpaymentlist() {
        Call<List<ShippingMethodResponse>> listCall = ApiClient.getPostServices().getZoneShippingCost(Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        listCall.enqueue(new Callback<List<ShippingMethodResponse>>() {
            @Override
            public void onResponse(Call<List<ShippingMethodResponse>> call, Response<List<ShippingMethodResponse>> response) {
                if (response.isSuccessful()) {
                    List<ShippingMethodResponse> results = response.body();
                    ShippingMethodCost cost = results.get(0).getSettings().getCost();
                    ShippingMethodTitle title = results.get(0).getSettings().getTitle();

                    shippingTitle = title.getValue();
                    shippingCost = cost.getValue();

                    shipping_fee.setText("৳" + cost.getValue());
                    double shippingInt = Double.parseDouble(cost.getValue());
                    double totalCost = Common.TOTAL_PRICE + shippingInt;
                    total_amount.setText("৳" + String.valueOf(totalCost));

                    checkout_button.setVisibility(View.VISIBLE);
                    item_progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<ShippingMethodResponse>> call, Throwable t) {

            }
        });
    }


    private void getUserCurrentData(String id) {
        Call<UserDataResponse> call = ApiClient.getPostServices().getUserData(id, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()) {
                    userDataResponse = response.body();
                    //get Current user data
                    //shipping and total amount add
                    sub_total_amount.setText("৳" + String.valueOf(Common.TOTAL_PRICE));

                    //get and set shipping data
                    Billing billing = userDataResponse.getBilling();
                    billingfname.setText(billing.getFirstName());
                    billinglname.setText(billing.getLastName());
                    billingphone.setText(billing.getPhone());
                    billingemail.setText(billing.getEmail());
                    billingcountry.setText(billing.getCountry());
                    billingcity.setText(billing.getCity());
                    billingpostalcode.setText(billing.getPostcode());
                    billingaddress.setText(billing.getAddress1());

                    shipping = userDataResponse.getShipping();


/*                    if(shipEdCheck){
                        layout_shipping_address.setVisibility(View.VISIBLE);
                        tv_edit_shipping_addresses.setText("Cancel");
                        //get and set shipping data
                        Shipping shipping = userDataResponse.getShipping();
                        tv_shipping_u_name.setText(shipping.getFirstName()+" "+shipping.getLastName());
                        tv_shipping_full_address.setText(shipping.getAddress1()+"-"+shipping.getPostcode());
                    }else {
                        layout_shipping_address.setVisibility(View.GONE);
                        tv_edit_shipping_addresses.setText("Edit");
                    }*/

/*                    Call<List<ShippingMethodResponse>> listCall=ApiClient.getPostServices().getZoneShippingCost(Common.CONSUMER_KEY,Common.CONSUMER_SECRET);
                    listCall.enqueue(new Callback<List<ShippingMethodResponse>>() {
                        @Override
                        public void onResponse(Call<List<ShippingMethodResponse>> call, Response<List<ShippingMethodResponse>> response) {
                            if(response.isSuccessful())
                            {
                                List<ShippingMethodResponse> results = response.body();
                                ShippingMethodCost cost = results.get(0).getSettings().getCost();
                                ShippingMethodTitle title = results.get(0).getSettings().getTitle();

                                shippingTitle = title.getValue();
                                shippingCost = cost.getValue();

                                shipping_fee.setText("৳"+cost.getValue());
                                double shippingInt = Double.parseDouble(cost.getValue());
                                double totalCost = Common.TOTAL_PRICE + shippingInt;
                                total_amount.setText("৳"+String.valueOf(totalCost));

                               // off.setVisibility(View.VISIBLE);
                                checkout_button.setVisibility(View.VISIBLE);
                                item_progress_bar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ShippingMethodResponse>> call, Throwable t) {

                        }
                    });*/


                } else {
                    pd.dismiss();
                    Toast.makeText(mContext, "not success try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(mContext, "Failed", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
