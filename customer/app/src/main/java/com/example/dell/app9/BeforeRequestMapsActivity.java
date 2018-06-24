package com.example.dell.app9;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.util.Log;
import android.view.View;

import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BeforeRequestMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GpsTracker gps;double lat = 0, lon = 0;
    String lat1,lon1;
    static final int MY_PERMISSIONS_REQUEST=123;
    private GoogleMap mMap;
    public LatLng position,goa;
    public ImageView dropPinView;
    String placeStored;
    public Marker mylocation;
    boolean locwithinBounds=false;

    double myswlatcoord=14.89833333,myswlngcoord=73.67583333;
    double mynelatcoord=15.66666667,mynelngcoord=74.33694444;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_before_request_maps);

        ArrayList<String> arrPerm=new ArrayList<>();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            arrPerm.add(android.Manifest.permission.ACCESS_COARSE_LOCATION);
            arrPerm.add(android.Manifest.permission.ACCESS_FINE_LOCATION);
            if(!arrPerm.isEmpty()){
                String[] permissions =new String[arrPerm.size()];
                permissions=arrPerm.toArray(permissions);
                ActivityCompat.requestPermissions(BeforeRequestMapsActivity.this,permissions,MY_PERMISSIONS_REQUEST);
            }
            //return TODO;
        }
        LocationManager manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean GPSstatus=manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!GPSstatus){
            Snackbar snackbar= Snackbar.make(findViewById(android.R.id.content),"Please enable GPS for requesting!",Snackbar.LENGTH_LONG);
            View view=snackbar.getView();
            int d=getResources().getColor(R.color.black);
            snackbar.setActionTextColor(d);
            view.setBackgroundResource(R.drawable.snackbar_aftersignin);
            snackbar.show();
            //Toast.makeText(getApplicationContext(),"Please enable GPS for requesting!",Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"Please enable GPS for requesting!",Toast.LENGTH_LONG).show();
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        dropPinView=findViewById(R.id.imageView);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        CameraUpdate Update;
        //getLocation();
        gps = new GpsTracker(BeforeRequestMapsActivity.this);
        //Current Location
        lat = gps.getLatitude();
        lon = gps.getLongitude();

        lat1 = Double.toString(lat);
        lon1 = Double.toString(lon);

        Log.d("msg", String.valueOf(lat));
        Log.d("msg", String.valueOf(lon));
       // goa = new LatLng(15.4909, 73.8278);
        goa = new LatLng(lat, lon);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(goa));
        mylocation = mMap.addMarker(new MarkerOptions().position(goa).title("My location"));
        Update = CameraUpdateFactory.newLatLngZoom(goa,16);
        mMap.animateCamera(Update);
        //mMap.setOnMarkerDragListener(this);

        try{
            Bundle bundle=getIntent().getExtras();
             placeStored=bundle.getString("placeName");
            Log.e("placeNameMaps",placeStored);

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public void onDoneClick(View v)
    {
        LocationManager manager=(LocationManager)getSystemService(Context.LOCATION_SERVICE);
        boolean GPSstatus=manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if(!GPSstatus){
            Snackbar snackbar= Snackbar.make(findViewById(android.R.id.content),"Please enable GPS for requesting!",Snackbar.LENGTH_LONG);
            View view=snackbar.getView();
            int d=getResources().getColor(R.color.black);
            snackbar.setActionTextColor(d);
            view.setBackgroundResource(R.drawable.snackbar_aftersignin);
            snackbar.show();

        }
        else {

            final LatLng position = mMap.getProjection().fromScreenLocation(new Point(dropPinView.getLeft() + (dropPinView.getWidth() / 2), dropPinView.getBottom()));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Pick-Up location")
                    .setMessage("Confirm Pick-up location")
                    .setCancelable(false)
                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Toast.makeText(BeforeRequestMapsActivity.this, position.toString(), Toast.LENGTH_SHORT).show();
                            dropPinView.setVisibility(View.INVISIBLE);
                            mylocation.remove();
                            mylocation = mMap.addMarker(new MarkerOptions().position(position).title("pick up location"));
//                            double mylat=position.latitude;
//                            double mylon=position.longitude;
//
//                            Geocoder gdc=new Geocoder(BeforeRequestMapsActivity.this, Locale.getDefault());
//                            try {
//                                List<Address> ad = gdc.getFromLocation(mylat, mylon, 1);
//                                Log.e("loc",""+ad.get(0).getAddressLine(0));
//                                Log.e("loc",""+ad.get(0).getLocality());
//                                Log.e("loc",""+ad.get(0).getFeatureName());
//                                Log.e("loc",""+ad.get(0).getThoroughfare());
//                               // Log.e("loc",""+ad.get(0).getn);
//
//
//                                // Log.e("position",""+position);
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }

                            String datalink= String.valueOf(position);
                            String tokens[] = datalink.split("[\\(\\)]");
                            String data = tokens[tokens.length - 1];
                           // Log.e("latlng",""+data);

                            String tok[]=data.split(",");
                            String first=tok[tok.length-1];
                            String sec=tok[tok.length-2];
                            double newlon=Double.parseDouble(first);
                            double newlat=Double.parseDouble(sec);
                            Log.e("newlat",""+newlat);
                            Log.e("newlng",""+newlon);


                            if(myswlatcoord<=newlat && myswlngcoord<=newlon){
                               // Log.e("here4",""+newlon);
                                if(mynelatcoord>=newlat && mynelngcoord>=newlon){
                                  //  Toast.makeText(getApplicationContext(),"in bounds",Toast.LENGTH_SHORT).show();
                                    locwithinBounds=true;
                                    // Log.e("her4e",""+newlon);
                                }
                            }
                            else{
                                Toast.makeText(getApplicationContext(),"Sorry our services are not available in your location yet!",Toast.LENGTH_SHORT).show();
                                //  Log.e("here",""+newlon);
                                locwithinBounds=false;

                            }
                            finish();

                            Intent phoneIntent = new Intent(BeforeRequestMapsActivity.this, PhoneAuthActivity.class);
                            phoneIntent.putExtra("mylocation_lat", newlat);
                            phoneIntent.putExtra("mylocation_lon", newlon);
                            phoneIntent.putExtra("locwithinBounds", locwithinBounds);
                           // Log.e("here",""+newlon);

                         //   phoneIntent.putExtra("placeName",placeStored);
                            startActivity(phoneIntent);
                        }
                    });
            builder.setNegativeButton(
                    "cancel",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            builder.create().show();
        }

    }
}
