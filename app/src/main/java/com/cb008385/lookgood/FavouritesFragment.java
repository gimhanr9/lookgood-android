package com.cb008385.lookgood;

import android.graphics.Color;
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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cb008385.lookgood.databinding.FragmentFavouritesBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import Adapter.CartAdapter;
import Adapter.FavouritesAdapter;
import ViewModel.FavouritesViewModel;
import models.Favourites;

public class FavouritesFragment extends Fragment {
    
    private FragmentFavouritesBinding fragmentFavouritesBinding;
    private FavouritesViewModel favouritesViewModel;
    private FavouritesAdapter favouritesAdapter;
    private List<Favourites> favouriteList;
    RecyclerView recyclerView;

    public FavouritesFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentFavouritesBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_favourites,container,false);
        initView();
        return fragmentFavouritesBinding.getRoot();
        
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoginData();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String name="Item removed";
                final int position=viewHolder.getAdapterPosition();
                final Favourites deletedItem=favouritesAdapter.getItemAt(position);
                favouriteList.remove(position);
                favouritesViewModel.deleteFavouriteItem(deletedItem.getProductId());
                favouritesAdapter.notifyItemRemoved(position);
                Snackbar snackbar=Snackbar.make(recyclerView,name,Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            favouritesViewModel.addToFavouritesAgain(deletedItem,deletedItem.getProductId());

                        });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        }).attachToRecyclerView(recyclerView);

        fragmentFavouritesBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=FavouritesFragmentDirections.actionFavouritesFragmentToUserFragment();
                Navigation.findNavController(getView()).navigate(action);
            }
        });

    }

    private void initView() {
        favouritesViewModel= new ViewModelProvider(this).get(FavouritesViewModel.class);
        recyclerView=fragmentFavouritesBinding.favouriteRecyclerView;
        recyclerView.setHasFixedSize(true);
        favouritesAdapter=new FavouritesAdapter();
        recyclerView.setAdapter(favouritesAdapter);
        /*Drawable mDivider = ContextCompat.getDrawable(getContext(), R.drawable.horizontal_line_recyclerview);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecor.setDrawable(mDivider);
        recyclerView.addItemDecoration(itemDecor);*/
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

    }

    private void getLoginData(){
        fragmentFavouritesBinding.setIsLoading(true);
        favouritesViewModel.getLoggedOutLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    if(fragmentFavouritesBinding.notLoggedIn.getVisibility()==View.VISIBLE){
                        fragmentFavouritesBinding.setIsVisible(false);
                    }
                    getFavourites();

                }else{
                    if(fragmentFavouritesBinding.rvDetails.getVisibility()==View.VISIBLE) {
                        fragmentFavouritesBinding.setIsVisibleRecyclerView(false);
                    }
                    if(fragmentFavouritesBinding.notLoggedIn.getVisibility()==View.GONE){
                        fragmentFavouritesBinding.setIsVisible(true);
                    }
                }
            }
        });
    }

    private void getFavouritesAvailability(){

    }

    private void getFavourites(){
        fragmentFavouritesBinding.setIsVisibleRecyclerView(true);
        favouritesViewModel.getFavourites().observe(getViewLifecycleOwner(), new Observer<List<Favourites>>() {
            @Override
            public void onChanged(List<Favourites> favourites) {
                fragmentFavouritesBinding.setIsLoading(false);
                favouriteList=favourites;

                fragmentFavouritesBinding.setIsAvailable(false);

                favouritesAdapter.submitList(favouriteList);
            }
        });
        favouritesViewModel.getFavouritesAvailability().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){

                    fragmentFavouritesBinding.setIsVisibleRecyclerView(false);

                    fragmentFavouritesBinding.setIsAvailable(true);
                }else {
                    fragmentFavouritesBinding.setIsAvailable(false);
                    fragmentFavouritesBinding.setIsVisibleRecyclerView(true);
                }
            }
        });
        //fragmentFavouritesBinding.setIsLoading(true);


    }
}