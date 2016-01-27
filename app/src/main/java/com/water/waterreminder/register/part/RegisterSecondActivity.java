package com.water.waterreminder.register.part;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.water.waterreminder.DBAdapter;
import com.water.waterreminder.R;
import com.water.waterreminder.background_tasks.BackgroundTask;
import com.water.waterreminder.background_tasks.MyTaskParams;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class RegisterSecondActivity extends AppCompatActivity {

    Spinner spinner;
    com.github.clans.fab.FloatingActionButton fab;

    EditText editText ;
    EditText editText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_second);

        spinner = (Spinner) findViewById(R.id.spinner);
        fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab);
        editText = (EditText) findViewById(R.id.username);
        editText2 = (EditText) findViewById(R.id.password);


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        //Filling spinner
        fillSpinner();
        countryOpen();
    }

    public void fillSpinner(){
        List<String> spinnerArray1 = new ArrayList<String>();
        spinnerArray1.add(getString(R.string.male));
        spinnerArray1.add(getString(R.string.female));

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,R.layout.custom_spinner, spinnerArray1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter1);
    }

    public void countryOpen(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String username = editText.getText().toString().toLowerCase();
                String password = editText2.getText().toString();
                DBAdapter db = new DBAdapter(getApplicationContext());
                String method = "check_username";
                BackgroundTask backgroundTask = new BackgroundTask(getApplicationContext());
                MyTaskParams params = new MyTaskParams(method,username);
                String checkUsername="0";
                try {
                    checkUsername = backgroundTask.execute(params).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                boolean usernameValidation = db.checkUsername(username);
                if(username.equals("") || password.equals("") || !(Integer.parseInt(checkUsername) > 0)){
                    Toast.makeText(getApplicationContext(), "Please enter a valid Username and Password", Toast.LENGTH_LONG).show();
                    // FIXME: 25/11/15 Snackbar does not appear, FIX IT !
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.relativeLayoutSecond),"Please enter a valid Username.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if(usernameValidation){
                    Toast.makeText(getApplicationContext(),"This Username is already using, please try another E-Mail",Toast.LENGTH_LONG).show();
                    // FIXME: 25/11/15 Snackbar does not appear, FIX IT !
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.relativeLayoutSecond),"This Username is already using, please try another E-Mail",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    saveShared();
                    Intent i = new Intent(getApplicationContext(), RegisterThirdActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    public void saveShared(){
        String spinnerGender = spinner.getSelectedItem().toString();
        String username = editText.getText().toString();
        String password = editText2.getText().toString();
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putString("gender", spinnerGender);
        Log.d("MyApp", "Username : " + username + "\n Password : " + password + "\n Gender : " + spinnerGender);

        editor.apply(); // This line is IMPORTANT. If you miss this one its not gonna work!
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
