package com.uber.kush.backgroundtask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;

import com.uber.kush.adapter.AdapterPhotoList;
import com.uber.kush.model.PhotoVO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.uber.kush.IConstants.CACHE_DIRECTORY_PATH;

public class DownloadAsyncTask extends AsyncTask<String,Integer,Bitmap> {
    private AdapterPhotoList adapter;
    private int gridViewImageHeightWidth;
    private PhotoVO photoVO;
    private int position;

    public DownloadAsyncTask(PhotoVO photoVO, AdapterPhotoList viewHolder, int gridViewImageHeightWidth, int position){
        this.photoVO = photoVO;
        this.adapter = viewHolder;
        this.gridViewImageHeightWidth = gridViewImageHeightWidth;
        this.position = position;
    }
    @Override
    protected Bitmap doInBackground(String... strings) {
        Bitmap bitmap = getBitmapFromURL(strings[0]);
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap mBitmap) {
        super.onPostExecute(mBitmap);
        /*if(mBitmap != null) {
            photoVO.setBitmap(mBitmap);
            adapter.notifyDataSetChanged();
        }*/
        adapter.notifyItemChanged(position);
    }

    public Bitmap getBitmapFromURL(String src) {
        InputStream input = null;
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.connect();
            input = connection.getInputStream();

            String cacheDirPath = CACHE_DIRECTORY_PATH;
            if(!new File(cacheDirPath).isDirectory()){
                new File(cacheDirPath).mkdirs();
            }
            File cacheFilePath = new File(cacheDirPath+"/"+photoVO.getId()+".jpg");
            copyInputStreamToFile(input,cacheFilePath);

            Bitmap myBitmap = BitmapFactory.decodeFile(cacheFilePath.getAbsolutePath());

            myBitmap = getResizedBitmap(myBitmap,gridViewImageHeightWidth,gridViewImageHeightWidth);

            input.close();

            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if ( input != null ) {
                    input.close();
                }

            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
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

    private void copyInputStreamToFile(InputStream in, File file) {
        FileOutputStream out = null;

        try {
            out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            // Ensure that the InputStreams are closed even if there's an exception.
            try {
                if ( out != null ) {
                    out.close();
                }

                // If you want to close the "in" InputStream yourself then remove this
                // from here but ensure that you close it yourself eventually.
            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
}
