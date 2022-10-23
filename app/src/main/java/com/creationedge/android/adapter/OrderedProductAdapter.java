package com.creationedge.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.data.CartResponse;
import com.creationedge.android.model.CartData;
import com.creationedge.android.model.ProductImageResponse;
import com.creationedge.android.model.ProductResponse;
import com.creationedge.android.view.activitys.ProductsViewActivity;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderedProductAdapter extends RecyclerView.Adapter<OrderedProductAdapter.MyViewHolder> {
    private List<CartResponse> cartODetailsList;
    private Context context;

    public OrderedProductAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<CartResponse> cartODetailsList) {
        this.cartODetailsList = cartODetailsList;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_ordered_product, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        CartResponse cartODetails = cartODetailsList.get(position);

        Call<ProductResponse> singleProductCall = ApiClient.getPostServices().getSingleProduct(cartODetails.getProductId(),Common.CONSUMER_KEY,Common.CONSUMER_SECRET);
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

                }else {
                    Toast.makeText(context, "errorCode"+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Toast.makeText(context, "errorFaild"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.tvProductName.setText(cartODetails.getName());
        holder.tvQuantity.setText(String.valueOf(cartODetails.getQuantity()));
        holder.tvTk.setText("৳"+String.valueOf(cartODetails.getPrice()));
        holder.tvTotalPrice.setText("৳"+cartODetails.getTotal());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Common.PRODUCT_id=cartODetails.getProductId();
                context.startActivity(new Intent(context, ProductsViewActivity.class));

            }
        });


    }

    @Override
    public int getItemCount() {
        return cartODetailsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView productImage;
        private TextView tvProductId;
        private TextView tvProductName;
        private TextView tvTk;
        private TextView tvTotalPrice;
        private TextView tvQuantity;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.img_product_image);
            tvProductName = itemView.findViewById(R.id.product_name);
            tvTk = itemView.findViewById(R.id.tk);
            tvTotalPrice = itemView.findViewById(R.id.total_tk);
            tvQuantity = itemView.findViewById(R.id.quantity);
        }
    }
}
