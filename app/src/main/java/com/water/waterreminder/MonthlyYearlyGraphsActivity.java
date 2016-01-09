package com.water.waterreminder;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

import static com.github.mikephil.charting.animation.Easing.EasingOption.EaseInOutBack;
import static com.github.mikephil.charting.animation.Easing.EasingOption.EaseOutSine;

public class MonthlyYearlyGraphsActivity extends AppCompatActivity {

    SharedPreferences prefs ;
    int water_goal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly_yearly_graphs);

        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        water_goal = prefs.getInt("daily_goal_water", 0);


        LineChart chart = (LineChart) findViewById(R.id.chart3);
        LineChart chart2 = (LineChart) findViewById(R.id.chart2);

        ArrayList<Entry> entries = new ArrayList<>(); //yearly
        entries.add(new Entry(4f, 1));
        entries.add(new Entry(4f, 2));
        entries.add(new Entry(1f, 3));
        entries.add(new Entry(3f, 4));
        entries.add(new Entry(6f, 5));
        entries.add(new Entry(7f, 6));
        entries.add(new Entry(9f, 7));
        entries.add(new Entry(4f, 8));
        entries.add(new Entry(8f, 9));
        entries.add(new Entry(2f, 10));
        entries.add(new Entry(10f, 11));
        entries.add(new Entry(4f, 12));


        ArrayList<Entry> entries2 = new ArrayList<>(); //monthly
        entries2.add(new Entry(9f, 1));
        entries2.add(new Entry(10f, 2));
        entries2.add(new Entry(2f, 3));
        entries2.add(new Entry(10f, 4));
        entries2.add(new Entry(10f, 5));
        entries2.add(new Entry(5f, 6));
        entries2.add(new Entry(10f, 7));
        entries2.add(new Entry(13f, 8));
        entries2.add(new Entry(24f, 9));


        LineDataSet dataset = new LineDataSet(entries,"Average Number of Glasses Drunk Yearly"); //yearly
        dataset.setDrawCubic(true);
        dataset.setDrawFilled(false);
        dataset.setValueTextSize(10);
        dataset.setFillColor(Color.BLUE);

        LineDataSet dataset2 = new LineDataSet(entries2,"Average Number of Glasses Drunk Monthly"); //monthly
        dataset2.setDrawCubic(true);
        dataset2.setDrawFilled(true);
        dataset2.setValueTextSize(10);
        dataset2.setFillColor(Color.WHITE);


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


        ArrayList<String> labels2 = new ArrayList<String>(); //monthly

        labels2.add("");
        labels2.add("1");
        labels2.add("7");
        labels2.add("14");
        labels2.add("21");
        labels2.add("28");
        labels2.add("35");
        labels2.add("42");
        labels2.add("49");
        labels2.add("52");


        LineData data = new LineData(labels,dataset); //yearly
        LineData data2 = new LineData(labels2,dataset2); //monthly

        chart.setData(data); //yearly
        //chart.setPadding(120, 20, 20, 20);
        chart2.setData(data2); //monthly

        LimitLine ll = new LimitLine(water_goal);
        ll.setLineColor(Color.WHITE);
        ll.setLineWidth(1f);


        YAxis y1 = chart.getAxisLeft();
        y1.setAxisMaxValue(water_goal);
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

        ////////////////////////////////////////////////////////////////////////


        YAxis y2 = chart2.getAxisLeft();
        y2.setAxisMaxValue(water_goal);
        y2.setLabelCount(8, true);
        y2.setStartAtZero(true);//
        y2.setDrawGridLines(false);
        y2.setAxisLineColor(getResources().getColor(R.color.myWhite));
        y2.setGridColor(getResources().getColor(R.color.myWhite));
        y2.setTextColor(getResources().getColor(R.color.myWhite));


        YAxis y22 = chart2.getAxisRight();      // MONTHLY X-Y INFO
        y22.setEnabled(false);

        XAxis x2 = chart2.getXAxis();
        x2.setSpaceBetweenLabels(3);
        x2.setDrawGridLines(false);
        x2.setAxisLineColor(getResources().getColor(R.color.myWhite));
        x2.setGridColor(getResources().getColor(R.color.myWhite));
        x2.setTextColor(getResources().getColor(R.color.myWhite));


        chart.setDescription("");               //yearly
        chart.animateY(3000, EaseInOutBack);  //yearly
        chart.setTouchEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setBorderColor(getResources().getColor(R.color.myWhite));
        chart.setBackgroundColor(getResources().getColor(R.color.transperent));


        chart2.setDescription("");              //monthly
        chart2.animateY(3000, EaseOutSine);  //monthly
        chart2.setTouchEnabled(false);
        chart2.setDrawGridBackground(false);
        chart2.setBorderColor(getResources().getColor(R.color.myWhite));
        chart2.setBackgroundColor(getResources().getColor(R.color.transperent));
    }



    public void getSharedPref(){



    }
}
