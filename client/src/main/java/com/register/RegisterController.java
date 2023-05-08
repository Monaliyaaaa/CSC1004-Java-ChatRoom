package com.register;

import com.Main;
import com.util.JdbcUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Executor;

public class RegisterController {
    @FXML
    private Button cancel;
    @FXML
    private TextField newusername;
    @FXML
    private PasswordField newpass;
    @FXML
    private Label notification;
    @FXML
    private Main mainAPP;

    private static  RegisterController instance;

    public RegisterController() {instance=this;}

    public Main getMainAPP() {
        return mainAPP;
    }
    public void setMainAPP(Main mainAPP) {
        this.mainAPP = mainAPP;
    }
    JdbcUtils jdbcUtils = new JdbcUtils();
    Connection con= jdbcUtils.getConnection();
    //Add a new user account.
    public void registerConfirm(ActionEvent event) throws IOException, SQLException {
        String name = newusername.getText();
        String pass = newpass.getText();
        String ip = InetAddress.getLocalHost().getHostAddress(); //get the local host address.
        int port = 666;

        try {

            if(jdbcUtils.duplicationName(name,con)){
                Statement stmt= con.createStatement();
                String sql="insert into userinfo(username, password, ip) values (?,?,?)";
                List<Object> params = new ArrayList<>();
                params.add(name);
                params.add(pass);
                params.add(ip);
                try {
                    jdbcUtils.update(sql, params);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                notification.setText("Registered successfully! Now you can click cancel and login then.");
                notification.setVisible(true);

            }else{
                notification.setText("The username has already been used, please change.");
                notification.setVisible(true); //Notice that the username has already been used.
            }

        }catch (SQLException e){
            e.printStackTrace();
        }


    }
    public void cancel(ActionEvent event) throws SQLException {     //Press the cancel Button to close the login window.
        close(con);
        Stage stage = (Stage) cancel.getScene().getWindow();
        stage.close();
    }

    public void close(Connection con) throws SQLException {
        con.close();
    }




}
