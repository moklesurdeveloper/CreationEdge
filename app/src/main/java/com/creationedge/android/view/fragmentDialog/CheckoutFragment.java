package com.creationedge.android.view.fragmentDialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.model.Billing;
import com.creationedge.android.model.CartRequest;
import com.creationedge.android.model.CheckoutRequest;
import com.creationedge.android.model.CheckoutResponse;
import com.creationedge.android.model.Shipping;
import com.creationedge.android.model.ShippingLine;
import com.creationedge.android.model.ShippingMethodCost;
import com.creationedge.android.model.ShippingMethodResponse;
import com.creationedge.android.model.ShippingMethodTitle;
import com.creationedge.android.model.UserDataResponse;
import com.creationedge.android.view.activitys.Account_InfoActivity;
import com.creationedge.android.view.activitys.HomeActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutFragment extends DialogFragment implements View.OnClickListener {
    public static String TAG = "ReferFragment";
    private final Method method = new Method();
    private ProgressDialog pd;
    private TextView tv_billing_u_name, tv_shipping_full_address,tv_edit_shipping_addresses,tv_shipping_u_name,
            tv_edit_billing_addresses,tv_billing_full_address,et_phone,et_email,sub_total_amount
            ,shipping_fee,total_amount,tv_confirm_order_btn;
    private EditText et_order_note,et_payment_number,et_transaction_id;
    private LinearLayout layout_billing_address,layout_shipping_address,off,checkout_button;
    private ProgressBar item_progress_bar;
    private Toolbar toolbar;
    private String user_token,id;
    private UserDataResponse userDataResponse;
    private List<CartRequest> cartRequestList;
    private boolean shipEdCheck = false;
    private String shippingTitle =" ", shippingCost="0.0";
    private AppCompatSpinner sp_payment_method;
    private LinearLayout layout_payment;
    private String selectPaymentMethod;
    private int i;
    JSONArray jsonArray;
    JSONObject studentsObj;
    JSONObject student1;
    private View view;
    public Context mContext;

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
        view = inflater.inflate(R.layout.fragment_checkout, container, false);
        //int alll
        init(view);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // progress dialog init
        pd = new ProgressDialog(mContext);
        pd.setMessage("Loading....");
        pd.setCancelable(false);
        //get current user data to sharePre
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        user_token = sharedPreferences.getString("user_token",null);
        id = sharedPreferences.getString("id",null);

        cartRequestList = new ArrayList<>();
        //Get BD Shipping cost
      //  getShippingBDCost();
        //Get current user data
        getUserCurrentData(id);

        //get Cart list all data set cart request list
        for (int i = 0; i<Common.cartResponseList.size(); i++)
        {
            CartRequest cartRequest = new CartRequest();
            cartRequest.setProduct_id(String.valueOf(Common.cartResponseList.get(i).getProductId()));
            cartRequest.setQuantity(String.valueOf(Common.cartResponseList.get(i).getQuantity()));
            cartRequestList.add(cartRequest);
        }

        //crate json json
       /* jsonArray = new JSONArray();
        for (int i = 0; i<Common.cartResponseList.size(); i++)
        {
            student1 = new JSONObject();
            try {
                student1.put("product_id", Common.cartResponseList.get(i).getProductId());
                student1.put("quantity",  Common.cartResponseList.get(i).getQuantity());

            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            jsonArray.put(student1);
        }
        studentsObj = new JSONObject();
        try {
            studentsObj.put("line_items", jsonArray);
            String jsonStr = studentsObj.toString();
            Toast.makeText(mContext, ""+jsonStr, Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        //spinner shiping to addrss
        CharSequence[] entries = getResources().getTextArray(R.array.array_shiptopickup);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<>(sp_payment_method.getContext(), android.R.layout.simple_spinner_item, entries);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        sp_payment_method.setAdapter(adapter);
        sp_payment_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectPaymentMethod = parent.getItemAtPosition(position).toString();
               i = sp_payment_method.getSelectedItemPosition();
               if(i == 2 || i == 3 || i == 4){
                   layout_payment.setVisibility(View.VISIBLE);
               }else {
                   layout_payment.setVisibility(View.GONE);
               }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void init(View view) {

        tv_billing_u_name = view.findViewById(R.id.tv_name);
        tv_shipping_full_address = view.findViewById(R.id.tv_full_address);
        tv_edit_shipping_addresses = view.findViewById(R.id.tv_edit_shipping_addresses);
        tv_shipping_u_name = view.findViewById(R.id.tv_shipping_u_name);
        tv_edit_billing_addresses = view.findViewById(R.id.tv_edit_billing_addresses);
        layout_billing_address = view.findViewById(R.id.layout_billing_address);
        layout_shipping_address = view.findViewById(R.id.layout_shipping_address);
        tv_billing_full_address = view.findViewById(R.id.tv_billing_full_address);
        et_phone = view.findViewById(R.id.et_phone);
        et_email = view.findViewById(R.id.et_email);
        et_order_note = view.findViewById(R.id.et_order_note);
        sp_payment_method = view.findViewById(R.id.sp_shipping);
        layout_payment = view.findViewById(R.id.layout_payment);
        et_payment_number = view.findViewById(R.id.et_payment_number);
        et_transaction_id = view.findViewById(R.id.et_transaction_id);
        sub_total_amount = view.findViewById(R.id.sub_total_amount);
        shipping_fee = view.findViewById(R.id.shipping_fee);
        total_amount = view.findViewById(R.id.total_amount);
        tv_confirm_order_btn = view.findViewById(R.id.tv_confirm_order_btn);
        off = view.findViewById(R.id.off);
        checkout_button = view.findViewById(R.id.checkout_button);
        item_progress_bar = view.findViewById(R.id.item_progress_bar);

        tv_edit_shipping_addresses.setOnClickListener(this);
        tv_edit_billing_addresses.setOnClickListener(this);
        tv_confirm_order_btn.setOnClickListener(this);

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
        switch (v.getId()){
            case R.id.tv_edit_shipping_addresses:
                if(shipEdCheck){
                    shipEdCheck = false;
                    tv_edit_shipping_addresses.setText("Edit");
                    tv_shipping_u_name.setText("Ship to the different address");
                    layout_shipping_address.setVisibility(View.GONE);
                }else {
                    showingShippingBottomDialog(userDataResponse.getShipping());
                }

                break;
            case R.id.tv_edit_billing_addresses:
                showingBillingBottomDialog();
                break;
            case R.id.tv_confirm_order_btn:
                String orderNote = et_order_note.getText().toString().trim();
                String payNumber = et_payment_number.getText().toString().trim();
                String transId = et_transaction_id.getText().toString().trim();
                String payvia = " ";

                if(selectPaymentMethod.equals("Select payment method")){
                    method.showToastMessage("Please select your payment method",0,mContext);
                }else {
                    if(i == 2 || i == 3 || i == 4){
                        if (payNumber.length() == 0) {
                            method.showToastMessage("Please enter payment number",0,mContext);
                        }else  if (transId.length() == 0) {
                            method.showToastMessage("Please enter transaction id",0,mContext);
                        }else {
                            if(i == 2){
                                payvia = "pay via your bkash mobile banking";
                                pd.show();
                                confirmOrder(orderNote,selectPaymentMethod,payvia,payNumber,transId);
                            }
                            if(i == 3){
                                payvia = "pay via your rocket mobile banking";
                                pd.show();
                                confirmOrder(orderNote,selectPaymentMethod,payvia,payNumber,transId);
                            }

                            if(i == 5){
                                payvia = "pay via your nagad mobile banking";
                                pd.show();
                                confirmOrder(orderNote,selectPaymentMethod,payvia,payNumber,transId);
                            }
                        }
                    }else if(i == 1){
                        payvia = "pay with cash upon delivery";
                        pd.show();
                        confirmOrder(orderNote,selectPaymentMethod,payvia," "," ");
                    }

                }
                break;
        }
    }

    private void getUserCurrentData(String id)
    {
        Call<UserDataResponse> call = ApiClient.getPostServices().getUserData(id, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        call.enqueue(new Callback<UserDataResponse>() {
            @Override
            public void onResponse(Call<UserDataResponse> call, Response<UserDataResponse> response) {
                if (response.isSuccessful()) {
                    userDataResponse = response.body();
                    //get Current user data
                    //shipping and total amount add
                    sub_total_amount.setText("৳"+String.valueOf(Common.TOTAL_PRICE));

                    //get and set shipping data
                    Billing billing = userDataResponse.getBilling();
                    tv_billing_u_name.setText(billing.getFirstName()+" "+billing.getLastName());
                    tv_billing_full_address.setText(billing.getAddress1()+"-"+billing.getPostcode());
                    et_email.setText(billing.getEmail());
                    et_phone.setText(billing.getPhone());

                    if(shipEdCheck){
                        layout_shipping_address.setVisibility(View.VISIBLE);
                        tv_edit_shipping_addresses.setText("Cancel");
                        //get and set shipping data
                        Shipping shipping = userDataResponse.getShipping();
                        tv_shipping_u_name.setText(shipping.getFirstName()+" "+shipping.getLastName());
                        tv_shipping_full_address.setText(shipping.getAddress1()+"-"+shipping.getPostcode());
                    }else {
                        layout_shipping_address.setVisibility(View.GONE);
                        tv_edit_shipping_addresses.setText("Edit");
                    }

                    Call<List<ShippingMethodResponse>> listCall=ApiClient.getPostServices().getZoneShippingCost(Common.CONSUMER_KEY,Common.CONSUMER_SECRET);
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

                                off.setVisibility(View.VISIBLE);
                                checkout_button.setVisibility(View.VISIBLE);
                                item_progress_bar.setVisibility(View.GONE);
                            }
                        }

                        @Override
                        public void onFailure(Call<List<ShippingMethodResponse>> call, Throwable t) {

                        }
                    });


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
                    shipEdCheck = true;

                    getUserCurrentData(id);

                    pd.dismiss();
                    off.setVisibility(View.GONE);
                    checkout_button.setVisibility(View.GONE);
                    item_progress_bar.setVisibility(View.VISIBLE);

                }else{
                    pd.dismiss();
                    method.showToastMessage("Something went wrong! Please try again",3,mContext);
                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                pd.dismiss();
                method.showToastMessage("Something went wrong! Please try again",3,mContext);
            }
        });
    }

    private void showingBillingBottomDialog()
    {

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
                                    String postalcode,String country)
    {
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
                    //get s
                    getUserCurrentData(id);

                    pd.dismiss();
                    off.setVisibility(View.GONE);
                    checkout_button.setVisibility(View.GONE);
                    item_progress_bar.setVisibility(View.VISIBLE);

                }else{
                    pd.dismiss();

                }
            }

            @Override
            public void onFailure(Call<UserDataResponse> call, Throwable t) {
                pd.dismiss();
            }
        });

    }

    private void confirmOrder(String orderNote,String paymentMethod, String payvia, String number,String transId)
    {
        CheckoutRequest checkoutRequest=new CheckoutRequest();
        //get billing data
        Billing billing = userDataResponse.getBilling();
        //Get Shipping data
        Shipping shipping = userDataResponse.getShipping();
        //Create object
        Billing bill=new Billing();
        Shipping sip = new Shipping();
        List<ShippingLine> shippingLineList =new ArrayList<>();
        ShippingLine shippingLine = new ShippingLine();

        checkoutRequest.setPaymentMethod(paymentMethod);
        checkoutRequest.setPaymentMethodTitle(payvia);
        checkoutRequest.setTransaction_id(transId);
        checkoutRequest.setNumber(number);
        checkoutRequest.setCustomer_note(orderNote);
        checkoutRequest.setSetPaid(false);
        checkoutRequest.setStatus("on-hold");
        checkoutRequest.setCustomer_id(id);
        //set billing data
        bill.setFirstName(billing.getFirstName());
        bill.setLastName(billing.getLastName());
        bill.setPhone(billing.getPhone());
        bill.setEmail(billing.getEmail());
        bill.setAddress1(billing.getAddress1());
        bill.setAddress2(billing.getAddress2());
        bill.setState(billing.getState());
        bill.setCity(billing.getCity());
        bill.setPostcode(billing.getPostcode());
        bill.setCountry(billing.getCountry());

        checkoutRequest.setBilling(bill);
        //Set shipping data

        if(tv_edit_shipping_addresses.getText().toString().equals("Cancel"))
        {
            sip.setFirstName(shipping.getFirstName());
            sip.setLastName(shipping.getLastName());
            sip.setAddress1(shipping.getAddress1());
            sip.setAddress2(shipping.getAddress2());
            sip.setState(shipping.getState());
            sip.setCity(shipping.getCity());
            sip.setPostcode(shipping.getPostcode());
            sip.setCountry(shipping.getCountry());

            checkoutRequest.setShipping(sip);
        }else {
            sip.setFirstName(billing.getFirstName());
            sip.setLastName(billing.getLastName());
            sip.setAddress1(billing.getAddress1());
            sip.setAddress2(billing.getAddress2());
            sip.setState(billing.getState());
            sip.setCity(billing.getCity());
            sip.setPostcode(billing.getPostcode());
            sip.setCountry(billing.getCountry());

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

        Call<CheckoutResponse> checkoutResponseCall = ApiClient.getPostServices().createOrder(Common.CONSUMER_KEY,Common.CONSUMER_SECRET,checkoutRequest);
        checkoutResponseCall.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response) {
                if(response.isSuccessful()){
                    CheckoutResponse checkoutResponse = response.body();
                    //cart all data clear
                    Call<String> cartClearCall = ApiClient.getPostServicesCoCart().clearDataCart("Bearer "+user_token);
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
                    });

                }else {
                    method.showToastMessage("Please try again.",0,mContext);
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                method.showToastMessage("Please try again.",0,mContext);
                pd.dismiss();
            }
        });
    }
}