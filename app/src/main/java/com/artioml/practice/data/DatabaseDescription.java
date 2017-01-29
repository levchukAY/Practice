package com.artioml.practice.data;

import android.provider.BaseColumns;


/**
 * Created by Artiom L on 28.01.2017.
 */

public class DatabaseDescription {

    public static final class Community implements BaseColumns {
        public final static String TABLE_NAME = "Community";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_LOGIN = "login";
        public final static String COLUMN_PASSWORD = "password";
    }

    public static final class History implements BaseColumns {
        public final static String TABLE_NAME = "History";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_PUNCH_TYPE = "punchType";
        public final static String COLUMN_HAND = "hand";
        public final static String COLUMN_GLOVES = "gloves";
        public final static String COLUMN_GLOVES_WEIGHT = "glovesWeight";
        public final static String COLUMN_POSITION = "position";
        public final static String COLUMN_SPEED = "speed";
        public final static String COLUMN_REACTION = "reaction";
        public final static String COLUMN_ACCELERATION = "acceleration";
        public final static String COLUMN_DATE = "date";
    }

}