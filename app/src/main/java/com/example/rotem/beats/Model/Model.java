package com.example.rotem.beats.Model;

import android.content.Context;

import com.example.rotem.beats.MyApplication;

/**
 * Created by Rotem on 25/06/2016.
 */
public class Model {
    private final static Model instance = new Model();
    Context context;
    ModelFirebase modelFirebase;
    //ModelCoulinary modelCloudinary;
    //ModelSql modelSql;

    private Model(){
        context = MyApplication.getAppContext();
        modelFirebase = new ModelFirebase();
        //modelCloudinary = new ModelCoulinary();
        //modelSql = new ModelSql(MyApplication.getAppContext());
    }

    public static Model getInstance(){
        return instance;
    }

    public interface AuthListener{
        void onDone(String userId, Exception e);
    }
    public void login(String email, String pwd, AuthListener listener){
        modelFirebase.login( email,  pwd,  listener);
    }
    public void signup(String email, String pwd, AuthListener listener){
        modelFirebase.signup( email,  pwd,  listener);
    }
    public String getUserId(){
        return modelFirebase.getUserId();
    }

    public void signout(){
        modelFirebase.signout();
    }
}
