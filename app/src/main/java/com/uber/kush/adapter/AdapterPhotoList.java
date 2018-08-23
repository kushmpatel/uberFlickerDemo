package com.uber.kush.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.uber.kush.R;
import com.uber.kush.backgroundtask.DownloadAsyncTask;
import com.uber.kush.helper.UberLog;
import com.uber.kush.model.PhotoVO;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.uber.kush.IConstants.CACHE_DIRECTORY_PATH;

public class AdapterPhotoList extends RecyclerView.Adapter<AdapterPhotoList.HolderPhoto> {
    private List<PhotoVO> listPhotos = new ArrayList();
    private Activity mActivity;
    private int gridViewImageHeightWidth;
    private final RequestOptions requestOptions;

    public AdapterPhotoList(Activity mActivity,List<PhotoVO> listPhotos){
        this.listPhotos = listPhotos;
        this.mActivity = mActivity;
        gridViewImageHeightWidth = mActivity.getResources().getDisplayMetrics().widthPixels * 1 / 3;
        requestOptions = new RequestOptions();
//        requestOptions.placeholder(R.drawable.default_file);
//        requestOptions.error(R.drawable.default_file);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.override(gridViewImageHeightWidth, gridViewImageHeightWidth);
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
            @SuppressLint("StringFormatMatches") String url = String.format(mActivity.getString(R.string.thumb_url),
                    photoVO.getFarm(),
                    photoVO.getServer(),
                    photoVO.getId(),
                    photoVO.getSecret());


            //Glide.with(mActivity).load(url).apply(requestOptions).into(holder.ivPhoto);

            String cacheDirPath = CACHE_DIRECTORY_PATH;
            File cacheFilePath = new File(cacheDirPath+"/"+photoVO.getId()+".jpg");
            if(cacheFilePath.exists()){
                UberLog.d(DownloadAsyncTask.class.getSimpleName(),photoVO.getId()+" is already exist");
                Bitmap bitmap = BitmapFactory.decodeFile(cacheFilePath.getAbsolutePath());
                holder.ivPhoto.setImageBitmap(bitmap);
            } else{
                DownloadAsyncTask mDownloadAsyncTask = new DownloadAsyncTask(photoVO,this,gridViewImageHeightWidth,position);
                mDownloadAsyncTask.execute(url);
            }
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
