package Repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.cb008385.lookgood.R;

import java.util.ArrayList;
import java.util.List;

import models.OnboardingItem;

public class SplashDetailsRepository {

    public MutableLiveData<List<OnboardingItem>> splashDetails=new MutableLiveData<>();

    public LiveData<List<OnboardingItem>> getSplashDetails() {

        List<OnboardingItem> items = new ArrayList<>();
        items.add(new OnboardingItem(R.drawable.ratings, "Highest Satisfaction","Our products and services has the highest customer satisfaction."));
        items.add(new OnboardingItem(R.drawable.price_tag, "Unbelievable Offers","Quality and the best price at one place."));
        items.add(new OnboardingItem(R.drawable.express_delivery, "Fast Island-wide Delivery","Receive the product to your doorstep within 2 days."));

        splashDetails.setValue(items);

        return splashDetails;
    }

}
