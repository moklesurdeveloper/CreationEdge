package com.creationedge.android.view.activitys;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.creationedge.android.R;
import com.creationedge.android.view.fragmentDialog.FragmentDia_Live;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zagori.bottomnavbar.BottomNavBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.pm.Signature;

public class HomeActivity extends AppCompatActivity {

    private FloatingActionButton live_bottom_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
    /*    AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_categoris,R.id.navigation_search, R.id.navigation_cart,R.id.navigation_account)
                .build();*/
      //  Toolbar toolbar = findViewById(R.id.toolbar);
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
      //  NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        live_bottom_img = findViewById(R.id.live_bottom_img);
        live_bottom_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentDia_Live fragmentDia_live = new FragmentDia_Live();
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                fragmentDia_live.show(ft,"TAG");
            }
        });


        /**
         * jks:creationedge.jks
         * path:I:\AndroidProject\jks
         * password:ZamZam@6767
         * alis:creation
         */

    }



}