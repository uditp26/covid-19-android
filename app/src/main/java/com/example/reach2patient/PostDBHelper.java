package com.example.reach2patient;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class PostDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Posts.db";
    private static final int DATABASE_VERSION = 1;

    public static final String BACKUP_TABLE_NAME = "backup";
    public static final String BACKUP_COLUMN_PID = "pid";
    public static final String BACKUP_COLUMN_BODY = "body";
    public static final String BACKUP_COLUMN_PHONE = "phone";
    public static final String BACKUP_COLUMN_TIMESTAMP = "timestamp";

    public PostDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + BACKUP_TABLE_NAME + "(" +
                BACKUP_COLUMN_PID + " INTEGER PRIMARY KEY, " +
                BACKUP_COLUMN_BODY + " TEXT, " +
                BACKUP_COLUMN_PHONE + " TEXT, " +
                BACKUP_COLUMN_TIMESTAMP + " TEXT)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + BACKUP_TABLE_NAME);
        onCreate(db);
    }

    public void backupPost(int pid, String body, String phone, String timestamp){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(BACKUP_COLUMN_PID, pid);
        contentValues.put(BACKUP_COLUMN_BODY, body);
        contentValues.put(BACKUP_COLUMN_PHONE, phone);
        contentValues.put(BACKUP_COLUMN_TIMESTAMP, timestamp);
        db.insert(BACKUP_TABLE_NAME, null, contentValues);
    }

    public Cursor getPostsFromBackup(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM " + BACKUP_TABLE_NAME + " ORDER BY " + BACKUP_COLUMN_PID + " DESC", null );
        return res;
    }

}
