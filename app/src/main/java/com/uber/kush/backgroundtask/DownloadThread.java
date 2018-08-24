package com.uber.kush.backgroundtask;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

import com.uber.kush.model.PhotoVO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.uber.kush.IConstants.CACHE_DIRECTORY_PATH;
import static com.uber.kush.IConstants.UPDATE_ITEM;

/**
 * Thread to download Images from Server
 */
public class DownloadThread extends Thread {
    private PhotoVO photoVO;
    private int position;
    private String url;
    private Handler mHandler;

    public DownloadThread(String url, PhotoVO photoVO, int position, Handler mHandler){
        this.photoVO = photoVO;
        this.position = position;
        this.url = url;
        this.mHandler = mHandler;
    }
    public void run() {
        getBitmapFromURL(url);
        Message message = new Message();
        message.what = UPDATE_ITEM;
        message.arg1 = position;
        mHandler.sendMessage(message);
    }

    /**
     * Method to download Image From Server
     * @param src
     * @return
     */
    private Bitmap getBitmapFromURL(String src) {
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

            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * Copy input stream to provided File Path
     * @param in
     * @param file
     */
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
            try {

                if ( in != null ) {
                    in.close();
                }

                if ( out != null ) {
                    out.close();
                }

            }
            catch ( IOException e ) {
                e.printStackTrace();
            }
        }
    }
}
