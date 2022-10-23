package com.creationedge.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.creationedge.android.ItemClickListener;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.model.CategoryResponse;
import com.creationedge.android.model.ProductImageResponse;
import com.creationedge.android.model.ProductResponse;
import com.creationedge.android.view.fragmentDialog.PurchaseHistoryFragment;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TopCategoryAdapter extends RecyclerView.Adapter<TopCategoryAdapter.BestSellerViewHolder> {

    private Context mContext;
    private List<CategoryResponse> productResponseList;
    private ItemClickListener mClickListener;

    public void setClickListener(ItemClickListener itemClickListener){
        this.mClickListener = itemClickListener;
    }


    public TopCategoryAdapter(Context mContext) {
        this.mContext = mContext;
    }

    public void setData(List<CategoryResponse> productResponseList){
        this.productResponseList=productResponseList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public BestSellerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.top_category_list_item, parent, false);
        return new BestSellerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BestSellerViewHolder holder, int position) {
        CategoryResponse productResponse = productResponseList.get(position);

        if(productResponse.getImage()!=null){
             Glide.with(mContext = holder.itemView.getContext())
                .applyDefaultRequestOptions(new RequestOptions().override(500, 500).placeholder(R.drawable.ic__product_loading))
                .load(productResponse.getImage().getSrc())
                .into(holder.productImage);
        }

        holder.productTitle.setText(productResponse.getName());
    }

    @Override
    public int getItemCount() {
        return productResponseList.size();
    }

    public class BestSellerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView productImage;
        private TextView productPrice, productTitle, productOff;

        public BestSellerViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.category_icon);
            productTitle = itemView.findViewById(R.id.category_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(mClickListener !=null) mClickListener.onItemClick(v,getAdapterPosition());
        }
    }

}
