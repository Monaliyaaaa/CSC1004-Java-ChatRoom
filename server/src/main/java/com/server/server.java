package com.server;

import java.net.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.io.*;

import com.messages.Message;
import com.messages.MessageType;
import com.messages.User;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class server {
    private static HashSet<ObjectOutputStream> writers = new HashSet<>();
    static Logger logger = LoggerFactory.getLogger(server.class);
    private ArrayList<User> userList = new ArrayList<>();

    private Message tempMessage = new Message();
    public server(int port) throws IOException {
        ServerSocket ss = new ServerSocket(port);
        try {

            while(true){
                Socket s = ss.accept();
                logger.info("The sever is running.");
                Thread t = new ClientHandler(s);
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            ss.close();
            logger.info("Server socket is closed. ");
        }


    }

    public static void main(String args[]) throws IOException {
        server Server = new server(666);


    }

    class ClientHandler extends Thread {

        private ObjectInputStream input;
        private OutputStream os;
        private ObjectOutputStream output;
        private InputStream is;
        public Socket s;
        private String username;
        private Message tempMsg;
        public ClientHandler(Socket s) throws IOException { //constructor: get the info
            this.s = s;
        }

        public void run() {
            while (true) {

                try {

                    is = s.getInputStream();
                    input = new ObjectInputStream(is);
                    os = s.getOutputStream();
                    output = new ObjectOutputStream(os);
                    writers.add(output);


                    while (s.isConnected()) {

                        Message inputmsg = (Message) input.readObject();

                        if (inputmsg != null) {
                            switch (inputmsg.getType()) {
                                case INFO:
                                    init(inputmsg);   //Obtain the user's information from client.
                                    break;

                                case USER:
                                    write(inputmsg);
                                    logger.info("user "+inputmsg.getName()+"'s message: "+inputmsg.getMsg());
                                    break;

                                case VOICE:
                                    write(inputmsg);
                                    logger.info("user "+inputmsg.getName()+"'s voice message: "+inputmsg.getMsg());
                                    break;

                                case SERVER:
                                    write(inputmsg);
                                    logger.info("user "+inputmsg.getName() + inputmsg.getMsg());
                                    break;
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } finally {
                    closeConnection();
                }
            }

        }

        /*Create a message and send it to the client to initialize the chat window.*/
        public void init(Message msg) {
            username = msg.getName();
        }
        private void write(Message msg) throws IOException {
            for (ObjectOutputStream writer : writers) {
                writer.writeObject(msg);
                writer.reset();
            }
        }


        public synchronized void closeConnection(){
            if (input != null){
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (output != null){
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is != null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}