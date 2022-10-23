package com.creationedge.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.common.Common;
import com.creationedge.android.model.Slider;
import com.creationedge.android.model.SliderImageResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MySliderAdapter extends RecyclerView.Adapter<MySliderAdapter.MyViewHolder> {
    private List<Slider> mySliderList;
    private ViewPager2 viewPager2;
    private Context context;

    public MySliderAdapter(List<Slider> mySliderList, ViewPager2 viewPager2, Context context) {
        this.mySliderList = mySliderList;
        this.viewPager2 = viewPager2;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.row_slider, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Slider sliderOb = mySliderList.get(position);

        Call<SliderImageResponse> call= ApiClient.getPostServicesV2().getSliderImage(sliderOb.getFeaturedMedia());
        call.enqueue(new Callback<SliderImageResponse>() {
            @Override
            public void onResponse(Call<SliderImageResponse> call, Response<SliderImageResponse> response) {
                if(response.isSuccessful()){
                    SliderImageResponse response1 = response.body();
                    Picasso.get()
                            .load(response1.getGuid().getRendered())
                            .fit()
                            .error(R.drawable.ic__product_loading)
                            .placeholder(R.drawable.ic__product_loading)
                            .into(holder.sliderImg);
                }
            }

            @Override
            public void onFailure(Call<SliderImageResponse> call, Throwable t) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, CategoryListActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mySliderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView sliderImg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            sliderImg = itemView.findViewById(R.id.myimage);
        }
    }
}
