package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import Repository.AuthenticationRepository;
import Repository.HomeControllerRepo;
import models.ProductItem;

public class HomeControllerViewModel extends AndroidViewModel {

    private AuthenticationRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<Boolean> isAvailable;
    private MutableLiveData<List<ProductItem>> products;

    public HomeControllerViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthenticationRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
        loggedOutLiveData = authAppRepository.getLoggedOutLiveData();
        this.isAvailable=new MutableLiveData<>();
    }

    public void finishActivity(Boolean value){

            isAvailable.postValue(value);

    }

    public MutableLiveData<List<ProductItem>> getProductData() {
        products=authAppRepository.getProducts();
        return products;
    }

    public void logOut() {
        authAppRepository.logOut();
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public MutableLiveData<Boolean> canFinish(){
        return isAvailable;
    }
}
