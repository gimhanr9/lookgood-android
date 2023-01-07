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

import models.AvailabilityModel;
import models.CartItem;
import models.GuestItem;
import models.PaymentDetails;
import models.PurchasesItem;
import models.RatingItem;
import models.UpdateItem;
import models.User;

public class PurchasesRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<List<PurchasesItem>> purchaseDetails;
    private MutableLiveData<Boolean> purchasesAvailability;
    private MutableLiveData<List<User>> accountData;
    private MutableLiveData<List<PaymentDetails>> paymentData;
    private MutableLiveData<List<AvailabilityModel>> nonAvailable;
    private MutableLiveData<List<PurchasesItem>> cartDetails;
    private MutableLiveData<List<RatingItem>> ratingDetails;
    private MutableLiveData<Boolean> isSubmitted;


    public PurchasesRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.purchaseDetails = new MutableLiveData<>();
        this.purchasesAvailability = new MutableLiveData<>();
        this.accountData=new MutableLiveData<>();
        this.paymentData =new MutableLiveData<>();
        this.nonAvailable=new MutableLiveData<>();
        this.cartDetails=new MutableLiveData<>();
        this.ratingDetails=new MutableLiveData<>();
        this.isSubmitted=new MutableLiveData<>();


        if (firebaseAuth.getCurrentUser() != null) {
            loggedOutLiveData.postValue(false);
        } else {
            loggedOutLiveData.postValue(true);
        }
    }

    public MutableLiveData<List<PurchasesItem>> getPurchasedItems() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("purchases").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<PurchasesItem> purchases = new ArrayList<>();
                    for (DataSnapshot purchasesSnapshot : snapshot.getChildren()) {
                        PurchasesItem purchasedItem = purchasesSnapshot.getValue(PurchasesItem.class);
                        purchases.add(new PurchasesItem(purchasesSnapshot.getKey(),purchasedItem.getProductId(), purchasedItem.getImageUrl(), purchasedItem.getProductName(), purchasedItem.getSize(), purchasedItem.getQuantity(),purchasedItem.getPrice(),purchasedItem.isRated()));
                    }
                    purchaseDetails.postValue(purchases);
                }else{
                    purchasesAvailability.postValue(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return purchaseDetails;
    }

    public MutableLiveData<List<RatingItem>> getFeedbackForItem(String productId,String purchaseId,String size){
        List<RatingItem> ratingList=new ArrayList<>();
        databaseReference.child("ratings").child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<RatingItem> ratings = new ArrayList<>();
                    for (DataSnapshot ratingSnapshot : snapshot.getChildren()) {
                        RatingItem ratingItem = ratingSnapshot.getValue(RatingItem.class);

                            if (ratingItem.getPurchaseId().equals(purchaseId) && ratingItem.getSize().equals(size)) {
                                ratingList.add(new RatingItem(ratingSnapshot.getKey(), productId, ratingItem.getPurchaseId(), ratingItem.getName(), ratingItem.getRatingText(), ratingItem.getSize(), ratingItem.getDate(), ratingItem.getRatingValue()));
                            }

                    }
                    ratingDetails.postValue(ratingList);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return ratingDetails;


    }

    public void submitFeedback(String purchaseId,String productId,String ratingText,String size,String date,float ratingValue){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            databaseReference.child("users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        RatingItem ratingItem = new RatingItem(purchaseId,user.getName(), ratingText,size,date, ratingValue);
                        writeFeedbackToDatabase(ratingItem,productId);
                        updatePurchases(purchaseId);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(application.getApplicationContext(), "Error Submitting rating", Toast.LENGTH_SHORT).show();

                }
            });
        }

    }

    public void updateRating(String productId,String ratingId,String feedback,float ratingValue){

        Map<String, Object> toUpdate = new HashMap<String, Object>();
        toUpdate.put("ratingText", feedback);
        toUpdate.put("ratingValue", ratingValue);
        databaseReference.child("ratings").child(productId).child(ratingId).updateChildren(toUpdate);
        Toast.makeText(application.getApplicationContext(), "Updated rating!", Toast.LENGTH_SHORT).show();

    }

    public void updatePurchases(String key){
        String userId;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Map<String, Object> toUpdate = new HashMap<String, Object>();
        toUpdate.put("rated", true);

        databaseReference.child("purchases").child(user.getUid()).child(key).updateChildren(toUpdate);

    }

    public MutableLiveData<List<PurchasesItem>> getCartItems(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            databaseReference.child("cart").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        List<PurchasesItem> cart = new ArrayList<>();
                        for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                            CartItem cartItem = cartSnapshot.getValue(CartItem.class);
                            boolean isRated = false;
                            cart.add(new PurchasesItem(cartItem.getProductId(), cartItem.getImageUrl(), cartItem.getProductName(), cartItem.getSize(), cartItem.getQuantity(), cartItem.getPrice(), isRated));
                        }
                        cartDetails.postValue(cart);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
            return cartDetails;


    }


    public void checkCartItems(List<PurchasesItem> purchasesItems,String condition){
        List<AvailabilityModel> nonAvailableItems=new ArrayList<>();
        List<PurchasesItem> availableItems=new ArrayList<>();
        List<UpdateItem> toUpdate=new ArrayList<>();


        for(PurchasesItem item : purchasesItems){// purchaseItems has the original cart values  //
            String productId=item.getProductId();
            String size=item.getSize();
            int quantity=item.getQuantity();

            databaseReference.child("products").child(productId).child(size).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer value=snapshot.getValue(Integer.class);
                    int valueInt=value;

                    if (valueInt >= quantity) {
                        int newQuantity = valueInt - quantity;
                        UpdateItem updateItem=new UpdateItem(productId,size,newQuantity);
                        availableItems.add(item);
                        updateStocks(updateItem);
                        writePurchase(item);

                    } else{
                        nonAvailableItems.add(new AvailabilityModel(item.getImageUrl(),item.getProductName(),item.getSize(),item.getQuantity()));
                    }

                    //Toast.makeText(application.getApplicationContext(),"Size came"+valueInt,Toast.LENGTH_SHORT).show();

                }




                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
            //nonAvailable.setValue(nonAvailableItems);

            //updateStocks(availableItems);


        }

        //updateStocks(toUpdate);
        //writePurchase(availableItems);
        nonAvailable.postValue(nonAvailableItems);
        if(condition.equals("cart")) {

            deleteCart();
        }



    }

    public void checkCartItemsGuest(PurchasesItem item, GuestItem guestDetails){

        List<AvailabilityModel> nonAvailableItems=new ArrayList<>();
        List<PurchasesItem> availableItems=new ArrayList<>();
        List<UpdateItem> toUpdate=new ArrayList<>();



            String productId = item.getProductId();
            String size = item.getSize();
            int quantity = item.getQuantity();


            databaseReference.child("products").child(productId).child(size).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    Integer value=snapshot.getValue(Integer.class);
                    int valueInt=value;

                    if (valueInt >= quantity) {
                        int newQuantity = valueInt - quantity;
                        UpdateItem updateItem=new UpdateItem(productId,size,newQuantity);
                        availableItems.add(item);
                        updateStocks(updateItem);
                        writeTransaction(item,guestDetails);

                    } else{
                        nonAvailableItems.add(new AvailabilityModel(item.getImageUrl(),item.getProductName(),item.getSize(),item.getQuantity()));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        nonAvailable.postValue(nonAvailableItems);

    }

    public void updateStocks(UpdateItem updates){

            Map<String, Object> toUpdate = new HashMap<String, Object>();
            toUpdate.put(updates.getSize(), updates.getQuantity());
            databaseReference.child("products").child(updates.getProductId()).updateChildren(toUpdate);
            Toast.makeText(application.getApplicationContext(), "Came update", Toast.LENGTH_SHORT).show();

    }

    public void deleteCart(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("cart").child(user.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                //Toast.makeText(application.getApplicationContext(),"Successfully deleted the item!",Toast.LENGTH_SHORT).show();
            }
        });


    }



    public void writePurchase(PurchasesItem finalItems){

            String userId;
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            String key = databaseReference.push().getKey();
            if (user != null) {
                userId = user.getUid();
            } else {
                userId = "Guest" + key;
            }
            databaseReference.child("purchases").child(userId).child(key).setValue(finalItems).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    Toast.makeText(application.getApplicationContext(), "Purchase Successfully recorded!", Toast.LENGTH_SHORT).show();

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(application.getApplicationContext(), "Purchase recording unsuccessful!", Toast.LENGTH_SHORT).show();
                }
            });



    }

    public void writeTransaction(PurchasesItem finalItems,GuestItem guestItem){

        String userId;

        String key = databaseReference.push().getKey();

            userId = "Guest" + key;

        databaseReference.child("purchases").child(userId).child(key).setValue(finalItems).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                writeGuest(userId,key, guestItem);

                Toast.makeText(application.getApplicationContext(), "Purchase Successfully recorded!", Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(), "Purchase recording unsuccessful!", Toast.LENGTH_SHORT).show();
            }
        });



    }

    public void writeGuest(String userId,String key, GuestItem guestItem){
        databaseReference.child("guestpurchases").child(userId).child(key).setValue(guestItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(), "Successfully recorded!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<List<User>> getUserData(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            databaseReference.child("users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        List<User> userDetails = new ArrayList<>();
                        userDetails.add(new User(user.getEmail(), user.getName(), user.getAddress(), user.getPhone()));
                        accountData.postValue(userDetails);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(application.getApplicationContext(), "Error getting data", Toast.LENGTH_SHORT).show();

                }
            });
        }
        return accountData;
    }

    public MutableLiveData<List<PaymentDetails>> getPaymentData(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null) {
            databaseReference.child("payment").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        PaymentDetails payment = snapshot.getValue(PaymentDetails.class);
                        List<PaymentDetails> paymentDetails = new ArrayList<>();
                        if (payment != null) {
                            char[] cardNumber = String.valueOf(payment.getNumber()).toCharArray();
                            for (int x = 0; x < cardNumber.length - 4; x++) {
                                cardNumber[x] = 'x';
                            }
                            String encryptedCard = String.valueOf(cardNumber);
                            paymentDetails.add(new PaymentDetails(encryptedCard, payment.getCvv(), payment.getDate(), payment.getType()));
                            paymentData.postValue(paymentDetails);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(application.getApplicationContext(), "Error getting data", Toast.LENGTH_SHORT).show();

                }
            });
        }
        return paymentData;
    }

    public void writeFeedbackToDatabase(RatingItem ratingItem, String productId){
        if(productId!=null) {
            String key=databaseReference.push().getKey();

            databaseReference.child("ratings").child(productId).child(key).setValue(ratingItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    isSubmitted.setValue(true);
                    Toast.makeText(application.getApplicationContext(),"Rating Submitted!",Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(application.getApplicationContext(),"Rating Submission failed! "+ e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Toast.makeText(application.getApplicationContext(),"ProductId null",Toast.LENGTH_SHORT).show();
        }

    }


    public MutableLiveData<Boolean> getPurchasesAvailability() {
        return purchasesAvailability;
    }

    public MutableLiveData<List<AvailabilityModel>> getNonPurchased(){
        return nonAvailable;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData(){
        return loggedOutLiveData;
    }

    public MutableLiveData<Boolean> getIsSubmitted() {
        return isSubmitted;
    }
}
