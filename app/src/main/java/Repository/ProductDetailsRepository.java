package Repository;

import android.app.Application;
import android.util.Log;
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

import models.CartItem;
import models.Favourites;
import models.ProductItem;
import models.Question;
import models.RatingItem;
import models.User;


public class ProductDetailsRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private MutableLiveData<List<String>> images;
    private MutableLiveData<List<Question>> questionDetails;
    private MutableLiveData<Boolean> questionAvailability;
    private MutableLiveData<List<RatingItem>> ratingDetails;
    private MutableLiveData<Boolean> ratingAvailability;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<Boolean> favouriteAvailability;
    private MutableLiveData<Float> totalRating;
    private MutableLiveData<List<ProductItem>> products;
    private MutableLiveData<Integer> ratingNumber;
    private int counterHelper;
    private boolean isAvailable;


    public ProductDetailsRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.questionDetails = new MutableLiveData<>();
        this.questionAvailability = new MutableLiveData<>();
        this.ratingDetails = new MutableLiveData<>();
        this.ratingAvailability = new MutableLiveData<>();
        this.products=new MutableLiveData<>();
        this.images = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.favouriteAvailability = new MutableLiveData<>();
        this.totalRating=new MutableLiveData<>();
        this.ratingNumber=new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            loggedOutLiveData.postValue(false);
        } else {
            loggedOutLiveData.postValue(true);
        }
    }

    public MutableLiveData<List<String>> getImages(String id){
        databaseReference.child("images").child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    List<String> imageList = new ArrayList<>();
                    for (DataSnapshot imageSnapshot: snapshot.getChildren()) {
                        String imageUrl=imageSnapshot.getValue(String.class);
                        imageList.add(imageUrl);
                    }
                    images.postValue(imageList);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(application.getApplicationContext(),"Error getting images",Toast.LENGTH_SHORT).show();

            }
        });
        return images;
    }

    public MutableLiveData<List<ProductItem>> getProducts(String productId){
        databaseReference.child("products").child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()) {
                    List<ProductItem> productDetails = new ArrayList<>();

                        ProductItem productItem = snapshot.getValue(ProductItem.class);
                        String key = snapshot.getKey();
                        productItem.setId(key);
                        productDetails.add(productItem);

                    products.postValue(productDetails);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return products;
    }

    public void submitQuestion(String productId,String question,String date){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            databaseReference.child("users").child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        User user = snapshot.getValue(User.class);
                        Question question1 = new Question(user.getName(), question, "Not answered yet...", date);
                        writeQuestionToDatabase(productId, question1);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(application.getApplicationContext(), "Error Submitting question", Toast.LENGTH_SHORT).show();

                }
            });
        }else{
            String name="Guest";
            Question question1 = new Question(name, question, "Not answered yet...", date);
            writeQuestionToDatabase(productId, question1);
        }

    }

    public void writeQuestionToDatabase(String productId,Question question){
        if(productId!=null) {
        String key=databaseReference.push().getKey();

        databaseReference.child("questions").child(productId).child(key).setValue(question).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(),"Question Submitted!",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(),"Question Submission failed! "+ e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        }else{
            Toast.makeText(application.getApplicationContext(),"ProductId null",Toast.LENGTH_SHORT).show();
        }

    }
    public MutableLiveData<List<Question>> getQuestions(String productId){

            databaseReference.child("questions").child(productId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        List<Question> questions = new ArrayList<>();
                        for (DataSnapshot questionSnapshot : snapshot.getChildren()) {
                            Question question = questionSnapshot.getValue(Question.class);
                            questions.add(new Question(questionSnapshot.getKey(), productId, question.getName(), question.getQuestion(), question.getAnswer(), question.getDate()));
                        }
                        questionDetails.postValue(questions);
                    }else{
                        questionAvailability.postValue(false);
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        return questionDetails;

    }

    public MutableLiveData<List<RatingItem>> getRatings(String productId){

        databaseReference.child("ratings").child(productId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    List<RatingItem> ratings = new ArrayList<>();
                    for (DataSnapshot ratingSnapshot : snapshot.getChildren()) {
                        RatingItem ratingItem = ratingSnapshot.getValue(RatingItem.class);
                        ratings.add(new RatingItem(ratingSnapshot.getKey(), productId, ratingItem.getPurchaseId(), ratingItem.getName(), ratingItem.getRatingText(), ratingItem.getSize(), ratingItem.getDate(), ratingItem.getRatingValue()));
                    }
                    ratingDetails.postValue(ratings);
                }else{
                    ratingAvailability.postValue(false);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return ratingDetails;

    }
    public MutableLiveData<Boolean> getQuestionAvailability() {
        return questionAvailability;
    }
    public MutableLiveData<Boolean> getRatingAvailability() {
        return ratingAvailability;
    }
    public MutableLiveData<Boolean> getLoggedOutLiveData(){
       /* FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null){
            loggedOutLiveData.postValue(false);
        }else{
            loggedOutLiveData.postValue(true);
        }*/
        return loggedOutLiveData;
    }
    public void checkCart(CartItem userCart){
        //check if the same product and size already exist in the user's cart
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) { //get user's cart information
            databaseReference.child("cart").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    isAvailable=false;
                    if(snapshot.exists()){
                        for (DataSnapshot cartSnapshot : snapshot.getChildren()) {
                            CartItem dbCart = cartSnapshot.getValue(CartItem.class);
                            if(dbCart.getProductId().equals(userCart.getProductId()) && dbCart.getSize().equals(userCart.getSize())){
                                isAvailable=true;
                            }
                        }
                        if(isAvailable){
                            Toast.makeText(application.getApplicationContext(),"Item already in cart!",Toast.LENGTH_SHORT).show();
                        }else{
                            addToCart(userCart);//add item to cart if same product and same size isn't available in user's cart currently
                        }
                    }else{
                        addToCart(userCart);// if user doesn't have anything in cart, then add product to cart
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
    }

    public void addToCart(CartItem cartItem){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser(); //get instance of current user
        String key = databaseReference.push().getKey();
        databaseReference.child("cart").child(user.getUid()).child(key).setValue(cartItem).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(), "Successfully added to cart!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(), "Could not add to cart!" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void addToFavourites(Favourites favourites,String productId){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            databaseReference.child("favourites").child(user.getUid()).child(productId).setValue(favourites).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(application.getApplicationContext(), "Successfully added to favourites!", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(application.getApplicationContext(), "Could not add to favourites!" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    public MutableLiveData<Boolean> isProductFavourite(String productId){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            databaseReference.child("favourites").child(user.getUid()).child(productId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
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
        return favouriteAvailability;

    }
    public void deleteFavouriteItem(String productId){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            databaseReference.child("favourites").child(user.getUid()).child(productId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(application.getApplicationContext(), "Successfully deleted the item!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public MutableLiveData<Float> getTotalRatings(String productId){
        databaseReference.child("ratings").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    int counter=0;
                    float total=0.0f;
                    List<RatingItem> ratings = new ArrayList<>();
                    for (DataSnapshot ratingSnapshot : snapshot.getChildren()) {
                        RatingItem ratingItem = ratingSnapshot.getValue(RatingItem.class);
                        total=total+ratingItem.getRatingValue();
                        counter++;

                    }
                    counterHelper=counter;

                    totalRating.postValue(total/counter);
                }else{
                    totalRating.postValue(0.0f);
                }
                ratingNumber.postValue(counterHelper);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return totalRating;


    }

    public MutableLiveData<Integer> noOfRatings(){
        return ratingNumber;

    }

}
