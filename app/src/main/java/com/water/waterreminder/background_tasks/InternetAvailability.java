package com.water.waterreminder.background_tasks;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import java.net.InetAddress;

/**
 * Created by freakycoder on 25/01/16.
 */
public class InternetAvailability {

    Context context;
    public InternetAvailability(Context context) {
        this.context = context;
    }

    public boolean isNetworkConnected() {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        Log.d("MyApp", "Network Connection checking..");
        //return connectivityManager.getActiveNetworkInfo() != null;

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



/*
    public boolean isInternetAvailable() {
        if(isNetworkConnected()) {
            try {
                InetAddress inetAddress = InetAddress.getByName("google.com"); //You can replace it with your name

                if (inetAddress.equals("")) {
                    Log.d("MyApp", "Fck !");
                    return false;
                } else {
                    Log.d("MyApp", "Internet is available");
                    return true;
                }
            } catch (Exception e) {
                Log.d("MyApp", "Internet Exception");
                return false;
            }
        }else{
            Log.d("MyApp", "No Internet 2nd Else");
            return false;
        }
    }
    */
}
