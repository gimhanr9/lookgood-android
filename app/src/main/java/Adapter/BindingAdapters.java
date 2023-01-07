package Adapter;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class BindingAdapters {
    @BindingAdapter("android:src")

    public static void setImage(ImageView imageView, Drawable icon){



        Glide.with(imageView.getContext())
                .load(icon)
                .into(imageView);


    }

    @BindingAdapter("android:src")

    public static void setImage(ImageView imageView, int icon){


        Glide.with(imageView.getContext())
                .load(icon)
                .into(imageView);

    }

    @BindingAdapter("android:imageURL")
    public static void setImageURL(ImageView imageView,String URL){
        Glide.with(imageView.getContext())
                .load(URL)
                .into(imageView);

    }

    @BindingAdapter("android:setQty")
    public static void getSelectedSpinnerValue(Spinner spinner, int quantity) {
        spinner.setSelection(quantity - 1, true);
    }

}