package com.uber.kush.backgroundtask;

import android.os.AsyncTask;

import com.uber.kush.helper.Result;
import com.uber.kush.interfaces.INetworkCallBack;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import static com.uber.kush.IConstants.*;

public class NetworkCallAsync extends AsyncTask<HashMap,Void,Result> {

    private String BASE_URL = HTTPS_SCHEME+"//"+FLICKR_DOMAIN+"/"+REQUEST_PATH;
    private INetworkCallBack mINetworkCallBack;

    public NetworkCallAsync(INetworkCallBack mINetworkCallBack){
        this.mINetworkCallBack = mINetworkCallBack;
    }

    @Override
    protected Result doInBackground(HashMap... maps) {
        HashMap<String, String> postDataParams = maps[0];
        Result responseResult = performPostCall(BASE_URL,postDataParams);
        return responseResult;
    }

    @Override
    protected void onPostExecute(Result s) {
        super.onPostExecute(s);
        mINetworkCallBack.onNetWorkCallCompleted(s);
    }

    public Result  performPostCall(String requestURL,
                                   HashMap<String, String> postDataParams) {

        URL url;
        String response = "";
        Result result;
        try {
            url = new URL(requestURL);

            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode=conn.getResponseCode();

            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line=br.readLine()) != null) {
                    response+=line;
                }
                result = new Result(response);
            }
            else {
                Exception ex = new Exception("Empty Data");
                result = new Result(ex);

            }
        } catch (Exception e) {
            result = new Result(e);
            e.printStackTrace();
        }

        return result;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
