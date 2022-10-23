package com.creationedge.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.model.ProductImageResponse;
import com.creationedge.android.model.ProductResponse;
import com.creationedge.android.view.activitys.ProductsViewActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodaySaleListAdapter extends RecyclerView.Adapter<TodaySaleListAdapter.TodaySaleVH> {
    private Method method;
    private ArrayList<String> todaySaleList;
    private Context context;

    public TodaySaleListAdapter(ArrayList<String> todaySaleList, Context context) {
        this.todaySaleList = todaySaleList;
        this.context = context;
    }

    @NonNull
    @Override
    public TodaySaleVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.featured_product_box, parent, false);
        return new TodaySaleVH(view);

    }

    @Override
    public void onBindViewHolder(@NonNull TodaySaleVH holder, int position) {
        String todaySaleResponse = todaySaleList.get(position);
        method = new Method();
        Call<ProductResponse> singleProductCall = ApiClient.getPostServices().getSingleProduct(Integer.parseInt(todaySaleResponse),Common.CONSUMER_KEY,Common.CONSUMER_SECRET);
        singleProductCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if(response.isSuccessful()){
                   ProductResponse productResponse = response.body();

                    List<ProductImageResponse> imageResponse = productResponse.getImages();
                    if(imageResponse.size()>0){
                        Glide.with(context= holder.itemView.getContext())
                                .applyDefaultRequestOptions(new RequestOptions().override(500, 500).placeholder(R.drawable.ic__product_loading))
                                .load(imageResponse.get(0).getSrc())
                                .into(holder.productImage);
                    }
                    holder.product_discounted_price.setText("৳"+productResponse.getRegularPrice());
                    holder.product_discounted_price.setPaintFlags( holder.product_discounted_price.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.productTitle.setText(productResponse.getName());
                    holder.productPrice.setText("৳"+productResponse.getPrice());
                    holder.productRating.setRating(Float.parseFloat(productResponse.getAverageRating()));

                    method.productOffPersent(productResponse.getPrice(), productResponse.getRegularPrice(), holder.productOff);
                }else {
                    Toast.makeText(context, "errorCode"+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(context, "errorFaild"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.PRODUCT_id = Integer.parseInt(todaySaleResponse);
                 context.startActivity(new Intent(context, ProductsViewActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return todaySaleList.size();
    }

    public class TodaySaleVH extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private RatingBar productRating;
        private TextView productPrice, productTitle, productOff, product_discounted_price;

        public TodaySaleVH(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productRating = itemView.findViewById(R.id.product_rating);
            productPrice = itemView.findViewById(R.id.product_price);
            productTitle = itemView.findViewById(R.id.product_name);
            productOff = itemView.findViewById(R.id.tv_price_off);
            product_discounted_price = itemView.findViewById(R.id.product_discounted_price);
        }
    }
}