package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.adapter.OrderedProductAdapter;
import com.creationedge.android.common.Common;
import com.creationedge.android.data.CartResponse;
import com.creationedge.android.model.Billing;
import com.creationedge.android.model.CartData;
import com.creationedge.android.model.CheckoutResponse;
import com.creationedge.android.model.Shipping;
import com.creationedge.android.model.ShippingLine;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseHistoryDetails extends DialogFragment implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView order_code, order_date, payment_method,
            payment_status,sub_total, tax, shipping_cost, coupon_discount
            ,grand_total, shipping_name, shipping_address,billing_name,billing_phone
            ,billing_address;
    private ProgressBar item_progress_bar;
    private RelativeLayout layout;
    //recyclerView
    private RecyclerView recyclerView;
    private OrderedProductAdapter orderedProductAdapter;

    private String user_token,id,name,photo;
    private Context mContext;
    private View view;

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
        view=inflater.inflate(R.layout.purshes_history_details,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get current user data to sharePre
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        user_token = sharedPreferences.getString("user_token",null);
        id = sharedPreferences.getString("id",null);
        name=sharedPreferences.getString("name",null);
        photo=sharedPreferences.getString("photo",null);

        // initialization all views
        initAll(view);

        //set up recyecler view
        recyclerView=view.findViewById(R.id.order_details_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        orderedProductAdapter =new OrderedProductAdapter(mContext);

        //get current user Single order
        getSingleOrder();

    }


    private void initAll(View view) {
        item_progress_bar = view.findViewById(R.id.item_progress_bar);
        layout = view.findViewById(R.id.layout);

        order_code = view.findViewById(R.id.order_code);

        order_date = view.findViewById(R.id.order_date);
        payment_method = view.findViewById(R.id.payment_method);
        payment_status = view.findViewById(R.id.payment_status);
        sub_total = view.findViewById(R.id.sub_total);
        tax = view.findViewById(R.id.tax);
        shipping_cost = view.findViewById(R.id.shipping_cost);
        coupon_discount = view.findViewById(R.id.coupon_discount);
        grand_total = view.findViewById(R.id.grand_total);
        shipping_name = view.findViewById(R.id.shipping_name);
        shipping_address = view.findViewById(R.id.shipping_address);
        billing_name = view.findViewById(R.id.billing_name);
        billing_phone = view.findViewById(R.id.billing_phone);
        billing_address = view.findViewById(R.id.billing_address);

        //set toolbar and back
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(getString(R.string.purchase_history));
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

    }

    private void getSingleOrder() {
        Call<CheckoutResponse> singleOrderCall = ApiClient.getPostServices().getCustomerSignalOrder(Common.ORDER_ID, Common.CONSUMER_KEY,Common.CONSUMER_SECRET);
        singleOrderCall.enqueue(new Callback<CheckoutResponse>() {
            @Override
            public void onResponse(Call<CheckoutResponse> call, Response<CheckoutResponse> response)
            {
                if(response.isSuccessful()){
                    CheckoutResponse result = response.body();
                    /**
                     * order_code, shipping_method, order_date, payment_method,
                     *             payment_status, total_amount, sub_total, tax, shipping_cost, coupon_discount
                     *             ,grand_total;
                     */
                    order_code.setText("Order #"+String.valueOf(result.getId()));
                    //get shipping line data
                    List<ShippingLine> shippingList =  result.getShippingLines();
                    //
                    //order_date.setText(result.getPaymentMethodTitle());

                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                    // replace with your start date string
                    Date d = null;
                    try {
                        d = df.parse(result.getDateCreated().replace("T", " "));
                        df.setTimeZone(TimeZone.getDefault());

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    long serverMilsTime = d.getTime();
                    String dateString = DateFormat.format("LLLL dd, yyyy", new Date(serverMilsTime)).toString();

                    List<CartResponse> recentOrderList = result.getLineItems();
                    orderedProductAdapter.setData(recentOrderList);
                    recyclerView.setAdapter(orderedProductAdapter);

                    order_date.setText("Placed on "+dateString);//end date format
                    payment_status.setText(result.getStatus());
                    payment_method.setText(result.getPaymentMethod());

                    //Shipping and billing
                    Shipping shipping = result.getShipping();
                    Billing billing = result.getBilling();
                    shipping_name.setText(shipping.getLastName() +" "+ shipping.getLastName());
                    shipping_address.setText(shipping.getAddress1() +"-"+ shipping.getCity());

                    billing_name.setText(billing.getLastName() +" "+ billing.getLastName());
                    billing_phone.setText(billing.getPhone());
                    billing_address.setText(billing.getAddress1() +"-"+ billing.getCity());

                    tax.setText("৳"+result.getTotalTax());
                    shipping_cost.setText("৳"+shippingList.get(0).getTotal());
                    coupon_discount.setText("৳"+result.getTotalTax());
                    grand_total.setText("৳"+result.getTotal());

                    double totalCostEQ = ((Double.parseDouble(result.getTotalTax())) + (Double.parseDouble(shippingList.get(0).getTotal())) +
                            (Double.parseDouble(result.getDiscountTax())));

                    double totalCost = Double.parseDouble(result.getTotal());
                    double subCost = totalCost - totalCostEQ;
                    sub_total.setText("৳"+String.valueOf(subCost));
                    //

                    //set and vissible
                    item_progress_bar.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);

                }else {
                    Toast.makeText(mContext, ""+response.code(), Toast.LENGTH_SHORT).show();
                    item_progress_bar.setVisibility(View.GONE);
                }

            }

            @Override
            public void onFailure(Call<CheckoutResponse> call, Throwable t) {
                item_progress_bar.setVisibility(View.GONE);

            }
        });
    }
}
