package Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008385.lookgood.R;
import com.cb008385.lookgood.databinding.FavouritesItemBinding;

import models.Favourites;

public class FavouritesAdapter extends ListAdapter<Favourites,FavouritesAdapter.FavouritesViewHolder> {
    private LayoutInflater layoutInflater;

    public FavouritesAdapter() {
        super(DIFF_CALLBACK);
    }
    private static final DiffUtil.ItemCallback<Favourites> DIFF_CALLBACK=new DiffUtil.ItemCallback<Favourites>() {
        @Override
        public boolean areItemsTheSame(@NonNull Favourites oldItem, @NonNull Favourites newItem) {
            return oldItem.getProductId().equals(newItem.getProductId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Favourites oldItem, @NonNull Favourites newItem) {
            return oldItem.getImageUrl().equals(newItem.getImageUrl()) && oldItem.getProductName().equals(newItem.getProductName())
                    && oldItem.getProductTitle().equals(newItem.getProductTitle()) && oldItem.getPrice()==newItem.getPrice();

        }
    };

    @NonNull
    @Override
    public FavouritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater==null){
            layoutInflater= LayoutInflater.from(parent.getContext());
        }
        FavouritesItemBinding favouritesItemBinding= DataBindingUtil.inflate(
                layoutInflater, R.layout.favourites_item,parent,false
        );
        return new FavouritesAdapter.FavouritesViewHolder(favouritesItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouritesViewHolder holder, int position) {
        holder.bindFavourites(getItem(position));

    }

    public Favourites getItemAt(int position){
        return getItem(position);
    }

    class FavouritesViewHolder extends RecyclerView.ViewHolder{
        private FavouritesItemBinding favouritesItemBinding;

        public FavouritesViewHolder(FavouritesItemBinding favouritesItemBinding) {
            super(favouritesItemBinding.getRoot());
            this.favouritesItemBinding=favouritesItemBinding;
        }

        public void bindFavourites(Favourites favourites){
            favouritesItemBinding.setFavouriteItem(favourites);
            favouritesItemBinding.executePendingBindings();
        }
    }
}
