package com.njsv.doctorwala;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.ChangeImageTransform;
import android.view.Window;
import android.view.WindowManager;
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
import com.njsv.doctorwala.login.LoginActivity;
import com.njsv.doctorwala.util.AppSharedPreferences;


public class Splash extends AppCompatActivity {
    private int INTERNET_PERMISSION = 1;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ////////////////  for transition
        // inside your activity (if you did not enable transitions in your theme)
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        // set an exit transition
        getWindow().setExitTransition(new ChangeImageTransform());
        ////////////////  for transition
        setContentView(R.layout.activity_splash);

        ///////////////////// hide status bar
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        if (ContextCompat.checkSelfPermission(Splash.this, Manifest.permission.INTERNET)== PackageManager.PERMISSION_GRANTED){

        }else{
            requestStoragePermission();
        }

        try{
            new Handler().postDelayed(() -> {
                AppSharedPreferences preferences = new AppSharedPreferences(getApplication());

                String user = preferences.getLoginDetails();
                Intent intent;
                if (user == null || user.isEmpty()) {
                    intent = new Intent(Splash.this, LoginActivity.class);
                    //                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
                else {
                    intent = new Intent(Splash.this, MainActivity.class);
                    //                    overridePendingTransition(R.anim.enter, R.anim.exit);
                }
//                startActivity(intent, ActivityOptions.makeSceneTransitionAnimation(Splash.this).toBundle());
                finish();
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            }, 3000);
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void requestStoragePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.INTERNET)){
            new AlertDialog.Builder(this).setTitle("Permission needed").setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        }else {
            ActivityCompat.requestPermissions(Splash.this,new String[] {Manifest.permission.INTERNET,Manifest.permission.ACCESS_NETWORK_STATE},INTERNET_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == INTERNET_PERMISSION){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}