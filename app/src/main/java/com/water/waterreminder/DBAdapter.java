package com.water.waterreminder;

/**
 * Created by kurayogun on 20/11/15.
 *
 * */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.water.waterreminder.pojos.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DBAdapter extends SQLiteOpenHelper {

    private SQLiteDatabase db;

    public static final String KEY_ROWID = "id";
    public static final String KEY_NAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_GENDER = "gender";
    public static final String KEY_AGE = "age";
    public static final String KEY_COUNTRY = "country";
    public static final String KEY_GOAL = "daily_goal";
    public static final String KEY_DAILY = "daily_water";
    public static final String KEY_WEEKLY = "weekly_water";
    public static final String KEY_MONTLY = "montly_water";
    public static final String KEY_YEARLY = "yearly_water";


    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "User";
    private static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATE = "CREATE TABLE `User` (\n" +
            "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`username`\tTEXT NOT NULL,\n" +
            "\t`password`\tTEXT NOT NULL,\n" +
            "\t`email`\tTEXT NOT NULL,\n" +
            "\t`gender`\tTEXT NOT NULL,\n" +
            "\t`age`\tINTEGER NOT NULL,\n" +
            "\t`country`\tTEXT,\n" +
            "\t`daily_goal`\tINTEGER NOT NULL,\n" +
            "\t`daily_water`\tINTEGER DEFAULT '0',\n" +
            "\t`weekly_water`\tINTEGER DEFAULT '0',\n" +
            "\t`montly_water`\tINTEGER DEFAULT '0',\n" +
            "\t`yearly_water`\tINTEGER DEFAULT '0'\n" +
            ");";

    public DBAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


        @Override
        public void onCreate(SQLiteDatabase db) {
            // onCreate() is called by the framework, if the database does not exist
            Log.d("MyApp", "Creating the database");

            try {
                db.execSQL(DATABASE_CREATE);
            } catch (android.database.SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Sends a Warn log message
            Log.w("MyApp", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");

            // Method to execute an SQL statement directly
            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
            onCreate(db);
        }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new user
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, user.getUsername().toLowerCase());              // User Name
        values.put(KEY_PASSWORD,user.getPassword());                        // User Password
        values.put(KEY_EMAIL, user.getEmail().toLowerCase());              // User Email
        values.put(KEY_GENDER, user.getGender());                         // User Gender
        values.put(KEY_AGE, user.getAge());                              // User Age
        values.put(KEY_COUNTRY, user.getCountry());                     // User Country
        values.put(KEY_GOAL,user.getDaily_goal());                     // User Daily Goal

        Log.d("MyApp", "Creating User");
        // Inserting Row
        db.insert(DATABASE_TABLE, null, values);
        db.close(); // Closing database connection
    }

    // Getting single user
    User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(DATABASE_TABLE, new String[] {KEY_NAME, KEY_GOAL}, KEY_ROWID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        // return user
        return new User(cursor.getString(0), Integer.parseInt(cursor.getString(1)));
    }


    /*
    1. SharedPreference should be encrypted ! password should be encrypted
    4. how to reset daily water value daily and add that value in weekly, montly and yearly
    5. how to reset weekly, montly, yearly..
     */

    /*
    DONE :

    1. Database created
    2. DBAdapter added
    3. Registeration is working now
    4. User can login, should login*
    5. Registeration part has totally new design.
    6. Bug fixes (After registeration, some bugs are fixed)
    7. User can login without capital letter sensitivity in username part
    8. Login Password has capital letter sensitivity
    9. E-Mail "@" validation is completed. Now, users need to insert an email which contains "@"
    10. E-Mail validation is completed. Now, users cannot register with the same E-Mail
    11. Username validation is completed. Now, users cannot register with the same Username which is already taken
     */

    /*
    IDEAS :
        Profile photo will be removed
        Notification can be control with SeekBar (1 to 5)
        !Remind Me should be done ! When a user login or register, whenever user opens the app again, it should login automatically
        Profile part should be change, also Oğuz should work on Setting part with notification
       ****! can use " SELECT username,email FROM User WHERE (username='kuray' OR email='wrathchaos@hotmail.com') AND (password='hello123');  !****
        Oguz should work on Facebook Register and Login

     */

    public Cursor fetchUser(String user_login, String password)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT "+KEY_EMAIL+","+KEY_PASSWORD+" FROM "+DATABASE_TABLE +" WHERE "+KEY_EMAIL+"="+"'"+user_login.toLowerCase()+"'"+" AND "+KEY_PASSWORD+"="+"'"+password+"'";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            return getWaterGoalValue(user_login, password);
        }else {
            Log.d("MyApp","Username Logining...");
            String sql2 = "SELECT "+KEY_NAME+","+KEY_PASSWORD+" FROM "+DATABASE_TABLE +" WHERE "+KEY_NAME+"="+"'"+user_login.toLowerCase()+"'"+" AND "+KEY_PASSWORD+"="+"'"+password+"'";
            Cursor cursor2 = db.rawQuery(sql2, null);
            if(cursor2.moveToFirst()){
                return getWaterGoalValue(user_login,password);
            }
        }
            Log.d("MyApp","Cannot Logged in!");
            return null;
    }

    public Cursor getWaterGoalValue(String user_login, String password){

        Cursor myCursor = db.query(DATABASE_TABLE,
                new String[]{KEY_GOAL},
                KEY_EMAIL + "='" + user_login.toLowerCase() + "' AND " +
                        KEY_PASSWORD + "='" + password + "'", null, null, null, null);
        if (myCursor.moveToFirst()) {
            return myCursor;
        }else{
            Cursor myCursor2 = db.query(DATABASE_TABLE,
                    new String[]{KEY_GOAL},
                    KEY_NAME + "='" + user_login.toLowerCase() + "' AND " +
                            KEY_PASSWORD + "='" + password + "'", null, null, null, null);
            if (myCursor2 != null) {
                myCursor2.moveToFirst();
                return myCursor2;
            }

        }

        return myCursor;
    }


    public int updateWaterGoal(String user_login,int new_goal){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag = checkWaterGoal(new_goal);
        if(flag) {
            ContentValues values = new ContentValues();
            values.put(KEY_GOAL, new_goal);           // Updating new water goal

            String selection = KEY_NAME + " = ?";
            String[] selectionArgs = {"" + user_login};
            // updating water_value row
            return db.update(DATABASE_TABLE, values,selection,selectionArgs);
        }
        return 0;

    }


    public Cursor getDailyWaterValue(String user_login, String password){
        String sql = "SELECT "+ KEY_DAILY + " FROM " +DATABASE_TABLE+" WHERE "+KEY_NAME+"='"+user_login+"'"+" AND "+KEY_PASSWORD+"='"+password+"'";
        String sql2 ="SELECT "+ KEY_DAILY + " FROM " +DATABASE_TABLE+" WHERE "+KEY_EMAIL+"='"+user_login+"'"+" AND "+KEY_PASSWORD+"='"+password+"'";
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            Log.d("MyApp", "SQL CURSOR 1 : " + cursor.moveToFirst());
            return cursor;
        }else{
            Cursor cursor2 = db.rawQuery(sql2, null);
            if(cursor2.moveToFirst()){
                Log.d("MyApp", "SQL CURSOR 2 : " + cursor2.moveToFirst());
                return cursor2;
            }
        }
        return null;
    }


    public int updateDailyWaterValue(String user_login,int addWater){
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("MyApp", "userLogin : " + user_login);
        ContentValues values = new ContentValues();
        values.put(KEY_DAILY, addWater);           // Adding water
        String selection;
        if(user_login.contains("@")){
            selection = KEY_EMAIL + " = ?";
        }else {
            selection = KEY_NAME + " = ?";
        }
        String[] selectionArgs = { "" + user_login };


        // updating water_value row
        return db.update(DATABASE_TABLE, values,selection,selectionArgs);
    }

    public int updateUsername(String user_login,String username){
        SQLiteDatabase db = this.getWritableDatabase();

        boolean flag = checkUsername(username);
        ContentValues values = new ContentValues();
        String selection = KEY_NAME + " = ?";
        String[] selectionArgs = {"" + user_login};
        if(flag)
            return 0;

        values.put(KEY_NAME, username);
        // updating username row
        return db.update(DATABASE_TABLE, values, selection, selectionArgs);
    }

    public boolean checkWaterGoal(int water_goal){
        if(water_goal<=0 || water_goal >35){
            return false;
        }
        return true;
    }

    public boolean checkEMail(String email){
        String sql = "SELECT " + KEY_EMAIL +" FROM "+DATABASE_TABLE+" WHERE "+KEY_EMAIL +"='"+email+"'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if(cursor.moveToFirst()){
            Log.d("MyApp","Email is exist");
            return true;
        }else
            return  false;
    }

    public boolean checkUsername(String username){
        String sql = "SELECT " + KEY_NAME + " FROM " + DATABASE_TABLE + " WHERE " + KEY_NAME + "='"+username+"'";
        Log.d("MyApp","SQL : "+sql);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            Log.d("MyApp","Username is exist");
            return true;
        }else{
            Log.d("MyApp","Username is NOT exist");
            return false;
        }
    }

    // Getting All Users
    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + DATABASE_TABLE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                User user = new User();
                user.setId(Integer.parseInt(cursor.getString(0)));
                user.setUsername(cursor.getString(1));
                user.setDaily_goal(Integer.parseInt(cursor.getString(2)));
                // Adding user to list
                userList.add(user);
            } while (cursor.moveToNext());
        }

        // return user list
        return userList;
    }

    // Updating single user
    public int updateUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ROWID,user.getId());
        values.put(KEY_NAME, user.getUsername());           // User Name
        values.put(KEY_EMAIL, user.getEmail());             // User Email
        values.put(KEY_GENDER, user.getGender());           // User Gender
        values.put(KEY_AGE, user.getAge());                 // User Age
        values.put(KEY_COUNTRY, user.getCountry());         // User Country
        values.put(KEY_GOAL,user.getDaily_goal());          // User Daily Goal
        values.put(KEY_DAILY, user.getDaily_water());       // User Daily Water
        values.put(KEY_WEEKLY, user.getWeekly_water());     // User Weekly Water
        values.put(KEY_MONTLY, user.getMontly_water());     // User Montly Water
        values.put(KEY_YEARLY, user.getYearly_water());     // User Yearly Water

        // updating row
        return db.update(DATABASE_TABLE, values, KEY_ROWID + " = ?",
                new String[] { String.valueOf(user.getId()) });
    }

    // Deleting single user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_ROWID + " = ?",
                new String[] { String.valueOf(user.getId()) });
        db.close();
    }
    // Deletes a particular user
    public boolean deleteUser(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteAllUsers(int count){
        SQLiteDatabase db = this.getWritableDatabase();
        boolean res = false;
        for(int i = 0;i<count;i++) {
           res =  db.delete(DATABASE_TABLE, KEY_ROWID + "=" + i, null) > 0;
        }
        return res;
    }
    // Getting users Count
    public int getUsersCount() {
        String countQuery = "SELECT  * FROM " + DATABASE_TABLE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // return count
        return cursor.getCount();
    }
    // Opens the database
    public DBAdapter open() throws SQLException {
        // Create and/or open a database that will be used for reading only
        db = this.getReadableDatabase();
        return this;
    }

    // Closes the database
    public void close() {
        // Closes the database
        db.close();
    }

}