package com.water.waterreminder;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.water.waterreminder.anim.ColoredSnackbar;
import com.water.waterreminder.register.part.RegisterMainActivity;

import general.TransparentStatusBar;


public class LoginActivity extends AppCompatActivity implements TransparentStatusBar{

    private CoordinatorLayout mRoot;
    private EditText mInputUserLogin;
    private EditText mInputPassword;


    Button loginButton;
    Button registerButton;

    DBAdapter db;
    String user_login;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initializing
        mRoot = (CoordinatorLayout) findViewById(R.id.root_activity_second);
        mInputUserLogin = (EditText) findViewById(R.id.user_login);
        mInputPassword = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.classicLoginButton);
        registerButton = (Button) findViewById(R.id.registerButton);

        db = new DBAdapter(getApplicationContext());

        // Opening the database for reading data
        try {
            db.open();
            Log.d("MyApp", "DB has opened in LoginActivity!");
        } catch (Exception e){
            e.printStackTrace();
        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                classicLogin(v);
            }
        });


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        else {
            //Transparent StatusBar
            transparentStatusBar();
        }

        // Prevent to auto popping keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }


    public void classicLogin(View view) {
        Intent intent = new Intent(this,MainActivity.class);

        user_login = mInputUserLogin.getText().toString().toLowerCase();
        password = mInputPassword.getText().toString();

        Cursor c = db.fetchUser(user_login,password);
        Cursor c2 = db.getDailyWaterValue(user_login,password);

            if(c != null || c2 != null){
                int water_goal_value = c.getInt(0);
                int water_daily_value = c2.getInt(0);
                Log.d("MyApp","Water Goal Value while Logining: " + water_goal_value);
                Log.d("MyApp","Water Value while Logining: "+water_daily_value);
                SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("username",user_login);
                editor.putString("password",password);
                editor.putInt("daily_goal_water", water_goal_value);
                editor.putInt("daily_water",water_daily_value);
                editor.apply(); // This line is IMPORTANT.

                db.close();
                startActivity(intent);
            }else{
                // FIXME: 23/11/15 Fix this snackbar it appears into the bottom bar
                //Toast.makeText(getApplicationContext(),"Email or Password is wrong! Try to register.",Toast.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar.make(findViewById(R.id.relativeLayout),"Email or Password is wrong! Try to register.",Snackbar.LENGTH_LONG);
                ColoredSnackbar.info(snackbar).show();

            }

    }

    public void registerButton(View view){
        Intent i = new Intent(this,RegisterMainActivity.class);
        startActivity(i);
    }

    @Override
    public void transparentStatusBar() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        getWindow().setStatusBarColor(Color.TRANSPARENT);
    }

    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
