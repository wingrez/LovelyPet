package com.wingrez.lovelypet.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    // 数据库名
    public static final String DB_NAME = "lovelypet.db";
    // 数据库表名
    public static final String TABLE_NAME = "user";
    // 数据库版本号
    public static final int DB_VERSION = 1;

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
        String sql = "CREATE TABLE " + TABLE_NAME + "( " +
                ID + " int(11) NOT NULL AUTO_INCREMENT, " +
                PETNAME + " varchar(255) NOT NULL, " +
                HOSTNAME + " varchar(255) NOT NULL, " +
                AGE + " int(11) NOT NULL, " +
                GENDER + " int(11) NOT NULL, " +
                KIND+" int(11) NOT NULL"+
                LEVEL + " int(11) NOT NULL, " +
                EXPERIENCE + " int(11) NOT NULL, " +
                HUNGRY + " int(11) NOT NULL, " +
                CLEANESS + "int(11) NOT NULL, " +
                HAPPINESS + " int(11) NOT NULL, " +
                MOOD + " int(11) NOT NULL, " +
                STATE + " int(11) NOT NULL, " +
                " PRIMARY KEY (\'id\')" +
                ")";

        db.execSQL(sql);
    }

    // 当数据库版本更新执行该方法
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}