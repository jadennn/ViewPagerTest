package com.example.viewpagertest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

/**
 * date: 2018/9/18
 * description: PhotoRecycleAdapter
 **/
public class PhotoRecycleAdapter extends RecyclerView.Adapter<PhotoRecycleAdapter.ViewHolder> implements View.OnClickListener{
    private Context mContext;
    private List<String> mData;
    private OnItemClickListener mItemClickListener;

    public PhotoRecycleAdapter(Context context, List<String> urlList){
        mContext = context;
        mData = urlList;
    }

    @NonNull
    @Override
    public PhotoRecycleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.photo_items, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoRecycleAdapter.ViewHolder holder, int position) {
        String url = mData.get(position);
        RequestOptions ro = new RequestOptions().dontAnimate().dontTransform();
        Glide.with(mContext)
                .load(url)
                .apply(ro)
                .into(holder.mImageView);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public void onClick(View v) {
        if(mItemClickListener != null){
            mItemClickListener.onPhotoItemClick((Integer) v.getTag());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.photo_image_view);
        }
    }

    public interface OnItemClickListener{
        void onPhotoItemClick(int position);
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }
}
