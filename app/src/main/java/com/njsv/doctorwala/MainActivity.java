package com.njsv.doctorwala;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.njsv.doctorwala.fragment.BookingFragment;
import com.njsv.doctorwala.fragment.HomeFragment;
import com.njsv.doctorwala.fragment.NursingFragment;
import com.njsv.doctorwala.fragment.PharmacyAdapter;
import com.njsv.doctorwala.fragment.PharmacyFragment;
import com.njsv.doctorwala.fragment.ProfileFragment;
import com.njsv.doctorwala.MainActivity;
import com.njsv.doctorwala.pharmacy.PharmacyActivity;
import com.njsv.doctorwala.util.AppSharedPreferences;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener {
    private FrameLayout frameLayout;
    private int currentFragment = -1;
    private static final int HOME_FRAGMENT = 0;
    private static final int PROFILE_FRAGMENT = 1;
    private static final int BOOKING_FRAGMENT = 2;
    private static final int PHARMACY_FRAGMENT = 3;
    private static final int NURSING_FRAGMENT = 4;
    private BottomNavigationView bottomNavigationView;

    ////////////////////////// Navigation menu
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private Window window;    //for change color toolbar and actionBar
    private TextView name,mobile;
    ////////////////////////// Navigation menu

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    private SettingsClient mSettingsClient;
    private LocationSettingsRequest mLocationSettingsRequest;
    private static final int REQUEST_CHECK_SETTINGS = 214;
    private static final int REQUEST_ENABLE_GPS = 516;

    ////////////////////////////  for update app
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private static final int FLEXIBLE_APP_UPDATE_REQ_CODE = 123;
    ////////////////////////////  for update app


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainlayout);

        locationSetting();
        showNavigation();

        frameLayout = findViewById(R.id.frame_layout);

        TextView verName = findViewById(R.id.ver_name);
        try {
            verName.setText("App Version\n"+getPackageManager().getPackageInfo(getPackageName(),0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //////////////////////////////////////////  for app update
        appUpdateManager = AppUpdateManagerFactory.create(getApplicationContext());
        installStateUpdatedListener = state -> {
            if (state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else if (state.installStatus() == InstallStatus.INSTALLED) {
                removeInstallStateUpdateListener();
            } else {
                Toast.makeText(getApplicationContext(), "InstallStateUpdatedListener: state: " + state.installStatus(), Toast.LENGTH_LONG).show();
            }
        };
        appUpdateManager.registerListener(installStateUpdatedListener);
        checkUpdate();
        //////////////////////////////////////////  for app update


        openFragment("Doctor Wala", new HomeFragment(), HOME_FRAGMENT);


        bottomNavigationView = findViewById(R.id.bottom_menu);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.bottom_home:
                    openFragment("Doctor Wala", new HomeFragment(), HOME_FRAGMENT);
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                    navigationView.getMenu().getItem(0).setChecked(true);
                    break;
                case R.id.bottom_profile:
                    openFragment("Profile", new ProfileFragment(), PROFILE_FRAGMENT);
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    navigationView.getMenu().getItem(1).setChecked(true);
                    break;
                case R.id.bottom_booking:
                    openFragment("Booking History", new BookingFragment(), BOOKING_FRAGMENT);
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    navigationView.getMenu().getItem(2).setChecked(true);
                    break;

                case R.id.bottom_pharmacy:
                    openFragment("Pharmacy", new PharmacyFragment(), PHARMACY_FRAGMENT);
                    bottomNavigationView.getMenu().getItem(3).setChecked(true);
                    navigationView.getMenu().getItem(3).setChecked(true);
                    break;
            }
            return false;
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (checkPermissions()) {
            // permissions granted.
        } else {
            // show dialog informing them that we lack certain permissions
        }
    }

    public void openFragment(String title, Fragment fragment, int fragmentNo) {
//        appName.setText(title);
        if (fragmentNo != currentFragment) {
//                window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));
//                toolbar.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            ////for change color toolbar and actionBar
            currentFragment = fragmentNo;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(frameLayout.getId(), fragment);
            fragmentTransaction.commit();
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(MainActivity.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //  permissions granted.
                } else {

                    // no permissions granted.
                }
                return;
            }
        }
    }

    private void alertWarning() {
        new SweetAlertDialog(MainActivity.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Logout")
                .setContentText("Are you sure want to logout?")
                .setConfirmText("Logout")
                .setConfirmClickListener(sweetAlertDialog -> {
                    new AppSharedPreferences(getApplication()).logout(MainActivity.this);
                    sweetAlertDialog.dismissWithAnimation();
                })
                .setCancelButton("Cancel", SweetAlertDialog::dismissWithAnimation).show();
    }

    @Override
    public void onBackPressed() {

        if (currentFragment == HOME_FRAGMENT) {
            currentFragment = -1;
            super.onBackPressed();

        } else {
            openFragment("Doctor Wala", new HomeFragment(), HOME_FRAGMENT);
            bottomNavigationView.getMenu().getItem(0).setChecked(true);
        }
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        ///    for home fragment slider
    }

    private void locationSetting() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY));
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();

        mSettingsClient = LocationServices.getSettingsClient(MainActivity.this);

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
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    //Success Perform Task Here

//                    fusedLocation();
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
//                fusedLocation();
                Log.d("GPS", "Change location");
            }
        }

        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(),"Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
    }

    ////////////////////////  Navigation menu   //////////////////////////////////////

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void showNavigation(){

        setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        navigationView = findViewById(R.id.navigation_menu);

        name = navigationView.getHeaderView(0).findViewById(R.id.name_header);
        mobile = navigationView.getHeaderView(0).findViewById(R.id.mobile_header);

        AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
        name.setText(preferences.getLoginUserName());
        mobile.setText(preferences.getLoginMobile());

        window = getWindow();   ////for change color toolbar and actionBar
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

        navigationView.bringToFront();       // intent navigation menu
        navigationView.getMenu().getItem(0).setChecked(true);

        final MenuItem[] menuItems = new MenuItem[1];
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                drawerLayout.closeDrawer(GravityCompat.START);
                drawerLayout.closeDrawers();
                menuItems[0] = menuItem;

                drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
                    @Override
                    public void onDrawerClosed(View drawerView) {
                        super.onDrawerClosed(drawerView);
                        int id = menuItems[0].getItemId();
                    }
                });

                switch (menuItem.getItemId()){
                    case R.id.home:
                        openFragment("Doctor Wala", new HomeFragment(), HOME_FRAGMENT);
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
                        navigationView.getMenu().getItem(0).setChecked(true);
                        return true;
                    case R.id.profile:
                        openFragment("Profile", new ProfileFragment(), PROFILE_FRAGMENT);
                        bottomNavigationView.getMenu().getItem(1).setChecked(true);
                        navigationView.getMenu().getItem(1).setChecked(true);
                        return true;
                    case R.id.service_booking:
                        openFragment("Booking History", new BookingFragment(), BOOKING_FRAGMENT);
                        bottomNavigationView.getMenu().getItem(2).setChecked(true);
                        navigationView.getMenu().getItem(2).setChecked(true);
                        return true;
                    case R.id.pharmacy_booking:
                        openFragment("Pharmacy", new PharmacyFragment(), PHARMACY_FRAGMENT);
                        bottomNavigationView.getMenu().getItem(3).setChecked(true);
                        navigationView.getMenu().getItem(3).setChecked(true);
                        return true;
                    case R.id.assistant_nursing:
                        openFragment("Care Taker", new NursingFragment(), NURSING_FRAGMENT);
                        bottomNavigationView.getMenu().getItem(0).setChecked(true);
                        navigationView.getMenu().getItem(4).setChecked(true);
                        return true;
                    case R.id.log_out:
                        alertWarning();
                        return true;
                }
                return false;
            }
        });

        //this code for toggle
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        toggle=new ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.Open,R.string.Close);     //open and close string create on string.xml
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        drawerLayout.addDrawerListener(toggle);


    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item)){
            return true;
        }
//        if (item.getItemId() == R.id.logout){
//            new AppSharedPreferences(getApplication()).logout(MainActivity.this);
//        }
        return super.onOptionsItemSelected(item);
    }

    ////////////////////////////  for app update
    private void checkUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {
                startUpdateFlow(appUpdateInfo);
            } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            }
        });
    }

    private void startUpdateFlow(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, MainActivity.FLEXIBLE_APP_UPDATE_REQ_CODE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void popupSnackBarForCompleteUpdate() {
        Snackbar.make(findViewById(android.R.id.content).getRootView(), "New app is ready!", Snackbar.LENGTH_INDEFINITE)

                .setAction("Install", view -> {
                    if (appUpdateManager != null) {
                        appUpdateManager.completeUpdate();
                    }
                })
                .setActionTextColor(getResources().getColor(R.color.purple_500))
                .show();
    }

    private void removeInstallStateUpdateListener() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(installStateUpdatedListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        removeInstallStateUpdateListener();
    }

    ////////////////////////////  for app update

    ////////////////////////  Navigation menu   //////////////////////////////////////

}