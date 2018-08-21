package com.uber.kush.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uber.kush.R;
import com.uber.kush.model.PhotoVO;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

public class AdapterPhotoList extends RecyclerView.Adapter<AdapterPhotoList.HolderPhoto> {
    private List<PhotoVO> listPhotos = new ArrayList();
    private Activity mActivity;

    public AdapterPhotoList(Activity mActivity,List<PhotoVO> listPhotos){
        this.listPhotos = listPhotos;
        this.mActivity = mActivity;
    }

    @NonNull
    @Override
    public HolderPhoto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPhoto holder, int position) {

    }

    @Override
    public int getItemCount() {
        return listPhotos.size();
    }

    public class HolderPhoto extends RecyclerView.ViewHolder {
        ImageView ivPhoto;
        public HolderPhoto(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
        }
    }
}
