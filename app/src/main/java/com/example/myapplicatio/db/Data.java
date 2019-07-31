package com.example.myapplicatio.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {TasksEntiry.class, UserEntity.class, ReminderEntity.class}, version = 4)
public abstract class Data extends RoomDatabase {

    public abstract TasksDao wordDao();
    public abstract UserDao userDao();
    public abstract ReminderDao remindrDao();
    private static Data INSTANCE;

    public static Data getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Data.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            Data.class, "tasksentiry")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }


    public static void destroyInstance() {
        INSTANCE = null;
    }
}
