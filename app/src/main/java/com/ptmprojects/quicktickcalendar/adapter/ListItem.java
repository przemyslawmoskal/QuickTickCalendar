package com.ptmprojects.quicktickcalendar.adapter;

public abstract class ListItem {

    public static final int TYPE_HEADER = 0;
    public static final int TYPE_TASK = 1;

    abstract public int getType();

}
