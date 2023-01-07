package Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008385.lookgood.R;
import com.cb008385.lookgood.databinding.RatingItemBinding;

import models.RatingItem;

public class RatingAdapter extends ListAdapter<RatingItem,RatingAdapter.RatingViewHolder> {

    private LayoutInflater layoutInflater;

    public RatingAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<RatingItem> DIFF_CALLBACK=new DiffUtil.ItemCallback<RatingItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull RatingItem oldItem, @NonNull RatingItem newItem) {
            return oldItem.getRatingId().equals(newItem.getRatingId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull RatingItem oldItem, @NonNull RatingItem newItem) {
            return oldItem.getProductId().equals(newItem.getProductId())
                    && oldItem.getName().equals(newItem.getName())  &&
                    oldItem.getRatingText().equals(newItem.getRatingText())  && oldItem.getDate().equals(newItem.getDate())  &&
                    oldItem.getRatingValue()==newItem.getRatingValue();
        }
    };


    @NonNull
    @Override
    public RatingAdapter.RatingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater==null){
            layoutInflater=LayoutInflater.from(parent.getContext());
        }
        RatingItemBinding ratingItemBinding= DataBindingUtil.inflate(
                layoutInflater, R.layout.rating_item,parent,false
        );
        return new RatingAdapter.RatingViewHolder(ratingItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RatingAdapter.RatingViewHolder holder, int position) {
        holder.bindRating(getItem(position));

    }

    public RatingItem getItemAt(int position){
        return getItem(position);
    }




    class RatingViewHolder extends RecyclerView.ViewHolder{

        private RatingItemBinding ratingItemBinding;

        public RatingViewHolder(RatingItemBinding ratingItemBinding) {
            super(ratingItemBinding.getRoot());
            this.ratingItemBinding=ratingItemBinding;
        }

        public void bindRating(RatingItem ratingItem){
            ratingItemBinding.setRatingList(ratingItem);
            ratingItemBinding.executePendingBindings();
        }
    }
}
