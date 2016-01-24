package com.water.waterreminder;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.github.lzyzsd.circleprogress.ArcProgress;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.water.waterreminder.anim.ColoredSnackbar;
import com.water.waterreminder.notification.NotificationEventReceiver;
import com.water.waterreminder.notification.NotificationIntentService;
import com.water.waterreminder.pojos.User;
import com.water.waterreminder.secretText.SecretTextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import static com.github.mikephil.charting.animation.Easing.EasingOption.EaseInOutQuad;


public class MainActivity extends AppCompatActivity {


    private ArcProgress arcProgressWater;
    Timer timer2;
    int total_water_percentage = 0;
    double change_water_perc = 0.0;

    int add_water_value = 1;
    int daily_goal;
    int daily_water;


    FloatingActionsMenu floatingActionsMenu;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab_complete;

    android.support.design.widget.FloatingActionButton fab;

    ImageView details;
    SecretTextView secretTextView;
    SecretTextView secretTextView2;
    TextView total_water_textView;

    DBAdapter db;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    String username;
    String password;

    //Graphs
    Button btngraphs;
    BarChart chart;

    //Water Sound
    SoundPool mySound;
    int soundID;


    //User ID
    int user_id;
    //Current Date
    String now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }

        //Initializing Variables
        floatingActionsMenu = (FloatingActionsMenu) findViewById(R.id.floatActionMenu);
        details = (ImageView) findViewById(R.id.details);
        secretTextView = (SecretTextView) findViewById(R.id.secretText);
        secretTextView2 = (SecretTextView) findViewById(R.id.secretText2);
        fab1 = (FloatingActionButton) findViewById(R.id.fab1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab_complete = (FloatingActionButton) findViewById(R.id.fab_complete);
        total_water_textView = (TextView) findViewById(R.id.daily_total_water);
        fab = (android.support.design.widget.FloatingActionButton) findViewById(R.id.info_button);

        //Water Sound
        mySound = new SoundPool(1, AudioManager.STREAM_MUSIC,0);
        soundID = mySound.load(this,R.raw.watersound,1);

        //Graph Variables
        chart = (BarChart) findViewById(R.id.chart);
        btngraphs = (Button) findViewById(R.id.btngrph);

        db = new DBAdapter(getApplicationContext());
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        editor = prefs.edit();

        username = prefs.getString("username","Username cannot be found in MainActivity");
        password = prefs.getString("password","Password cannot be found in MainActivity");

        // Opening the database for reading data
        try {
            db.open();
            Log.d("MyApp", "DB has opened in MainActivity!");
        } catch (Exception e){
            Log.d("MyApp", " DB has NOT opened in MainActivity!");
            e.printStackTrace();
        }

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            now = df.format(new Date());


            String exact_day = now;
            Log.d("MyApp", "Exact_Day : " + exact_day);
            Log.d("MyApp", "username : "+username);


        // User_ID from Server
        user_id = prefs.getInt("user_id",0);
/*
        Cursor cursor3 = db.getUserID(username);
            user_id = cursor3.getInt(0);
*/
        if(!db.checkDateTableIDEmpty(user_id)) {
            int id = prefs.getInt("user_id",0);
            String e_mail = prefs.getString("user_email", "No E-Mail found, we might missed it, Sorry :(");
            String gender = prefs.getString("gender", "No Gender found, we might have missed it, Sorry :(");
            int age = prefs.getInt("age", 0);
            String country = prefs.getString("country","No Country found, we might have missed it, Sorry :(");
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
            db.addUser(new User(id,username, password, e_mail, gender, age, country, daily_goal));

            Log.d("MyApp", "date table is empty ! User ID : " + user_id + "\nValue : " + getWater() + "\nDate : " + now);
            db.insertDate(user_id, getWater(), now, getTheCurrentDay());
            db.updateDay(now, username);
        }else{
            Cursor cursor = db.getDBDay(username);
            String db_day = cursor.getString(0);
            Log.d("MyApp", "DB Day : "+db_day);
            if(!db_day.equals(exact_day)){
                    Cursor cursor2 = db.getUserID(username);
                    user_id = cursor2.getInt(0);
                    db.updateDailyWaterValue(username, 0);
                    Log.d("MyApp", "Day is different ! " + "\n+Exact_day1 : " + exact_day + "\nDB Day1 : " + db_day);
                    editor.putInt("daily_water", 0);
                    editor.apply();
                    db.updateDay(exact_day, username);
               }
            if (!db.checkDateValue(user_id, now)) {
                Log.d("MyApp", "Table is exist! User ID : " + user_id + "\nValue : " + getWater() + "\nDate : " + now);
                db.insertDate(user_id, getWater(), now, getTheCurrentDay());
               }
            }
        //Notification
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        String test = sdf.format(cal.getTime());
        String[] time = test.split(":");
        Log.e("MyApp", "Time0 : " + test + "\nExact Hour0 : " + time[0]);

        if(Integer.parseInt(time[0])<22 && Integer.parseInt(time[0])>8){
            Log.e("MyApp", "Time : " + test + "\nExact Hour : " + time[0]);
            NotificationEventReceiver.setupAlarm(getApplicationContext());
        }

        //Functions
        setWaterValue();
        arcProgressFunctionWater();
        actionButtonClick();
        if (db.checkDateTableIDEmpty(user_id)) {
            drawGrahps(db.getDateCount(user_id));
        }


    }

    public int getWater(){
        Cursor getWater = db.getDailyWaterValue(username, password);
        if(getWater != null){
            int water = getWater.getInt(0);
            return water;
        }
        return 0;

    }

    public int getTheCurrentDay() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        day--;
        Log.d("MyApp", "TheCurrentDay : "+day);
        return day;
    }

    public void drawGrahps(int count){
        //Graphs
        ArrayList<BarEntry> entries = new ArrayList<>();
        int[] list = new int[7];
        //count--;
        Cursor values = db.getDateValue(user_id);
        Log.d("MyApp", "Count : "+count);
        if(values != null && count >=7 ) {
            for (int i = count; i > count-7; i--) {
                float a_side = (float)values.getInt(0);
                int b_side = values.getInt(1);
                //b_side--;
                //Log.d("MyApp", "Count>=7: " + values.getInt(0) + " ----- : " + b_side);
                if(list[b_side]<=0)
                    list[b_side] = (int)a_side;

                values.moveToNext();
            }
            for(int j=0; j<=count; j++) {
                entries.add(new BarEntry(list[j], j));
                //Log.d("MyApp", "list : "+list[j]);
            }

        } else if(values != null && count <7){
            for (int i = 1; i <=count; i++) {
                //Log.d("MyApp", "fck it  : " + values.getInt(1));
                float a_side = (float)values.getInt(0);
                int b_side = values.getInt(1);
                //b_side--;
                //Log.d("MyApp", "Count>=7: " + values.getInt(0) + " ----- : " + b_side);
                if(list[b_side]<=0)
                    list[b_side] = (int) a_side;
                //Log.d("MyApp", "A_SIDE : "+a_side+"\nB_SIDE : "+b_side+"\nlist : "+list[6]);
                values.moveToNext();
            }
            for(int j = 0; j <= 6; j++) {
                entries.add(new BarEntry(list[j], j));
                //Log.d("MyApp", "list : " + list[j]);
            }
        }

        BarDataSet dataset = new BarDataSet(entries, "Weekly");
        dataset.setValueTextSize(10);
        dataset.setBarSpacePercent(25f);
        dataset.setValueFormatter(new MyValueFormatter());
        //dataset.setColor(getResources().getColor(R.color.myWhite));
        ArrayList<String> labels = new ArrayList<String>();

        labels.add("Sun");
        labels.add("Mon");
        labels.add("Tue");
        labels.add("Wed");
        labels.add("Thu");
        labels.add("Fri");
        labels.add("Sat");

        BarData data = new BarData(labels,dataset);

        LimitLine ll = new LimitLine(daily_goal);
        ll.setLineColor(Color.BLUE);
        ll.setLineWidth(1f);

        chart.setData(data);
        chart.setDrawValueAboveBar(false);
        //chart.setDrawBorders(true);

        YAxis y1 = chart.getAxisLeft();
        y1.setAxisMaxValue(daily_goal + 1);
        y1.setLabelCount(8, true);
        y1.setStartAtZero(true);
        y1.setDrawGridLines(false);
        y1.addLimitLine(ll);

        YAxis y12 = chart.getAxisRight();
        y12.setEnabled(false);

        XAxis x1 = chart.getXAxis();
        x1.setDrawGridLines(false);

        chart.setDescription("");
        chart.setTouchEnabled(false);
        chart.animateY(3500, EaseInOutQuad);

    }

    public void toGraphs(View view){
        Intent i = new Intent(this, GraphActivity.class);
        startActivity(i);
    }

    //Setting Water Value from SharedPreferences
    public void setWaterValue(){
        //SharedPreferences Getting Items
        daily_goal = prefs.getInt("daily_goal_water", 0);
        daily_water = prefs.getInt("daily_water",0);
        /*
        Cursor cursor = db.getDailyWaterValue(username,password);
        if(cursor.moveToFirst()) {
            daily_water = cursor.getInt(0);
            Log.d("MyApp", "Daily Water from DB : " + cursor.getInt(0));
        } else{
            int flag = db.updateCurrentValue(0, user_id,now);
            Log.d("MyApp", "Water is changed by registeration : 0"+"Flag : "+flag);
        }*/

        Log.d("MyApp", "Daily Goal From Shared : " + prefs.getInt("daily_goal_water", 0));
        total_water_textView.setText(daily_water + " / " + daily_goal);
    }

    public void fadeIn(){
        secretTextView.show();    // fadeIn
        secretTextView.setDuration(1000);     // set fade duration to 1 seconds
        secretTextView2.show();   // fadeIn
        secretTextView2.setDuration(1000);     // set fade duration to 1 seconds

        fab.setVisibility(View.VISIBLE);
        AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1); // start alpha, end alpha
        fadeInAnimation.setDuration(1000);
        fadeInAnimation.setFillAfter(true); // make the transformation persist
        fab_complete.setAnimation(fadeInAnimation);
        fab.setAnimation(fadeInAnimation);

    }

    public void fadeOut(){
        secretTextView.hide();    // fadeOut
        secretTextView.setDuration(1000);     // set fade duration to 1 seconds
        secretTextView2.hide();   // fadeOut
        secretTextView2.setDuration(1000);     // set fade duration to 1 seconds
        fab_complete.setVisibility(View.INVISIBLE);
        fab.setVisibility(View.INVISIBLE);

        AlphaAnimation fadeOutAnimation = new AlphaAnimation(1, 0); // start alpha, end alpha
        fadeOutAnimation.setDuration(1000); // time for animation in milliseconds
        fadeOutAnimation.setFillAfter(true); // make the transformation persist
        fab_complete.setAnimation(fadeOutAnimation);
        fab.setAnimation(fadeOutAnimation);
    }

    //Floating ActionButtonMenu Click Function
    public void actionButtonClick() {
            floatingActionsMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
                @Override
                public void onMenuExpanded() {
                    fadeIn();
                }

                @Override
                public void onMenuCollapsed() {
                    fadeOut();
                }
            });

            //Expanded Buttons' onClickListener
            fab1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (add_water_value < 12) {
                        add_water_value++;
                        secretTextView2.setText("  " + add_water_value + " " + getResources().getString(R.string.cup));
                    } else {
                        add_water_value = 12;
                        secretTextView2.setText("  " + add_water_value + " " + getResources().getString(R.string.cup));
                    }
                }
            });
            fab2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (add_water_value > -1) {
                        if (daily_water > 0) {
                            add_water_value--;
                            secretTextView2.setText("  " + add_water_value + " " + getResources().getString(R.string.cup));
                        } else {
                            add_water_value = 1;
                            secretTextView2.setText("  " + add_water_value + " " + getResources().getString(R.string.cup));

                        }
                    } else {
                        add_water_value = -1;
                        secretTextView2.setText("  " + add_water_value + " " + getResources().getString(R.string.cup));
                    }
                }
            });

            fab_complete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    daily_water += add_water_value;
                    Log.d("MyApp", "Add_Water_Value : " + add_water_value);
                    Log.d("MyApp", "Daily_Water : " + daily_water);

                    total_water_textView.setText(daily_water + " / " + daily_goal);
                    change_water_perc = (daily_water / (daily_goal * 1.0)) * 100;

                    int update = db.updateDailyWaterValue(username.toLowerCase(), daily_water);
                    Log.d("MyApp", "Water Value Updated : " + update);
                    if (add_water_value > 0 && update != 0) {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.anchor), add_water_value + " " + getResources().getString(R.string.snackbar_water_supply) + " !", Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.info(snackbar).show();
                        int flag = db.updateCurrentValue(daily_water, user_id, now);
                        //editor = prefs.edit();
                        editor.putInt("daily_water", daily_water);
                        editor.apply();
                        Log.d("MyApp", "Current Value is updated ?: " + flag + "\nValue : " + prefs.getInt("daily_water", 10));
                        arcProgressFunctionWater();
                        if (db.checkDateTableIDEmpty(user_id)) {
                            drawGrahps(db.getDateCount(user_id));
                        }
                    } else if (add_water_value == 0 || update == 0) {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.anchor), getResources().getString(R.string.snackbar_water_add_fail) + " !", Snackbar.LENGTH_LONG);
                        ColoredSnackbar.info(snackbar).show();
                    } else if (add_water_value < 0 && update != 0) {
                        Snackbar snackbar = Snackbar.make(findViewById(R.id.anchor), 1 + " " + getResources().getString(R.string.snackbar_water_supply_delete) + " !", Snackbar.LENGTH_SHORT);
                        ColoredSnackbar.info(snackbar).show();
                        int flag = db.updateCurrentValue(daily_water, user_id, now);
                        editor.putInt("daily_water", daily_water);
                        editor.apply();
                        Log.d("MyApp", "Current Value is updated ?: " + flag + "\nValue : " + daily_water + "\nDate : " + now);
                        arcProgressFunctionWater();
                        if (db.checkDateTableIDEmpty(user_id)) {
                            drawGrahps(db.getDateCount(user_id));
                        }
                    }
                    fadeOut();
                    floatingActionsMenu.collapse();
                    add_water_value = 1;
                    //Here's a runnable/handler combo
                    Runnable mMyRunnable = new Runnable() {
                        @Override
                        public void run() {
                            secretTextView2.setText(add_water_value + "  " + getResources().getString(R.string.cup));
                        }
                    };

                    //Water Sound Play
                    mySound.play(soundID, 1, 1, 1, 0, 1);

                    Handler myHandler = new Handler();
                    myHandler.postDelayed(mMyRunnable, 900); //Message will be delivered in 0.9 second.

                    //Toast.makeText(getBaseContext(), add_water_value + " water supply is added !", Toast.LENGTH_SHORT).show();
                }
            });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Dialog customDialog;

                customDialog = new Dialog(MainActivity.this);
                customDialog.setContentView(R.layout.custom_dialog_info);

                ImageView close_button = (ImageView) customDialog.findViewById(R.id.close_button);
                //customDialog.setTitle("Phone Numbers");
                customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                close_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customDialog.dismiss();
                    }
                });
                customDialog.show();
            }
            });
    }

    //Second Progress Function for Daily Water
    public void arcProgressFunctionWater() {
        arcProgressWater = (ArcProgress) findViewById(R.id.arc_progress_water);
        change_water_perc=(daily_water*100)/daily_goal;
        total_water_percentage = arcProgressWater.getProgress();

        timer2 = new Timer();
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (total_water_percentage < change_water_perc) {
                            arcProgressWater.setProgress(arcProgressWater.getProgress() + 1);
                            total_water_percentage += 1;
                        } else {
                            // FIXME: 19/10/15 timer should go onDestroy at there
                        }
                    }
                });
            }
        }, 500, 50);
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

    public void logOut(View view) {
        db.close();
        editor.clear();
        editor.commit();
        Log.d("MyApp", "Logout ! ");
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    //Intent SettingActivity
    public void settingsOpen(View view){
        Intent i = new Intent(this,SettingsActivity.class);
        startActivity(i);
    }

    //Intent ProfileActivity
    public void profileOpen(View view){
        onStop();
        Intent i = new Intent(this,ProfileActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this,LoginActivity.class);
        startActivity(i);
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
