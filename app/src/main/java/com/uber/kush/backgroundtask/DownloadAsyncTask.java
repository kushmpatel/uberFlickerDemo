package com.uber.kush.backgroundtask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;

import com.uber.kush.adapter.AdapterPhotoList;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadAsyncTask extends AsyncTask<String,Integer,Bitmap> {
    private AdapterPhotoList.HolderPhoto viewHolder;

    public DownloadAsyncTask(AdapterPhotoList.HolderPhoto viewHolder){
        this.viewHolder = viewHolder;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = getBitmapFromURL(strings[0]);
        //bitmap = getResizedBitmap(bitmap,50,50);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap mBitmap) {
        super.onPostExecute(mBitmap);
        if(mBitmap != null)
            viewHolder.ivPhoto.setImageBitmap(mBitmap);
    }

    public Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }
}
