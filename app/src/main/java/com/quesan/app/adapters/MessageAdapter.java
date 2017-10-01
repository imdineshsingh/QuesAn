package com.quesan.app.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quesan.app.R;
import com.quesan.app.model.Message;
import com.quesan.app.model.User;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Dinesh Singh on 8/22/2017.
 */

public class MessageAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Message> list;
    DatabaseReference databaseReference;

    public MessageAdapter(Context context, ArrayList<Message> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.layout_row_message, parent, false);

        final TextView sender = (TextView) view.findViewById(R.id.tv_sender);
        final TextView receiver = (TextView) view.findViewById(R.id.tv_receiver);
        final TextView message = (TextView) view.findViewById(R.id.tv_message);
        TextView date = (TextView) view.findViewById(R.id.tv_date);


        final Message message1 = (Message) getItem(position);

        sender.setText(message1.getSender());
        receiver.setText(message1.getReceiver());

        message.setText(message1.getMessage());
        date.setText(new Date(message1.getDate()).toString());


        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addChildEventListener(new ChildEventListener()
        {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                User user = dataSnapshot.getValue(User.class);

                if (message1.getSender().equals(user.getUserId()))
                {
                    sender.setText("sent by: " + user.getFirstName() + " " + user.getLastName());


                }
                else if(message1.getReceiver().equals(user.getUserId()))
                {
                    receiver.setText("Received by :"+user.getFirstName()+" "+user.getLastName());

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

/*
        databaseReference= FirebaseDatabase.getInstance().getReference("Messages");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                User u=new User();

                Message m=dataSnapshot.getValue(Message.class);
                if(m.getSender().equals(message1.getSender()))
                {
                    sender.setText("sent by: "+u.getFirstName()+" "+u.getLastName());
                    receiver.setText("Received by: "+u.getFirstName()+" "+u.getLastName());
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

*/
        return view;


    }



}
