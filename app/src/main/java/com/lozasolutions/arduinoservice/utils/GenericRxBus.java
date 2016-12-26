package com.lozasolutions.arduinoservice.utils;

import com.jakewharton.rxrelay.PublishRelay;
import com.jakewharton.rxrelay.SerializedRelay;

import rx.Observable;

/**
 * Created by Loza on 26/12/2016.
 */

public class GenericRxBus {

    public GenericRxBus() {
        this._bus = PublishRelay.create().toSerialized();
    }

    private final SerializedRelay<Object, Object> _bus;

    public void post(Object o) {
        _bus.call(o);
    }

    public Observable<Object> asObservable() {
        return _bus.onBackpressureLatest();
    }

}
