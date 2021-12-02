package com.example.chatfx.controller;

import com.example.chatfx.StageController;
import com.example.chatfx.bean.Message;
import com.example.chatfx.bean.MessageType;
import com.example.chatfx.bean.User;
import com.example.chatfx.client.ClientSend;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;

import java.io.IOException;

public class ChatController implements ControlledStage {
    private StageController stageController;
    private static ObservableList<Text> chatRecord = FXCollections.observableArrayList();
    //有点像 Session
    private static User user;
    @FXML
    private ListView<Text> chatList;
    @FXML
    private TextArea textArea;

    public static User getUser() {
        return user;
    }

    public static void setUser(User user) {
        ChatController.user = user;
    }

    @FXML
    private void initialize() {
        Thread chatRefreshThread = new Thread(new ChatRefreshThread());
        chatRefreshThread.setDaemon(true);
        chatRefreshThread.start();
    }

    class ChatRefreshThread implements Runnable {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Message message = ClientSend.getMessage();
                if (message != null) {
                    try {
                        Text texts = new Text("[" + message.getSenderName() + " to " + message.getReceiverName() + "] " + message.getContent());
                        chatRecord.add(texts);
                        chatList.setItems(chatRecord);
                        ClientSend.setMessage(null);
                    } catch (Exception e) {

                    }
                }
            }
        }
    }

    @FXML
    private void onSendClick() throws IOException {
        String text = textArea.getText();

        Message message = new Message();
        message.setType(MessageType.COMMON);
        message.setSenderName(user.getName());
        message.setContent(text);
        ClientSend.sendChatMessage(message);

        textArea.clear();
    }

    @Override
    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }
}