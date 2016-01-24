package com.water.waterreminder.pojos;

/**
 * Created by kurayogun on 20/11/15.
 */
public class User {

    private int id;
    private String username;
    private String password;
    private String email;
    private String gender;
    private int age;
    private String country;
    private int daily_goal;
    private int daily_water;
    private int weekly_water;
    private int montly_water;
    private int yearly_water;
    private int exact_day;


    //Default Constructor
    public User() {
    }

    public User(String username, String password, String email, String gender, int age, String country,int daily_goal) {
        this.daily_goal = daily_goal;
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.country = country;
    }

    public User(String username, int daily_goal) {
        this.username = username;
        this.daily_goal = daily_goal;
    }

    //Constructor


    public User(int id, String username, String password, String email, String gender, int age, String country, int daily_goal) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.gender = gender;
        this.age = age;
        this.country = country;
        this.daily_goal = daily_goal;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getDaily_goal() {
        return daily_goal;
    }

    public void setDaily_goal(int daily_goal) {
        this.daily_goal = daily_goal;
    }

    public int getDaily_water() {
        return daily_water;
    }

    public void setDaily_water(int daily_water) {
        this.daily_water = daily_water;
    }

    public int getWeekly_water() {
        return weekly_water;
    }

    public void setWeekly_water(int weekly_water) {
        this.weekly_water = weekly_water;
    }

    public int getMontly_water() {
        return montly_water;
    }

    public void setMontly_water(int montly_water) {
        this.montly_water = montly_water;
    }

    public int getYearly_water() {
        return yearly_water;
    }

    public void setYearly_water(int yearly_water) {
        this.yearly_water = yearly_water;
    }

    public int getExact_day() {
        return exact_day;
    }

    public void setExact_day(int exact_day) {
        this.exact_day = exact_day;
    }
}
