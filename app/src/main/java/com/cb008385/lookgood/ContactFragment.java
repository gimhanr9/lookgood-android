package com.cb008385.lookgood;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cb008385.lookgood.databinding.FragmentContactBinding;

public class ContactFragment extends Fragment {
    private FragmentContactBinding fragmentContactBinding;
    private static final int REQUEST_CALL=1;

    public ContactFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentContactBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_contact,container,false);
        return fragmentContactBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentContactBinding.callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makePhoneCall();
            }
        });
    }

    private void makePhoneCall(){
        if(ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CALL_PHONE},REQUEST_CALL);
           //request permission to take call
        }else{
            //permission already granted
            String number="0770599068";
            String dial="tel:"+ number;
            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));  // dial the number
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode==REQUEST_CALL){
            if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                makePhoneCall(); //permission granted
            }else{
                Toast.makeText(getActivity().getApplicationContext(),"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }
}