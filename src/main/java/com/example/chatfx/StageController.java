package com.example.chatfx;

import com.example.chatfx.controller.ControlledStage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class StageController {
    private HashMap<String, Stage> stages;

    public StageController() {
        this.stages = new HashMap<>();
    }

    public void loadStage(String name,String fxmlSource) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlSource));
        Parent parent = fxmlLoader.load();

        ControlledStage controlledStage = fxmlLoader.getController();
        controlledStage.setStageController(this);

        Stage stage = new Stage();
        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.setTitle(name);

        addStage(name, stage);
    }

    public void setStage(String oldStageName, String newStageName) {
        getStage(oldStageName).close();
        setStage(newStageName);
    }

    public void setStage(String stageName) {
        getStage(stageName).show();
    }

    private void addStage(String name, Stage stage) {
        stages.put(name, stage);
    }

    public Stage getStage(String name) {
        return stages.get(name);
    }
}
