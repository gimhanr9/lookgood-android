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

import com.cb008385.lookgood.databinding.FragmentConfirmPurchaseBinding;

import java.util.List;

import ViewModel.CartViewModel;
import ViewModel.PurchasesViewModel;
import ViewModel.StockOutViewModel;
import models.AvailabilityModel;
import models.PaymentDetails;
import models.PurchasesItem;
import models.User;

public class ConfirmPurchaseFragment extends Fragment {

    private FragmentConfirmPurchaseBinding fragmentConfirmPurchaseBinding;
    private PurchasesViewModel purchasesViewModel;
    private StockOutViewModel stockOutViewModel;
    private List<PurchasesItem> finalCart;
    private List<AvailabilityModel> nonPurchasedItems;

    public ConfirmPurchaseFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentConfirmPurchaseBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_confirm_purchase,container,false);
        initView();
        return fragmentConfirmPurchaseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getCartItems();
        getData();
        purchasesViewModel.getNonPurchased().observe(getViewLifecycleOwner(), new Observer<List<AvailabilityModel>>() {
            @Override
            public void onChanged(List<AvailabilityModel> availabilityModels) {
                nonPurchasedItems=availabilityModels;
            }
        });
        fragmentConfirmPurchaseBinding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchasesViewModel.purchase(finalCart,"cart");
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
                            NavDirections action=ConfirmPurchaseFragmentDirections.actionConfirmPurchaseFragmentToOrderConfirmationFragment();
                            Navigation.findNavController(view).navigate(action);


                    }
                };

                Handler pdCanceller = new Handler();
                pdCanceller.postDelayed(progressRunnable, 5000);
            }
        });

        fragmentConfirmPurchaseBinding.editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=ConfirmPurchaseFragmentDirections.actionConfirmPurchaseFragmentToAccountFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    private void initView(){
        purchasesViewModel= new ViewModelProvider(this).get(PurchasesViewModel.class);
        stockOutViewModel=new ViewModelProvider(getActivity()).get(StockOutViewModel.class);

    }
    private void getData(){
        purchasesViewModel.getUserData().observe(getViewLifecycleOwner(), new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                fragmentConfirmPurchaseBinding.setName(users.get(users.size()-1).getName());
                fragmentConfirmPurchaseBinding.setEmail(users.get(users.size()-1).getEmail());
                fragmentConfirmPurchaseBinding.setAddress(users.get(users.size()-1).getAddress());
                fragmentConfirmPurchaseBinding.setPhone(users.get(users.size()-1).getPhone());
            }
        });

        purchasesViewModel.getPaymentData().observe(getViewLifecycleOwner(), new Observer<List<PaymentDetails>>() {
            @Override
            public void onChanged(List<PaymentDetails> paymentDetails) {
                fragmentConfirmPurchaseBinding.setCard(paymentDetails.get(paymentDetails.size()-1).getNumber());
            }
        });

    }

    private void getCartItems(){
        purchasesViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<List<PurchasesItem>>() {
            @Override
            public void onChanged(List<PurchasesItem> purchasesItems) {
                finalCart=purchasesItems;
            }
        });
    }


}