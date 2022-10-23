package com.creationedge.android.common;

import com.creationedge.android.model.CartDBModel;
import com.creationedge.android.model.CartData;

import java.util.ArrayList;
import java.util.List;

public class Common {
    public static final String BASE_URL = "https://creationedge.com.bd/wp-json/wc/v3/";
    public static final String BASE_URL_V2 = "https://creationedge.com.bd/wp-json/wp/v2/";
    public static final String BASE_URL_FOR_AUTH = "https://creationedge.com.bd/wp-json/jwt-auth/v1/";
    public static final String BASE_URL_FOR_FORGOT_PASSWORD = "https://creationedge.com.bd/wp-json/bdpwr/v1/";
    public static final String BASE_URL_CO_CART = "https://creationedge.com.bd/wp-json/cocart/v1/";
    public static final String CONSUMER_KEY = "ck_f7acca5da5060c4f97492b9f160f185f3d3fdfdc";
    public static final String CONSUMER_SECRET = "cs_757fa9395fda27d319be96b9e60039bb790bd8a4";
    public static int PRODUCT_id;
    public static int ORDER_ID;
    public static int CATEGORY_ID;
    public static int TOTAL_PRICE;
    public static String EMAIL;
    public static String IMAGE_LINK;
    public static String CATEGORY_NAME;
    public static List<CartData> cartResponseList = new ArrayList<>();

    public static List<CartDBModel> cartDBModelList = new ArrayList<>();
    public static boolean logingCheck = false;

}
