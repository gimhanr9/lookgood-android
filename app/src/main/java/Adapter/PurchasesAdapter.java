package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008385.lookgood.R;
import com.cb008385.lookgood.databinding.PurchasesItemBinding;

import listeners.PurchasesItemReviewClick;
import models.PurchasesItem;

public class PurchasesAdapter extends ListAdapter<PurchasesItem,PurchasesAdapter.PurchasesViewHolder> {
    private LayoutInflater layoutInflater;
    private PurchasesItemReviewClick purchasesItemReviewClick;

    public PurchasesAdapter(PurchasesItemReviewClick purchasesItemReviewClick) {
        super(DIFF_CALLBACK);
        this.purchasesItemReviewClick=purchasesItemReviewClick;
    }

    private static final DiffUtil.ItemCallback<PurchasesItem> DIFF_CALLBACK=new DiffUtil.ItemCallback<PurchasesItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull PurchasesItem oldItem, @NonNull PurchasesItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull PurchasesItem oldItem, @NonNull PurchasesItem newItem) {
            return oldItem.getProductId().equals(newItem.getProductId()) && oldItem.getImageUrl().equals(newItem.getImageUrl()) && oldItem.getProductName().equals(newItem.getProductName())
                    && oldItem.getSize().equals(newItem.getSize()) && oldItem.getQuantity()==newItem.getQuantity() && oldItem.getPrice()==newItem.getPrice();
        }
    };


    @NonNull
    @Override
    public PurchasesAdapter.PurchasesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater==null){
            layoutInflater=LayoutInflater.from(parent.getContext());
        }
        PurchasesItemBinding purchasesItemBinding= DataBindingUtil.inflate(
                layoutInflater, R.layout.purchases_item,parent,false
        );
        return new PurchasesAdapter.PurchasesViewHolder(purchasesItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull PurchasesAdapter.PurchasesViewHolder holder, int position) {
        holder.bindPurchases(getItem(position));

    }

    public PurchasesItem getItemAt(int position){
        return getItem(position);
    }




    class PurchasesViewHolder extends RecyclerView.ViewHolder{

        private PurchasesItemBinding purchasesItemBinding;

        public PurchasesViewHolder(PurchasesItemBinding purchasesItemBinding) {
            super(purchasesItemBinding.getRoot());
            this.purchasesItemBinding=purchasesItemBinding;
        }

        public void bindPurchases(PurchasesItem purchasesItem){
            purchasesItemBinding.setPurchasesItem(purchasesItem);
            purchasesItemBinding.executePendingBindings();
            purchasesItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    purchasesItemReviewClick.onPurchasesReviewClicked(purchasesItem,getAdapterPosition());
                }
            });
            if(purchasesItem.isRated()){
                purchasesItemBinding.writeReview.setText("Edit Review");
            }
            purchasesItemBinding.writeReview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    purchasesItemReviewClick.onWriteReviewClicked(purchasesItem,getAdapterPosition());
                }
            });
            purchasesItemBinding.writeReview.setVisibility(View.VISIBLE);
        }
    }

}
