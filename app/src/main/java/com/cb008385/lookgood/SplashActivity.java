package com.cb008385.lookgood;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cb008385.lookgood.databinding.ActivitySplashBinding;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import Adapter.OnboardingAdapter;
import ViewModel.SplashViewModel;
import models.OnboardingItem;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding activitySplashBinding;
    private SplashViewModel splashViewModel;
    private OnboardingAdapter onboardingAdapter;
    private List<OnboardingItem> splashDetails=new ArrayList<>();
    private ViewPager2 viewPager2;
    private LinearLayout onboardingIndicator;
    private MaterialButton materialButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activitySplashBinding= DataBindingUtil.setContentView(this,R.layout.activity_splash);
        onboardingIndicator=activitySplashBinding.onboardingIndicatorDots;
        materialButton=activitySplashBinding.onboardingAction;
        //items();

        initView();
        viewPager2=activitySplashBinding.onboardingViewpager;
        viewPager2.setAdapter(onboardingAdapter);
       // setOnboardingIndicator();
       // setCurrentOnboardingIndicator(0);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentOnboardingIndicator(position);
            }
        });

        materialButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager2.getCurrentItem()+1<onboardingAdapter.getItemCount()){
                    viewPager2.setCurrentItem(viewPager2.getCurrentItem()+1);

                }else{
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    savePreferenceData();
                    finish();

                }
            }
        });


    }

    private void initView(){
        splashViewModel=new ViewModelProvider(this).get(SplashViewModel.class);
        onboardingAdapter=new OnboardingAdapter(splashDetails);
        getDetails();



    }

    private void getDetails(){
        splashViewModel.getDetails().observe(this,data->{
            splashDetails.addAll(data);
            onboardingAdapter.notifyDataSetChanged();
            setOnboardingIndicator();
            setCurrentOnboardingIndicator(0);
        });

    }

    private void setOnboardingIndicator(){
        ImageView [] indicators=new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for(int i=0;i<indicators.length;i++){
            indicators[i]=new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            onboardingIndicator.addView(indicators[i]);
        }
    }

    private void setCurrentOnboardingIndicator(int position){
        int childCount=onboardingIndicator.getChildCount();
        for(int i=0;i<childCount;i++){
            ImageView imageView=(ImageView) onboardingIndicator.getChildAt(i);
            if(i==position){
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_active));

            }else{
                imageView.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive));

            }
        }
        if(position==onboardingAdapter.getItemCount()-1){
            materialButton.setText("Start");
        }else{
            materialButton.setText("Next");
        }
    }


    private void savePreferenceData() {

        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isSplashOpened",true);
        editor.commit();


    }



}