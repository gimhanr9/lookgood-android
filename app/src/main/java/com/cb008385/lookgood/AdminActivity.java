package com.cb008385.lookgood;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.cb008385.lookgood.databinding.ActivityAdminBinding;
import com.google.android.material.navigation.NavigationView;

import ViewModel.HomeControllerViewModel;

public class AdminActivity extends AppCompatActivity {

    private ActivityAdminBinding activityAdminBinding;
    private NavController navController;
    private NavigationView navView;
    private Toolbar mToolbar;
    private AppBarConfiguration appBarConfiguration;
    private HomeControllerViewModel homeControllerViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set layout
        activityAdminBinding= DataBindingUtil.setContentView(this,R.layout.activity_admin);
        mToolbar=activityAdminBinding.toolbarMain;
        //set toolbar as action bar
        setSupportActionBar(mToolbar);


        homeControllerViewModel= new ViewModelProvider(this).get(HomeControllerViewModel.class);//initialize viewmodel
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_admin);
        navController = navHostFragment.getNavController();
        appBarConfiguration =
                new AppBarConfiguration.Builder(navController.getGraph())
                        .build();

        NavigationUI.setupActionBarWithNavController(this,navController,appBarConfiguration);//set functionality to appbar and navigation drawer
        //observe boolean value from child fragment whether to finish activity. Needed when logged in as Admin
        homeControllerViewModel.canFinish().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    finish();// finish activity if true
                }
            }
        });






    }

    @Override
    public boolean onSupportNavigateUp() {
        //add navigation features such as back arrow in fragments
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_admin);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }




}
