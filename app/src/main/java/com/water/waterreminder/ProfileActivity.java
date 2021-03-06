package com.water.waterreminder;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.water.waterreminder.anim.AnimationUtils;
import com.water.waterreminder.anim.ColoredSnackbar;
import com.water.waterreminder.background_tasks.MyTaskParams;
import com.water.waterreminder.background_tasks.ProfilePartTask;
import com.water.waterreminder.pojos.Statistic;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import general.BitMap;

public class ProfileActivity extends AppCompatActivity {


    //Le RecyclerView <3
    private RecyclerView recyclerView;
    private ArrayList<Statistic> list;

    TextView profileName;
    TextView current_water_goal;

    DBAdapter db;
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    String username;
    String password;
    int shared_water_goal;
    //User ID
    int user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view_statistic);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profileName = (TextView)findViewById(R.id.profile_name);
        current_water_goal = (TextView) findViewById(R.id.current_daily_goal_value);


        db = new DBAdapter(getApplicationContext());


        prefs = getSharedPreferences("user_info", MODE_PRIVATE);
        username = prefs.getString("username", "Username cannot be found in ProfileActivity");
        password = prefs.getString("password", "Password cannot be found in ProfileActivity");
        shared_water_goal = prefs.getInt("daily_goal_water",0);

        current_water_goal.setText(shared_water_goal+"");

        //Capitalize the first letter of username
        String output = firstLetterCapital(username);
        profileName.setText(output);
        Cursor cursor3 = db.getUserID(username);
        user_id = cursor3.getInt(0);

        fillStatistics();
        recyclerView.setAdapter(new MyRecyclerAdapter(this, list));
    }

/*
    public void getAvg(){
        sum = db.getSumWaterValue(user_id);
        int count = db.getDateCount(user_id);

        avg = sum / count;
    }*/

    public String firstLetterCapital(String input){
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }

    public void name_change(View view){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        alert.setView(dialogView);

        final TextView title = (TextView) dialogView.findViewById(R.id.dialog_title);
        Log.d("MyApp", "title name : " + title.getText());
        title.setText(getResources().getString(R.string.change_username));


        final EditText new_username = (EditText) dialogView.findViewById(R.id.editText);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String new_un = new_username.getText().toString().toLowerCase();

                String method = "update_username";
                ProfilePartTask profilePartTask = new ProfilePartTask(getApplicationContext());
                MyTaskParams params = new MyTaskParams(method,username.toLowerCase(),new_un,true);
                String updateCheck="0";
                try {
                    updateCheck = profilePartTask.execute(params).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                int update = db.updateUsername(username.toLowerCase(), new_un);

                if (Integer.parseInt(updateCheck) > 0 && update >0) {
                    profileName.setText(firstLetterCapital(new_un));
                    editor = prefs.edit();
                    editor.putString("username",new_un);
                    editor.apply(); // This line is IMPORTANT.
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.relativeLayout), getString(R.string.name_changed_success), Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.relativeLayout), getString(R.string.name_changed_failed), Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }



    public void daily_goal_change(View view){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        // ...Irrelevant code for customizing the buttons and title
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        alert.setView(dialogView);

        final TextView title = (TextView) dialogView.findViewById(R.id.dialog_title);
        title.setText(getResources().getString(R.string.change_daily_goal));

        final EditText new_water_goal = (EditText) dialogView.findViewById(R.id.editText);
        new_water_goal.setRawInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                int new_goal = Integer.parseInt(new_water_goal.getText().toString());
                String method = "update_water_goal";
                ProfilePartTask profilePartTask = new ProfilePartTask(getApplicationContext());
                MyTaskParams params = new MyTaskParams(method,username,new_goal);
                String updateCheck="0";
                try {
                    updateCheck = profilePartTask.execute(params).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                int update = db.updateWaterGoal(username.toLowerCase(), new_goal);

                // check is in server side
                if (Integer.parseInt(updateCheck) > 0 && update>0) {
                    current_water_goal.setText(new_goal + "");
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("daily_goal_water",new_goal);
                    editor.apply();
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.mainRelativeLayout), getString(R.string.goal_changed_success), Snackbar.LENGTH_LONG);
                    ColoredSnackbar.info(snackbar).show();
                } else {
                    Snackbar snackbar = Snackbar.make(findViewById(R.id.mainRelativeLayout), getString(R.string.goal_changed_failed), Snackbar.LENGTH_LONG);
                    ColoredSnackbar.info(snackbar).show();
                }
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
       startActivity(new Intent(this,MainActivity.class));
    }

    public void backButton(View view){
        startActivity(new Intent(this, MainActivity.class));
    }

    public void fillStatistics(){
       float avg = prefs.getFloat("average",0);
       double sum = db.getSumWaterValue(user_id);
       int count = db.getDateCount(user_id);
       list.add(new Statistic(R.drawable.ic_info,getString(R.string.average),new DecimalFormat("##.##").format(avg)+" cups"));
       list.add(new Statistic(R.drawable.ic_settings_special,getString(R.string.total_days),count+" "+ getString(R.string.days)));
       list.add(new Statistic(R.drawable.ic_person,getString(R.string.total_water_cons),(int)sum+" "+getString(R.string.cup)));
       list.add(new Statistic(R.drawable.ic_password,"Achievement","114 victory !"));
    }

    //MyRecyclerAdapter Inner Class
    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerViewHolder> {
        private ArrayList<Statistic> list = new ArrayList<>();
        private LayoutInflater inflater;
        private Context context;

        public MyRecyclerAdapter(Context context,ArrayList<Statistic> list) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public MyRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View root = inflater.inflate(R.layout.statistic_layout, viewGroup, false);
            MyRecyclerViewHolder holder = new MyRecyclerViewHolder(root);
            return holder;
        }

        @Override
        public void onBindViewHolder(MyRecyclerViewHolder holder, int position) {
            Statistic current = list.get(position);
            holder.imageView.setBackgroundResource(current.getIcon());
            holder.textView.setText(current.getStatistic_name());
            holder.textView2.setText(current.getStatistic_result());

            AnimationUtils.animate(holder, true);
        }


        @Override
        public int getItemCount() {
            return list.size();
        }

        class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView textView;
            TextView textView2;

            public MyRecyclerViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.sta_icon);
                textView = (TextView) itemView.findViewById(R.id.sta_name);
                textView2 = (TextView) itemView.findViewById(R.id.sta_result);

            }
        }
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
