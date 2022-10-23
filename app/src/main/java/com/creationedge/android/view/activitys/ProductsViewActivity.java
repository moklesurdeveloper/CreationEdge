package com.creationedge.android.view.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.ItemClickListener;
import com.creationedge.android.R;
import com.creationedge.android.adapter.ProductsImageAdapter;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.db.DBHelper;
import com.creationedge.android.model.AddMeta;
import com.creationedge.android.model.CartDBModel;
import com.creationedge.android.model.CartData;
import com.creationedge.android.model.CartRequest;
import com.creationedge.android.model.ProductImageResponse;
import com.creationedge.android.model.ProductResponse;
import com.creationedge.android.model.ShareKeyResponse;
import com.creationedge.android.model.SliderImageResponse;
import com.creationedge.android.model.WishlistRequest;
import com.creationedge.android.model.WishlistResponse;
import com.creationedge.android.view.fragmentDialog.CartDialogFragment;
import com.creationedge.android.view.fragmentDialog.ImageViewFragmentDialog;
import com.creationedge.android.view.fragmentDialog.SignInFragment;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsViewActivity extends AppCompatActivity implements ItemClickListener, View.OnClickListener {
    private Method method = new Method();
    private TextView product_name, tv_stock, product_rating_count, product_price_range, product_discounted_price, tvdiscounted_off, totalsize, currentsize;
    private RelativeLayout description_layout, return_policy;
    private RatingBar product_rating;
    private ImageView heart_icon, img_backbutton;
    private TextView addToCart, buyNow;
    private ViewPager2 viewPager;
    List<ProductImageResponse> galleriesList;
    private ProductsImageAdapter adapter;
    private RelativeLayout indicatorlay, layout;
    private LinearLayout product_buttons;
    private String user_token, id, name, photo, sharedkey;
    private String inStock = "onso";
    private Integer product_id, itemid;
    private ProgressDialog pd;
    private ProgressBar item_progress_bar;
    private boolean isWishlist = false;
    ProductResponse productResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        //get current user data to sharePre
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        user_token = sharedPreferences.getString("user_token", null);
        id = sharedPreferences.getString("id", null);
        name = sharedPreferences.getString("name", null);
        photo = sharedPreferences.getString("photo", null);

        // getting wishlist shared key if not saved
        SharedPreferences sp = getSharedPreferences("wishListData", Context.MODE_PRIVATE);
        sharedkey = sp.getString("key", null);
        if (TextUtils.isEmpty(sharedkey)) {
            getShareKey(id);
        }




        //progress dialog init
        pd = new ProgressDialog(this);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        //getIint
        init();
        adapter = new ProductsImageAdapter(ProductsViewActivity.this);
        adapter.setClickListener(this);
        //getProductDetails

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {

                super.onPageSelected(position);
                setupCurrentIndicator();

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("userData", Context.MODE_PRIVATE);
        user_token = sharedPreferences.getString("user_token", null);
        id = sharedPreferences.getString("id", null);
        name = sharedPreferences.getString("name", null);
        photo = sharedPreferences.getString("photo", null);
    }

    private void getShareKey(String id) {
        Call<List<ShareKeyResponse>> call = ApiClient.getPostServices().getShareKey(id, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        call.enqueue(new Callback<List<ShareKeyResponse>>() {
            @Override
            public void onResponse(Call<List<ShareKeyResponse>> call, Response<List<ShareKeyResponse>> response) {
                if (response.isSuccessful()) {
                    List<ShareKeyResponse> shareKeyResponselsit = response.body();
                    ShareKeyResponse shareKeyResponse = shareKeyResponselsit.get(0);

                    // save shared key in sharedPreferences
                    SharedPreferences preferences = getSharedPreferences("wishListData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("key", shareKeyResponse.getShareKey());
                    editor.apply();

                }
            }

            @Override
            public void onFailure(Call<List<ShareKeyResponse>> call, Throwable t) {
            }
        });
    }


    public void getProductDetails() {
        Call<ProductResponse> singleProductCall = ApiClient.getPostServices().getSingleProduct(Common.PRODUCT_id, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        singleProductCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    productResponse = response.body();

                    product_id = productResponse.getId();

                    galleriesList = productResponse.getImages();
                    adapter.setData(galleriesList, viewPager);
                    viewPager.setAdapter(adapter);
                    viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {

                            super.onPageSelected(position);
                            // setupCurrentIndicator(position);
                        }
                    });

                    setupIndicator();
                    setupCurrentIndicator();

                    product_name.setText(productResponse.getName());
                    product_price_range.setText("৳" + productResponse.getPrice());
                    product_rating_count.setText(productResponse.getAverageRating());
                    product_rating.setRating(Float.parseFloat(productResponse.getAverageRating()));

                    //set global string value stock count
                    inStock = productResponse.getStockStatus();

                    if (productResponse.getStockStatus().equals("instock")) {
                        tv_stock.setText("In Stock");
                        tv_stock.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_stock_blue, 0, 0, 0);
                        tv_stock.setTextColor(ContextCompat.getColor(ProductsViewActivity.this, R.color.blue));
                    } else {
                        tv_stock.setText("Out Of Stock");
                        tv_stock.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_close_circle, 0, 0, 0);
                        tv_stock.setTextColor(ContextCompat.getColor(ProductsViewActivity.this, R.color.red));
                    }
                    int price = Integer.parseInt(productResponse.getPrice());
                    int rprice = Integer.parseInt(productResponse.getRegularPrice());

                    if (price == rprice) {
                        product_discounted_price.setVisibility(View.GONE);
                        tvdiscounted_off.setVisibility(View.GONE);
                    } else {
                        product_discounted_price.setText("৳" + productResponse.getRegularPrice());
                        product_discounted_price.setPaintFlags(product_discounted_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                        method.productOffPersent(productResponse.getPrice(), productResponse.getRegularPrice(), tvdiscounted_off);
                    }


                    Call<SliderImageResponse> returnCall = ApiClient.getPostServicesV2().getReturnPolicy();
                    returnCall.enqueue(new Callback<SliderImageResponse>() {
                        @Override
                        public void onResponse(Call<SliderImageResponse> call, Response<SliderImageResponse> response) {
                            if (response.isSuccessful()) {
                                SliderImageResponse response1 = response.body();
                                return_policy.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        showDescriptionPopupDialog(response1.getContent().getRendered(), getString(R.string.return_policy));
                                    }
                                });

                            }
                        }

                        @Override
                        public void onFailure(Call<SliderImageResponse> call, Throwable t) {

                        }
                    });

                    description_layout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (productResponse.getDescription() == null) {

                            } else {
                                showDescriptionPopupDialog(productResponse.getDescription(), getString(R.string.description));
                            }

                        }
                    });

                    //Vissible
                    layout.setVisibility(View.VISIBLE);
                    product_buttons.setVisibility(View.VISIBLE);
                    item_progress_bar.setVisibility(View.GONE);

                    //show wishlist image
                    showheartbtnactive(sharedkey);

                } else {
                    Toast.makeText(ProductsViewActivity.this, "errorCode" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(ProductsViewActivity.this, "errorFaild" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void setupIndicator() {
        if (!(adapter.getItemCount() == 1)) {
            indicatorlay.setVisibility(View.VISIBLE);
            totalsize.setText("/ " + String.valueOf(adapter.getItemCount()));
        }
    }

    private void setupCurrentIndicator() {
        currentsize.setText(String.valueOf(viewPager.getCurrentItem() + 1) + " ");
    }

    public void init() {
        layout = findViewById(R.id.layout);
        product_buttons = findViewById(R.id.product_buttons);
        item_progress_bar = findViewById(R.id.item_progress_bar);
        viewPager = findViewById(R.id.viewPager);
        totalsize = findViewById(R.id.txt_totalsize);
        currentsize = findViewById(R.id.txt_currentsize);
        indicatorlay = findViewById(R.id.indecatorlayout);
        product_name = findViewById(R.id.product_name);
        tv_stock = findViewById(R.id.tv_stock);
        product_rating = findViewById(R.id.product_rating);
        product_rating_count = findViewById(R.id.product_rating_count);
        product_price_range = findViewById(R.id.product_price_range);
        product_discounted_price = findViewById(R.id.product_discounted_price);
        tvdiscounted_off = findViewById(R.id.tvdiscounted_off);
        heart_icon = findViewById(R.id.heart_icon);
        description_layout = findViewById(R.id.description_layout);
        return_policy = findViewById(R.id.return_policy);
        img_backbutton = findViewById(R.id.img_backbutton);
        addToCart = findViewById(R.id.addToCart);
        buyNow = findViewById(R.id.buyNow);

        getProductDetails();

        img_backbutton.setOnClickListener(this);
        addToCart.setOnClickListener(this);
        buyNow.setOnClickListener(this);
        heart_icon.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_backbutton:
                finish();
                break;
            case R.id.addToCart:

                addCartOffline();
                //   addToCurrentUser();
                /*                if (TextUtils.isEmpty(id)) {

                 *//*                    method.showToastMessage("Please LogIn First!", 0, ProductsViewActivity.this);
                    startActivity(new Intent(ProductsViewActivity.this, LoginActivity.class));*//*
                } else {
                    CartRequest cartRequest = new CartRequest();
                    cartRequest.setProduct_id(String.valueOf(Common.PRODUCT_id));
                    cartRequest.setQuantity("1");
                    pd.setMessage("Adding item to your shopping cart. Please wait...");
                    if (inStock.equals("instock")) {
                        pd.show();
                        addToCurrentUser(cartRequest, true);
                    } else {
                        method.showToastMessage("Out Of Stock", 0, ProductsViewActivity.this);
                    }

                }*/
                break;
            case R.id.buyNow:
                //


                CartRequest cartRequest = new CartRequest();
                cartRequest.setProduct_id(String.valueOf(Common.PRODUCT_id));
                cartRequest.setQuantity("1");
                pd.setMessage("Adding item to your shopping cart. Please wait...");
                if (inStock.equals("instock")) {
                    pd.show();
                    addToCurrentUser();
                } else {
                    method.showToastMessage("Out Of Stock", 0, ProductsViewActivity.this);
                }
/*                if (TextUtils.isEmpty(id)) {
                    method.showToastMessage("Please LogIn First!", 0, ProductsViewActivity.this);
                    startActivity(new Intent(ProductsViewActivity.this, LoginActivity.class));
                } else {
                    CartRequest cartRequest = new CartRequest();
                    cartRequest.setProduct_id(String.valueOf(Common.PRODUCT_id));
                    cartRequest.setQuantity("1");
                    pd.setMessage("Adding item to your shopping cart. Please wait...");
                    if (inStock.equals("instock")) {
                        pd.show();
                        addToCurrentUser(cartRequest, false);
                    } else {
                        method.showToastMessage("Out Of Stock", 0, ProductsViewActivity.this);
                    }

                }*/
                break;

            case R.id.heart_icon:
                if (TextUtils.isEmpty(id)) {
                    method.showToastMessage("Please LogIn First!", 0, ProductsViewActivity.this);
                    startActivity(new Intent(ProductsViewActivity.this, LoginActivity.class));
                } else {

                    if (heart_icon.getDrawable().getConstantState().equals(//it is not a favorite
                            ContextCompat.getDrawable(v.getContext(),
                                    R.drawable.ic_heart).getConstantState())) {
                        //
                        pd.setMessage("Adding item to your WishList. Please wait...");
                        pd.show();
                        addWishlist(product_id);
                        heart_icon.setImageDrawable(getDrawable(R.drawable.ic_heart_filled));
                    } else {
                        pd.setMessage("Remove item to your WishList. Please wait...");
                        pd.show();
                        removerequest(itemid);
                        heart_icon.setImageDrawable(getDrawable(R.drawable.ic_heart));
                    }


                }
                break;
        }

    }

    private void addCartOffline() {
        DBHelper dbHelper = new DBHelper(this);
        CartDBModel cart = new CartDBModel();
        cart.setProductId(productResponse.getId());
        cart.setProductName(productResponse.getName());
        cart.setProduct_image(productResponse.getImages().get(0).getSrc());
        cart.setLineTotal(Integer.parseInt(productResponse.getPrice()));
        cart.setSinglePrice(Integer.parseInt(productResponse.getPrice()));
        cart.setQuantity(1);
        dbHelper.addContact(cart);
        method.showToastMessage("added", 1, this);

    }

    private void addWishlist(Integer product_id) {
        WishlistRequest wishlistRequest = new WishlistRequest();
        wishlistRequest.setProductId(product_id);
        wishlistRequest.setVariationId(0);
        AddMeta meta = new AddMeta();
        meta.setTest("text");
        wishlistRequest.setMeta(meta);

        Call<List<WishlistResponse>> call = ApiClient.getPostServices().addWishlist(sharedkey, wishlistRequest, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        call.enqueue(new Callback<List<WishlistResponse>>() {
            @Override
            public void onResponse(Call<List<WishlistResponse>> call, Response<List<WishlistResponse>> response) {
                if (response.isSuccessful()) {
                    isWishlist = true;
                    heart_icon.setImageDrawable(getDrawable(R.drawable.ic_heart_filled));
                    method.showToastMessage("Added WishList", 1, ProductsViewActivity.this);
                    pd.dismiss();
                } else {
                    pd.dismiss();
                    //  method.showToastMessage("Something went wrong! please try again", 2, ProductsViewActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<WishlistResponse>> call, Throwable t) {
                pd.dismiss();
                //  method.showToastMessage("Failed! please try again later", 3, ProductsViewActivity.this);
            }
        });


    }

    private void addToCurrentUser() {
        DBHelper dbHelper = new DBHelper(this);
        CartDBModel cart = new CartDBModel();
        cart.setProductId(productResponse.getId());
        cart.setProductName(productResponse.getName());
        cart.setProduct_image(productResponse.getImages().get(0).getSrc());
        cart.setLineTotal(Integer.parseInt(productResponse.getPrice()));
        cart.setSinglePrice(Integer.parseInt(productResponse.getPrice()));
        cart.setQuantity(1);
        dbHelper.addContact(cart);

        //by now button click
        //open Cart Dialog page
        pd.dismiss();
        CartDialogFragment cartDialogFragment = new CartDialogFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        cartDialogFragment.show(ft, "Tag");


/*        Call<CartData> addToCartCall = ApiClient.getPostServicesCoCart().addToCart("Bearer " + user_token, cartRequest);
        addToCartCall.enqueue(new Callback<CartData>() {
            @Override
            public void onResponse(Call<CartData> call, Response<CartData> response) {
                if (response.isSuccessful()) {
                    pd.dismiss();
                    method.showToastMessage("Successfully added.", 1, ProductsViewActivity.this);
                    if (toCheck) {
                        //cart to add button click
                    } else {
                        //by now button click
                        //open Cart Dialog page
                        CartDialogFragment cartDialogFragment = new CartDialogFragment();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        cartDialogFragment.show(ft, "Tag");
                    }
                } else {
                    pd.dismiss();
                    method.showToastMessage("Something wrong! Please try again", 3, ProductsViewActivity.this);
                }
            }

            @Override
            public void onFailure(Call<CartData> call, Throwable t) {
                pd.dismiss();
                method.showToastMessage(t.getMessage(), 3, ProductsViewActivity.this);
            }
        });*/
    }

    private void showDescriptionPopupDialog(String description, String stitle) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ProductsViewActivity.this, R.style.BottomSheetDialogTheme);

        bottomSheetDialog.setContentView(R.layout.description_bottom_dialog);
        bottomSheetDialog.setCancelable(true);
        HtmlTextView textView = bottomSheetDialog.findViewById(R.id.policy);
        TextView title = bottomSheetDialog.findViewById(R.id.title);
        ImageView close = bottomSheetDialog.findViewById(R.id.img_close);

        title.setText(stitle);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            textView.setClickable(true);
           // String linkTxt=getResources().getString(R.string.link);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(Html.fromHtml(description, Html.FROM_HTML_MODE_COMPACT));
          //  textView.setText(Html.fromHtml( linkTxt));
        } else {
            textView.setClickable(true);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setText(Html.fromHtml(description));

        }

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.dismiss();
            }
        });


        bottomSheetDialog.show();
    }

    @Override
    public void onItemClick(View view, int position) {
        ProductImageResponse galleries = galleriesList.get(position);
        // Galleries galleries=ga
        Common.IMAGE_LINK = galleries.getSrc();
        ImageViewFragmentDialog dialog = new ImageViewFragmentDialog();
        FragmentTransaction ftp = getSupportFragmentManager().beginTransaction();
        dialog.show(ftp, ImageViewFragmentDialog.TAG);
    }

    private void showheartbtnactive(String sharedkey) {

        Call<List<WishlistResponse>> call = ApiClient.getPostServices().showWishlist(sharedkey, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        call.enqueue(new Callback<List<WishlistResponse>>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(Call<List<WishlistResponse>> call, Response<List<WishlistResponse>> response) {
                if (response.isSuccessful()) {
                    List<WishlistResponse> wishlistResponsesList = response.body();

                    for (int i = 0; i < wishlistResponsesList.size(); i++) {

                        if (wishlistResponsesList.get(i).getProductId().equals(product_id)) {
                            heart_icon.setImageDrawable(getDrawable(R.drawable.ic_heart_filled));
                            itemid = wishlistResponsesList.get(i).getItemId();
                            break;
                        }

                    }
                    //Vissible
                    layout.setVisibility(View.VISIBLE);
                    product_buttons.setVisibility(View.VISIBLE);
                    item_progress_bar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<WishlistResponse>> call, Throwable t) {

            }
        });

    }

    private void removerequest(Integer itemId) {
        Call<String> call = ApiClient.getPostServices().removewishlist(itemId, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    heart_icon.setImageDrawable(getDrawable(R.drawable.ic_heart));
                    pd.dismiss();
                } else {
                    pd.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                pd.dismiss();

            }
        });

    }
}