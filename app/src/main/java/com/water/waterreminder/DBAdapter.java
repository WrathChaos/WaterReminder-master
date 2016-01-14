package com.water.waterreminder;

/**
 * Created by kurayogun on 20/11/15.
 */

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
    public static final String KEY_EXACT_DAY = "exact_day";

    //Date Table
    public static final String KEY_DATE_ID = "date_id";
    public static final String KEY_VALUE = "value";
    public static final String KEY_DATE = "date";
    public static final String KEY_CURRENT_DATE = "current_day";

    private static final String TAG = "DBAdapter";

    private static final String DATABASE_NAME = "MyDB";
    private static final String DATABASE_TABLE = "User";
    private static final String DATABASE_TABLE_DATE = "User_Date";

    private static final int DATABASE_VERSION = 11;

    private static final String DATABASE_CREATE = "CREATE TABLE `User` (\n" +
            "\t`id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`username`\tTEXT NOT NULL,\n" +
            "\t`password`\tTEXT NOT NULL,\n" +
            "\t`email`\tTEXT NOT NULL,\n" +
            "\t`gender`\tTEXT NOT NULL,\n" +
            "\t`age`\tINTEGER NOT NULL,\n" +
            "\t`country`\tTEXT,\n" +
            "\t`daily_goal`\tINTEGER NOT NULL,\n" +
            "\t`daily_water`\tINTEGER,\n" +
            "\t`weekly_water`\tINTEGER,\n" +
            "\t`montly_water`\tINTEGER,\n" +
            "\t`yearly_water`\tINTEGER,\n" +
            "\t`exact_day`\tINTEGER\n" +
            ");";

    private static final String DATABASE_CREATE_DATE = "CREATE TABLE `User_Date` (\n" +
            "\t`date_id`\tINTEGER NOT NULL,\n" +
            "\t`value`\tINTEGER,\n" +
            "\t`date`\tTEXT,\n" +
            "\t`current_day`\tINTEGER\n" +
            ");";

    public DBAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // onCreate() is called by the framework, if the database does not exist
        Log.d("MyApp", "Creating the database");

        try {
            db.execSQL(DATABASE_CREATE_DATE);
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
        values.put(KEY_PASSWORD, user.getPassword());                        // User Password
        values.put(KEY_EMAIL, user.getEmail().toLowerCase());              // User Email
        values.put(KEY_GENDER, user.getGender());                         // User Gender
        values.put(KEY_AGE, user.getAge());                              // User Age
        values.put(KEY_COUNTRY, user.getCountry());                     // User Country
        values.put(KEY_GOAL, user.getDaily_goal());                     // User Daily Goal
        values.put(KEY_EXACT_DAY, 0);

        Log.d("MyApp", "User is created!");
        // Inserting Row
        db.insert(DATABASE_TABLE, null, values);
        db.close(); // Closing database connection
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

    public Cursor fetchUser(String user_login, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT " + KEY_EMAIL + "," + KEY_PASSWORD + " FROM " + DATABASE_TABLE + " WHERE " + KEY_EMAIL + "=" + "'" + user_login.toLowerCase() + "'" + " AND " + KEY_PASSWORD + "=" + "'" + password + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            return getWaterGoalValue(user_login, password);
        } else {
            Log.d("MyApp", "Username Logining...");
            String sql2 = "SELECT " + KEY_NAME + "," + KEY_PASSWORD + " FROM " + DATABASE_TABLE + " WHERE " + KEY_NAME + "=" + "'" + user_login.toLowerCase() + "'" + " AND " + KEY_PASSWORD + "=" + "'" + password + "'";
            Cursor cursor2 = db.rawQuery(sql2, null);
            if (cursor2.moveToFirst()) {
                return getWaterGoalValue(user_login, password);
            }
        }
        Log.d("MyApp", "Cannot Logged in!");
        return null;
    }

    public Cursor getWaterGoalValue(String user_login, String password) {

        Cursor myCursor = db.query(DATABASE_TABLE,
                new String[]{KEY_GOAL},
                KEY_EMAIL + "='" + user_login.toLowerCase() + "' AND " +
                        KEY_PASSWORD + "='" + password + "'", null, null, null, null);
        if (myCursor.moveToFirst()) {
            return myCursor;
        } else {
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

    public int updateWaterGoal(String user_login, int new_goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean flag = checkWaterGoal(new_goal);
        if (flag) {
            ContentValues values = new ContentValues();
            values.put(KEY_GOAL, new_goal);           // Updating new water goal

            String selection = KEY_NAME + " = ?";
            String[] selectionArgs = {"" + user_login};
            // updating water_value row
            return db.update(DATABASE_TABLE, values, selection, selectionArgs);
        }
        return 0;
    }

    public Cursor getDailyWaterValue(String user_login, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT " + KEY_DAILY + " FROM " + DATABASE_TABLE + " WHERE " + KEY_NAME + "='" + user_login + "'" + " AND " + KEY_PASSWORD + "='" + password + "'";
        String sql2 = "SELECT " + KEY_DAILY + " FROM " + DATABASE_TABLE + " WHERE " + KEY_EMAIL + "='" + user_login + "'" + " AND " + KEY_PASSWORD + "='" + password + "'";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            Log.d("MyApp", "SQL CURSOR 1 : " + cursor.moveToFirst());
            return cursor;
        } else {
            Cursor cursor2 = db.rawQuery(sql2, null);
            if (cursor2.moveToFirst()) {
                Log.d("MyApp", "SQL CURSOR 2 : " + cursor2.moveToFirst());
                return cursor2;
            }
        }
        return null;
    }

    public Cursor getUserID(String user_login) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT " + KEY_ROWID + " FROM " + DATABASE_TABLE + " WHERE " + KEY_NAME + "='" + user_login.toLowerCase() + "'";
        String sql2 = "SELECT " + KEY_ROWID + " FROM " + DATABASE_TABLE + " WHERE " + KEY_EMAIL + "='" + user_login.toLowerCase() + "'";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            return cursor;
        } else {
            Cursor cursor2 = db.rawQuery(sql2, null);
            if (cursor2.moveToFirst()) {
                return cursor2;
            }
        }
        return null;
    }

    public int updateDailyWaterValue(String user_login, int addWater) {
        SQLiteDatabase db = this.getWritableDatabase();
        Log.d("MyApp", "userLogin : " + user_login);
        ContentValues values = new ContentValues();
        values.put(KEY_DAILY, addWater);           // Adding water
        String selection;
        if (user_login.contains("@")) {
            selection = KEY_EMAIL + " = ?";
        } else {
            selection = KEY_NAME + " = ?";
        }
        String[] selectionArgs = {"" + user_login};

        // updating water_value row
        return db.update(DATABASE_TABLE, values, selection, selectionArgs);
    }

    public int updateUsername(String user_login, String username) {
        SQLiteDatabase db = this.getWritableDatabase();

        boolean flag = checkUsername(username);
        ContentValues values = new ContentValues();
        String selection = KEY_NAME + " = ?";
        String[] selectionArgs = {"" + user_login};
        if (flag)
            return 0;

        values.put(KEY_NAME, username);
        // updating username row
        return db.update(DATABASE_TABLE, values, selection, selectionArgs);
    }

    public boolean checkWaterGoal(int water_goal) {
        if (water_goal <= 0 || water_goal > 35) {
            return false;
        }
        return true;
    }

    public boolean checkEMail(String email) {
        String sql = "SELECT " + KEY_EMAIL + " FROM " + DATABASE_TABLE + " WHERE " + KEY_EMAIL + "='" + email + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            Log.d("MyApp", "Email is exist");
            return true;
        } else
            return false;
    }

    public boolean checkUsername(String username) {
        String sql = "SELECT " + KEY_NAME + " FROM " + DATABASE_TABLE + " WHERE " + KEY_NAME + "='" + username + "'";
        Log.d("MyApp", "SQL : " + sql);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            Log.d("MyApp", "Username is exist");
            return true;
        } else {
            Log.d("MyApp", "Username is NOT exist");
            return false;
        }
    }

    //Insert exact_day
    public int updateDay(int day, String user_login) {
               SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        String selection = KEY_NAME + " = ?";
        String[] selectionArgs = {"" + user_login};

        values.put(KEY_EXACT_DAY, day);
        Log.d("MyApp", "Day is updated : " + day);
        // updating username row
        //return db.update(DATABASE_TABLE, values, selection, selectionArgs);
        return db.update(DATABASE_TABLE, values, selection, selectionArgs);
    }


    //Getting the exact_day on DB
    public Cursor getDBDay(String user_login) {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT " + KEY_EXACT_DAY + " FROM " + DATABASE_TABLE + " WHERE " + KEY_EMAIL + "=" + "'" + user_login.toLowerCase() + "'";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            return cursor;
        } else {
            String sql2 = "SELECT " + KEY_EXACT_DAY + " FROM " + DATABASE_TABLE + " WHERE " + KEY_NAME + "=" + "'" + user_login.toLowerCase() + "'";
            Log.d("MyApp", "day sql : " + sql2);
            Cursor cursor2 = db.rawQuery(sql2, null);
            if (cursor2.moveToFirst()) {
                return cursor2;
            }
        }
        return null;
    }

    // Inserting new date
    public void insertDate(int id, int value, String date,int current_date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE_ID, id);                         // Date ID
        values.put(KEY_VALUE, value);                        // Date Value
        values.put(KEY_DATE, date);                         // Date
        values.put(KEY_CURRENT_DATE,current_date);

        Log.d("MyApp", "Date is created!");
        // Inserting Row
        db.insertWithOnConflict(DATABASE_TABLE_DATE, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close(); // Closing database connection
    }

    public int getDateCount(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor mCount= db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE_DATE +" WHERE " +KEY_DATE_ID+" = "+id, null);
        int count;
        if(mCount.moveToFirst()) {
            count = mCount.getInt(0);
            Log.d("MyApp", "Date Count : " + count);
        }else{
            return 0;
        }
        return count;
    }

    //Getting the date_value on User_date DB
    public Cursor getDateValue(int user_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        //String sql = "SELECT " + KEY_VALUE +","+KEY_CURRENT_DATE+" FROM " + DATABASE_TABLE_DATE + " WHERE " + KEY_DATE_ID + "=" + user_id;
        String sql = " SELECT "+ KEY_VALUE +","+KEY_CURRENT_DATE+" FROM "+ DATABASE_TABLE_DATE +" WHERE "+ KEY_DATE_ID + "=" + user_id+" ORDER BY "+KEY_DATE+" DESC LIMIT 7";
        Log.d("MyApp", "date value sql : "+sql);
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.moveToFirst()) {
            return cursor;
        }
        return null;
    }

    //Insert value on User_date
    public int updateCurrentValue(int value, int user_id,String now) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        String selection = KEY_DATE_ID + " = ? AND " + KEY_DATE +" = ?";
        String[] selectionArgs = {""+user_id,now};

        values.put(KEY_VALUE, value);
        Log.d("MyApp", "Day is updated : " + value);
        // updating username row
        //return db.update(DATABASE_TABLE, values, selection, selectionArgs);
        return db.update(DATABASE_TABLE_DATE, values, selection, selectionArgs);
    }


    public boolean checkDateValue(int user_id,String now) {
        String sql = "SELECT " + KEY_DATE + " FROM " + DATABASE_TABLE_DATE + " WHERE " + KEY_DATE_ID + "=" + user_id;
        Log.d("MyApp", "SQL : " + sql);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(sql, null);
        cursor.moveToFirst();
        Log.d("MyApp", "DB Date : " + cursor.getString(0) + "\nCurrent Now : " + now);

        while(cursor.moveToNext()) {
            if (cursor.getString(0).equals(now)) {
                //Log.d("MyApp", "DB Date : " + cursor.getString(0) + "\nCurrent Now : " + now);
                Log.d("MyApp", "date is exist and date : "+ cursor.getString(0));
                return true;
            }
        }
                Log.d("MyApp", "Current Now : "+now);
                Log.d("MyApp", "date is NOT exist");
                return false;
    }

    public boolean checkDateTableIDEmpty(int user_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT " + KEY_DATE_ID + " FROM " + DATABASE_TABLE_DATE + " WHERE " + KEY_DATE_ID + "=" + user_id;

       // String count = "SELECT count(*) FROM "+DATABASE_TABLE_DATE;
        Cursor mCursor = db.rawQuery(sql, null);
        if(mCursor.moveToFirst()){
            return true;
        }else{
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
        values.put(KEY_ROWID, user.getId());
        values.put(KEY_NAME, user.getUsername());           // User Name
        values.put(KEY_EMAIL, user.getEmail());             // User Email
        values.put(KEY_GENDER, user.getGender());           // User Gender
        values.put(KEY_AGE, user.getAge());                 // User Age
        values.put(KEY_COUNTRY, user.getCountry());         // User Country
        values.put(KEY_GOAL, user.getDaily_goal());          // User Daily Goal
        values.put(KEY_DAILY, user.getDaily_water());       // User Daily Water
        values.put(KEY_WEEKLY, user.getWeekly_water());     // User Weekly Water
        values.put(KEY_MONTLY, user.getMontly_water());     // User Montly Water
        values.put(KEY_YEARLY, user.getYearly_water());     // User Yearly Water

        // updating row
        return db.update(DATABASE_TABLE, values, KEY_ROWID + " = ?",
                new String[]{String.valueOf(user.getId())});
    }

    // Deleting single user
    public void deleteUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE, KEY_ROWID + " = ?",
                new String[]{String.valueOf(user.getId())});
        db.close();
    }

    // Deletes a particular user
    public boolean deleteUser(long rowId) {
        return db.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public boolean deleteAllUsers(int count) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean res = false;
        for (int i = 0; i < count; i++) {
            res = db.delete(DATABASE_TABLE, KEY_ROWID + "=" + i, null) > 0;
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