package com.water.waterreminder.background_tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by kurayogun on 25/01/16.
 */
public class MainPartTask extends AsyncTask<MyTaskParams,Void,String> {
    Context context;
    public MainPartTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(MyTaskParams... params) {
        int userID = params[0].user_id;
        String method = params[0].method;
        String username = params[0].username;
        int daily_water = params[0].daily_water;
        String date2 = params[0].date;
        Log.d("MyApp", "Method : "+method);
        String main_part_url = "http://keepinprogress.com/db_main_part.php";
        String update_day = "http://keepinprogress.com/db_update_day.php";
        String function_url = "http://keepinprogress.com/db_functions.php";

        if (method.equals("update_water")) {
            try {
                URL url = new URL(main_part_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data =
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode(date2, "UTF-8") + "&" +
                        URLEncoder.encode("user_id", "UTF-8") + "=" + URLEncoder.encode(""+userID, "UTF-8") + "&" +
                        URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode(method, "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                        URLEncoder.encode("daily_water", "UTF-8") + "=" + URLEncoder.encode("" + daily_water, "UTF-8");
                Log.d("MyApp", "Water Value is Updated !");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
            } catch (Exception e) {
                Log.d("MyApp", "ERROR URL in MainActivity Update Water");
                e.printStackTrace();
            }
        }else if (method.equals("insert_date")){
            int user_id = params[0].user_id;
            int daily_water2 = params[0].daily_water;
            String date = params[0].date;
            int current_day = params[0].current_day;
            try {
                URL url = new URL (main_part_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS =  httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
                String data =
                        URLEncoder.encode("method","UTF-8")+"="+ URLEncoder.encode(""+method,"UTF-8")+"&"+
                        URLEncoder.encode("user_id","UTF-8")+"="+ URLEncoder.encode(""+user_id,"UTF-8")+"&"+
                                URLEncoder.encode("daily_water","UTF-8")+"="+ URLEncoder.encode(""+daily_water2,"UTF-8")+"&"+
                                URLEncoder.encode("date","UTF-8")+"="+ URLEncoder.encode(""+date,"UTF-8")+"&"+
                                URLEncoder.encode("current_day","UTF-8")+"="+ URLEncoder.encode(""+current_day,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
            }catch (Exception e){
                Log.d("MyApp", "ERROR Insert DATE URL in MainActivity");
                e.printStackTrace();
            }
        } else if(method.equals("update_day")){
            int daily_water2 = params[0].daily_water;
            int current_day = params[0].current_day;
            String username2 = params[0].username;
            try {
                URL url = new URL(update_day);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data =
                        URLEncoder.encode("daily_water", "UTF-8") + "=" + URLEncoder.encode(""+daily_water2, "UTF-8") + "&" +
                        URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode(method, "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode("" + username2, "UTF-8")+"&"+
                        URLEncoder.encode("current_day", "UTF-8") + "=" + URLEncoder.encode("" + current_day, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
            } catch (Exception e) {
                Log.d("MyApp", "ERROR URL in MainActivity Update DAY");
                e.printStackTrace();
            }
        }else if(method.equals("insert_batch")){
            Log.d("MyApp", "BATCH !");
            int date_id = params[0].user_id;
            int value = params[0].daily_water;
            String date = params[0].date;
            int current_day = params[0].current_day;
            try {
                URL url = new URL(function_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data =
                        URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode(method, "UTF-8") + "&" +
                        URLEncoder.encode("date_id", "UTF-8") + "=" + URLEncoder.encode("" + date_id, "UTF-8")+"&"+
                        URLEncoder.encode("value", "UTF-8") + "=" + URLEncoder.encode(""+value, "UTF-8") + "&" +
                        URLEncoder.encode("date", "UTF-8") + "=" + URLEncoder.encode("" + date, "UTF-8")+"&"+
                        URLEncoder.encode("current_day", "UTF-8") + "=" + URLEncoder.encode("" + current_day, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();
                InputStream IS = httpURLConnection.getInputStream();
                IS.close();
            } catch (Exception e) {
                Log.d("MyApp", "ERROR URL in MainActivity Batch Insertion");
                e.printStackTrace();
            }
        }else if(method.equals("get_user_id")){
            String username2 = params[0].username;
            try {
                URL url = new URL(function_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data =
                        URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode(method, "UTF-8") + "&" +
                                URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username2, "UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                //RESPONSE FROM SERVER
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String response = "";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    response += line;
                }
                Log.d("MyApp", "response User ID on MainPart: " + response);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (Exception e) {
                Log.d("MyApp", "ERROR URL in MainActivity get User ID from Server");
                e.printStackTrace();
            }
        }
        return null;

    }
}
