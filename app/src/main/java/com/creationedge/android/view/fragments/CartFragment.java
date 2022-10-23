package com.creationedge.android.view.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creationedge.android.ItemClickListener;
import com.creationedge.android.R;
import com.creationedge.android.adapter.OfflineCartViewAdapter;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.db.DBHelper;
import com.creationedge.android.model.CartDBModel;
import com.creationedge.android.model.CartRequest;
import com.creationedge.android.view.activitys.ProductsViewActivity;
import com.creationedge.android.view.fragmentDialog.CheckoutFragment;
import com.creationedge.android.view.fragmentDialog.OfflineCheckoutFragment;

import java.util.List;


public class CartFragment extends Fragment implements ItemClickListener {
    String JsonURL = "https://creationedge.com.bd/wp-json/cocart/v1/get-cart/?thumb=true";
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_cart, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get current user data to sharePre
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        user_token = sharedPreferences.getString("user_token", null);
        id = sharedPreferences.getString("id", null);
        name = sharedPreferences.getString("name", null);
        photo = sharedPreferences.getString("photo", null);

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
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        user_token = sharedPreferences.getString("user_token", null);
        id = sharedPreferences.getString("id", null);
        name = sharedPreferences.getString("name", null);
        photo = sharedPreferences.getString("photo", null);
/*        if (TextUtils.isEmpty(id)){
            Toast.makeText(mContext, "Please LogIn First!", Toast.LENGTH_SHORT).show();
            Common.logingCheck = true;
            startActivity(new Intent(mContext, LoginActivity.class));
        }*/
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

    /*private void getCartProduct() {
        cartResponseList.clear();
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest myReq = new StringRequest(Request.Method.GET, JsonURL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if(response.length() == 2){
                            checkout_button.setVisibility(View.GONE);
                            cartRecycler.setVisibility(View.GONE);
                            item_progress_bar.setVisibility(View.GONE);
                            cart_empty_text.setVisibility(View.VISIBLE);
                        }else {
                            try {
                                //getting the whole json object from the response
                                JSONObject objects = new JSONObject (response);
                                JSONArray key = objects.names ();
                                for (int i = 0; i < key.length (); ++i) {
                                    String keys = key.getString (i);
                                    String value = objects.getString (keys);
                                    //Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
                                    CartKeyResponse hero = new CartKeyResponse();
                                    hero.setKey(keys);


                                    try {
                                        JSONObject obj = new JSONObject(value);
                                        Log.e("My App", obj.getString("key"));

                                        CartData cartData = new CartData();
                                        cartData.setKey(obj.getString("key"));
                                        cartData.setProductId(obj.getInt("product_id"));
                                        cartData.setQuantity(obj.getInt("quantity"));
                                        cartData.setLineTotal(obj.getInt("line_total"));
                                        cartData.setProductName(obj.getString("product_name"));
                                        cartData.setProduct_image(obj.getString("product_image"));
                                        cartResponseList.add(cartData);

                                        // Toast.makeText(mContext, ""+obj.getString("product_image"), Toast.LENGTH_SHORT).show();
                                    } catch (Throwable t) {
                                        Log.e("My App", "Could not parse malformed JSON: \"" + value + "\"");
                                    }

                                }
                                adapter.setData(cartResponseList);
                                cartRecycler.setLayoutManager(new LinearLayoutManager(mContext));
                                cartRecycler.setAdapter(adapter);

                                int totalAmountCount = 0;
                                for (int i = 0; i<cartResponseList.size(); i++)
                                {
                                    int subItemTotal = cartResponseList.get(i).getLineTotal();
                                    totalAmountCount += subItemTotal;
                                    total_amount.setText("৳"+String.valueOf(totalAmountCount));
                                    total_price = totalAmountCount;
                                }
                                checkout_button.setVisibility(View.VISIBLE);
                                cartRecycler.setVisibility(View.VISIBLE);
                                item_progress_bar.setVisibility(View.GONE);


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                },
                new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        checkout_button.setVisibility(View.GONE);
                        cartRecycler.setVisibility(View.GONE);
                        item_progress_bar.setVisibility(View.GONE);
                        cart_empty_text.setVisibility(View.VISIBLE);
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Bearer "+user_token);

                return params;
            }

        };



        myReq.setRetryPolicy(new DefaultRetryPolicy(15000,
                1,
                1));
        queue.add(myReq);
    }*/

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

   /* private void inCreDecreQty(CartRequest cartRequest,boolean checkin) {
        Call<CartData> addToCartCall = ApiClient.getPostServicesCoCart().cartIncrementAndDecrement("Bearer "+user_token,cartRequest);
        addToCartCall.enqueue(new Callback<CartData>() {
            @Override
            public void onResponse(Call<CartData> call, retrofit2.Response<CartData> response) {
                if(response.isSuccessful()){

                    //get cart data
                    getCartProduct();
                    pd.dismiss();

                    if(checkin){
                        method.showToastMessage("Successfully added !", 1, mContext);
                    }else {
                        method.showToastMessage("Successfully reduce !", 1, mContext);
                    }

                }else {
                    pd.dismiss();
                    switch (response.code()){
                        case 403:
                            method.showToastMessage("Out Of Stock this product",3,mContext);
                            break;
                        default:
                            method.showToastMessage("Something wrong! Please try again",3,mContext);
                    }


                }
            }

            @Override
            public void onFailure(Call<CartData> call, Throwable t) {
                pd.dismiss();
                method.showToastMessage(t.getMessage(),3,mContext);
            }
        });
    }*/

    /*private void removeItem(String key) {
        Call<String> addToCartCall = ApiClient.getPostServicesCoCart().removeCartItem("Bearer "+user_token,key);
        addToCartCall.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if(response.isSuccessful()){

                    //get cart data
                    checkout_button.setVisibility(View.GONE);
                    cartRecycler.setVisibility(View.GONE);
                    item_progress_bar.setVisibility(View.VISIBLE);
                    getCartProduct();
                    pd.dismiss();
                    method.showToastMessage(response.body(), 1, mContext);

                }else {
                    pd.dismiss();
                    method.showToastMessage("Something wrong! Please try again",3,mContext);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pd.dismiss();
                method.showToastMessage(t.getMessage(),3,mContext);
            }
        });
    }*/

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