package com.cb008385.lookgood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cb008385.lookgood.databinding.FragmentProductDetailsBinding;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import Adapter.ImageSliderAdapter;
import ViewModel.ProductDetailsViewModel;
import models.CartItem;
import models.Favourites;
import models.ProductItem;


public class ProductDetailsFragment extends Fragment {

    private FragmentProductDetailsBinding fragmentProductDetailsBinding;
    private ProductDetailsViewModel productDetailsViewModel;
    private String id,productSize;
    private boolean isWatched=false;
    private boolean isLoggedIn;

    List<String> imageList;
    ArrayList<String> sizeList;
    ArrayAdapter<String> sizeAdapter;
    ArrayList<Integer> quantityList;
    ArrayList<String> arrayQuantity;
    ArrayAdapter<String> quantityAdapter;

/*Note: In this fragment, all views are initially set to gone(not visible), to avoid showing empty views. Once data is loaded and set
 to  views, each view is then set to visible*/
    public ProductDetailsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        fragmentProductDetailsBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_product_details,container,false);
        initView();
        return fragmentProductDetailsBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id=ProductDetailsFragmentArgs.fromBundle(getArguments()).getProductId();
        getProductImages(id);

        //get values which were passed as safe args into variables and set them to appropriate views in layout
        String productImage=ProductDetailsFragmentArgs.fromBundle(getArguments()).getImageUrl();
        fragmentProductDetailsBinding.setProductImageUrl(productImage);
        String productName=ProductDetailsFragmentArgs.fromBundle(getArguments()).getProductName();
        fragmentProductDetailsBinding.setName(productName);
        String productTitle=ProductDetailsFragmentArgs.fromBundle(getArguments()).getProductTitle();
        fragmentProductDetailsBinding.setTitle(productTitle);
        fragmentProductDetailsBinding.setBrand(ProductDetailsFragmentArgs.fromBundle(getArguments()).getBrand());
        fragmentProductDetailsBinding.setDescription(ProductDetailsFragmentArgs.fromBundle(getArguments()).getDescription());
        int small=ProductDetailsFragmentArgs.fromBundle(getArguments()).getSmall();
        int medium=ProductDetailsFragmentArgs.fromBundle(getArguments()).getMedium();
        int large=ProductDetailsFragmentArgs.fromBundle(getArguments()).getLarge();
        int xlarge=ProductDetailsFragmentArgs.fromBundle(getArguments()).getXlarge();
        int xxlarge=ProductDetailsFragmentArgs.fromBundle(getArguments()).getXxlarge();
        double price=(double) ProductDetailsFragmentArgs.fromBundle(getArguments()).getPrice();
        getLoggedInStatus();
        // once data is set, make each view visible.
        fragmentProductDetailsBinding.mainImage.setVisibility(View.VISIBLE);
        fragmentProductDetailsBinding.productName.setVisibility(View.VISIBLE);
        fragmentProductDetailsBinding.productBrand.setVisibility(View.VISIBLE);
        fragmentProductDetailsBinding.productPrice.setVisibility(View.VISIBLE);
        fragmentProductDetailsBinding.productDescription.setVisibility(View.VISIBLE);
        fragmentProductDetailsBinding.availabilityText.setVisibility(View.VISIBLE);
        sizeList =new ArrayList<>();
        quantityList=new ArrayList<>();
        sizeAdapter =new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sizeList);
        fragmentProductDetailsBinding.sizeType.setAdapter(sizeAdapter);
       //get the size variations and quantity each size of the product has. Used when displaying available sizes
        if(small>0){
            sizeList.add("small");
            quantityList.add(small);
        }
        if(medium>0){
            sizeList.add("medium");
            quantityList.add(medium);
        }
        if(large>0){
            sizeList.add("large");
            quantityList.add(large);
        }
        if(xlarge>0){
            sizeList.add("xlarge");
            quantityList.add(xlarge);
        }
        if(xxlarge>0){
            sizeList.add("xxl");
            quantityList.add(xxlarge);
        }
        sizeAdapter.notifyDataSetChanged();// notify dataset change to spinner
        fragmentProductDetailsBinding.sizeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getId()==R.id.size_type) {
                    String size = parent.getItemAtPosition(position).toString();
                    productSize = size;
                    String availability = quantityList.get(position) + " items available";// show appropriate quantity available for each size selected in spinner
                    setUpQuantity(quantityList.get(position));
                    fragmentProductDetailsBinding.availabilityText.setText(availability);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fragmentProductDetailsBinding.sizeType.setVisibility(View.VISIBLE);
        fragmentProductDetailsBinding.readMore.setVisibility(View.VISIBLE);
        checkIsFavourite(id);
        getTotalRatings(id);
        fragmentProductDetailsBinding.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentProductDetailsBinding.readMore.getText().toString().equals("Read More")){
                    fragmentProductDetailsBinding.productDescription.setMaxLines(Integer.MAX_VALUE);
                    fragmentProductDetailsBinding.productDescription.setEllipsize(null);
                    fragmentProductDetailsBinding.readMore.setText("Read Less");

                }else{
                    fragmentProductDetailsBinding.productDescription.setMaxLines(2);
                    fragmentProductDetailsBinding.productDescription.setEllipsize(TextUtils.TruncateAt.END);
                    fragmentProductDetailsBinding.readMore.setText("Read More");
                }
            }
        });

        fragmentProductDetailsBinding.askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to dialog to allow user to ask question
                NavDirections action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToQuestionSheetDialog(id);
                Navigation.findNavController(getView()).navigate(action);
            }
        });

        fragmentProductDetailsBinding.askQuestion.setVisibility(View.VISIBLE);

        fragmentProductDetailsBinding.questionsAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //navigate to list of questions on particular product
                NavDirections action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToQuestionListFragment(id);
                Navigation.findNavController(getView()).navigate(action);
            }
        });
        fragmentProductDetailsBinding.questionsAnswers.setVisibility(View.VISIBLE);

        fragmentProductDetailsBinding.btnAddCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggedIn){   //check if user is logged in
                    //if logged in, add product to cart
                    int quantity=Integer.parseInt(fragmentProductDetailsBinding.quantity.getSelectedItem().toString());
                    CartItem cartItem=new CartItem(id,productImage,productName,productSize,quantity,price);
                    productDetailsViewModel.addToCart(cartItem);
                }else{
                    //navigate to login fragment if not logged in
                    NavDirections action=ProductDetailsFragmentDirections.actionProductDetailsFragmentToUserFragment();
                    Navigation.findNavController(view).navigate(action);

                }

            }
        });
        fragmentProductDetailsBinding.btnAddCart.setVisibility(View.VISIBLE);

        fragmentProductDetailsBinding.favouriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggedIn) {
                    if (isWatched) {
                        //check if item is already available in favourites and add/delete the item from favourites accordingly and show icon changes
                        productDetailsViewModel.deleteFavouriteItem(id);//delete item from favourites because it is already in favourites
                        isWatched = false;
                        fragmentProductDetailsBinding.favouriteIcon.setImageResource(R.drawable.add_to_favourite);
                        Toast.makeText(getActivity().getApplicationContext(), "Removed from favourites!", Toast.LENGTH_SHORT).show();

                    } else {
                        Favourites favourites = new Favourites(productImage, productName, productTitle, price);// create an object with necessary data to be inserted to database
                        productDetailsViewModel.addToFavourites(favourites, id);// add item to favourites since it is not available in favourites
                        fragmentProductDetailsBinding.favouriteIcon.setImageResource(R.drawable.add_to_favourite_filled);// change icon
                        Toast.makeText(getActivity().getApplicationContext(), "Added to favourites!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"You have to login first!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        fragmentProductDetailsBinding.favouriteIcon.setVisibility(View.VISIBLE);


        fragmentProductDetailsBinding.btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isLoggedIn){
                    int quantity=Integer.parseInt(fragmentProductDetailsBinding.quantity.getSelectedItem().toString());
                    float FPrice=(float) price;
                    NavDirections action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToRegisteredUserSinglePurchaseFragment(id,productImage,productName,productSize,
                            quantity,FPrice);
                    Navigation.findNavController(v).navigate(action);

                }else{
                    int quantity=Integer.parseInt(fragmentProductDetailsBinding.quantity.getSelectedItem().toString());
                    float FPrice=(float) price;
                    NavDirections action = ProductDetailsFragmentDirections.actionProductDetailsFragmentToGuestCheckoutFragment(id,productImage,productName,productSize,
                            quantity,FPrice);
                    Navigation.findNavController(v).navigate(action);

                }

            }
        });
        fragmentProductDetailsBinding.btnBuyNow.setVisibility(View.VISIBLE);

        fragmentProductDetailsBinding.readRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // navigate to read ratings for product
                NavDirections action=ProductDetailsFragmentDirections.actionProductDetailsFragmentToRatingListFragment(id);
                Navigation.findNavController(view).navigate(action);

            }
        });
        fragmentProductDetailsBinding.readRatings.setVisibility(View.VISIBLE);
    }



    private void initView() {
        //initialize viewmodel
        productDetailsViewModel= new ViewModelProvider(this).get(ProductDetailsViewModel.class);

    }

    private void checkIsFavourite(String productId){
        //check if product is in favourites
        productDetailsViewModel.getFavouriteAvailability(productId).observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    isWatched=true;
                    fragmentProductDetailsBinding.favouriteIcon.setImageResource(R.drawable.add_to_favourite_filled);
                }
            }
        });
    }


    private void getTotalRatings(String productId){
        productDetailsViewModel.getTotalRating(productId).observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                if(aFloat!=null){
                    fragmentProductDetailsBinding.ratings.setRating(aFloat);
                    fragmentProductDetailsBinding.ratings.setVisibility(View.VISIBLE);
                    DecimalFormat df = new DecimalFormat("#.#");
                    df.setRoundingMode(RoundingMode.CEILING);
                    fragmentProductDetailsBinding.setTextRating(String.valueOf(df.format(aFloat)));
                    fragmentProductDetailsBinding.ratingText.setVisibility(View.VISIBLE);
                }
            }
        });
        productDetailsViewModel.getNoOfRatings().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String numberText;
                if(integer==1){
                    numberText=integer.toString()+" rating";
                }else{
                    numberText=integer.toString()+" ratings";
                }
                fragmentProductDetailsBinding.setNoOfRatings(numberText);
                if(integer>0) {
                    fragmentProductDetailsBinding.noOfRatings.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void getProductImages(String id) {
        //get product images from database
        fragmentProductDetailsBinding.setIsLoading(true);
        productDetailsViewModel.getProductImages(id).observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                fragmentProductDetailsBinding.setIsLoading(false);
                imageList=list;
                String [] array=imageList.toArray(new String[0]);
                loadImageSlider(array);
            }
        });

    }

    private void loadImageSlider(String[] sliderImages){
        fragmentProductDetailsBinding.imageSlider.setOffscreenPageLimit(1);
        fragmentProductDetailsBinding.imageSlider.setAdapter(new ImageSliderAdapter(sliderImages));
        fragmentProductDetailsBinding.imageSlider.setVisibility(View.VISIBLE);
        fragmentProductDetailsBinding.fadingEdges.setVisibility(View.VISIBLE);
        setUpSliderIndicators(sliderImages.length);
        fragmentProductDetailsBinding.imageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });

    }

    private void setUpSliderIndicators(int count){
        // setup image slider indicators
        fragmentProductDetailsBinding.imageSliderIndicator.removeAllViews();
        ImageView [] indicators=new ImageView[count];
        LinearLayout.LayoutParams layoutParams=new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);
        for(int i=0;i<indicators.length;i++){
            indicators[i]=new ImageView(getActivity().getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getActivity().getApplicationContext(),R.drawable.image_slider_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            fragmentProductDetailsBinding.imageSliderIndicator.addView(indicators[i]);
        }
        fragmentProductDetailsBinding.imageSliderIndicator.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);

    }

    private void setCurrentSliderIndicator(int position){
        int childCount=fragmentProductDetailsBinding.imageSliderIndicator.getChildCount();
        for(int i=0;i<childCount;i++){
            ImageView imageView=(ImageView) fragmentProductDetailsBinding.imageSliderIndicator.getChildAt(i);
            if(i==position){
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.image_slider_indicator_active)
                );
            }else{
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getActivity().getApplicationContext(),R.drawable.image_slider_indicator_inactive)
                );
            }
        }

    }


    public void setUpQuantity(int i){
        arrayQuantity=new ArrayList<>();
        for(int x=1;x<=i;x++){
            arrayQuantity.add(String.valueOf(x));
        }
        quantityAdapter =new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayQuantity);
        fragmentProductDetailsBinding.quantity.setAdapter(quantityAdapter);
        fragmentProductDetailsBinding.quantityText.setVisibility(View.VISIBLE);
        fragmentProductDetailsBinding.quantity.setVisibility(View.VISIBLE);
    }

    public void getLoggedInStatus(){
        //check if user is logged in
        productDetailsViewModel.getLoggedOutLiveData().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    isLoggedIn=true;
                }
            }
        });

    }
}