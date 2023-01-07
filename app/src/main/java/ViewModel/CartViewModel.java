package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import Repository.CartRepository;
import models.CartItem;

public class CartViewModel extends AndroidViewModel {
    private CartRepository cartRepository;
    private MutableLiveData<Boolean> cartAvailability;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<List<CartItem>> cartItems;
    private MutableLiveData<Double> totalCartPrice;


    public CartViewModel(@NonNull Application application) {
        super(application);
        cartRepository=new CartRepository(application);
        cartAvailability=cartRepository.getCartAvailability();
        loggedOutLiveData = cartRepository.getLoggedOutLiveData();
        totalCartPrice=cartRepository.getTotalPrice();


    }

    public MutableLiveData<List<CartItem>> getCart() {
        cartItems=cartRepository.getCartItems();
        return cartItems;
    }

    public void deleteCartItem(String id){
        cartRepository.deleteCartItem(id);
    }

    public void addToCartAgain(CartItem cartItem){
        cartRepository.addToCartAgain(cartItem);
    }

    public void updateCart(CartItem cartItem,int quantity){
        cartRepository.updateCart(cartItem,quantity);
    }

    public MutableLiveData<Double> getCartTotal(){
        return totalCartPrice;
    }

    public MutableLiveData<Boolean> getCartAvailability(){
        return cartAvailability;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
