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
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cb008385.lookgood.databinding.FragmentAdminQuestionListBinding;
import com.cb008385.lookgood.databinding.FragmentQuestionListBinding;

import java.util.List;

import Adapter.QuestionListAdapter;
import ViewModel.AdminViewModel;
import ViewModel.ProductDetailsViewModel;
import listeners.QuestionClickListener;
import models.Question;

public class AdminQuestionListFragment extends Fragment implements QuestionClickListener {

    private FragmentAdminQuestionListBinding fragmentAdminQuestionListBinding;
    private AdminViewModel adminViewModel;
    private QuestionListAdapter questionListAdapter;
    RecyclerView recyclerView;

    public AdminQuestionListFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentAdminQuestionListBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_admin_question_list,container,false);
        initView();
        return fragmentAdminQuestionListBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String key=AdminQuestionListFragmentArgs.fromBundle(getArguments()).getProductId();
        getQuestions(key);


    }

    private void initView() {
        recyclerView=fragmentAdminQuestionListBinding.adminquestionRecyclerview;
        adminViewModel=new ViewModelProvider(this).get(AdminViewModel.class);
        recyclerView.setHasFixedSize(true);
        questionListAdapter=new QuestionListAdapter(this);
        recyclerView.setAdapter(questionListAdapter);
        Drawable mDivider = ContextCompat.getDrawable(getContext(), R.drawable.horizontal_line_recyclerview);
        DividerItemDecoration itemDecor = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        itemDecor.setDrawable(mDivider);
        recyclerView.addItemDecoration(itemDecor);
    }

    private void getQuestions(String productId){
        fragmentAdminQuestionListBinding.setIsLoading(true);
        adminViewModel.getQuestions(productId).observe(getViewLifecycleOwner(), new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {

                fragmentAdminQuestionListBinding.setIsLoading(false);
                if(fragmentAdminQuestionListBinding.noQuestionText.getVisibility()==View.GONE){
                    fragmentAdminQuestionListBinding.noQuestionText.setVisibility(View.INVISIBLE);
                }
                questionListAdapter.submitList(questions);
            }
        });

        adminViewModel.getQuestionAvailability().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(!aBoolean){
                    fragmentAdminQuestionListBinding.setIsLoading(false);
                    fragmentAdminQuestionListBinding.noQuestionText.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    public void onQuestionClicked(Question question) {
        NavDirections action=AdminQuestionListFragmentDirections.actionAdminQuestionListFragmentToAnswerSheetDialog(question.getQuestionId(),
                question.getProductId(),question.getName(),question.getQuestion());
        Navigation.findNavController(getView()).navigate(action);


    }
}