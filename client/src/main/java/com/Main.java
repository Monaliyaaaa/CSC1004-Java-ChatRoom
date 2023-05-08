package com;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {
    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        try {
            Parent root = FXMLLoader.load(Main.class.getResource("login/loginView.fxml"));
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setTitle("Login Window");
            Scene mainScene = new Scene(root);
            mainScene.setRoot(root);
            primaryStage.setResizable(true);
            primaryStage.setScene(mainScene);
            primaryStage.show();
        }catch (IOException e) {
            e.printStackTrace();
        }


    }
    public static void main(String args[]){
        launch();
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }


}
