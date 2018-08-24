package com.uber.kush.helper;

import com.uber.kush.model.PhotoResponseVO;
import com.uber.kush.model.PhotoVO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to parse JSon
 */
public class JSonResponseParser {

    /**
     * Parse Json for Photo List Response
     * @param responseJson
     * @return
     */
    public PhotoResponseVO getPhotoResponseVO(String responseJson){
        PhotoResponseVO mPhotoResponseVO = null;
        try{
            JSONObject parentObject = new JSONObject(responseJson);
            JSONObject photoResponseObject = parentObject.getJSONObject("photos");
            int page = photoResponseObject.getInt("page");
            int pages = photoResponseObject.getInt("pages");
            int perpage = photoResponseObject.getInt("perpage");
            String total = photoResponseObject.getString("total");

            mPhotoResponseVO = new PhotoResponseVO();
            mPhotoResponseVO.setPage(page);
            mPhotoResponseVO.setPages(pages);
            mPhotoResponseVO.setPerpage(perpage);
            mPhotoResponseVO.setTotal(total);

            List<PhotoVO> listPhoto = new ArrayList<>();

            JSONArray photoArray = photoResponseObject.getJSONArray("photo");
            int size = photoArray.length();

            for(int i=0;i<size;i++){
                PhotoVO mPhotoVO = new PhotoVO();
                JSONObject photoObject = photoArray.getJSONObject(i);
                long id = photoObject.getLong("id");
                String owner = photoObject.getString("owner");
                String secret = photoObject.getString("secret");
                String server = photoObject.getString("server");
                int farm = photoObject.getInt("farm");
                String title = photoObject.getString("title");
                int ispublic = photoObject.getInt("ispublic");
                int isfriend = photoObject.getInt("isfriend");
                int isfamily = photoObject.getInt("isfamily");

                mPhotoVO.setId(id);
                mPhotoVO.setOwner(owner);
                mPhotoVO.setSecret(secret);
                mPhotoVO.setServer(server);
                mPhotoVO.setFarm(farm);
                mPhotoVO.setTitle(title);
                mPhotoVO.setIspublic(ispublic);
                mPhotoVO.setIsfamily(isfamily);
                mPhotoVO.setIsfriend(isfriend);
                listPhoto.add(mPhotoVO);
            }
            mPhotoResponseVO.setPhoto(listPhoto);
            
        } catch (JSONException ex){
            ex.printStackTrace();
        }
        
        return mPhotoResponseVO;
    }
}
