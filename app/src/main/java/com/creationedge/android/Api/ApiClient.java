package com.creationedge.android.Api;

import android.content.Context;
import android.util.Base64;

import com.creationedge.android.common.Common;
import com.google.android.gms.common.internal.Constants;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class ApiClient {
    private static final String AUTH = "Basic "+ Base64.encodeToString(("ck_f7acca5da5060c4f97492b9f160f185f3d3fdfdc:cs_757fa9395fda27d319be96b9e60039bb790bd8a4").getBytes(),Base64.NO_WRAP);

    private static Retrofit getRetrofit(){
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .readTimeout(40,TimeUnit.SECONDS)
                .connectTimeout(40,TimeUnit.SECONDS)
                .build();
        //Set up Basic Auth Authorization
       /* OkHttpClient okHttpClient= new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .addHeader("Authorization",AUTH)
                                .method(original.method(),original.body());

                        Request request = requestBuilder.build();
                        return chain.proceed(request);

                    }
                }).build();*/

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Common.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    private static Retrofit getRetrofitc() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //set OAuth 1.0 consumer Authorization
      /*  OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer(Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        consumer.setTokenWithSecret("", "");

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .build();*/
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .readTimeout(40,TimeUnit.SECONDS)
                .connectTimeout(40,TimeUnit.SECONDS)
                .build();

        Retrofit retrof = new Retrofit.Builder()
                .baseUrl(Common.BASE_URL_FOR_AUTH)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        return retrof;
    }

    private static Retrofit getRetrofitV2(){
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .readTimeout(40,TimeUnit.SECONDS)
                .connectTimeout(40,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Common.BASE_URL_V2)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    private static Retrofit getRetrofitCoCart(){
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .readTimeout(40,TimeUnit.SECONDS)
                .connectTimeout(40,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Common.BASE_URL_CO_CART)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }
    private static Retrofit getRetrofitForForgot(){
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .readTimeout(40,TimeUnit.SECONDS)
                .connectTimeout(40,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Common.BASE_URL_FOR_FORGOT_PASSWORD)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }

    private static Retrofit getSocialRtrofit(){
        HttpLoggingInterceptor httpLoggingInterceptor=new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(httpLoggingInterceptor)
                .readTimeout(40,TimeUnit.SECONDS)
                .connectTimeout(40,TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://creationedge.com.bd/wp-json/nextend-social-login/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        return retrofit;
    }
    public static ApiInterfaceServices getPostServices() {
        ApiInterfaceServices apiInterfaceServices = getRetrofit().create(ApiInterfaceServices.class);
        return apiInterfaceServices;
    }

    public static ApiInterfaceServices getPostServiceDev() {
        ApiInterfaceServices apiInterfaceServices = getRetrofitc().create(ApiInterfaceServices.class);
        return apiInterfaceServices;
    }
    public static ApiInterfaceServices getForgotPasswordService() {
        ApiInterfaceServices apiInterfaceServices = getRetrofitForForgot().create(ApiInterfaceServices.class);
        return apiInterfaceServices;
    }
    public static ApiInterfaceServices getPostServicesV2() {
        ApiInterfaceServices apiInterfaceServices = getRetrofitV2().create(ApiInterfaceServices.class);
        return apiInterfaceServices;
    }


    public static ApiInterfaceServices getPostServicesCoCart() {
        ApiInterfaceServices apiInterfaceServices = getRetrofitCoCart().create(ApiInterfaceServices.class);
        return apiInterfaceServices;
    }
    public static ApiInterfaceServices getSocialServices() {
        ApiInterfaceServices apiInterfaceServices = getSocialRtrofit().create(ApiInterfaceServices.class);
        return apiInterfaceServices;
    }
}
