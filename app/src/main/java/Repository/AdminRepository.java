package Repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.ProductItem;
import models.Question;
import models.RatingItem;

public class AdminRepository {
    private Application application;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private MutableLiveData<List<ProductItem>> products;
    private MutableLiveData<List<String>> images;
    private MutableLiveData<List<Question>> questionDetails;
    private MutableLiveData<Boolean> questionAvailability;
    private MutableLiveData<List<RatingItem>> ratingDetails;
    private MutableLiveData<Boolean> ratingAvailability;
    private MutableLiveData<Float> totalRating;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<Integer> ratingNumber;
    private MutableLiveData<List<ProductItem>> brandProducts;
    private MutableLiveData<List<String>> brands;
    private int counterHelper;



    public AdminRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.products=new MutableLiveData<>();
        this.questionDetails = new MutableLiveData<>();
        this.questionAvailability = new MutableLiveData<>();
        this.ratingDetails = new MutableLiveData<>();
        this.ratingAvailability = new MutableLiveData<>();
        this.images = new MutableLiveData<>();
        this.totalRating=new MutableLiveData<>();
        this.loggedOutLiveData=new MutableLiveData<>();
        this.ratingNumber=new MutableLiveData<>();
        this.brandProducts=new MutableLiveData<>();
        this.brands=new MutableLiveData<>();
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

    public MutableLiveData<List<ProductItem>> getProducts(){
        databaseReference.child("products").addValueEventListener(new ValueEventListener() {
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

    public void  deleteProducts(String productId){
        databaseReference.child("products").child(productId).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(),"Successfully deleted the product!",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public MutableLiveData<List<ProductItem>> getBrand(String brand){
        databaseReference.child("products").addListenerForSingleValueEvent(new ValueEventListener() {
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

    public void submitAnswer(String productId,String questionId,String answer){

        Map<String, Object> toUpdate = new HashMap<String, Object>();
        toUpdate.put("answer", answer);
        databaseReference.child("questions").child(productId).child(questionId).updateChildren(toUpdate);

    }

    public MutableLiveData<Float> getTotalRatings(String productId){
        databaseReference.child("ratings").child(productId).addValueEventListener(new ValueEventListener() {
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

    public void update(String productId, Map<String, Object> toUpdate){
        Map<String, Object> dbValues = new HashMap<String, Object>();
        Map<String, Object> updatedValues = new HashMap<String, Object>();
        databaseReference.child("products").child(productId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){

                    ProductItem productItem=snapshot.getValue(ProductItem.class);
                    String key=snapshot.getKey();
                    productItem.setId(key);
                    dbValues.put("small",productItem.getSmall());
                    dbValues.put("medium",productItem.getMedium());
                    dbValues.put("large",productItem.getLarge());
                    dbValues.put("xlarge",productItem.getXlarge());
                    dbValues.put("xxl",productItem.getXxl());
                    int newSmall=Integer.parseInt(String.valueOf(dbValues.get("small")))+Integer.parseInt(String.valueOf(toUpdate.get("small")));
                    int newMedium=Integer.parseInt(String.valueOf(dbValues.get("medium")))+Integer.parseInt(String.valueOf(toUpdate.get("medium")));
                    int newLarge=Integer.parseInt(String.valueOf(dbValues.get("large")))+Integer.parseInt(String.valueOf(toUpdate.get("large")));
                    int newXlarge=Integer.parseInt(String.valueOf(dbValues.get("xlarge")))+Integer.parseInt(String.valueOf(toUpdate.get("xlarge")));
                    int newXxl=Integer.parseInt(String.valueOf(dbValues.get("xxl")))+Integer.parseInt(String.valueOf(toUpdate.get("xxl")));
                    updatedValues.put("small",newSmall);
                    updatedValues.put("medium",newMedium);
                    updatedValues.put("large",newLarge);
                    updatedValues.put("xlarge",newXlarge);
                    updatedValues.put("xxl",newXxl);
                    doUpdate(productId,updatedValues);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




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

    public void doUpdate(String productId,Map<String, Object> tempValues){
        databaseReference.child("products").child(productId).updateChildren(tempValues);
        Toast.makeText(application.getApplicationContext(),"Updated!",Toast.LENGTH_SHORT).show();
    }
    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }
    public MutableLiveData<Integer> noOfRatings(){
        return ratingNumber;

    }

    public MutableLiveData<Boolean> getQuestionAvailability() {
        return questionAvailability;
    }
    public MutableLiveData<Boolean> getRatingAvailability() {
        return ratingAvailability;
    }
    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }
}
