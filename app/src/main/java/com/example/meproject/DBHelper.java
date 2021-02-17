package com.example.meproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context){
        super(context, "UserDetails.db", null, 1);
    }
    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("create table if not exists users(email TEXT primary key, password TEXT not null, fullName TEXT not null, userType TEXT not null, balance REAL not null default 0, points INTEGER default 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int i, int i1) {
        MyDB.execSQL("drop table if exists users");
    }
    public Boolean insertUsersData(String email, String password, String fullName, String userType, double balance)
    {
        SQLiteDatabase MYDB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("fullName", fullName);
        contentValues.put("userType", userType);
        contentValues.put("balance", balance);
        contentValues.put("points", 0);
        long result=MYDB.insert("users", null, contentValues);
        if (result==-1)
            return false;
        else
            return true;
    }
    public Boolean updateBalanceAndPoints(String email, double balance, double points)
    {
        SQLiteDatabase MYDB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("balance", balance);
        contentValues.put("points", points);
        long result=MYDB.update("users", contentValues, "email=?", new String[]{email});
        if (result==-1)
            return false;
        else
            return true;
    }

    public Boolean updateBalance(String email, double balance)
    {
        SQLiteDatabase MYDB=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("balance", balance);
        long result=MYDB.update("users", contentValues, "email=?", new String[]{email});
        if (result==-1)
            return false;
        else
            return true;
    }

    public Boolean checkUserExist(String email){
        SQLiteDatabase MYDB=this.getWritableDatabase();
        Cursor cursor=MYDB.rawQuery("select * from users where email = ?", new String[]{email});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public Boolean checkEmailAndPassword(String email, String password){
        SQLiteDatabase MYDB=this.getWritableDatabase();
        Cursor cursor=MYDB.rawQuery("select * from users where email = ? and password= ?", new String[]{email, password});
        if(cursor.getCount()>0)
            return true;
        else
            return false;
    }

    public String getUserType(String email){
        String userType="";
        SQLiteDatabase MYDB=this.getWritableDatabase();
        Cursor cursor=MYDB.rawQuery("select * from users where email = ?", new String[]{email});
        if(cursor.moveToFirst()){
            userType = cursor.getString(cursor.getColumnIndex("userType"));
        }
        return userType;
    }

    public Cursor getData(String email){
        SQLiteDatabase MYDB=this.getWritableDatabase();
        Cursor cursor=MYDB.rawQuery("select * from users where email = ?", new String[]{email});
        cursor.moveToFirst();
        return cursor;
    }


}
