package com.example.myapplicatio.aralash;//package com.example.myapplicatio;
//
//import android.app.Application;
//import android.arch.lifecycle.ViewModelProvider;
//import android.arch.persistence.room.Room;
//import android.support.v4.content.PermissionChecker;
//
//import com.example.myapplicatio.db.Data;
//import com.example.myapplicatio.db.task.TaskRepositor;
//import com.example.myapplicatio.db.task.TasksDao;
//
//import javax.inject.Singleton;
//
//import dagger.Module;
//import dagger.Provides;
//
//@Singleton
//@Module
//public class RoomModule {
//    private final Data database;
//
//    public RoomModule(Application application){
//        this.database = Room.databaseBuilder(
//                application,
//                Data.class,
//                "tasksentiry.db"
//        ).build();
//    }
//
//    @Provides
//    @Singleton
//    TaskRepositor repositor(TasksDao dao){
//        return new TaskRepositor(dao);
//    }
//
//    @Provides
//    @Singleton
//    TasksDao dao(Data database){
//        return database.wordDao();
//    }
//
//
//    @Singleton
//    @Provides
//    Data getDatabase(Application application){
//        return database;
//    }
//
//    @Singleton
//    @Provides
//    ViewModelProvider.Factory providerModelFactory(TaskRepositor repositor){
//        return new CustomViewModelProvider(repositor);
//    }
//}
