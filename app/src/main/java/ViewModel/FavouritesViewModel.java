package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import Repository.FavouriteRepository;
import models.Favourites;

public class FavouritesViewModel extends AndroidViewModel {
    private FavouriteRepository favouriteRepository;
    private MutableLiveData<Boolean> favouriteAvailability;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<List<Favourites>> favouriteItems;

    public FavouritesViewModel(@NonNull Application application) {
        super(application);
        favouriteRepository=new FavouriteRepository(application);
        favouriteAvailability=favouriteRepository.getFavouritesAvailability();
        loggedOutLiveData = favouriteRepository.getLoggedOutLiveData();
    }
    public MutableLiveData<List<Favourites>> getFavourites() {
        favouriteItems=favouriteRepository.getFavouriteItems();
        return favouriteItems;
    }

    public void deleteFavouriteItem(String id){
        favouriteRepository.deleteFavouriteItem(id);
    }

    public void addToFavouritesAgain(Favourites favourites,String productId){
        favouriteRepository.addToFavouritesAgain(favourites,productId);
    }

    public MutableLiveData<Boolean> getFavouritesAvailability(){
        return favouriteAvailability;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
