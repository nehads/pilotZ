package com.example.dell.app9;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MapsActivity2  extends FragmentActivity implements OnMapReadyCallback,LocationListener,GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ChildEventListener mChildEventListener;
    public DatabaseReference mdatabase;
    MarkerOptions a;
    Marker myMarker;
    final String path = "location" + "/" + "1001/";
    GpsTracker gps;
    String highestKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mdatabase= FirebaseDatabase.getInstance().getReference("pilot");
        a=new MarkerOptions();

        Bundle bundle = getIntent().getExtras();
        highestKey = bundle.getString("keyName");

        Log.e("highestKey",""+highestKey);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        gps=new GpsTracker(MapsActivity2.this);
        mMap = googleMap;
        final CameraUpdate[] Update = new CameraUpdate[1];
        googleMap.setOnMarkerClickListener(this);
        double lat,lon;
        lat= gps.getLatitude();
        lon= gps.getLongitude();
        LatLng cur = new LatLng(lat,lon);
        //mMap.addMarker(new MarkerOptions().position(cur).title("My Location")).setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_bike));setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));

        int height=120;
        int width=70;
        BitmapDrawable bitmapDrawable=(BitmapDrawable)getResources().getDrawable(R.drawable.location_marker2);
        Bitmap b=bitmapDrawable.getBitmap();
        Bitmap smallmarker=Bitmap.createScaledBitmap(b,width,height,false);
        mMap.addMarker(new MarkerOptions().position(cur).title("Me")).setIcon(BitmapDescriptorFactory.fromBitmap(smallmarker));
        Update[0] = CameraUpdateFactory.newLatLngZoom(cur,13);
        mMap.animateCamera(Update[0]);

        //child has to be ''highestKey''
        mdatabase.child(highestKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                double lat = 0,log=0;
                int i=0;
                for (DataSnapshot s:dataSnapshot.getChildren())
                {
                    //recv r=s.getValue(recv.class);

                    if(i==0)
                    {

                       // lat=Double.parseDouble(s.getValue().toString());
                       lat= Double.valueOf(dataSnapshot.child("latitude").getValue().toString());
                        Log.e("lat",""+lat);
                        //Log.e("lat",""+newlat);
                        i++;
                    }
                    if(i==2)
                    {
                       // log=Double.parseDouble(s.getValue().toString());


                        log= Double.valueOf(dataSnapshot.child("longitude").getValue().toString());
                        Log.e("lon",""+log);
                        //Log.e("lon",""+newllon);
                        i++;
                    }
                    i++;
                    if(i==4)
                    {
                        LatLng location = new LatLng(lat, log);
                        a.position(location);
                        if(myMarker==null)
                        {
                            myMarker=mMap.addMarker(a);
                        }
                        else
                        {
                            myMarker.setPosition(location);
                        }
                        //Update[0] = CameraUpdateFactory.newLatLngZoom(location,14);
                        //mMap.animateCamera(Update[0]);
                        i=0;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
       /*mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               double lat = 0,log=0;
               int i=0;
               for (DataSnapshot s:dataSnapshot.getChildren())
               {
                   //recv r=s.getValue(recv.class);
                   if(i==0)
                   {
                       lat=Double.parseDouble(s.getValue().toString());
                       i++;
                   }
                   if(i==2)
                   {
                       log=Double.parseDouble(s.getValue().toString());
                       i++;
                   }
                   i++;
                   if(i==4)
                   {
                       LatLng location = new LatLng(lat, log);
                       mMap.addMarker(new MarkerOptions().position(location).title("My Ride")).setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                       Update[0] = CameraUpdateFactory.newLatLngZoom(location,14);
                       mMap.animateCamera(Update[0]);
                       i=0;
                   }
               }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });*/
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }
}
