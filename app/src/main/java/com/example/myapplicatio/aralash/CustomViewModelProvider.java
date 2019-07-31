package com.example.myapplicatio.aralash;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.myapplicatio.db.TaskRepositor;
import com.example.myapplicatio.db.TaskViewModel;

public class CustomViewModelProvider implements ViewModelProvider.Factory {

    public final TaskRepositor repositor;

    public CustomViewModelProvider(TaskRepositor repositor) {
        this.repositor = repositor;
    }


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(TaskViewModel.class))
            return (T) new TaskViewModel(repositor);

//        else if (modelClass.isAssignableFrom(ListCollectionItem.class))
//            return (T) new ListCollectionItem(repositor);
//        else if (modelClass.isAssignableFrom(NewListItemMolule.class))
//            return (T) new NewListItemMolule(repositor);

        else {
            throw new IllegalArgumentException("View Not Found");
        }
    }
}
