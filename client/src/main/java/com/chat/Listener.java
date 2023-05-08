package com.chat;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

import com.messages.Message;
import com.messages.MessageType;
import com.messages.User;
import com.util.JdbcUtils;
import javafx.application.Platform;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Listener implements Runnable{

    public static String username;
    public int port;
    public String hostname;
    public ChatController controller;
    private static Socket socket;
    public static OutputStream outputStream;
    public static InputStream inputStream;
    public static ObjectOutputStream output;
    public ObjectInputStream input;
    Logger logger = LoggerFactory.getLogger(Listener.class);
    public Listener(String username, int port, String hostname, ChatController controller){
        this.username=username;
        this.port=port;
        this.hostname=hostname;
        this.controller=controller;
    }
    @Override
    public void run() {
        try {
            socket = new Socket(hostname,port);
            outputStream = socket.getOutputStream();
            output = new ObjectOutputStream(outputStream);
            inputStream = socket.getInputStream();
            input = new ObjectInputStream(inputStream);
            logger.info("client "+username+" got connected.");

        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            Message firstMessage = new Message();   //create the first message to tell sever this user's information.
            firstMessage.setType(MessageType.INFO);
            firstMessage.setName(username);
            output.writeObject(firstMessage);

            Message initMsg= initChatWindow();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        controller.setUsernameLabel(initMsg);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    controller.setUserList(initMsg);
                }
            });

            logger.info("socket start to read inputStream.");
            while (socket.isConnected()) {

                Message message = (Message) input.readObject();
                logger.info("Receive a message from server: "+message.getName()+" "+message.getType()+" "+message.getMsg());
                if(message!=null){
                    switch (message.getType()) {
                        case USER:
                            controller.addToChat(message);
                            break;

                        case VOICE:
                            controller.addToChat(message);
                            break;

                        case SERVER:
                            controller.addAsServer(message);
                            break;


                    }
                }

            }


        } catch (RuntimeException ex) {
               throw new RuntimeException(ex);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void send(String msg) throws IOException {
        Message newMsg= new Message();
        newMsg.setName(username);
        newMsg.setMsg(msg);
        newMsg.setType(MessageType.USER);
        output.writeObject(newMsg);
        output.flush();

    }

    public static void sendVoiceMessage(String audioName) throws IOException {
        Message newMsg = new Message();
        newMsg.setName(username);
        newMsg.setMsg(audioName);
        newMsg.setType(MessageType.VOICE);
        output.writeObject(newMsg);
        output.flush();
    }


    public Message initChatWindow() throws SQLException, IOException {
        JdbcUtils jdbcUtils = new JdbcUtils();
        Connection con = jdbcUtils.getConnection();
        ArrayList<User> userList = jdbcUtils.getUserList(username, con); // It is a list includes all the users except this client.

        Message initMsg = new Message();  // Create a initMsg to initialize the chat window
        initMsg.setType(MessageType.SERVER);
        initMsg.setName(username);
        initMsg.setMsg("init");
        initMsg.setUsers(userList);
        output.writeObject(initMsg);

        return initMsg;
    }

    public static void closeChat() throws IOException {
        Message msg = new Message();
        msg.setName(username);
        msg.setType(MessageType.SERVER);
        msg.setMsg("close");
        output.writeObject(msg);
        output.flush();

    }

}
