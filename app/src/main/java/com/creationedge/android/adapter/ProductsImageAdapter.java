package com.creationedge.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.creationedge.android.ItemClickListener;
import com.creationedge.android.R;
import com.creationedge.android.model.ProductImageResponse;


import java.util.List;

public class ProductsImageAdapter extends RecyclerView.Adapter<ProductsImageAdapter.ImageViewHolder> {

    private List<ProductImageResponse> imasgesliderlist;
    private ViewPager2 viewPager2;
    private Context context;
    private ItemClickListener mClickListener;


    public ProductsImageAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<ProductImageResponse> imasgesliderlist, ViewPager2 viewPager2) {
        this.imasgesliderlist = imasgesliderlist;
        this.viewPager2 = viewPager2;

    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_products_details_slider, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        ProductImageResponse galleries = imasgesliderlist.get(position);

        Glide.with(context = holder.itemView.getContext())
                .applyDefaultRequestOptions(new RequestOptions().override(500, 500).placeholder(R.drawable.ic__product_loading))
                .load(galleries.getSrc())
                .into(holder.imageView);

    }

    @Override
    public int getItemCount() {
        return imasgesliderlist.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ImageView imageView;


        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.myimage);

            itemView.setOnClickListener(this);

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
