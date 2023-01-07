package com.cb008385.lookgood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import com.cb008385.lookgood.databinding.ActivityLauncherBinding;

public class LauncherActivity extends AppCompatActivity {

    private ActivityLauncherBinding activityLauncherBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLauncherBinding= DataBindingUtil.setContentView(this,R.layout.activity_launcher);
        if(restorePreferenceData() || Build.VERSION.SDK_INT < 21){

            Intent homeActivity = new Intent(getApplicationContext(),HomeActivity.class );
            startActivity(homeActivity);
            finish();

        }else{
            Intent splashActivity = new Intent(getApplicationContext(),SplashActivity.class );
            startActivity(splashActivity);
            finish();
        }
    }

    private boolean restorePreferenceData() {


        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        boolean isOpenedBefore = pref.getBoolean("isSplashOpened",false);
        return  isOpenedBefore;



    }
}