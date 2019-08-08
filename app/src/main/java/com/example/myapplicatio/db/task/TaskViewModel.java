package com.example.myapplicatio.db.task;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.AsyncTask;

import java.util.List;

public class TaskViewModel extends ViewModel {
    public TaskRepositor repositor;

    public TaskViewModel(TaskRepositor model){
        this.repositor = model;
    }

    public LiveData<List<TasksEntiry>> getAllTasks(){
        return repositor.getAllTasks();
    }

    public void deleteTask(TasksEntiry taskRepositor){
        DeleteTask delete = new DeleteTask();
        delete.doInBackground(taskRepositor);
    }

    private class DeleteTask extends AsyncTask<TasksEntiry, Void, Void>{
        @Override
        protected Void doInBackground(TasksEntiry... tasksEntiries) {
            repositor.delete(tasksEntiries[0]);
            return null;

        }
    }
}
