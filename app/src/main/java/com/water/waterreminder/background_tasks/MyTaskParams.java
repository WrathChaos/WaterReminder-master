package com.water.waterreminder.background_tasks;

/**
 * Created by freakycoder on 21/01/16.
 */
public class MyTaskParams {
    String method;
    String username;
    String password;
    String email;
    String gender;
    int age;
    String country;
    int daily_goal;
    String user_login;

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


}