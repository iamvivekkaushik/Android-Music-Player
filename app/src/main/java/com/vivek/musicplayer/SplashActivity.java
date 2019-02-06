package com.vivek.musicplayer;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.vivek.musicplayer.player.PlayerState;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    TextView appName;
    View parentLayout;
    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        parentLayout = findViewById(R.id.parentLayout);

        // Initialize text View object
        appName = findViewById(R.id.appName);
        // Set the custom font
        appName.setTypeface(Typeface.createFromAsset(getAssets(), "Roboto-Regular.ttf"));

        PlayerState.setMusicPlaying(false);

        if(checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));

            //Finish the current Activity
            SplashActivity.this.finish();
        } else{
            getPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case EXTERNAL_STORAGE_PERMISSION_CONSTANT: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    SplashActivity.this.startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    //Finish the current Activity
                    SplashActivity.this.finish();
                } else {
                    Snackbar snackbar = Snackbar.make(parentLayout, "Permission Denied",
                            Snackbar.LENGTH_LONG).setAction("Setting", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.fromParts("package", getPackageName(), null));
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    });

                    snackbar.show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public boolean checkPermission(String permission){
        //check if permission is granted
        if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
            //You have permission
            return true;
        }

        //You don't have permission
        return false;
    }

    public void getPermission(String permission) {
        // No explanation needed, we can request the permission.

        ActivityCompat.requestPermissions(this,
                new String[]{permission},
                EXTERNAL_STORAGE_PERMISSION_CONSTANT);
    }
}
