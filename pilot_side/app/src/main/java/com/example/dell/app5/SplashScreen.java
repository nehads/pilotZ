package com.example.dell.app5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

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

                    Intent intent=new Intent(SplashScreen.this,EmailPasswordActivity.class);
                    startActivity(intent);
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
