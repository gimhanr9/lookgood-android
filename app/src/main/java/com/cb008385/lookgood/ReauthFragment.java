package com.cb008385.lookgood;

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

import com.cb008385.lookgood.databinding.FragmentReauthBinding;

import ViewModel.HomeViewModel;

public class ReauthFragment extends Fragment {

    private FragmentReauthBinding fragmentReauthBinding;
    private HomeViewModel homeViewModel;
    private String choice;


    public ReauthFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentReauthBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_reauth,container,false);
        if(savedInstanceState!=null){
            choice=savedInstanceState.getString("choice");
        }
        initView();
        return fragmentReauthBinding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel.getReauthenticationData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    if(choice.equals("email")) {
                        NavDirections action = ReauthFragmentDirections.actionReauthFragmentToUpdateEmailFragment();
                        Navigation.findNavController(getView()).navigate(action);
                    }else{
                        NavDirections action = ReauthFragmentDirections.actionReauthFragmentToResetPasswordFragment();
                        Navigation.findNavController(getView()).navigate(action);
                    }

                }
            }
        });

        fragmentReauthBinding.changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice="password";
                reAuthenticate();
            }
        });

        fragmentReauthBinding.changeEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choice="email";
                reAuthenticate();
            }
        });

    }

    private void initView() {
        homeViewModel=new ViewModelProvider(this).get(HomeViewModel.class);
    }

    private void reAuthenticate() {
        String password=fragmentReauthBinding.reauthPassword.getText().toString();
        if(password.length()>0){
            homeViewModel.reauthenticate(password);


        }else{
            Toast.makeText(getActivity().getApplicationContext(),"Please fill in a proper password",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("choice",choice);
    }
}