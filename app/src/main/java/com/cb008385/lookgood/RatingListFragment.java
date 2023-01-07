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

import com.cb008385.lookgood.databinding.FragmentRatingListBinding;

import java.util.List;

import Adapter.RatingAdapter;
import ViewModel.ProductDetailsViewModel;
import models.Question;
import models.RatingItem;

public class RatingListFragment extends Fragment {

    private FragmentRatingListBinding fragmentRatingListBinding;
    private ProductDetailsViewModel productDetailsViewModel;
    private RatingAdapter ratingAdapter;
    RecyclerView recyclerView;


    public RatingListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentRatingListBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_rating_list,container,false);
        initView();
        return fragmentRatingListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String key=RatingListFragmentArgs.fromBundle(getArguments()).getProductId();
        getRatings(key);

    }

    private void initView() {
        recyclerView=fragmentRatingListBinding.ratingRecyclerview;
        productDetailsViewModel=new ViewModelProvider(getActivity()).get(ProductDetailsViewModel.class);
        recyclerView.setHasFixedSize(true);
        ratingAdapter=new RatingAdapter();
        recyclerView.setAdapter(ratingAdapter);
        Drawable mDivider = ContextCompat.getDrawable(getContext(), R.drawable.horizontal_line_recyclerview);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecor.setDrawable(mDivider);
        recyclerView.addItemDecoration(itemDecor);


    }

    private void getRatings(String productId){
        fragmentRatingListBinding.setIsLoading(true);
        productDetailsViewModel.getRatingAvailability().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    fragmentRatingListBinding.setIsLoading(false);
                    if(fragmentRatingListBinding.noRatingText.getVisibility()==View.GONE) {
                        fragmentRatingListBinding.noRatingText.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        productDetailsViewModel.getRatings(productId).observe(getViewLifecycleOwner(), new Observer<List<RatingItem>>() {
            @Override
            public void onChanged(List<RatingItem> ratingItems) {
                fragmentRatingListBinding.setIsLoading(false);
                if(fragmentRatingListBinding.noRatingText.getVisibility()==View.VISIBLE){
                    fragmentRatingListBinding.noRatingText.setVisibility(View.GONE);
                }
                ratingAdapter.submitList(ratingItems);
            }
        });


    }
}