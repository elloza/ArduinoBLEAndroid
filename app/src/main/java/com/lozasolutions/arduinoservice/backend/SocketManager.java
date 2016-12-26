package com.lozasolutions.arduinoservice.backend;


import com.lozasolutions.arduinoservice.backend.models.SocketMessage;

import rx.Observable;

/**
 * Created by Loza on 26/12/2016.
 */

public interface SocketManager {

    void sendMessage(Object socketMessage);

    boolean isConnected();

    void connect();

    void disconnect();

    Observable<SocketMessage> getMessagesObservable();

    Observable<Boolean> getConnectionObservable();

}
