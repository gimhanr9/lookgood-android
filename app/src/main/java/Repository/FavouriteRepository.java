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

import models.Favourites;

public class FavouriteRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<List<Favourites>> favouriteDetails;
    private MutableLiveData<Boolean> favouriteAvailability;

    public FavouriteRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.favouriteDetails = new MutableLiveData<>();
        this.favouriteAvailability = new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            loggedOutLiveData.postValue(false);

        } else {
            loggedOutLiveData.postValue(true);
        }
    }

    public MutableLiveData<List<Favourites>> getFavouriteItems() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            databaseReference.child("favourites").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        List<Favourites> favourites = new ArrayList<>();
                        for (DataSnapshot favouriteSnapshot : snapshot.getChildren()) {
                            Favourites favourites1 = favouriteSnapshot.getValue(Favourites.class);
                            favourites.add(new Favourites(favouriteSnapshot.getKey(), favourites1.getImageUrl(), favourites1.getProductName(), favourites1.getProductTitle(), favourites1.getPrice()));
                        }
                        favouriteDetails.postValue(favourites);
                        favouriteAvailability.postValue(true);
                    } else {
                        favouriteAvailability.postValue(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        return favouriteDetails;
    }

    public void deleteFavouriteItem(String productId){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("favourites").child(user.getUid()).child(productId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(),"Successfully deleted the item!",Toast.LENGTH_SHORT).show();
            }
        });
    }


    public void addToFavouritesAgain(Favourites favourites,String productId){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Favourites favourites1=new Favourites(favourites.getProductId(),favourites.getImageUrl(),favourites.getProductName(),favourites.getProductTitle(), favourites.getPrice());
        databaseReference.child("favourites").child(user.getUid()).child(productId).setValue(favourites1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(),"Successfully added to favourites!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(),"Could not add to favourites!"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }
    public MutableLiveData<Boolean> getFavouritesAvailability() {
        return favouriteAvailability;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData(){
        return loggedOutLiveData;
    }
}
