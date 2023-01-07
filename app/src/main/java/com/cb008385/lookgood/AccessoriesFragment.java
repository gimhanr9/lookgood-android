package com.cb008385.lookgood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cb008385.lookgood.databinding.FragmentAccessoriesBinding;

public class AccessoriesFragment extends Fragment {
    private FragmentAccessoriesBinding fragmentAccessoriesBinding;

    public AccessoriesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflate the layout
        fragmentAccessoriesBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_accessories,container,false);
        return fragmentAccessoriesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentAccessoriesBinding.handbagExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to product page which would display category 'Handbag'
                NavDirections action=NavGraphDirections.actionGlobalMensTopsFragment("Handbag");
                Navigation.findNavController(view).navigate(action);

            }
        });

        fragmentAccessoriesBinding.watchExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to product page which would display category 'Watches'
                NavDirections action=NavGraphDirections.actionGlobalMensTopsFragment("Watches");
                Navigation.findNavController(view).navigate(action);

            }
        });

        fragmentAccessoriesBinding.sunglassExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to product page which would display category 'Sunglasses'
                NavDirections action=NavGraphDirections.actionGlobalMensTopsFragment("Sunglasses");
                Navigation.findNavController(view).navigate(action);

            }
        });
    }
}