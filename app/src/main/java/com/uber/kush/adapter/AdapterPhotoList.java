package com.uber.kush.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uber.kush.R;
import com.uber.kush.backgroundtask.DownloadAsyncTask;
import com.uber.kush.model.PhotoVO;

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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_row_photo, parent, false);
        return new HolderPhoto(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderPhoto holder, int position) {
        try {
            PhotoVO photoVO = listPhotos.get(position);
            String url = String.format(mActivity.getString(R.string.thumb_url),
                    photoVO.getFarm(),
                    photoVO.getServer(),
                    photoVO.getId(),
                    photoVO.getSecret());

            DownloadAsyncTask mDownloadAsyncTask = new DownloadAsyncTask(holder);
            mDownloadAsyncTask.execute(url);
            //UberLog.d(AdapterPhotoList.class.getSimpleName(),"position->"+position+" Name->"+listPhotos.get(position).getTitle());
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return listPhotos.size();
    }

    public class HolderPhoto extends RecyclerView.ViewHolder {
       public ImageView ivPhoto;
        public HolderPhoto(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.ivPhoto);
        }
    }
}
