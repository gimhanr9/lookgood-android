package com.cb008385.lookgood;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cb008385.lookgood.databinding.FragmentQuestionListBinding;

import java.util.List;

import Adapter.QuestionListAdapter;
import ViewModel.ProductDetailsViewModel;
import ViewModel.ProductViewModel;
import listeners.QuestionClickListener;
import models.Question;

public class QuestionListFragment extends Fragment implements QuestionClickListener {

    private FragmentQuestionListBinding fragmentQuestionListBinding;
    private ProductDetailsViewModel productDetailsViewModel;
    private QuestionListAdapter questionListAdapter;
    RecyclerView recyclerView;


    public QuestionListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentQuestionListBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_question_list,container,false);
        initView();
        return fragmentQuestionListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String key=QuestionListFragmentArgs.fromBundle(getArguments()).getProductId();
        getQuestions(key);
    }

    private void initView() {
        recyclerView=fragmentQuestionListBinding.questionRecyclerview;
        productDetailsViewModel=new ViewModelProvider(this).get(ProductDetailsViewModel.class);
        recyclerView.setHasFixedSize(true);
        questionListAdapter=new QuestionListAdapter(this);
        recyclerView.setAdapter(questionListAdapter);
        Drawable mDivider = ContextCompat.getDrawable(getContext(), R.drawable.horizontal_line_recyclerview);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecor.setDrawable(mDivider);
        recyclerView.addItemDecoration(itemDecor);

    }

    private void getQuestions(String productId){
        fragmentQuestionListBinding.setIsLoading(true);
        productDetailsViewModel.getQuestions(productId).observe(getViewLifecycleOwner(), new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                fragmentQuestionListBinding.setIsLoading(false);
                if(fragmentQuestionListBinding.noQuestionText.getVisibility()==View.VISIBLE){
                    fragmentQuestionListBinding.noQuestionText.setVisibility(View.GONE);
                }
                questionListAdapter.submitList(questions);
            }
        });

        productDetailsViewModel.getQuestionAvailability().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    fragmentQuestionListBinding.setIsLoading(false);
                    fragmentQuestionListBinding.noQuestionText.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onQuestionClicked(Question question) {

    }
}