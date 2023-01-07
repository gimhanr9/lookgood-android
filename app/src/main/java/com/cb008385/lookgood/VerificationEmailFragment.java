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

import com.cb008385.lookgood.databinding.FragmentVerificationEmailBinding;

import ViewModel.AuthenticationViewModel;
import ViewModel.HomeViewModel;

public class VerificationEmailFragment extends Fragment {

    private FragmentVerificationEmailBinding fragmentVerificationEmailBinding;
    private AuthenticationViewModel authenticationViewModel;

    public VerificationEmailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentVerificationEmailBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_verification_email,container,false);
        initView();
        return fragmentVerificationEmailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authenticationViewModel.getPasswordEmailLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    NavDirections action = VerificationEmailFragmentDirections.actionVerificationEmailFragmentToUserFragment();
                    Navigation.findNavController(getView()).navigate(action);

                }
            }
        });

        fragmentVerificationEmailBinding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=fragmentVerificationEmailBinding.confirmUsername.getText().toString().trim();
                if(email.length()>0){
                    authenticationViewModel.updatePassword(email);

                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Please fill in a valid email address",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        authenticationViewModel=new ViewModelProvider(this).get(AuthenticationViewModel.class);
    }
}