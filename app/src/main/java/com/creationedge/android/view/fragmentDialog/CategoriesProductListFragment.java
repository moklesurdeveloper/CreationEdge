package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
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

public class CategoriesProductListFragment extends DialogFragment implements View.OnClickListener {
    private Toolbar toolbar;
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
        view=inflater.inflate(R.layout.categoriess_product_list_fragment,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialization all views
        initAll(view);

        //recycler set method cateygory post
        setUpRecycler();
        //onScrollListener
        recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            protected void loadMoreItems() {
                isLoading = true;
                currentPage += 1;
                loadNextPage();
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

        loadFirstPage();

        //swipeRefresh
        lay_swipe_refresh = view.findViewById(R.id.lay_swipe_refresh);
        lay_swipe_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                setUpRecycler();
                pageCount = 2;

                progressBar.setVisibility(View.VISIBLE);
                //onScrollListener
                recyclerView.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                    @Override
                    protected void loadMoreItems() {
                        isLoading = true;
                        currentPage += 1;
                        loadNextPage();
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

                loadFirstPage();

                lay_swipe_refresh.setRefreshing(false);
            }
        });
    }

    private void initAll(View view) {
        recyclerView = view.findViewById(R.id.recyclerview);
        progressBar =  view.findViewById(R.id.item_progress_bar);
        //set toolbar and back
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(Common.CATEGORY_NAME);
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

    private void setUpRecycler() {
        //set view

        linearLayoutManager = new GridLayoutManager(mContext, 2);
        paginationAdapter = new CategoriesProductListAdapter(mContext);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(paginationAdapter);

    }

    private void loadNextPage() {

        Call<List<ProductResponse>> categoryCall = ApiClient.getPostServices().getCategoriesProductList(Common.CONSUMER_KEY,Common.CONSUMER_SECRET,Common.CATEGORY_ID,pageCount);
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
        Call<List<ProductResponse>> productByCategoryCall = ApiClient.getPostServices().getCategoriesProductList(Common.CONSUMER_KEY,Common.CONSUMER_SECRET,Common.CATEGORY_ID,1);
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
}
