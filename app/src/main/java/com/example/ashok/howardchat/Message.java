package com.example.ashok.howardchat;

import com.google.firebase.database.DataSnapshot;

/**
 * Created by akafle on 8/7/17.
 */

public class Message{
    private String mUserName;
    private String mUserId;
    private String mMessage;


    public  Message(String userName,String userId,String message){
        mUserName = userName;
        mUserId = userId;
        mMessage = message;
    }

    public Message(DataSnapshot msgSnapshot){
        mUserName = msgSnapshot.child("fromUserName").getValue(String.class);
        mUserId = msgSnapshot.child("fromUserId").getValue(String.class);
        mMessage = msgSnapshot.child("content").getValue(String.class);
    }
    public String getmUserName(){
        return mUserName;
    }

    public String getmUserId(){
        return mUserId;
    }

    public String getmMessage(){
        return mMessage;
    }

}