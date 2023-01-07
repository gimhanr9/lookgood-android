package Repository;

import android.app.Application;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

import models.ProductItem;

public class AddProductRepository {

    private Application application;
    private StorageReference storageReference;
    private DatabaseReference databaseReference;
    private MutableLiveData<Boolean> productUploadState;
    private List<String> imageUrls;
    private MutableLiveData<List<String>> downloadedUrls;
    private MutableLiveData<List<String>> downloadedSingleUrl;

    public AddProductRepository(Application application) {
        this.application=application;
        this.storageReference= FirebaseStorage.getInstance().getReference();
        this.databaseReference = FirebaseDatabase.getInstance().getReference();
        this.productUploadState = new MutableLiveData<>();
        this.downloadedUrls = new MutableLiveData<>();
        this.downloadedSingleUrl = new MutableLiveData<>();
        this.imageUrls=new ArrayList<>();
    }

    public void uploadProduct(ProductItem productItem,List<String> images){

            String key = databaseReference.push().getKey();
            databaseReference.child("products").child(key).setValue(productItem).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    addImagesToDatabase(key,images);

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(application.getApplicationContext(), "Couldn't Upload!" + e, Toast.LENGTH_SHORT).show();
                }
            });

    }

    public String getImageName(Uri uri){
        String result=null;
        if(uri.getScheme().equals("content")){
            Cursor cursor=application.getApplicationContext().getContentResolver().query(uri,null,null,null,null);
            try{
                if(cursor!=null && cursor.moveToFirst()){
                    result=cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            }finally {
                cursor.close();
            }
        }
        if(result==null){
            result=uri.getPath();
            int cut=result.lastIndexOf('/');
            if(cut != -1){
                result=result.substring(cut + 1);
            }
        }
        return  result;
    }

    public void getImageUrl(List<String> list) {
        List<String> downloadedUrlList = new ArrayList<>();
        for (int x = 0; x < list.size(); x++) {
            Uri imageUri = Uri.parse(list.get(x));
            String imageName = getImageName(imageUri);

            StorageReference imageToUpload = storageReference.child("images").child(imageName);
            imageToUpload.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageToUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadedUrlList.add(String.valueOf(uri));
                            if(downloadedUrlList.size()==list.size()){
                                downloadedUrls.setValue(downloadedUrlList);

                            }


                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(application.getApplicationContext(),"Image upload failed"+e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            });

        }



    }

    public void getSingleImageUrl(String uri) {
        List<String> downloadedUrlList = new ArrayList<>();
            Uri imageUri = Uri.parse(uri);
            String imageName = getImageName(imageUri);

            StorageReference imageToUpload = storageReference.child("images").child(imageName);
            imageToUpload.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    imageToUpload.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadedUrlList.add(String.valueOf(uri));
                            downloadedSingleUrl.setValue(downloadedUrlList);

                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(application.getApplicationContext(), "Image upload failed" + e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });


    }

    public void deleteImages(String imageName){
        StorageReference deleteReference=storageReference.child("images/"+imageName);
        deleteReference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(application.getApplicationContext(),"Successfully deleted image",Toast.LENGTH_SHORT).show();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(application.getApplicationContext(),"Could not delete image",Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void addImagesToDatabase(String key,List<String>imageUri){

        databaseReference.child("images").child(key).setValue(imageUri).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                productUploadState.postValue(true);
                Toast.makeText(application.getApplicationContext(),"All Uploaded!",Toast.LENGTH_SHORT).show();
            }
        });

    }
    public MutableLiveData<Boolean> getUploadDetails() {
        return productUploadState;
    }

    public MutableLiveData<List<String>> getImageUploadDetails() {
        return downloadedUrls;
    }

    public MutableLiveData<List<String>> getSingleUploadDetails() {
        return downloadedSingleUrl;
    }
}
