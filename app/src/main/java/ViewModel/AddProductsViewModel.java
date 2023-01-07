package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import Repository.AddProductRepository;
import models.ProductItem;

public class AddProductsViewModel extends AndroidViewModel {

    private AddProductRepository addProductRepository;
    private MutableLiveData<Boolean> productState;
    private MutableLiveData<List<String>> imageUrls;
    private MutableLiveData<List<String>> updatedimageUrls;
    private MutableLiveData<List<String>> singleimageUrls;

    public AddProductsViewModel(@NonNull Application application) {
        super(application);
        addProductRepository=new AddProductRepository(application);
        productState=addProductRepository.getUploadDetails();
        //imageUrls=addProductRepository.getImageUploadDetails();
        updatedimageUrls=addProductRepository.getImageUploadDetails();
        singleimageUrls=addProductRepository.getSingleUploadDetails();
    }


    public void uploadProduct(ProductItem productItem,List<String> list){
        addProductRepository.uploadProduct(productItem,list);

    }



    public MutableLiveData<Boolean> getUploadDetails(){
        return productState;
    }

    public MutableLiveData<List<String>> getUrlDetails(){

        return updatedimageUrls;
    }
    public MutableLiveData<List<String>> getSingleUrlDetails(){

        return updatedimageUrls;
    }

    public void getImageUploadDetails(List<String> list){
        addProductRepository.getImageUrl(list);

    }


    public void deleteImage(String imageName){
        addProductRepository.deleteImages(imageName);
    }

}
