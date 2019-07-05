package com.wingrez.lovelypet.sqlite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.wingrez.lovelypet.bean.PetBean;

import java.util.ArrayList;
import java.util.List;

public class DBOp {

    private SQLiteDatabase db;

    public DBOp(SQLiteDatabase db){
        this.db=db;
    }

    // 表名
    // null。数据库如果插入的数据为null，会引起数据库不稳定。为了防止崩溃，需要传入第二个参数要求的对象
    // 如果插入的数据不为null，没有必要传入第二个参数避免崩溃，所以为null
    // 插入的数据
    public void insertData(PetBean petBean) {
        ContentValues data = new ContentValues();
        data.put("petname", petBean.getPetName());
        data.put("hostname", petBean.getHostNmae());
        data.put("age", petBean.getAge());
        data.put("gender", petBean.getGender());
        data.put("kind", petBean.getKind());
        data.put("level", petBean.getLevel());
        data.put("experence", petBean.getExperience());
        data.put("hungry", petBean.getHungry());
        data.put("cleaness", petBean.getCleaness());
        data.put("happiness", petBean.getHappiness());
        data.put("mood", petBean.getMood());
        data.put("state", petBean.getState());

        db.insert(DBHelper.TABLE_NAME, null, data);
    }

    // 表名
    // 删除条件
    // 满足删除的值
    public void deleteData(int id) {
        int count = db.delete(DBHelper.TABLE_NAME, DBHelper.ID + " = ?", new String[]{String.valueOf(id)});
    }

    // 表名
    // 修改后的数据
    // 修改条件
    // 满足修改的值
    public void updateData(int id, String key, String value) {
        ContentValues data = new ContentValues();
        data.put(key, value);
        int count = db.update(DBHelper.TABLE_NAME, data, DBHelper.ID + " = ?", new String[]{String.valueOf(id)});
    }

    // 表名
    // 查询字段
    // 查询条件
    // 满足查询的值
    // 分组
    // 分组筛选关键字
    // 排序
    public List<String> queryData(int id) {
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, DBHelper.ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        int petNameIndex = cursor.getColumnIndex(DBHelper.PETNAME);
        int hostNameIndex = cursor.getColumnIndex(DBHelper.HOSTNAME);
        int ageIndex = cursor.getColumnIndex(DBHelper.AGE);
        int genderIndex = cursor.getColumnIndex(DBHelper.GENDER);
        int kindIndex = cursor.getColumnIndex(DBHelper.KIND);
        int levelIndex = cursor.getColumnIndex(DBHelper.LEVEL);
        int experienceIndex = cursor.getColumnIndex(DBHelper.EXPERIENCE);
        int hungryIndex = cursor.getColumnIndex(DBHelper.HUNGRY);
        int cleanessIndex = cursor.getColumnIndex(DBHelper.CLEANESS);
        int happinessIndex = cursor.getColumnIndex(DBHelper.HAPPINESS);
        int moodIndex = cursor.getColumnIndex(DBHelper.MOOD);
        int stateIndex = cursor.getColumnIndex(DBHelper.STATE);

        List<String> list = new ArrayList<>();
        String str = "";

        Log.e("hhh", "hhh");
        while (cursor.moveToNext()) {
            String petname = cursor.getString(petNameIndex);
            String hostName = cursor.getString(hostNameIndex);
            String age = cursor.getString(ageIndex);
            String gender = cursor.getString(genderIndex);
            String kind = cursor.getString(kindIndex);
            String level = cursor.getString(levelIndex);
            String experience = cursor.getString(experienceIndex);
            String hunry = cursor.getString(hungryIndex);
            String cleaness = cursor.getString(cleanessIndex);
            String happiness = cursor.getString(happinessIndex);
            String mood = cursor.getString(moodIndex);
            String state = cursor.getString(stateIndex);
            str += petname + "#" + hostName + "#" + age + "#" + gender + "#" + kind + "#" + level + "#" + experience + "#" + hunry + "#" + cleaness + "#" + happiness + "#" + mood + state;
            Log.e("aaa", str);
            list.add(str);
        }

        return list;

    }

    public String query(int id, String key){
        Cursor cursor = db.query(DBHelper.TABLE_NAME, null, DBHelper.ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);
        int index=cursor.getColumnIndex(key);
        while (cursor.moveToNext()) {
            return cursor.getString(index);
        }
        return null;
    }

}
