package com.cb008385.lookgood;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cb008385.lookgood.databinding.FragmentHomeBinding;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import Adapter.HomeAdapter;
import Adapter.ProductAdapter;
import ViewModel.AuthenticationViewModel;
import ViewModel.HomeControllerViewModel;
import listeners.ProductClickListener;
import models.ProductItem;


public class HomeFragment extends Fragment implements ProductClickListener {
    private HomeControllerViewModel homeControllerViewModel;
    private AuthenticationViewModel authenticationViewModel;
    SharedPreferences pref;
    private HomeAdapter homeAdapter;
    private List<ProductItem> products =new ArrayList<>();
    private FragmentHomeBinding fragmentHomeBinding;
    private NavController navController;
    RecyclerView homerecyclerView;

    public HomeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentHomeBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_home,container,false);
        initView();
        setHasOptionsMenu(true);
        return fragmentHomeBinding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        authenticationViewModel.getUserLiveData().observe(getViewLifecycleOwner(), new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null) {
                    if(firebaseUser.getEmail()!=null) {
                        if (firebaseUser.getEmail().equals("gimhanrd19@gmail.com")) {
                            Intent intent = new Intent(getActivity(), AdminActivity.class);
                            startActivity(intent);
                            homeControllerViewModel.finishActivity(true);

                        }
                    }

                }
            }
        });
        getProducts();

        pref = getActivity().getApplicationContext().getSharedPreferences("myPrefs", Context.MODE_PRIVATE);


        homeControllerViewModel.getLoggedOutLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {

            }
        });
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);

    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.options_menu,menu);
        if(restorePreferenceData()){
            menu.findItem(R.id.userFragment).setVisible(false);
            menu.findItem(R.id.logoutItem).setVisible(true);
        }else{
            menu.findItem(R.id.userFragment).setVisible(true);
            menu.findItem(R.id.logoutItem).setVisible(false);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logoutItem){
            savePreferenceData(false);
            homeControllerViewModel.logOut();
            getActivity().invalidateOptionsMenu();
            Toast.makeText(getActivity(),"Logged out",Toast.LENGTH_LONG).show();


        }
        navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }

    private void savePreferenceData(boolean item) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isLoggedIn",item);
        editor.apply();


    }

    private boolean restorePreferenceData() {

        boolean isLoggedIn = pref.getBoolean("isLoggedIn",false);
        return  isLoggedIn;

    }

    private void initView() {
        authenticationViewModel=new ViewModelProvider(this).get(AuthenticationViewModel.class);
        homeControllerViewModel= new ViewModelProvider(getActivity()).get(HomeControllerViewModel.class);
        homerecyclerView=fragmentHomeBinding.trendingRv;
        homerecyclerView.setHasFixedSize(true);
        homeAdapter=new HomeAdapter(products,this);
        homerecyclerView.setAdapter(homeAdapter);

    }

    private void getProducts(){
        fragmentHomeBinding.setIsLoading(true);
        homeControllerViewModel.getProductData().observe(getViewLifecycleOwner(), new Observer<List<ProductItem>>() {
            @Override
            public void onChanged(List<ProductItem> productItems) {
                fragmentHomeBinding.setIsLoading(false);
                if(products.size()>0){
                    products.clear();
                }
                products.addAll(productItems);
                homeAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onProductClicked(ProductItem productItem) {
        float FPrice=(float) productItem.getPrice();
        NavDirections action=NavGraphDirections.actionGlobalProductDetailsFragment(productItem.getId(),productItem.getImageUrl(),productItem.getName(),productItem.getTitle(),
                productItem.getBrand(),productItem.getDescription(),productItem.getSmall(),productItem.getMedium(),productItem.getLarge(),
                productItem.getXlarge(),productItem.getXxl(),FPrice);
        Navigation.findNavController(getView()).navigate(action);

    }
}