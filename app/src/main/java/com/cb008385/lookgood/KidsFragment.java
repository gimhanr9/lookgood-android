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

import com.cb008385.lookgood.databinding.FragmentKidsBinding;

public class KidsFragment extends Fragment {

    private FragmentKidsBinding fragmentKidsBinding;


    public KidsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentKidsBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_kids,container,false);
        return fragmentKidsBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentKidsBinding.topExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=NavGraphDirections.actionGlobalMensTopsFragment("Boys Top");
                Navigation.findNavController(view).navigate(action);

            }
        });

        fragmentKidsBinding.bottomExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=NavGraphDirections.actionGlobalMensTopsFragment("Boys Bottom");
                Navigation.findNavController(view).navigate(action);

            }
        });

        fragmentKidsBinding.gtopsExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=NavGraphDirections.actionGlobalMensTopsFragment("Girls Top");
                Navigation.findNavController(view).navigate(action);

            }
        });

        fragmentKidsBinding.gbottomExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=NavGraphDirections.actionGlobalMensTopsFragment("Girls Bottom");
                Navigation.findNavController(view).navigate(action);
            }
        });
    }
}