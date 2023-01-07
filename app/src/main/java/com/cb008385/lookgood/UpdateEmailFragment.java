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

import com.cb008385.lookgood.databinding.FragmentUpdateEmailBinding;

import ViewModel.HomeViewModel;


public class UpdateEmailFragment extends Fragment {
    private FragmentUpdateEmailBinding fragmentUpdateEmailBinding;
    private HomeViewModel homeViewModel;

    public UpdateEmailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentUpdateEmailBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_update_email,container,false);
        initView();
        return  fragmentUpdateEmailBinding.getRoot();
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        homeViewModel.getEmailUpdateData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    NavDirections action = UpdateEmailFragmentDirections.actionUpdateEmailFragmentToAccountFragment();
                    Navigation.findNavController(getView()).navigate(action);
                }
            }
        });
        fragmentUpdateEmailBinding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=fragmentUpdateEmailBinding.updateUsername.getText().toString().trim();
                if(email.length()>0){
                    homeViewModel.updateEmail(email);


                }else{
                    Toast.makeText(getActivity(),"Please fill in a valid email address",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fragmentUpdateEmailBinding.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = UpdateEmailFragmentDirections.actionUpdateEmailFragmentToAccountFragment();
                Navigation.findNavController(getView()).navigate(action);

            }
        });
    }

    private void initView() {
        homeViewModel=new ViewModelProvider(this).get(HomeViewModel.class);
    }
}