package com.creationedge.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.text.format.DateFormat;
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
import com.creationedge.android.ItemClickListener;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.model.CheckoutResponse;
import com.creationedge.android.model.ProductImageResponse;
import com.creationedge.android.model.ProductResponse;
import com.creationedge.android.view.activitys.ProductsViewActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ProductViewHolder> {

    private Context context;
    private List<CheckoutResponse> checkoutResponseList;

    private ItemClickListener mClickListener;

    public void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }

    public OrderListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<CheckoutResponse> checkoutResponseList){
        this.checkoutResponseList=checkoutResponseList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.purchase_history_item, parent, false);
        return new ProductViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        CheckoutResponse productResponse = checkoutResponseList.get(position);

        holder.order_id.setText("Order #"+String.valueOf(productResponse.getId()));
        holder.price.setText("৳"+productResponse.getTotal());
        holder.tv_status.setText(productResponse.getStatus());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        // replace with your start date string
        Date d = null;
        try {
            d = df.parse(productResponse.getDateCreated().replace("T", " "));
            df.setTimeZone(TimeZone.getDefault());

        } catch (ParseException e) {
            e.printStackTrace();
        }

        long serverMilsTime = d.getTime();
        String dateString = DateFormat.format("LLLL dd, yyyy", new Date(serverMilsTime)).toString();

        holder.order_date.setText(dateString);//end date format
    }

    @Override
    public int getItemCount() {
        return checkoutResponseList.size();
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView order_id, order_date, price,tv_status;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);

            order_id = itemView.findViewById(R.id.order_id);
            order_date = itemView.findViewById(R.id.order_date);
            price = itemView.findViewById(R.id.price);
            tv_status = itemView.findViewById(R.id.tv_status);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mClickListener !=null) mClickListener.onItemClick(v,getAdapterPosition());
        }
    }
}
