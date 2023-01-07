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

import com.cb008385.lookgood.databinding.FragmentResetPasswordBinding;

import ViewModel.HomeViewModel;

public class ResetPasswordFragment extends Fragment {

    private FragmentResetPasswordBinding fragmentResetPasswordBinding;
    private HomeViewModel homeViewModel;

    public ResetPasswordFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentResetPasswordBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_reset_password,container,false);
        initView();
        return fragmentResetPasswordBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel.getPasswordUpdateData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean) {
                    NavDirections action = ResetPasswordFragmentDirections.actionResetPasswordFragmentToAccountFragment();
                    Navigation.findNavController(getView()).navigate(action);
                }
            }
        });
        fragmentResetPasswordBinding.resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String pass1=fragmentResetPasswordBinding.updatePassword.getText().toString().trim();
                String pass2=fragmentResetPasswordBinding.confirmPassword.getText().toString().trim();
                if(pass1.length()>0 && pass2.length()>0){
                    if(pass1.equals(pass2)){
                        homeViewModel.updatePassword(pass1);


                    }else{
                        Toast.makeText(getActivity().getApplicationContext(),"Passwords do not match",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Please fill all fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fragmentResetPasswordBinding.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void initView() {
        homeViewModel=new ViewModelProvider(this).get(HomeViewModel.class);

    }

}