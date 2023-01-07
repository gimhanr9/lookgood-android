package Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cb008385.lookgood.R;

import java.util.List;

public class UploadImageAdapter extends RecyclerView.Adapter<UploadImageAdapter.UploadImageViewHolder> {
    public List<String>fileNameList;


    public UploadImageAdapter(List<String> fileNameList) {
        this.fileNameList = fileNameList;

    }

    @NonNull
    @Override
    public UploadImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.image_list_item,parent,false);
        return new UploadImageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull UploadImageViewHolder holder, int position) {
        String fileName=fileNameList.get(position);
        holder.fileNameView.setText(fileName);

    }

    @Override
    public int getItemCount() {
        return fileNameList.size();
    }

    class UploadImageViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TextView fileNameView;

        public UploadImageViewHolder(@NonNull View itemView) {
            super(itemView);
            mView=itemView;

            fileNameView=(TextView) mView.findViewById(R.id.upload_filename);

        }
    }
}
