package com.njsv.doctorwala.pharmacy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

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

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.njsv.doctorwala.MainActivity;
import com.njsv.doctorwala.R;

import com.njsv.doctorwala.orderHistory.DescriptionShowActivity;
import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.util.AppSharedPreferences;
import com.njsv.doctorwala.util.GPSTracker;
import com.njsv.doctorwala.util.MyGlide;
import com.github.dhaval2404.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PharmacyActivity extends AppCompatActivity {
    private TextView upload_documents;
    private ImageView documents_images;
    private EditText name, address, message;
    private Button btnsubmit;
    private Boolean filestaus = false;
    private File file;
    private String categoryId, categoryName, categoryImage;
    private int position;
    private AppSharedPreferences preferences;
    private GPSTracker gpsTracker;
    private double latitude = 0.0;
    private double longitude = 0.0;

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;
    private String homeOrder="no";

    private FusedLocationProviderClient fusedLocationProviderClient;
    String currentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);
        inite();
        checkRunTimePermission();
        locationSetting();
        fusedLocation();

        categoryId = getIntent().getStringExtra("categoryId");
        categoryName = getIntent().getStringExtra("categoryName");
        categoryImage = getIntent().getStringExtra("categoryImage");
        homeOrder = getIntent().getStringExtra("homeOrder");
        position = getIntent().getIntExtra("position", -1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Pharmacy");

        preferences = new AppSharedPreferences(getApplication());
        upload_documents.setOnClickListener(view -> selectImage());

        try {

            latitude = gpsTracker.getLocation().getLatitude();
            longitude = gpsTracker.getLocation().getLongitude();

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (latitude == 0.0 && longitude == 0.0) {

            try {

                latitude = gpsTracker.getLocation().getLatitude();
                longitude = gpsTracker.getLocation().getLongitude();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }


        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (name.getText().toString().isEmpty()) {
                    name.setError("Enter your name");
                    return;
                }
                if (message.getText().toString().isEmpty()) {
                    message.setError("Enter your message");
                    return;
                }
                if (file == null) {
                    Toast.makeText(PharmacyActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
                }
                if (latitude == 0.0 || longitude == 0.0) {
                    Toast.makeText(PharmacyActivity.this, "Please open gps location", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PharmacyActivity.this, MainActivity.class);
                    startActivity(intent);
                }

                try {
                    if (UtilMethods.INSTANCE.isNetworkAvialable(getApplicationContext())) {
                        String parchaId = "PRE" + String.valueOf(Calendar.getInstance().getTimeInMillis());

                        UtilMethods.INSTANCE.pharmacy(PharmacyActivity.this, escapeMetaCharacters(name.getText().toString()), preferences.getLoginMobile(), parchaId, escapeMetaCharacters(message.getText().toString()), escapeMetaCharacters(address.getText().toString()), latitude, longitude, file, new mCallBackResponse() {
                            @Override
                            public void success(String from, String message) {
                                filestaus = true;

                                Intent intent = new Intent(PharmacyActivity.this, DescriptionShowActivity.class);
                                startActivity(intent);
                                finishAffinity();
                            }

                            @Override
                            public void fail(String from) {
                                Toast.makeText(PharmacyActivity.this, "error" + from, Toast.LENGTH_SHORT).show();
                                Log.d("uploadError",from);
                            }
                        });
                    } else {

                        UtilMethods.INSTANCE.internetNotAvailableMessage(getApplicationContext());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (filestaus == true) {
                    Toast.makeText(PharmacyActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void inite() {
        upload_documents = findViewById(R.id.upload_documents);
        documents_images = findViewById(R.id.documents_images);

        name = findViewById(R.id.name);
        address = findViewById(R.id.address);
        message = findViewById(R.id.message);
        btnsubmit = findViewById(R.id.submit);
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK && data != null) {
//
//            switch (requestCode) {
//
//                case 101: {
//
//                    documents_images.setVisibility(View.VISIBLE);
//                    MyGlide.with(PharmacyActivity.this, String.valueOf(data.getData()), (ImageView) findViewById(R.id.documents_images));
//                    new SweetAlertDialog(PharmacyActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                            .setTitleText("Success")
//                            .setContentText("documents Uploaded Successfully......")
//                            .show();
//                    file = new File(data.getData().getPath());
//
//                    break;
//                }
//            }
//        }
//
//        if (requestCode == REQUEST_CHECK_SETTINGS) {
//            switch (resultCode) {
//                case Activity.RESULT_OK:
//                    //Success Perform Task Here
//                    startActivity(getIntent());
//                    finish();
//                    fusedLocation();
//                    Log.d("GPS", "Change this location");
//                    break;
//                case Activity.RESULT_CANCELED:
//                    Log.e("GPS", "User denied to access location");
//                    openGpsEnableSetting();
//                    break;
//            }
//        } else if (requestCode == REQUEST_ENABLE_GPS) {
//            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//            boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//
//            if (!isGpsEnabled) {
//                openGpsEnableSetting();
//            } else {
//                fusedLocation();
//                Log.d("GPS", "Change location");
//            }
//        }
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PharmacyActivity)) return false;
        PharmacyActivity that = (PharmacyActivity) o;
        return position == that.position && Objects.equals(upload_documents, that.upload_documents) && Objects.equals(documents_images, that.documents_images) && Objects.equals(name, that.name) && Objects.equals(address, that.address) && Objects.equals(message, that.message) && Objects.equals(btnsubmit, that.btnsubmit) && Objects.equals(filestaus, that.filestaus) && Objects.equals(file, that.file) && Objects.equals(categoryId, that.categoryId) && Objects.equals(categoryName, that.categoryName) && Objects.equals(categoryImage, that.categoryImage) && Objects.equals(preferences, that.preferences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(upload_documents, documents_images, name, address, message, btnsubmit, filestaus, file, categoryId, categoryName, categoryImage, position, preferences);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public String escapeMetaCharacters(String inputString) {
        final String[] metaCharacters = {"\\", "^", "$", "{", "}", "[", "]", "(", ")", ".", "*", "+", "?", "|", "<", ">", "-", "&", "%", "'"};

        for (int i = 0; i < metaCharacters.length; i++) {
            if (inputString.contains(metaCharacters[i])) {
                inputString = inputString.replace(metaCharacters[i], "\\" + metaCharacters[i]);
            }
        }
        return inputString;
    }

    public void checkRunTimePermission() {

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ActivityCompat.checkSelfPermission(PharmacyActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(PharmacyActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    gpsTracker = new GPSTracker(PharmacyActivity.this);

                } else {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                            10);
                }
            } else {
                gpsTracker = new GPSTracker(PharmacyActivity.this); //GPSTracker is class that is used for retrieve user current location
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void locationSetting() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(PharmacyActivity.this);

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
                                    rae.startResolutionForResult(PharmacyActivity.this, REQUEST_CHECK_SETTINGS);
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
                    Toast.makeText(PharmacyActivity.this, "Location value null", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void selectImage() {

        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(PharmacyActivity.this);
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
                        Toast.makeText(PharmacyActivity.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressLint("LongLogTag")
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                setPic();

            } else if (requestCode == 2) {
                try {

                    Uri selectedImage = data.getData();
//                    file = new File(selectedImage.getPath());
                    file = new File(getRealPathFromURI(selectedImage));

                    currentPhotoPath = getRealPathFromURI(selectedImage);

                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImage);
                    //use the bitmap as you like
                    documents_images.setImageBitmap(bitmap);

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
                    startActivity(getIntent());
                    finish();
                    fusedLocation();
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
                Log.d("files",file.toString());
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

    private void setPic() {
        // Get the dimensions of the View
        int targetW = documents_images.getWidth();
        int targetH = documents_images.getHeight();

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
        documents_images.setImageBitmap(bitmap);
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