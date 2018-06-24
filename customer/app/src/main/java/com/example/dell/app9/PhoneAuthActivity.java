package com.example.dell.app9;

import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.Locale;

public class PhoneAuthActivity extends BaseActivity  implements View.OnClickListener,View.OnKeyListener, AboutFragment.OnFragmentInteractionListener{

    private static final String TAG = "PhoneAuthActivity";
    static final int MY_PERMISSIONS_REQUEST=123;
    private static final String KEY_VERIFY_IN_PROGRESS = "key_verify_in_progress";
    private static final int STATE_INITIALIZED = 1;
    private static final int STATE_CODE_SENT = 2;

    private static final int STATE_VERIFY_FAILED = 3;

    private static final int STATE_VERIFY_SUCCESS = 4;

    private static final int STATE_SIGNIN_FAILED = 5;

    private static final int STATE_SIGNIN_SUCCESS = 6;

    private static final String API_KEY = "AIzaSyBcfE-YgXF1b-V7Z8TDRphvX4EMwIN9h3U";

    private FirebaseAuth mAuth;

    private DatabaseReference mFirebaseDatabase;
    private DatabaseReference newFirebaseDatabase;
    private DatabaseReference waitFirebaseDatabase;
    private DatabaseReference newhubFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;

    private PhoneAuthProvider.ForceResendingToken mResendToken;

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    private EditText mPhoneNumberField;
    private EditText mVerificationField;

    int progressValue=25;

    int otp;
    int count=0;
    int index;
    int pcount=0;
    int faresgen;
    int counter=0;
    int countVal=0;
    int ignoreindex;
    int hubcalcount=0;
    int randomNumber,length=4,range=9;

    float km,fare,distance;

    double min;
    double lati;
    double distanceinkm;
    double lat = 0, lon = 0;
    double myswlatcoord=14.89833333,myswlngcoord=73.67583333;
    double mynelatcoord=15.66666667,mynelngcoord=74.33694444;

    String lat1_fromMap;
    String lon1_fromMap;
    Double double_lon1_fromMap;
    Double double_lat1_fromMap;


    boolean reply;
    boolean checkReply;
    boolean locwithinBounds;
    boolean isMenuNeeded=false;
    boolean requestSent=false;
    boolean isInBackground = true;
    boolean isInBackground2 = true;
    boolean isrequestAccepted=false;

    String Id;
    String ids;
    String faretv;
    String allkeys;
    String pilot_id;
    String allHubs;
    String keynew;
    String keynew2;
    String hubName;
    String parentkey;
    String text;
    String closebyPilots;
    String highestwaitPilotCheck;
    String minDistanceHub;
    String allkeynames;
    String loggedUser;
    String bnumRetrivedstring;
    String highestwaitPilot;
    String placenam2e;

    GpsTracker gps;

    long bnumRetrived;


    NotificationHelper helper;
    NotificationHelper2 helper2;
    NotificationHelper3 helper3;

    private TextView pilotName;
    private TextView bikeNumber;
    private TextView otpNumber;
    private TextView mDetailText;
    private TextView resendDetail;
    private TextView verifyDetail;
    private TextView faregenerated;

    private String strAdd;
    private String strAddor;
    private String mVerificationId;
    public String lat1,lat2,lon1,lon2;
    String pnameRetrived;

    private Button send;
    private Button mStartButton;
    private Button mVerifyButton;
    private Button mResendButton;
    private Button btnrideDetails;

    private ViewGroup mainView;
    private ViewGroup phoneView;
    private ViewGroup verifyView;
    private ViewGroup resendView;
    private ViewGroup logoView;
    private ViewGroup tickGroup;

    private ViewGroup afterSigninView;

    ArrayList<Double> dis = new ArrayList<>();
    ArrayList<String> highestpilotqueue=new ArrayList<>();
    ArrayList<String> newpilotlist=new ArrayList<>();
    ArrayList<String> checkpilotlist=new ArrayList<>();
    ArrayList<String> newpilotlist2=new ArrayList<>();
    ArrayList<String> newHubnames=new ArrayList<>();
    ArrayList<Long> waitarray=new ArrayList<>();
    ArrayList<String> names = new ArrayList<>();
    ArrayList<Long> testpilotswait=new ArrayList<>();
    ArrayList<String> testpilots=new ArrayList<>();
    ArrayList<Integer> ignoreindexlist=new ArrayList<>();
    final ArrayList<String> newparentList = new ArrayList<>();

    CountDownTimer progressDialogCancelTimer;
    private ProgressDialog progressDialog;
    ProgressDialog progressDialogRequest;
    ProgressDialog progressDialogCancel;

    private boolean mVerificationInProgress = false;

    AlertDialog.Builder restrictingbuilder;
    private Handler alerthandler = new Handler();

    private MyCountDown timer;

    static PhoneAuthActivity instance;

    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        instance=this;

        setContentView(R.layout.activity_phone_auth);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ArrayList<String> arrPerm=new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            arrPerm.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            arrPerm.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if(!arrPerm.isEmpty()){
                String[] permissions =new String[arrPerm.size()];
                permissions=arrPerm.toArray(permissions);
                ActivityCompat.requestPermissions(PhoneAuthActivity.this,permissions,MY_PERMISSIONS_REQUEST);
            }
        }

        mFirebaseInstance = FirebaseDatabase.getInstance();
        mFirebaseDatabase = mFirebaseInstance.getReference("customer");
        newFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("pilot");
        progressDialog=new ProgressDialog(this);

        final PlaceAutocompleteFragment places= (PlaceAutocompleteFragment) getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

//        if(places.getTag().equals("autocomplete_places")){
//            Log.e("fragment","visible");
//            Bundle bundle = getIntent().getExtras();
//            try {
//                double_lat1_fromMap = bundle.getDouble("mylocation_lat");
//                double_lon1_fromMap = bundle.getDouble("mylocation_lon");
//            }catch (Exception e){
//                e.printStackTrace();
//            }
//            if ((double_lat1_fromMap == null) || (double_lon1_fromMap == null)) {
//                Toast.makeText(getApplicationContext(), "Are you Here ?", Toast.LENGTH_SHORT).show();
//                Intent mapIntent = new Intent(PhoneAuthActivity.this, BeforeRequestMapsActivity.class);
//                finish();
//                mapIntent.putExtra("placeName",placenam2e);
//
//                startActivity(mapIntent);
//            }
//        }
        places.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                placenam2e=place.getName().toString();
                Log.e("placename",placenam2e);

                final LatLng location = place.getLatLng();
                double lat,lan;
                lat=location.latitude;
                lan=location.longitude;
                Log.e("dest double",""+lat);
                Log.e("dest double",""+lan);
                lat2=Double.toString(lat);
                lon2=Double.toString(lan);
                getCompleteAddressString(lat,lan);
                Log.e("dest",lat2);
                Log.e("dest",lon2);

            }

            @Override
            public void onError(Status status) {

                Toast.makeText(getApplicationContext(),status.toString(),Toast.LENGTH_SHORT).show();

            }
        });


        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(Place.TYPE_STREET_ADDRESS)
                .build();

        places.setFilter(typeFilter);

//        LatLng sw = new LatLng(14.89833333, 73.67583333);
//        LatLng ne = new LatLng(15.66666667,74.33694444);
        LatLng sw = new LatLng(14.919797, 73.749797);
        LatLng ne = new LatLng(15.682766,74.381511);
        LatLngBounds boundsBias = new LatLngBounds(sw, ne);
        places.setBoundsBias(boundsBias);
        places.setHint("Enter Destination");


        // Restore instance state

        if (savedInstanceState != null) {

            onRestoreInstanceState(savedInstanceState);

        }

        mainView = (ViewGroup) findViewById(R.id.main_layout);
        mDetailText = (TextView) findViewById(R.id.detail);
        resendDetail = (TextView) findViewById(R.id.resend_detail);
        verifyDetail = (TextView) findViewById(R.id.verify_detail);

        tickGroup=(ViewGroup) findViewById(R.id.tick_layout);

        pilotName = (TextView) findViewById(R.id.pilotName);
        bikeNumber = (TextView) findViewById(R.id.bikeNumber);
        otpNumber = (TextView) findViewById(R.id.otpNumber);

        helper = new NotificationHelper(this);
        helper2 = new NotificationHelper2(this);
        helper3 = new NotificationHelper3(this);

        btnrideDetails=findViewById(R.id.rideDetails);

        phoneView=findViewById(R.id.signin_phone_layout);

        verifyView=findViewById(R.id.signin_verify_layout);

        resendView=findViewById(R.id.signin_resend_layout);

        logoView=(ViewGroup)findViewById(R.id.logo);

        afterSigninView=(ViewGroup)findViewById(R.id.after_signin_layout);

        mPhoneNumberField = (EditText) findViewById(R.id.field_phone_number);

        mPhoneNumberField.setOnKeyListener(this);

        mVerificationField = (EditText) findViewById(R.id.field_verification_code);

        mStartButton = (Button) findViewById(R.id.button_start_verification);

        mVerifyButton = (Button) findViewById(R.id.button_verify_phone);

        mResendButton = (Button) findViewById(R.id.button_resend);

        progressDialogRequest=new ProgressDialog(PhoneAuthActivity.this);
        progressDialogCancel=new ProgressDialog(PhoneAuthActivity.this);
        progressDialogCancel.setTitle("Checking things for you..");
        progressDialogRequest.setMessage("Searching Pilots,Please Wait...");
        progressDialogRequest.setCanceledOnTouchOutside(false);
        progressDialogRequest.setCancelable(false);

        send = (Button) findViewById(R.id.send);

        faregenerated=(TextView)findViewById(R.id.fare);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
                boolean GPSstatus=manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(!GPSstatus){
                    Snackbar snackbar= Snackbar.make(findViewById(android.R.id.content),"Please enable GPS for requesting!",Snackbar.LENGTH_LONG);
                    View view=snackbar.getView();
                    int d=getResources().getColor(R.color.black);
                    snackbar.setActionTextColor(d);
                    view.setBackgroundResource(R.drawable.snackbar_aftersignin);
                    snackbar.show();
                    places.setText(null);
                }
                else {

                    if(placenam2e==null){
                        Log.e("google places empty", "error");
                       Toast.makeText(getApplicationContext(),"Please choose a Destination!",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Bundle bundle = getIntent().getExtras();
                        try {
                            double_lat1_fromMap = bundle.getDouble("mylocation_lat");
                            double_lon1_fromMap = bundle.getDouble("mylocation_lon");
                            locwithinBounds = bundle.getBoolean("locwithinBounds");


                            if ((double_lat1_fromMap == null) || (double_lon1_fromMap == null)) {
                                Toast.makeText(getApplicationContext(), "Are you Here ?", Toast.LENGTH_SHORT).show();
                                Intent mapIntent = new Intent(PhoneAuthActivity.this, BeforeRequestMapsActivity.class);
                                finish();
                                mapIntent.putExtra("placeName",placenam2e);

                                startActivity(mapIntent);
                            }
                            else {

                                Log.e("lat1fromMap", "" + double_lat1_fromMap);
                                Log.e("lon1fromMap", "" + double_lon1_fromMap);


                                lat1_fromMap = Double.toString(double_lat1_fromMap);
                                lon1_fromMap = Double.toString(double_lon1_fromMap);

                                Log.e("lat1fromMapString", "" + lat1_fromMap);
                                Log.e("lon1fromMapString", "" + lon1_fromMap);


                                if (locwithinBounds) {
                                    boolean requestpressed = true;
                                    SharedPreferences sharedPreferences5 = getApplicationContext().getSharedPreferences("RequestPressed", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences5.edit();
                                    editor.putBoolean("customer_request_sent", requestpressed);
                                    editor.apply();
                                    editor.commit();

                                    requestSent = true;
                                    if (requestSent) {

                                        progressDialogRequest.show();
                                    } else {
                                        Log.e("request", "already sent");
                                    }

                                    generateRandomNumber();
                                    otp = randomNumber;
                                    reply = false;
                                    pilot_id = "searching..";
                                    //hello = "problem here";
                                    newparentList.clear();
                                    checkpilotlist.clear();
                                    testpilotswait.clear();
                                    testpilots.clear();
                                    ignoreindexlist.clear();

                                    countVal = 0;
                                    getLocation(v);
//                                Log.e("checkin list1","newparentlist"+newparentList);
//                                Log.e("checkin list2","checkpilotlist"+checkpilotlist);
//                                Log.e("checkin list3","testpilotlist"+testpilots);
//                                Log.e("checkin list4","testpilotswait"+testpilotswait);
//                                Log.e("checkin list5","ignoreindexlist"+ignoreindexlist);
//                                Log.e("checkin list6","highestpilotqueue"+highestpilotqueue);
//                                Log.e("checkin counter1","countval"+countVal);


                                }
                                else{
                                    Toast.makeText(getApplicationContext(),"Sorry our services are not available in your location yet!",Toast.LENGTH_SHORT).show();
                                }
                            }
                        }catch (Exception e){
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Are you Here ?", Toast.LENGTH_SHORT).show();
                            Intent mapIntent = new Intent(PhoneAuthActivity.this, BeforeRequestMapsActivity.class);
                            finish();
                            mapIntent.putExtra("placeName",placenam2e);

                            startActivity(mapIntent);
                        }
                    }

                }
            }
        });

        btnrideDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rideDetails();
            }
        });

        mStartButton.setOnClickListener(this);

        mVerifyButton.setOnClickListener(this);

        mResendButton.setOnClickListener(this);


        // [START initialize_auth]

        mAuth = FirebaseAuth.getInstance();

        // [END initialize_auth]

        // Initialize phone auth callbacks

        // [START phone_auth_callbacks]

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override

            public void onVerificationCompleted(PhoneAuthCredential credential) {

                // For Instant verification and Auto-retrieval

                // Instant verification. In some cases the phone number can be instantly

                //     verified without needing to send or enter a verification code.

                // Auto-retrieval. On some devices Google Play services can automatically

                //     detect the incoming verification SMS and perform verificaiton without

                //     user action.

                Log.d(TAG, "onVerificationCompleted:" + credential);

                // [START_EXCLUDE silent]

                mVerificationInProgress = false;

                // [END_EXCLUDE]

                // [START_EXCLUDE silent]

                // Update the UI and attempt sign in with the phone credential

                updateUI(STATE_VERIFY_SUCCESS, credential);

                // [END_EXCLUDE]

                signInWithPhoneAuthCredential(credential);

            }


            @Override

            public void onVerificationFailed(FirebaseException e) {

                // This callback is invoked in an invalid requests for verification is made,

                // for instance if the the phone number format is not valid.

                Log.w(TAG, "onVerificationFailed", e);

                // [START_EXCLUDE silent]

                mVerificationInProgress = false;

                // [END_EXCLUDE]


                if (e instanceof FirebaseAuthInvalidCredentialsException) {

                    // Invalid requests

                    // [START_EXCLUDE]
                    hideProgressDialog();
                    Snackbar.make(findViewById(android.R.id.content), "Format of the phone number is incorrect; Enter:[+][country code][subscriber number]",

                            Snackbar.LENGTH_LONG).show();
                    mPhoneNumberField.setError("Invalid phone number.");

                    // [END_EXCLUDE]

                } else if (e instanceof FirebaseTooManyRequestsException) {

                    // The SMS quota for the project has been exceeded

                    // [START_EXCLUDE]
                    hideProgressDialog();

                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.Please Try Again After Sometime !",

                            Snackbar.LENGTH_SHORT).show();

                    // [END_EXCLUDE]

                }


                // Show a message and update the UI

                // [START_EXCLUDE]

                updateUI(STATE_VERIFY_FAILED);

                // [END_EXCLUDE]

            }


            @Override

            public void onCodeSent(String verificationId,

                                   PhoneAuthProvider.ForceResendingToken token) {

                // The SMS verification code has been sent to the provided phone number, we

                // now need to ask the user to enter the code and then construct a credential

                // by combining the code with a verification ID.

                Log.d(TAG, "onCodeSent:" + verificationId);


                // Save verification ID and resending token so we can use them later

                mVerificationId = verificationId;

                mResendToken = token;


                // [START_EXCLUDE]

                // Update UI




                updateUI(STATE_CODE_SENT);

                // [END_EXCLUDE]

            }

        };

        // [END phone_auth_callbacks]

    }



    public void getLocation(View v) {

        gps = new GpsTracker(PhoneAuthActivity.this);

        //Current Location
        lat = gps.getLatitude();
        lon = gps.getLongitude();

        lat1 = Double.toString(lat);
        lon1 = Double.toString(lon);

        Log.d("msg", String.valueOf(double_lat1_fromMap));
        Log.d("msg", String.valueOf(double_lon1_fromMap));

        getCompleteAddressString1(double_lat1_fromMap,double_lon1_fromMap);
        Location locationA = new Location("point A");

        locationA.setLatitude(Double.parseDouble(lat1_fromMap));
        locationA.setLongitude(Double.parseDouble(lon1_fromMap));

        Location locationB = new Location("point B");

        locationB.setLatitude(Double.parseDouble(lat2));
        locationB.setLongitude(Double.parseDouble(lon2));

        Log.d("msg2", String.valueOf(lat2));
        Log.d("msg2", String.valueOf(lon2));

         distance = locationA.distanceTo(locationB);
         km= (float) (distance*0.001);
          fare= 20+km*10;
         faretv="Your Fare : ";
         faresgen=(int)fare;
        Log.e("fare",faretv);
        SharedPreferences sharedPreferences3=getApplicationContext().getSharedPreferences("share5", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences3.edit();
        editor.putInt("reply_true_fare",faresgen);
        editor.apply();
         text=faretv.toString() + String.valueOf(faresgen);

        createUser(otp,reply,pilot_id,double_lat1_fromMap,lat2,double_lon1_fromMap,lon2,strAdd,strAddor,fare);

    }


    private String getCompleteAddressString(double lat2, double lon2) {
        //  String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat2, lon2, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

                Log.d("Curent loction address",strAdd);
            } else {
                Log.w("Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Current loction address", "Canont get Address!");
        }
        return strAdd;
    }
    private String getCompleteAddressString1(double lat1, double lon1) {
        //  String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat1, lon1, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAddor = strReturnedAddress.toString();

                Log.d("Curent loction address",strAddor);
            } else {
                Log.w("Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("Current loction address", "Canont get Address!");

        }
        return strAdd;
    }


    private void generateRandomNumber() {


        SecureRandom secureRandom=new SecureRandom();
        String c="";
        for(int i=0;i<length;i++){
            int number=secureRandom.nextInt(range);
            if(number==0 && i==0){
                i=-1;
                continue;
            }
            c=c+number;
        }
        randomNumber=Integer.parseInt(c);
        Log.e("OTP"," "+randomNumber);

    }


    // [START on_start_check_user]

    @Override

    public void onStart() {

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.

        FirebaseUser currentUser = mAuth.getCurrentUser();

        findViewById(R.id.tick_layout).setVisibility(View.GONE);

        isMenuNeeded=true;
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("share3",Context.MODE_PRIVATE);
         pnameRetrived=sharedPreferences.getString("reply_true_pname","");
        Log.e("pname_retruend2", pnameRetrived);
        SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences("share4",Context.MODE_PRIVATE);
         bnumRetrived=sharedPreferences2.getLong("reply_true_bnumber",0);
        Log.e("bnum_retruend2", String.valueOf(bnumRetrived));
        bnumRetrivedstring=String.valueOf(bnumRetrived);

        updateUI(currentUser);

        // [START_EXCLUDE]

        if (mVerificationInProgress && validatePhoneNumber()) {

            startPhoneNumberVerification(mPhoneNumberField.getText().toString());

        }

        // [END_EXCLUDE]

    }

    // [END on_start_check_user]

    @Override

    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);

        outState.putBoolean(KEY_VERIFY_IN_PROGRESS, mVerificationInProgress);

    }

    @Override

    protected void onRestoreInstanceState(Bundle savedInstanceState) {

        super.onRestoreInstanceState(savedInstanceState);

        mVerificationInProgress = savedInstanceState.getBoolean(KEY_VERIFY_IN_PROGRESS);

    }

    private void startPhoneNumberVerification(String phoneNumber) {

        // [START start_phone_auth]
//
//        findViewById(R.id.after_signin_layout).setVisibility(View.GONE);
//        findViewById(R.id.logo).setVisibility(View.GONE);
//        findViewById(R.id.signin_verify_layout).setVisibility(View.VISIBLE);
//        findViewById(R.id.signin_resend_layout).setVisibility(View.GONE);
//        findViewById(R.id.signin_phone_layout).setVisibility(View.GONE);

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                phoneNumber,        // Phone number to verify

                60,                 // Timeout duration

                TimeUnit.SECONDS,   // Unit of timeout

                this,               // Activity (for callback binding)

                mCallbacks);        // OnVerificationStateChangedCallbacks

        // [END start_phone_auth]

        mVerificationInProgress = true;


    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {

        // [START verify_with_code]

//        findViewById(R.id.after_signin_layout).setVisibility(View.GONE);
//        findViewById(R.id.logo).setVisibility(View.GONE);
        findViewById(R.id.signin_verify_layout).setVisibility(View.GONE);
//        findViewById(R.id.signin_resend_layout).setVisibility(View.GONE);
//        findViewById(R.id.signin_phone_layout).setVisibility(View.GONE);

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);

        // [END verify_with_code]

        signInWithPhoneAuthCredential(credential);

    }

    // [START resend_verification]

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(

                phoneNumber,        // Phone number to verify

                60,                 // Timeout duration

                TimeUnit.SECONDS,   // Unit of timeout

                this,               // Activity (for callback binding)

                mCallbacks,         // OnVerificationStateChangedCallbacks

                token);             // ForceResendingToken from callbacks

    }

    // [END resend_verification]

    // [START sign_in_with_phone]

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        showProgressDialog();

        mAuth.signInWithCredential(credential)

                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {

                    @Override

                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information

                            Log.d(TAG, "signInWithCredential:success");


                            FirebaseUser user = task.getResult().getUser();

                            // [START_EXCLUDE]
                            hideProgressDialog();

                            invalidateOptionsMenu();

                            findViewById(R.id.tick_layout).setVisibility(View.VISIBLE);

                            timer = new MyCountDown(3000, 1000);

                            updateUI(STATE_SIGNIN_SUCCESS, user);

                            // [END_EXCLUDE]

                        } else {

                            // Sign in failed, display a message and update the UI

                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                                // The verification code entered was invalid

                                // [START_EXCLUDE silent]

                               // mVerificationField.setError("Invalid code.");

                                // [END_EXCLUDE]

                            }

                            // [START_EXCLUDE silent]

                            // Update UI

                            hideProgressDialog();

                            updateUI(STATE_SIGNIN_FAILED);

                            // [END_EXCLUDE]

                        }

                    }

                });

    }

    // [END sign_in_with_phone]


    private void signOut() {

        mAuth.signOut();

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.hide();

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            Window window=getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.appbackground));
        }

        invalidateOptionsMenu();

        isMenuNeeded=false;
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // this.setContentView(R.layout.activity_phone_auth);
        //getSupportActionBar().hide();
//        setSupportActionBar(toolbar);
//        getSupportActionBar().hide();

        //flush all sharepreferences


        updateUI(STATE_INITIALIZED);

    }


    private void updateUI(int uiState) {

        updateUI(uiState, mAuth.getCurrentUser(), null);

    }


    private void updateUI(FirebaseUser user) {

        if (user != null) {

            updateUI(STATE_SIGNIN_SUCCESS, user);

        } else {

            updateUI(STATE_INITIALIZED);

        }

    }


    private void updateUI(int uiState, FirebaseUser user) {

        updateUI(uiState, user, null);

    }


    private void updateUI(int uiState, PhoneAuthCredential cred) {

        updateUI(uiState, null, cred);

    }


    private void updateUI(int uiState, FirebaseUser user, PhoneAuthCredential cred) {

        switch (uiState) {

            case STATE_INITIALIZED:

                // Initialized state, show only the phone number field and start button

                findViewById(R.id.after_signin_layout).setVisibility(View.GONE);
                findViewById(R.id.logo).setVisibility(View.VISIBLE);
                findViewById(R.id.signin_verify_layout).setVisibility(View.GONE);
                findViewById(R.id.signin_resend_layout).setVisibility(View.GONE);
                findViewById(R.id.signin_phone_layout).setVisibility(View.VISIBLE);

                mDetailText.setText(null);

                break;

            case STATE_CODE_SENT:
                hideProgressDialog();

                // Code sent state, show the verification field, the

                Log.d("uimsg","in code sent");
                findViewById(R.id.after_signin_layout).setVisibility(View.GONE);
                findViewById(R.id.logo).setVisibility(View.GONE);
                findViewById(R.id.signin_verify_layout).setVisibility(View.VISIBLE);
                findViewById(R.id.signin_resend_layout).setVisibility(View.GONE);
                findViewById(R.id.signin_phone_layout).setVisibility(View.GONE);

                verifyDetail.setText(R.string.status_code_sent);

                break;

            case STATE_VERIFY_FAILED:

                // Verification has failed, show all options

                phoneView.setVisibility(View.VISIBLE);

                verifyView.setVisibility(View.GONE);

                logoView.setVisibility(View.VISIBLE);

                resendView.setVisibility(View.GONE);

                afterSigninView.setVisibility(View.GONE);

                break;

            case STATE_VERIFY_SUCCESS:

                isMenuNeeded=true;

                phoneView.setVisibility(View.GONE);

                verifyView.setVisibility(View.GONE);

                logoView.setVisibility(View.GONE);

                resendView.setVisibility(View.GONE);

                if (cred != null) {

                    if (cred.getSmsCode() != null) {

                        mVerificationField.setText(cred.getSmsCode());

                    } else {

                        mVerificationField.setText(R.string.instant_validation);

                    }

                }

                break;

            case STATE_SIGNIN_FAILED:

                // No-op, handled by sign-in check

                mVerificationField.setError("Invalid code.");
                resendDetail.setText(R.string.status_sign_in_failed);
                resendView.setVisibility(View.VISIBLE);

                break;

            case STATE_SIGNIN_SUCCESS:

                // Np-op, handled by sign-in check

                break;

        }


        if (user == null) {

            // Signed out

            isMenuNeeded=false;
            ActionBar actionBar=getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.hide();

            mainView.setBackgroundResource(R.color.appbackground);
            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                Window window=getWindow();
                window.setStatusBarColor(getResources().getColor(R.color.appbackground));
            }

            Log.d("uimsg","in user==null");

            afterSigninView.setVisibility(View.GONE);

        } else {

            // Signed in
            LocationManager manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
            boolean GPSstatus=manager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if(!GPSstatus){

                Snackbar snackbar= Snackbar.make(findViewById(android.R.id.content),"Please enable GPS for requesting!",Snackbar.LENGTH_LONG);
                View view=snackbar.getView();
                view.setBackgroundResource(R.drawable.snackbar_aftersignin);
                snackbar.show();

            }
            isMenuNeeded=true;

            Log.d("uimsg","in user not null");
            ActionBar actionBar=getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(true);
            actionBar.show();

            mainView.setBackgroundResource(R.color.displaycolor);

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
                Window window=getWindow();
                window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
            }

            logoView.setVisibility(View.GONE);

            phoneView.setVisibility(View.GONE);

            verifyView.setVisibility(View.GONE);

            resendView.setVisibility(View.GONE);

            enableViews(mPhoneNumberField, mVerificationField);

            mPhoneNumberField.setText(null);

            mVerificationField.setText(null);

            mDetailText.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            loggedUser=user.getUid();
            Id=user.getUid();

            //check if map coordinates found
            SharedPreferences sharedPreferences5 = getApplicationContext().getSharedPreferences("RequestPressed", Context.MODE_PRIVATE);
            boolean requestPressed = sharedPreferences5.getBoolean("customer_request_sent", false);
            Log.e("request_sent", "fromPhoneA " + requestPressed);
            if (!requestPressed) {
                Log.e("in loop", "fromPhoneA " + requestPressed);
            try {
                Log.e("in loop", "loop2 " + requestPressed);
                afterSigninView.setVisibility(View.VISIBLE);
                Bundle bundle = getIntent().getExtras();
                double_lat1_fromMap = bundle.getDouble("mylocation_lat");
                double_lon1_fromMap = bundle.getDouble("mylocation_lon");
                locwithinBounds = bundle.getBoolean("locwithinBounds");

                if ((double_lat1_fromMap == null) || (double_lon1_fromMap == null)) {
                    Log.e("in loop", "maps " + requestPressed);
                    //  Toast.makeText(getApplicationContext(), "Location not found", Toast.LENGTH_SHORT).show();
                    Intent mapIntent = new Intent(PhoneAuthActivity.this, BeforeRequestMapsActivity.class);
                    finish();
                    mapIntent.putExtra("placeName", placenam2e);
                    startActivity(mapIntent);
                }
//                else{
//                    afterSigninView.setVisibility(View.VISIBLE);
//                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        else {

                //check if req accepted ,if yes display resp layout
                SharedPreferences sharedPreferences6 = getApplicationContext().getSharedPreferences("requestAccepted", Context.MODE_PRIVATE);
                boolean retrieveRequestAccepted = sharedPreferences6.getBoolean("is_request_accepted", false);
                Log.e("rerieveRequestA","fromPhoneA "+ String.valueOf(retrieveRequestAccepted));

                if (retrieveRequestAccepted) {
                    Log.e("request", "Accepted");
                    findViewById(R.id.after_request_accepted).setVisibility(View.VISIBLE);
                    afterSigninView.setVisibility(View.GONE);
                } else {
                    Log.e("request", "Not Accepted");
                    afterSigninView.setVisibility(View.VISIBLE);
                    findViewById(R.id.after_request_accepted).setVisibility(View.GONE);

                }
            }

     try{
                //check if any pilot has accepted request
            mFirebaseDatabase.child(Id).child("pilot_id").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    try {
                        String pid = (String) dataSnapshot.getValue();
                        Log.e("pid","no data" +pid);
                        System.out.println("no data");
                        if (pid == null || pid.equalsIgnoreCase("searching")) {
                            Log.e("no data", "available!");

                            SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("share3",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor2=sharedPreferences.edit();
                            editor2.clear();
                            editor2.commit();
                            SharedPreferences sharedPreferences5=getApplicationContext().getSharedPreferences("RequestPressed",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor4=sharedPreferences5.edit();
                            editor4.clear();
                            editor4.commit();
                            SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences("share4",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor3=sharedPreferences2.edit();
                            editor3.clear();
                            editor3.commit();
                            SharedPreferences sharedPreferences6=getApplicationContext().getSharedPreferences("requestAccepted",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor5=sharedPreferences6.edit();
                            editor5.clear();
                            editor5.commit();
                        } else {

                            //check if otp matched
            mFirebaseDatabase.child(Id).child("otpMatched").addValueEventListener(new ValueEventListener() {
             @Override
             public void onDataChange(DataSnapshot dataSnapshot) {
                 String otpMatched=(String)dataSnapshot.getValue();
                 if(otpMatched!=null){

                     findViewById(R.id.after_matchotp_layout).setVisibility(View.VISIBLE);
                     findViewById(R.id.after_request_accepted).setVisibility(View.GONE);
                     findViewById(R.id.after_signin_layout).setVisibility(View.GONE);
                     SharedPreferences sharedPreferences6=getApplicationContext().getSharedPreferences("requestAccepted",Context.MODE_PRIVATE);
                     SharedPreferences.Editor editor5=sharedPreferences6.edit();
                     editor5.clear();
                     editor5.commit();
                     SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("share3",Context.MODE_PRIVATE);
                     String pnameRetrived2=sharedPreferences.getString("reply_true_pname","");
                     Log.e("pname_retruend2_func", pnameRetrived);
                     SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences("share4",Context.MODE_PRIVATE);
                     long bnumRetrived2=sharedPreferences2.getLong("reply_true_bnumber",0);
                     String bnumRetrivedstring2=String.valueOf(bnumRetrived2);
                     Log.e("bool_retruend2_func", String.valueOf(bnumRetrived));
                     SharedPreferences sharedPreferences3=getApplicationContext().getSharedPreferences("share5",Context.MODE_PRIVATE);
                     int fare2=sharedPreferences3.getInt("reply_true_fare",0);
                     String fareRetrivedstring2=String.valueOf(fare2);
                     Log.e("fare_retruend2_func", String.valueOf(fare2));

                     SharedPreferences sharedPreferences7=getApplicationContext().getSharedPreferences("cannot_signout_cust",Context.MODE_PRIVATE);
                     SharedPreferences.Editor editor=sharedPreferences7.edit();
                     editor.clear();
                     editor.commit();

                     if((pnameRetrived2==null) && (bnumRetrivedstring2==null)){
                         pilotName.setText(pnameRetrived);
                         bikeNumber.setText(bnumRetrivedstring);
                         otpNumber.setText(fareRetrivedstring2);
                     }
                     else{
                         pilotName.setText(pnameRetrived2);
                         bikeNumber.setText(bnumRetrivedstring2);
                         otpNumber.setText(fareRetrivedstring2);
                     }

                 }
                 else{
                     Log.e("in ","otp");
                     findViewById(R.id.after_matchotp_layout).setVisibility(View.GONE);
                   //  afterSigninView.setVisibility(View.VISIBLE);
                 }

             }

             @Override
             public void onCancelled(DatabaseError databaseError) {

             }
         });
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

          }catch (Exception e){
                     e.printStackTrace();
                    }
               }
                }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void checkmap(View view) {
        if ((double_lat1_fromMap == null) || (double_lon1_fromMap == null)) {
            Toast.makeText(getApplicationContext(), "Are you Here ?", Toast.LENGTH_SHORT).show();
            Intent mapIntent = new Intent(PhoneAuthActivity.this, BeforeRequestMapsActivity.class);
            finish();
            mapIntent.putExtra("placeName",placenam2e);

            startActivity(mapIntent);
        }
    }


    private class MyCountDown extends CountDownTimer {
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

    private boolean validatePhoneNumber() {

        String phoneNumber = mPhoneNumberField.getText().toString();

        if (TextUtils.isEmpty(phoneNumber)) {

            mPhoneNumberField.setError("Invalid phone number.");

            hideProgressDialog();

            return false;

        }


        return true;

    }


    private void enableViews(View... views) {

        for (View v : views) {

            v.setEnabled(true);

        }

    }


    @Override

    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_start_verification:

                startButtonFunction();
                break;

            case R.id.button_verify_phone:

                String code = mVerificationField.getText().toString();

                if (TextUtils.isEmpty(code)) {

                    mVerificationField.setError("Cannot be empty.");

                    return;

                }

                verifyPhoneNumberWithCode(mVerificationId, code);

                break;

            case R.id.button_resend:

                mVerificationField.setText(null);

                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);

                break;

        }

    }


    private void startButtonFunction() {
        showProgressDialog();
        progressDialog.setMessage("Signing In");
        progressDialog.setCancelable(false);
        if (!validatePhoneNumber()) {
            return;
        }
        startPhoneNumberVerification(mPhoneNumberField.getText().toString());
    }


    private void createUser(int otp,boolean reply,String pilot_id,double lat1, String lat2, double lon1, String lon2,String strAdd,String strAddor, Float fare) {

        User user = new User(otp,reply,pilot_id,lat1,lat2,lon1,lon2,strAdd,strAddor,fare);

        mFirebaseDatabase.child(Id).setValue(user);

        getPilot();

    }


    private void getPilot() {
        dbcalNew();

        Log.e("minimum distance value", ":" + min);

        mFirebaseDatabase.child(Id).child("pilot_id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                pilot_id = (String) dataSnapshot.getValue();
                if (pilot_id == null) {
                    Log.e("pilot :", "" + pilot_id);
                    SharedPreferences sharedPreferences5=getApplicationContext().getSharedPreferences("RequestPressed",Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor4=sharedPreferences5.edit();
                    editor4.clear();
                    editor4.commit();

                    progressDialogCancelTimer.cancel();
                    progressDialogCancel.dismiss();
                    progressDialogCancel.cancel();

                    isAppIsInBackground(PhoneAuthActivity.this);

                    if(isInBackground){
                        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            Notification.Builder builder = helper2.getPilotChannelNotification();
                            helper2.getManager().notify(new Random().nextInt(), builder.build());
                        }
                        else {
                            notification();
                        }
                    }

                }

                else{

                    Log.e("else :", "Request Sent!");

                    //notification2();

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        mFirebaseDatabase.child(Id).child("reply").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 checkReply=(boolean) dataSnapshot.getValue();
                if(checkReply) {
                    isrequestAccepted=true;
                    SharedPreferences sharedPreferences6=getApplicationContext().getSharedPreferences("requestAccepted", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences6.edit();
                    editor.putBoolean("is_request_accepted",isrequestAccepted);
                    editor.apply();
                    boolean restrictsignoutcust = true;
                    SharedPreferences sharedPreferences7 = getApplicationContext().getSharedPreferences("cannot_signout_cust", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor2 = sharedPreferences7.edit();
                    editor2.putBoolean("signout_restricted_cust", restrictsignoutcust);
                    Log.e("signoutr_cust", String.valueOf(restrictsignoutcust));
                    editor2.apply();
                    editor2.commit();
                    Log.e("printing","requestA"+isrequestAccepted);

                    progressDialogCancelTimer.cancel();
                    progressDialogCancel.dismiss();
                    progressDialogCancel.cancel();

                    isAppIsInBackground2(PhoneAuthActivity.this);

                    if (isInBackground2) {
                        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            Notification.Builder builder = helper.getPilotChannelNotification(loggedUser);
                            helper.getManager().notify(new Random().nextInt(), builder.build());
                        }
                        else {
                            notification2();
                        }
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mFirebaseDatabase.child(Id).child("pilot_arrived").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    boolean pilotarrived = (boolean) dataSnapshot.getValue();
                    Log.e("pilotarrived", " : " + pilotarrived);

                    if (pilotarrived) {
                        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                            Notification.Builder builder = helper3.getPilotChannelNotification();
                            helper3.getManager().notify(new Random().nextInt(), builder.build());
                        }
                        else {
                            notifyCustomer();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e("exception ","no pilot found ");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }


    public void dbcalNew() {
        newhubFirebaseDatabase = FirebaseDatabase.getInstance().getReference().child("Hub");

        newhubFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    allHubs = childSnapshot.getKey();
                    Log.e("all hubs", "" + allHubs);
                    //  Log.e("hub", "calcount: " + hubcalcount);
                    ++hubcalcount;

                    final int maxhubcount=hubcalcount;

                    //Log.e("hub next", "calcount: " + maxhubcount);
                    names.add(allHubs);

                    newhubFirebaseDatabase.child(allHubs).child("lat").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // for (final DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                            lati =(double) dataSnapshot.getValue();
                            //  double value =(double) dataSnapshot.getValue();
                            Log.e("lat", "" + lati);
                        }


                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    newhubFirebaseDatabase.child(allHubs).child("lon").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            double longi =(double) dataSnapshot.getValue();

                            Log.e("lon", "" + longi);
                            // String val= names.get(0);
                            // ++count;
                            Log.e("i value",""+count);

                            hubName=names.get(count);
                            newHubnames.add(hubName);

                            cal(lati,longi,lat,lon);

                            ++count;

                            if(hubcalcount==maxhubcount) {
                                // Log.e("minDistanceHub is func", "" + newHubnames.get(index));
                                //Log.e("hub next func", "calcount: " + maxhubcount);
                                Log.e("minimum distance func", "calcount" + min);

                                checkhubAvailability();

                            }

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
        // Log.e("minimum distance", ":" + min);
//        min = Collections.min(dis);
//        Log.e("minimum distance", ":" + min);
    }


    public void cal(double h1lat,double h1lon,double mylat,double mylon){
        double theta = mylon - h1lon;
        double dist = Math.sin(deg2rad(mylat))
                * Math.sin(deg2rad(h1lat))
                + Math.cos(deg2rad(mylat))
                * Math.cos(deg2rad(h1lat))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        distanceinkm=dist * 60 * 1.1515;
        dis.add((double) (dist * 60 * 1.1515));
        Log.d("km", "values: " + (distanceinkm/0.62137));
        Log.d("dist", "distancecal: " + dis);

//        Location startPoint=new Location("Point A");
//        startPoint.setLatitude(mylat);
//        startPoint.setLongitude(mylon);
//
//        Location endPoint=new Location("Point B");
//        endPoint.setLatitude(h1lat);
//        endPoint.setLongitude(h1lon);
//
//        double diffdistance=startPoint.distanceTo(endPoint);
//        double kmdiff=diffdistance/1000;
//        Log.e("km","distance "+kmdiff);

        min = Collections.min(dis);

        Log.e("minimum distance", ":" + min);

        index = dis.indexOf(Collections.min(dis));
        //  Log.e("index",""+index);
        minDistanceHub = newHubnames.get(index);
        Log.e("minDistanceHub is", "" + newHubnames.get(index));

    }


    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }


    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }


    private void checkhubAvailability() {
        Log.e("checking", "availablity" + min);
        //Log.e("checking", "availability" + minDistanceHub);
        distanceinkm=min/0.62137;
        Log.d("km", "values: checkavailability " + distanceinkm);

        if(distanceinkm<=5){
            Log.e("its below","reqd value");

            getallPilots();
        }
        else{
            Log.e("its above","reqd value");
            progressDialogRequest.cancel();
            Toast.makeText(getApplicationContext(),"Cannot find Pilots , no hubs available in your Location !",Toast.LENGTH_LONG).show();
        }
    }


    private void getallPilots() {

        newFirebaseDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (final DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    allkeys = childSnapshot.getKey();
                    Log.e("countVal", "" + countVal);
                    Log.e("all keys", "" + allkeys);
                    ++countVal;

                    final int max = countVal;
                    ArrayList<String> parentList = new ArrayList<>();

                    parentList.add(allkeys);
//
                    //Log.e("alllist", "" + parentList);

                    Query mDbHub = newFirebaseDatabase.child(allkeys).child("currentHub");
                    mDbHub.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String minSaved = minDistanceHub;
                            Log.e("minimum distance2", ":" + minSaved);
                            String hubs = (String) dataSnapshot.getValue();
                           // Log.e("minimum hub", ":" + hubs);

                            if (hubs.equals(minDistanceHub)) {
                                //Log.e("index", "h ");
                                keynew = childSnapshot.getKey();
                              //  Log.e("key ", "" + keynew);
                                newpilotlist.add(keynew);
                                checkpilotlist.add(keynew);

                            }
                            if (countVal == max) {
                                Log.e("loop ", "looping");
                                keynew2 = childSnapshot.getKey();
                                Log.e("key2 ", "" + keynew2);
                               // Log.e("arraycheck ", "" + newpilotlist);
                                printArray(newpilotlist);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //   throw databaseError.toException();
            }
        });
    }


    private void printArray(ArrayList<String> newpilotlist) {
        //Log.e("check_val",""+newpilotlist);
        Log.e("mindistancehub","in fucn down "+minDistanceHub);
        Log.e("mindistancehubvalue","in fucn down "+min);
        dis.remove(min);
        newHubnames.remove(minDistanceHub);
        Log.e("mindistancehubarray","in fucn down "+dis);
        Log.e("mindistancehubname","in fucn down "+newHubnames);

        if(!dis.isEmpty()) {
            min = Collections.min(dis);
            Log.e("second minimum distance", ":" + min);
            distanceinkm=min/0.62137;
            Log.d("km", "values: printarray " + distanceinkm);
            if(distanceinkm<=5) {
                index = dis.indexOf(Collections.min(dis));
                //  Log.e("index",""+index);
                minDistanceHub = newHubnames.get(index);
                Log.e("second ", " minDistanceHub is " + newHubnames.get(index));

                mFirebaseDatabase.child(Id).child("nearbyPilots").setValue(newpilotlist);
                getHubNames();   //gets pilots with id null
            }
            else{
                try{
                    progressDialogCancelTimer.cancel();
                    progressDialogCancel.dismiss();
                    progressDialogRequest.dismiss();
                    faregenerated.setText(null);
                    faregenerated.setBackgroundResource(R.drawable.textview_border_cancelreq);
                }catch (Exception e){
                    e.printStackTrace();
                }

                Toast.makeText(getApplicationContext(),"No CloseBy Hubs,Please Try again later !",Toast.LENGTH_SHORT).show();
            }
        }
        else{
            try {
                progressDialogCancel.dismiss();
                progressDialogCancelTimer.cancel();
                faregenerated.setText(null);
                faregenerated.setBackgroundResource(R.drawable.textview_border_cancelreq);
            }catch (Exception e){
                e.printStackTrace();
            }
            Toast.makeText(getApplicationContext(),"No More Hubs left,Please Try again later !",Toast.LENGTH_SHORT).show();
        }
        for(int i=0;i<newpilotlist.size();i++){
          //  allkeynames=newpilotlist;
            allkeynames=newpilotlist.get(i);
        }

    }


    private void getHubNames() {
        mFirebaseDatabase.child(Id).child("nearbyPilots").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (final DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                 closebyPilots=(String)childSnapshot.getValue();
                    Log.e("counter",""+counter);
                    Log.e("closebypilots",""+closebyPilots);

                    ++counter;
                   final int maxval=counter;

                        newFirebaseDatabase.child(closebyPilots).child("cust_id").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ids = (String) dataSnapshot.getValue();
                                if (ids == null) {
                                    String datalink = dataSnapshot.getRef().toString();
                                    String tokens[] = datalink.split("/");
                                    parentkey = tokens[tokens.length - 2];
                                    Log.e("pilotnameKey", "" + parentkey);
                                    newpilotlist2.add(parentkey);
                                }
                                Log.e("all childs", "" + ids);
                                //   Log.e("all newch", "" + dataSnapshot.getRef().toString());

                                //  Log.e("counter",""+counter);
                                newparentList.add(ids);

                                System.out.println(newparentList);

                                if (counter == maxval) {
                                    Log.e("array", "" + newparentList);
                                    gettest();
                                }
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


    private void gettest() {
        Log.e("works", "okay");
        Log.e("arraylist", "" + newparentList);
        Log.e("arraylist2", "" + checkpilotlist);

        for(int i=0;i<checkpilotlist.size();i++){
            if(newparentList.get(i)==null){
                Log.e("pcount", "" + pcount);
                testpilots.add(checkpilotlist.get(i));
                String testpilotsstring=checkpilotlist.get(i);

                ++pcount;
                final int max = pcount;

                if(highestpilotqueue.contains(testpilotsstring)){
                    Log.e("used before"," "+testpilotsstring);
                     ignoreindex=testpilots.indexOf(testpilotsstring);
                    Log.e("ignored "," index "+ignoreindex);
                    ignoreindexlist.add(ignoreindex);
                }

                    newFirebaseDatabase.child(testpilotsstring).child("wait_time").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            long mywait = (long) dataSnapshot.getValue();
                            Log.e("mywait", "" + mywait);
                            testpilotswait.add(mywait);
                            try {
                                // Log.e("check without", "customer" + testpilotswait);
                                if (pcount == max) {
                                    Log.e("check without", "this customer " + testpilotswait);

                                    if (newparentList.contains(null)) {
                                        Log.e("waitarray", "" + testpilotswait);
                                        long highestwait = Collections.max(testpilotswait);
                                        Log.e("highestwait", "" + highestwait);
                                        int indexofwaittime = testpilotswait.indexOf(Collections.max(testpilotswait));
                                        if (ignoreindexlist.contains(indexofwaittime)) {
                                            Log.e("ignore", "this index " + indexofwaittime);
                                            testpilotswait.set(indexofwaittime, (long) 0);
                                            Log.e("new ", "testpilotsw " + testpilotswait);
                                            highestwait = Collections.max(testpilotswait);
                                            Log.e("second", "highestwait " + highestwait);
                                            indexofwaittime = testpilotswait.indexOf(highestwait);
                                            Log.e("second", "indexpilot " + indexofwaittime);

                                        }
                                        if (highestwait != 0) {

                                            Log.e("indexOf", "highestwait -" + indexofwaittime);
                                            highestwaitPilot = testpilots.get(indexofwaittime);
                                            Log.e("before loop", "found before " + highestwaitPilot);

                                            Log.e("queue ", " before " + highestpilotqueue);

                                            if (highestpilotqueue.contains(highestwaitPilot)) {
                                                Log.e("already", "present");
                                                testpilots.clear();
                                                testpilotswait.clear();
                                                newparentList.clear();
                                                Log.e("in newparentlist", "already list " + newparentList);
                                                Log.e("in testpilot", "already list " + testpilots);
                                                checkpilotlist.clear();
                                                ignoreindexlist.clear();
                                                countVal = 0;
                                                Log.e("in", "alrady queue " + checkpilotlist);
                                                getallPilots();

                                            } else {
                                                highestpilotqueue.add(highestwaitPilot);
                                                Log.e("pilotNameOf", "highestwait -" + highestwaitPilot);
                                                highestwaitPilotCheck = highestwaitPilot;

                                                newFirebaseDatabase.child(highestwaitPilotCheck).child("cust_id").setValue(Id);

                                                faregenerated.setText(text);
                                                faregenerated.setBackgroundResource(R.drawable.textview_border);

                                                progressDialogRequest.dismiss();
                                            }

                                            if (requestSent) {
                                                Toast.makeText(getApplicationContext(), "Request sent!", Toast.LENGTH_SHORT).show();

                                                showLoading();
                                            } else {
                                                Log.e("request", "already sent");
                                            }

                                            newparentList.clear();
                                            waitarray.clear();
                                            testpilotswait.clear();
                                            testpilots.clear();
                                            counter = 0;
                                            pcount = 0;

                                        } else {
                                            // progressDialogRequest.dismiss();
                                            newpilotlist.clear();
                                            checkpilotlist.clear();
                                            testpilots.clear();
                                            newparentList.clear();
                                            testpilotswait.clear();
                                            countVal = 0;
                                            ignoreindexlist.clear();
                                            Log.e("new ", "pilotlist" + newpilotlist);
                                            Log.e("no pilots ", "this hub");
                                            getallPilots();
//                                        progressDialogCancel.dismiss();
//                                        progressDialogCancelTimer.cancel();
                                            // countVal=0;
                                            //Toast.makeText(getApplicationContext(), "No pilots Available,Please try Again after Sometime!", Toast.LENGTH_SHORT).show();

                                            // Toast.makeText(getApplicationContext(),"else loop",Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        faregenerated.setText(null);
                                        faregenerated.setBackgroundResource(R.drawable.textview_border_cancelreq);
                                        // faregenerated.setBackgroundResource(Integer.parseInt(null));
                                        progressDialogRequest.dismiss();
                                        newpilotlist.clear();
                                        checkpilotlist.clear();
                                        testpilots.clear();
                                        Toast.makeText(getApplicationContext(), "No pilots Available,Please try Again after Sometime!", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


            }

        }

        Log.e("pilots without", "customer" + testpilots);


    }


    private void showLoading() {

        progressDialogCancel.setCancelable(false);
        progressDialogCancel.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialogCancel.incrementProgressBy(-50);
                progressDialogCancel.dismiss();
                progressDialogCancel.cancel();
                progressDialogCancelTimer.cancel();
                try {
                    newpilotlist.clear();
                    checkpilotlist.clear();
                    newFirebaseDatabase.child(highestwaitPilotCheck).child("cust_id").setValue(null);
                }catch (Exception e){
                    e.printStackTrace();
                }
                requestSent=false;
            }
        });
        try {
          //  Log.e("log", "progress v" + progressValue);
            progressDialogCancel.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialogCancel.setIndeterminate(false);
            progressDialogCancel.setMessage("searching");
            progressDialogCancel.incrementProgressBy(progressValue);
            progressDialogCancel.setMax(100);
            progressDialogCancel.setProgressPercentFormat(null);
            progressDialogCancel.setProgressNumberFormat(null);
            progressDialogCancel.show();

        }catch (Exception e){
            e.printStackTrace();
        }
          progressDialogCancelTimer=new CountDownTimer(60000,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                int progess = (int) ((60000 - millisUntilFinished) / 1000);
                Log.e("log", "progress" + progess);

                if (pilot_id == null || checkReply) {
                    progressDialogCancel.dismiss();
                } else {
                    String number = String.valueOf(progess);
                    if (number.endsWith(String.valueOf(1))) {
                        progressDialogCancel.setMessage("Checking fuel");
                    } else if (number.endsWith(String.valueOf(2))) {
                        Drawable d = getResources().getDrawable(R.mipmap.ic_checking_tyres);
                       // progressDialogCancel.setProgressDrawable(d);
                        progressDialogCancel.setMessage("Checking tyres");
                    } else if (number.endsWith(String.valueOf(3))) {
                        progressDialogCancel.setMessage("Filling air");
                    } else if (number.endsWith(String.valueOf(4))) {
                        Drawable d = getResources().getDrawable(R.mipmap.ic_filling_fuel);
                       // progressDialogCancel.setProgressDrawable(d);
                        progressDialogCancel.setMessage("Filling fuel");
                    } else if (number.endsWith(String.valueOf(5))) {
                        progressDialogCancel.setMessage("Tightening screw");
                    } else if (number.endsWith(String.valueOf(6))) {
                        progressDialogCancel.setMessage("Cleaning seats");
                    } else if (number.endsWith(String.valueOf(7))) {
                        progressDialogCancel.setMessage("Testing brakes");
                    } else if (number.endsWith(String.valueOf(8))) {
                        progressDialogCancel.setMessage("Cleaning seats");
                    } else if (number.endsWith(String.valueOf(9))) {
                        progressDialogCancel.setMessage("Checking gears");
                    } else if (number.endsWith(String.valueOf(0))) {
                        progressDialogCancel.setMessage("Checking mirrors");
                    }
                }

                if( progess==19 || progess==39 ){
                    Log.e("progress","time_out");
                    progressDialogCancel.incrementProgressBy(progressValue+25);
                    requestSent=false;
                    if(highestwaitPilotCheck!=null) {
                        newFirebaseDatabase.child(highestwaitPilotCheck).child("cust_id").setValue(null);
                        newFirebaseDatabase.child(highestwaitPilotCheck).child("autoDecline").setValue(true);
                    }
                    else{
                        Log.e("its","null");
                    }
                    faregenerated.setText(null);
                    faregenerated.setBackgroundResource(R.drawable.textview_border_cancelreq);
                    newparentList.clear();
                    waitarray.clear();
                    counter=0;pcount=0;

                    testpilots.clear();
                    testpilotswait.clear();

                    getHubNames();
                }
            }

            @Override
            public void onFinish() {
                if(highestwaitPilotCheck!=null) {
                    newFirebaseDatabase.child(highestwaitPilotCheck).child("cust_id").setValue(null);
                    newFirebaseDatabase.child(highestwaitPilotCheck).child("autoDecline").setValue(true);
                }
                else{
                    Log.e("its","null");
                }
                try {
                    newFirebaseDatabase.child(highestwaitPilotCheck).child("cust_id").setValue(null);
                    newFirebaseDatabase.child(highestwaitPilotCheck).child("autoDecline").setValue(true);
                }catch (Exception e){
                    e.printStackTrace();
                }
                faregenerated.setText(null);
                faregenerated.setBackgroundResource(R.drawable.textview_border_cancelreq);
                progressValue=0;
                progressDialogCancel.incrementProgressBy(-50);
                progressDialogCancel.dismiss();
                progressDialogCancelTimer.cancel();
                progressDialogCancel.cancel();
                Toast.makeText(getApplicationContext(),"Could not find pilots for you, Try again in sometime !",Toast.LENGTH_LONG).show();
            }
        }.start();


    }


    private boolean isAppIsInBackground(Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                            Toast.makeText(getApplicationContext(), "Send Again", Toast.LENGTH_SHORT).show();
                            SharedPreferences sharedPreferences5=getApplicationContext().getSharedPreferences("RequestPressed",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor4=sharedPreferences5.edit();
                            editor4.clear();
                            editor4.commit();
                            finish();
                            Intent abc=new Intent(PhoneAuthActivity.this,BeforeRequestMapsActivity.class);
                            startActivity(abc);
                            Log.e("check","inforegnd");
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
                Log.e("check2","inforegnd");
            }
        }

        Log.e("back","backgrnd");
        return isInBackground;
    }


    private boolean isAppIsInBackground2(Context context) {

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                          //  Toast.makeText(getApplicationContext(), "Send Again", Toast.LENGTH_SHORT).show();
                           // Intent abc=new Intent(PhoneAuthActivity.this,PhoneAuthActivity.class);
                            Intent intent = new Intent(this, displayDetails.class);
                            intent.putExtra("data",loggedUser);
                            startActivity(intent);
                            Log.e("check","inforegnd");
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
                Log.e("check2","inforegnd");
            }
        }

        Log.e("back","backgrnd");
        return isInBackground;
    }


    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences=getApplicationContext().getSharedPreferences("share3",Context.MODE_PRIVATE);
        pnameRetrived=sharedPreferences.getString("reply_true_pname","");
        Log.e("pname_retruend_resume", pnameRetrived);
        SharedPreferences sharedPreferences2=getApplicationContext().getSharedPreferences("share4",Context.MODE_PRIVATE);
        bnumRetrived=sharedPreferences2.getLong("reply_true_bnumber",0);
        Log.e("bnum_retruend2_resume", String.valueOf(bnumRetrived));
        bnumRetrivedstring=String.valueOf(bnumRetrived);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.signout_menu,menu);
        menu.setGroupVisible(R.id.menu_group,isMenuNeeded);
      //  menu.setGroupVisible(R.id.ride_details,isReqAvailable);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //item.setVisible(isMenuNeeded);
        switch (item.getItemId()){
            case R.id.sign_out_menu:
                boolean restrictedsignoutcust;
                SharedPreferences sharedPreferences4=getApplicationContext().getSharedPreferences("cannot_signout_cust",Context.MODE_PRIVATE);
                restrictedsignoutcust=sharedPreferences4.getBoolean("signout_restricted_cust",false);
                Log.e("signout_restricted_cust", String.valueOf(restrictedsignoutcust));

                if(restrictedsignoutcust){

                    restrictingbuilder=new AlertDialog.Builder(this);
                    restrictingbuilder.setMessage("Ride is already On");
                    final AlertDialog alertDialog=restrictingbuilder.create();
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
        Log.e("im ","here");
        AboutFragment br=new AboutFragment();
        FragmentManager mg=getSupportFragmentManager();
        FragmentTransaction ft=mg.beginTransaction();
      //  mainView.setBackgroundResource(R.color.place_autocomplete_search_text);
      //  findViewById(R.id.after_signin_layout).setVisibility(View.GONE);
     //   findViewById(R.id.send).setVisibility(View.GONE);
        ft.addToBackStack("Fragment");
      //  if(afterSigninView.isv)
      //  Log.e("visiblity",""+abc);

        ft.replace(R.id.temp,br);
       // findViewById(R.id.fragment_layout).setVisibility(View.VISIBLE);
       //332 ft.replace(R.id.after_signin_layout,br);
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
        intent.putExtra("data",loggedUser);
        startActivity(intent);
    }



    private void notification2() {
        finish();
        Intent intent = new Intent(this, displayDetails.class);
        intent.putExtra("data",loggedUser);
        intent.putExtra("user",loggedUser);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setOnlyAlertOnce(true);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setSmallIcon(R.drawable.ic_cutmypic__1_);
        }
        else {
            builder.setSmallIcon(R.drawable.signin_logo);
        }
        builder.setContentTitle("PilotZai");
        builder.setContentText("Request accepted!");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);

        // Gets an instance of the NotificationManager service

        builder.setAutoCancel(true);
        // Builds the notification and issues it.
        mNotifyMgr.notify(1, builder.build());
    }


    private void notification() {
        finish();
        Intent intent = new Intent(this, PhoneAuthActivity.class);

        final NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        builder.setOnlyAlertOnce(true);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setSmallIcon(R.drawable.ic_cutmypic__1_);
        }
        else {
            builder.setSmallIcon(R.drawable.signin_logo);
        }

        builder.setContentTitle("PilotZai");
        builder.setContentText("Send Again!");

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Gets an instance of the NotificationManager service

        builder.setAutoCancel(true);

        mNotifyMgr.notify(1,builder.build());

    }


    private void notifyCustomer(){
        final android.support.v7.app.NotificationCompat.Builder builder = new android.support.v7.app.NotificationCompat.Builder(this);
        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        builder.setSound(alarmSound);
        builder.setOnlyAlertOnce(true);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            builder.setSmallIcon(R.drawable.ic_cutmypic__1_);
        }
        else{
            builder.setSmallIcon(R.drawable.signin_logo);
        }
        builder.setContentTitle("PilotZai");
        builder.setContentText("Your pilot has arrived !");
      //  builder.setContentInfo("Its AutoDeclined");
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        // Gets an instance of the NotificationManager service
        builder.setAutoCancel(true);
        // Builds the notification and issues it.
        mNotifyMgr.notify(2, builder.build());

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

