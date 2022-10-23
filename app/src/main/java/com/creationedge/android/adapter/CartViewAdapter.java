package com.creationedge.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.creationedge.android.ItemClickListener;
import com.creationedge.android.R;
import com.creationedge.android.model.CartData;
import com.creationedge.android.model.CartKeyResponse;
import com.creationedge.android.view.activitys.ProductsViewActivity;

import java.util.List;

public class CartViewAdapter extends RecyclerView.Adapter<CartViewAdapter.CartViewHolder> {
    private List<CartData> cartResponseList;
    private Context context;
    private ItemClickListener mClickListener;

    public CartViewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<CartData> cartResponseList) {
        this.cartResponseList = cartResponseList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartData cartKeyResponse = cartResponseList.get(position);
        holder.name.setText(cartKeyResponse.getProductName());
        Glide.with(holder.itemView.getContext())
                .applyDefaultRequestOptions(new RequestOptions().override(500, 500).placeholder(R.drawable.ic__product_loading))
                .load(cartKeyResponse.getProduct_image())
                .into(holder.productImage);

       holder.price.setText("৳"+String.valueOf(cartKeyResponse.getLineTotal()));
       holder.displaynumber.setText(String.valueOf(cartKeyResponse.getQuantity()));


       if(cartKeyResponse.getQuantity() == 1){
           holder.decrement.setTextColor(ContextCompat.getColor(context, R.color.main_grey));
           holder.decrement.setEnabled(false);
       }else {
           holder.decrement.setTextColor(ContextCompat.getColor(context, R.color.black));
           holder.decrement.setEnabled(true);
       }
    }

    @Override
    public int getItemCount() {
        return cartResponseList.size();
    }


    public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView productImage, remove_item;
        private TextView name, price, increment, decrement, displaynumber;
        private RelativeLayout cart_item;


        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.img_product_image);
            remove_item = itemView.findViewById(R.id.img_delete);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            increment = itemView.findViewById(R.id.increment);
            decrement = itemView.findViewById(R.id.decrement);
            displaynumber = itemView.findViewById(R.id.display);
            cart_item=itemView.findViewById(R.id.cart_item);


            remove_item.setOnClickListener(this);
            increment.setOnClickListener(this);
            decrement.setOnClickListener(this);
            cart_item.setOnClickListener(this);

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
}
