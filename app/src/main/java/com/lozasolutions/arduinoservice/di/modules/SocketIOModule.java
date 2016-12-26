package com.lozasolutions.arduinoservice.di.modules;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.lozasolutions.arduinoservice.backend.SocketIOManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.socket.client.Socket;

/**
 * Created by Loza on 25/12/2016.
 */

@Module
public final class SocketIOModule {


    @Singleton
    @Provides
    SocketIOManager providesSocketManagerI(Gson gson){
        return new SocketIOManager(gson);

    }

}
