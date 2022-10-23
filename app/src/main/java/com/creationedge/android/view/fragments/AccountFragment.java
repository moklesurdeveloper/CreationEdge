package com.creationedge.android.view.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.creationedge.android.R;
import com.creationedge.android.view.activitys.Account_InfoActivity;
import com.creationedge.android.view.activitys.HomeActivity;
import com.creationedge.android.view.activitys.LoginActivity;
import com.creationedge.android.view.fragmentDialog.PoliciesFragment;
import com.creationedge.android.view.fragmentDialog.PurchaseHistoryFragment;
import com.creationedge.android.view.fragmentDialog.SetNewPasswordFragment;
import com.creationedge.android.view.fragmentDialog.WishListFragment;
import com.squareup.picasso.Picasso;

import java.util.List;


public class AccountFragment extends Fragment implements View.OnClickListener {

    private RelativeLayout wishList, purchaseHistory, accountInfo, resetPassword, aboutUs,
            privacyPolicy, logout, contact_us;
    private ImageView profileImage;
    private TextView profileName;
    private Context mContext;
    private String id, name, photo;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //init alll view
        init(view);
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);
        name = sharedPreferences.getString("name", null);
        photo = sharedPreferences.getString("photo", null);

    }

    private void init(View view) {
        wishList = view.findViewById(R.id.wishlist);
        purchaseHistory = view.findViewById(R.id.purchase_history);
        accountInfo = view.findViewById(R.id.account_info);
        aboutUs = view.findViewById(R.id.about_info);
        privacyPolicy = view.findViewById(R.id.privacy_policy);
        logout = view.findViewById(R.id.logout);
        contact_us = view.findViewById(R.id.contact_us);
        profileImage = view.findViewById(R.id.account_image);
        profileName = view.findViewById(R.id.account_name);
        resetPassword = view.findViewById(R.id.resetpassword);

        wishList.setOnClickListener(this);
        purchaseHistory.setOnClickListener(this);
        accountInfo.setOnClickListener(this);
        aboutUs.setOnClickListener(this);
        privacyPolicy.setOnClickListener(this);
        resetPassword.setOnClickListener(this);
        contact_us.setOnClickListener(this);
        logout.setOnClickListener(this);
        profileName.setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", null);
        name = sharedPreferences.getString("name", null);
        photo = sharedPreferences.getString("photo", null);

        if (!TextUtils.isEmpty(id)) {
            Picasso.get().load(photo).placeholder(R.drawable.icons8_male_user_100_black).fit().into(profileImage);
            profileName.setText(name);
        }
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.wishlist) {

            if (TextUtils.isEmpty(id)) {
                Toast.makeText(mContext, "Please LogIn First!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext, LoginActivity.class));
            } else {
                WishListFragment wishListFragment = new WishListFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                wishListFragment.show(ft, "Tag");
            }
        }
        if (v.getId() == R.id.purchase_history) {
            if (TextUtils.isEmpty(id)) {
                Toast.makeText(mContext, "Please LogIn First!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext, LoginActivity.class));
            } else {
                PurchaseHistoryFragment purchaseHistoryFragment = new PurchaseHistoryFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                purchaseHistoryFragment.show(ft, "Tag");
            }
        }

        if (v.getId() == R.id.account_info) {
            if (TextUtils.isEmpty(id)) {
                Toast.makeText(mContext, "Please LogIn First!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext, LoginActivity.class));
            } else {
                startActivity(new Intent(mContext, Account_InfoActivity.class));
            }

        }

        if (v.getId() == R.id.resetpassword) {

            if (TextUtils.isEmpty(id)) {
                Toast.makeText(mContext, "Please LogIn First!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mContext, LoginActivity.class));
            } else {
                SetNewPasswordFragment setNewPasswordFragment = new SetNewPasswordFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                setNewPasswordFragment.show(ft, "Tag");
            }

        }
        if (v.getId() == R.id.about_info) {
            Uri uriy = Uri.parse("https://creationedge.com.bd/about-us/");
            Intent yt = new Intent(Intent.ACTION_VIEW, uriy);
            startActivity(yt);
            Toast.makeText(mContext, "Please Wait", Toast.LENGTH_SHORT).show();
        }

        if (v.getId() == R.id.privacy_policy) {
            PoliciesFragment policiesFragment = new PoliciesFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            policiesFragment.show(ft, "Tag");
        }
        if (v.getId() == R.id.logout) {
            if (TextUtils.isEmpty(id)) {
                Toast.makeText(mContext, "LogIn First!", Toast.LENGTH_SHORT).show();
            } else {
                dialogLogout();
            }
        }

        if (v.getId() == R.id.contact_us) {
            suppont();
        }

        if (v == profileName) {
            if (TextUtils.isEmpty(id)) {
                startActivity(new Intent(mContext, LoginActivity.class));
            }
        }
    }

    public void suppont() {
        Log.i("Send email", "");
        String[] TO = {"creationedge.net@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Creation Edge");

        final PackageManager pm = mContext.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            emailIntent.setClassName(best.activityInfo.packageName, best.activityInfo.name);

        mContext.startActivity(emailIntent);
    }

    private void dialogLogout() {
        // Custom Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.logout_dailog, null);

        ImageButton imageButton = view.findViewById(R.id.logoutImg);
        TextView title = view.findViewById(R.id.titleText);
        title.setText("Are your sure to Log out?");
        imageButton.setImageResource(R.drawable.ic_baseline_exit_to_app_24);
        builder.setCancelable(true);

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("YES, LOG OUT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = mContext.getSharedPreferences("userData", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                SharedPreferences pf = mContext.getSharedPreferences("wishListData", Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = pf.edit();
                ed.clear();
                ed.apply();
                mContext.startActivity(new Intent(mContext, HomeActivity.class));
                getActivity().finish();
            }
        });
        builder.setView(view);
        builder.show();
    }
}