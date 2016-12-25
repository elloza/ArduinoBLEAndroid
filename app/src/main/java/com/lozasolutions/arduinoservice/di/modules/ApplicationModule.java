package com.lozasolutions.arduinoservice.di.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Loza on 25/12/2016.
 */

@Module
public final class ApplicationModule {

    private final Context mContext;

    public ApplicationModule(Context context) {
        mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }
}
