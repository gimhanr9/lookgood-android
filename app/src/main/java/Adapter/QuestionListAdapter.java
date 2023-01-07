package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008385.lookgood.R;
import com.cb008385.lookgood.databinding.QuestionListItemBinding;

import listeners.QuestionClickListener;
import models.Question;

public class QuestionListAdapter extends ListAdapter<Question,QuestionListAdapter.QuestionListViewHolder> {

    private LayoutInflater layoutInflater;
    private QuestionClickListener questionClickListener;

    public QuestionListAdapter(QuestionClickListener questionClickListener) {
        super(DIFF_CALLBACK);
        this.questionClickListener=questionClickListener;
    }

    private static final DiffUtil.ItemCallback<Question> DIFF_CALLBACK=new DiffUtil.ItemCallback<Question>() {
        @Override
        public boolean areItemsTheSame(@NonNull Question oldItem, @NonNull Question newItem) {
            return oldItem.getQuestionId().equals(newItem.getQuestionId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Question oldItem, @NonNull Question newItem) {
            return oldItem.getProductId().equals(newItem.getProductId())
                    && oldItem.getName().equals(newItem.getName())  &&
                    oldItem.getQuestion().equals(newItem.getQuestion())  && oldItem.getAnswer().equals(newItem.getAnswer())  &&
                    oldItem.getDate().equals(newItem.getDate());
        }
    };


    @NonNull
    @Override
    public QuestionListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(layoutInflater==null){
            layoutInflater= LayoutInflater.from(parent.getContext());
        }
        QuestionListItemBinding questionListItemBinding= DataBindingUtil.inflate(
                layoutInflater, R.layout.question_list_item,parent,false
        );
        return new QuestionListAdapter.QuestionListViewHolder(questionListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionListViewHolder holder, int position) {
        holder.bindQuestions(getItem(position));

    }

    class QuestionListViewHolder extends RecyclerView.ViewHolder{
        private QuestionListItemBinding questionListItemBinding;

        public QuestionListViewHolder(QuestionListItemBinding questionListItemBinding) {
            super(questionListItemBinding.getRoot());
            this.questionListItemBinding=questionListItemBinding;
        }

        public void bindQuestions(Question question){
            questionListItemBinding.setQuestionList(question);
            questionListItemBinding.executePendingBindings();
            questionListItemBinding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    questionClickListener.onQuestionClicked(question);
                }
            });
        }
    }
}
