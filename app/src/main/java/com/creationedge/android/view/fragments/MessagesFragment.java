package com.creationedge.android.view.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.creationedge.android.R;

public class MessagesFragment extends Fragment {
    private static final String TAG = "TAG";
    private RelativeLayout layout_continue;
    private TextView tv_not_now;
    private Context mContext;
    public MessagesFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        layout_continue = view.findViewById(R.id.layout_continue);
        tv_not_now = view.findViewById(R.id.tv_not_now);

        layout_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
          /*      String facebookId = "fb://page/180886265900454";
                String urlPage = "https://www.facebook.com/creationedges";
                //live url
               // String urlPage = "https://www.facebook.com/creationedges/live_videos/?ref=page_internal";

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
                } catch (Exception e) {
                    Log.e(TAG, "Application not intalled.");
                    //Open url web page.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }*/

                String facebookId = "fb://messaging/" + "180886265900454";
                String urlPage = "https://www.facebook.com/messages/t/180886265900454";

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
                } catch (Exception e) {
                    Log.e(TAG, "Application not intalled.");
                    //Open url web page.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }
                /*Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("fb://messaging/" + "180886265900454"));
                startActivity(i);*/

            }
        });
    }
}