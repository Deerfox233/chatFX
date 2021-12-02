package com.example.chatfx;

import com.example.chatfx.client.ClientSend;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;

public class HelloApplication extends Application {
    private StageController stageController;

    @Override
    public void start(Stage stage) throws IOException {
        stageController = new StageController();
        stageController.loadStage("登录", "login-view.fxml");
        stageController.loadStage("注册", "register-view.fxml");
        stageController.loadStage("聊天", "chat-view.fxml");
        stageController.getStage("聊天").setOnCloseRequest(windowEvent -> {
            try {
                ClientSend.sendDisconnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        stageController.setStage("登录");
    }

    public static void main(String[] args) {
        launch();
    }
}