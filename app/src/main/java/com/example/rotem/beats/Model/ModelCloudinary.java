package com.example.rotem.beats.Model;

import android.graphics.Bitmap;
import android.util.Log;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * Created by STAS on 21/07/2016.
 */
public class ModelCloudinary {

    Cloudinary cloudinary;



    public ModelCloudinary() {
        this.cloudinary = new Cloudinary("cloudinary://931893361627358:__axMLcEpgeBlJKHQz86Ilm8aFc@stasfainberg");
    }




    public void saveImage(Bitmap image, String imageName, Model.SaveImageListener listener){
        try{
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            byte [] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(bitmapdata);
            String name = imageName.substring(0,imageName.lastIndexOf("."));
            Map res = null;
            res = cloudinary.uploader().upload(bis, ObjectUtils.asMap("public_id", name));
            Log.d("TAG","save image to URL" + res.get("url"));
        }catch(IOException e){
            e.printStackTrace();
        }

    }


    public void getImage(String imageName, Model.GetImageListener listener){

    }






}
