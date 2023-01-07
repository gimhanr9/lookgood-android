package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Map;

import Repository.AdminRepository;
import models.ProductItem;
import models.Question;
import models.RatingItem;

public class AdminViewModel extends AndroidViewModel {
    private AdminRepository adminRepository;
    private MutableLiveData<List<ProductItem>> products;
    private MutableLiveData<List<Question>> questions;
    private MutableLiveData<List<String>> productImages;
    private MutableLiveData<Boolean> questionAvailability;
    private MutableLiveData<List<RatingItem>> ratings;
    private MutableLiveData<Float> totalRating;
    private MutableLiveData<Boolean> ratingAvailability;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<Integer> ratingNumber;
    private MutableLiveData<List<ProductItem>> brandProducts;
    private MutableLiveData<List<String>> brands;


    public AdminViewModel(@NonNull Application application) {
        super(application);
        adminRepository=new AdminRepository(application);
        questionAvailability=adminRepository.getQuestionAvailability();
        ratingAvailability=adminRepository.getRatingAvailability();
        loggedOutLiveData=adminRepository.getLoggedOutLiveData();
        ratingNumber=adminRepository.noOfRatings();
        brands=adminRepository.getBrandData();
    }

    public MutableLiveData<List<String>> getProductImages(String id) {
        productImages=adminRepository.getImages(id);
        return productImages;
    }


    public MutableLiveData<List<ProductItem>> getBrandItems(String brand) {
        brandProducts=adminRepository.getBrand(brand);
        return brandProducts;
    }
    public MutableLiveData<List<String>> getBrands() {
        return brands;
    }

    public MutableLiveData<List<ProductItem>> getProductData() {
        products=adminRepository.getProducts();
        return products;
    }

    public MutableLiveData<Float> getTotalRating(String productId){
        totalRating=adminRepository.getTotalRatings(productId);
        return totalRating;
    }

    public void submitAnswer(String productId, String questionId,String answer) {
        adminRepository.submitAnswer(productId,questionId,answer);
    }

    public MutableLiveData<List<Question>> getQuestions(String id) {
        questions=adminRepository.getQuestions(id);
        return questions;
    }

    public MutableLiveData<List<RatingItem>> getRatings(String id) {
        ratings=adminRepository.getRatings(id);
        return ratings;
    }
    public void update(String productId, Map<String, Object> toUpdate){
        adminRepository.update(productId,toUpdate);
    }

    public void delete(String productId){
        adminRepository.deleteProducts(productId);
    }

    public MutableLiveData<Integer> getNoOfRatings() {
        return ratingNumber;
    }

    public MutableLiveData<Boolean> getQuestionAvailability(){
        return questionAvailability;
    }

    public MutableLiveData<Boolean> getRatingAvailability(){
        return ratingAvailability;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public void logOut() {
        adminRepository.logOut();
    }

}
