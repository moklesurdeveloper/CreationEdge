package com.creationedge.android.adapter;

import android.content.Context;
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
import com.creationedge.android.model.ProductResponse;
import com.creationedge.android.model.WishlistResponse;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishlistAdapter extends RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder> {

    private List<WishlistResponse> wishlistResponseList;
    private Context mContext;
    private Method method=new Method();
    private ItemClickListener mClickListener;

    public WishlistAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<WishlistResponse> wishlistResponseList){
        this.wishlistResponseList=wishlistResponseList;
    }

    @NonNull
    @Override
    public WishlistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.wishlist_product_item,parent,false);
        return new WishlistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WishlistViewHolder holder, int position) {
        WishlistResponse wishlistResponse=wishlistResponseList.get(position);
        getProductDetails(holder,wishlistResponse.getProductId());


    }



    private void getProductDetails(WishlistViewHolder holder,Integer productId) {

        Call<ProductResponse> singleProductCall = ApiClient.getPostServices().getSingleProduct(productId, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        singleProductCall.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()) {
                    ProductResponse productResponse = response.body();

                    int price = Integer.parseInt(productResponse.getPrice());
                    int rprice = Integer.parseInt(productResponse.getRegularPrice());
                    holder.productPrice.setText("৳"+String.valueOf(price));
                    holder.productName.setText(productResponse.getName());
                    holder.productRating.setRating(Float.valueOf(productResponse.getAverageRating()));
                    Glide.with(mContext = holder.itemView.getContext())
                            .applyDefaultRequestOptions(new RequestOptions().override(500, 500).placeholder(R.drawable.ic__product_loading))
                            .load(productResponse.getImages().get(0).getSrc())
                            .into(holder.productImage);


                } else {
                    Toast.makeText(mContext, "errorCode" + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {

            }
        });



    }

    @Override
    public int getItemCount() {
        return wishlistResponseList.size();
    }

    public class WishlistViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView productImage,remove;
        private TextView productName,discountPrice,productPrice;
        private RatingBar productRating;

        public WishlistViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage=itemView.findViewById(R.id.product_image);
            remove=itemView.findViewById(R.id.heart_icon);
            productName=itemView.findViewById(R.id.product_name);
            discountPrice=itemView.findViewById(R.id.product_discounted_price);
            productPrice=itemView.findViewById(R.id.product_price);
            productRating=itemView.findViewById(R.id.product_rating);
            remove.setOnClickListener(this);

        }
        @Override
        public void onClick(View v) {
            if (mClickListener != null) mClickListener.onItemClick(v, getAdapterPosition());


        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}

