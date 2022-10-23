package com.creationedge.android.Api;

import com.creationedge.android.model.CartRequest;
import com.creationedge.android.model.CartData;
import com.creationedge.android.model.CategoryResponse;
import com.creationedge.android.model.CheckoutRequest;
import com.creationedge.android.model.CheckoutResponse;
import com.creationedge.android.model.CodeResponse;
import com.creationedge.android.model.LogInResponse;
import com.creationedge.android.model.LoginRequest;
import com.creationedge.android.model.ProductResponse;
import com.creationedge.android.model.RegisterRequest;
import com.creationedge.android.model.ShareKeyResponse;
import com.creationedge.android.model.ShippingMethodResponse;
import com.creationedge.android.model.Slider;
import com.creationedge.android.model.SliderImageResponse;
import com.creationedge.android.model.TimmerResponse;
import com.creationedge.android.model.TodaySaleResponse;
import com.creationedge.android.model.UserDataResponse;
import com.creationedge.android.model.UserResponse;
import com.creationedge.android.model.WishlistRequest;
import com.creationedge.android.model.WishlistResponse;
import com.facebook.AccessToken;
import com.facebook.login.LoginResult;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterfaceServices {
    //Get featured product
    @GET("products/?")
    Call<List<ProductResponse>> getFeaturedProduct(
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret,
            @Query("featured") boolean featured
    );

    //Get Flash sale product list
    @GET("products/?")
    Call<List<ProductResponse>> getOnSaleProduct(
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret,
            @Query("on_sale") boolean on_sale


            );

    //Get product Category
    @GET("products/categories/?")
    Call<List<CategoryResponse>> getFeaturedTopCategory(
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret
    );

    //Get Category Base product
    @GET("products/?")
    Call<List<ProductResponse>> getCategoriesProductList(
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret,
            @Query("category") int category,
            @Query("page") int page
    );
    //Get Best selling product list
    @GET("products/?")
    Call<List<ProductResponse>> getBestSellingProductList(
            @Query("orderby") String popularity,
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret,
            @Query("page") int page
    );
    //
    //Get Single product details
    @GET("products/{id}?")
    Call<ProductResponse> getSingleProduct(
            @Path("id") int id,
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret);

    @POST("token")
    Call<LogInResponse> login(
            @Body LoginRequest loginRequest
    );

    @POST("facebook/get_user")
    Call<String> socialLogin(
      @Query("access_token") AccessToken loginResult
    );


    @GET("customers/{id}/?")
    Call<UserDataResponse> getUserData(
            @Path("id") String id,
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret
    );

    @PUT("customers/{id}/?")
    Call<UserDataResponse> editProfile(
            @Path("id") String id,
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret,
            @Body UserDataResponse userDataResponse
    );


    // change password with current password
    @PUT("customers/{id}/?")
    Call<UserDataResponse> editepassword(
            @Path("id") String id,
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret,
            @Query("password") String password
    );

    // send code for forgot password
    @POST("reset-password")
    Call<CodeResponse> sendCode(
            @Query("email") String email
    );

    // set new password
    @POST("set-password")
    Call<CodeResponse> setNewPassword(
            @Query("email") String email,
            @Query("password") String password,
            @Query("code") String code
    );


    //User Register
    @POST("customers/?")
    Call<UserResponse> register(
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret, @Body RegisterRequest registerRequest);

    //Get today sales
    @GET("todaysales/?")
    Call<List<TodaySaleResponse>> getTodaySale(
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret);

    //Add to Cart
    @POST("add-item")
    Call<CartData> addToCart(@Header("Authorization") String token, @Body CartRequest cartRequest);

    //cart Increment And Decrement
    @POST("item")
    Call<CartData> cartIncrementAndDecrement(
            @Header("Authorization") String token,
            @Body CartRequest cartRequest);//

    //Remove cart item
    @HTTP(method = "DELETE", path = "item/?", hasBody = true)
    Call<String> removeCartItem(
            @Header("Authorization") String token,
            @Query("cart_item_key") String cart_item_key);

    //Clear cart all data
    @POST("clear")
    Call<String> clearDataCart(
            @Header("Authorization") String token);

    //get wishlist share key
    @GET("wishlist/get_by_user/{user_id}")
    Call<List<ShareKeyResponse>> getShareKey(
            @Path("user_id") String user_id,
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret

    );

    //add product to wishlist
    @POST("wishlist/{key}/add_product")
    Call<List<WishlistResponse>> addWishlist(
            @Path("key") String key,
            @Body WishlistRequest wishlistRequest,
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret
    );

    // show wishlist Data
    @GET("wishlist/{key}/get_products")
    Call<List<WishlistResponse>> showWishlist(
            @Path("key") String key,
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret
    );

    // remove wishlist product
    @GET("wishlist/remove_product/{item_id}")
    Call<String> removewishlist(
            @Path("item_id") Integer itemid,
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret
    );


    //Create Order
    @POST("orders/?")
    Call<CheckoutResponse> createOrder(
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret,
            @Body CheckoutRequest checkoutRequest);///

    //Get current user order list
    @GET("orders/?")
    Call<List<CheckoutResponse>> getCustomerOrder(
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret,
            @Query("customer") int customer);

    //Get Current user single order
    @GET("orders/{id}/?")
    Call<CheckoutResponse> getCustomerSignalOrder(
            @Path("id") int id,
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret);

    //Get Shipping method cost in bd
    @GET("shipping/zones/1/methods/?")
    Call<List<ShippingMethodResponse>> getZoneShippingCost(
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret);//main_slider

    //Get Shipping method cost in bd
    @GET("main_slider/?")
    Call<List<Slider>> getTopSlider(
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret);////

    //Get just single slider image response
    @GET("media/{id}")
    Call<SliderImageResponse> getSliderImage(@Path("id") int id);

    //Get CountDown Timer
    @GET("timer_countdown")
    Call<List<TimmerResponse>> getCountDownTimer();////

    //Search product list
    @GET("products/?")
    Call<List<ProductResponse>> searchAProducts(
            @Query("consumer_key") String consumer_key,
            @Query("consumer_secret") String consumer_secret,
            @Query("search") String search,
            @Query("page") int page);//

    //Get Single product details
    @GET("cms_block/242")
    Call<SliderImageResponse> getReturnPolicy();
}
