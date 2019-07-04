package com.wingrez.lovelypet.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // 数据库名
    public static final String DB_NAME = "lovelypet.db";
    // 数据库表名
    public static final String TABLE_NAME = "pet";
    // 数据库版本号
    public static final int DB_VERSION = 2;

    public static final String ID = "id";
    public static final String PETNAME = "petname";
    public static final String HOSTNAME = "hostname";
    public static final String AGE = "age";
    public static final String GENDER = "gender";
    public static final String KIND="kind";
    public static final String LEVEL = "level";
    public static final String EXPERIENCE = "experence";
    public static final String HUNGRY = "hungry";
    public static final String CLEANESS = "cleaness";
    public static final String HAPPINESS = "happiness";
    public static final String MOOD = "mood";
    public static final String STATE = "state";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    // 当数据库文件创建时，执行初始化操作，并且只执行一次
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 建表
        String sql = "create table " + TABLE_NAME + "( " +
                ID + " integer not null primary key autoincrement, " +
                PETNAME + " varchar not null, " +
                HOSTNAME + " varchar not null, " +
                AGE + " integer not null, " +
                GENDER + " integer not null, " +
                KIND+" integer not null, "+
                LEVEL + " integer not null, " +
                EXPERIENCE + " integer not null, " +
                HUNGRY + " integer not null, " +
                CLEANESS + " integer not null, " +
                HAPPINESS + " integer not null, " +
                MOOD + " integer not null, " +
                STATE + " integer not null )";

//        String sql = "create table " +
//                TABLE_NAME +
//                "(_id integer primary key autoincrement, " +
//                PETNAME + " varchar, " +
//                AGE + " varchar"
//                + ")";

        db.execSQL(sql);
    }

    // 当数据库版本更新执行该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}