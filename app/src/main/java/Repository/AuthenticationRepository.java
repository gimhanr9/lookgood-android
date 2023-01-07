package Repository;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
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
import models.User;

public class AuthenticationRepository {

    private Application application;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private MutableLiveData<FirebaseUser> userLiveData;
    private MutableLiveData<List<User>> accountData;
    private MutableLiveData<Boolean> loggedOutLiveData;
    private MutableLiveData<Boolean> reauthenticationDetails;
    private MutableLiveData<Boolean> emailUpdateDetails;
    private MutableLiveData<Boolean> passwordUpdateDetails;
    private MutableLiveData<Boolean> passwordEmailDetails;
    private MutableLiveData<List<PaymentDetails>> paymentData;
    private MutableLiveData<List<ProductItem>> products;
    private MutableLiveData<Boolean> loginFailure;

    public AuthenticationRepository(Application application) {
        this.application = application;
        this.firebaseAuth = FirebaseAuth.getInstance();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.userLiveData = new MutableLiveData<>();
        this.loggedOutLiveData = new MutableLiveData<>();
        this.accountData=new MutableLiveData<>();
        this.reauthenticationDetails=new MutableLiveData<>();
        this.emailUpdateDetails =new MutableLiveData<>();
        this.passwordUpdateDetails =new MutableLiveData<>();
        this.passwordEmailDetails =new MutableLiveData<>();
        this.paymentData =new MutableLiveData<>();
        this.products=new MutableLiveData<>();
        this.loginFailure=new MutableLiveData<>();

        if (firebaseAuth.getCurrentUser() != null) {
            userLiveData.postValue(firebaseAuth.getCurrentUser());
            loggedOutLiveData.postValue(false);
            databaseReference.child("users").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
                    User user=snapshot.getValue(User.class);
                    List<User> userDetails=new ArrayList<>();
                    userDetails.add(new User(user.getEmail(),user.getName(),user.getAddress(),user.getPhone()));
                    accountData.postValue(userDetails);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(application.getApplicationContext(),"Error getting data",Toast.LENGTH_SHORT).show();

                }
            });

            databaseReference.child("payment").child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
                    if(snapshot.exists()) {
                        PaymentDetails payment = snapshot.getValue(PaymentDetails.class);
                        List<PaymentDetails> paymentDetails = new ArrayList<>();
                        if(payment!=null) {
                            char[] cardNumber = String.valueOf(payment.getNumber()).toCharArray();
                            for(int x=0;x<cardNumber.length-4;x++){
                                cardNumber[x]='x';
                            }
                            String encryptedCard=String.valueOf(cardNumber);
                            paymentDetails.add(new PaymentDetails(encryptedCard, payment.getCvv(), payment.getDate(), payment.getType()));
                            paymentData.postValue(paymentDetails);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(application.getApplicationContext(),"Error getting data",Toast.LENGTH_SHORT).show();

                }
            });


        }else{
            loggedOutLiveData.postValue(true);
        }

    }

    public void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(ContextCompat.getMainExecutor(application), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userLiveData.postValue(firebaseAuth.getCurrentUser());

                        } else {
                            loginFailure.postValue(true);
                            Toast.makeText(application.getApplicationContext(), "Login Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void register(User user) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword())
                .addOnCompleteListener(ContextCompat.getMainExecutor(application), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            userLiveData.postValue(firebaseAuth.getCurrentUser());

                            //write user details to database apart from authentication.
                            writeNewUser(task.getResult().getUser().getUid(),user.getEmail(),user.getName(),user.getAddress(),user.getPhone());

                        } else {
                            loginFailure.postValue(true);
                            Toast.makeText(application.getApplicationContext(), "Registration Failure: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void updateWholeUser(User user){
        String Uid=firebaseAuth.getCurrentUser().getUid();

        databaseReference.child("users").child(Uid).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(),"Update Successful",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(),"Update failure"+ e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }



    public void writeNewUser(String UserID, String email, String name, String address, String phone){
        User user=new User(email,name,address,phone);
        databaseReference.child("users").child(UserID).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(),"Registration Successful",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(),"Registration failure"+ e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

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



    public void logOut() {
        firebaseAuth.signOut();
        loggedOutLiveData.postValue(true);
    }





    public void reauthenticate(String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), password);

            user.reauthenticate(credential).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()) {
                        reauthenticationDetails.postValue(true);
                    }else{
                        Toast.makeText(application.getApplicationContext(),"Re-authentication Failed",Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    public  void updatePaymentDetails(PaymentDetails paymentDetails){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference.child("payment").child(user.getUid()).setValue(paymentDetails).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(),"Update Successful",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(),"Update not Successful "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateEmail(String email){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String UserID=user.getUid();
                if(task.isSuccessful()) {
                    databaseReference.child("users").child(UserID).child("email").setValue(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            emailUpdateDetails.postValue(true);
                            Toast.makeText(application.getApplicationContext(), "Successfully Updated email", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(application.getApplicationContext(), "Failed to update email" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(),"Failed to update email "+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void updatePassword(String password){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user.updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    passwordUpdateDetails.postValue(true);
                    Toast.makeText(application.getApplicationContext(), "Successfully Updated password", Toast.LENGTH_SHORT).show();

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(),"Failed to update password "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updatePasswordEmail(String email){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    passwordEmailDetails.postValue(true);
                    Toast.makeText(application.getApplicationContext(),"Email sent!",Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(),"Failed to send reset email "+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public MutableLiveData<FirebaseUser> getUserLiveData() {
        return userLiveData;
    }

    public MutableLiveData<List<User>> userData() {
        return accountData;
    }

    public MutableLiveData<List<PaymentDetails>> getpaymentData() {
        return paymentData;
    }

    public MutableLiveData<Boolean> getLoggedOutLiveData() {
        return loggedOutLiveData;
    }

    public MutableLiveData<Boolean> getReauthenticationDetails() {
        return reauthenticationDetails;
    }

    public MutableLiveData<Boolean> getEmailUpdateDetails() {
        return emailUpdateDetails;
    }

    public MutableLiveData<Boolean> getPasswordUpdateDetails() {
        return passwordUpdateDetails;
    }

    public MutableLiveData<Boolean> getPasswordEmailDetails() {
        return passwordEmailDetails;
    }

    public MutableLiveData<Boolean> getLoginFailure() {
        return loginFailure;
    }

}
