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

public class RegisterController implements ControlledStage {
    private StageController stageController;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void onSubmitClick() throws IOException, ClassNotFoundException {
        String name = nameField.getText();
        String password = passwordField.getText();
        InetAddress IP = InetAddress.getLocalHost();
        User user = new User(name, password, IP, AddressInfo.localPort);
        user.setRegistered(false);

        new Client(user);
        int state = Integer.parseInt(Client.getMessage().getContent());
        if (state == -1) {
            System.out.println("注册完成");
        } else if (state == 0) {
            System.out.println("用户已存在");
        }

        nameField.clear();
        passwordField.clear();

        stageController.setStage("注册", "登录");
    }

    @Override
    public void setStageController(StageController stageController) {
        this.stageController = stageController;
    }
}
