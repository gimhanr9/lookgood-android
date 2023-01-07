package com.cb008385.lookgood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.cb008385.lookgood.databinding.DialogLayoutBinding;

import ViewModel.AdminViewModel;


public class AlertDialog extends DialogFragment {

    private DialogLayoutBinding dialogLayoutBinding;
    private AdminViewModel adminViewModel;


    public AlertDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        dialogLayoutBinding= DataBindingUtil.inflate(inflater,R.layout.dialog_layout,container,false);
        adminViewModel= new ViewModelProvider(this).get(AdminViewModel.class);
        return dialogLayoutBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String id=AlertDialogArgs.fromBundle(getArguments()).getProductId();
        dialogLayoutBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        dialogLayoutBinding.yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Hi"+id,Toast.LENGTH_LONG).show();
                //adminViewModel.delete(id);
            }
        });

    }
}
