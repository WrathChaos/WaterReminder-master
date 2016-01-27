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
 * Created by kurayogun on 27/01/16.
 */
public class ProfilePartTask extends AsyncTask<MyTaskParams, Void, String> {

    Context context;

    public ProfilePartTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(MyTaskParams... params) {
        String method = params[0].method;
        String username = params[0].username;
        int daily_goal = params[0].daily_goal;
        String new_username = params[0].new_username;
        String profile_update_url = "http://keepinprogress.com/db_functions.php";
        if (method.equals("update_username")) {
            try {
                URL url = new URL(profile_update_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                String data =
                        URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode(method, "UTF-8") + "&" +
                                URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+"&"+
                                URLEncoder.encode("new_username", "UTF-8") + "=" + URLEncoder.encode(new_username, "UTF-8");

                                Log.d("MyApp", "Username is Updated !");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                OS.close();

                //RESPONSE FROM SERVER
                InputStream inputStream = httpURLConnection.getInputStream();
                Log.d("MyApp", "InputStream UpdateUsername : " + inputStream);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                Log.d("MyApp", "BufferedReader UpdateUsername : " + bufferedReader);
                String response = "";
                String line = "";
                //Log.d("MyApp", "bufferead null ? : " + bufferedReader.readLine());
                while ((line = bufferedReader.readLine()) != null) {
                    Log.d("MyApp", "Line : "+line);
                    response += line;
                }
                Log.d("MyApp", "response update ?: " + response);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();

                return response;
            } catch (Exception e) {
                Log.d("MyApp", "UPDATE USERNAME ERROR URL !");
                e.printStackTrace();
            }
        } else if (method.equals("update_water_goal")) {
            try {
                URL url = new URL(profile_update_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream OS = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS, "UTF-8"));
                Log.d("MyApp", "Username goal : "+daily_goal);
                String data =
                        URLEncoder.encode("method", "UTF-8") + "=" + URLEncoder.encode(method, "UTF-8") + "&" +
                        URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8")+"&"+
                        URLEncoder.encode("water_goal", "UTF-8") + "=" + URLEncoder.encode("" + daily_goal, "UTF-8");
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
                Log.d("MyApp", "response update dailygoal ?: " + response);
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return response;
            } catch (Exception e) {
                Log.d("MyApp", "Update Daily Goal ERROR ! ");
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
