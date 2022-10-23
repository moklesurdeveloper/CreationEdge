package com.creationedge.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.util.List;

public class FeaturedProductAdapter extends RecyclerView.Adapter<FeaturedProductAdapter.ProductViewHolder> {

    private Context context;
    private List<ProductResponse> productResponseList;


    public FeaturedProductAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ProductResponse> productResponseList){
        this.productResponseList=productResponseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.featured_product_box, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductResponse productResponse = productResponseList.get(position);

        List<ProductImageResponse> imageResponse = productResponse.getImages();
        if(imageResponse.size()>0){
        Glide.with(context = holder.itemView.getContext())
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
    }

    @Override
    public int getItemCount() {
        return productResponseList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
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
}
