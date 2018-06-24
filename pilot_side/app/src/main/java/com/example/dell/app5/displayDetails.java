package com.example.dell.app5;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;

public class displayDetails extends AppCompatActivity implements View.OnKeyListener{
    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference newFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private EditText enterOTP;
    private Button match;
    private Button openMap;
    private TextView origin;
    private TextView dest;
    private TextView fare;

    String dvalue;
    String ovalue;
    String cust_id;
    double custlat;
    double custlon;
    String phonumNumberEmail;

    double custlatdouble;
    double custlondouble;

    private ViewGroup cardView;

    int OTPentered;

     Button yes;
     Button no;


    boolean reply_true, otp_reply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Ride Details");
        setContentView(R.layout.activity_display_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Bundle bundle = getIntent().getExtras();
        phonumNumberEmail = bundle.getString("data");
        cardView = (ViewGroup) findViewById(R.id.cancelledRequest);
        openMap=(Button)findViewById(R.id.openMap);

        Log.e("data sent", " " + phonumNumberEmail);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("share", Context.MODE_PRIVATE);
        reply_true = sharedPreferences.getBoolean("reply_true", false);
        Log.e("bool_retruend", String.valueOf(reply_true));
        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("shareotp", Context.MODE_PRIVATE);
        otp_reply = sharedPreferences2.getBoolean("otp_reply", false);
        Log.e("bool_otp_retruend", String.valueOf(otp_reply));

        if (reply_true && !otp_reply) {
            findViewById(R.id.otplayout).setVisibility(View.VISIBLE);
            findViewById(R.id.yes_no_layout).setVisibility(View.GONE);
        } else if (otp_reply && reply_true) {

            findViewById(R.id.otplayout).setVisibility(View.GONE);
            findViewById(R.id.yes_no_layout).setVisibility(View.GONE);
            findViewById(R.id.main_layout_displayDetails).setVisibility(View.VISIBLE);
            findViewById(R.id.cancelledRequest).setVisibility(View.GONE);


        }

        //  instancevar=this;

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("pilot");
        newFirebaseDatabase = mFirebaseInstance.getReference("customer");
        origin = (TextView) findViewById(R.id.origin);
        dest = (TextView) findViewById(R.id.dest);
        fare = (TextView) findViewById(R.id.fare);

        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);

        match = (Button) findViewById(R.id.match);
        enterOTP = (EditText) findViewById(R.id.enterOTP);

        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accept();
            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reject();
            }
        });

        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openMapfunction();
            }
        });

        match.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                afterMatchButton();
            }
        });
        enterOTP.setOnKeyListener(displayDetails.this);

        //  origin.setText("fine");
        mFirebaseDatabase.child(phonumNumberEmail).child("cust_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cust_id = (String) dataSnapshot.getValue();

                if (cust_id == null) {

                        findViewById(R.id.cancelledRequest).setVisibility(View.VISIBLE);
                        findViewById(R.id.yes_no_layout).setVisibility(View.GONE);
                        findViewById(R.id.otplayout).setVisibility(View.GONE);
                        findViewById(R.id.main_layout_displayDetails).setVisibility(View.GONE);

                } else {

                    findViewById(R.id.main_layout_displayDetails).setVisibility(View.VISIBLE);
                    if (reply_true && !otp_reply) {
                        findViewById(R.id.otplayout).setVisibility(View.VISIBLE);
                        findViewById(R.id.yes_no_layout).setVisibility(View.GONE);
                    } else if (otp_reply && reply_true) {

                        findViewById(R.id.otplayout).setVisibility(View.GONE);
                        findViewById(R.id.yes_no_layout).setVisibility(View.GONE);
                        findViewById(R.id.main_layout_displayDetails).setVisibility(View.VISIBLE);
                        findViewById(R.id.cancelledRequest).setVisibility(View.GONE);


                    } else {
                        findViewById(R.id.yes_no_layout).setVisibility(View.VISIBLE);
                        findViewById(R.id.cancelledRequest).setVisibility(View.GONE);

                    }


                    DatabaseReference ref1 = newFirebaseDatabase.child(cust_id).child("strAddor");
                    DatabaseReference ref2 = newFirebaseDatabase.child(cust_id).child("strAdd");
                    DatabaseReference ref3 = newFirebaseDatabase.child(cust_id).child("fare");

                    ref1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ovalue = dataSnapshot.getValue(String.class);

                            origin.setText(ovalue);
                            Log.e("origin", " " + ovalue);
                            // o= Double.parseDouble(ovalue);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    ref2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            dvalue = dataSnapshot.getValue(String.class);
                            dest.setText(dvalue);
                            Log.e("dest", " " + dvalue);
                            // d= Double.parseDouble(dvalue);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    ref3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            double farev = (double) dataSnapshot.getValue();

                            int faref = (int) farev;
                            String hg = String.valueOf(faref);

                            fare.setText(hg);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Failed to read value
                Log.w("data", "Failed to read value.", databaseError.toException());
            }
        });


        }

    private void openMapfunction() {
        mFirebaseDatabase.child(phonumNumberEmail).child("cust_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String cust_id=(String)dataSnapshot.getValue();
                if (cust_id != null) {
                    newFirebaseDatabase.child(cust_id).child("lat1").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            custlat = (double) dataSnapshot.getValue();
                            Log.e("cust", "lat " + custlat);
                           // custlatdouble=Double.parseDouble(custlat);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                    newFirebaseDatabase.child(cust_id).child("lon1").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            custlon = (double) dataSnapshot.getValue();
                            Log.e("cust", "lon " + custlon);
                           // custlondouble=Double.parseDouble(custlon);
                            String uri="http://maps.google.com/maps?daddr="+custlat+","+custlon+"(" + "customer location" + ")";
                            Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                            intent.setPackage("com.google.android.apps.maps");
                            startActivity(intent);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void afterMatchButton(){
        String enterOTPtext=enterOTP.getText().toString();
        if(TextUtils.isEmpty(enterOTPtext))
        {
            Drawable d=getResources().getDrawable(R.mipmap.ic_error);
            enterOTP.setError("Enter 4-digit OTP",d);
        }

        else{
            OTPentered = Integer.parseInt(enterOTP.getText().toString());
            matchOTP(OTPentered);
        }

    }

    private void accept() {
       stopServicefunc();
        mFirebaseDatabase.child(phonumNumberEmail).child("wait_time").setValue(0);
        mFirebaseDatabase.child(phonumNumberEmail).child("autoDecline").setValue(null);

        mFirebaseDatabase.child(phonumNumberEmail).child("cust_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cust_id= (String) dataSnapshot.getValue();
                Log.e("id", " " + cust_id);

                if(cust_id!=null) {
                    newFirebaseDatabase.child(cust_id).child("reply").setValue(true);
                    newFirebaseDatabase.child(cust_id).child("pilot_id").setValue(phonumNumberEmail);

                    findViewById(R.id.otplayout).setVisibility(View.VISIBLE);
                    findViewById(R.id.yes_no_layout).setVisibility(View.GONE);

                    Toast.makeText(getApplicationContext(), "Tracker Started", Toast.LENGTH_LONG).show();
                    Log.e("cehck", "");

                    reply_true = true;

                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("share", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("reply_true", reply_true);
                    editor.apply();

                    Intent trackerIntent = new Intent(displayDetails.this, TrackerService.class);
                    trackerIntent.putExtra("currentCust", cust_id);
                    trackerIntent.putExtra("loggedUser", phonumNumberEmail);

                    startService(trackerIntent);
                }
                     else{
                    findViewById(R.id.cancelledRequest).setVisibility(View.VISIBLE);
                    findViewById(R.id.yes_no_layout).setVisibility(View.GONE);
                    findViewById(R.id.otplayout).setVisibility(View.GONE);
                    findViewById(R.id.main_layout_displayDetails).setVisibility(View.GONE);
                    SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("share", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.clear();
                    editor.commit();

                    Intent startIntent = new Intent(displayDetails.this, TimerService.class);
                    startIntent.putExtra("loggedUser", phonumNumberEmail);
                    startService(startIntent);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void reject() {
         stopServicefunc();
        mFirebaseDatabase.child(phonumNumberEmail).child("wait_time").setValue(0);
        mFirebaseDatabase.child(phonumNumberEmail).child("autoDecline").setValue(null);
        mFirebaseDatabase.child(phonumNumberEmail).child("cust_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cust_id= (String) dataSnapshot.getValue();
                Log.e("id", " " + cust_id);

                if(cust_id!=null) {
                    newFirebaseDatabase.child(cust_id).child("reply").setValue(false);
                    newFirebaseDatabase.child(cust_id).child("pilot_id").setValue(null);
                    mFirebaseDatabase.child(phonumNumberEmail).child("cust_id").setValue(null);
                }
                else{
                    findViewById(R.id.cancelledRequest).setVisibility(View.VISIBLE);
                    findViewById(R.id.yes_no_layout).setVisibility(View.GONE);
                    findViewById(R.id.otplayout).setVisibility(View.GONE);
                    findViewById(R.id.main_layout_displayDetails).setVisibility(View.GONE);
                }
                    SharedPreferences sharedPreferences8 = getApplicationContext().getSharedPreferences("toggleON", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor8 = sharedPreferences8.edit();
                    editor8.clear();
                    editor8.commit();
                    finish();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }


    private void matchOTP(final int dataPassed) {

        mFirebaseDatabase.child(phonumNumberEmail).child("cust_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cust_id = (String) dataSnapshot.getValue();
                Log.e("id", " " + cust_id);
                newFirebaseDatabase.child(cust_id).child("otp").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        long value = (long) dataSnapshot.getValue();
                        //  Log.e("value", " " + value);
                        if (value == dataPassed) {
                            Toast.makeText(getApplicationContext(), "Match successful!Take the ride", Toast.LENGTH_SHORT).show();
                            Log.e("Match successful!", "Take the ride");
                            mFirebaseDatabase.child(phonumNumberEmail).child("otpMatched").setValue("ride_is_on");
                            newFirebaseDatabase.child(cust_id).child("otpMatched").setValue("ride_is_on");
                            newFirebaseDatabase.child(cust_id).child("pilot_arrived").setValue(null);

                            Intent trk=new Intent(displayDetails.this,TrackerService.class);
                            stopService(trk);
                             otp_reply=true;

                            SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences("shareotp", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences2.edit();
                            editor.putBoolean("otp_reply",otp_reply);
                            editor.apply();
                            Log.e("shared", String.valueOf(otp_reply));

                            finish();

                        } else {
                            Toast.makeText(getApplicationContext(), "Try Again", Toast.LENGTH_SHORT).show();
                            Log.e("Wrong customer", "Try Again");
                        }
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

        //  if((mFirebaseInstance.getReference("pilot").child(phonumNumberEmail).child("OTP").setValue(dataPassed))==)

    }

    private void stopServicefunc(){
        Intent stopIntent=new Intent(displayDetails.this,TimerService.class);
        stopService(stopIntent);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
       if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode==KeyEvent.KEYCODE_ENTER))
       {
           afterMatchButton();
           return true;
       }
        return false;
    }
}
