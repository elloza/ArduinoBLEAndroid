package com.lozasolutions.arduinoservice.di.components;

import com.lozasolutions.arduinoservice.BaseApplication;
import com.lozasolutions.arduinoservice.Monitor;
import com.lozasolutions.arduinoservice.di.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Loza on 25/12/2016.
 */

@Singleton
@Component( modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(BaseApplication baseApplication);
    void inject(Monitor activity);
}