package com.water.waterreminder.background_tasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.water.waterreminder.MainActivity;

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
 * Created by freakycoder on 21/01/16.
 */


public class BackgroundTask extends AsyncTask<MyTaskParams,Void,String> {

    Context context;
    public BackgroundTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(MyTaskParams... params) {
        String reg_url = "http://keepinprogress.com/db_register.php";
        String login_url = "http://keepinprogress.com/db_login.php";
        String method             = params[0].method;
        if(method.equals("Register")){
            String username           = params[0].username;
            String password           = params[0].password;
            String email              = params[0].email;
            String gender             = params[0].gender;
            int age                   = params[0].age;
            String country            = params[0].country;
            int daily_goal            = params[0].daily_goal;
            Log.d("MyApp", "**** Information ***** \n" + username + "\n" + password + "\n" + email + "\n" + gender + "\n" + age + "\n" + country + "\n" + daily_goal);

            try {
            URL url = new URL(reg_url);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream OS =  httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(OS,"UTF-8"));
            String data =
                    URLEncoder.encode("username","UTF-8")+"="+ URLEncoder.encode(""+username,"UTF-8")+"&"+
                    URLEncoder.encode("password","UTF-8")+"="+ URLEncoder.encode(""+password,"UTF-8")+"&"+
                    URLEncoder.encode("email","UTF-8")+"="+ URLEncoder.encode(""+email,"UTF-8")+"&"+
                    URLEncoder.encode("gender","UTF-8")+"="+ URLEncoder.encode(""+gender,"UTF-8")+"&"+
                    URLEncoder.encode("age","UTF-8")+"="+ URLEncoder.encode(""+age,"UTF-8")+"&"+
                    URLEncoder.encode("country","UTF-8")+"="+ URLEncoder.encode(""+country,"UTF-8")+"&"+
                    URLEncoder.encode("daily_goal","UTF-8")+"="+ URLEncoder.encode(""+daily_goal,"UTF-8");
            bufferedWriter.write(data);
            bufferedWriter.flush();
            bufferedWriter.close();
            OS.close();
            InputStream IS = httpURLConnection.getInputStream();
            IS.close();
            return "Registration succeed..!";
        }catch (Exception e){
            Log.d("MyApp", "ERROR URL");
            e.printStackTrace();
        }
        }else if(method.equals("Login")){
            String user_login = params[0].user_login;
            String password = params[0].password;

            Log.d("MyApp", "**** Login Info ***** \nUser Login : "+user_login+"\nPassword : "+password);
            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true); // This line is IMPORTANT to get value from server !

                OutputStream outputStream = httpURLConnection.getOutputStream();
                Log.d("MyApp", "OS : "+outputStream);
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream,"UTF-8"));
                Log.d("MyApp", "bufferedWriter : "+bufferedWriter);
                String data =
                        URLEncoder.encode("user_login","UTF-8")+"="+URLEncoder.encode(user_login,"UTF-8")+"&"+
                        URLEncoder.encode("password","UTF-8")+"="+URLEncoder.encode(password,"UTF-8");
                bufferedWriter.write(data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();

                //RESPONSE FROM SERVER
                InputStream inputStream = httpURLConnection.getInputStream();
                Log.d("MyApp", "InputStream : "+inputStream);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
                Log.d("MyApp", "BufferedReader : "+bufferedReader);
                String response = "";
                String line = "";
                while((line = bufferedReader.readLine()) != null){
                    response += line;
                }
                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                Log.d("MyApp", "RESPONSE : "+response);

                return response;
            }catch (Exception e){
                Log.d("MyApp", "ERROR URL LOGIN PART");
                e.printStackTrace();
            }

        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String result) {
        SharedPreferences  prefs = context.getSharedPreferences("user_info", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if(result.equals("-2")){
            Log.d("MyApp", "fck");
            editor.putInt("daily_goal_water",-2);
        }else {
            String arr[] = result.split(",");
            int water_goal = Integer.parseInt(arr[0]);
            int daily_water = Integer.parseInt(arr[1]);
            String username = arr[2];
            String password = arr[3];
            int user_id = Integer.parseInt(arr[4]);
            String email = arr[5];
            String gender = arr[6];
            int age = Integer.parseInt(arr[7]);
            String country = arr[8];

            editor.putInt("daily_goal_water", water_goal);
            editor.putInt("daily_water", daily_water);
            editor.putString("username", username);
            editor.putString("password", password);
            editor.putInt("user_id", user_id);
            editor.putString("user_email", email);
            editor.putString("gender", gender);
            editor.putInt("age", age);
            editor.putString("country", country);
            editor.apply(); // This line is IMPORTANT
            Log.d("MyApp", "Server Side Goal : " + water_goal);
        }
    }
}
