package com.water.waterreminder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.water.waterreminder.anim.AnimationUtils;
import com.water.waterreminder.pojos.Devs;

import java.util.ArrayList;

import general.BitMap;

public class SettingsActivity extends AppCompatActivity {


    //Le RecyclerView <3
    private RecyclerView recyclerView;
    private ArrayList<Devs> list;

    //Notification
    Switch switch1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        list = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        switch1 = (Switch) findViewById(R.id.switch1);


        fillDevs();
        recyclerView.setAdapter(new MyRecyclerAdapter(this, list));

    }


    public void fillDevs() {
        list.add(new Devs(R.drawable.team0,
                getResources().getString(R.string.team_0),
                getResources().getString(R.string.team_0_job),
                R.drawable.ic_facebook_black,
                R.drawable.ic_twitter_black,
                R.drawable.ic_linkedin_black,
                R.drawable.ic_github_black,
                getResources().getString(R.string.team_0_facebook),
                getResources().getString(R.string.team_0_twitter),
                getResources().getString(R.string.team_0_linkedin),
                getResources().getString(R.string.team_0_github)));
    }

    //MyRecyclerAdapter Inner Class
    class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyRecyclerViewHolder> {
        private ArrayList<Devs> list = new ArrayList<>();
        private LayoutInflater inflater;
        private Context context;

        public MyRecyclerAdapter(Context context, ArrayList<Devs> list) {
            this.context = context;
            this.list = list;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public MyRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View root = inflater.inflate(R.layout.creator_layout, viewGroup, false);
            MyRecyclerViewHolder holder = new MyRecyclerViewHolder(root);
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyRecyclerViewHolder holder, int position) {
            final Devs current = list.get(position);


            Bitmap bitmap = BitMap.decodeSampledBitmapFromResource(getResources(), current.getDev_logo(), 70, 70);
            holder.imageView.setImageBitmap(general.getRoundedCornerBitmap.getRoundedCornerBitmap(bitmap, 240));

            holder.textView.setText(current.getDev_name());
            holder.textView2.setText(current.getDev_job());

            holder.imageView2.setBackgroundResource(current.getFacebook_logo());
            holder.imageView2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(current.getDev_facebook_link()));
                    startActivity(intent);
                }
            });

            holder.imageView3.setBackgroundResource(current.getTwitter_logo());
            holder.imageView3.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(current.getDev_twitter_link()));
                    startActivity(intent);
                }
            });

            holder.imageView4.setBackgroundResource(current.getLinked_in_logo());
            holder.imageView4.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(current.getDev_linkedin_link()));
                    startActivity(intent);
                }
            });

            holder.imageView5.setBackgroundResource(current.getGithub_logo());
            holder.imageView5.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse(current.getDev_github_link()));
                    startActivity(intent);
                }
            });

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
            ImageView imageView2;
            ImageView imageView3;
            ImageView imageView4;
            ImageView imageView5;
            RelativeLayout relativeLayout;

            public MyRecyclerViewHolder(View itemView) {
                super(itemView);
                imageView = (ImageView) itemView.findViewById(R.id.logo);
                textView = (TextView) itemView.findViewById(R.id.creator_name);
                textView2 = (TextView) itemView.findViewById(R.id.job_name);
                imageView2 = (ImageView) itemView.findViewById(R.id.facebook_logo);
                imageView3 = (ImageView) itemView.findViewById(R.id.twitter_Logo);
                imageView4 = (ImageView) itemView.findViewById(R.id.linked_in);
                imageView5 = (ImageView) itemView.findViewById(R.id.github_logo);
                relativeLayout = (RelativeLayout) itemView.findViewById(R.id.relativeLayout_creator);
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
