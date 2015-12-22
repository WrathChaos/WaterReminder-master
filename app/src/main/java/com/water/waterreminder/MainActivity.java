package com.water.waterreminder;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
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
import android.widget.Toast;

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
import com.water.waterreminder.notification.NotifyService;
import com.water.waterreminder.secretText.SecretTextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.github.mikephil.charting.animation.Easing.EasingOption.EaseInOutBounce;


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

    ImageView details;
    SecretTextView secretTextView;
    SecretTextView secretTextView2;
    TextView total_water_textView;

    DBAdapter db;
    SharedPreferences prefs;

    String username;
    String password;

    //Graphs
    Button btngraphs;
    BarChart chart;

    //Notification
    public static AlarmManager alarmmanager1;
    public static long interval = 1000*60*60*8;
    public static PendingIntent pendingIntentNotification;


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

        //Graph Variables
        chart = (BarChart) findViewById(R.id.chart);
        btngraphs = (Button) findViewById(R.id.btngrph);

        db = new DBAdapter(getApplicationContext());
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);


        username = prefs.getString("username","Username cannot be found in MainActivity");
        password = prefs.getString("password","Password cannot be found in MainActivity");

        // Opening the database for reading data
        try {
            db.open();
            Log.d("MyApp", "DB has opened in MainActivity!");
        } catch (Exception e){
            e.printStackTrace();
        }

        //Functions
        setWaterValue();
        arcProgressFunctionWater();
        actionButtonClick();

        drawGrahps();

        //Notification
        SharedPreferences prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        int active = prefs.getInt("notify_active",1);

        if(active <2){
            alarmMethod(alarmmanager1);
            active++;
            editor.putInt("notify_active",active);
            editor.apply();
            }

    }

    public void drawGrahps(){
        //Graphs
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(4f, 0));
        entries.add(new BarEntry(8f, 1));
        entries.add(new BarEntry(6f, 2));
        entries.add(new BarEntry(5f, 3));
        entries.add(new BarEntry(10f, 4));
        entries.add(new BarEntry(9f, 5));
        entries.add(new BarEntry(7f, 6));

        BarDataSet dataset = new BarDataSet(entries, "Number of Glasses Drunk in a Week");
        dataset.setValueTextSize(10);
        dataset.setBarSpacePercent(25f);

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
        ll.setLineWidth(4f);


        chart.setData(data);
        chart.setDrawValueAboveBar(false);


        YAxis y1 = chart.getAxisLeft();
        y1.setAxisMaxValue(daily_goal);
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
        chart.animateY(3000, EaseInOutBounce);
    }

    public void tographs(View view){
        Intent i = new Intent(this, MonthlyYearlyGraphsActivity.class);
        startActivity(i);
    }

    //Setting Water Value from SharedPreferences
    public void setWaterValue(){
        //SharedPreferences Getting Items
        daily_goal = prefs.getInt("daily_goal_water", 0);
        daily_water = prefs.getInt("daily_water",0);

        Log.d("MyApp", "Daily Water from DB : " + daily_water);
        total_water_textView.setText(daily_water + " / " + daily_goal);
    }

    public void fadeIn(){
        secretTextView.show();    // fadeIn
        secretTextView.setDuration(1000);     // set fade duration to 1 seconds
        secretTextView2.show();   // fadeIn
        secretTextView2.setDuration(1000);     // set fade duration to 1 seconds

        AlphaAnimation fadeInAnimation = new AlphaAnimation(0, 1); // start alpha, end alpha
        fadeInAnimation.setDuration(1000);
        fadeInAnimation.setFillAfter(true); // make the transformation persist
        fab_complete.setAnimation(fadeInAnimation);
    }

    public void fadeOut(){
        secretTextView.hide();    // fadeOut
        secretTextView.setDuration(1000);     // set fade duration to 1 seconds
        secretTextView2.hide();   // fadeOut
        secretTextView2.setDuration(1000);     // set fade duration to 1 seconds
        fab_complete.setVisibility(View.INVISIBLE);

        AlphaAnimation fadeOutAnimation = new AlphaAnimation(1, 0); // start alpha, end alpha
        fadeOutAnimation.setDuration(1000); // time for animation in milliseconds
        fadeOutAnimation.setFillAfter(true); // make the transformation persist
        fab_complete.setAnimation(fadeOutAnimation);
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
                    secretTextView2.setText("  "+add_water_value + " "+getResources().getString(R.string.cup));
                } else {
                    add_water_value = 12;
                    secretTextView2.setText("  "+add_water_value + " "+getResources().getString(R.string.cup));
                }
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (add_water_value > -1) {
                    if (daily_water > 0) {
                        add_water_value--;
                        secretTextView2.setText("  "+add_water_value + " "+getResources().getString(R.string.cup));
                    } else {
                        add_water_value = 1;
                        secretTextView2.setText("  "+add_water_value + " "+getResources().getString(R.string.cup));

                    }
                } else {
                    add_water_value = -1;
                    secretTextView2.setText("  "+add_water_value + " "+getResources().getString(R.string.cup));
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
                arcProgressFunctionWater();

                int update = db.updateDailyWaterValue(username.toLowerCase(), daily_water);
                Log.d("MyApp", "Water Value Updated : " + update);
                if (add_water_value > 0 && update != 0) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.anchor), add_water_value + " "+getResources().getString(R.string.snackbar_water_supply) +" !", Snackbar.LENGTH_SHORT);
                    ColoredSnackbar.info(snackbar).show();
                } else if (add_water_value == 0 || update == 0) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.anchor), getResources().getString(R.string.snackbar_water_add_fail)+" !", Snackbar.LENGTH_LONG);
                    ColoredSnackbar.info(snackbar).show();
                } else if (add_water_value < 0 && update != 0) {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.anchor), 1 + " "+getResources().getString(R.string.snackbar_water_supply_delete) +" !", Snackbar.LENGTH_LONG);
                    ColoredSnackbar.info(snackbar).show();
                }
                fadeOut();
                floatingActionsMenu.collapse();
                add_water_value = 1;
                //Here's a runnable/handler combo
                Runnable mMyRunnable = new Runnable() {
                    @Override
                    public void run() {
                        secretTextView2.setText(add_water_value + "  "+getResources().getString(R.string.cup));
                    }
                };

                Handler myHandler = new Handler();
                myHandler.postDelayed(mMyRunnable, 900); //Message will be delivered in 0.9 second.

                //Toast.makeText(getBaseContext(), add_water_value + " water supply is added !", Toast.LENGTH_SHORT).show();
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

        public void alarmMethod(AlarmManager alarmmanager){
        Intent myIntent1 = new Intent(this, NotifyService.class);
        alarmmanager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntentNotification = PendingIntent.getService(this,0,myIntent1,0);

        Calendar cal=Calendar.getInstance();

        cal.set(Calendar.MILLISECOND, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.HOUR_OF_DAY, 16);
        alarmmanager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), interval, pendingIntentNotification);

        Toast.makeText(this, "Start Alarm", Toast.LENGTH_LONG).show();
    }
}
