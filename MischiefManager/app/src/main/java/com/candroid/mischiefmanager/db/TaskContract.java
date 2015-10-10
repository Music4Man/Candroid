package com.candroid.mischiefmanager.db;

/**
 * Created by Firdous on 2015-09-19.
 */

import android.provider.BaseColumns;

public class TaskContract {
    public static final String DB_NAME = "com.example.TodoList.db.tasks";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "ToDo";

    public class Columns {
        public static final String TASK = "entry";
        public static final String DATE = "date";
        public static final String TIME = "time";
        public static final String USER = "username";
        public static final String _ID = BaseColumns._ID;
    }
}

