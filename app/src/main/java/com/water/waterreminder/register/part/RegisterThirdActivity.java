package com.water.waterreminder.register.part;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.water.waterreminder.DBAdapter;
import com.water.waterreminder.R;
import com.water.waterreminder.anim.ColoredSnackbar;

public class RegisterThirdActivity extends AppCompatActivity {


    EditText editText;
    EditText editText2;

    com.github.clans.fab.FloatingActionButton fab;

    DBAdapter db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_third);

        editText = (EditText) findViewById(R.id.daily_water);
        editText2 = (EditText) findViewById(R.id.age);
        fab = (com.github.clans.fab.FloatingActionButton)  findViewById(R.id.fab);
        db = new DBAdapter(getApplicationContext());


        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String water_goal = editText.getText().toString();
                String age = editText2.getText().toString();
                boolean flag = db.checkWaterGoal(Integer.parseInt(water_goal));
                if(water_goal.equals("") || age.equals("") || !flag){
                    Toast.makeText(getApplicationContext(), "Please enter fill all fields", Toast.LENGTH_LONG).show();
                    // FIXME: 25/11/15 Snackbar does not appear, FIX IT !
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.relativeLayoutThird),"Please enter fill all fields \nWater Goal cannot be more than 35",Snackbar.LENGTH_LONG);
                    ColoredSnackbar.info(snackbar).show();
                }else {
                    saveShared();
                    Intent i = new Intent(getApplicationContext(),CountryActivity.class);
                    startActivity(i);
                }
            }
        });
    }


    public void saveShared(){
        int daily_water = Integer.parseInt(editText.getText().toString());
        int age = Integer.parseInt(editText2.getText().toString());
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("age", age);
        editor.putInt("daily_goal_water", daily_water);

        editor.apply(); // This line is IMPORTANT. If you miss this one its not gonna work!
    }

}
