package com.example.chatfx.controller;

import com.example.chatfx.StageController;
import com.example.chatfx.bean.User;
import com.example.chatfx.client.AddressInfo;
import com.example.chatfx.client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.InetAddress;

public class LoginViewController implements ControlledStage {
    private StageController stageController;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void onLoginClick() throws IOException, ClassNotFoundException {
        String name = nameField.getText();
        String password = passwordField.getText();
        InetAddress IP = InetAddress.getLocalHost();
        User user = new User(name, password, IP, AddressInfo.localPort);
        user.setRegistered(true);

        new Client(user);
        int state = Integer.parseInt(Client.getMessage().getContent());
        switch (state) {
            case -1:
                System.out.println("用户不存在");

                break;
            case 0:
                nameField.clear();
                passwordField.clear();
                System.out.println("登录成功");
                ChatController.setUser(user);
                stageController.setStage("登录", "聊天");

                break;
            case 1:
                System.out.println("密码错误");
                passwordField.clear();
                break;
        }
    }

    @FXML
    private void onRegisterClick() {
        stageController.setStage("登录", "注册");
    }

    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }
}