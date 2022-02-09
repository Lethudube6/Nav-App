package com.example.mypocketnavv;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.text.Layout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineListener;
import com.mapbox.android.core.location.LocationEnginePriority;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.permissions.PermissionsListener;
import com.mapbox.android.core.permissions.PermissionsManager;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.api.geocoding.v5.GeocodingCriteria;
import com.mapbox.api.geocoding.v5.MapboxGeocoding;
import com.mapbox.api.geocoding.v5.models.CarmenFeature;
import com.mapbox.api.geocoding.v5.models.GeocodingResponse;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.MarkerOptions;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mapbox.mapboxsdk.plugins.locationlayer.LocationLayerPlugin;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.CameraMode;
import com.mapbox.mapboxsdk.plugins.locationlayer.modes.RenderMode;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncher;
import com.mapbox.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.mapbox.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements
        OnMapReadyCallback, LocationEngineListener, PermissionsListener, MapboxMap.OnMapClickListener {

    ////welcome dialog;

    //Declarations of main main ui elements
    TextView txt;
    String fname="",lname="",currentuser;
    Button startButton;
    FloatingActionButton search;

    //MapBox step1 - 4 ...1 map....2 permissions and location engine ....3 Markers nad points...4 Navigation Routes
    //Step 1: Map stuff
     private MapView mapView;
     private MapboxMap map;
     //Step 2:permissions and location engine
     private PermissionsManager permissionsManager;
     private LocationEngine locationEngine;
     private LocationLayerPlugin locationLayerPlugin;
     //Step 3: Markers and points
     private Location originLocation;
     private Point originPosition;
     private Point destinationPosition;
     private Marker destinationMarker;
     //Step 4: Navigation Routes
     private NavigationMapRoute navigationMapRoute;
     private static final String TAG="MapActivity";

     //Step 5: geo coding
     MapboxGeocoding mapboxGeocoding;
     MapboxGeocoding reverseGeocode;

     //Step 6: search bar
    private LinearLayout linearLayout;
    private Boolean visibleLayout=false;
    Button btnsearchLocation;
    EditText elsearchLocation;

    ///Firebase Declarations
    DatabaseReference mydatabase;
    private FirebaseAuth mAuth;
    String departure_address1,
            departure_address2,
            destination_address1,
            destination_address2;
    String[] address1;

    //IconFactory iconFactory = IconFactory.getInstance(MapActivity.this);
    //Icon icon = iconFactory.fromResource(R.drawable.ic_user_location_custom);

//***************************************************************************************************************************
//***************************************************************************************************************************
//***************************************************************************************************************************
//***************************************************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Mapbox.getInstance(this,"pk.eyJ1IjoiYmVlMTIiLCJhIjoiY2tnbzlmem55MDA1ZTJ6bGJoeHRvMDMzeSJ9.O5vxYsDHyDk-hvDSqPjUiQ");

        setContentView(R.layout.activity_map);
        mapView= findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
        linearLayout = findViewById(R.id.linearLayout1);
        btnsearchLocation=findViewById(R.id.btnLocationSearch);
        elsearchLocation=findViewById(R.id.etLocationsearch);



        startButton=findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //String departureArr[]= detailsTrip(originPosition);
                //String destinationArr[]= detailsTrip(destinationPosition);
                addTrip("60 Rosebank Avenue, Morningside, Berea", "Florida Road, Windermere, Berea",destination_address1,destination_address2);
                //addTrip(departureArr[0],departureArr[1],destinationArr[0],destinationArr[1]);
                NavigationLauncherOptions options=NavigationLauncherOptions.builder()
                        .origin(originPosition)
                        .destination(destinationPosition)
                        .shouldSimulateRoute(true) //THIS IS WHERE YOU MAKE IT DRIVE AUTO OR NOT
                        .build();
                NavigationLauncher.startNavigation(MapActivity.this,options);
            }
        });
        search=findViewById(R.id.floatingActionButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!visibleLayout) {
                    linearLayout.setVisibility(View.VISIBLE);
                    visibleLayout=true;
                }else {
                    linearLayout.setVisibility(View.GONE);
                    visibleLayout=false;
                }
            }
        });

        btnsearchLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String address=elsearchLocation.getText().toString();
                if(!address.isEmpty()){
                    convertAddressToCoordinates(address);
                }else {
                    elsearchLocation.setError("Address should be entered");
                    elsearchLocation.requestFocus();
                }


            }
        });

        //some other code dont mind it

        //Fire Base code
        mAuth = FirebaseAuth.getInstance();
        mydatabase= FirebaseDatabase.getInstance().getReference("TripHistory");



        currentuser = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        txt=findViewById(R.id.textView);
        queryinit(currentuser);
        opendialog();

    }
    public void opendialog(){
        mapActivityDialog1 dialog1= new mapActivityDialog1();
        dialog1.show(getSupportFragmentManager(),"MapActivity Dialog");
    }

//************* * *************************************************************************************************************
//************* **  ************************************************************************************************************
//************* **************************************************************************************************************
//************* **************************************************************************************************************



    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        map=mapboxMap;
        map.addOnMapClickListener(this);
        enableLocation();

    }
    public void enableLocation(){
        if(PermissionsManager.areLocationPermissionsGranted(this)){
            initializeLocationEngine();
            initializeLocationLayer();
        }else {
            permissionsManager= new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
    public void initializeLocationEngine(){
        locationEngine= new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.HIGH_ACCURACY);
        locationEngine.activate();

        Location lastLocation=locationEngine.getLastLocation();
        if(lastLocation!=null){
            originLocation=lastLocation;
            setCameraPosition(lastLocation);
        }else {
            locationEngine.addLocationEngineListener(this);
        }
    }
    public void initializeLocationLayer(){
        locationLayerPlugin= new LocationLayerPlugin(mapView,map,locationEngine);
        locationLayerPlugin.setLocationLayerEnabled(true);
        locationLayerPlugin.setCameraMode(CameraMode.TRACKING);
        locationLayerPlugin.setRenderMode(RenderMode.NORMAL);
    }

    public void setCameraPosition(Location location){
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(),
                                                            location.getLongitude()),13.0));
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if(destinationMarker!=null){
            map.removeMarker(destinationMarker);
        }

            destinationMarker = map.addMarker(new MarkerOptions().position(point));

            destinationPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());
            //originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
            originPosition = Point.fromLngLat(31.04999, -29.8579);
            getRoute(originPosition,destinationPosition);
            startButton.setEnabled(true);
            startButton.setBackgroundResource(R.color.mapbox_blue);

            //I will post some thing here hopefully
            convertCoordinatesToAddress(point);
    }

    private void getRoute(Point origin, Point destination){
        NavigationRoute.builder()
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .destination(destination)
                .build()
                .getRoute(new Callback<DirectionsResponse>() {
                    @Override
                    public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                        if(response==null){
                            Log.e(TAG,"No routes found check map");
                            return;
                        }else  if(response.body().routes().size()==0){
                            Log.e(TAG,"No routes found ");
                            return;
                        }
                        DirectionsRoute currentRoute=response.body().routes().get(0);
                        if(navigationMapRoute !=null){
                            navigationMapRoute.removeRoute();
                        }else {
                            navigationMapRoute = new NavigationMapRoute(null, mapView, map);
                        }
                        navigationMapRoute.addRoute(currentRoute);
                    }

                    @Override
                    public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                        Log.e(TAG,"Error:"+t.getMessage());
                    }
                });
    }

    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location!=null){
            originLocation=location;
            setCameraPosition(location);
        }
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
            if(granted){
                enableLocation();
            }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void convertAddressToCoordinates(String address){
        //My other random code don't mind me im just passing by
        //wow you still reading i just said dont mind me
        String originaladdress="1600 Pennsylvania Ave NW";
        mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken("pk.eyJ1IjoiYmVlMTIiLCJhIjoiY2tnbzlmem55MDA1ZTJ6bGJoeHRvMDMzeSJ9.O5vxYsDHyDk-hvDSqPjUiQ")
                .query(address)
                .build();
        mapboxGeocoding.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                List<CarmenFeature> results = response.body().features();

                if (results.size() > 0) {

                    // Log the first results Point.
                    Point firstResultPoint = results.get(0).center();
                    LatLng latLng = new LatLng(firstResultPoint.latitude(),firstResultPoint.longitude());
                    searchAddroute(latLng);

                    Log.d(TAG, "onResponse: " + firstResultPoint.toString());
                    //Toast.makeText(MapActivity.this, "These are the coordinates:\n"+firstResultPoint.toString(), Toast.LENGTH_SHORT).show();

                } else {

                    // No result for your request were found.
                    Log.d(TAG, "onResponse: No result found");

                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    public void convertCoordinatesToAddress(LatLng placeC){
        reverseGeocode = MapboxGeocoding.builder()
                .accessToken(getString(R.string.access_token))
                .query(Point.fromLngLat(placeC.getLongitude(), placeC.getLatitude()))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build();
        reverseGeocode.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {

                List<CarmenFeature> results = response.body().features();

                if (results.size() > 0) {

                    // Log the first results Point.
                    String address = response.body().features().toString();
                    String string = "004-034556";
                    String[] parts = address.split("text=");
                    String []part1 = parts[1].split(", placeName="); // 004
                    String address2 = part1[0]; // 034556

                    String[]parts3 =address.split("placeName=");
                    String[]parts4 =parts3[1].split(", placeType=");
                    String fullAddress=parts4[0];

                    destination_address1=address2;
                    destination_address2=fullAddress;

                    Log.d(TAG, "onResponse: " + address);
                   // Toast.makeText(MapActivity.this, "This is the address:\n"+address2+"\nFull Address:\n"+fullAddress, Toast.LENGTH_LONG).show();

                } else {

                    // No result for your request were found.
                    Log.d(TAG, "onResponse: No result found");
                    Toast.makeText(MapActivity.this, "No location for this address at this point", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {

            }
        });
    }
    public String[]detailsTrip(Point point){

        MapboxGeocoding reverseGeocodee = MapboxGeocoding.builder()
                .accessToken(getString(R.string.access_token))
                .query(Point.fromLngLat(point.longitude(), point.latitude()))
                .geocodingTypes(GeocodingCriteria.TYPE_ADDRESS)
                .build();
        reverseGeocodee.enqueueCall(new Callback<GeocodingResponse>() {
            @Override
            public void onResponse(Call<GeocodingResponse> call, Response<GeocodingResponse> response) {
                List<CarmenFeature> results = response.body().features();

                if (results.size() > 0) {

                    // Log the first results Point.
                    //Point firstResultPoint = results.get(0).center();
                    //Log.d(TAG, "onResponse: " + firstResultPoint.toString());
                    String address = response.body().features().toString();
                    String string = "004-034556";
                    String[] parts = address.split("text=");
                    String []part1 = parts[1].split(", placeName="); // 004
                    String address2 = part1[0]; // 034556

                    String[]parts3 =address.split("placeName=");
                    String[]parts4 =parts3[1].split(", placeType=");
                    String fullAddress=parts4[0];

                    address1[0]=address2;
                    address1[1]=fullAddress;

                } else {

                    // No result for your request were found.
                    Log.d(TAG, "onResponse: No result found");

                }
            }

            @Override
            public void onFailure(Call<GeocodingResponse> call, Throwable t) {

            }
        });




        return address1;
    }

    public void searchAddroute(LatLng point){
        if(destinationMarker!=null){
            map.removeMarker(destinationMarker);
        }

        destinationMarker = map.addMarker(new MarkerOptions().position(point));

        destinationPosition = Point.fromLngLat(point.getLongitude(), point.getLatitude());
        originPosition = Point.fromLngLat(originLocation.getLongitude(), originLocation.getLatitude());
        //originPosition = Point.fromLngLat(31.0284, -29.8579);
        getRoute(originPosition,destinationPosition);
        startButton.setEnabled(true);
        startButton.setBackgroundResource(R.color.mapbox_blue);

        //I will post some thing here hopefully
        convertCoordinatesToAddress(point);
    }















//***************************************************************************************************************************
//***************************************************************************************************************************
//***************************************************************************************************************************
//***************************************************************************************************************************

    public void queryinit(String emals){
        Query query = FirebaseDatabase.getInstance().getReference("userdetails")
                .orderByChild("email")
                .equalTo(emals);
        query.addListenerForSingleValueEvent(valueEventListener);
    }

    ValueEventListener valueEventListener= new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(dataSnapshot.exists()) {
                for (DataSnapshot nudr22:dataSnapshot.getChildren()) {
                    newuserdatareg nudr=nudr22.getValue(newuserdatareg.class);
                    fname=nudr.fname;
                    lname=nudr.lname;
                }
                //txt.setText("Welcome user " + fname + " " + lname);
            }else {
                Toast.makeText(MapActivity.this, " empty", Toast.LENGTH_SHORT).show();
                //txt.setText("Eisha its empty has nothing  " );
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

   //***********************************************************************************************************************
   //***********************************************************************************************************************
    public void addTrip(String dep1,String dep2,String des1,String des2){

        Date currentTime = Calendar.getInstance().getTime();
        String dateNow=currentTime.toString();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        String id = mydatabase.push().getKey();
        TripHandler tripHandler= new TripHandler(currentUser.getEmail(),dep1,dep2,des1,des2,dateNow);
        mydatabase.child(currentUser.getUid()).child(id).setValue(tripHandler);


    }







//***************************************************************************************************************************
//***************************************************************************************************************************
//***************************************************************************************************************************
//***************************************************************************************************************************



    @Override
    protected void onStart() {
        super.onStart();
        if(locationEngine!=null){
            locationEngine.requestLocationUpdates();
        }
        if(locationLayerPlugin!=null){
            locationLayerPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(locationEngine!=null){
            locationEngine.requestLocationUpdates();
        }
        if(locationLayerPlugin!=null){
            locationLayerPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(locationEngine!=null){
            locationEngine.deactivate();
        }
        mapView.onDestroy();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.menu_mapactivity_toolbar,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
           // case R.id.item_tripHistory:
                //startActivity(new Intent(MapActivity.this,TripHistoryActivity.class));
               // return true;
            //case R.id.item_settings:
               // startActivity(new Intent(MapActivity.this,SettingsActivity.class));
                //return true;
            case R.id.item_signOut:
                startActivity(new Intent(MapActivity.this,MainActivity.class));
                return true;

            default: return super.onOptionsItemSelected(item);
        }

    }
}
