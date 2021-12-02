package com.example.chatfx.bean;

import java.io.Serializable;
import java.net.InetAddress;

public class User implements Serializable {
    private String name;
    private String password;
    private InetAddress clientIP;
    private int clientPort;
    private boolean isRegistered;

    public User(String name, String password, InetAddress IP, int clientPort) {
        this.name = name;
        this.password = password;
        this.clientIP = IP;
        this.clientPort = clientPort;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public InetAddress getClientIP() {
        return clientIP;
    }

    public void setClientIP(InetAddress clientIP) {
        this.clientIP = clientIP;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRegistered() {
        return isRegistered;
    }

    public void setRegistered(boolean registered) {
        this.isRegistered = registered;
    }
}
