package com.njsv.doctorwala.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;

import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.njsv.doctorwala.MainActivity;
import com.njsv.doctorwala.R;

import com.njsv.doctorwala.retrofit.UtilMethods;
import com.njsv.doctorwala.retrofit.mCallBackResponse;
import com.njsv.doctorwala.signup.SignupActivity;
import com.njsv.doctorwala.util.AppSharedPreferences;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText mobileNo, password;
    private TextView tvSignup, forgetPassword;
    private Button btnLogin;
    private ImageView showPwd;
    private Boolean SHOW = false;

    public static final int MULTIPLE_PERMISSIONS = 10; // code you want.

    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    ////////////////////////////  for update app
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;
    private static final int FLEXIBLE_APP_UPDATE_REQ_CODE = 123;
    ////////////////////////////  for update app


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        checkPermissions();

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

        showPwd.setOnClickListener(v -> {
            if (SHOW) {
                // hide password
                password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                SHOW = false;
            } else {
                // show password
                password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                SHOW = true;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String nm = mobileNo.getText().toString();
                    String pass = password.getText().toString();

                    if (formvalidate()) {
                        login(nm, pass);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        forgetPassword.setOnClickListener(view -> {
            Intent forgetIntent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(forgetIntent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        });

        tvSignup.setOnClickListener(view -> {
            Intent tvSignupIntent = new Intent(LoginActivity.this, SignupActivity.class);
            startActivity(tvSignupIntent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        });

    }

    public void login(String nm, String pass) {

        try {
            if (UtilMethods.INSTANCE.isNetworkAvialable(getApplicationContext())) {

                UtilMethods.INSTANCE.login(LoginActivity.this, nm, pass, new mCallBackResponse() {
                    @Override
                    public void success(String from, String message) {

                        Type listType = new TypeToken<ArrayList<LoginData>>() {
                        }.getType();

                        try {
                            AppSharedPreferences preferences = new AppSharedPreferences(getApplication());
                            preferences.setLoginDetails(message);

                            Intent loginIntent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(loginIntent);
                            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void fail(String from) {
                        Toast.makeText(LoginActivity.this, "fails =>" + from, Toast.LENGTH_SHORT).show();
                    }
                });


            } else {

                UtilMethods.INSTANCE.internetNotAvailableMessage(LoginActivity.this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void init() {

        ///////////////// login
        mobileNo = findViewById(R.id.mobile_no);
        password = findViewById(R.id.password);
        btnLogin = findViewById(R.id.btn_login);
        forgetPassword = findViewById(R.id.forget_password);
        tvSignup = findViewById(R.id.text_signup);

        showPwd = findViewById(R.id.show_pwd);
    }

    private boolean formvalidate() {
        if (mobileNo.getText().toString().equals("")) {
            mobileNo.setError("Enter Your Mobile No.");
            return false;
        }

        if (password.getText().toString().equals("")) {
            password.setError("Enter Your Password");
            return false;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(LoginActivity.this, p);
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
            appUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, this, LoginActivity.FLEXIBLE_APP_UPDATE_REQ_CODE);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FLEXIBLE_APP_UPDATE_REQ_CODE) {
            if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "Update canceled by user! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_OK) {
                Toast.makeText(getApplicationContext(), "Update success! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Update Failed! Result Code: " + resultCode, Toast.LENGTH_LONG).show();
                checkUpdate();
            }
        }
    }
}