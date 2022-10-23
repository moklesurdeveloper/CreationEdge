package com.creationedge.android.view.fragments;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import androidx.recyclerview.widget.GridLayoutManager;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.ItemClickListener;
import com.creationedge.android.R;
import com.creationedge.android.adapter.CategoriesProductListAdapter;
import com.creationedge.android.adapter.FeaturedProductAdapter;
import com.creationedge.android.adapter.MySliderAdapter;
import com.creationedge.android.adapter.OnSaleProductAdapter;
import com.creationedge.android.adapter.TodaySaleListAdapter;
import com.creationedge.android.adapter.TopCategoryAdapter;
import com.creationedge.android.common.Common;

import com.creationedge.android.model.BrProductsOfDay;
import com.creationedge.android.model.CategoryResponse;
import com.creationedge.android.model.ProductResponse;
import com.creationedge.android.model.Slider;
import com.creationedge.android.model.TimmerResponse;
import com.creationedge.android.model.TodaySaleResponse;
import com.creationedge.android.view.activitys.ScanerActivity;
import com.creationedge.android.view.activitys.SearchActivity;
import com.creationedge.android.view.fragmentDialog.CategoriesProductListFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

import cn.iwgang.countdownview.CountdownView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class HomeFragment extends Fragment implements View.OnClickListener , ItemClickListener {
    private RelativeLayout layout;
    private NestedScrollView nested_layout;
    private CountdownView flash_countdown,today_countdown;
    private RelativeLayout img_search,flash_deal_section,todays_deal_section,featured_products_layout;
    private ImageView img_scan;
    private ViewPager2 viewPager;
    List<Slider> mySliderLists;
    private static int currentPage_BIG_SAVE2 = 0;
    private static int NUM_PAGES = 2;
    private LinearLayout indicatorlay;
    private MySliderAdapter adapter;
    List<CategoryResponse> topCategoryList;
    //Recycler
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recycler_top_categories,onSaleRecycler,todaySale,featuredRecycler;
    private TopCategoryAdapter topCategoryAdapter;
    private OnSaleProductAdapter  onSaleProductAdapter;
    private TodaySaleListAdapter todaySaleResponse;
    private FeaturedProductAdapter featuredProductAdapter;
    private ArrayList<String> todaySaleList;
    private SwipeRefreshLayout lay_swipe_refresh;
    private RecyclerView recyclerView;
    private GridLayoutManager linearLayoutManager;
    private CategoriesProductListAdapter paginationAdapter;
    private ProgressBar progressBar;
    private static final int PAGE_START = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 5;
    private int currentPage = PAGE_START;
    private int pageCount = 2;

    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //iint alll
        init(view);
        topCategoryList = new ArrayList<>();

        //Get top slider banner list
        getSliderdata();
        viewPager.registerOnPageChangeCallback( new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupCurrentIndicator(position);
            }
        });
        //NUM_PAGES =onBordingLists.size();
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage_BIG_SAVE2 == NUM_PAGES) {
                    currentPage_BIG_SAVE2 = 0;
                }
                viewPager.setCurrentItem(currentPage_BIG_SAVE2++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        //recycler set method cateygory post
        setUpRecycler();
        //onScrollListener
        nested_layout.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                    isLoading = true;
                    currentPage += 1;
                    loadNextPage();
                }
            }
        });
        loadFirstPage();

        //Set up Top Featured Category
        setUpRecyclerTopCategory();
        //load top Category list
        loadTopCategoryList();
        //Set up onSale recycler
        setUpRecyclerOnSale();
        //load onSale product list
        loadOnSaleProductList();
        //LOAD TODAY SALE PRODUCT LIST
        loadTodaySaleProductList();
        //Set up Featured recycler
        setUpRecyclerFeatured();
        //loadFeaturedProductList
        loadFeaturedProductList();

        //swipeRefresh
        lay_swipe_refresh = view.findViewById(R.id.lay_swipe_refresh);
        lay_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUpRecycler();
                pageCount = 2;
                //onScrollListener
                nested_layout.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        if (scrollY == v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight()) {
                            isLoading = true;
                            currentPage += 1;

                            loadNextPage();
                        }
                    }
                });

                loadFirstPage();

                //Set up Top Featured Category
                setUpRecyclerTopCategory();
                //load top Category list
                loadTopCategoryList();
                //Set up onSale recycler
                setUpRecyclerOnSale();
                //load onSale product list
                loadOnSaleProductList();



                lay_swipe_refresh.setRefreshing(false);
            }
        });

        //Set countdown timer
        setCountDownTimer();

        img_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, ScanerActivity.class));
            }
        });
    }

    private void init(View view) {
        img_scan = view.findViewById(R.id.img_scan);
        viewPager = view.findViewById(R.id.viewPager);
        indicatorlay = view.findViewById(R.id.lay_indicator);
        img_search = view.findViewById(R.id.searchlayout);
        flash_deal_section=view.findViewById(R.id.flash_deal_section);
        featured_products_layout=view.findViewById(R.id.featured_products_layout);
        todays_deal_section=view.findViewById(R.id.todays_deal_section);
        recycler_top_categories = view.findViewById(R.id.recycler_top_categories);
        onSaleRecycler = view.findViewById(R.id.recycler_flash_deals);
        todaySale = view.findViewById(R.id.recycler_todays_deals);
        featuredRecycler = view.findViewById(R.id.recycler_featured_products);
        recyclerView = view.findViewById(R.id.recycler_best_selling);
        layout = view.findViewById(R.id.layout);
        nested_layout = view.findViewById(R.id.nested_layout);
        flash_countdown = view.findViewById(R.id.flash_countdown);
        today_countdown = view.findViewById(R.id.today_countdown);
        progressBar =  view.findViewById(R.id.item_progress_bar);
        img_search.setOnClickListener(this);
    }

    private void setCountDownTimer() {
        Call<List<TimmerResponse>> call= ApiClient.getPostServicesV2().getCountDownTimer();
        call.enqueue(new Callback<List<TimmerResponse>>() {
            @Override
            public void onResponse(Call<List<TimmerResponse>> call, Response<List<TimmerResponse>> response) {
                if(response.isSuccessful()){
                    List<TimmerResponse> results = response.body();//2021-03-18 08:56 AM",
                    //Set timer flash deal
                    SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm a", Locale.ENGLISH);
                    f.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String countDate = (results.get(0).getEndDatetime());
                    Date now = new Date();
                    try {
                        Date d = f.parse(countDate);
                        f.setTimeZone(TimeZone.getDefault());
                        long currentTime = now.getTime();
                        long dtime = d.getTime();
                        long countdowntime = dtime - currentTime;
                        flash_countdown.start(countdowntime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //Set timer today dell
                    SimpleDateFormat todayf = new SimpleDateFormat("yyyy-MM-dd HH:mm a", Locale.ENGLISH);
                    todayf.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String todayTime = (results.get(1).getEndDatetime());
                    Date todaynow = new Date();
                    try {
                        Date d = todayf.parse(todayTime);
                        todayf.setTimeZone(TimeZone.getDefault());
                        long currentTime = todaynow.getTime();
                        long dtime = d.getTime();
                        long countdowntime = dtime - currentTime;
                        today_countdown.start(countdowntime);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }


                }
            }

            @Override
            public void onFailure(Call<List<TimmerResponse>> call, Throwable t) {

            }
        });
    }

    private void setUpRecyclerTopCategory() {
        //set view
        gridLayoutManager = new GridLayoutManager(mContext, 1,GridLayoutManager.HORIZONTAL,false);
        topCategoryAdapter = new TopCategoryAdapter(mContext);
        recycler_top_categories.setLayoutManager(gridLayoutManager);
        topCategoryAdapter.setClickListener(this);
    }

    private void setUpRecyclerOnSale() {
        //set view
        gridLayoutManager = new GridLayoutManager(mContext, 2,GridLayoutManager.VERTICAL,false);
        onSaleProductAdapter = new OnSaleProductAdapter(mContext);
        onSaleRecycler.setLayoutManager(gridLayoutManager);
    }

    private void setUpRecyclerFeatured() {
        //set view
        gridLayoutManager = new GridLayoutManager(mContext, 2,GridLayoutManager.VERTICAL,false);
        featuredProductAdapter = new FeaturedProductAdapter(mContext);
        featuredRecycler.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.searchlayout){
            startActivity(new Intent(mContext, SearchActivity.class));
        }//tv_live
    }

    public void getSliderdata(){
        Call<List<Slider>> call= ApiClient.getPostServicesV2().getTopSlider(Common.CONSUMER_KEY,Common.CONSUMER_SECRET);
        call.enqueue(new Callback<List<Slider>>() {
            @Override
            public void onResponse(Call<List<Slider>> call, Response<List<Slider>> response) {
                if (response.isSuccessful()){
                    mySliderLists=response.body();
                    adapter=new MySliderAdapter(mySliderLists,viewPager,mContext);
                    viewPager.setAdapter(adapter);

                    setupIndicator();
                    setupCurrentIndicator(0);

                    layout.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.GONE);
                }else{

                }

            }

            @Override
            public void onFailure(Call<List<Slider>> call, Throwable t) {

            }
        });
    }

    private void setupIndicator() {
        ImageView[] indicator=new ImageView[adapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(4,0,4,0);
        for (int i=0; i<indicator.length; i++){
            indicator[i]=new ImageView(mContext);
            //  indicator[i].setImageDrawable(ContextCompat.getDrawable(getContext(),R.drawable.indicator_inactive));
            indicator[i].setImageResource(R.drawable.indicator_inactive);
            indicator[i].setLayoutParams(layoutParams);
            indicatorlay.addView(indicator[i]);
        }

    }

    private void setupCurrentIndicator(int index) {
        int itemcildcount=indicatorlay.getChildCount();
        for (int i = 0; i < itemcildcount; i++) {
            ImageView imageView = (ImageView) indicatorlay.getChildAt(i);
            if (i == index) {
                // imageView.setImageDrawable(ContextCompat.getDrawable(requireActivity(),R.drawable.indicator_active));
                imageView.setImageResource(R.drawable.indicator_active);
            } else {
                imageView.setImageResource(R.drawable.indicator_inactive);
            }
        }

    }

    private void loadTopCategoryList() {
        Call<List<CategoryResponse>> featuredProductCall =ApiClient.getPostServices().getFeaturedTopCategory(Common.CONSUMER_KEY,Common.CONSUMER_SECRET);
        featuredProductCall.enqueue(new Callback<List<CategoryResponse>>() {
            @Override
            public void onResponse(Call<List<CategoryResponse>> call, Response<List<CategoryResponse>> response) {
                if(response.isSuccessful()){
                    topCategoryList = response.body();

                    topCategoryAdapter.setData(topCategoryList);
                    recycler_top_categories.setAdapter(topCategoryAdapter);
                }else {
                   // Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CategoryResponse>> call, Throwable t) {
             //   Toast.makeText(mContext, "error2"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadOnSaleProductList() {
        Call<List<ProductResponse>> featuredProductCall =ApiClient.getPostServices().getOnSaleProduct(Common.CONSUMER_KEY,Common.CONSUMER_SECRET,true);
        featuredProductCall.enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                if(response.isSuccessful()){
                    List<ProductResponse> results = response.body();

                    if(results.size() > 0){
                        flash_deal_section.setVisibility(View.VISIBLE);
                        onSaleProductAdapter.setData(results);
                        onSaleRecycler.setAdapter(onSaleProductAdapter);
                    }else {

                    }

                }else {
                    // Toast.makeText(mContext, "errorCode"+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {

            }
        });
    }

    private void loadTodaySaleProductList() {
        Call<List<TodaySaleResponse>> todaySaleCAll =ApiClient.getPostServicesV2().getTodaySale(Common.CONSUMER_KEY,Common.CONSUMER_SECRET);
        todaySaleCAll.enqueue(new Callback<List<TodaySaleResponse>>() {
            @Override
            public void onResponse(Call<List<TodaySaleResponse>> call, Response<List<TodaySaleResponse>> response) {
                if(response.isSuccessful()){
                    List<TodaySaleResponse> results = response.body();
                    BrProductsOfDay todaysale = results.get(0).getBrProductsOfDay();
                    todaySaleList = todaysale.getProducts();

                    if(results.size() > 0){
                        todays_deal_section.setVisibility(View.VISIBLE);
                        gridLayoutManager = new GridLayoutManager(mContext, 2,GridLayoutManager.VERTICAL,false);
                        todaySale.setLayoutManager(gridLayoutManager);
                        todaySaleResponse = new TodaySaleListAdapter(todaySaleList, mContext);
                        todaySale.setAdapter(todaySaleResponse);
                    }else {

                    }

                }else {

                }
            }

            @Override
            public void onFailure(Call<List<TodaySaleResponse>> call, Throwable t) {

            }
        });
    }

    private void loadFeaturedProductList() {
        Call<List<ProductResponse>> featuredProductCall =ApiClient.getPostServices().getFeaturedProduct(Common.CONSUMER_KEY,Common.CONSUMER_SECRET,true);
        featuredProductCall.enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                if(response.isSuccessful()){
                    List<ProductResponse> results = response.body();
                    featured_products_layout.setVisibility(View.VISIBLE);
                    featuredProductAdapter.setData(results);
                    featuredRecycler.setAdapter(featuredProductAdapter);
                }else {
                    //Toast.makeText(mContext, "errorCode"+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {
              ///  Toast.makeText(mContext, "errorFaild"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setUpRecycler() {
        //set view

        linearLayoutManager = new GridLayoutManager(mContext, 2);
        paginationAdapter = new CategoriesProductListAdapter(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(paginationAdapter);

    }

    private void loadNextPage() {

        Call<List<ProductResponse>> categoryCall = ApiClient.getPostServices().getBestSellingProductList("popularity",Common.CONSUMER_KEY,Common.CONSUMER_SECRET,pageCount);
        categoryCall.enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                if (response.isSuccessful()) {
                    List<ProductResponse> categoryProducts = response.body();

                    if (categoryProducts.size() <= 0) {

                        paginationAdapter.setPro(1);
                        paginationAdapter.removeLoadingFooter();
                        isLoading = false;

                        paginationAdapter.addAll(categoryProducts);

                        pageCount = pageCount + 1;

                        if (currentPage != TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }else {
                        paginationAdapter.setPro(0);
                        paginationAdapter.removeLoadingFooter();
                        isLoading = false;

                        paginationAdapter.addAll(categoryProducts);

                        pageCount = pageCount +  1;

                        if (currentPage != TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }

                }else {
                    isLoading = false;
                }
            }

            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {

            }
        });



    }

    private void loadFirstPage() {
        Call<List<ProductResponse>> productByCategoryCall = ApiClient.getPostServices().getBestSellingProductList("popularity",Common.CONSUMER_KEY,Common.CONSUMER_SECRET,1);
        productByCategoryCall.enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                if (response.isSuccessful()) {
                    List<ProductResponse> categoryProducts = response.body();
                    progressBar.setVisibility(View.GONE);
                    paginationAdapter.setData(categoryProducts);

                    if (currentPage <= TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                    else isLastPage = true;
                }
            }

            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onItemClick(View view, int position) {
        CategoryResponse categoryResponse = topCategoryList.get(position);
        Common.CATEGORY_ID = categoryResponse.getId();
        Common.CATEGORY_NAME = categoryResponse.getName();
        CategoriesProductListFragment purchaseHistoryFragment = new CategoriesProductListFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        purchaseHistoryFragment.show(ft,"Tag");
    }
}