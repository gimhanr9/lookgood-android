package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

import Repository.AuthenticationRepository;
import models.User;

public class AuthenticationViewModel extends AndroidViewModel {
    private AuthenticationRepository authAppRepository;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<Boolean> passwordEmailLiveData;
    private MutableLiveData<Boolean> loginFailure;

    public AuthenticationViewModel(@NonNull Application application) {
        super(application);
        authAppRepository = new AuthenticationRepository(application);
        userLiveData = authAppRepository.getUserLiveData();
        loggedOutLiveData = authAppRepository.getLoggedOutLiveData();
        loginFailure=authAppRepository.getLoginFailure();
    }

    public void login(String email, String password) {
        authAppRepository.login(email, password);
    }

    public void register(User user) {
        authAppRepository.register(user);
    }

    public void updatePassword(String email) {
        authAppRepository.updatePasswordEmail(email);
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public void logOut() {
        authAppRepository.logOut();
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public MutableLiveData<Boolean> getPasswordEmailLiveData() {
        passwordEmailLiveData=authAppRepository.getPasswordEmailDetails();
        return passwordEmailLiveData;
    }

    public MutableLiveData<Boolean> getLoginFailure() {
        return loginFailure;
    }


}
