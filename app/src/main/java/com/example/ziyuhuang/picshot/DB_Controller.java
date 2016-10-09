package com.example.ziyuhuang.picshot;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by ziyuhuang on 10/8/16.
 * sqlite database
 */

public class DB_Controller extends SQLiteOpenHelper{
    public DB_Controller(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, "Images.db", factory, version);
    }

    /**
     * create a table name IMAGES
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE IMAGES(ID INTEGER PRIMARY KEY AUTOINCREMENT, URL TEXT);";
        db.execSQL(query);
    }

    /**
     * save the image url in database
     * @param imageUrl
     */
    public void insert(String imageUrl){
        ContentValues contentValues = new ContentValues();
        contentValues.put("URL", imageUrl);
        this.getWritableDatabase().insertOrThrow("IMAGES", "", contentValues);
    }

    /**
     * show all the records of the images url saved in the database
     * @return
     */
    public String list(){
        Log.v("testing", "knock knock");
        Cursor cursor = this.getReadableDatabase().rawQuery("SELECT * FROM IMAGES", null);
        StringBuilder stringBuilder = new StringBuilder();
        while(cursor.moveToNext()){
            stringBuilder.append(cursor.getString(1) + "\n");
        }

        if(stringBuilder.toString().equals(""))
            Log.v("testing", "failed");

        return stringBuilder.toString();
    }

    /**
     * delete all the data in the database
     */
    public void deleteAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        // db.delete(TABLE_NAME,null,null);
        //db.execSQL("delete * from"+ TABLE_NAME);
        db.execSQL("delete from " + "IMAGES");
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS IMAGES;");
    }
}
