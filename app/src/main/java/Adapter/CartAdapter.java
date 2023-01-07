package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008385.lookgood.R;
import com.cb008385.lookgood.databinding.CartItemBinding;

import listeners.CartItemClick;
import models.CartItem;

public class CartAdapter extends ListAdapter<CartItem,CartAdapter.CartViewHolder> {

    private LayoutInflater layoutInflater;
    private CartItemClick cartItemClick;

    public CartAdapter(CartItemClick cartItemClick) {
        super(DIFF_CALLBACK);
        this.cartItemClick=cartItemClick;
    }

    private static final DiffUtil.ItemCallback<CartItem> DIFF_CALLBACK=new DiffUtil.ItemCallback<CartItem>() {
        @Override
        public boolean areItemsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CartItem oldItem, @NonNull CartItem newItem) {
            return oldItem.getProductId().equals(newItem.getProductId()) && oldItem.getImageUrl().equals(newItem.getImageUrl()) && oldItem.getProductName().equals(newItem.getProductName())
                    && oldItem.getSize().equals(newItem.getSize()) && oldItem.getQuantity()==newItem.getQuantity() && oldItem.getPrice()==newItem.getPrice();
        }
    };


    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater==null){
            layoutInflater=LayoutInflater.from(parent.getContext());
        }
        CartItemBinding cartItemBinding= DataBindingUtil.inflate(
                layoutInflater, R.layout.cart_item,parent,false
        );
        return new CartAdapter.CartViewHolder(cartItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bindCart(getItem(position));

    }

    public CartItem getItemAt(int position){
        return getItem(position);
    }




    class CartViewHolder extends RecyclerView.ViewHolder{

        private CartItemBinding cartItemBinding;

        public CartViewHolder(CartItemBinding cartItemBinding) {
            super(cartItemBinding.getRoot());
            this.cartItemBinding=cartItemBinding;
        }

        public void bindCart(CartItem cartItem){
            cartItemBinding.setCartItem(cartItem);
            cartItemBinding.executePendingBindings();
            cartItemBinding.quantity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int quantity = position + 1;
                    if (quantity == getItem(getAdapterPosition()).getQuantity()) {
                        return;
                    }
                    cartItemClick.changeQuantity(getItem(getAdapterPosition()), quantity);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}
