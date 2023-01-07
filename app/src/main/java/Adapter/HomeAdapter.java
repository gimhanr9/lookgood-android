package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008385.lookgood.R;
import com.cb008385.lookgood.databinding.ProductItemBinding;

import java.util.List;

import listeners.ProductClickListener;
import models.ProductItem;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {
    private LayoutInflater layoutInflater;
    private List<ProductItem> products;
    private ProductClickListener productClickListener;

    public HomeAdapter(List<ProductItem> products, ProductClickListener productClickListener) {
        this.products = products;
        this.productClickListener = productClickListener;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater==null){
            layoutInflater=LayoutInflater.from(parent.getContext());
        }
        ProductItemBinding productListBinding= DataBindingUtil.inflate(
                layoutInflater, R.layout.product_item,parent,false
        );
        return new HomeViewHolder(productListBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int position) {
        holder.bindProducts(products.get(position));

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public ProductItem getProductAt(int position){
        return products.get(position);
    }

    class HomeViewHolder extends RecyclerView.ViewHolder{
        private ProductItemBinding productItemBinding;

        public HomeViewHolder(ProductItemBinding productItemBinding) {
            super(productItemBinding.getRoot());
            this.productItemBinding=productItemBinding;
        }

        public void bindProducts(ProductItem productItem){
            productItemBinding.setProduct(productItem);
            productItemBinding.executePendingBindings();
            productItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    productClickListener.onProductClicked(productItem);
                }
            });
        }
    }


}
