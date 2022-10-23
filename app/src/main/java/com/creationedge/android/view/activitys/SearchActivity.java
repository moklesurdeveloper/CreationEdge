package com.creationedge.android.view.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.adapter.CategoriesProductListAdapter;
import com.creationedge.android.common.Common;
import com.creationedge.android.data.PaginationScrollListener;
import com.creationedge.android.model.ProductResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
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
    private ImageView img_backbutton;
    private SearchView search_key;
    private TextView tv_empty_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        tv_empty_text = findViewById(R.id.tv_empty_text);
        search_key = findViewById(R.id.search_key);
        img_backbutton = findViewById(R.id.img_backbutton);
        recyclerView = findViewById(R.id.product_list);
        progressBar =  findViewById(R.id.item_progress_bar);
        search_key.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do something here

                tv_empty_text.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
               //recycler set method cateygory post
                setUpRecycler();
                //onScrollListener
                recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                    @Override
                    protected void loadMoreItems() {
                        isLoading = true;
                        currentPage += 1;
                        loadNextPage(query);
                    }

                    @Override
                    public boolean isLastPage() {
                        return isLastPage;
                    }

                    @Override
                    public boolean isLoading() {
                        return isLoading;
                    }
                });

                loadFirstPage(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        img_backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void setUpRecycler() {
        //set view

        linearLayoutManager = new GridLayoutManager(SearchActivity.this, 2);
        paginationAdapter = new CategoriesProductListAdapter(SearchActivity.this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(paginationAdapter);

    }

    private void loadNextPage(String query) {

        Call<List<ProductResponse>> categoryCall = ApiClient.getPostServices().searchAProducts(Common.CONSUMER_KEY,Common.CONSUMER_SECRET,query,pageCount);
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

    private void loadFirstPage(String query) {
        Call<List<ProductResponse>> productByCategoryCall = ApiClient.getPostServices().searchAProducts(Common.CONSUMER_KEY,Common.CONSUMER_SECRET,query,1);
        productByCategoryCall.enqueue(new Callback<List<ProductResponse>>() {
            @Override
            public void onResponse(Call<List<ProductResponse>> call, Response<List<ProductResponse>> response) {
                if (response.isSuccessful()) {
                    List<ProductResponse> categoryProducts = response.body();
                    progressBar.setVisibility(View.GONE);

                    if(categoryProducts.size()>0){
                        paginationAdapter.setData(categoryProducts);
                        if (currentPage <= TOTAL_PAGES) paginationAdapter.addLoadingFooter();
                        else isLastPage = true;
                    }else {
                        tv_empty_text.setVisibility(View.VISIBLE);
                    }


                }
            }

            @Override
            public void onFailure(Call<List<ProductResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
            }
        });

    }
}