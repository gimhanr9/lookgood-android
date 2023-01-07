package com.cb008385.lookgood;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008385.lookgood.databinding.ManagerProductBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import Adapter.UploadImageAdapter;
import ViewModel.AddProductsViewModel;
import models.CartItem;
import models.ProductItem;

public class ProductManager extends AppCompatActivity {
    private ManagerProductBinding managerProductBinding;
    private AddProductsViewModel addProductsViewModel;
    private static final int RESULT_LOAD_IMAGE=1;
    private List<String> imageNameList;
    private List<String> convertedUriList;
    private List<String> downloadedUrlList;
    private UploadImageAdapter uploadImageAdapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        managerProductBinding= DataBindingUtil.setContentView(this,R.layout.manager_product);
        initView();

        if(downloadedUrlList!=null) {
            if(downloadedUrlList.size()==convertedUriList.size()) {

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
                itemTouchHelper.attachToRecyclerView(recyclerView);
            }
        }else{
            Toast.makeText(this,"Please wait for images to get uploaded",Toast.LENGTH_SHORT).show();
        }

    }

    ItemTouchHelper.SimpleCallback simpleCallback=new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.START | ItemTouchHelper.END,ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            if(downloadedUrlList!=null) {
                if(downloadedUrlList.size()==convertedUriList.size()) {
            int fromPosition=viewHolder.getAdapterPosition();
            int toPosition=target.getAdapterPosition();
            Collections.swap(imageNameList,fromPosition,toPosition);
            Collections.swap(convertedUriList,fromPosition,toPosition);
            Collections.swap(downloadedUrlList,fromPosition,toPosition);
            uploadImageAdapter.notifyItemMoved(fromPosition,toPosition);
            return false;
                }
            }
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            if(downloadedUrlList!=null) {
                if(downloadedUrlList.size()==convertedUriList.size()) {
            String name="Item removed";
            int position=viewHolder.getAdapterPosition();
            String deletedImage=imageNameList.get(position);
            String deletedUri=convertedUriList.get(position);
            imageNameList.remove(position);
            convertedUriList.remove(position);
            downloadedUrlList.remove(position);
            addProductsViewModel.deleteImage(deletedImage);
            uploadImageAdapter.notifyItemRemoved(position);
            Snackbar snackbar=Snackbar.make(recyclerView,name,Snackbar.LENGTH_LONG)
                    .setAction("Undo", v -> {
                        //addProductsViewModel.deleteImage(deletedUri);

                    });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
                }
            }


        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK){
            if(data.getClipData()!=null){
                int totalItemsSelected=data.getClipData().getItemCount();
                for(int x=0; x<totalItemsSelected;x++){

                    Uri fileUri=data.getClipData().getItemAt(x).getUri();
                    String imageName=getImageName(fileUri);
                    convertedUriList.add(fileUri.toString());
                    imageNameList.add(imageName);
                    uploadImageAdapter.notifyDataSetChanged();
                    addProductsViewModel.getImageUploadDetails(convertedUriList);
                    managerProductBinding.setIsLoading(true);


                }

               // Toast.makeText(ProductManager.this,"Select multiple images",Toast.LENGTH_SHORT).show();
            }else if(data.getData()!=null){
                Toast.makeText(ProductManager.this,"Select image",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public String getImageName(Uri uri){
        String result=null;
        if(uri.getScheme().equals("content")){
            Cursor cursor=getContentResolver().query(uri,null,null,null,null);
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

    public void initView(){
        addProductsViewModel=new ViewModelProvider(this).get(AddProductsViewModel.class);
        addProductsViewModel.getUrlDetails().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                managerProductBinding.setIsLoading(false);
                downloadedUrlList.addAll(list);
                
            }
        });


        //adapter for category spinner
        ArrayAdapter<CharSequence> arrayAdapter=ArrayAdapter.createFromResource(this,R.array.items_array,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        managerProductBinding.categoryItem.setAdapter(arrayAdapter);

        //adapter for brand spinner
        ArrayAdapter<CharSequence> arrayAdapter1=ArrayAdapter.createFromResource(this,R.array.brand_array,android.R.layout.simple_spinner_item);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        managerProductBinding.productBrand.setAdapter(arrayAdapter1);
        recyclerView=managerProductBinding.imageList;

        imageNameList=new ArrayList<>();
        downloadedUrlList=new ArrayList<>();
        convertedUriList=new ArrayList<>();
        uploadImageAdapter=new UploadImageAdapter(imageNameList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(uploadImageAdapter);


        addProductsViewModel.getUploadDetails().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    managerProductBinding.productName.setText("");
                    managerProductBinding.productDescription.setText("");
                    managerProductBinding.price.setText("");
                    managerProductBinding.quantity.setSelection(0);
                    managerProductBinding.quantityMedium.setSelection(0);
                    managerProductBinding.quantityLarge.setSelection(0);
                    managerProductBinding.quantityXlarge.setSelection(0);
                    managerProductBinding.quantityXxlarge.setSelection(0);
                    imageNameList.clear();
                    convertedUriList.clear();
                    downloadedUrlList.clear();
                    uploadImageAdapter.notifyDataSetChanged();

                }
            }
        });

        managerProductBinding.addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select images"),RESULT_LOAD_IMAGE);
            }
        });
        managerProductBinding.uploadAllButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = managerProductBinding.productName.getText().toString().trim();
                double realPrice = 0.0;
                String brand = managerProductBinding.productBrand.getSelectedItem().toString();
                String category = managerProductBinding.categoryItem.getSelectedItem().toString();
                String title=managerProductBinding.productSubtitle.getText().toString().trim();
                String description = managerProductBinding.productDescription.getText().toString().trim();
                String price = managerProductBinding.price.getText().toString().trim();
                int small = Integer.parseInt(managerProductBinding.quantity.getSelectedItem().toString());
                int medium = Integer.parseInt(managerProductBinding.quantityMedium.getSelectedItem().toString());
                int large = Integer.parseInt(managerProductBinding.quantityLarge.getSelectedItem().toString());
                int xlarge = Integer.parseInt(managerProductBinding.quantityXlarge.getSelectedItem().toString());
                int xxlarge = Integer.parseInt(managerProductBinding.quantityXxlarge.getSelectedItem().toString());


                    if (name.length() > 0 && description.length() >= 50 && downloadedUrlList.size()>0 && price.length() > 0) {
                        Toast.makeText(ProductManager.this, "conditions valid", Toast.LENGTH_SHORT).show();
                        realPrice = Double.parseDouble(price);
                        ProductItem productItem = new ProductItem(downloadedUrlList.get(0), name,title, description, category, brand, realPrice, small, medium, large, xlarge, xxlarge);
                        addProductsViewModel.uploadProduct(productItem,downloadedUrlList);
                        finish();

                    } else {
                        if (name.length() == 0) {
                            Toast.makeText(ProductManager.this, "Please enter a valid product name!", Toast.LENGTH_SHORT).show();

                        } else if (description.length() < 50) {
                            Toast.makeText(ProductManager.this, "Please enter a description of at least 50 words!", Toast.LENGTH_SHORT).show();

                        } else if (price.length() == 0) {
                            Toast.makeText(ProductManager.this, "Please enter a valid product price!", Toast.LENGTH_SHORT).show();
                        } else if (small == 0 && medium == 0 && large == 0 && xlarge == 0 && xxlarge == 0) {
                            Toast.makeText(ProductManager.this, "Please enter quantities for product sizes!", Toast.LENGTH_SHORT).show();

                        }else if(downloadedUrlList.size()==0){
                            Toast.makeText(ProductManager.this,"Please add two images",Toast.LENGTH_SHORT).show();
                        }
                    }

            }
        });

    }
}
