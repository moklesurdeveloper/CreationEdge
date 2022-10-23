package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.creationedge.android.R;

public class FragmentDia_Live extends DialogFragment {
    private static final String TAG = "TAG";
    private RelativeLayout layout_continue;
    private TextView tv_not_now;
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
        view=inflater.inflate(R.layout.fragment_fb_live,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // initialization all views

        layout_continue = view.findViewById(R.id.layout_continue);
        tv_not_now = view.findViewById(R.id.tv_not_now);

        layout_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookId = "fb://page/180886265900454";
                //String urlPage = "https://www.facebook.com/creationedges";
                //live url
                String urlPage = "https://www.facebook.com/creationedges/live_videos/?ref=page_internal";

                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
                } catch (Exception e) {
                    Log.e(TAG, "Application not intalled.");
                    //Open url web page.
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(urlPage)));
                }

            }
        });

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

}
