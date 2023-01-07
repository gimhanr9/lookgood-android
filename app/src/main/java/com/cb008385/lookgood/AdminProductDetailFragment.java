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

import com.cb008385.lookgood.databinding.FragmentAdminProductDetailBinding;
import com.cb008385.lookgood.databinding.FragmentProductDetailsBinding;

import java.util.ArrayList;
import java.util.List;

import Adapter.ImageSliderAdapter;
import ViewModel.AdminViewModel;
import ViewModel.ProductDetailsViewModel;

public class AdminProductDetailFragment extends Fragment {

    private FragmentAdminProductDetailBinding fragmentAdminProductDetailBinding;
    private AdminViewModel adminViewModel;
    private String id,productSize;
    private boolean isWatched=false;
    private boolean isLoggedIn;

    List<String> imageList;
    ArrayList<String> sizeList;
    ArrayAdapter<String> sizeAdapter;
    ArrayList<Integer> quantityList;
    ArrayList<String> arrayQuantity;
    ArrayAdapter<String> quantityAdapter;


    public AdminProductDetailFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAdminProductDetailBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_admin_product_detail,container,false);
        initView();
        return fragmentAdminProductDetailBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id=AdminProductDetailFragmentArgs.fromBundle(getArguments()).getProductId();
        getProductImages(id);

        String productImage=AdminProductDetailFragmentArgs.fromBundle(getArguments()).getImageUrl();
        fragmentAdminProductDetailBinding.setProductImageUrl(productImage);
        String productName=AdminProductDetailFragmentArgs.fromBundle(getArguments()).getName();
        fragmentAdminProductDetailBinding.setName(productName);
        String productTitle=AdminProductDetailFragmentArgs.fromBundle(getArguments()).getTitle();
        fragmentAdminProductDetailBinding.setTitle(productTitle);
        fragmentAdminProductDetailBinding.setBrand(AdminProductDetailFragmentArgs.fromBundle(getArguments()).getBrand());
        fragmentAdminProductDetailBinding.setDescription(AdminProductDetailFragmentArgs.fromBundle(getArguments()).getDescription());
        int small=AdminProductDetailFragmentArgs.fromBundle(getArguments()).getSmall();
        int medium=AdminProductDetailFragmentArgs.fromBundle(getArguments()).getMedium();
        int large=AdminProductDetailFragmentArgs.fromBundle(getArguments()).getLarge();
        int xlarge=AdminProductDetailFragmentArgs.fromBundle(getArguments()).getXlarge();
        int xxlarge=AdminProductDetailFragmentArgs.fromBundle(getArguments()).getXxl();
        double price=(double) AdminProductDetailFragmentArgs.fromBundle(getArguments()).getPrice();

        fragmentAdminProductDetailBinding.mainImage.setVisibility(View.VISIBLE);
        fragmentAdminProductDetailBinding.productName.setVisibility(View.VISIBLE);
        fragmentAdminProductDetailBinding.productBrand.setVisibility(View.VISIBLE);
        fragmentAdminProductDetailBinding.productPrice.setVisibility(View.VISIBLE);
        fragmentAdminProductDetailBinding.productDescription.setVisibility(View.VISIBLE);
        fragmentAdminProductDetailBinding.availabilityText.setVisibility(View.VISIBLE);
        sizeList =new ArrayList<>();
        quantityList=new ArrayList<>();
        sizeAdapter =new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, sizeList);
        fragmentAdminProductDetailBinding.sizeType.setAdapter(sizeAdapter);

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
        sizeAdapter.notifyDataSetChanged();
        fragmentAdminProductDetailBinding.sizeType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(parent.getId()==R.id.size_type) {
                    String size = parent.getItemAtPosition(position).toString();
                    productSize = size;
                    String availability = quantityList.get(position) + " items available";
                    setUpQuantity(quantityList.get(position));
                    fragmentAdminProductDetailBinding.availabilityText.setText(availability);

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        fragmentAdminProductDetailBinding.sizeType.setVisibility(View.VISIBLE);
        fragmentAdminProductDetailBinding.readMore.setVisibility(View.VISIBLE);

        getTotalRatings(id);
        fragmentAdminProductDetailBinding.readRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=AdminProductDetailFragmentDirections.actionAdminProductDetailFragmentToAdminRatingListFragment(id);
                Navigation.findNavController(view).navigate(action);
            }
        });

        fragmentAdminProductDetailBinding.readRatings.setVisibility(View.VISIBLE);

        fragmentAdminProductDetailBinding.questionsAnswers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = AdminProductDetailFragmentDirections.actionAdminProductDetailFragmentToAdminQuestionListFragment(id);
                Navigation.findNavController(view).navigate(action);
            }
        });
        fragmentAdminProductDetailBinding.questionsAnswers.setVisibility(View.VISIBLE);

        fragmentAdminProductDetailBinding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action = AdminProductDetailFragmentDirections.actionAdminProductDetailFragmentToAdminUpdateFragment(id);
                Navigation.findNavController(view).navigate(action);

            }
        });
        fragmentAdminProductDetailBinding.btnUpdate.setVisibility(View.VISIBLE);
        fragmentAdminProductDetailBinding.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavDirections action=AdminProductDetailFragmentDirections.actionAdminProductDetailFragmentToAlertDialog(id);
                Navigation.findNavController(view).navigate(action);

            }
        });
        fragmentAdminProductDetailBinding.btnDelete.setVisibility(View.VISIBLE);
        fragmentAdminProductDetailBinding.readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragmentAdminProductDetailBinding.readMore.getText().toString().equals("Read More")){
                    fragmentAdminProductDetailBinding.productDescription.setMaxLines(Integer.MAX_VALUE);
                    fragmentAdminProductDetailBinding.productDescription.setEllipsize(null);
                    fragmentAdminProductDetailBinding.readMore.setText("Read Less");

                }else{
                    fragmentAdminProductDetailBinding.productDescription.setMaxLines(2);
                    fragmentAdminProductDetailBinding.productDescription.setEllipsize(TextUtils.TruncateAt.END);
                    fragmentAdminProductDetailBinding.readMore.setText("Read More");
                }
            }
        });

    }

    private void initView() {
        adminViewModel= new ViewModelProvider(this).get(AdminViewModel.class);

    }



    private void getTotalRatings(String productId){
        adminViewModel.getTotalRating(productId).observe(getViewLifecycleOwner(), new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                if(aFloat!=null){
                    fragmentAdminProductDetailBinding.ratings.setRating(aFloat);
                    fragmentAdminProductDetailBinding.ratings.setVisibility(View.VISIBLE);
                    fragmentAdminProductDetailBinding.setTextRating(String.valueOf(aFloat));
                    fragmentAdminProductDetailBinding.ratingText.setVisibility(View.VISIBLE);
                }
            }
        });
        adminViewModel.getNoOfRatings().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                String numberText;
                if(integer==1){
                    numberText=integer.toString()+" rating";
                }else{
                    numberText=integer.toString()+" ratings";
                }
                fragmentAdminProductDetailBinding.setNoOfRatings(numberText);
                if(integer>0) {
                    fragmentAdminProductDetailBinding.noOfRatings.setVisibility(View.VISIBLE);
                }
            }
        });
    }


    private void getProductImages(String id) {
        fragmentAdminProductDetailBinding.setIsLoading(true);
        adminViewModel.getProductImages(id).observe(getViewLifecycleOwner(), new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> list) {
                fragmentAdminProductDetailBinding.setIsLoading(false);
                imageList=list;
                String [] array=imageList.toArray(new String[0]);
                loadImageSlider(array);
            }
        });

    }

    private void loadImageSlider(String[] sliderImages){
        fragmentAdminProductDetailBinding.imageSlider.setOffscreenPageLimit(1);
        fragmentAdminProductDetailBinding.imageSlider.setAdapter(new ImageSliderAdapter(sliderImages));
        fragmentAdminProductDetailBinding.imageSlider.setVisibility(View.VISIBLE);
        fragmentAdminProductDetailBinding.fadingEdges.setVisibility(View.VISIBLE);
        setUpSliderIndicators(sliderImages.length);
        fragmentAdminProductDetailBinding.imageSlider.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicator(position);
            }
        });

    }

    private void setUpSliderIndicators(int count){
        fragmentAdminProductDetailBinding.imageSliderIndicator.removeAllViews();
        ImageView[] indicators=new ImageView[count];
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
            fragmentAdminProductDetailBinding.imageSliderIndicator.addView(indicators[i]);
        }
        fragmentAdminProductDetailBinding.imageSliderIndicator.setVisibility(View.VISIBLE);
        setCurrentSliderIndicator(0);

    }

    private void setCurrentSliderIndicator(int position){
        int childCount=fragmentAdminProductDetailBinding.imageSliderIndicator.getChildCount();
        for(int i=0;i<childCount;i++){
            ImageView imageView=(ImageView) fragmentAdminProductDetailBinding.imageSliderIndicator.getChildAt(i);
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
        fragmentAdminProductDetailBinding.quantity.setAdapter(quantityAdapter);
        fragmentAdminProductDetailBinding.quantityText.setVisibility(View.VISIBLE);
        fragmentAdminProductDetailBinding.quantity.setVisibility(View.VISIBLE);
    }
}