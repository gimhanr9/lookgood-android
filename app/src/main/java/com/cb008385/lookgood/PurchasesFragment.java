package com.cb008385.lookgood;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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

import com.cb008385.lookgood.databinding.FragmentPurchasesBinding;

import java.util.List;

import Adapter.CartAdapter;
import Adapter.PurchasesAdapter;
import ViewModel.CartViewModel;
import ViewModel.PurchasesViewModel;
import listeners.PurchasesItemReviewClick;
import models.CartItem;
import models.PurchasesItem;

public class PurchasesFragment extends Fragment implements PurchasesItemReviewClick {

    private FragmentPurchasesBinding fragmentPurchasesBinding;
    private PurchasesViewModel purchasesViewModel;
    private PurchasesAdapter purchasesAdapter;
    RecyclerView recyclerView;
    private int position;

    public PurchasesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPurchasesBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_purchases,container,false);
        initView();
        return fragmentPurchasesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoginData();
        fragmentPurchasesBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=PurchasesFragmentDirections.actionPurchasesFragmentToUserFragment();
                Navigation.findNavController(view).navigate(action);
            }
        });


    }

    private void initView(){
        purchasesViewModel= new ViewModelProvider(getActivity()).get(PurchasesViewModel.class);//check
        recyclerView=fragmentPurchasesBinding.purchasesRecyclerView;
        recyclerView.setHasFixedSize(true);
        purchasesAdapter=new PurchasesAdapter(this);
        recyclerView.setAdapter(purchasesAdapter);
        Drawable mDivider = ContextCompat.getDrawable(getContext(), R.drawable.horizontal_line_recyclerview);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecor.setDrawable(mDivider);
        recyclerView.addItemDecoration(itemDecor);

    }
    public void getPurchasesData(){
        fragmentPurchasesBinding.setIsVisibleRecyclerView(true);
        fragmentPurchasesBinding.setIsLoading(true);
        purchasesViewModel.getPurchases().observe(getViewLifecycleOwner(), new Observer<List<PurchasesItem>>() {
            @Override
            public void onChanged(List<PurchasesItem> purchasesItems) {
                fragmentPurchasesBinding.setIsLoading(false);
                fragmentPurchasesBinding.setIsVisible(false);
                purchasesAdapter.submitList(purchasesItems);
            }
        });

        purchasesViewModel.getIsSubmitted().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    Toast.makeText(getActivity(),"Came here",Toast.LENGTH_SHORT).show();
                    purchasesAdapter.notifyItemChanged(position);
                }
            }
        });

        purchasesViewModel.getPurchasesAvailability().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){

                    fragmentPurchasesBinding.setIsVisibleRecyclerView(false);

                    fragmentPurchasesBinding.setIsAvailable(true);
                }else {
                    fragmentPurchasesBinding.setIsAvailable(false);
                    fragmentPurchasesBinding.setIsVisibleRecyclerView(true);
                }
            }
        });
    }

    private void getLoginData(){
        purchasesViewModel.getLoggedOutLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){

                    if(fragmentPurchasesBinding.notLoggedIn.getVisibility()==View.VISIBLE){
                        fragmentPurchasesBinding.setIsVisible(false);
                    }
                    getPurchasesData();
                }else{
                    if(fragmentPurchasesBinding.rvDetails.getVisibility()==View.VISIBLE) {
                        fragmentPurchasesBinding.setIsVisibleRecyclerView(false);
                    }
                    if(fragmentPurchasesBinding.notLoggedIn.getVisibility()==View.GONE){
                        fragmentPurchasesBinding.setIsVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onPurchasesReviewClicked(PurchasesItem purchasesItem, int position) {

    }

    @Override
    public void onWriteReviewClicked(PurchasesItem purchasesItem, int position) {
        this.position=position;
        NavDirections action=PurchasesFragmentDirections.actionPurchasesFragmentToReviewSheetDialog(purchasesItem.getProductId(),purchasesItem.getId(),purchasesItem.isRated(),purchasesItem.getSize());
        Navigation.findNavController(getView()).navigate(action);

    }
}