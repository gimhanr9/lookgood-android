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

import com.cb008385.lookgood.databinding.FragmentLoginBinding;
import com.google.firebase.auth.FirebaseUser;

import ViewModel.AuthenticationViewModel;
import ViewModel.HomeControllerViewModel;

public class LoginFragment extends Fragment {

    private AuthenticationViewModel authenticationViewModel;
    private HomeControllerViewModel homeControllerViewModel;
    private FragmentLoginBinding fragmentLoginBinding;
    SharedPreferences pref;

    public LoginFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);
        initView();
        return fragmentLoginBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pref = getActivity().getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);
        authenticate();

        fragmentLoginBinding.forgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=LoginFragmentDirections.actionUserFragmentToVerificationEmailFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });

        fragmentLoginBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    String email = fragmentLoginBinding.loginUsername.getText().toString().trim();
                    String password = fragmentLoginBinding.loginPassword.getText().toString().trim();
                    if(email.length()>0 && password.length()>0) {
                        authenticationViewModel.login(email, password);
                        fragmentLoginBinding.setIsLoading(true);

                    }else {
                        Toast.makeText(getContext(), "Email Address and Password Must Be Entered", Toast.LENGTH_SHORT).show();
                    }

            }
        });

        fragmentLoginBinding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=LoginFragmentDirections.actionUserFragmentToRegisterFragment();
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
                fragmentLoginBinding.setIsLoading(false);
            }
        });
        authenticationViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                fragmentLoginBinding.setIsLoading(false);
                if (firebaseUser != null) {
                    savePreferenceData(true);
                    if(firebaseUser.getEmail()!=null) {
                        if (firebaseUser.getEmail().equals("gimhanrd19@gmail.com")) {
                            Intent intent = new Intent(getActivity(), AdminActivity.class);
                            startActivity(intent);
                            homeControllerViewModel.finishActivity(true);

                        } else {
                            NavDirections action = LoginFragmentDirections.actionUserFragmentToHomeFragment();
                            Navigation.findNavController(getView()).navigate(action);
                        }
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