package com.creationedge.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.model.ProductImageResponse;
import com.creationedge.android.model.ProductResponse;
import com.creationedge.android.view.activitys.ProductsViewActivity;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cn.iwgang.countdownview.CountdownView;

public class CategoriesProductListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ProductResponse> categoryProductsList;
    private Context context;
    private static final int LOADING = 0;
    private static final int ITEM = 1;
    private boolean isLoadingAdded = false;
    private int ischat;

    public CategoriesProductListAdapter(Context context) {
        this.context = context;
        categoryProductsList = new LinkedList<>();
    }

    public void setData(List<ProductResponse> categoryProducts) {
        this.categoryProductsList = categoryProducts;
    }

    public void setPro(int ischat) {
        this.ischat = ischat;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                View viewItem = inflater.inflate(R.layout.featured_product_box, parent, false);
                viewHolder = new ProductViewHolder(viewItem);
                break;
            case LOADING:
                View viewLoading = inflater.inflate(R.layout.item_progress, parent, false);
                viewHolder = new LoadingViewHolder(viewLoading);
                break;
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holderm, int position) {

        ProductResponse productResponse = categoryProductsList.get(position);

        switch (getItemViewType(position)) {
            case ITEM:
                ProductViewHolder holder = (ProductViewHolder) holderm;
                //set product img
                List<ProductImageResponse> imageResponse = productResponse.getImages();
                if(imageResponse.size()>0){
                    Glide.with(context= holder.itemView.getContext())
                            .applyDefaultRequestOptions(new RequestOptions().override(500, 500).placeholder(R.drawable.ic__product_loading))
                            .load(imageResponse.get(0).getSrc())
                            .into(holder.productImage);
                }
                holder.productTitle.setText(productResponse.getName());
                holder.productPrice.setText("৳"+productResponse.getPrice());
                holder.productRating.setRating(Float.parseFloat(productResponse.getAverageRating()));
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Common.PRODUCT_id = productResponse.getId();
                        context.startActivity(new Intent(context, ProductsViewActivity.class));
                    }
                });
                break;

            case LOADING:
                LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holderm;
                if(ischat == 0){
                    loadingViewHolder.progressBar.setVisibility(View.VISIBLE);
                }
                else if(ischat == 1){
                    loadingViewHolder.progressBar.setVisibility(View.GONE);
                }

                break;
        }
    }

    @Override
    public int getItemCount() {
        return categoryProductsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == categoryProductsList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ProductResponse());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;


        int position = categoryProductsList.size() - 1;
        ProductResponse result = getItem(position);

        if (result != null) {
            categoryProductsList.remove(position);
            notifyItemRemoved(position);
        }


    }

    public void add(ProductResponse movie) {
        categoryProductsList.add(movie);
        notifyItemInserted(categoryProductsList.size() - 1);
    }

    public void addAll(List<ProductResponse> moveResults) {
        for (ProductResponse result : moveResults) {
            add(result);
        }

    }

    public ProductResponse getItem(int position) {
        return categoryProductsList.get(position);
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder{
        private ImageView productImage;
        private RatingBar productRating;
        private TextView productPrice, productTitle, productOff, product_discounted_price;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productRating = itemView.findViewById(R.id.product_rating);
            productPrice = itemView.findViewById(R.id.product_price);
            productTitle = itemView.findViewById(R.id.product_name);
            productOff = itemView.findViewById(R.id.tv_price_off);
            product_discounted_price = itemView.findViewById(R.id.product_discounted_price);
            productOff.setVisibility(View.GONE);
            product_discounted_price.setVisibility(View.GONE);
        }
    }

    public static class LoadingViewHolder extends RecyclerView.ViewHolder {

        private static ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = (ProgressBar) itemView.findViewById(R.id.loadmore_progress);

        }
    }
}
