package k.s.task2banaoapp.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import k.s.task2banaoapp.R;
import k.s.task2banaoapp.model.PhotosBean;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    private ArrayList<PhotosBean> photosBeanArrayList;
    private Context context;

    public CustomAdapter(ArrayList<PhotosBean> photosBeanArrayList, Context context) {
        this.photosBeanArrayList = photosBeanArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PhotosBean photosBean = photosBeanArrayList.get(position);
        Glide.with(context)
                .load(photosBean.getUrl_s())
                .placeholder(R.drawable.baseline_image_24)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photosBeanArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.img_view);
        }
    }
}
