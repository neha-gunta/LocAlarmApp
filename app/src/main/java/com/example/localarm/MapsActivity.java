package com.example.localarm;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.webkit.PermissionRequest;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.localarm.contacts.ContactModel;
import com.example.localarm.contacts.DbHelper;
import com.example.localarm.shakeServices.SensorService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.localarm.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.IOException;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private static final int PRIORITY_HIGH_ACCURACY = 100;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private FusedLocationProviderClient fusedLocationClient;
    SearchView searchView;
    TextView dis;
    double circleradius =  2.0;
    SeekBar seekbar;
    TextView radiusTv;
    Switch sw;
    Location startLoc;
    private Marker currMarker;
    private Marker startMarker;
    private Marker finalMarker;
    LocationManager locationManager;
    MarkerOptions mo;
    Circle c;
    boolean flag=false;
    boolean searchflag=false;
    boolean flag3=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());
        searchView = findViewById(R.id.idSearchView);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        seekbar = (SeekBar) findViewById(R.id.radiusSeekbar);
        radiusTv = (TextView) findViewById(R.id.radius);
        radiusTv.setText(String.valueOf(circleradius));
        sw =(Switch) findViewById(R.id.onEntry);
        dis=findViewById(R.id.dist);
        Button buttonset = findViewById(R.id.set);
        buttonset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapsActivity.this,AddAlarm.class);
                startActivity(intent);
            }
        });


        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true)
                {
                    //onentry is called
                    flag3=true;
                }
                else
                {
                    //onexit
                    flag3=false;
                }
            }
        });

        //permission check

        locationManager =
                (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mo=new MarkerOptions().position(new LatLng(0,0)).title("My current location");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // on below line we are getting the
                // location name from search view.
                String location = searchView.getQuery().toString();

                // below line is to create a list of address
                // where we will store the list of all address.
                List<Address> addressList = null;

                // checking if the entered location is null or not.
                if (location != null || location.equals("")) {
                    // on below line we are creating and initializing a geo coder.
                    Geocoder geocoder = new Geocoder(MapsActivity.this);
                    try {
                        // on below line we are getting location from the
                        // location name and adding that location to address list.
                        addressList = geocoder.getFromLocationName(location, 1);
                    } catch (IOException e) {
                        Toast.makeText(MapsActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                        return false;
                    }
                    // on below line we are getting the location
                    // from our list a first position.
                    Address address = addressList.get(0);

                    // on below line we are creating a variable for our location
                    // where we will add our locations latitude and longitude.
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());

                    // on below line we are adding marker to that position.
                    finalMarker=mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    c = mMap.addCircle(new CircleOptions()
                            .center(latLng)
                            .radius(circleradius)//meters
                            .strokeColor(R.color.black).fillColor(R.color.black));
                    // below line is to animate camera to that position.

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    searchflag=true;
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        requestLoc();
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Your location"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Dexter.withContext(this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                @Override
                public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                    Toast.makeText(MapsActivity.this, "permission granted", Toast.LENGTH_SHORT).show();

                }

                @Override
                public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), "");
                    intent.setData(uri);
                    startActivity(intent);
                }

                @Override
                public void onPermissionRationaleShouldBeShown(com.karumi.dexter.listener.PermissionRequest permissionRequest, PermissionToken permissionToken) {

                }

//                @Override
//                public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
//                    permissionToken.continuePermissionRequest();
//                }
            }).check();

        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {


                        LatLng start = new LatLng(location.getLatitude(), location.getLongitude());

                        startMarker=mMap.addMarker(new MarkerOptions().position(start).title("Start location"));
                        currMarker=mMap.addMarker(mo.title("Your location"));
                        finalMarker=startMarker;
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(start, 15));
                        CameraUpdateFactory.zoomIn();

//                        c = mMap.addCircle(new CircleOptions()
//                                .center(start)
//                                .radius(circleradius)//meters
//                                .strokeColor(R.color.black).fillColor(R.color.black));

                        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                radiusTv.setText(String.valueOf(i));
                                circleradius=(double)i;
                                c.setRadius(i);
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {

                            }
                        });

                        // Got last known location. In some rare situations this can be null.
//                        if (location != null) {
//                            Toast.makeText(MapsActivity.this,"Could not retrieve location, check settings",Toast.LENGTH_LONG).show();
//                            // Logic to handle location object
//                        }
                    }
                });





    }


    @Override
    public void onLocationChanged(@NonNull Location location) {
//        float[] results= new float[5];
//        Location.distanceBetween(currMarker.getPosition().latitude,currMarker.getPosition().longitude,location.getLatitude(),location.getLongitude(),results);
        if(currMarker!=null)
        currMarker.setPosition(new LatLng(location.getLatitude(),location.getLongitude()));

        if(searchflag && !flag && onExitCheck() && !flag3)
        {
            Toast.makeText(MapsActivity.this,"Alarm should ring",Toast.LENGTH_LONG).show();
            flag=true;
            setAlarm(true);
        }

        if(searchflag && !flag && onEntryCheck() && flag3)
        {
            Toast.makeText(MapsActivity.this,"Alarm should ring",Toast.LENGTH_LONG).show();
            flag=true;
            setAlarm(true);
        }
        //going out of circle

//        if(results[0]>circleradius){
//            Toast.makeText(MapsActivity.this,"Alarm should ring",Toast.LENGTH_LONG).show();
//        }


    }

    @SuppressLint("MissingPermission")
    private void requestLoc(){
        Criteria cr=new Criteria();
        cr.setAccuracy(Criteria.ACCURACY_FINE);
        cr.setPowerRequirement(Criteria.POWER_HIGH);

        String provider=locationManager.getBestProvider(cr,true);
        locationManager.requestLocationUpdates(provider,1000,5,this);


    }
    private boolean onExitCheck(){
        float[] results= new float[1];
        Location.distanceBetween(currMarker.getPosition().latitude,currMarker.getPosition().longitude,finalMarker.getPosition().latitude,finalMarker.getPosition().longitude,results);
        dis.setText(String.valueOf(results[0]));
        if(results[0]>circleradius)
            dis.setText("Alarm");
        return (results[0]>circleradius);
    }
    private boolean onEntryCheck(){
        float[] results= new float[1];
        Location.distanceBetween(currMarker.getPosition().latitude,currMarker.getPosition().longitude,finalMarker.getPosition().latitude,finalMarker.getPosition().longitude,results);
        dis.setText(String.valueOf(results[0]));
        if(results[0]<circleradius)
            dis.setText("Alarm");
        return (results[0]<circleradius);
//        return (results[0]/1000<(float)circleradius);
    }


    @SuppressLint("MissingPermission")
    private void setAlarm(boolean flag) {
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, PendingIntent.FLAG_MUTABLE);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC, 2000, AlarmManager.INTERVAL_DAY, pi);
//        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
//        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), Settings.System.DEFAULT_RINGTONE_URI);
//        mediaPlayer.start();
        // create FusedLocationProviderClient to get the user location
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());

        // use the PRIORITY_BALANCED_POWER_ACCURACY
        // so that the service doesn't use unnecessary power via GPS
        // it will only use GPS at this very moment
        fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_BALANCED_POWER_ACCURACY, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @NonNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }
        }).addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // check if location is null
                // for both the cases we will
                // create different messages
                if (location != null) {

                    SmsManager smsManager = SmsManager.getDefault();
                    DbHelper db = new DbHelper(MapsActivity.this);
                    String message = "Hey, I've safely reached the destination.  Here are my coordinates. "+"http://maps.google.com/?q=" + location.getLatitude() + "," + location.getLongitude();
                    List<ContactModel> list = db.getAllContacts();
                    for (ContactModel c : list) {
                        smsManager.sendTextMessage(c.getPhoneNo(), null, message, null, null);
                    }
                } else {
                    String message = "I am in DANGER, I need help. Please urgently reach me out.\n" + "GPS was turned off.Couldn't find location. Call your nearest Police Station.";
                    SmsManager smsManager = SmsManager.getDefault();
                    DbHelper db = new DbHelper(MapsActivity.this);
                    List<ContactModel> list = db.getAllContacts();
                    for (ContactModel c : list) {
                        smsManager.sendTextMessage(c.getPhoneNo(), null, message, null, null);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("Check: ", "OnFailure");
                String message = "I am in DANGER, i need help. Please urgently reach me out.\n" + "GPS was turned off.Couldn't find location. Call your nearest Police Station.";
                SmsManager smsManager = SmsManager.getDefault();
                DbHelper db = new DbHelper(MapsActivity.this);
                List<ContactModel> list = db.getAllContacts();
                for (ContactModel c : list) {
                    smsManager.sendTextMessage(c.getPhoneNo(), null, message, null, null);
                }
            }
        });



        Intent intent = new Intent(this,RingAlarm.class);
        startActivity(intent);
    }

}