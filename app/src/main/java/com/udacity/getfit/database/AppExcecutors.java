package com.udacity.getfit.database;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by HARSHAD on 15/08/2018.
 */

public class AppExcecutors {

    private static AppExcecutors sInstance;
    private static final Object LOCK = new Object();
    private final Executor diskIO;

    public AppExcecutors(Executor diskIO){
        this.diskIO = diskIO;
    }
    public static AppExcecutors getInstance(){
        if (sInstance == null) {
            synchronized (LOCK){
                sInstance = new AppExcecutors(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO(){return diskIO;}
}
