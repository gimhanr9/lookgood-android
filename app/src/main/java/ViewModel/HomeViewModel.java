package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import Repository.AuthenticationRepository;
import models.PaymentDetails;
import models.ProductItem;
import models.User;

public class HomeViewModel extends AndroidViewModel {

    private AuthenticationRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<User> userData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<Boolean> reauthenticationData;
    private MutableLiveData<Boolean> emailUpdateData;
    private MutableLiveData<Boolean> passwordUpdateData;
    private MutableLiveData<List<User>> accountData;
    private MutableLiveData<List<ProductItem>> products;
    private MutableLiveData<List<PaymentDetails>> paymentData;


    public HomeViewModel(@NonNull Application application) {
        super(application);

        authAppRepository = new AuthenticationRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
        loggedOutLiveData = authAppRepository.getLoggedOutLiveData();
        accountData=authAppRepository.userData();
        paymentData=authAppRepository.getpaymentData();

    }

    public MutableLiveData<List<ProductItem>> getProductData() {
        products=authAppRepository.getProducts();
        return products;
    }

    public void logOut() {
        authAppRepository.logOut();
    }
    public void updateEmail(String email) {
        authAppRepository.updateEmail(email);
    }

    public void updatePassword(String password) {
        authAppRepository.updatePassword(password);
    }

    public void updateUser(User user) {
        authAppRepository.updateWholeUser(user);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public void reauthenticate(String password) {
        authAppRepository.reauthenticate(password);
    }

    public void addPaymentDetails(PaymentDetails paymentDetails) {
        authAppRepository.updatePaymentDetails(paymentDetails);
    }

    public MutableLiveData<Boolean> getReauthenticationData() {
        reauthenticationData = authAppRepository.getReauthenticationDetails();
        return reauthenticationData;

    }

    public MutableLiveData<Boolean> getEmailUpdateData() {
        emailUpdateData = authAppRepository.getEmailUpdateDetails();
        return emailUpdateData;

    }

    public MutableLiveData<Boolean> getPasswordUpdateData() {
        passwordUpdateData = authAppRepository.getPasswordUpdateDetails();
        return passwordUpdateData;

    }

    public MutableLiveData<List<User>> userData() {
        return accountData;
    }

    public MutableLiveData<List<PaymentDetails>> paymentData() {
        return paymentData;
    }




}
