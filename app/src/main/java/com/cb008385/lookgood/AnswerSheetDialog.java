package com.cb008385.lookgood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cb008385.lookgood.databinding.FragmentAnswerSheetDialogBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import ViewModel.AdminViewModel;
import ViewModel.ProductDetailsViewModel;


public class AnswerSheetDialog extends BottomSheetDialogFragment {

    private FragmentAnswerSheetDialogBinding fragmentAnswerSheetDialogBinding;
    private AdminViewModel adminViewModel;

    public AnswerSheetDialog() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAnswerSheetDialogBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_answer_sheet_dialog,container,false);
        initView();
        return fragmentAnswerSheetDialogBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String questionId=AnswerSheetDialogArgs.fromBundle(getArguments()).getQuestionId();
        String productId=AnswerSheetDialogArgs.fromBundle(getArguments()).getProductId();
        String name=AnswerSheetDialogArgs.fromBundle(getArguments()).getCustomerName();
        String question=AnswerSheetDialogArgs.fromBundle(getArguments()).getQuestion();
        fragmentAnswerSheetDialogBinding.setName(name);
        fragmentAnswerSheetDialogBinding.setQuestion(question);
        fragmentAnswerSheetDialogBinding.submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer=fragmentAnswerSheetDialogBinding.answerDescription.getText().toString().trim();
                if(answer.length()>10){

                    adminViewModel.submitAnswer(productId,questionId,answer);
                    dismiss();
                }else{
                    Toast.makeText(getActivity(),"Please describe your answer in at least 10 words",Toast.LENGTH_SHORT).show();
                }
            }
        });

        fragmentAnswerSheetDialogBinding.cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private void initView() {
        adminViewModel=new ViewModelProvider(getActivity()).get(AdminViewModel.class);
    }
}