package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import Repository.PurchasesRepository;
import models.AvailabilityModel;
import models.CartItem;
import models.GuestItem;
import models.PaymentDetails;
import models.PurchasesItem;
import models.RatingItem;
import models.User;

public class PurchasesViewModel extends AndroidViewModel {
    private PurchasesRepository purchasesRepository;
    private MutableLiveData<Boolean> purchasesAvailability;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<List<PurchasesItem>> purchasedItems;
    private MutableLiveData<List<User>> accountData;
    private MutableLiveData<List<PaymentDetails>> paymentData;
    private MutableLiveData<List<AvailabilityModel>> purchaseData;
    private MutableLiveData<List<PurchasesItem>> cartItems;
    private MutableLiveData<List<RatingItem>> ratingItems;
    private MutableLiveData<Boolean> isSubmitted;


    public PurchasesViewModel(@NonNull Application application) {
        super(application);
        purchasesRepository=new PurchasesRepository(application);
        purchasesAvailability=purchasesRepository.getPurchasesAvailability();
        accountData=purchasesRepository.getUserData();
        paymentData=purchasesRepository.getPaymentData();
        loggedOutLiveData = purchasesRepository.getLoggedOutLiveData();
        isSubmitted=purchasesRepository.getIsSubmitted();
    }

    public MutableLiveData<List<PurchasesItem>> getPurchases() {
        purchasedItems=purchasesRepository.getPurchasedItems();
        return purchasedItems;
    }

    public MutableLiveData<List<PurchasesItem>> getCart() {
        cartItems=purchasesRepository.getCartItems();
        return cartItems;
    }

    public MutableLiveData<List<RatingItem>> getRating(String productId,String purchaseId,String size) {
        ratingItems=purchasesRepository.getFeedbackForItem(productId,purchaseId,size);
        return ratingItems;
    }

    public void submitReview(String purchaseId,String productId, String ratingText,String size,String date,float ratingValue) {
        purchasesRepository.submitFeedback(purchaseId,productId,ratingText,size,date,ratingValue);
    }
    public void updateReview(String productId,String ratingId, String ratingText,float ratingValue) {
        purchasesRepository.updateRating(productId,ratingId,ratingText,ratingValue);
    }

    public void purchase(List<PurchasesItem> purchasesItems,String condition){
        purchasesRepository.checkCartItems(purchasesItems,condition);
    }

    public void purchaseGuest(PurchasesItem purchasesItems, GuestItem guestItem){
        purchasesRepository.checkCartItemsGuest(purchasesItems,guestItem);
    }

    public MutableLiveData<List<AvailabilityModel>> getNonPurchased(){
        purchaseData=purchasesRepository.getNonPurchased();
        return purchaseData;
    }

    public MutableLiveData<Boolean> getPurchasesAvailability(){
        return purchasesAvailability;
    }

    public MutableLiveData<List<User>> getUserData() {
        return accountData;
    }

    public MutableLiveData<List<PaymentDetails>> getPaymentData() {
        return paymentData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public MutableLiveData<Boolean> getIsSubmitted() {
        return isSubmitted;
    }
}
