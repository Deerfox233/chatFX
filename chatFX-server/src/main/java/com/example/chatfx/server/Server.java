package com.example.chatfx.server;

import com.example.chatfx.bean.Message;
import com.example.chatfx.bean.MessageType;
import com.example.chatfx.bean.User;
import com.example.chatfx.db.dao.UserDaoImplement;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Server {
    private static ServerSocket serverSocket;
    private static Socket socket;
    private static int port = 9999;
    private static ObjectInputStream tempObjectInputStream;
    private static ObjectOutputStream tempObjectOutputStream;
    private static DatagramSocket datagramSocket;
    private static HashMap<User, Thread> threads;

    public Server() throws IOException {
        serverSocket = new ServerSocket(port);
        datagramSocket = new DatagramSocket(port);
        threads = new HashMap<>();
    }

    public void startServer() throws IOException, ClassNotFoundException, SQLException {
        System.out.println("[Server] " + "start");
        while (true) {
            socket = serverSocket.accept();
            System.out.println("[Server] " + "connection received");
            tempObjectInputStream = new ObjectInputStream(socket.getInputStream());
            tempObjectOutputStream = new ObjectOutputStream(socket.getOutputStream());

            //处理登录或注册
            User user = (User) tempObjectInputStream.readObject();
            UserDaoImplement userDaoImplement = new UserDaoImplement();
            Message message = new Message();
            if (user.isRegistered()) {      //用户准备登录
                int state = userDaoImplement.select(user);
                if (state == -1) {
                    message.setContent("-1");
                } else if (state == 1) {
                    message.setContent("1");
                } else {
                    message.setContent("0");
                    System.out.println("[Server] welcome " + user.getName());
                    //开启线程
                    Thread thread = new Thread(new ServerThread(user, tempObjectInputStream, tempObjectOutputStream,datagramSocket));
                    thread.start();
                    threads.put(user, thread);
                }
            } else {            //用户准备注册
                if (userDaoImplement.select(user) == -1) {
                    //用户未注册
                    message.setContent("-1");
                    userDaoImplement.insert(user);
                } else {
                    //用户已注册
                    message.setContent("0");
                    System.out.println("[Server] " + user.getName() + " has registered");
                }
            }
            tempObjectOutputStream.writeObject(message);
        }
    }

    public void close() throws IOException {
        tempObjectOutputStream.close();
        tempObjectInputStream.close();
        socket.close();
        serverSocket.close();
    }

    public static void tick() {
        System.out.println("tick");
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException, SQLException {
        new Server().startServer();
    }

    class ServerThread implements Runnable {
        private User user;
        private ObjectInputStream objectInputStream;
        private ObjectOutputStream objectOutputStream;
        private DatagramSocket datagramSocket;

        public ServerThread(User user,
                            ObjectInputStream objectInputStream,
                            ObjectOutputStream objectOutputStream,
                            DatagramSocket datagramSocket) {
            this.user = user;
            this.objectInputStream = objectInputStream;
            this.objectOutputStream = objectOutputStream;
            this.datagramSocket = datagramSocket;
        }

        public void close() throws IOException {
            objectInputStream.close();
            objectOutputStream.close();
        }

        //专门接收聊天消息 UDP
        //这是内部类中的内部类
        class ChatThread implements Runnable {
            //这些东西用来使对象能够以字节数组的形式流通于 UDP 协议的通信中
            ByteArrayInputStream byteArrayInputStream;
            ObjectInputStream objectInputStream;
            ByteArrayOutputStream byteArrayOutputStream;
            ObjectOutputStream objectOutputStream;

            @Override
            public void run() {
                while (true) {
                    try {
                        byte[] b = new byte[1024 * 1024];
                        DatagramPacket datagramPacket = new DatagramPacket(b, 0, b.length);
                        datagramSocket.receive(datagramPacket);
                        byteArrayInputStream = new ByteArrayInputStream(b);
                        objectInputStream = new ObjectInputStream(byteArrayInputStream);

                        Message message = (Message) objectInputStream.readObject();
                        System.out.println("[" + message.getSenderName() + " to " + message.getReceiverName() + "] " + message.getContent());

                        byteArrayOutputStream = new ByteArrayOutputStream();
                        objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
                        objectOutputStream.writeObject(message);
                        objectOutputStream.flush();
                        b = byteArrayOutputStream.toByteArray();

                        for (Map.Entry<User, Thread> entry : threads.entrySet()) {
                            User tempUser = entry.getKey();
                            //test
//                            System.out.println(Arrays.toString(tempUser.getClientIP().getAddress()));
                            datagramPacket = new DatagramPacket(b, 0, b.length, tempUser.getClientIP(), tempUser.getClientPort());
                            datagramSocket.send(datagramPacket);
                        }
                    } catch (IOException | ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        @Override
        public void run() {
            Thread chatThread = new Thread(new ChatThread());
            chatThread.setDaemon(true);
            chatThread.start();
            while (true) {
                try {
                    Message message = (Message) objectInputStream.readObject();
                    if (message.getType() == MessageType.DISCONNECTION) {
                        //客户端断开连接
                        System.out.println("[Server] " + user.getName() + " has disconnected from server");
                        threads.remove(user);
                        close();
                        break;
                    }
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
