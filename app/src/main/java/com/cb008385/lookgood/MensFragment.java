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

import com.cb008385.lookgood.databinding.FragmentMensBinding;


public class MensFragment extends Fragment {
    private FragmentMensBinding fragmentMensBinding;


    public MensFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentMensBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_mens,container,false);
        return fragmentMensBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentMensBinding.topExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=NavGraphDirections.actionGlobalMensTopsFragment("Mens Top");
                Navigation.findNavController(view).navigate(action);
            }
        });

        fragmentMensBinding.bottomExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=NavGraphDirections.actionGlobalMensTopsFragment("Mens Bottom");
                Navigation.findNavController(view).navigate(action);
            }
        });


    }
}