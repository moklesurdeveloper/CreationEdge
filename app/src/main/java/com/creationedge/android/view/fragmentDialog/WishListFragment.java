package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.R;
import com.creationedge.android.adapter.WishlistAdapter;
import com.creationedge.android.common.Common;
import com.creationedge.android.common.Method;
import com.creationedge.android.model.ShareKeyResponse;
import com.creationedge.android.model.WishlistResponse;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WishListFragment extends DialogFragment implements WishlistAdapter.ItemClickListener {
    private Context mContext;
    private View view;
    private Toolbar toolbar;
    private ProgressBar progressBar;
    private RecyclerView wishlistRecycler;
    private TextView emptyText;
    private WishlistAdapter wishlistAdapter;
    private List<WishlistResponse> wishlistResponsesList;
    private String id, sharedkey;
    private Method method = new Method();

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()) {
            @Override
            public void onBackPressed() {
                getDialog().dismiss();
            }
        };
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.wishlist, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialization all views
        initAll(view);
        wishlistRecycler.setLayoutManager(new LinearLayoutManager(mContext));
        wishlistAdapter = new WishlistAdapter(mContext);
        wishlistAdapter.setClickListener(this);

        //get current user data to sharePre
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);

        // getting wishlist shared key if not saved
        SharedPreferences sp = mContext.getSharedPreferences("wishListData", Context.MODE_PRIVATE);
        sharedkey = sp.getString("key", null);
        if (TextUtils.isEmpty(sharedkey)) {
            getShareKey(id);

        } else {
            progressBar.setVisibility(View.VISIBLE);
            getWishlist(sharedkey);
        }

    }


    private void initAll(View view) {

        progressBar = view.findViewById(R.id.item_progress_bar);
        emptyText = view.findViewById(R.id.wishlist_empty_text);
        wishlistRecycler = view.findViewById(R.id.product_list);

        //set toolbar and back
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(getString(R.string.wishlist));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    private void getShareKey(String id) {
        progressBar.setVisibility(View.VISIBLE);
        Call<List<ShareKeyResponse>> call = ApiClient.getPostServices().getShareKey(id, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        call.enqueue(new Callback<List<ShareKeyResponse>>() {
            @Override
            public void onResponse(Call<List<ShareKeyResponse>> call, Response<List<ShareKeyResponse>> response) {
                if (response.isSuccessful()) {
                    List<ShareKeyResponse> shareKeyResponselsit = response.body();
                    ShareKeyResponse shareKeyResponse = shareKeyResponselsit.get(0);

                    // save shared key in sharedPreferences
                    SharedPreferences preferences = mContext.getSharedPreferences("wishListData", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("key", shareKeyResponse.getShareKey());
                    editor.apply();
                    getWishlist(shareKeyResponse.getShareKey());

                }
            }

            @Override
            public void onFailure(Call<List<ShareKeyResponse>> call, Throwable t) {
            }
        });
    }

    private void getWishlist(String sharedkey) {
        Call<List<WishlistResponse>> call = ApiClient.getPostServices().showWishlist(sharedkey, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        call.enqueue(new Callback<List<WishlistResponse>>() {
            @Override
            public void onResponse(Call<List<WishlistResponse>> call, Response<List<WishlistResponse>> response) {
                if (response.isSuccessful()) {
                    progressBar.setVisibility(View.GONE);
                    wishlistResponsesList = response.body();

                    if (0 < wishlistResponsesList.size()) {
                        wishlistAdapter.setData(wishlistResponsesList);
                        wishlistRecycler.setAdapter(wishlistAdapter);
                    } else {
                        emptyText.setVisibility(View.VISIBLE);
                    }

                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Something went wrong! Please try again", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<WishlistResponse>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(mContext, "Failed! Please try again", Toast.LENGTH_SHORT).show();

            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {

        WishlistResponse wishlistResponse = wishlistResponsesList.get(position);
        if (view.getId() == R.id.heart_icon) {
            wishlistAdapter.notifyItemRemoved(position);
            wishlistResponsesList.remove(position);
            wishlistAdapter.notifyDataSetChanged();
            removerequest(wishlistResponse.getItemId());
        }
    }

    private void removerequest(Integer itemId) {
        Call<String> call = ApiClient.getPostServices().removewishlist(itemId, Common.CONSUMER_KEY, Common.CONSUMER_SECRET);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    method.showToastMessage("Remove!", 1, mContext);

                    if (!(0 < wishlistResponsesList.size())) {
                        emptyText.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }
}
