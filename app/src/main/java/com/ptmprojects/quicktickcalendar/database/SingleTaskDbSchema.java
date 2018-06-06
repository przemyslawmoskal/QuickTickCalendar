package com.ptmprojects.quicktickcalendar.database;

public class SingleTaskDbSchema {
    public static final class SingleTaskTable {
        public static final String NAME = "taska";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String DATE = "date";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String IS_DONE = "isDone";
            public static final String ALARM = "alarm";
            public static final String LOCATION = "location";
        }
    }
}
