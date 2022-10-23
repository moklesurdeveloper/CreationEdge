package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.ItemClickListener;
import com.creationedge.android.R;
import com.creationedge.android.adapter.CartViewAdapter;
import com.creationedge.android.adapter.OfflineCartViewAdapter;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.db.DBHelper;
import com.creationedge.android.model.CartDBModel;
import com.creationedge.android.model.CartData;
import com.creationedge.android.model.CartKeyResponse;
import com.creationedge.android.model.CartRequest;
import com.creationedge.android.view.activitys.ProductsViewActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class CartDialogFragment extends DialogFragment implements ItemClickListener {
    DBHelper dbHelper;
    private String user_token, id, name, photo;
    private RecyclerView cartRecycler;
    private List<CartDBModel> cartDBModelList;
    private LinearLayout checkout_button;
    private OfflineCartViewAdapter adapter;
    private TextView total_amount, cart_empty_text;
    private TextView checkout;
    private Method method = new Method();
    private ProgressDialog pd;
    private int total_price = 0;
    private Context mContext;
    private ProgressBar item_progress_bar;
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
        view=inflater.inflate(R.layout.fragment_cart,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialization all views
        //get current user data to sharePre
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        user_token = sharedPreferences.getString("user_token",null);
        id = sharedPreferences.getString("id",null);

        // progress dialog init
        pd = new ProgressDialog(mContext);
        pd.setMessage("Loading....");
        pd.setCancelable(false);

        //init all view
        init(view);


        // cartResponseList = new ArrayList<>();
        adapter = new OfflineCartViewAdapter(mContext);
        adapter.setClickListener(this);
        //creating a string request to send request to the url
        // getCartProduct();
        getCartListI();

        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.TOTAL_PRICE = total_price;
                Common.cartDBModelList = dbHelper.getAllContacts();
                OfflineCheckoutFragment checkoutFragment = new OfflineCheckoutFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                checkoutFragment.show(ft, "Tag");
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

    private void init(View view) {
        cartRecycler = view.findViewById(R.id.recycler_art_list);
        cartRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        checkout_button = view.findViewById(R.id.checkout_button);
        total_amount = view.findViewById(R.id.total_amount);
        checkout = view.findViewById(R.id.checkout);
        item_progress_bar = view.findViewById(R.id.item_progress_bar);
        cart_empty_text = view.findViewById(R.id.cart_empty_text);
    }

    @Override
    public void onItemClick(View view, int position) {
        CartDBModel cartResponse = cartDBModelList.get(position);

        if (view.getId() == R.id.cart_item) {
            Common.PRODUCT_id = cartResponse.getProductId();
            mContext.startActivity(new Intent(mContext, ProductsViewActivity.class));
        }

        if (view.getId() == R.id.img_delete) {
            pd.show();
            // removeItem(cartResponse.getKey());

            List<CartDBModel> cartList = dbHelper.getAllContacts();
            CartDBModel cartDBModel = new CartDBModel();
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getProductId() == cartResponse.getProductId()) {
                    cartDBModel = cartDBModelList.get(i);
                }
            }

            dbHelper.deleteContact(cartDBModel);
/*            cartDBModelList.remove(position);
            adapter.notifyItemRemoved(position);
            adapter.notifyDataSetChanged();*/
            getCartListI();

            pd.dismiss();
            method.showToastMessage("Delete",1,mContext);
            //  removeCartItem(cartResponse.get_id());

        }
        if (view.getId() == R.id.increment) {
            pd.show();

            List<CartDBModel> cartList = dbHelper.getAllContacts();
            CartDBModel cartDBModel = new CartDBModel();
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getProductId() == cartResponse.getProductId()) {
                    cartDBModel = cartDBModelList.get(i);
                }
            }

            int total=cartDBModel.getLineTotal()+cartDBModel.getSinglePrice();
            CartDBModel c=new CartDBModel();
            c.set_id(cartDBModel.get_id());
            c.setProductId(cartDBModel.getProductId());
            c.setProduct_image(cartDBModel.getProduct_image());
            c.setProductName(cartDBModel.getProductName());
            c.setLineTotal(total);
            c.setSinglePrice(cartDBModel.getSinglePrice());
            c.setQuantity(cartDBModel.getQuantity()+1);

            dbHelper.updateContact(c);
            getCartListI();
            pd.dismiss();

/*
            //increment cart this item product
            int increment = cartResponse.getQuantity() + 1;
            CartRequest cartRequest = new CartRequest();
            // cartRequest.setCart_item_key(cartResponse.getKey());
            cartRequest.setQuantity(String.valueOf(increment));
            // inCreDecreQty(cartRequest,true);*/
        }
        if (view.getId() == R.id.decrement) {
            pd.show();
            if (cartResponse.getQuantity()>1){
                List<CartDBModel> cartList = dbHelper.getAllContacts();
                CartDBModel cartDBModel = new CartDBModel();
                for (int i = 0; i < cartList.size(); i++) {
                    if (cartList.get(i).getProductId() == cartResponse.getProductId()) {
                        cartDBModel = cartDBModelList.get(i);
                    }
                }

                CartDBModel c=new CartDBModel();
                int total=cartDBModel.getLineTotal()-cartDBModel.getSinglePrice();

                c.set_id(cartDBModel.get_id());
                c.setProductId(cartDBModel.getProductId());
                c.setProduct_image(cartDBModel.getProduct_image());
                c.setProductName(cartDBModel.getProductName());
                c.setLineTotal(total);
                c.setSinglePrice(cartDBModel.getSinglePrice());
                c.setQuantity(cartDBModel.getQuantity()-1);
                dbHelper.updateContact(c);
                getCartListI();
                pd.dismiss();
            }



/*            //increment cart this item product
            int increment = cartResponse.getQuantity() - 1;
            CartRequest cartRequest = new CartRequest();
            //  cartRequest.setCart_item_key(cartResponse.getKey());
            cartRequest.setQuantity(String.valueOf(increment));
            // inCreDecreQty(cartRequest,false);*/

        }
    }
    public void getCartListI() {
        dbHelper = new DBHelper(mContext);
        cartDBModelList = dbHelper.getAllContacts();
        int totalAmountCount = 0;
        for (int i = 0; i<cartDBModelList.size(); i++)
        {
            int subItemTotal = cartDBModelList.get(i).getLineTotal();
            totalAmountCount += subItemTotal;
            total_amount.setText("৳"+String.valueOf(totalAmountCount));
            total_price = totalAmountCount;
        }

        if (cartDBModelList.size()==0){
            item_progress_bar.setVisibility(View.GONE);
            cart_empty_text.setVisibility(View.VISIBLE);

        }else{
            adapter.notifyDataSetChanged();
            adapter.setData(cartDBModelList);
            cartRecycler.setAdapter(adapter);
            item_progress_bar.setVisibility(View.GONE);
            cartRecycler.setVisibility(View.VISIBLE);
            checkout_button.setVisibility(View.VISIBLE);
        }

    }
}
