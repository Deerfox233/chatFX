package com.example.chatfx.client;

import com.example.chatfx.bean.Message;
import com.example.chatfx.bean.MessageType;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ClientSend {
    private static ObjectOutputStream objectOutputStream;
    private static ObjectInputStream objectInputStream;
    private static DatagramSocket datagramSocket;
    private static Message message;

    public static void setDatagramSocket(DatagramSocket datagramSocket) {
        ClientSend.datagramSocket = datagramSocket;
    }

    public static void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
        ClientSend.objectOutputStream = objectOutputStream;
    }

    public static void setObjectInputStream(ObjectInputStream objectInputStream) {
        ClientSend.objectInputStream = objectInputStream;
    }

    public static Message sendUser(Object object) throws IOException, ClassNotFoundException {
        objectOutputStream.writeObject(object);

        return (Message) objectInputStream.readObject();
    }

    //UDP 协议
    public static void sendChatMessage(Message message) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream);
        oos.writeObject(message);
        oos.flush();
        byte[] b = byteArrayOutputStream.toByteArray();
        DatagramPacket datagramPacket = new DatagramPacket(b, 0, b.length, AddressInfo.IP, AddressInfo.serverPort);
        datagramSocket.send(datagramPacket);
    }

    public static void sendDisconnection() throws IOException {
        Message messageDisconnection = new Message();
        messageDisconnection.setType(MessageType.DISCONNECTION);
        objectOutputStream.writeObject(messageDisconnection);
    }

    static class ClientReceive implements Runnable {
        ByteArrayInputStream byteArrayInputStream;
        ObjectInputStream objectInputStream;
        @Override
        public void run() {
            while (true) {
                try {
                    byte[] b = new byte[1024 * 1024];
                    DatagramPacket datagramPacket = new DatagramPacket(b, 0, b.length);
                    datagramSocket.receive(datagramPacket);

                    byteArrayInputStream = new ByteArrayInputStream(b);
                    objectInputStream = new ObjectInputStream(byteArrayInputStream);

                    message = (Message) objectInputStream.readObject();
                    System.out.println("[" + message.getSenderName() + " to " + message.getReceiverName() + "] " + message.getContent());
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Message getMessage() {
        return message;
    }

    public static void setMessage(Message message) {
        ClientSend.message = message;
    }
}
