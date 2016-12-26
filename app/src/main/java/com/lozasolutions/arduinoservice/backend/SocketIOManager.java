package com.lozasolutions.arduinoservice.backend;

import com.google.gson.Gson;
import com.jakewharton.rxrelay.PublishRelay;
import com.lozasolutions.arduinoservice.backend.models.CommandMessage;
import com.lozasolutions.arduinoservice.backend.models.SocketMessage;
import com.lozasolutions.arduinoservice.utils.Constants;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import rx.Observable;

/**
 * Created by Loza on 26/12/2016.
 */

public class SocketIOManager implements SocketManager {

    private static final String COMMAND_MESSAGE = "command_message";
    private PublishRelay<Boolean> connectionRelay = PublishRelay.create();
    private PublishRelay<SocketMessage> messageRelay = PublishRelay.create();
    Gson gson;

    private Socket socket;
    private boolean connectionWithBackend;

    public SocketIOManager(Gson gson) {

        this.gson = gson;
        PublishRelay.create().toSerialized();

    }

    @Override
    public void sendMessage(Object socketMessage) {
        socket.send(socketMessage);

    }

    @Override
    public boolean isConnected() {
        return connectionWithBackend;
    }

    @Override
    public void connect() {

        try {
            socket  = IO.socket(Constants.SOCKET_URL);
            socket.on(Socket.EVENT_CONNECT, args -> {
                connectionWithBackend = true;
                connectionRelay.call(true);
            })
                    .on(COMMAND_MESSAGE, args -> {
                        messageRelay.call(gson.fromJson((String) args[0], CommandMessage.class));
                    })
                    .on(Socket.EVENT_DISCONNECT, args -> {
                        connectionRelay.call(false);
                        connectionWithBackend = false;
                    });
            socket.connect();


        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void disconnect() {
        socket.disconnect();
    }

    @Override
    public Observable<SocketMessage> getMessagesObservable() {
        return messageRelay.asObservable();
    }

    @Override
    public Observable<Boolean> getConnectionObservable() {
        return connectionRelay.asObservable();
    }


}
