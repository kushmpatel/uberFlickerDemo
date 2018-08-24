package com.uber.kush.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.uber.kush.R;
import com.uber.kush.backgroundtask.DownloadThread;
import com.uber.kush.model.PhotoVO;

import java.io.File;
import java.util.List;

import static com.uber.kush.IConstants.CACHE_DIRECTORY_PATH;
import static com.uber.kush.IConstants.UPDATE_ITEM;

/**
 * Adapter to render Photo List
 */
public class AdapterPhotoList extends RecyclerView.Adapter<AdapterPhotoList.HolderPhoto> {
    private List<PhotoVO> listPhotos;
    private Context mActivity;
    private int gridViewImageHeightWidth;
    //private final RequestOptions requestOptions;
    private Handler mHandler;

    public AdapterPhotoList(List<PhotoVO> listPhotos) {
        this.listPhotos = listPhotos;
        /*requestOptions = new RequestOptions();
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.override(gridViewImageHeightWidth, gridViewImageHeightWidth);*/

        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                if (msg.what == UPDATE_ITEM) {
                    notifyItemChanged(msg.arg1);
                }
            }
        };
    }

    @NonNull
    @Override
    public HolderPhoto onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mActivity = parent.getContext();
        gridViewImageHeightWidth = mActivity.getResources().getDisplayMetrics().widthPixels / 3;
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
            File cacheFilePath = new File(cacheDirPath + "/" + photoVO.getId() + ".jpg");
            if (cacheFilePath.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(cacheFilePath.getAbsolutePath());
                bitmap = getResizedBitmap(bitmap, gridViewImageHeightWidth, gridViewImageHeightWidth);
                holder.ivPhoto.setImageBitmap(bitmap);
            } else {
                /*DownloadAsyncTask mDownloadAsyncTask = new DownloadAsyncTask(photoVO,this,gridViewImageHeightWidth,position);
                mDownloadAsyncTask.execute(url);*/
                DownloadThread mDownloadThread = new DownloadThread(url, photoVO, this, gridViewImageHeightWidth, position, mHandler);
                mDownloadThread.start();
            }
        } catch (Exception ex) {
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

    /**
     * Resize Bitmap with given height and Width
     *
     * @param bm
     * @param newHeight
     * @param newWidth
     * @return
     */
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }
}
