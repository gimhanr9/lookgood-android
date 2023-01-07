package Adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008385.lookgood.R;
import com.cb008385.lookgood.databinding.FragmentSplashItemBinding;

import java.util.List;

import models.OnboardingItem;

public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder> {

    private List<OnboardingItem> splashDetails;
    private LayoutInflater layoutInflater;

    public OnboardingAdapter(List<OnboardingItem> splashDetails) {
        this.splashDetails = splashDetails;
    }

    @NonNull
    @Override
    public OnboardingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater==null){
            layoutInflater=LayoutInflater.from(parent.getContext());
        }
        FragmentSplashItemBinding fragmentSplashItemBinding= DataBindingUtil.inflate(
                layoutInflater, R.layout.fragment_splash_item,parent,false
        );
        return new OnboardingViewHolder(fragmentSplashItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull OnboardingViewHolder holder, int position) {
        holder.bindDetails(splashDetails.get(position));

    }

    @Override
    public int getItemCount() {
        return splashDetails.size();
    }

    class OnboardingViewHolder extends RecyclerView.ViewHolder{
        private FragmentSplashItemBinding fragmentSplashItemBinding;

        public OnboardingViewHolder(FragmentSplashItemBinding fragmentSplashItemBinding) {
            super(fragmentSplashItemBinding.getRoot());
            this.fragmentSplashItemBinding=fragmentSplashItemBinding;
        }

        public void bindDetails(OnboardingItem onboardingItem){
            fragmentSplashItemBinding.setDetails(onboardingItem);
            fragmentSplashItemBinding.executePendingBindings();

        }
    }


}
