package com.larry.cloundusb.cloundusb.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.larry.cloundusb.cloundusb.baseclass.FileHistoryInfo;
import com.larry.cloundusb.cloundusb.util.CommonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LARRYSEA on 2016/6/22.
 *
 * 发送文件接收文件的数据库
 *
 *
 */
public class DB_AceClound extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "AceClound.db";
    private final static int DATABASE_VERSION = 1;
    private final static String TABLE_NAME = "FileTFAccount";

    public DB_AceClound(Context context) {
        // TODO Auto-generated constructor stub
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //初始化数据库
        CreateTableTF(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //创建数据库表
    private void CreateTableTF(SQLiteDatabase db) {
        String sql = "CREATE TABLE if not exists " +
                TABLE_NAME + "(" +
                "id INTEGER PRIMARY KEY," +
                "people_pic  INTEGER," +
                "people_name varchar(50), " +
                "file_path varchar(100)," +
                "file_name varchar(50)," +
                "file_size varchar(10)," +                //文件大小 为格式化后的文件大小（3k）
                "time varchar(50)," +
                "TFtype INTEGER," +                            //区分是接收还是发送 (接收1 发送0)
                "TFPlatform INTEGER" +
                ")";
        db.execSQL(sql);
    }


    /*
    *
    * 2 是查询所有记录
    *
    *
    * 0  是查询发送方历史记录
    *
    *
    * 1  是查询接收方历史记录
    *
    * */
    //获取所有记录
    public List<FileHistoryInfo> GetHistoryTFInfo(int type) {
        List<FileHistoryInfo> tflist = new ArrayList<FileHistoryInfo>();
        String selsql = null;
        switch (type) {
            case 2:
                selsql = "select *  from " + TABLE_NAME;
                break;
            case 0:
                selsql = "select * from " + TABLE_NAME + " where TFtype= 0";
                break;

            case 1:
                selsql = "select * from " + TABLE_NAME + " where TFtype= 1";
                break;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selsql, null);

        while (cursor.moveToNext()) {
            FileHistoryInfo tfinfo = new FileHistoryInfo();
            tfinfo.setFileid(cursor.getInt(0));
            tfinfo.SetItemValue(cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    CommonUtil.FormatTimeToday(Long.parseLong(cursor.getString(6))),
                    cursor.getInt(7),
                    cursor.getInt(8));
                    tflist.add(tfinfo);
        }
        return tflist;
    }

    //保存单个记录
    public boolean SaveTFInfo(FileHistoryInfo TFinfo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("people_pic", TFinfo.getTFUserPicId());
        cv.put("people_name", TFinfo.getTFUserName());
        cv.put("file_path", TFinfo.getTFFilePath());
        cv.put("file_name", TFinfo.getTFFileName());
        cv.put("file_size", TFinfo.getTFFileSize());
        cv.put("time", TFinfo.getTFTime());
        cv.put("TFtype", TFinfo.getTFtype());
        cv.put("TFPlatform", TFinfo.getTFtype());
        long row = db.insert(TABLE_NAME, null, cv);
        if (row > 0)
            return true;
        else
            return false;
    }

    //删除单个记录
    public void DelTFInfo(int Infoid) {
        SQLiteDatabase db = this.getWritableDatabase();
        String Delsql = "delete from " + TABLE_NAME + " where id=" + Infoid;
        db.execSQL(Delsql);
    }

    //清空记录
    public void ClearAllInfo() {
        SQLiteDatabase db = this.getWritableDatabase();
        String Delsql = "delete from " + TABLE_NAME;
        db.execSQL(Delsql);
    }

}
