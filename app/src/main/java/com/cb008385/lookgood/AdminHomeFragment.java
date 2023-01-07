package com.cb008385.lookgood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cb008385.lookgood.databinding.FragmentAdminHomeBinding;

import ViewModel.AdminViewModel;
import ViewModel.HomeControllerViewModel;
import ViewModel.PurchasesViewModel;

public class AdminHomeFragment extends Fragment {
    private HomeControllerViewModel homeControllerViewModel;
    private FragmentAdminHomeBinding fragmentAdminHomeBinding;
    private AdminViewModel adminViewModel;
    SharedPreferences pref;

    public AdminHomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAdminHomeBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_admin_home,container,false);
        initView();
        return fragmentAdminHomeBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //observe if admin is logged out, triggered when logout is clicked

        adminViewModel.getLoggedOutLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    savePreferenceData(false);
                    //if admin is logged out then navigate back to HomeActivity hence HomeFragment as a guest
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    startActivity(intent);
                    homeControllerViewModel.finishActivity(true);

                }
            }
        });

        fragmentAdminHomeBinding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProductManager.class);// go to ProductManager to add products
                startActivity(intent);
            }
        });
        pref = getActivity().getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);

        fragmentAdminHomeBinding.btnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = AdminHomeFragmentDirections.actionAdminHomeFragmentToViewProductFragment();// navigate to view all products
                Navigation.findNavController(view).navigate(action);

            }
        });

        fragmentAdminHomeBinding.btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adminViewModel.logOut();
            } //click logout
        });
    }

    private void initView() {
        homeControllerViewModel= new ViewModelProvider(getActivity()).get(HomeControllerViewModel.class);// initialize viewmodel to share data between fragment and activity
        adminViewModel=new ViewModelProvider(this).get(AdminViewModel.class);// viewmodel used to add products and manage products

    }

    private void savePreferenceData(boolean item) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isLoggedIn",item);
        editor.apply();


    }
}