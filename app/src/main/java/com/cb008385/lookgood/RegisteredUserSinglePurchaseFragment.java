package com.cb008385.lookgood;

import android.app.ProgressDialog;
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

import com.cb008385.lookgood.databinding.FragmentRegisteredUserSinglePurchaseBinding;

import java.util.ArrayList;
import java.util.List;

import ViewModel.PurchasesViewModel;
import ViewModel.StockOutViewModel;
import models.AvailabilityModel;
import models.PaymentDetails;
import models.PurchasesItem;
import models.User;

public class RegisteredUserSinglePurchaseFragment extends Fragment {

    private FragmentRegisteredUserSinglePurchaseBinding fragmentRegisteredUserSinglePurchaseBinding;
    private PurchasesViewModel purchasesViewModel;
    private StockOutViewModel stockOutViewModel;
    private List<PurchasesItem> finalCart;
    private List<AvailabilityModel> nonPurchasedItems;


    public RegisteredUserSinglePurchaseFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentRegisteredUserSinglePurchaseBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_registered_user_single_purchase,container,false);
        initView();
        return fragmentRegisteredUserSinglePurchaseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String id=RegisteredUserSinglePurchaseFragmentArgs.fromBundle(getArguments()).getProductId();
        String productImage=RegisteredUserSinglePurchaseFragmentArgs.fromBundle(getArguments()).getProductImage();
        String productName=RegisteredUserSinglePurchaseFragmentArgs.fromBundle(getArguments()).getProductName();
        String size=RegisteredUserSinglePurchaseFragmentArgs.fromBundle(getArguments()).getSize();
        int quantity=RegisteredUserSinglePurchaseFragmentArgs.fromBundle(getArguments()).getQuantity();
        double price=(double) RegisteredUserSinglePurchaseFragmentArgs.fromBundle(getArguments()).getPrice();

        PurchasesItem finalizedItem=new PurchasesItem(id,productImage,productName,size,quantity,price,false);
        finalCart.add(finalizedItem);
        getData();
        purchasesViewModel.getNonPurchased().observe(getViewLifecycleOwner(), new Observer<List<AvailabilityModel>>() {
            @Override
            public void onChanged(List<AvailabilityModel> availabilityModels) {
                nonPurchasedItems=availabilityModels;
            }
        });
        fragmentRegisteredUserSinglePurchaseBinding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchasesViewModel.purchase(finalCart,"single");
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

                        NavDirections action=RegisteredUserSinglePurchaseFragmentDirections.actionRegisteredUserSinglePurchaseFragmentToOrderConfirmationFragment();
                        Navigation.findNavController(view).navigate(action);


                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 5000);
            }
        });

        fragmentRegisteredUserSinglePurchaseBinding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=RegisteredUserSinglePurchaseFragmentDirections.actionRegisteredUserSinglePurchaseFragmentToAccountFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    private void initView() {
        purchasesViewModel= new ViewModelProvider(this).get(PurchasesViewModel.class);
        stockOutViewModel=new ViewModelProvider(getActivity()).get(StockOutViewModel.class);
        finalCart=new ArrayList<>();

    }

    private void getData(){
        purchasesViewModel.getUserData().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                fragmentRegisteredUserSinglePurchaseBinding.setName(users.get(users.size()-1).getName());
                fragmentRegisteredUserSinglePurchaseBinding.setEmail(users.get(users.size()-1).getEmail());
                fragmentRegisteredUserSinglePurchaseBinding.setAddress(users.get(users.size()-1).getAddress());
                fragmentRegisteredUserSinglePurchaseBinding.setPhone(users.get(users.size()-1).getPhone());
            }
        });

        purchasesViewModel.getPaymentData().observe(getViewLifecycleOwner(), new Observer<List<PaymentDetails>>() {
            @Override
            public void onChanged(List<PaymentDetails> paymentDetails) {
                fragmentRegisteredUserSinglePurchaseBinding.setCard(paymentDetails.get(paymentDetails.size()-1).getNumber());
            }
        });

    }


}