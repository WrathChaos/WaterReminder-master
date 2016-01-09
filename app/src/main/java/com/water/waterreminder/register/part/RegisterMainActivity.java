package com.water.waterreminder.register.part;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.water.waterreminder.DBAdapter;
import com.water.waterreminder.MainActivity;
import com.water.waterreminder.R;

public class RegisterMainActivity extends AppCompatActivity {

    EditText editText;
    com.github.clans.fab.FloatingActionButton fab;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (Build.VERSION.SDK_INT < 16) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_registermain);

        View decorView = getWindow().getDecorView();
        // Hide the status bar.
        decorView.setSystemUiVisibility(

                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        editText = (EditText) findViewById(R.id.e_mail);
        fab = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.fab);
        gestureDetector = new GestureDetector(new SwipeGestureDetector());
        openRegister();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureDetector.onTouchEvent(event)) {
            return true;
        }
        return super.onTouchEvent(event);
    }

    private void onLeftSwipe() {
        if(editText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter your email, seems you missed that part :)",Toast.LENGTH_SHORT).show();
        }else {
            saveShared();
            Intent i = new Intent(getApplicationContext(), RegisterSecondActivity.class);
            startActivity(i);
        }
    }

    private void onRightSwipe() {
        if(editText.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(),"Please enter your email, seems you missed that part :)",Toast.LENGTH_SHORT).show();
        }else {
            saveShared();
            Intent i = new Intent(getApplicationContext(), RegisterSecondActivity.class);
            startActivity(i);
        }
    }

    // Private class for gestures
    private class SwipeGestureDetector
            extends GestureDetector.SimpleOnGestureListener {
        // Swipe properties, you can change it to make the swipe
        // longer or shorter and speed
        private static final int SWIPE_MIN_DISTANCE = 120;
        private static final int SWIPE_MAX_OFF_PATH = 200;
        private static final int SWIPE_THRESHOLD_VELOCITY = 200;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               float velocityX, float velocityY) {
            try {
                float diffAbs = Math.abs(e1.getY() - e2.getY());
                float diff = e1.getX() - e2.getX();

                if (diffAbs > SWIPE_MAX_OFF_PATH)
                    return false;

                // Left swipe
                if (diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    RegisterMainActivity.this.onLeftSwipe();

                    // Right swipe
                } else if (-diff > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                    RegisterMainActivity.this.onRightSwipe();
                }
            } catch (Exception e) {
                Log.e("YourActivity", "Error on gestures");
            }
            return false;
        }
    }

    public void saveShared(){
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        String email = editText.getText().toString();
        if(email.contains("@"))
            editor.putString("user_email", email);

        editor.apply(); // This line is IMPORTANT.
    }

    //Button to Intent activity
    public void openRegister(){
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editText.getText().toString().toLowerCase();

                DBAdapter db = new DBAdapter(getApplicationContext());
                boolean emailValidation = db.checkEMail(email);
                if(email.equals("") || !email.contains("@") ){
                    Toast.makeText(getApplicationContext(),"Please enter a valid E-Mail",Toast.LENGTH_LONG).show();
                    // FIXME: 25/11/15 Snackbar does not appear, FIX IT !
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.relativeLayout_mainRegister),"Please enter a valid E-Mail.",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else if(emailValidation){
                    Toast.makeText(getApplicationContext(),"This E-Mail is already using, please try another E-Mail",Toast.LENGTH_LONG).show();
                    // FIXME: 25/11/15 Snackbar does not appear, FIX IT !
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.relativeLayout_mainRegister),"This E-Mail is already using, please try another E-Mail",Snackbar.LENGTH_LONG);
                    snackbar.show();
                }else {
                    saveShared();
                    Intent i = new Intent(getApplicationContext(), RegisterSecondActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            Intent i = new Intent(this,RegisterSecondActivity.class);
            if(editText.getText().toString().equals("")){
                Toast.makeText(getApplicationContext(),"Please enter your email, seems you missed that part :)",Toast.LENGTH_SHORT).show();
            }else {
                //SharedPreferences
                saveShared();
                startActivity(i);
                return true;
            }
        }
        return super.dispatchKeyEvent(e);
    }

    @Override
    public void onBackPressed() {
        Log.d("MyApp","fck");
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
