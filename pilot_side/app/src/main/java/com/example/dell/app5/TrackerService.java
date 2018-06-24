package com.example.dell.app5;

import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class TrackerService extends Service {
    private static final String TAG = TrackerService.class.getSimpleName();
    FusedLocationProviderClient client;

    String phonumNumberEmail;
    String currentcust;
    String path;
    double cust_lat;

    String otp_matched;
    String lat1, lon1;
    double lat = 0, lon = 0;

    DatabaseReference ref;
    DatabaseReference cust_ref;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        phonumNumberEmail=intent.getStringExtra("loggedUser");
        currentcust=intent.getStringExtra("currentCust");

        Toast.makeText(getApplicationContext(), phonumNumberEmail, Toast.LENGTH_LONG).show();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.e("break", "");

        client = LocationServices.getFusedLocationProviderClient(this);

        requestLocationUpdates();
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

    public BroadcastReceiver stopReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d(TAG, "received stop broadcast");
            unregisterReceiver(stopReceiver);
            stopSelf();
        }
    };

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    private void requestLocationUpdates() {

        final LocationRequest request = new LocationRequest();
        request.setInterval(1000);
        request.setFastestInterval(5000);
        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        path = "pilot" + "/" + "test";

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        client.requestLocationUpdates(request, new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {



                 ref = FirebaseDatabase.getInstance().getReference("pilot");
                 cust_ref = FirebaseDatabase.getInstance().getReference("customer");
                Location location = locationResult.getLastLocation();
                lat = location.getLatitude();
                lon = location.getLongitude();
                lat1 = Double.toString(lat);
                lon1 = Double.toString(lon);

                Log.e("point", "");

                ref.child(phonumNumberEmail).child("otpMatched").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                         otp_matched=(String)dataSnapshot.getValue();
                        Log.e("trkservice","cust_id " +otp_matched);
                        if(otp_matched== null){
                            ref.child(phonumNumberEmail).child("latitude").setValue(lat1);
                            ref.child(phonumNumberEmail).child("longitude").setValue(lon1);
                            Log.d("trkser", "uploading");
                            trackme();
                         //  Toast.makeText(getApplicationContext(), "in if", Toast.LENGTH_LONG).show();
                        }
                        else{
                            Log.d("in not uploading","trkser");
                          //  Toast.makeText(getApplicationContext(), "not uploading data", Toast.LENGTH_LONG).show();
                            //unregisterReceiver(stopReceiver);
                            remove();
                            //  stopReceiver.onReceive(TrackerService.this,nserviceIntent);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });



            }

            private void remove() {
                Intent nserviceIntent = new Intent(TrackerService.this, TrackerService.class);
                client.removeLocationUpdates(this);
                stopService(nserviceIntent);
            }
        }, null);

    }

    private void trackme() {
        Log.d("cust_lat","check "+cust_lat);
        cust_ref.child(currentcust).child("lat1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cust_lat=(double)dataSnapshot.getValue();
                Log.d("cust_lat","trkser "+cust_lat);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        cust_ref.child(currentcust).child("lon1").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double cust_lon=(double)dataSnapshot.getValue();
                Log.d("cust_lon","trkser "+cust_lon);

                //double dcust_lat=Double.parseDouble(cust_lat);
                //double dcust_lon=Double.parseDouble(cust_lon);
                Log.d("cust_lon double","trkser "+cust_lat);
                Log.d("cust_lon double","trkser "+cust_lon);

                Location pilot=new Location("pilot");
                pilot.setLatitude(lat);
                pilot.setLongitude(lon);

                Location customer=new Location("customer");
                customer.setLatitude(cust_lat);
                customer.setLongitude(cust_lon);

                float distanceinMeters=pilot.distanceTo(customer);
                Log.d("distance meters","trkser "+distanceinMeters);

                if(distanceinMeters<=150){
                    cust_ref.child(currentcust).child("pilot_arrived").setValue(true);

                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
