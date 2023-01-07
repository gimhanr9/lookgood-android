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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cb008385.lookgood.databinding.FragmentOrderConfirmationBinding;

import java.util.List;

import Adapter.NonAvailabilityAdapter;
import ViewModel.StockOutViewModel;
import models.AvailabilityModel;

public class OrderConfirmationFragment extends Fragment {

    private FragmentOrderConfirmationBinding fragmentOrderConfirmationBinding;
    private StockOutViewModel stockOutViewModel;
    private NonAvailabilityAdapter nonAvailabilityAdapter;
    RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentOrderConfirmationBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_order_confirmation,container,false);
        initView();
        return fragmentOrderConfirmationBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isEverythingPurchased();
        fragmentOrderConfirmationBinding.continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=OrderConfirmationFragmentDirections.actionOrderConfirmationFragmentToHomeFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });
    }

    private void initView() {
        stockOutViewModel=new ViewModelProvider(getActivity()).get(StockOutViewModel.class);
        recyclerView=fragmentOrderConfirmationBinding.nonAvailableRecyclerView;
        recyclerView.setHasFixedSize(true);
        nonAvailabilityAdapter=new NonAvailabilityAdapter();
        recyclerView.setAdapter(nonAvailabilityAdapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);
    }

    private void isEverythingPurchased() {
        fragmentOrderConfirmationBinding.setIsVisible(true);
        stockOutViewModel.getNonPurchases().observe(getViewLifecycleOwner(), new Observer<List<AvailabilityModel>>() {
            @Override
            public void onChanged(List<AvailabilityModel> availabilityModels) {
                Toast.makeText(getActivity(),"Have Values",Toast.LENGTH_SHORT).show();
                fragmentOrderConfirmationBinding.setIsLoading(false);
                fragmentOrderConfirmationBinding.setIsVisible(false);
                fragmentOrderConfirmationBinding.setIsVisibleRecyclerView(true);
                nonAvailabilityAdapter.submitList(availabilityModels);

            }
        });
        stockOutViewModel.isNonPurchasesAvailable().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    fragmentOrderConfirmationBinding.setIsVisibleRecyclerView(true);
                    fragmentOrderConfirmationBinding.setIsVisible(false);
                }else{
                    fragmentOrderConfirmationBinding.setIsVisible(true);
                    fragmentOrderConfirmationBinding.setIsVisibleRecyclerView(false);
                }
            }
        });

    }
}