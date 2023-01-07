package com.cb008385.lookgood;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.cb008385.lookgood.databinding.FragmentAccountBinding;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import Repository.AuthenticationRepository;
import ViewModel.HomeViewModel;
import models.PaymentDetails;
import models.User;

public class AccountFragment extends Fragment {

    private FragmentAccountBinding fragmentAccountBinding;
    private HomeViewModel homeViewModel;
    private DatePickerDialog.OnDateSetListener dateSetListener;


    public AccountFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate layout
        fragmentAccountBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_account,container,false);
        initView();

        return fragmentAccountBinding.getRoot();

    }

    private void initView() {
        homeViewModel=new ViewModelProvider(this).get(HomeViewModel.class);
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.cards_array,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentAccountBinding.cardType.setAdapter(arrayAdapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //observe user data which is passed on from repository
        homeViewModel.userData().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                //data came
                //set data to text-views
                fragmentAccountBinding.setIsVisibleLogin(false);
                fragmentAccountBinding.setName(users.get(users.size()-1).getName());
                fragmentAccountBinding.setEmail(users.get(users.size()-1).getEmail());
                fragmentAccountBinding.setAddress(users.get(users.size()-1).getAddress());
                fragmentAccountBinding.setPhone(users.get(users.size()-1).getPhone());
                fragmentAccountBinding.setIsAvailable(true);

            }
        });
        //get payment data if there is any.
        homeViewModel.paymentData().observe(getViewLifecycleOwner(), new Observer<List<PaymentDetails>>() {
            @Override
            public void onChanged(List<PaymentDetails> paymentDetails) {

                fragmentAccountBinding.setCard(paymentDetails.get(paymentDetails.size()-1).getNumber());

            }
        });

        homeViewModel.getLoggedOutLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){

                    if(fragmentAccountBinding.notLoggedIn.getVisibility()==View.VISIBLE){
                        fragmentAccountBinding.setIsVisibleLogin(false);
                    }

                }else{
                    if(fragmentAccountBinding.detailLayout.getVisibility()==View.VISIBLE) {
                        fragmentAccountBinding.setIsAvailable(false);
                    }
                    if(fragmentAccountBinding.notLoggedIn.getVisibility()==View.GONE){
                        fragmentAccountBinding.setIsVisibleLogin(true);
                    }
                }

            }
        });
        fragmentAccountBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=AccountFragmentDirections.actionAccountFragmentToUserFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });

        fragmentAccountBinding.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentAccountBinding.editorLayout.getVisibility()==View.GONE){
                    fragmentAccountBinding.setIsVisible(true);
                    fragmentAccountBinding.accountName.setText(fragmentAccountBinding.getName());
                    fragmentAccountBinding.accountAddress.setText(fragmentAccountBinding.getAddress());
                    fragmentAccountBinding.accountPhone.setText(fragmentAccountBinding.getPhone());
                }else{
                    fragmentAccountBinding.setIsVisible(false);
                }
            }
        });

        fragmentAccountBinding.updateEmailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = AccountFragmentDirections.actionAccountFragmentToReauthFragment();
                Navigation.findNavController(getView()).navigate(action);
            }
        });

        fragmentAccountBinding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=fragmentAccountBinding.accountName.getText().toString().trim();
                String address=fragmentAccountBinding.accountAddress.getText().toString().trim();
                String phone=fragmentAccountBinding.accountPhone.getText().toString().trim();

                if(name.length()>0 && address.length()>0 && phone.length()>0){
                    User user=new User(fragmentAccountBinding.getEmail(),name,address,phone);
                    homeViewModel.updateUser(user);

                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Please fill all user detail fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fragmentAccountBinding.updateCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store details in variables
                String cardNumber=fragmentAccountBinding.cardNumber.getText().toString().trim();
                String cvv=fragmentAccountBinding.cvvNumber.getText().toString().trim();
                String type=fragmentAccountBinding.cardType.getSelectedItem().toString();
                String date=fragmentAccountBinding.dateText.getText().toString();
                //validate all details
                if(cardNumber.length()>0 && cvv.length()>0){
                    if(cardNumber.length()==16 && cvv.length() ==3 && !date.equals("Select Date")){
                        PaymentDetails paymentDetails=new PaymentDetails(cardNumber,cvv,date,type);
                        //send details through viewmodel to repository, to store them in database
                        homeViewModel.addPaymentDetails(paymentDetails);

                    }else{
                        //display error toast
                        Toast.makeText(getActivity().getApplicationContext(),"Please enter valid card details",Toast.LENGTH_SHORT).show();
                    }
                }else{
                    //display error toast
                    Toast.makeText(getActivity().getApplicationContext(),"Please fill all card details",Toast.LENGTH_SHORT).show();
                }
            }
        });
        // open datepicker dialog
        fragmentAccountBinding.dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance(); //set date format
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH); //usually first day of the month

                DatePickerDialog datePickerDialog=new DatePickerDialog(
                        getActivity(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        //listen for setting date
        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=month + "/" + year;
                //set the selected date in the textview
                fragmentAccountBinding.dateText.setText(date);
            }
        };

    }

    @Override
    public void onPause() {
        super.onPause();

    }
}