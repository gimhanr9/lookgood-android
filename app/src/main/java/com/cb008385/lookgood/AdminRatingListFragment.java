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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cb008385.lookgood.databinding.FragmentAdminRatingListBinding;
import com.cb008385.lookgood.databinding.FragmentRatingListBinding;

import java.util.List;

import Adapter.RatingAdapter;
import ViewModel.AdminViewModel;
import ViewModel.ProductDetailsViewModel;
import models.RatingItem;

public class AdminRatingListFragment extends Fragment {

    private FragmentAdminRatingListBinding fragmentAdminRatingListBinding;
    private AdminViewModel adminViewModel;
    private RatingAdapter ratingAdapter;
    RecyclerView recyclerView;


    public AdminRatingListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAdminRatingListBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_admin_rating_list,container,false);
        initView();
        return fragmentAdminRatingListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String key=AdminRatingListFragmentArgs.fromBundle(getArguments()).getProductId();
        getRatings(key);
    }

    private void initView() {
        recyclerView=fragmentAdminRatingListBinding.ratingRecyclerview;
        adminViewModel=new ViewModelProvider(getActivity()).get(AdminViewModel.class);
        recyclerView.setHasFixedSize(true);
        ratingAdapter=new RatingAdapter();
        recyclerView.setAdapter(ratingAdapter);
        Drawable mDivider = ContextCompat.getDrawable(getContext(), R.drawable.horizontal_line_recyclerview);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecor.setDrawable(mDivider);
        recyclerView.addItemDecoration(itemDecor);

    }
    private void getRatings(String productId){
        fragmentAdminRatingListBinding.setIsLoading(true);
        adminViewModel.getRatingAvailability().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    fragmentAdminRatingListBinding.setIsLoading(false);
                    if(fragmentAdminRatingListBinding.noRatingText.getVisibility()==View.GONE) {
                        fragmentAdminRatingListBinding.noRatingText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        adminViewModel.getRatings(productId).observe(getViewLifecycleOwner(), new Observer<List<RatingItem>>() {
            @Override
            public void onChanged(List<RatingItem> ratingItems) {
                fragmentAdminRatingListBinding.setIsLoading(false);
                if(fragmentAdminRatingListBinding.noRatingText.getVisibility()==View.VISIBLE){
                    fragmentAdminRatingListBinding.noRatingText.setVisibility(View.GONE);
                }
                ratingAdapter.submitList(ratingItems);
            }
        });


    }
}