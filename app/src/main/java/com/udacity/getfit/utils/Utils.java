package com.udacity.getfit.utils;


public class Utils {

    public static String getCurrentUserForDB(String userName){
        String displayName = "";
        if(userName.contains("@")){
            displayName = userName.substring(0,userName.indexOf("@"));
            return displayName;
        }else{
            return userName;
        }


    }
}


