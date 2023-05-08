package com.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Message implements Serializable {
    public String name;
    public String msg;
    public String picture;
    public ArrayList<User> users;
    public ArrayList<User> list;

    public MessageType type;

    public Message() {
    }
    public void setMsg(String msg){
        this.msg=msg;
    }
    public void setName(String name){
        this.name=name;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }


    public String getMsg(){
        return msg;
    }
    public String getName(){
        return name;
    }
    public String getPicture(){
        return picture;
    }
    public void setUserlist(HashMap<String, User> userList) {
        this.list = new ArrayList<>(userList.values());
    }
    public ArrayList<User> getUsers() {

        return users;
    }
    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }


    public void setType(MessageType type) {
        this.type = type;
    }
    public MessageType getType(){
        return type;
    }
}
