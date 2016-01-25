package com.water.waterreminder.background_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by kurayogun on 25/01/16.
 */
public class UpdateWaterTask extends AsyncTask<MyTaskParams,Void,Void> {
    Context context;
    public UpdateWaterTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(MyTaskParams... params) {
        String username = params[0].username;
        int daily_water = params[0].daily_water;
        String update_water_url = "http://keepinprogress.com/db_update_water.php";
        try {
            URL url = new URL(update_water_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            Log.d("MyApp", "httpConnection : " + httpURLConnection);
            OutputStream OS =  httpURLConnection.getOutputStream();
            Log.d("MyApp", "OS : "+OS);
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
            Log.d("MyApp", "BufferedWriter : "+bufferedWriter);
            String data =
                    URLEncoder.encode("username", "UTF-8")+"="+ URLEncoder.encode(username,"UTF-8")+"&"+
                            URLEncoder.encode("daily_water","UTF-8")+"="+ URLEncoder.encode(""+daily_water,"UTF-8");
            Log.d("MyApp", "Data Main : "+data);
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            IS.close();
        }catch (Exception e){
            Log.d("MyApp", "ERROR URL in MainActivity Update Water");
            e.printStackTrace();
        }
        return null;
    }
}
