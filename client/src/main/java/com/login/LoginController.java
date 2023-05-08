package com.login;

import com.Main;
import com.chat.ChatController;
import com.chat.Listener;
import com.util.JdbcUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;


public class LoginController{
    @FXML
    private Button cancelbutton;
    @FXML
    private Label loginMessage;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Main mainAPP;

    private static LoginController instance;
    public Stage stage;

    public void setMainAPP(Main mainAPP) {
        this.mainAPP = mainAPP;
    }

    public Main getMainAPP() {
        return mainAPP;
    }

    public LoginController() {
        instance = this;
    }
    public static LoginController getInstance() {
        return instance;
    }


    public void cancelbuttonOnAction(ActionEvent event){     //Press the cancel Button to close the login window.
        stage= (Stage) cancelbutton.getScene().getWindow();
        stage.close();
    }
    public void loginbuttonOnAction() throws IOException {
        String name = username.getText();
        String pass = password.getText();
        String ip = InetAddress.getLocalHost().getHostAddress();
        int port = 666;

        try {
            JdbcUtils jdbcUtils = new JdbcUtils();
            Connection con= jdbcUtils.getConnection();
            String result = jdbcUtils.search(name,pass,con);
            if (result!=""){  // Has found the information of this user!
                loginMessage.setVisible(false);
                FXMLLoader loader= new FXMLLoader();
                loader.setLocation(Main.class.getResource("chat/ChatWindow.fxml"));
                Parent root = loader.load();
                showScene("Chat Window",root);
                ChatController controller = loader.getController();
                Listener listener = new Listener(name,port,ip,controller);
                Thread x = new Thread(listener);
                x.start();
                stage= (Stage) cancelbutton.getScene().getWindow();
                stage.close();   //The login window should be closed at the same time.

            }else{
                loginMessage.setVisible(true);
            }
            con.close();
        }catch (SQLException e){
            e.printStackTrace();
        }

    }


    public void registerbuttonOnAction(ActionEvent event) {
        try {
            FXMLLoader loader= new FXMLLoader();
            loader.setLocation(Main.class.getResource("login/RegisterView.fxml"));
            Parent root = loader.load();
            showScene("Register Window", root);

        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showScene(String title, Parent parent){
        Stage newstage = new Stage();
        newstage.initStyle(StageStyle.UNDECORATED);
        newstage.setTitle(title);
        newstage.setResizable(false);
        Scene scene= new Scene(parent);
        newstage.setScene(scene);
        newstage.show();

    }
}
