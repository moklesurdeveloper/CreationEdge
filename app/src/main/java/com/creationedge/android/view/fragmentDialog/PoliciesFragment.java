package com.creationedge.android.view.fragmentDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.creationedge.android.R;

public class PoliciesFragment extends DialogFragment implements View.OnClickListener {
    public static String TAG = "TearmandCondition";
    private Context mContext;
    private View view;
    private Toolbar toolbar;
    private LinearLayout privacy,terms;
    private View vprivacy,vterms;
    private WebView webView;
    private ProgressBar progressBar;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext=context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new Dialog(getActivity(), getTheme()){
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
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_policis,container,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // init
        init(view);
        webSite("https://creationedge.com.bd/privacy-policy/");
    }

    private void init(View view) {

        terms=view.findViewById(R.id.termslayout);
        privacy=view.findViewById(R.id.privacy_layout);
        vprivacy=view.findViewById(R.id.privacyview);
        vterms=view.findViewById(R.id.termsview);
        webView=view.findViewById(R.id.web_view);
        progressBar = view.findViewById(R.id.spin_kit);

        terms.setOnClickListener(this);
        privacy.setOnClickListener(this);


        //set toolbar and back
        toolbar = view.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setTitle("Policies");
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


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.termslayout){
            vprivacy.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            vterms.setVisibility(View.VISIBLE);
            webSite("https://creationedge.com.bd/term-conditions/");
        }
        if (v.getId()==R.id.privacy_layout){
            vprivacy.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
            vterms.setVisibility(View.GONE);
            webSite("https://creationedge.com.bd/privacy-policy/");
        }
    }

    public void webSite(String urll){

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient() {

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                // progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(view.GONE);
                //  webView.getSettings().setTextZoom(160);
            }
        });
        webView.loadUrl(urll);
        WebSettings webSettings=webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setJavaScriptEnabled(true);

    }

}
