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
        cloudinary = new Cloudinary("cloudinary://931893361627358:__axMLcEpgeBlJKHQz86Ilm8aFc@stasfainberg");
    }

    public void saveImage(final Bitmap imageBitmap, final String imageName) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
                    byte[] bitmapdata = bos.toByteArray();
                    ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
                    String name = imageName.substring(0,imageName.lastIndexOf("."));
                    Map res = cloudinary.uploader().upload(bs , ObjectUtils.asMap("public_id", name));
                    Log.d("ModelCloud:saveImage","save image to url " + res.get("url"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


    public void getImage(String imageName, Model.GetImageListener listener){

    }






}
