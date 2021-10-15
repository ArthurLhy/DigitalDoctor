package com.jxstarxxx.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.List;

public class VaccineFinderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final Integer REQUEST_CODE = 44;
    private static final float MAP_ZOOM = 10;

    // Initialize variable
    private SupportMapFragment mapFragment;
    private GoogleMap mapAPI;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private Location lastKnownLocation;
    private LocationCallback locationCallback;

    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private Button btnFind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine_finder);

        materialSearchBar = findViewById(R.id.search_bar);
        btnFind = findViewById(R.id.btn_find);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(VaccineFinderActivity.this);
        Places.initialize(VaccineFinderActivity.this, "AIzaSyD02TgyZOhs80_7tv76NY3ORgwyQ6Puhro");
        placesClient = Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();
    }

    /**
     * Initial view in map page
     * @param googleMap
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mapAPI = googleMap;
        mapAPI.setMyLocationEnabled(true);
        mapAPI.getUiSettings().setMyLocationButtonEnabled(true);

        if(mapView != null && mapView.findViewById(Integer.parseInt("1")) != null){
            View locationButton = ((View) mapView.findViewById(Integer.parseInt("1")).getParent())
                    .findViewById(Integer.parseInt("2"));
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) locationButton.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP,0);
            layoutParams.setMargins(0,0,40,180);
        }

        // check if gps is enable or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(VaccineFinderActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(VaccineFinderActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getCurrentLocation();
            }
        });

        task.addOnFailureListener(VaccineFinderActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult(VaccineFinderActivity.this, REQUEST_CODE);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            if(resultCode == RESULT_OK) {
                getCurrentLocation();
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
       fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
           @Override
           public void onComplete(@NonNull Task<Location> task) {
               if (task.isSuccessful()){
                   lastKnownLocation = task.getResult();
                   if (lastKnownLocation != null) {
                       mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), MAP_ZOOM));
                   } else {
                       LocationRequest locationRequest = LocationRequest.create();
                       locationRequest.setInterval(10000);
                       locationRequest.setFastestInterval(5000);
                       locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                       locationCallback = new LocationCallback() {
                           @Override
                           public void onLocationResult(LocationResult locationResult) {
                               super.onLocationResult(locationResult);
                               if(locationResult == null) {
                                   return;
                               }
                               lastKnownLocation = locationResult.getLastLocation();
                               mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()), MAP_ZOOM));
                               fusedLocationProviderClient.removeLocationUpdates(locationCallback);
                           }
                       };

                       fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
                   }
               } else {
                   Toast.makeText(VaccineFinderActivity.this, "Unable to get the last location", Toast.LENGTH_SHORT).show();
               }
           }
       });
    }
}