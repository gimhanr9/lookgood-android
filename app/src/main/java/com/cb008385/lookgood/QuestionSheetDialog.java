package com.cb008385.lookgood;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.cb008385.lookgood.databinding.BottomsheetDialogQuestionBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ViewModel.ProductDetailsViewModel;
import ViewModel.ProductViewModel;
import models.Question;

public class QuestionSheetDialog extends BottomSheetDialogFragment {

    private BottomsheetDialogQuestionBinding bottomsheetDialogQuestionBinding;
    private ProductDetailsViewModel productDetailsViewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        bottomsheetDialogQuestionBinding= DataBindingUtil.inflate(inflater,R.layout.bottomsheet_dialog_question,container,false);
        initView();
        return bottomsheetDialogQuestionBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String productId=QuestionSheetDialogArgs.fromBundle(getArguments()).getProductId();
        bottomsheetDialogQuestionBinding.askButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String question=bottomsheetDialogQuestionBinding.questionDescription.getText().toString().trim();
                if(question.length()>10){
                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                    String date = dateFormat.format(calendar.getTime());
                    productDetailsViewModel.submitQuestion(productId,question,date);
                    dismiss();
                }else{
                    Toast.makeText(getActivity(),"Please describe your question in at least 10 words",Toast.LENGTH_SHORT).show();
                }
            }
        });

        bottomsheetDialogQuestionBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initView(){
        productDetailsViewModel=new ViewModelProvider(getActivity()).get(ProductDetailsViewModel.class);
    }
}
