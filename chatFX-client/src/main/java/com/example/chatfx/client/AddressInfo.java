package com.example.chatfx.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class AddressInfo {
    public static InetAddress IP;

    //服务器地址
    static {
        try {
            IP = InetAddress.getByName("10.62.49.217");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public static int localPort = 8888;
    public static int serverPort = 9999;
}
