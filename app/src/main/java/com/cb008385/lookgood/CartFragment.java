package com.cb008385.lookgood;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import com.cb008385.lookgood.databinding.FragmentCartBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import Adapter.CartAdapter;
import ViewModel.CartViewModel;
import listeners.CartItemClick;
import models.CartItem;


public class CartFragment extends Fragment implements CartItemClick {

    private FragmentCartBinding fragmentCartBinding;
    private CartViewModel cartViewModel;
    private CartAdapter cartAdapter;
    private List<CartItem> cartList;
    RecyclerView recyclerView;


    public CartFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentCartBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_cart,container,false);
        initView();
        return fragmentCartBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLoginData();

        cartViewModel.getCartTotal().observe(getViewLifecycleOwner(), new Observer<Double>() {
            @Override
            public void onChanged(Double aDouble) {
                String totalText="Total: Rs "+aDouble.toString();
                fragmentCartBinding.orderTotalTextView.setText(totalText);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String name="Item removed";
                final int position=viewHolder.getAdapterPosition();
                final CartItem deletedItem=cartAdapter.getItemAt(position);
                cartList.remove(position);
                cartViewModel.deleteCartItem(deletedItem.getId());
                cartAdapter.notifyItemRemoved(position);
                fragmentCartBinding.placeOrderButton.setEnabled(cartList.size()>0);
                Snackbar snackbar=Snackbar.make(recyclerView,name,Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            //cartList.add(position,deletedItem);
                           // cartAdapter.notifyItemInserted(position);
                            cartViewModel.addToCartAgain(deletedItem);

                        });
                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        }).attachToRecyclerView(recyclerView);

        fragmentCartBinding.placeOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = CartFragmentDirections.actionCartFragmentToConfirmPurchaseFragment();
                Navigation.findNavController(getView()).navigate(action);
            }
        });

        fragmentCartBinding.loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=CartFragmentDirections.actionCartFragmentToUserFragment();
                Navigation.findNavController(getView()).navigate(action);
            }
        });



    }

    private void initView() {
        cartViewModel= new ViewModelProvider(this).get(CartViewModel.class);
        recyclerView=fragmentCartBinding.cartRecyclerView;
        recyclerView.setHasFixedSize(true);
        cartAdapter=new CartAdapter(this);
        recyclerView.setAdapter(cartAdapter);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

    }

    public void getCartData(){
        fragmentCartBinding.setIsLoading(true);
        fragmentCartBinding.setIsVisibleRecyclerView(true);
        cartViewModel.getCart().observe(getViewLifecycleOwner(), new Observer<List<CartItem>>() {
            @Override
            public void onChanged(List<CartItem> cartItems) {
                fragmentCartBinding.setIsLoading(false);
                cartList=cartItems;

                fragmentCartBinding.setIsAvailable(false);
                cartAdapter.submitList(cartList);
                fragmentCartBinding.placeOrderButton.setEnabled(cartItems.size()>0);
            }
        });

        cartViewModel.getCartAvailability().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    fragmentCartBinding.setIsVisibleRecyclerView(false);

                    fragmentCartBinding.setIsAvailable(true);
                }else{
                    fragmentCartBinding.setIsAvailable(false);
                    fragmentCartBinding.setIsVisibleRecyclerView(true);
                }

            }
        });
    }

    private void getLoginData(){
        cartViewModel.getLoggedOutLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){

                if(fragmentCartBinding.notLoggedIn.getVisibility()==View.VISIBLE){
                    fragmentCartBinding.setIsVisible(false);
                }
                getCartData();
                }else{
                    if(fragmentCartBinding.rvDetails.getVisibility()==View.VISIBLE) {
                        fragmentCartBinding.setIsVisibleRecyclerView(false);
                    }
                    if(fragmentCartBinding.notLoggedIn.getVisibility()==View.GONE){
                        fragmentCartBinding.setIsVisible(true);
                    }
                }
            }
        });
    }

    @Override
    public void onCartItemClicked(CartItem cartItem) {

    }

    @Override
    public void changeQuantity(CartItem cartItem, int quantity) {
        cartViewModel.updateCart(cartItem,quantity);

    }
}