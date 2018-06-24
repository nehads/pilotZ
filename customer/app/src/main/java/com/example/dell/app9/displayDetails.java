package com.example.dell.app9;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class displayDetails extends AppCompatActivity {
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference newFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private TextView pilotName;
    private TextView bikeNumber;
    private TextView OTPgenerated;

    String pilot_id;
    String pname;
    long pbikeNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Ride Details");
        setContentView(R.layout.activity_display_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mFirebaseInstance = FirebaseDatabase.getInstance();

        mFirebaseDatabase = mFirebaseInstance.getReference("customer");
        newFirebaseDatabase = mFirebaseInstance.getReference("pilot");

        pilotName = (TextView) findViewById(R.id.pilotName);
        bikeNumber = (TextView) findViewById(R.id.bikeNumber);
        OTPgenerated = (TextView) findViewById(R.id.OTP);

        Bundle bundle = getIntent().getExtras();
        String loggedUser = bundle.getString("data");

        Log.e("loggedUser", "" + loggedUser);
        if (loggedUser == null) {
            Log.e("value", "null");
            ArrayList<String> array = new ArrayList<>();

            Bundle b2 = getIntent().getExtras();
            String lU = b2.getString("user");
            String customer = lU;
            Log.e("loggeduser", customer);

            assert customer != null;
            mFirebaseDatabase.child(customer).child("pilot_id").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pilot_id = (String) dataSnapshot.getValue();

                    DatabaseReference ref1 = newFirebaseDatabase.child(pilot_id).child("name");
                    DatabaseReference ref2 = newFirebaseDatabase.child(pilot_id).child("bikeNumber");

                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pname = dataSnapshot.getValue(String.class);

                            pilotName.setText(pname);
                            Log.e("pilotName", " " + pname);
                            // o= Double.parseDouble(ovalue);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pbikeNumber = (long) dataSnapshot.getValue();
                            String bNumber = String.valueOf(pbikeNumber);
                            bikeNumber.setText(bNumber);
                            Log.e("bikeNumber", " " + pbikeNumber);
                            // d= Double.parseDouble(dvalue);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            mFirebaseDatabase.child(customer).child("otp").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    long otp = (long) dataSnapshot.getValue();
                    Log.e("otp_retrieved", String.valueOf(otp));
                    String ootp = "Your OTP : ";
                    String text = ootp.toString() + String.valueOf(otp);

                    OTPgenerated.setText(text);

                    OTPgenerated.setBackgroundResource(R.drawable.textview_border);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
           // mFirebaseDatabase.child(loggedUser).child("pilot_id").addValueEventListener(new ValueEventListener()
        } else {

            mFirebaseDatabase.child(loggedUser).child("pilot_id").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    pilot_id = (String) dataSnapshot.getValue();

                    DatabaseReference ref1 = newFirebaseDatabase.child(pilot_id).child("name");
                    DatabaseReference ref2 = newFirebaseDatabase.child(pilot_id).child("bikeNumber");

                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pname = dataSnapshot.getValue(String.class);

                            pilotName.setText(pname);
                            Log.e("pilotName", " " + pname);

                            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("share3", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString("reply_true_pname",""+pname);
                            editor.apply();
                            editor.commit();
                            // o= Double.parseDouble(ovalue);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            pbikeNumber = (long) dataSnapshot.getValue();
                            String bNumber = String.valueOf(pbikeNumber);
                            bikeNumber.setText(bNumber);
                            Log.e("bikeNumber", " " + pbikeNumber);

                            SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences("share4", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences2.edit();
                            editor.putLong("reply_true_bnumber",pbikeNumber);
                            editor.apply();
                            editor.commit();
                            // d= Double.parseDouble(dvalue);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // Failed to read value
                    Log.w("data", "Failed to read value.", databaseError.toException());
                }
            });

            mFirebaseDatabase.child(loggedUser).child("otp").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    long otp = (long) dataSnapshot.getValue();
                    Log.e("otp_retrieved", String.valueOf(otp));
                    String ootp = "Your OTP : ";
                    String text = ootp.toString() + String.valueOf(otp);

                    OTPgenerated.setText(text);

                    OTPgenerated.setBackgroundResource(R.drawable.textview_border);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }
        try {
            mFirebaseDatabase.child(loggedUser).child("otpMatched").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String otpMatched=(String)dataSnapshot.getValue();
                    if(otpMatched!=null) {
                    finish();
                    Intent phoneIntent=new Intent(displayDetails.this,PhoneAuthActivity.class);
                    startActivity(phoneIntent);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            Log.e("exception",""+e.getMessage());
        }
    }

    public void track(View v)
    {
        Intent i=new Intent(this,MapsActivity2.class);
        i.putExtra("keyName",pname);
        startActivity(i);
    }


}
