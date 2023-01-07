package ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import Repository.ProductRepository;
import models.ProductItem;
import models.Question;
import models.User;

public class ProductViewModel extends AndroidViewModel {
    private ProductRepository productRepository;
    private MutableLiveData<List<ProductItem>> products;
    private MutableLiveData<List<ProductItem>> brandProducts;
    private MutableLiveData<List<String>> brands;

    public ProductViewModel(@NonNull Application application) {
        super(application);
        productRepository=new ProductRepository(application);
        brands=productRepository.getBrandData();

    }

    public MutableLiveData<List<ProductItem>> getProductData(String category) {
        products=productRepository.getProducts(category);
        return products;
    }

    public MutableLiveData<List<ProductItem>> getBrandItems(String category, String brand) {
        brandProducts=productRepository.getBrand(category,brand);
        return brandProducts;
    }

    public MutableLiveData<List<String>> getBrands() {
        return brands;
    }


}
