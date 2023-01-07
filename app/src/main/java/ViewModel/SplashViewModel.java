package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

import Repository.SplashDetailsRepository;
import models.OnboardingItem;

public class SplashViewModel extends AndroidViewModel {

    private SplashDetailsRepository splashDetailsRepository;

    private LiveData<List<OnboardingItem>> allDetails;

    public SplashViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<OnboardingItem>> getDetails(){
        splashDetailsRepository= new SplashDetailsRepository();
        return splashDetailsRepository.getSplashDetails();
       /* if(switchData==null){
            switchData=new MutableLiveData<>();
            loadSwitchData();
        }
        return switchData;*/

    }



}
