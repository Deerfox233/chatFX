package com.example.chatfx.client;

import com.example.chatfx.bean.Message;
import com.example.chatfx.bean.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.Socket;

public class Client {
    private static User user;
    private static Socket socket;
    private static ObjectInputStream objectInputStream;
    private static ObjectOutputStream objectOutputStream;
    private static DatagramSocket datagramSocket;
    private static Message message;

    //TCP 协议
    public Client(User user) throws IOException, ClassNotFoundException {
        Client.user = user;
        socket = new Socket(AddressInfo.IP, AddressInfo.serverPort);
        //OutputStream 和 InputStream 顺序很重要
        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        if (datagramSocket == null || !datagramSocket.isBound()) {
            datagramSocket = new DatagramSocket(AddressInfo.localPort);
        }

        //准备 ClientSend 类
        ClientSend.setObjectOutputStream(objectOutputStream);
        ClientSend.setObjectInputStream(objectInputStream);
        ClientSend.setDatagramSocket(datagramSocket);
        Thread clientReceive = new Thread(new ClientSend.ClientReceive());
        clientReceive.setDaemon(true);
        clientReceive.start();

        //发送用户信息用于登录或注册
        message = ClientSend.sendUser(user);
    }

    public static Message getMessage() {
        return message;
    }

    public static void setMessage(Message message) {
        Client.message = message;
    }

    public void close() throws IOException {
        objectOutputStream.close();
        objectInputStream.close();
        socket.close();
    }

    public static void tick() {
        System.out.println("tick");
    }
}
