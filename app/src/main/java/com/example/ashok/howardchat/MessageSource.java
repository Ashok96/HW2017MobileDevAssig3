package com.example.ashok.howardchat;

import android.content.Context;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by akafle on 8/7/17.
 */

public class MessageSource {
    public interface MessageListener {
        void onMessageReceived(List<Message> pingList);
    }

    private static MessageSource sMessageSource;

    private Context mContext;

    public static MessageSource get(Context context) {
        if (sMessageSource == null) {
            sMessageSource = new MessageSource(context);
        }
        return sMessageSource;
    }

    private MessageSource(Context context) {
        mContext = context;
    }

    // Firebase methods for you to implement.

    public void getMessage(final MessageListener messageListener) {
        DatabaseReference messageRef = FirebaseDatabase.getInstance().getReference();
//        Query last50PingsQuery = pingsRef.limitToLast(50);
        DatabaseReference mqueryRef = messageRef.child("messages");
        mqueryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Message> messageList = new ArrayList<>();
                Iterable<DataSnapshot> messageSnapshots = dataSnapshot.getChildren();
                for(DataSnapshot MessageSnapshot:messageSnapshots){
                    if((MessageSnapshot.child("fromUserName").getValue()!= null) && (MessageSnapshot.child("content").getValue()!=null)){
                    Message NewMessage = new Message(MessageSnapshot);
                    messageList.add(NewMessage);
                }
                }

                messageListener.onMessageReceived(messageList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }




    public void sendMessage(Message message) {
        DatabaseReference  messageRef= FirebaseDatabase.getInstance().getReference();
        DatabaseReference mMessageRef = messageRef.child("messages");
        DatabaseReference newmessageRef = mMessageRef.push();
        Map<String,Object> MessageValMap = new HashMap<String, Object>();
        MessageValMap.put("fromUserName",message.getmUserName());
        MessageValMap.put("fromUserId",message.getmUserId());
        MessageValMap.put("content", message.getmMessage());
        newmessageRef.setValue(MessageValMap);
    }
}
