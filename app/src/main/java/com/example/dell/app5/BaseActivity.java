package com.example.dell.app5;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class BaseActivity extends AppCompatActivity {

    @VisibleForTesting

    public ProgressDialog mProgressDialog;


    Drawable d;

    public void showProgressDialog() {

        if (mProgressDialog == null) {

         //   getCurrentFocus().setBackground(getDrawable(R.color.border_color));

            mProgressDialog = new ProgressDialog(this);

            mProgressDialog.setMessage(getString(R.string.loading));

            mProgressDialog.setIndeterminate(true);

            mProgressDialog.setCancelable(false);

            d=getDrawable(R.drawable.my_progress_bar);

            mProgressDialog.setIndeterminateDrawable(d);

        }



        mProgressDialog.show();

    }



    public void hideProgressDialog() {

        if (mProgressDialog != null && mProgressDialog.isShowing()) {

            mProgressDialog.dismiss();

        }

    }



    @Override

    public void onStop() {

        super.onStop();

        hideProgressDialog();

    }



}