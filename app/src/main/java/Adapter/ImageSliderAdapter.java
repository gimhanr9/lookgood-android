package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008385.lookgood.R;
import com.cb008385.lookgood.databinding.ImageSliderContainerBinding;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder> {

    private String [] sliderImages;
    private LayoutInflater layoutInflater;

    public ImageSliderAdapter(String[] sliderImages) {
        this.sliderImages = sliderImages;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater==null){
            layoutInflater=LayoutInflater.from(parent.getContext());
        }
        ImageSliderContainerBinding sliderContainerBinding= DataBindingUtil.inflate(layoutInflater, R.layout.image_slider_container,parent,false);

        return new ImageSliderViewHolder(sliderContainerBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {
        holder.bindSliderImage(sliderImages[position]);

    }

    @Override
    public int getItemCount() {
        return sliderImages.length;
    }

    static class ImageSliderViewHolder extends RecyclerView.ViewHolder{
        private ImageSliderContainerBinding imageSliderContainerBinding;

        public ImageSliderViewHolder(ImageSliderContainerBinding imageSliderContainerBinding) {
            super(imageSliderContainerBinding.getRoot());
            this.imageSliderContainerBinding=imageSliderContainerBinding;
        }

        public void bindSliderImage(String imageURL){
            imageSliderContainerBinding.setImageURL(imageURL);

        }
    }
}
