package com.example.dell.app5;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.content.ContentValues.TAG;

import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.Random;

/**
 * Created by DELL on 09-Nov-17.
 */

public class TimerService  extends Service {
    public static String LOG_TAG = "TimerService";
    private IBinder mBinder = new MyBinder();
    long wait_time;
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference newFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    NotificationHelper helper;

    private Handler handler = new Handler();

    public BroadcastReceiver br;
    Handler onlineTimeHandler = new Handler();

    String cust_id;
    boolean notificationCalled=false;
    String phonumNumberEmail;

    public boolean isReqAvailable = false;
    private FirebaseAuth mAuth;

    long i;

    boolean autoDecline;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        phonumNumberEmail = intent.getStringExtra("loggedUser");

     //   Log.e("put in",phonumNumberEmail);

     //   Toast.makeText(getApplicationContext(), phonumNumberEmail, Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);


    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(LOG_TAG, "in onCreate");

        FirebaseApp.initializeApp(this);
        helper = new NotificationHelper(this);
        startTimer();
        buildNotification();

    }

    private void buildNotification() {
        String stop = "stop";
        registerReceiver(stopReceiver, new IntentFilter(stop));
        PendingIntent broadcastIntent = PendingIntent.getBroadcast(
                this, 0, new Intent(stop), PendingIntent.FLAG_UPDATE_CURRENT);
        // Create the persistent notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(getString(R.string.app_name))
                .setContentText("Tracking, tap to cancel")
                .setOngoing(true)
                .setContentIntent(broadcastIntent);
        
        startForeground(1, builder.build());
    }

    protected BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    private void startTimer() {
        onlineTimeHandler.post(updateTimerThread);

    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v(LOG_TAG, "in onBind");

        return mBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        Log.v(LOG_TAG, "in onRebind");
        super.onRebind(intent);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.v(LOG_TAG, "in onUnbind");
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.v(LOG_TAG, "in onDestroy");
        if (onlineTimeHandler != null) {
            onlineTimeHandler.removeCallbacks(updateTimerThread);
            // unregisterReceiver(br);
        }
    }

    public class MyBinder extends Binder {
        TimerService getService() {
            return TimerService.this;
        }
    }

    private Runnable updateTimerThread = new Runnable() {

        public void run() {

            i++;

            Log.e("counter:", " " + i);

            mFirebaseInstance = FirebaseDatabase.getInstance();
            mFirebaseDatabase = mFirebaseInstance.getReference("pilot");
            boolean restrictsignout = true;
            SharedPreferences sharedPreferences4 = getApplicationContext().getSharedPreferences("cannot_signout", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences4.edit();
            editor.putBoolean("signout_restricted", restrictsignout);
           // Log.e("signoutr", String.valueOf(restrictsignout));
            editor.apply();
            editor.commit();
            //  Log.e("cust_id"," "+cust_id);
            mFirebaseDatabase.child(phonumNumberEmail).child("wait_time").setValue(i);
            mFirebaseDatabase.child(phonumNumberEmail).child("otpMatched").setValue(null);

            Log.e("msg", "in null");
            Intent c = new Intent();
            c.putExtra("msg", +i);
            c.setAction("filter");
            sendBroadcast(c);

            try {
                mFirebaseDatabase.child(phonumNumberEmail).child("cust_id").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            cust_id = (String) dataSnapshot.getValue();

                            if (cust_id == null) {
                                notificationCalled = false;
                                Log.e("cust_id", "null");
                                //Toast.makeText(getApplicationContext(), "works", Toast.LENGTH_SHORT).show();
                            } else {
                                if (!notificationCalled) {
                                    Log.e("with customer", " " + cust_id);
                                    if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                                        Notification.Builder builder = helper.getPilotChannelNotification(phonumNumberEmail);
                                        helper.getManager().notify(new Random().nextInt(), builder.build());
                                    }
                                    else {
                                        notification();
                                    }
                                    notificationCalled = true;
                                } else {
                                   // Log.e("with customer", "notificationCalled");
                                }
                            }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }catch(Exception e){
                e.printStackTrace();
            }


                onlineTimeHandler.postDelayed(this, 1 * 1000);


            try {
                mFirebaseDatabase.child(phonumNumberEmail).child("autoDecline").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        try {
                            autoDecline = (boolean) dataSnapshot.getValue();
                            Log.e("autodecline", "" + autoDecline);
                            if (autoDecline) {
                                Log.e("its", "autoDeclined!");
                                notifyPilot();
                            } else {
                                Log.e("okay", "oa");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                          //  Log.e("errorDsplayDetails", "" + e.getMessage());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }catch (Exception e){
                e.printStackTrace();
               // Log.e("errorAutoD",e.getMessage());
            }


            }

        };


    private void notification() {

        Intent intent = new Intent(this, displayDetails.class);
        intent.putExtra("data", phonumNumberEmail);

        final android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(this);
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
        builder.setSound(alarmSound);
        builder.setOnlyAlertOnce(true);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setSmallIcon(R.drawable.ic_cutmypic__1_);
        }
        else{
            builder.setSmallIcon(R.drawable.signin_logo);
        }
        builder.setContentTitle("PilotZai");
        builder.setContentText("New customer request!");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Gets an instance of the NotificationManager service
        builder.setAutoCancel(true);
        // Builds the notification and issues it.
        mNotifyMgr.notify(2, builder.build());

    }

    private void notifyPilot() {

        Intent nserviceIntent = new Intent(TimerService.this, TimerService.class);
                unregisterReceiver(stopReceiver);
                stopService(nserviceIntent);

        final android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(this);
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setSmallIcon(R.drawable.ic_cutmypic__1_);
        }
        else{
            builder.setSmallIcon(R.drawable.signin_logo);
        }
        builder.setContentTitle("PilotZai");
        builder.setContentText("Time Out!");
        builder.setContentInfo("Its AutoDeclined");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Gets an instance of the NotificationManager service
        builder.setAutoCancel(true);
        // Builds the notification and issues it.
        mNotifyMgr.notify(2, builder.build());
        Intent startIntent=new Intent(TimerService.this,TimerService.class);
        startIntent.putExtra("loggedUser", phonumNumberEmail);
      //  Log.e("lgg put in",phonumNumberEmail);
        mFirebaseDatabase.child(phonumNumberEmail).child("autoDecline").setValue(null);
        startService(startIntent);
    }
    }





