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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.CartItem;

public class CartRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<List<CartItem>> cartDetails;
    private MutableLiveData<Boolean> cartAvailability;
    private MutableLiveData<Double> totalCartPrice;

    public CartRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.cartDetails = new MutableLiveData<>();
        this.cartAvailability = new MutableLiveData<>();
        this.totalCartPrice=new MutableLiveData<>();


        if (firebaseAuth.getCurrentUser() != null) {
            loggedOutLiveData.postValue(false);


        } else {
            loggedOutLiveData.postValue(true);
        }
    }

    public MutableLiveData<List<CartItem>> getCartItems() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("cart").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<CartItem> cart = new ArrayList<>();
                    for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                        CartItem cartItem = cartSnapshot.getValue(CartItem.class);
                        cart.add(new CartItem(cartSnapshot.getKey(),cartItem.getProductId(), cartItem.getImageUrl(), cartItem.getProductName(), cartItem.getSize(), cartItem.getQuantity(),cartItem.getPrice()));
                    }
                    cartDetails.setValue(cart);
                    cartAvailability.setValue(true);

                }else{
                    cartAvailability.setValue(false);

                }
                calculateCartTotal();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return cartDetails;
    }

    public void deleteCartItem(String id){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("cart").child(user.getUid()).child(id).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(application.getApplicationContext(),"Successfully deleted the item!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addToCartAgain(CartItem cartItem){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        //String key=databaseReference.push().getKey();
        CartItem cartItem1=new CartItem(cartItem.getProductId(),cartItem.getImageUrl(),cartItem.getProductName(),cartItem.getSize(),cartItem.getQuantity(),cartItem.getPrice());
        databaseReference.child("cart").child(user.getUid()).child(cartItem.getId()).setValue(cartItem1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {

                Toast.makeText(application.getApplicationContext(),"Successfully added to cart!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(),"Could not add to cart!"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        //calculateCartTotal();


    }

    public void updateCart(CartItem cartItem,int quantity){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            Map<String, Object> updates = new HashMap<String,Object>();
            updates.put("quantity",quantity);
            databaseReference.child("cart").child(user.getUid()).child(cartItem.getId()).updateChildren(updates);
            //calculateCartTotal();

        }
    }



    public void calculateCartTotal(){
        if(cartDetails.getValue()==null) return;
        double total=0.0;
        List<CartItem> cartList=cartDetails.getValue();
        for(CartItem cartItem : cartList){
            total+=cartItem.getPrice() * cartItem.getQuantity();
        }

        totalCartPrice.postValue(total);
    }

    public MutableLiveData<Double> getTotalPrice(){
        if(totalCartPrice.getValue()==null){
            totalCartPrice.setValue(0.0);
        }

        return totalCartPrice;
    }
    public MutableLiveData<Boolean> getCartAvailability() {
        return cartAvailability;
    }
    public MutableLiveData<Boolean> getLoggedOutLiveData(){
        return loggedOutLiveData;
    }

}
