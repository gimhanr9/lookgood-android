package Repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import models.ProductItem;

public class HomeControllerRepo {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private MutableLiveData<List<ProductItem>> products;

    public HomeControllerRepo(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.products=new MutableLiveData<>();
    }

    public MutableLiveData<List<ProductItem>> getProducts(){
        databaseReference.child("products").limitToFirst(7).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    List<ProductItem> productDetails = new ArrayList<>();
                    for (DataSnapshot productSnapshot: snapshot.getChildren()) {
                        ProductItem productItem=productSnapshot.getValue(ProductItem.class);
                        String key=productSnapshot.getKey();
                        productItem.setId(key);
                        productDetails.add(productItem);

                    }
                    products.postValue(productDetails);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return products;

    }
}
