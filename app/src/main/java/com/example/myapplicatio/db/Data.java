package com.example.myapplicatio.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.example.myapplicatio.db.memo.ConvertImages;
import com.example.myapplicatio.db.memo.MemoDao;
import com.example.myapplicatio.db.memo.MemoEntity;
import com.example.myapplicatio.db.reminder.ReminderDao;
import com.example.myapplicatio.db.reminder.ReminderEntity;
import com.example.myapplicatio.db.task.TasksDao;
import com.example.myapplicatio.db.task.TasksEntiry;
import com.example.myapplicatio.db.user.UserDao;
import com.example.myapplicatio.db.user.UserEntity;

@Database(entities = {TasksEntiry.class, UserEntity.class, ReminderEntity.class, MemoEntity.class}, version = 5)
@TypeConverters(ConvertImages.class)
public abstract class Data extends RoomDatabase {

    public abstract TasksDao wordDao();
    public abstract UserDao userDao();
    public abstract ReminderDao remindrDao();
    public abstract MemoDao memoDao();
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
