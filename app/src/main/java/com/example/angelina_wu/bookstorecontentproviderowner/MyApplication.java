package com.example.angelina_wu.bookstorecontentproviderowner;


import android.app.Application;
import android.util.Log;

public class MyApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("Application","- onCreate");
    }
}
