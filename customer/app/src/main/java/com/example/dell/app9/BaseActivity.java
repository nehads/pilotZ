package com.example.dell.app9;

import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class BaseActivity extends AppCompatActivity {

    @VisibleForTesting

    public ProgressDialog mProgressDialog;
    Drawable d;

    public void showProgressDialog() {

        if (mProgressDialog == null) {

            mProgressDialog = new ProgressDialog(this);

            mProgressDialog.setMessage(getString(R.string.loading));

            mProgressDialog.setIndeterminate(true);

            mProgressDialog.setCancelable(false);
//
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