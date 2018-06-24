package com.example.dell.app5;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.CountDownTimer;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.NonNull;

import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;

import android.util.Log;

import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;

import android.widget.ImageView;
import android.widget.TextView;

import android.widget.Toast;
import android.widget.ToggleButton;


import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;
import java.util.Timer;
import java.util.TimerTask;

import com.google.firebase.database.DatabaseError;

public class EmailPasswordActivity extends BaseActivity implements View.OnClickListener ,View.OnKeyListener, AboutUsFragment.OnFragmentInteractionListener{

    private static final String serviceTAG = TrackerService.class.getSimpleName();
    PowerManager.WakeLock wl;
    private static final String TAG = "EmailPassword";

    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference newFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    // private EditText ed1;

    AlertDialog.Builder builder;

    private Handler alerthandler = new Handler();
    private Handler positionhandler = new Handler();
    private Button getHighest;
    private TextView mStatusTextView;
    private TextView timer_textview;

    String cust_id;
    String names;
    String thispilot;

    private TextView mDetailTextView;

    boolean isMyTimerRunningvar=false;
    private ViewGroup tickGroup;
    private ViewGroup end_journey_layout;

    ArrayList<String> pilotnames=new ArrayList<>();
    ArrayList<Long> sortedlist=new ArrayList<>();
    int indexOfPilot;
    private EditText mEmailField;

    private EditText mPasswordField;

    private Button rideDetails;
    private Button endJourney;
    String currentPilot;
    private Button yes;
    private Button no;
    int totalpilots=0;
    int maxpilots;
    private ToggleButton toggle;
    int length;
    String temppilots;

    static final int MY_PERMISSIONS_REQUEST = 123;

    ArrayList<String> list=new ArrayList<>();
    ArrayList<Long> allwaittimes=new ArrayList<>();
    ArrayList<Long> allwaittimes2=new ArrayList<>();
    boolean reply_true;
    String currentpilotHub;
    String thispilotname;
    String phonumNumberEmail;
    long wait_time;
    int viewId;
    private Button start;
    private Button stop;
    private EditText enterOTP;
    int countOfShowPosition=0;
    private Button match;
    int OTPentered;
    private String userId;
    String id;
    int checkcount=0;
    int maximum;
    int positionCounter;
    String printpositionText="";
    boolean isMenuNeeded=false;
    boolean toggleValue=false;

    static EmailPasswordActivity emailInstance;

    // [START declare_auth]

    private FirebaseAuth mAuth;

    // [END declare_auth]

    private MyCountDown timer;

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_email_password);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ArrayList<String> arrPerm = new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            arrPerm.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            arrPerm.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if (!arrPerm.isEmpty()) {
                String[] permissions = new String[arrPerm.size()];
                permissions = arrPerm.toArray(permissions);
                ActivityCompat.requestPermissions(EmailPasswordActivity.this, permissions, MY_PERMISSIONS_REQUEST);
            }

            //return TODO;
        }
        LocationManager manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean GPSstatus=manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!GPSstatus){
            Snackbar snackbar=Snackbar.make(findViewById(android.R.id.content),"Please enable GPS to Start !",Snackbar.LENGTH_LONG);
           // Toast toast=Toast.makeText(getApplicationContext(),"Please enable GPS to Start !",Toast.LENGTH_LONG);
            View view=snackbar.getView();
            Drawable d=getDrawable(R.color.black);
          //  view.setBackgroundResource(R.drawable.toast);

            view.setBackground(d);

            snackbar.show();
        }

        emailInstance = this;


        mStatusTextView = (TextView) findViewById(R.id.status);

      //  mDetailTextView = (TextView) findViewById(R.id.detail);

        mEmailField = (EditText) findViewById(R.id.field_uname);

        mPasswordField = (EditText) findViewById(R.id.field_password);

        mPasswordField.setOnKeyListener(this);

        tickGroup=(ViewGroup) findViewById(R.id.tick_layout);
        end_journey_layout=(ViewGroup) findViewById(R.id.end_journey_layout);

        findViewById(R.id.email_sign_in_button).setOnClickListener(this);

        mFirebaseInstance = FirebaseDatabase.getInstance();
        final Intent d = new Intent(EmailPasswordActivity.this, TimerService.class);


        mFirebaseDatabase = mFirebaseInstance.getReference("pilot");
        newFirebaseDatabase = mFirebaseInstance.getReference("customer");

        rideDetails = (Button) findViewById(R.id.rideDetails);
        endJourney = (Button) findViewById(R.id.endJourney);

        rideDetails.setOnClickListener(this);
        endJourney.setOnClickListener(this);

        match = (Button) findViewById(R.id.match);
        yes = (Button) findViewById(R.id.yes);
        no = (Button) findViewById(R.id.no);
        timer_textview = (TextView) findViewById(R.id.timer);
        enterOTP = (EditText) findViewById(R.id.enterOTP);

        toggle=(ToggleButton)findViewById(R.id.toggleBtn);

        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("toggleON",Context.MODE_PRIVATE);
        toggleValue=sharedPreferences.getBoolean("toggleValue",false);
        Log.e("toggleValue", String.valueOf(toggleValue));
        if(toggleValue){
            toggle.setChecked(true);
        }
        else
        {
            toggle.setChecked(false);
        }

        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Toast.makeText(getApplicationContext(),"its ON",Toast.LENGTH_SHORT).show();
                    setToggle();
                    onStartFunction();
                }
                else{
                    Toast.makeText(getApplicationContext(),"its OFF",Toast.LENGTH_SHORT).show();
                    clearToggle();
                    toggle.setChecked(false);
                    onStopFunction();
                }
            }
        });


        // [START initialize_auth]

        mAuth = FirebaseAuth.getInstance();

        // [END initialize_auth]

    }


    private void onStartFunction() {
        Intent serviceIntent = new Intent(EmailPasswordActivity.this, TimerService.class);
        timer_textview.setText(R.string.calculating);

        LocationManager manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean GPSstatus=manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!GPSstatus){
            Snackbar snackbar= Snackbar.make(findViewById(android.R.id.content),"Please enable GPS to Start !",Snackbar.LENGTH_LONG);
            clearToggle();
            toggle.setChecked(false);
            View view=snackbar.getView();
            view.setBackgroundResource(R.drawable.snackbar);
            snackbar.show();

        }
        else {
            Toast.makeText(getApplicationContext(), "Timer Started", Toast.LENGTH_SHORT).show();
            serviceIntent.putExtra("loggedUser", phonumNumberEmail);
            startService(serviceIntent);
        }
    }


    private void onStopFunction() {
        final Intent serviceIntent = new Intent(EmailPasswordActivity.this, TimerService.class);
        timer_textview.setText(R.string.timer);
        printpositionText="";
        if (positionhandler != null) {
            positionhandler.removeCallbacks(showPosition);
            // unregisterReceiver(br);
        }

        mFirebaseDatabase.child(phonumNumberEmail).child("cust_id").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cust_id = (String) dataSnapshot.getValue();
                Log.e("id", " " + cust_id);

                if (cust_id == null) {

                    mFirebaseDatabase.child(phonumNumberEmail).child("wait_time").setValue(0);
                    SharedPreferences sharedPreferences4=getApplicationContext().getSharedPreferences("cannot_signout",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3=sharedPreferences4.edit();
                    editor3.clear();
                    editor3.commit();

                    stopService(serviceIntent);
                } else {
                    newFirebaseDatabase.child(cust_id).child("reply").setValue(false);
                    newFirebaseDatabase.child(cust_id).child("pilot_id").setValue("searching");
                    mFirebaseDatabase.child(phonumNumberEmail).child("cust_id").setValue(null);
                    mFirebaseDatabase.child(phonumNumberEmail).child("wait_time").setValue(0);
                    SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("share", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    SharedPreferences preferences=getApplicationContext().getSharedPreferences("shareotp",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2=preferences.edit();
                    editor2.clear();
                    editor2.commit();
                    SharedPreferences sharedPreferences4=getApplicationContext().getSharedPreferences("cannot_signout",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor3=sharedPreferences4.edit();
                    editor3.clear();
                    editor3.commit();

                    Intent rejectedIntent = new Intent(EmailPasswordActivity.this, TrackerService.class);

                    rejectedIntent.putExtra("loggedUser", phonumNumberEmail);
                    stopService(rejectedIntent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    // [START on_start_check_user]

    @Override

    public void onStart() {

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();
        findViewById(R.id.tick_layout).setVisibility(View.GONE);

        isMenuNeeded=true;
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("share",Context.MODE_PRIVATE);
        reply_true=sharedPreferences.getBoolean("reply_true",false);
        Log.e("bool_retruend4", String.valueOf(reply_true));

        updateUI(currentUser);

    }

    // [END on_start_check_user]


    //Sign-In
    private void signIn(String email, String password) {

        Log.d(TAG, "signIn:" + email);

        if (!validateForm()) {

            return;

        }

        showProgressDialog();


        // [START sign_in_with_email]

        mAuth.signInWithEmailAndPassword(email, password)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override


                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();

                          //  onCreateOptionsMenu(createMenu());

                            invalidateOptionsMenu();

                            isMenuNeeded=true;

                            findViewById(R.id.tick_layout).setVisibility(View.VISIBLE);

                             timer = new MyCountDown(3000, 1000);



                            updateUI(user);

                        } else {

                            // If sign in fails, display a message to the user.

                            Log.w(TAG, "signInWithEmail:failure", task.getException());

                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",

                                    Toast.LENGTH_SHORT).show();

                            updateUI(null);

                        }


                        // [START_EXCLUDE]

                        if (!task.isSuccessful()) {

                            mStatusTextView.setText(R.string.auth_failed);

                        }

                        hideProgressDialog();

                        // [END_EXCLUDE]

                    }

                });

        // [END sign_in_with_email]

    }


    //Sign-Out
    private void signOut() {

        mAuth.signOut();

        SharedPreferences preferences=getSharedPreferences("share",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.clear();
        editor.commit();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.hide();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window=getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.appbackground));
        }
        updateUI(null);

    }


    private boolean isMyTimerRunning(Class<?> TimerServiceClass){
        ActivityManager manager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for(ActivityManager.RunningServiceInfo service:manager.getRunningServices(Integer.MAX_VALUE)){
            if(TimerServiceClass.getName().equals(service.service.getClassName())){
                isMyTimerRunningvar=true;
                Log.e("timer running", String.valueOf(isMyTimerRunningvar));
                return true;
            }
        }
        isMyTimerRunningvar=false;
        return false;
    }


    BroadcastReceiver br = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent c) {
            long h = (long) c.getExtras().get("msg");
            Log.e("extra", " " + h);
          //  timer_textview.setText(String.valueOf(h));
            if(String.valueOf(h)!=null){
                positionhandler.post(showPosition);
            }

            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("share",Context.MODE_PRIVATE);
            reply_true=sharedPreferences.getBoolean("reply_true",false);
            Log.e("bool_retruend2", String.valueOf(reply_true));
            FirebaseUser user = mAuth.getCurrentUser();
            updateUI(user);

        }
    };


    private Runnable showPosition=new Runnable() {
        @Override
        public void run() {
            countOfShowPosition++;
            //Log.e("logging",""+countOfShowPosition);

            if(countOfShowPosition % 10 ==0){
                Log.e("multiple","of 10");
                mFirebaseDatabase.child(phonumNumberEmail).child("currentHub").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        currentpilotHub=(String)dataSnapshot.getValue();
                        Log.e("pilothub","is "+currentpilotHub);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                getallValues();
            }

            positionhandler.postDelayed(this,10000);
        }
    };


    private void getallValues() {
        mFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()) {
                    final String allpilots = (String) ds.getKey();
                  //  Log.e("new list", "names" + allpilots);
                    if (allpilots.equals(phonumNumberEmail)) {
                     //   Log.e("thispilot", "loggedName " + allpilots);
                        thispilotname = allpilots;
                    }
                    //list.clear();
                    mFirebaseDatabase.child(allpilots).child("currentHub").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String hb = (String) dataSnapshot.getValue();
                           // Log.e("new list", "hubs" + hb);
                           // Log.e("new namlist", "names " + allpilots);
                            if (currentpilotHub.equals(hb)) {
                             //   Log.e("new list", "currenthub " + hb);
                               // Log.e("new namlist", "catezdnames " + allpilots);
                                if (list.isEmpty()) {
                                    Log.e("list", "is empty");
                                } else {
                                   // Log.e("list", "not empty");
                                }

                                if (list.contains(allpilots)) {
                                    Log.e("info:", "already_present");
                                } else {
                                    list.add(allpilots);
                                    temppilots = allpilots;

                                    length = list.size();
                                    Log.e("thisplot", "length " + length);
                                  //  Log.e("check", "countercheck" + checkcount);
                                    ++checkcount;
                                    final int maxval=checkcount;


                                    Log.e("thisplot", "check " + list);
                                   // Log.e("list", "checklist" + allpilots);
                                    mFirebaseDatabase.child(allpilots).child("wait_time").addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            long varwaitt = (long) dataSnapshot.getValue();
                                         //   Log.e("varwait", "time " + varwaitt);

                                            if (allwaittimes.size() < length) {
                                                allwaittimes.add(varwaitt);

                                                Log.e("list", "waitlist" + allwaittimes);
                                            } else {
                                              //  Log.e("im", "out");
                                            }

                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                    if(allwaittimes.isEmpty()) {
                        Log.e("error","in printvalues");
                        list.clear();
                       // Log.e("check2","funcplot2 "+list);
                        allwaittimes.clear();
                        sortedlist.clear();
                        totalpilots=0;
                        maxpilots=0;

                    }
                    else{
                        Log.e("i m","here");
                        printValues();
                        totalpilots=0;
                        maxpilots=0;
                    }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    //retrievs wait time to check if both arrays equal[retrieved bfore and now] and calls showPilotPosition
    private void printValues() {
        Log.e("thispilot","name "+thispilotname);
        Log.e("resphub","pilotlist "+list);
        indexOfPilot = list.indexOf(thispilotname);
        Log.e("this pilot", "index" + indexOfPilot);


        for(int i=0;i<list.size();i++){
           String temp=list.get(i);
            Log.e("list","from loop "+temp);
            mFirebaseDatabase.child(temp).child("wait_time").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    long time=(long)dataSnapshot.getValue();
                 //   Log.e("wait","update"+time);
                    allwaittimes2.add(time);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        Log.e("resphub","waittimes "+allwaittimes);
       Log.e("resphub2","waittimes2 "+allwaittimes2);
       if(thispilotname!=null && (!allwaittimes2.isEmpty())) {
           if(allwaittimes.equals(allwaittimes2)){
               Log.e("both","equal "+allwaittimes2);
               showPilotPosition();
           }
           else{
               Log.e("both","mismatch");
               list.clear();
               allwaittimes.clear();
               allwaittimes2.clear();
               sortedlist.clear();
           }
       // if(thispilotname!=null){
         //  Log.e("resphubch","waittimes2 "+allwaittimes2);

        }
        else{
            Log.e("error","func printvalues");
            list.clear();
           // Log.e("check3","funcplot3 "+list);
            allwaittimes.clear();
            allwaittimes2.clear();
            sortedlist.clear();
        }
    }



    private void showPilotPosition() {

            list.clear();

            try{
                if(indexOfPilot>=0){

            long currentPilotWaittime = allwaittimes2.get(indexOfPilot);
            Log.e("this pilot", "wait_time" + currentPilotWaittime);

            Collections.sort(allwaittimes2,Collections.<Long>reverseOrder());
            Log.e("sort","result "+allwaittimes2);

            for (long asc : allwaittimes2) {
                System.out.println(asc);
                sortedlist.add(asc);
            }
            Log.e("sortedlist", String.valueOf(sortedlist));

            for(long element:sortedlist){
                if(currentPilotWaittime==element){
                    int iindex=sortedlist.indexOf(element);
                    Log.e("Your position:",""+iindex);
                    positionCounter=iindex+1;
                    String positionText="Your Position :";

                    String position= String.valueOf(positionCounter);
                    if(positionCounter==1){
                        Log.e("Your position:","next");
                        printpositionText=positionText+ "Next";
                        timer_textview.setText(printpositionText);
                    }else {
                        printpositionText = positionText +position;
                        timer_textview.setText(printpositionText);
                    }
                }
            }
                }
                list.clear();
                allwaittimes2.clear();
                sortedlist.clear();
            }
            catch (Exception e){
                e.printStackTrace();
                list.clear();
                allwaittimes2.clear();
                sortedlist.clear();
            }
        list.clear();
       // Log.e("check4","funcplot4 "+list);
        allwaittimes.clear();
        allwaittimes2.clear();
        sortedlist.clear();

    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.e("log", "onResume");
        isMyTimerRunning(TimerService.class);
        if(isMyTimerRunningvar) {
            timer_textview.setText(R.string.calculating);
        }
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("share",Context.MODE_PRIVATE);
        reply_true=sharedPreferences.getBoolean("reply_true",false);
        Log.e("bool_retruend", String.valueOf(reply_true));

        IntentFilter filt = new IntentFilter("filter");
        this.registerReceiver(br, filt);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private class MyCountDown extends CountDownTimer{
        public MyCountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            start();
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            tickGroup.setVisibility(View.GONE);
        }
    }


    private boolean validateForm() {

        boolean valid = true;


        String email = mEmailField.getText().toString();

        if (TextUtils.isEmpty(email)) {

            mEmailField.setError("Required.");

            valid = false;

        } else {

            mEmailField.setError(null);

        }


        String password = mPasswordField.getText().toString();

        if (TextUtils.isEmpty(password)) {

            mPasswordField.setError("Required.");

            valid = false;

        } else {

            mPasswordField.setError(null);

        }


        return valid;

    }

    private String usernameFromEmail(String email) {

        if (email.contains("@")) {

            //  phoneNumberEmail=email;

            return email.split("@")[0];


        } else {

            return email;

        }

    }



    private void updateUI( FirebaseUser user) {

        hideProgressDialog();

        if (user != null) {

            isMenuNeeded = true;

            ActionBar actionBar = getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.show();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Window window = getWindow();
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            phonumNumberEmail = usernameFromEmail(user.getEmail());

            findViewById(R.id.email_password_fields).setVisibility(View.GONE);
            findViewById(R.id.logo).setVisibility(View.GONE);

            if (reply_true) {
                findViewById(R.id.end_journey_layout).setVisibility(View.VISIBLE);
                clearToggle();
                findViewById(R.id.start_stop_layout).setVisibility(View.GONE);

            }

         else {
                mStatusTextView.setText(getString(R.string.emailpassword_status_fmt,

                        user.getEmail()));

              //  mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));


                id = user.getUid();

                String loggeduser = usernameFromEmail(user.getEmail());
                Log.e("data sent", " " + loggeduser);

                findViewById(R.id.end_journey_layout).setVisibility(View.GONE);

                findViewById(R.id.start_stop_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.tempdetail_layout).setVisibility(View.VISIBLE);

            }

        } else {

            ActionBar actionBar=getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.hide();
            supportInvalidateOptionsMenu();

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                Window window=getWindow();
                window.setStatusBarColor(getResources().getColor(R.color.appbackground));
            }


          //  mDetailTextView.setText(null);

            mEmailField.setText(null);

            mPasswordField.setText(null);

            findViewById(R.id.start_stop_layout).setVisibility(View.GONE);
            findViewById(R.id.tempdetail_layout).setVisibility(View.GONE);
            findViewById(R.id.end_journey_layout).setVisibility(View.GONE);

            findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
            findViewById(R.id.logo).setVisibility(View.VISIBLE);

            isMenuNeeded=false;
            invalidateOptionsMenu();


        }

    }


    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(br);
    }

    @Override

    public void onClick(View v) {

        int i = v.getId();

//        if (i == R.id.email_create_account_button) {
//
//            startActivity(new Intent(this, SignInActivity.class));
//
//            finish();
//

        //   createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString(),ed1.getText().toString());

        if (i == R.id.email_sign_in_button) {

            startButtonFunction();



        }

        else if(i== R.id.rideDetails){
            rideDetails();
        }

        else if(i==R.id.endJourney){
            endPilotJourney();
        }
//        else if (i == R.id.sign_out_button) {
//
//            signOut();
//
//        }
//        else if (i == R.id.verify_email_button) {
//
//            sendEmailVerification();
//
//        }

    }


    private void startButtonFunction() {
        signIn(mEmailField.getText().toString() + "@pilotzai.com", mPasswordField.getText().toString());
    }



    private void endPilotJourney() {
        boolean otp_reply;
        SharedPreferences sharedPreferences4=getApplicationContext().getSharedPreferences("shareotp",Context.MODE_PRIVATE);
        otp_reply=sharedPreferences4.getBoolean("otp_reply",false);
        Log.e("otp_reply", String.valueOf(otp_reply));
        if(!otp_reply) {
        Toast.makeText(getApplicationContext(),"OTP has not been matched",Toast.LENGTH_SHORT).show();
        }
        else {
            mFirebaseDatabase.child(phonumNumberEmail).child("cust_id").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    cust_id = (String) dataSnapshot.getValue();
                    Log.e("id", " " + cust_id);
                    newFirebaseDatabase.child(cust_id).child("reply").setValue(false);
                    newFirebaseDatabase.child(cust_id).child("pilot_id").setValue("searching");
                    mFirebaseDatabase.child(phonumNumberEmail).child("cust_id").setValue(null);
                    mFirebaseDatabase.child(phonumNumberEmail).child("wait_time").setValue(0);
                    mFirebaseDatabase.child(phonumNumberEmail).child("otpMatched").setValue(null);
                    newFirebaseDatabase.child(cust_id).child("otpMatched").setValue(null);
                    reply_true = false;
                    SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("share", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.commit();
                    SharedPreferences preferences = getApplicationContext().getSharedPreferences("shareotp", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = preferences.edit();
                    editor2.clear();
                    editor2.commit();
                    findViewById(R.id.start_stop_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.tempdetail_layout).setVisibility(View.VISIBLE);
                    findViewById(R.id.end_journey_layout).setVisibility(View.GONE);

                    setToggle();
                    toggle.setChecked(true);

                    Intent startIntent = new Intent(EmailPasswordActivity.this, TimerService.class);
                    startIntent.putExtra("loggedUser", phonumNumberEmail);
                    startService(startIntent);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }


    //for signout menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.signout_menu,menu);
        menu.setGroupVisible(R.id.menu_group,isMenuNeeded);
       // menu.setGroupVisible(R.id.ride_details,isReqAvailable);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //item.setVisible(isMenuNeeded);
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                boolean restrictedsignout;
                SharedPreferences sharedPreferences4=getApplicationContext().getSharedPreferences("cannot_signout",Context.MODE_PRIVATE);
                restrictedsignout=sharedPreferences4.getBoolean("signout_restricted",false);
                Log.e("signout_restricted", String.valueOf(restrictedsignout));

                if(restrictedsignout){

                    builder=new AlertDialog.Builder(this);
                    builder.setMessage("Press End to SignOut");
                    final AlertDialog alertDialog=builder.create();
                    alertDialog.setTitle("SignOut Error !");
                    alertDialog.show();

                    alerthandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            alertDialog.dismiss();
                        }
                    },1*4000);
                }
                else {
                    signOut();
                }
                break;

            case R.id.about_us_menu:

                callfragment();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    private void callfragment(){
//        Log.e("im ","here");
        AboutUsFragment br=new AboutUsFragment();
        FragmentManager mg=getSupportFragmentManager();
        FragmentTransaction ft=mg.beginTransaction();
         //findViewById(R.id.start_stop_layout).setVisibility(View.GONE);
        ft.addToBackStack("Fragment");
      //  findViewById(R.id.fragment_layout).setVisibility(View.VISIBLE);
        ft.replace(R.id.temp,br);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount()>0){
            getFragmentManager().popBackStack();
        }else {
            super.onBackPressed();
        }
    }

    private void rideDetails() {
        Intent intent = new Intent(this, displayDetails.class);
        intent.putExtra("data", phonumNumberEmail);
        startActivity(intent);
    }


    //clears the Timer On ToggleValue
    private void clearToggle(){
        SharedPreferences sharedPreferences8=getApplicationContext().getSharedPreferences("toggleON", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor8=sharedPreferences8.edit();
        editor8.clear();
        editor8.commit();

    }


    //sets the Toggle On value
    private void setToggle(){
        toggleValue=true;
        SharedPreferences sharedPreferences8=getApplicationContext().getSharedPreferences("toggleON", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor8=sharedPreferences8.edit();
        editor8.putBoolean("toggleValue",toggleValue);
        editor8.apply();

        boolean restrictsignout = true;
        SharedPreferences sharedPreferences4 = getApplicationContext().getSharedPreferences("cannot_signout", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences4.edit();
        editor.putBoolean("signout_restricted", restrictsignout);
        Log.e("signoutr", String.valueOf(restrictsignout));
        editor.apply();
        editor.commit();
    }



    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if((event.getAction()==KeyEvent.ACTION_DOWN) && (keyCode==KeyEvent.KEYCODE_ENTER))
        {
            Log.e("key","enter");
            startButtonFunction();
            return true;
        }
        return false;
    }

}