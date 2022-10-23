package com.creationedge.android.common;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.valdesekamdem.library.mdtoast.MDToast;

import java.text.DecimalFormat;

public class Method {
    public void showToastMessage(String message, int type, Context context) {
        MDToast.makeText(context, message, MDToast.LENGTH_SHORT, type).show();

    }

    public void productOffPersent(String price, String previousPrice, TextView view, ConstraintLayout v) {

        if (previousPrice.equals("")) {

        } else {
            //set discount
            double priceSub = Double.parseDouble(price.substring(1));
            double previous_priceSub = Double.parseDouble(previousPrice.substring(1));
            v.setVisibility(View.VISIBLE);
            double dis_div = (previous_priceSub - priceSub);
            double multiply = dis_div * 100;
            double discount = (multiply / previous_priceSub);
            DecimalFormat dec = new DecimalFormat("#");
            view.setText(" -" + dec.format(discount) + "%");
        }


    }

    public void productOffPersent(String price, String previousPrice, TextView view) {

        if (previousPrice.equals("৳0")) {
            view.setVisibility(View.GONE);
        } else {
            //set discount
            double priceSub = Double.parseDouble(price);
            double previous_priceSub = Double.parseDouble(previousPrice);
            double dis_div = (previous_priceSub - priceSub);
            double multiply = dis_div * 100;
            double discount = (multiply / previous_priceSub);
            DecimalFormat dec = new DecimalFormat("#");
            view.setText(" -" + dec.format(discount) + "%");
        }
    }
}
