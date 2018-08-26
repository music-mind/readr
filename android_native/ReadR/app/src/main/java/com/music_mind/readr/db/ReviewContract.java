package com.music_mind.readr.db;

import android.provider.BaseColumns;

public class ReviewContract {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
        public static final String DB_NAME = "com.music_mind.readr.db";
        public static final int DB_VERSION = 1;

    /* Inner class that defines the table contents */
    public static class ReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "reviews";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_ENTRY_ID = "entry_id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_QUOTE = "quote";
        public static final String COLUMN_NAME_RATING = "rating";
    }
}
