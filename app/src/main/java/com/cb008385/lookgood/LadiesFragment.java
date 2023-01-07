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

import com.cb008385.lookgood.databinding.FragmentLadiesBinding;

public class LadiesFragment extends Fragment {
    private FragmentLadiesBinding fragmentLadiesBinding;


    public LadiesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentLadiesBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_ladies,container,false);
        return fragmentLadiesBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentLadiesBinding.topExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=NavGraphDirections.actionGlobalMensTopsFragment("Womens Top");
                Navigation.findNavController(view).navigate(action);
            }
        });

        fragmentLadiesBinding.bottomExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=NavGraphDirections.actionGlobalMensTopsFragment("Womens Bottom");
                Navigation.findNavController(view).navigate(action);
            }
        });

    }
}