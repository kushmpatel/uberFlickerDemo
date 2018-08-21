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

public class NetworkCallAsync extends AsyncTask<String,Void,Result> {

    private String BASE_URL = "https://api.flickr.com/services/rest";
    private INetworkCallBack mINetworkCallBack;

    public NetworkCallAsync(INetworkCallBack mINetworkCallBack){
        this.mINetworkCallBack = mINetworkCallBack;
    }

    @Override
    protected Result doInBackground(String... strings) {
        String searchText = strings[0];

        HashMap<String, String> postDataParams = new HashMap();
        postDataParams.put("method","flickr.photos.search");
        postDataParams.put("api_key","3e7cc266ae2b0e0d78e279ce8e361736");
        postDataParams.put("format","json");
        postDataParams.put("nojsoncallback","1");
        postDataParams.put("safe_search","1");
        postDataParams.put("text",searchText);

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
