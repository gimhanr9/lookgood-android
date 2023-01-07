package Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008385.lookgood.R;
import com.cb008385.lookgood.databinding.NonAvailableItemBinding;

import models.AvailabilityModel;

public class NonAvailabilityAdapter extends ListAdapter<AvailabilityModel,NonAvailabilityAdapter.NonAvailabilityViewHolder> {
    private LayoutInflater layoutInflater;

    public NonAvailabilityAdapter() {
        super(DIFF_CALLBACK);
    }
    private static final DiffUtil.ItemCallback<AvailabilityModel> DIFF_CALLBACK=new DiffUtil.ItemCallback<AvailabilityModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull AvailabilityModel oldItem, @NonNull AvailabilityModel newItem) {
            return oldItem.getName().equals(newItem.getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull AvailabilityModel oldItem, @NonNull AvailabilityModel newItem) {
            return oldItem.getImageUrl().equals(newItem.getImageUrl())
                    && oldItem.getSize().equals(newItem.getSize()) && oldItem.getQuantity()==newItem.getQuantity();

        }
    };

    @NonNull
    @Override
    public NonAvailabilityAdapter.NonAvailabilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater==null){
            layoutInflater= LayoutInflater.from(parent.getContext());
        }
        NonAvailableItemBinding nonAvailableItemBinding= DataBindingUtil.inflate(
                layoutInflater, R.layout.non_available_item,parent,false
        );
        return new NonAvailabilityAdapter.NonAvailabilityViewHolder(nonAvailableItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull NonAvailabilityAdapter.NonAvailabilityViewHolder holder, int position) {
        holder.bindNonAvailable(getItem(position));

    }

    public AvailabilityModel getItemAt(int position){
        return getItem(position);
    }

    class NonAvailabilityViewHolder extends RecyclerView.ViewHolder{
        private NonAvailableItemBinding nonAvailableItemBinding;

        public NonAvailabilityViewHolder(NonAvailableItemBinding nonAvailableItemBinding) {
            super(nonAvailableItemBinding.getRoot());
            this.nonAvailableItemBinding=nonAvailableItemBinding;
        }

        public void bindNonAvailable(AvailabilityModel availabilityModel){
            nonAvailableItemBinding.setNonAvailableItem(availabilityModel);
            nonAvailableItemBinding.executePendingBindings();
        }
    }
}
