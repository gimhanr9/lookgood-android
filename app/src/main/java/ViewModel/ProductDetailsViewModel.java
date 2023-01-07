package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import Repository.ProductDetailsRepository;
import models.CartItem;
import models.Favourites;
import models.ProductItem;
import models.Question;
import models.RatingItem;

public class ProductDetailsViewModel extends AndroidViewModel {
    private ProductDetailsRepository productDetailsRepository;
    private MutableLiveData<List<String>> productImages;
    private MutableLiveData<List<Question>> questions;
    private MutableLiveData<Boolean> questionAvailability;
    private MutableLiveData<List<RatingItem>> ratings;
    private MutableLiveData<Boolean> ratingAvailability;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<Boolean> favouriteAvailability;
    private MutableLiveData<Float> totalRating;
    private MutableLiveData<Integer> ratingNumber;
    private MutableLiveData<List<ProductItem>> products;


    public ProductDetailsViewModel(@NonNull Application application) {
        super(application);
        productDetailsRepository=new ProductDetailsRepository(application);
        questionAvailability=productDetailsRepository.getQuestionAvailability();
        ratingAvailability=productDetailsRepository.getRatingAvailability();
        loggedOutLiveData = productDetailsRepository.getLoggedOutLiveData();
        ratingNumber=productDetailsRepository.noOfRatings();
    }

    public MutableLiveData<List<ProductItem>> getProductData(String productId) {
        products=productDetailsRepository.getProducts(productId);
        return products;
    }

    public MutableLiveData<List<String>> getProductImages(String id) {
        productImages=productDetailsRepository.getImages(id);
        return productImages;
    }

    public void submitQuestion(String productId, String question,String date) {
        productDetailsRepository.submitQuestion(productId,question,date);
    }

    public MutableLiveData<List<Question>> getQuestions(String id) {
        questions=productDetailsRepository.getQuestions(id);
        return questions;
    }

    public MutableLiveData<List<RatingItem>> getRatings(String id) {
        ratings=productDetailsRepository.getRatings(id);
        return ratings;
    }
    public void addToCart(CartItem cartItem) {
        productDetailsRepository.checkCart(cartItem);
    }

    public void addToFavourites(Favourites favourites,String productId) {
        productDetailsRepository.addToFavourites(favourites,productId);
    }

    public MutableLiveData<Boolean> getQuestionAvailability(){
        return questionAvailability;
    }

    public MutableLiveData<Float> getTotalRating(String productId){
        totalRating=productDetailsRepository.getTotalRatings(productId);
        return totalRating;
    }

    public MutableLiveData<Boolean> getRatingAvailability(){
        return ratingAvailability;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public MutableLiveData<Integer> getNoOfRatings() {
        return ratingNumber;
    }

    public MutableLiveData<Boolean> getFavouriteAvailability(String productId) {
        favouriteAvailability=productDetailsRepository.isProductFavourite(productId);
        return favouriteAvailability;
    }

    public void deleteFavouriteItem(String id){
        productDetailsRepository.deleteFavouriteItem(id);
    }


}
