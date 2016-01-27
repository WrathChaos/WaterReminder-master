package com.water.waterreminder.background_tasks;

/**
 * Created by freakycoder on 21/01/16.
 */
public class MyTaskParams {
    String method;
    int user_id;
    String username;
    String new_username;
    String password;
    String email;
    String gender;
    int age;
    String country;
    int daily_goal;
    String user_login;
    int daily_water;
    String date;
    int current_day;

    public MyTaskParams(String method, String username, String password, String email, String gender, int age, String country, int daily_goal) {
        this.method = method;
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.country = country;
        this.daily_goal = daily_goal;
    }

    public MyTaskParams(String method,String user_login, String password){
        this.method = method;
        this.user_login = user_login;
        this.password = password;
    }

    public MyTaskParams(int user_id,String method, String username, int daily_water,String date) {
        this.date = date;
        this.user_id = user_id;
        this.method = method;
        this.username = username;
        this.daily_water = daily_water;
    }

    public MyTaskParams(String method, int user_id, int daily_water, String date, int current_day) {
        this.method = method;
        this.user_id = user_id;
        this.daily_water = daily_water;
        this.date = date;
        this.current_day = current_day;
    }

    public MyTaskParams(int daily_water,int current_day, String username, String method) {
        this.daily_water = daily_water;
        this.method = method;
        this.username = username;
        this.current_day = current_day;
    }

    //Update Username
    public MyTaskParams(String method, String username, String new_username,boolean flag) {
        this.method = method;
        this.username = username;
        this.new_username = new_username;
    }

    //Update Daily Goal
    public MyTaskParams(String method, String username, int daily_goal) {
        this.method = method;
        this.username = username;
        this.daily_goal = daily_goal;
    }
}