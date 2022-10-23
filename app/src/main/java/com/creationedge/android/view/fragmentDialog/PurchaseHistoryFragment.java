package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.creationedge.android.Api.ApiClient;
import com.creationedge.android.ItemClickListener;
import com.creationedge.android.R;
import com.creationedge.android.adapter.OnSaleProductAdapter;
import com.creationedge.android.adapter.OrderListAdapter;
import com.creationedge.android.common.Common;
import com.creationedge.android.model.CheckoutResponse;
import com.creationedge.android.model.ProductResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PurchaseHistoryFragment extends DialogFragment implements ItemClickListener {
    private Toolbar toolbar;
    private OrderListAdapter orderListAdapter;
    private List<CheckoutResponse> results;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView;
    private ProgressBar item_progress_bar;
    private TextView purchase_history_empty_text;
    private String user_token,id,name,photo;
    private Context mContext;
    private View view;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new Dialog(getActivity(),getTheme()){
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
        view=inflater.inflate(R.layout.fragment_purchase_history,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //get current user data to sharePre
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        user_token = sharedPreferences.getString("user_token",null);
        id = sharedPreferences.getString("id",null);
        name=sharedPreferences.getString("name",null);
        photo=sharedPreferences.getString("photo",null);
        // initialization all views
        initAll(view);

        results = new ArrayList<>();

        //Set up recyclerView
        setUpRecyclerOrder();
        //Get and load order list
        loadOrderList();

    }

    private void initAll(View view) {
        recyclerView = view.findViewById(R.id.purchase_history_list);
        item_progress_bar = view.findViewById(R.id.item_progress_bar);
        purchase_history_empty_text = view.findViewById(R.id.purchase_history_empty_text);

        //set toolbar and back
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle(getString(R.string.purchase_history));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });


    }

    private void setUpRecyclerOrder() {
        //set view
        gridLayoutManager = new GridLayoutManager(mContext, 1,GridLayoutManager.VERTICAL,false);
        orderListAdapter = new OrderListAdapter(mContext);
        recyclerView.setLayoutManager(gridLayoutManager);
        orderListAdapter.setClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog=getDialog();
        if (dialog!=null){
            int width=ViewGroup.LayoutParams.MATCH_PARENT;
            int height=ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width,height);
        }
    }

    private void loadOrderList() {
        Call<List<CheckoutResponse>> featuredProductCall = ApiClient.getPostServices().getCustomerOrder(Common.CONSUMER_KEY,Common.CONSUMER_SECRET,Integer.parseInt(id));
        featuredProductCall.enqueue(new Callback<List<CheckoutResponse>>() {
            @Override
            public void onResponse(Call<List<CheckoutResponse>> call, Response<List<CheckoutResponse>> response) {
                if(response.isSuccessful()){
                    results = response.body();
                    item_progress_bar.setVisibility(View.GONE);
                    if(results.size() > 0){
                        orderListAdapter.setData(results);
                        recyclerView.setAdapter(orderListAdapter);
                    }else {
                        purchase_history_empty_text.setVisibility(View.VISIBLE);
                    }

                }else {
                    item_progress_bar.setVisibility(View.GONE);
                    // Toast.makeText(mContext, "errorCode"+response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CheckoutResponse>> call, Throwable t) {
                item_progress_bar.setVisibility(View.GONE);
                Toast.makeText(mContext, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onItemClick(View view, int position) {
        CheckoutResponse response = results.get(position);
        Common.ORDER_ID = response.getId();
        PurchaseHistoryDetails purchaseHistoryDetails = new PurchaseHistoryDetails();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        purchaseHistoryDetails.show(ft,"Tag");
    }
}
