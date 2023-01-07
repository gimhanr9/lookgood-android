package com.cb008385.lookgood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cb008385.lookgood.databinding.BottomsheetDialogReviewBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import ViewModel.PurchasesViewModel;
import models.RatingItem;

public class ReviewSheetDialog extends BottomSheetDialogFragment {

    private BottomsheetDialogReviewBinding bottomsheetDialogReviewBinding;
    private PurchasesViewModel purchasesViewModel;
    private String ratingId,ratingText;
    private float ratingValue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomsheetDialogReviewBinding= DataBindingUtil.inflate(inflater,R.layout.bottomsheet_dialog_review,container,false);
        initView();
        return bottomsheetDialogReviewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String productId=ReviewSheetDialogArgs.fromBundle(getArguments()).getProductId();
        String purchasesId=ReviewSheetDialogArgs.fromBundle(getArguments()).getPurchaseKey();
        String size=ReviewSheetDialogArgs.fromBundle(getArguments()).getSize();
        boolean isRated=ReviewSheetDialogArgs.fromBundle(getArguments()).getIsRated();

        if(isRated){
            purchasesViewModel.getRating(productId,purchasesId,size).observe(getViewLifecycleOwner(), new Observer<List<RatingItem>>() {
                @Override
                public void onChanged(List<RatingItem> ratingItems) {
                    ratingId=ratingItems.get(ratingItems.size()-1).getRatingId();
                    ratingText=ratingItems.get(ratingItems.size()-1).getRatingText();
                    ratingValue=ratingItems.get(ratingItems.size()-1).getRatingValue();
                    bottomsheetDialogReviewBinding.ratingPrevious.setText(ratingItems.get(ratingItems.size()-1).getRatingText());
                    bottomsheetDialogReviewBinding.ratingPrevious.setVisibility(View.VISIBLE);
                    bottomsheetDialogReviewBinding.reviewDescription.setText(bottomsheetDialogReviewBinding.ratingPrevious.getText());
                    bottomsheetDialogReviewBinding.giveRating.setRating(ratingValue);
                    bottomsheetDialogReviewBinding.rateButton.setText("Update");
                }
            });


        }
        bottomsheetDialogReviewBinding.rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String review=bottomsheetDialogReviewBinding.reviewDescription.getText().toString().trim();
                if(review.length()>10){
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String date = dateFormat.format(calendar.getTime());
                    float rating=bottomsheetDialogReviewBinding.giveRating.getRating();
                    if(isRated){
                        String ratingField=bottomsheetDialogReviewBinding.reviewDescription.getText().toString();
                        float ratingAmount=bottomsheetDialogReviewBinding.giveRating.getRating();
                        purchasesViewModel.updateReview(productId,ratingId,ratingField,ratingAmount);
                    }else {
                        purchasesViewModel.submitReview(purchasesId, productId, review, size, date, rating);
                    }
                    dismiss();
                }else{
                    Toast.makeText(getActivity(),"Please describe your feedback in at least 10 words",Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomsheetDialogReviewBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initView(){
        purchasesViewModel=new ViewModelProvider(getActivity()).get(PurchasesViewModel.class);//check
    }
}
