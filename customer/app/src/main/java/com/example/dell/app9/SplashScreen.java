package com.example.dell.app9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    boolean requestPressed=false;
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread timerThread = new Thread()
        {
            public void run()
            {
                try
                {
                    sleep(2000);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                finally {
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    Log.e("current user",""+currentUser);

                    if(currentUser==null){
                        Intent intent=new Intent(SplashScreen.this,PhoneAuthActivity.class);
                        startActivity(intent);
                    }
                    else {
                        SharedPreferences sharedPreferences5 = getApplicationContext().getSharedPreferences("RequestPressed", Context.MODE_PRIVATE);
                        requestPressed = sharedPreferences5.getBoolean("customer_request_sent", false);
                        Log.e("request_sent", "" + requestPressed);
                        if (requestPressed) {
                            Intent intent = new Intent(SplashScreen.this, PhoneAuthActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(SplashScreen.this, BeforeRequestMapsActivity.class);
                            startActivity(intent);
                        }
                    }

                }
            }
        };
        timerThread.start();
    }
    protected  void  onPause()
    {
        super.onPause();
        finish();
    }
}
