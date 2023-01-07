package com.cb008385.lookgood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cb008385.lookgood.databinding.FragmentAdminRatingListBinding;
import com.cb008385.lookgood.databinding.FragmentAdminUpdateBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.HashMap;
import java.util.Map;

import ViewModel.AdminViewModel;
import ViewModel.ProductDetailsViewModel;

public class AdminUpdateFragment extends BottomSheetDialogFragment {

    private FragmentAdminUpdateBinding fragmentAdminUpdateBinding;
    private AdminViewModel adminViewModel;


    public AdminUpdateFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAdminUpdateBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_admin_update,container,false);
        initView();
        return fragmentAdminUpdateBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String productId=AdminUpdateFragmentArgs.fromBundle(getArguments()).getProductId();
        fragmentAdminUpdateBinding.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> toUpdate = new HashMap<String, Object>();
                toUpdate.put("small",Integer.parseInt(fragmentAdminUpdateBinding.quantity.getSelectedItem().toString()));
                toUpdate.put("medium",Integer.parseInt(fragmentAdminUpdateBinding.quantityMedium.getSelectedItem().toString()));
                toUpdate.put("large",Integer.parseInt(fragmentAdminUpdateBinding.quantityLarge.getSelectedItem().toString()));
                toUpdate.put("xlarge",Integer.parseInt(fragmentAdminUpdateBinding.quantityXlarge.getSelectedItem().toString()));
                toUpdate.put("xxl",Integer.parseInt(fragmentAdminUpdateBinding.quantityXxlarge.getSelectedItem().toString()));

                adminViewModel.update(productId,toUpdate);

            }
        });

        fragmentAdminUpdateBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    private void initView() {
        adminViewModel=new ViewModelProvider(getActivity()).get(AdminViewModel.class);
    }
}