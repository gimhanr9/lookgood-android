package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import models.AvailabilityModel;

public class StockOutViewModel extends AndroidViewModel {
    private MutableLiveData<List<AvailabilityModel>> nonPurchaseData;
    private MutableLiveData<Boolean> isAvailable;

    public StockOutViewModel(@NonNull Application application) {
        super(application);
        nonPurchaseData=new MutableLiveData<>();
        this.isAvailable=new MutableLiveData<>();
    }

    public void setNonPurchases(List<AvailabilityModel> nonPurchases){
        if(nonPurchases.size()>0) {
            nonPurchaseData.setValue(nonPurchases);
            isAvailable.postValue(true);
        }else{
            isAvailable.postValue(false);
        }
    }

    public MutableLiveData<List<AvailabilityModel>> getNonPurchases(){
        return nonPurchaseData;
    }
    public MutableLiveData<Boolean> isNonPurchasesAvailable(){
        return isAvailable;
    }
}
