package com.lozasolutions.arduinoservice;

import android.app.Application;

import com.lozasolutions.arduinoservice.di.components.ApplicationComponent;
import com.lozasolutions.arduinoservice.di.components.DaggerApplicationComponent;
import com.lozasolutions.arduinoservice.di.modules.ApplicationModule;


/**
 * Created by Loza on 25/12/2016.
 */

public class BaseApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(getApplicationContext())).build();

    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }

}
