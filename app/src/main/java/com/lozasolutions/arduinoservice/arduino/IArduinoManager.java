package com.lozasolutions.arduinoservice.arduino;

import com.lozasolutions.arduinoservice.arduino.models.ArduinoMessage;

import rx.Observable;

/**
 * Created by Loza on 26/12/2016.
 */

public interface IArduinoManager {

    void sendMessage(ArduinoMessage message);

    Observable<ArduinoMessage> getArduinoMessageObservable();

    Observable<Boolean> getConnectionObservable();

    boolean isConnected();

    void connect();

    void disconnect();

}
