package com.creationedge.android.view.activitys;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.creationedge.android.R;
import com.creationedge.android.utils.DetectConnection;
import com.creationedge.android.view.fragmentDialog.CategoriesProductListFragment;
import com.creationedge.android.view.fragmentDialog.SignInFragment;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                ,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        getWindow().setStatusBarColor(Color.TRANSPARENT);
        setContentView(R.layout.activity_splash);

        printKeyHash();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!DetectConnection.checkInternetConnection(SplashActivity.this)) {
                    AlertDialogConnect();
                }else {
                    startActivity(new Intent(SplashActivity.this, HomeActivity.class));
                    finish();
                }
            }
        },1000);
    }

    private void gotoSignInPage(){
        SignInFragment purchaseHistoryFragment = new SignInFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        purchaseHistoryFragment.show(ft,"Tag");
    }

    private void AlertDialogConnect() {
        // Custom Alert Dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.row_iternet_connection_checking_layout, null);

        ImageButton imageButton = view.findViewById(R.id.logoutImg);
        TextView title = view.findViewById(R.id.titleText);
        title.setText("Please check your internet Connection and try again!");
        imageButton.setImageResource(R.drawable.ic_baseline_wifi_off_24);
        builder.setCancelable(false);

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setPositiveButton("Go to Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
                finish();
            }
        });
        builder.setView(view);
        builder.show();
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.foodgulliver.reviewapp", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }
}