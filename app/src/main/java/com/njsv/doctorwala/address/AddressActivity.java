package com.njsv.doctorwala.address;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;

import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageView;

import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;


import com.github.dhaval2404.imagepicker.ImagePicker;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.njsv.doctorwala.R;
import com.njsv.doctorwala.assistant.CareServiceDetailActivity;
import com.njsv.doctorwala.charges.ServiceChargesActivity;
import com.njsv.doctorwala.address.AddressActivity;
import com.njsv.doctorwala.pharmacy.PharmacyActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.AppSharedPreferences;
import com.njsv.doctorwala.util.ApplicationConstants;
import com.njsv.doctorwala.util.GPSTracker;
import com.njsv.doctorwala.util.MyGlide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddressActivity extends AppCompatActivity implements OnMapReadyCallback {

    private EditText e_address, e_pincode, e_landmrk, id_card;
    private Button upload;
    private String service, categoryId, subCategoryId, date, slotTiming, subCategoryLink, subCategoryName;
    private ImageView uploadDocument;
    private File file;
    private GPSTracker gpsTracker;
    private LinearLayout currentLocationLayout;
    private RelativeLayout relativeLayout;
    private Spinner verificationSpinner;
    private String[] verificationSpinnerList = {"Select ID", "Aadhar Card", "Pancard", "Voter Card", "Other"};
    private String selectedIds;
    LocationManager locationManager;
    String currentPhotoPath;

    ///////////////////////////////////////////
    private GoogleMap mMap;
    private double latitude = 0.0;
    private double longitude = 0.0;
    private Marker marker;
    private Geocoder geocoder;
    private PlacesClient placesClient;
    ///////////////////////////////////////////
    //
    // ///////////////////////////////////////
    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;
    private FusedLocationProviderClient fusedLocationProviderClient;
    ///////////////////////////////////////////
    ///////////////////////////////////////////

    private Boolean CARE_TAKER = false;
    private String assistName = "";
    private String reason = "";
    private String hospitalNamel = "";

    ///////////////////////////////////////////

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initeView();
        checkRunTimePermission();
        locationSetting();
        fusedLocation();

        service = getIntent().getStringExtra("service");
        categoryId = getIntent().getStringExtra("categoryId");
        subCategoryId = getIntent().getStringExtra("subCategoryId");
        date = getIntent().getStringExtra("date");
        slotTiming = getIntent().getStringExtra("slotTiming");
        subCategoryName = getIntent().getStringExtra("subCategoryName");
        subCategoryLink = getIntent().getStringExtra("subCategoryLink");

        CARE_TAKER = getIntent().getExtras().getBoolean("CARE_TAKER");
        assistName = getIntent().getStringExtra("name");
        reason = getIntent().getStringExtra("reason");
        hospitalNamel = getIntent().getStringExtra("hospitalNamel");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Address");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        ArrayAdapter<String> spinnerList = new ArrayAdapter<String>(AddressActivity.this, R.layout.spinner_item, verificationSpinnerList);
        spinnerList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        verificationSpinner.setAdapter(spinnerList);
        verificationSpinner.setElevation(8);
        verificationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    TextView spinnerText = findViewById(R.id.spinner_text);
                    if (!verificationSpinner.getSelectedItem().toString().equals("Select ID")) {
                        spinnerText.setTextColor(Color.rgb(0, 0, 0));
                        selectedIds = verificationSpinner.getSelectedItem().toString();

                        if (selectedIds == "Aadhar Card") {
                            id_card.setHint("Aadhar Card Number");
                            id_card.setInputType(InputType.TYPE_CLASS_NUMBER);
                        } else if (selectedIds == "Pancard") {
                            id_card.setHint("Pancard Number");
                            id_card.setInputType(InputType.TYPE_CLASS_TEXT);
                        } else if (selectedIds == "Voter Card") {
                            id_card.setHint("Voter Card Number");
                            id_card.setInputType(InputType.TYPE_CLASS_TEXT);
                        } else if (selectedIds == "Other") {
                            id_card.setHint("Document Number");
                            id_card.setInputType(InputType.TYPE_CLASS_TEXT);
                        } else {
                            Toast.makeText(AddressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }

                        relativeLayout.setVisibility(View.VISIBLE);
                    } else {
                        spinnerText.setTextColor(Color.rgb(175, 172, 172));
                        selectedIds = "Select ID";
                        relativeLayout.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            if (!Places.isInitialized()) {
                Places.initialize(getApplicationContext(), getString(R.string.map_api), Locale.US);
                //         Create a RectangularBounds object.
                placesClient = Places.createClient(this);
//          Use the builder to create a FindAutocompletePredictionsRequest.
                geocoder = new Geocoder(this, Locale.getDefault());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

            latitude = gpsTracker.getLocation().getLatitude();
            longitude = gpsTracker.getLocation().getLongitude();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            e_address.setText(ApplicationConstants.INSTANCE.escapeMetaCharacters(gpsTracker.getAddressLine(AddressActivity.this)));
            e_pincode.setText(ApplicationConstants.INSTANCE.escapeMetaCharacters(gpsTracker.getPostalCode(AddressActivity.this)));
        } catch (Exception e) {
            e.printStackTrace();
        }


        if (latitude == 0.0 && longitude == 0.0) {

            fusedLocation();

            try {

                latitude = gpsTracker.getLocation().getLatitude();
                longitude = gpsTracker.getLocation().getLongitude();

            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        }


        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        id_card.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (selectedIds == "Aadhar Card") {
                    if (id_card.getText().toString().length() != 12) {
                        id_card.setError("please enter a valid Aadhar Card Number");
                        return;
                    }
                } else if (selectedIds == "Pancard") {
                    if (id_card.getText().toString().length() != 10) {
                        id_card.setError("please enter a valid Pancard Number");
                        return;
                    }
                } else if (selectedIds == "Voter Card") {
                    if (id_card.getText().toString().length() != 10) {
                        id_card.setError("please enter a valid Voter Card Number");
                        return;
                    }
                } else if (selectedIds == "Other") {

                } else {
                    Toast.makeText(AddressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void initeView() {
        e_address = findViewById(R.id.e_address);
        e_pincode = findViewById(R.id.e_pincode);
        e_landmrk = findViewById(R.id.e_landmrk);
        id_card = findViewById(R.id.id_card);
        upload = findViewById(R.id.upload);
        uploadDocument = findViewById(R.id.uploadDocument);
        currentLocationLayout = findViewById(R.id.location_layout);
        relativeLayout = findViewById(R.id.verification_ids);
        verificationSpinner = findViewById(R.id.verification_spinner);
    }

    public void payment(View view) {
        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());

        String id = preferences.getLoginUserLoginId();
        String mobile = preferences.getLoginMobile();
        String add = e_address.getText().toString();
        String pincode = e_pincode.getText().toString();
        String landmark = e_landmrk.getText().toString();
        String aadharNumber = id_card.getText().toString();

        try {
            if (!e_address.getText().toString().isEmpty()) {
                if (!e_pincode.getText().toString().isEmpty()) {
                    if (e_pincode.getText().toString().length() == 6) {
                        if (!e_landmrk.getText().toString().isEmpty()) {
                            if (!selectedIds.equals("Select ID")) {
                                if (!id_card.getText().toString().isEmpty()) {
                                    if (file != null) {
                                        if (latitude == 0.0 && longitude == 0.0) {
                                            locationSetting();
                                        }
                                        if (CARE_TAKER) {
                                            if (!assistName.isEmpty()) {
                                                if (!reason.isEmpty()) {
                                                    if (!hospitalNamel.isEmpty()) {
                                                        String nurOrderId = "CARE" + String.valueOf(Calendar.getInstance().getTimeInMillis());
                                                        nusrsingCare(nurOrderId, assistName, id, mobile, add.replaceAll(",",""), selectedIds, aadharNumber, hospitalNamel, longitude, latitude, reason, file);
                                                    } else {
                                                        Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Toast.makeText(this, "something went wrong", Toast.LENGTH_SHORT).show();
                                            }

                                        } else {
                                            insertdata(id, add.replaceAll(",",""), pincode, landmark, selectedIds, aadharNumber, file, latitude, longitude);
                                        }
                                    } else {
                                        Toast.makeText(AddressActivity.this, "Please upload Document", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    id_card.setError("Enter your Verification ID");
                                }
                            } else {
                                Toast.makeText(this, "Please select type of verification", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            e_landmrk.setError("Enter your landmark");
                        }
                    } else {
                        e_pincode.setError("Please valid pincode");
                    }
                } else {
                    e_pincode.setError("Enter your pincode");
                }
            } else {
                e_address.setError("Enter your address");
            }
        }catch (Exception e){
            e.printStackTrace();
        }



    }

    public void checkRunTimePermission() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(AddressActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    gpsTracker = new GPSTracker(AddressActivity.this);

                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            10);
                }
            } else {
                gpsTracker = new GPSTracker(AddressActivity.this); //GPSTracker is class that is used for retrieve user current location
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void insertdata(String id, String address, String pincode, String landmark, String documentType, String aadharNumber, File file, Double lat, Double log) {


        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            UtilMethods.INSTANCE.updateAddress(this, id, address, pincode, landmark, documentType, aadharNumber, file, lat, log, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {

                    Intent intent = new Intent(AddressActivity.this, ServiceChargesActivity.class);

                    intent.putExtra("service", service);
                    intent.putExtra("categoryId", categoryId);
                    intent.putExtra("subCategoryId", subCategoryId);
                    intent.putExtra("date", date);
                    intent.putExtra("slotTiming", slotTiming);
                    intent.putExtra("subCategoryName", subCategoryName);
                    intent.putExtra("subCategoryLink", subCategoryLink);
                    intent.putExtra("adddress", e_address.getText().toString());
                    intent.putExtra("landmark", e_landmrk.getText().toString());
                    intent.putExtra("pincode", e_pincode.getText().toString());
                    intent.putExtra("aadhar", id_card.getText().toString());

                    startActivity(intent);

                }

                @Override
                public void fail(String from) {
                    Toast.makeText(AddressActivity.this, "error =>" + from, Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

//        // Add a marker in Sydney and move the camera
//        LatLng lko = new LatLng(26.864553, 81.028507);
//        mMap.addMarker(new MarkerOptions().position(lko).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(lko));

        if (latitude != 0.0 && longitude != 0.0) {

            LatLng vapi = new LatLng(latitude, longitude);
            marker = mMap.addMarker(new MarkerOptions().position(vapi).title(""));
            //  marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(vapi));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(vapi, 12.0f));
//            location.setText("address");


        } else {
            LatLng vapi = new LatLng(26.864553, 81.028507);
            marker = mMap.addMarker(new MarkerOptions().position(vapi).title("Lucknow"));
            //  marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(vapi));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(vapi, 12.0f));
//            location.setText("else");
        }

        currentLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latitude = gpsTracker.getLocation().getLatitude();
                longitude = gpsTracker.getLocation().getLongitude();

                marker.remove();

                LatLng current = new LatLng(latitude, longitude);
                marker = mMap.addMarker(new MarkerOptions().position(current).title(""));
                //  marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(current));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current, 12.0f));

                if (marker == null) {
                    marker = mMap.addMarker(new MarkerOptions().position(current));
                } else {
                    marker.setPosition(current);
                }

                try {
                    e_address.setText(ApplicationConstants.INSTANCE.escapeMetaCharacters(gpsTracker.getAddressLine(AddressActivity.this)));
                    e_pincode.setText(ApplicationConstants.INSTANCE.escapeMetaCharacters(gpsTracker.getPostalCode(AddressActivity.this)));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Marker perth = null;
        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (marker == null) {
                    marker = mMap.addMarker(new MarkerOptions().position(latLng));
                } else {
                    marker.setPosition(latLng);
                }
                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                android.location.Address address = addresses.get(0);

                if (address != null) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i) + "\n");
//                        location.setText(sb);

                    }
                    if (address.getAddressLine(0) != null || address.getAddressLine(0).equals("")) ;
//                        location.setText(address.getAddressLine(0));

                    try {
                        e_address.setText(ApplicationConstants.INSTANCE.escapeMetaCharacters(address.getAddressLine(0)));
                        e_pincode.setText(ApplicationConstants.INSTANCE.escapeMetaCharacters(address.getPostalCode()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                }

                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12.0f));
            }
        });
    }

    private void locationSetting() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(AddressActivity.this);

        mSettingsClient.checkLocationSettings(mLocationSettingsRequest).addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                //  yet gps enabled
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                try {
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(AddressActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.d("GPS", "Unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                Log.d("GPS", "Location settings are inadequate, and cannot be fixed here. Fix in Settings.");
                        }
                    }
                })
                .addOnCanceledListener(new OnCanceledListener() {
                    @Override
                    public void onCanceled() {
                        Log.d("GPS", "checkLocationSettings -> onCanceled");
                    }
                });
    }

    private void openGpsEnableSetting() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, REQUEST_ENABLE_GPS);

    }

    private void fusedLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    longitude = location.getLongitude();
                    latitude = location.getLatitude();
                } else {
                    Toast.makeText(AddressActivity.this, "Location value null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void nusrsingCare(String nurOrderId, String name, String userId, String mobile, String address, String documentType, String documentNo, String subname, Double longi, Double lat, String msg, File file) {


        if (UtilMethods.INSTANCE.isNetworkAvialable(this)) {
            UtilMethods.INSTANCE.nursing(this, nurOrderId, name, userId, mobile, address, documentType, documentNo, subname, longi, lat, msg, file, new mCallBackResponse() {
                @Override
                public void success(String from, String message) {

                    Intent intent = new Intent(AddressActivity.this, CareServiceDetailActivity.class);

                    startActivity(intent);

                }

                @Override
                public void fail(String from) {
                    Toast.makeText(AddressActivity.this, "error =>" + from, Toast.LENGTH_SHORT).show();
                }
            });

        } else {

            UtilMethods.INSTANCE.internetNotAvailableMessage(this);
        }

    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AddressActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    dispatchTakePictureIntent();

                } else if (options[item].equals("Choose from Gallery")) {

                    try {

                        Intent pickIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        pickIntent.setType("image/*");
                        startActivityForResult(pickIntent, 2);



                    } catch (ActivityNotFoundException e) {
                        e.printStackTrace();
                        Log.d("Imaage from gallery", e.getMessage());
                        Toast.makeText(AddressActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            file = null;
            try {
                file = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (file != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        file);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 1);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("result code",""+resultCode);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                setPic();

            } else if (requestCode == 2) {
                try {
                    Log.d("result code",""+resultCode);

                    Uri selectedImage = data.getData();
//                    file = new File(selectedImage.getPath());
                    file = new File(getRealPathFromURI(selectedImage));

                    currentPhotoPath = getRealPathFromURI(selectedImage);

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    //use the bitmap as you like
                    uploadDocument.setImageBitmap(bitmap);

//                    documents_images.setImageURI(selectedImage);
                    Log.d("gallery", String.valueOf(file));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //Success Perform Task Here
                    fusedLocation();
                    onBackPressed();
                    Log.d("GPS", "Change this location");
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e("GPS", "User denied to access location");
                    openGpsEnableSetting();
                    break;
            }
        } else if (requestCode == REQUEST_ENABLE_GPS) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

            if (!isGpsEnabled) {
                openGpsEnableSetting();
            } else {
                fusedLocation();
                Log.d("GPS", "Change location");
            }
        }
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = uploadDocument.getWidth();
        int targetH = uploadDocument.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.max(1, Math.min(photoW / targetW, photoH / targetH));

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
        uploadDocument.setImageBitmap(bitmap);
    }

    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

}