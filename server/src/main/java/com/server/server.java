package com.server;
//20230314: 做一个框架

import java.net.*;
import java.util.*;
import java.io.*;
import java.text.*;
public class server {
    public server(int port) throws IOException {
        ServerSocket ss = new ServerSocket(port);
        Socket s = ss.accept();
        try{

            DataInputStream dis = new DataInputStream(new BufferedInputStream(s.getInputStream()));
            DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(s.getOutputStream()));

            Thread t = new ClientHandler(s,dis,dos);
            t.start();


        }catch (IOException e){
            e.printStackTrace();
        }finally {
            ss.close();
        }


    }
    public static void main(String args[]) throws IOException {
        server Server = new server(666);  //以后可以做一个能选择端口的选项，类似于不同服务器？？
    }
}
class ClientHandler extends Thread{
    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket s;

    public ClientHandler(Socket s, DataInputStream dis, DataOutputStream dos){ //constructor: get the info
        this.s=s;
        this.dis=dis;
        this.dos=dos;
    }
    public void run(){
        while (true){
            try{
                //主体，之后填充
            }catch (IOException e){
                e.printStackTrace();
            }
        }
        try {          //close the stream
            this.dis.close();
            this.dos.close();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}