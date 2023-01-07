package Repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import models.PaymentDetails;
import models.ProductItem;
import models.Question;
import models.User;

public class ProductRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private MutableLiveData<List<ProductItem>> products;
    private MutableLiveData<List<ProductItem>> brandProducts;
    private MutableLiveData<List<String>> brands;

    public ProductRepository(Application application) {
        this.application=application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.products=new MutableLiveData<>();
        this.brandProducts=new MutableLiveData<>();
        this.brands=new MutableLiveData<>();

    }

    public MutableLiveData<List<ProductItem>> getProducts(String category){
        databaseReference.child("products").orderByChild("category").equalTo(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<ProductItem> productDetails = new ArrayList<>();
                    List<String> brandDetails = new ArrayList<>();
                    List<String> sortedBrands;
                    for (DataSnapshot productSnapshot: snapshot.getChildren()) {
                        ProductItem productItem=productSnapshot.getValue(ProductItem.class);
                        String key=productSnapshot.getKey();
                        productItem.setId(key);
                        productDetails.add(productItem);
                        brandDetails.add(productItem.getBrand());
                    }
                    products.postValue(productDetails);
                    sortedBrands=removeDuplicates(brandDetails);
                    brands.postValue(sortedBrands);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return products;
    }

    public MutableLiveData<List<ProductItem>> getBrand(String category, String brand){
        databaseReference.child("products").orderByChild("category").equalTo(category).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    List<ProductItem> brands = new ArrayList<>();

                    for (DataSnapshot productSnapshot: snapshot.getChildren()) {
                        ProductItem productItem=productSnapshot.getValue(ProductItem.class);
                        String key=productSnapshot.getKey();
                        productItem.setId(key);
                        if (productItem.getBrand().equals(brand)) {

                            brands.add(productItem);
                        }
                    }
                    brandProducts.postValue(brands);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return brandProducts;
    }
    public MutableLiveData<List<ProductItem>> getPrice(String category, String condition){
        databaseReference.child("products").orderByChild("category").equalTo(category).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    List<ProductItem> price = new ArrayList<>();

                    for (DataSnapshot productSnapshot: snapshot.getChildren()) {
                        ProductItem productItem=productSnapshot.getValue(ProductItem.class);
                        String key=productSnapshot.getKey();
                        productItem.setId(key);
                        //if (productItem.getBrand().equals(brand)) {

                            //brands.add(productItem);
                       // }
                    }
                   // brandProducts.postValue(brands);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return brandProducts;
    }


    public List<String> removeDuplicates(List<String> list){


        List<String> newList = new ArrayList<>();

        for (String element : list) {


            if (!newList.contains(element)) {

                newList.add(element);
            }
        }

        return newList;
    }
    public MutableLiveData<List<String>> getBrandData() {
        return brands;
    }

}
