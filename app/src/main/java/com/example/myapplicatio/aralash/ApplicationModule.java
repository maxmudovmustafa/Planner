package com.example.myapplicatio.aralash;

import android.app.Application;

import dagger.Module;
import dagger.Provides;

@Module
class ApplicationModule {
    private final Application application;
    public ApplicationModule(RoomDemoApplication application) {
        this.application = application;
    }

    @Provides

    Application provide(){
        return application;
    }
}
