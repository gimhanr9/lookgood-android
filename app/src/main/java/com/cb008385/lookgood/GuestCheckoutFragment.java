package com.cb008385.lookgood;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
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

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.cb008385.lookgood.databinding.FragmentGuestCheckoutBinding;

import java.util.Calendar;
import java.util.List;

import ViewModel.PurchasesViewModel;
import ViewModel.StockOutViewModel;
import models.AvailabilityModel;
import models.GuestItem;
import models.ProductItem;
import models.PurchasesItem;

public class GuestCheckoutFragment extends Fragment {

    private FragmentGuestCheckoutBinding fragmentGuestCheckoutBinding;
    private PurchasesViewModel purchasesViewModel;
    private StockOutViewModel stockOutViewModel;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private List<AvailabilityModel> nonPurchasedItems;


    public GuestCheckoutFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentGuestCheckoutBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_guest_checkout,container,false);
        initView();
        return fragmentGuestCheckoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String id=GuestCheckoutFragmentArgs.fromBundle(getArguments()).getProductId();
        String productImage=GuestCheckoutFragmentArgs.fromBundle(getArguments()).getProductImage();
        String productName=GuestCheckoutFragmentArgs.fromBundle(getArguments()).getProductName();
        String size=GuestCheckoutFragmentArgs.fromBundle(getArguments()).getSize();
        int quantity=GuestCheckoutFragmentArgs.fromBundle(getArguments()).getQuantity();
        double price=(double) GuestCheckoutFragmentArgs.fromBundle(getArguments()).getPrice();
        PurchasesItem finalizedItem=new PurchasesItem(id,productImage,productName,size,quantity,price,false);

        purchasesViewModel.getNonPurchased().observe(getViewLifecycleOwner(), new Observer<List<AvailabilityModel>>() {
            @Override
            public void onChanged(List<AvailabilityModel> availabilityModels) {
                nonPurchasedItems=availabilityModels;
            }
        });

        fragmentGuestCheckoutBinding.dateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
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

        dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=month + "/" + year;
                fragmentGuestCheckoutBinding.dateText.setText(date);
            }
        };

        fragmentGuestCheckoutBinding.confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=fragmentGuestCheckoutBinding.accountName.getText().toString().trim();
                String email=fragmentGuestCheckoutBinding.emailAddress.getText().toString().trim();
                String address=fragmentGuestCheckoutBinding.accountAddress.getText().toString().trim();
                String phone=fragmentGuestCheckoutBinding.accountPhone.getText().toString().trim();
                String cardNumber=fragmentGuestCheckoutBinding.cardNumber.getText().toString().trim();
                String cvv=fragmentGuestCheckoutBinding.cvvNumber.getText().toString().trim();
                String type=fragmentGuestCheckoutBinding.cardType.getSelectedItem().toString();
                String date=fragmentGuestCheckoutBinding.dateText.getText().toString();

                if(name.length()>0 && email.length()>0 && address.length()>0 && phone.length()>0 && cardNumber.length()>0 && cvv.length()>0
                 && !date.equals("Select Date") && type.length()>0){

                    GuestItem guestItem=new GuestItem(email,name,address,phone);
                    purchasesViewModel.purchaseGuest(finalizedItem,guestItem);
                    ProgressDialog progressDialog=new ProgressDialog(getActivity());
                    progressDialog.show();
                    progressDialog.setContentView(R.layout.progress_dialog);

                    progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    Runnable progressRunnable = new Runnable() {

                        @Override
                        public void run() {

                            progressDialog.dismiss();
                            if(nonPurchasedItems!=null) {
                                stockOutViewModel.setNonPurchases(nonPurchasedItems);
                            }
                            NavDirections action=GuestCheckoutFragmentDirections.actionGuestCheckoutFragmentToOrderConfirmationFragment();
                            Navigation.findNavController(view).navigate(action);

                        }
                    };

                    Handler pdCanceller = new Handler();
                    pdCanceller.postDelayed(progressRunnable, 5000);

                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Please fill all the fields!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initView() {
        purchasesViewModel= new ViewModelProvider(this).get(PurchasesViewModel.class);
        stockOutViewModel=new ViewModelProvider(getActivity()).get(StockOutViewModel.class);
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(getActivity(),R.array.cards_array,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fragmentGuestCheckoutBinding.cardType.setAdapter(arrayAdapter);
    }

}