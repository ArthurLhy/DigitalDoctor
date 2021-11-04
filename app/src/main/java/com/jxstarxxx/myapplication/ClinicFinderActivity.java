package com.jxstarxxx.myapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
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
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.jxstarxxx.myapplication.DTO.HospitalsDTO;
import com.jxstarxxx.myapplication.myUtils.MapDataParser;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.adapter.SuggestionsAdapter;
import com.skyfishjy.library.RippleBackground;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClinicFinderActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final Integer REQUEST_CODE = 44;
    private static final float MAP_ZOOM = 13;

    private SupportMapFragment mapFragment;
    private GoogleMap mapAPI;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private PlacesClient placesClient;
    private List<AutocompletePrediction> predictionList;

    private Location lastKnownLocation;
    private LocationCallback locationCallback;

    private MaterialSearchBar materialSearchBar;
    private View mapView;
    private Button btnFind, zoomIn, zoomOut;
    private RippleBackground rippleBg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clinic_finder);

        materialSearchBar = findViewById(R.id.searchBar);
        btnFind = findViewById(R.id.btn_find);
        rippleBg = findViewById(R.id.ripple_bg);
        zoomIn = findViewById(R.id.zoomInBtn);
        zoomOut = findViewById(R.id.zoomOutBtn);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mapView = mapFragment.getView();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(ClinicFinderActivity.this);
        Places.initialize(ClinicFinderActivity.this, getResources().getString(R.string.map_key));
        placesClient = Places.createClient(this);
        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

         //search bar
        materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean enabled) {

            }

            @Override
            public void onSearchConfirmed(CharSequence text) {
                startSearch(text.toString(), true, null, true);
            }

            @Override
            public void onButtonClicked(int buttonCode) {
                if (buttonCode == MaterialSearchBar.BUTTON_NAVIGATION) {

                } else if (buttonCode == MaterialSearchBar.BUTTON_BACK) {
                    materialSearchBar.disableSearch();
                }
            }
        });

        materialSearchBar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // restriction bounds
                RectangularBounds bounds = RectangularBounds.newInstance(
                        new LatLng(-33.880490, 151.184363),
                        new LatLng(-33.858754, 151.229596)
                );

                FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                        .setLocationBias(bounds)
                        .setOrigin(new LatLng(-33.8749937, 151.2041382))
                        .setCountries("AU", "NZ")
                        .setSessionToken(token)
                        .setQuery(s.toString())
                        .build();
                placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
                    predictionList = response.getAutocompletePredictions();
                    List<String> suggestionList = new ArrayList<>();
                    for(AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                        suggestionList.add(prediction.getFullText(null).toString());
                    }
                    materialSearchBar.updateLastSuggestions(suggestionList);
                    if (materialSearchBar.isSuggestionsVisible()) {
                        materialSearchBar.showSuggestionsList();
                    }

                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("Google Map Place API", "Place not found:" + apiException.getMessage());
                    }
                });
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        materialSearchBar.setSuggstionsClickListener(new SuggestionsAdapter.OnItemViewClickListener() {
            @Override
            public void OnItemClickListener(int position, View v) {
                if (position >= predictionList.size()) {
                    return;
                }

                AutocompletePrediction selectedPrediction = predictionList.get(position);
                String suggestion = materialSearchBar.getLastSuggestions().get(position).toString();
                materialSearchBar.setText(suggestion);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        materialSearchBar.clearSuggestions();
                    }
                }, 1000);


                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(materialSearchBar.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
                }
                String placeId = selectedPrediction.getPlaceId();
                List<Place.Field> placeFields = Arrays.asList(Place.Field.LAT_LNG);

                FetchPlaceRequest fetchPlaceRequest = FetchPlaceRequest.builder(placeId, placeFields).build();
                placesClient.fetchPlace(fetchPlaceRequest).addOnSuccessListener((response) -> {
                    Place place = response.getPlace();
                    Log.i("Google Map Place API", "Place found:" + place.getName());
                    LatLng latLngOfPlace = place.getLatLng();
                    Log.i("map find place", "latitude: " + latLngOfPlace.latitude + " longitude" + latLngOfPlace.longitude);
                    if (latLngOfPlace != null) {
                        mapAPI.moveCamera(CameraUpdateFactory.newLatLngZoom(latLngOfPlace, MAP_ZOOM));
                    }

                }).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e("Google Map Place API", "Place not found:" + apiException.getMessage());
                    }
                });
            }

            @Override
            public void OnItemDeleteListener(int position, View v) {

            }
        });

        zoomOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapAPI.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        zoomIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapAPI.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LatLng currentLocation = mapAPI.getCameraPosition().target;
                Log.i("map current place", "latitude: " + currentLocation.latitude + " longitude" + currentLocation.longitude);
                rippleBg.startRippleAnimation();
                new retrievePlacesTask(currentLocation.latitude, currentLocation.longitude).execute();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rippleBg.stopRippleAnimation();
                        // todo: skip the hospital card view
                    }
                }, 3000);
            }
        });

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
            layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
            layoutParams.setMargins(0,0,30,240);
        }

        // check if gps is enable or not and then request user to enable it
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);

        SettingsClient settingsClient = LocationServices.getSettingsClient(ClinicFinderActivity.this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(ClinicFinderActivity.this, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                getCurrentLocation();
            }
        });

        task.addOnFailureListener(ClinicFinderActivity.this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    try {
                        resolvableApiException.startResolutionForResult(ClinicFinderActivity.this, REQUEST_CODE);
                    } catch (IntentSender.SendIntentException sendIntentException) {
                        sendIntentException.printStackTrace();
                    }
                }
            }
        });

        mapAPI.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (materialSearchBar.isSuggestionsVisible()) {
                    materialSearchBar.clearSuggestions();
                }
                if (materialSearchBar.isSearchEnabled()){
                    materialSearchBar.disableSearch();
                }
                return false;
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
                   Toast.makeText(ClinicFinderActivity.this, "Unable to get the last location", Toast.LENGTH_SHORT).show();
               }
           }
       });
    }

    private class retrievePlacesTask extends AsyncTask<String, Integer, String> {

        private double latitude, longitude;

        public retrievePlacesTask(double latitude, double longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        @Override
        protected String doInBackground(String... strings) {
            StringBuilder strBuilder = new StringBuilder()
                    .append("https://maps.googleapis.com/maps/api/place/nearbysearch/json?rankby=distance&type=hospital&location=")
                    .append(latitude).append(",").append(longitude)
                    .append("&key=").append(getResources().getString(R.string.map_key));
            String result = null;

            try {
                result = downloadUrl(strBuilder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }

            Log.i("Find nearby hospitals", result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }

        private String downloadUrl(String string) throws IOException {
            Log.i("Find hospitals request", string);
            URL url = new URL(string);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            InputStream streamIn = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(streamIn));
            StringBuilder builder = new StringBuilder();
            String line = "";
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            reader.close();
            Log.i("Retrieve data with size",":" + builder.length());
            return builder.toString();
        }
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HospitalsDTO>> {

        @Override
        protected List<HospitalsDTO> doInBackground(String... strings) {
            MapDataParser mapDataParser = new MapDataParser();
            List<HospitalsDTO> result = new ArrayList<>(mapDataParser.parse(strings[0]));
            return result;
        }

        @Override
        protected void onPostExecute(List<HospitalsDTO> hospitalsDTOS) {
            mapAPI.clear();
            for (HospitalsDTO hospital : hospitalsDTOS) {
                LatLng latLng = new LatLng(hospital.getLatitude(), hospital.getLongitude());
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(hospital.getHospitalName());
                mapAPI.addMarker(options);
            }
        }
    }

}