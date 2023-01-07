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
import android.widget.Toast;

import com.cb008385.lookgood.databinding.FragmentRegisterBinding;
import com.google.firebase.auth.FirebaseUser;

import ViewModel.AuthenticationViewModel;
import ViewModel.HomeControllerViewModel;
import models.User;

public class RegisterFragment extends Fragment {

    private FragmentRegisterBinding fragmentRegisterBinding;
    private AuthenticationViewModel authenticationViewModel;
    private HomeControllerViewModel homeControllerViewModel;
    SharedPreferences pref;



    public RegisterFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentRegisterBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        initView();
        return fragmentRegisterBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = getActivity().getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        authenticate();
        fragmentRegisterBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String email = fragmentRegisterBinding.registerUsername.getText().toString().trim();
                    String password = fragmentRegisterBinding.registerPassword.getText().toString().trim();
                    String name=fragmentRegisterBinding.registerName.getText().toString().trim();
                    String address=fragmentRegisterBinding.registerAddress.getText().toString().trim();
                    String phone=fragmentRegisterBinding.registerPhone.getText().toString().trim();
                if(email.length()>0 && password.length()>0 && name.length()>0 && address.length()>0 && phone.length()>0) {
                    User user=new User(email,password,name,address,phone);

                    authenticationViewModel.register(user);
                    fragmentRegisterBinding.setIsLoading(true);


                }else {
                    Toast.makeText(getContext(), "Please fill all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });
        fragmentRegisterBinding.loginBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=RegisterFragmentDirections.actionRegisterFragmentToUserFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    private void initView(){
        authenticationViewModel= new ViewModelProvider(this).get(AuthenticationViewModel.class);
        homeControllerViewModel= new ViewModelProvider(getActivity()).get(HomeControllerViewModel.class);


    }

    private void authenticate(){
        authenticationViewModel.getLoginFailure().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                fragmentRegisterBinding.setIsLoading(false);
            }
        });
        authenticationViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                fragmentRegisterBinding.setIsLoading(false);
                savePreferenceData(true);
                if(firebaseUser.getEmail()!=null) {
                    if (firebaseUser.getEmail().equals("gimhanrd19@gmail.com")) {
                        Intent intent = new Intent(getActivity(), AdminActivity.class);
                        startActivity(intent);
                        homeControllerViewModel.finishActivity(true);
                    } else {
                        NavDirections action = RegisterFragmentDirections.actionRegisterFragmentToHomeFragment();
                        Navigation.findNavController(getView()).navigate(action);
                    }
                }

            }
        });

    }

    private void savePreferenceData(boolean item) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isLoggedIn",item);
        editor.commit();


    }

    private boolean restorePreferenceData() {

        boolean isLoggedIn = pref.getBoolean("isLoggedIn",false);
        return  isLoggedIn;

    }
}