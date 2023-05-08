package com.chat;

import com.Main;
import com.messages.Bubble.BubbleType;
import com.messages.Bubble.Bubbledlabel;
import com.messages.Message;
import com.messages.MessageType;
import com.messages.User;
import com.voice.AudioRecorder;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


import static jdk.jfr.consumer.EventStream.openFile;


public class ChatController implements Initializable{
    @FXML
    public TextArea msgBox;
    @FXML
    public Button close;
    @FXML
    public ListView chatPane;
    @FXML
    public ImageView userImageView;
    @FXML
    public Label usernameLabel;
    @FXML
    public ListView userList;
    @FXML
    public Button voiceRecord;
    @FXML
    BorderPane borderPane;
    private double xOffset;
    private double yOffset;
    static Logger logger = LoggerFactory.getLogger(ChatController.class);
    private int number=1;
    private boolean t =true;


    public void setUsernameLabel(Message msg) throws IOException{
                this.usernameLabel.setText(msg.getName());
    }
    public void cancelbuttonOnAction(ActionEvent event) throws IOException {     //Press the cancel Button to close the login window.
        Stage stage = (Stage) close.getScene().getWindow();
        stage.close();
        Message msg = new Message();
        msg.setName(usernameLabel.getText());
        msg.setType(MessageType.SERVER);
        msg.setMsg("close");
        Listener.closeChat();
    }


    public void sendButton() throws IOException{
        String msg= msgBox.getText();
        if(!msgBox.getText().isEmpty()){
            Listener.send(msg);
            msgBox.clear();
        }
    }
    public void voiceButton() throws IOException {
        String audioName = "test"+number+".wav";
        AudioRecorder audioRecorder = new AudioRecorder(audioName);
        if (t){
            audioRecorder.start();
            voiceRecord.setText("recording");
        }else {
            audioRecorder.stopRecording();
            number++;
            Listener.sendVoiceMessage(audioName);
            voiceRecord.setText("Voice");
        }
        t=!t;


    }

    public synchronized void addToChat(Message msg) {
        Task<HBox> othersMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Bubbledlabel bl = new Bubbledlabel();
                if (msg.getType() == MessageType.VOICE){
                    bl.setText(msg.getName()+" : Sent a voice message!");
                    AudioRecorder audioRecorder= new AudioRecorder(msg.getMsg());
                    audioRecorder.play(msg.getMsg());
                }else {
                    bl.setText(msg.getName() + " : " + msg.getMsg());
                }
                bl.setBackground(new Background(new BackgroundFill(Color.LIGHTSKYBLUE,null, null)));
                HBox x = new HBox();
                bl.setBubbleSpec(BubbleType.LEFT_CENTER);
                x.getChildren().addAll(bl);

                return x;
            }
        };

        othersMessages.setOnSucceeded(event -> {
            chatPane.getItems().add(othersMessages.getValue());
        });

        Task<HBox> yourMessages = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Image image = userImageView.getImage();
                ImageView profileImage = new ImageView(image);
                profileImage.setFitHeight(32);
                profileImage.setFitWidth(32);

                Bubbledlabel bl = new Bubbledlabel();
                if (msg.getType() == MessageType.VOICE){
                    bl.setText("You sent a voice message");
                    AudioRecorder audioRecorder= new AudioRecorder(msg.getMsg());
                    audioRecorder.play(msg.getMsg());
                }else {
                    bl.setText(msg.getMsg());
                }
                bl.setBackground(new Background(new BackgroundFill(Color.LIGHTPINK, null, null)));
                HBox x = new HBox();
                x.setMaxWidth(chatPane.getWidth() - 20);
                x.setAlignment(Pos.TOP_RIGHT);
                bl.setBubbleSpec(BubbleType.RIGHT_CENTER);
                x.getChildren().addAll(bl, profileImage);

                return x;
            }
        };
        yourMessages.setOnSucceeded(event -> chatPane.getItems().add(yourMessages.getValue()));

        if (msg.getName().equals(usernameLabel.getText())) {
            Thread t2 = new Thread(yourMessages);
            t2.setDaemon(true);
            t2.start();
        } else {
            Thread t = new Thread(othersMessages);
            t.setDaemon(true);
            t.start();
        }
    }

    public void setUserList(Message msg) {
        Platform.runLater(() -> {
            ObservableList<User> users= FXCollections.observableList(msg.getUsers());
            userList.setItems(users);
            userList.setCellFactory(new CellRenderer());

        });
        logger.info("Initialization complete.");
    }


    public synchronized void addAsServer(Message message){
        Task<HBox> task = new Task<HBox>() {
            @Override
            public HBox call() throws Exception {
                Bubbledlabel serverMsg = new Bubbledlabel();
                if (message.getMsg().equals("init")){
                    serverMsg.setText(message.getName()+" has joined the chat.");
                }else {
                    serverMsg.setText(message.getName()+" has left the chat room.");
                }
                serverMsg.setBackground(new Background(new BackgroundFill(Color.MEDIUMPURPLE, null, null)));
                HBox x =new HBox();
                serverMsg.setBubbleSpec(BubbleType.BOTTOM);
                x.setAlignment(Pos.CENTER);
                x.getChildren().addAll(serverMsg);
                return x;
            }
        };
        task.setOnSucceeded(event -> {
            chatPane.getItems().add(task.getValue());
        });
        Thread t = new Thread(task);
        t.setDaemon(true);
        t.start();
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {   //先放着

        borderPane.setOnMousePressed(event -> {
            xOffset = Main.getPrimaryStage().getX() - event.getScreenX();
            yOffset = Main.getPrimaryStage().getY() - event.getScreenY();
            borderPane.setCursor(Cursor.CLOSED_HAND);
        });

        borderPane.setOnMouseDragged(event -> {
            Main.getPrimaryStage().setX(event.getScreenX() + xOffset);
            Main.getPrimaryStage().setY(event.getScreenY() + yOffset);

        });

        borderPane.setOnMouseReleased(event -> {
            borderPane.setCursor(Cursor.DEFAULT);
        });
    }

}
