package com.water.waterreminder.register.part;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.water.waterreminder.DBAdapter;
import com.water.waterreminder.MainActivity;
import com.water.waterreminder.R;
import com.water.waterreminder.SettingsActivity;
import com.water.waterreminder.background_tasks.MyTaskParams;
import com.water.waterreminder.background_tasks.BackgroundTask;
import com.water.waterreminder.pojos.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CountryActivity extends AppCompatActivity {

    final List<Country> countryList=new ArrayList<>();
    ListView listView ;
    ListViewAdapter adapter;
    final Context context = this;

    String username;
    String password;
    String e_mail;
    String gender;
    String country;
    int age;
    int daily_goal;
    // ArrayList<MyTaskParams> batch_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registercountry);

        // If the database already exist no need to copy it from the assets
        // folder
        try {
            String destPath = "/data/data/" + getPackageName()
                    + "/databases/KeepInProgress";
            File file = new File(destPath);
            File path = new File("/data/data/" + getPackageName()
                    + "/databases/");
            if (!file.exists()) {
                path.mkdirs();
                Log.d("MyApp", "path...");

                CopyDB(getBaseContext().getAssets().open("KeepInProgress"),
                        new FileOutputStream(destPath));
            }
            else{
                Log.d("MyApp","File Path : "+file.toString());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        decorView.setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        listView = (ListView) findViewById(R.id.country_list);
        adapter = new ListViewAdapter(this, countryList);
        listView.setAdapter(adapter);

        fillCountryList();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Get the clicked country name
                TextView tv = (TextView) view.findViewById(R.id.country_name);
                country = tv.getText().toString();


                //SharedPreferences Getting Items
                SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);

                username = prefs.getString("username", "No Username found, we might have missed it, Sorry :(");
                password = prefs.getString("password", "No Username found, we might have missed it, Sorry :(");
                e_mail = prefs.getString("user_email", "No E-Mail found, we might missed it, Sorry :(");
                gender = prefs.getString("gender", "No Gender found, we might have missed it, Sorry :(");
                age = prefs.getInt("age", 0);

                daily_goal = prefs.getInt("daily_goal_water", 0);
                int water_daily_value = 0;
                SharedPreferences.Editor editor = prefs.edit();
                editor.putInt("daily_water", water_daily_value);
                editor.apply();


                DBAdapter db = new DBAdapter(getApplicationContext());

                // Opening the database for reading data
                try {
                    db.open();
                    Log.d("MyApp", "DB Opened");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /**
                 * CRUD Operations
                 * */
                // Inserting Contacts
                Log.d("MyApp", "Inserting ..");
                db.addUser(new User(username, password, e_mail, gender, age, country, daily_goal));

                /*
                if(internet){
                    backgroundTask.execute(params);
                }else{
                    batch_list.add(params);
                }
                */

                String method = "Register";
                BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                MyTaskParams params = new MyTaskParams(method, username, password, e_mail, gender, age, country, daily_goal);
                backgroundTask.execute(params);

                //finish();
                db.close();
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
            }

        });
    }

    public void CopyDB(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        // Copy 1K bytes at a time
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }


    public void fillCountryList(){
        String[] countries = Locale.getISOCountries();
        int j = 0;
        // Loop each country
        for(int i = 0; i < countries.length; i++) {

            String country = countries[i];
            Locale locale = new Locale("en", country);

            // Get the country name by calling getDisplayCountry()
            String countryName = locale.getDisplayCountry();

            // Printing the country name on the console
            countryList.add(new Country(countryName));
            j++;
        }
        Log.d("MyApp", "Total Country # : " + j);
    }

    public void openSetting(View view){
        Intent i = new Intent(this, SettingsActivity.class);
        startActivity(i);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_country, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
