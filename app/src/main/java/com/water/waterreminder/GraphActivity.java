package com.water.waterreminder;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.Calendar;

import static com.github.mikephil.charting.animation.Easing.EasingOption.EaseInOutBack;

public class GraphActivity extends AppCompatActivity {

    DBAdapter db;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    int water_goal;
    int user_id;
    String username;
    double sum;
    double avg;

    HorizontalBarChart horizontalBarChart;
    LineChart chart;
    TextView total_day;
    TextView total_water;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphs);

        db = new DBAdapter(getApplicationContext());
        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        editor = prefs.edit();

        water_goal = prefs.getInt("daily_goal_water", 0);
        username = prefs.getString("username", "Username cannot be found in GraphActivity");

        horizontalBarChart = (HorizontalBarChart) findViewById(R.id.hori_barchart);
        chart = (LineChart) findViewById(R.id.yearly_chart);
        total_day = (TextView) findViewById(R.id.total_day);
        total_water = (TextView) findViewById(R.id.total_water);

        Cursor cursor3 = db.getUserID(username);
        user_id = cursor3.getInt(0);

        //Calculating Average && put it into SharedPreference
        calcAvg();
        setTotalValues();
        drawGrahps();
    }

    public void setTotalValues(){
        int totalDay = db.getDateCount(user_id);
        total_day.setText(totalDay+" Days |  ");
        double totalWater = db.getSumWaterValue(user_id);
        total_water.setText((int)totalWater+" Cups");
    }

    public void calcAvg(){
        sum = db.getSumWaterValue(user_id);
        int count = db.getDateCount(user_id);

        avg = sum / count;

        editor.putFloat("average",(float)avg);
        editor.apply(); // This line is IMPORTANT

    }

    public void drawGrahps(){

        ArrayList<Entry> entries = new ArrayList<>(); //yearly

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        for(int i =1; i<10; i++){
            int month_sum = db.getSumOfMonth(user_id,"0"+i,year);
            //Log.d("MyApp", " Graph "+i+" : "+month_sum);
            entries.add(new Entry(month_sum, i));
        }
        for(int i=10; i<=12; i++){
            int month_sum = db.getSumOfMonth(user_id,""+i,year);
            //Log.d("MyApp", " Graph "+i+" : "+month_sum);
            entries.add(new Entry(month_sum, i));
        }

        LineDataSet dataset = new LineDataSet(entries,"Average Number of Glasses Drunk Yearly"); //yearly
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(false);
        dataset.setValueTextSize(10);
        dataset.setFillColor(Color.BLUE);


        ArrayList<String> labels = new ArrayList<String>(); //yearly

        labels.add("");
        labels.add("Jan");
        labels.add("Feb");
        labels.add("Mar");
        labels.add("Apr");
        labels.add("May");
        labels.add("Jun");
        labels.add("Jul");
        labels.add("Aug");
        labels.add("Sep");
        labels.add("Oct");
        labels.add("Nov");
        labels.add("Dec");


        LineData data = new LineData(labels,dataset); //yearly

        chart.setData(data); //yearly

        LimitLine ll = new LimitLine(water_goal);
        ll.setLineColor(Color.WHITE);
        ll.setLineWidth(1f);


        YAxis y1 = chart.getAxisLeft();
        y1.setAxisMaxValue((float)sum+20);
        y1.setLabelCount(8, true);
        y1.setStartAtZero(true);
        y1.setDrawGridLines(false);
        y1.setAxisLineColor(getResources().getColor(R.color.myWhite));
        y1.setGridColor(getResources().getColor(R.color.myWhite));
        y1.setTextColor(getResources().getColor(R.color.myWhite));

        YAxis y12 = chart.getAxisRight();       // YEARLY X-Y INFO
        y12.setEnabled(false);

        XAxis x1 = chart.getXAxis();
        x1.setSpaceBetweenLabels(1);
        x1.setDrawGridLines(false);
        x1.setAxisLineColor(getResources().getColor(R.color.myWhite));
        x1.setGridColor(getResources().getColor(R.color.myWhite));
        x1.setTextColor(getResources().getColor(R.color.myWhite));


        chart.setDescription("");               //yearly
        chart.animateY(3000, EaseInOutBack);  //yearly
        chart.setTouchEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setBorderColor(getResources().getColor(R.color.myWhite));
        chart.setBackgroundColor(getResources().getColor(R.color.transperent));

        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////////////////

        horizontalBarChart.setDescription("");
        horizontalBarChart.setTouchEnabled(false);
        horizontalBarChart.setDrawBarShadow(false);
        horizontalBarChart.setDrawValueAboveBar(true);
        horizontalBarChart.setDrawGridBackground(false);

        XAxis xl = horizontalBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xl.setTypeface(tf);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(2f);
        xl.setAxisLineColor(getResources().getColor(R.color.myWhite));
        xl.setGridColor(getResources().getColor(R.color.myWhite));
        xl.setTextColor(getResources().getColor(R.color.myWhite));

        YAxis yl = horizontalBarChart.getAxisLeft();
        // yl.setTypeface(tf);
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(2f);
        yl.setAxisMaxValue(water_goal);
        yl.setAxisLineColor(getResources().getColor(R.color.myWhite));
        yl.setGridColor(getResources().getColor(R.color.myWhite));
        yl.setTextColor(getResources().getColor(R.color.myWhite));

        // yl.setInverted(true);

        YAxis yr = horizontalBarChart.getAxisRight();
        // yr.setTypeface(tf);
        yr.setGridLineWidth(2f);
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);
        yr.setAxisMaxValue(water_goal);

        yr.setAxisLineColor(getResources().getColor(R.color.myWhite));
        yr.setGridColor(getResources().getColor(R.color.myWhite));
        yr.setTextColor(getResources().getColor(R.color.myWhite));


        setData((float)avg);
        horizontalBarChart.animateY(2500);
    }

    private void setData(float avg) {

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("Avg");
        yVals1.add(new BarEntry(avg, 0));


        BarDataSet set1 = new BarDataSet(yVals1, "Average of daily water consumption");
        set1.setValueTextColor(getResources().getColor(R.color.myWhite));
        set1.setColor(getResources().getColor(R.color.accent));

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);
        data.setValueTextSize(18f);
        // data.setValueTypeface(tf);
        data.setValueTextColor(getResources().getColor(R.color.myWhite));

        horizontalBarChart.setData(data);
    }
}
