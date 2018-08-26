package com.music_mind.readr.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class ReviewDbHelper extends SQLiteOpenHelper {

    public ReviewDbHelper(Context context) {
        super(context, ReviewContract.DB_NAME, null, ReviewContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + ReviewContract.ReviewEntry.TABLE_NAME + " ( " +
                ReviewContract.ReviewEntry.COLUMN_NAME_ENTRY_ID + " TEXT PRIMARY KEY, " +
                ReviewContract.ReviewEntry.COLUMN_NAME_USER_ID + " TEXT, " +
                ReviewContract.ReviewEntry.COLUMN_NAME_TITLE + " TEXT NOT NULL, " +
                ReviewContract.ReviewEntry.COLUMN_NAME_TEXT + " TEXT NOT NULL, " +
                ReviewContract.ReviewEntry.COLUMN_NAME_QUOTE + " TEXT NOT NULL, " +
                ReviewContract.ReviewEntry.COLUMN_NAME_RATING + " INTEGER );";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ReviewContract.ReviewEntry.TABLE_NAME);
        onCreate(db);
    }
}
