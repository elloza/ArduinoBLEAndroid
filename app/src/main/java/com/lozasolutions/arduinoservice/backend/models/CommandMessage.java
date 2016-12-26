package com.lozasolutions.arduinoservice.backend.models;

/**
 * Created by Loza on 26/12/2016.
 */

public class CommandMessage extends SocketMessage{

    private String command;
    private long timestamp;

    public CommandMessage(String command, long timestamp) {
        this.command = command;
        this.timestamp = timestamp;
    }
}
