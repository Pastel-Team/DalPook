package com.pastel.dalpook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(!checkPermission()){
            requestSapPermissions();
        }
    }

    private boolean checkPermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }

        return true;
    }

    private void requestSapPermissions() {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

        }catch (Exception ignored){
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        if(checkPermission()){
            //Sap_Func.setNotiBarLockScreen(this, false);
            //Sap_act_main_launcher.initsapStart(this, "pastel.team.official", true, true);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    final Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }, 500);

        }
    }
}