package com.quesan.app.adapters;

import android.content.Context;
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
import com.quesan.app.model.Question;
import com.quesan.app.model.User;

import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by Dinesh Singh on 8/7/2017.
 */

public class QuestionAdapter extends BaseAdapter

{
    private Context context;
    private ArrayList<Question> list;

    DatabaseReference databaseReference;
    public QuestionAdapter(Context context, ArrayList<Question> list) {
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

        //inflating view object
        LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view =inflater.inflate(R.layout.layout_row_questions,parent,false);

        //initializinggg

        TextView textViewTitle=(TextView)view.findViewById(R.id.txtQuestionTitle);
        TextView textViewid=(TextView)view.findViewById(R.id.txtQuestionid);
        TextView textViewDescription=(TextView)view.findViewById(R.id.txtQuestionDescription);
        final TextView textViewPostedBy=(TextView)view.findViewById(R.id.txtPostedBy);


        TextView textViewPostedOn=(TextView)view.findViewById(R.id.txtPostedDate);


        //Setting properties

        final Question question=(Question) getItem(position);
        textViewTitle.setText(question.getTitle());
        textViewDescription.setText(question.getDescription());

        textViewPostedOn.setText("posted on :"+new Date(question.getPostDate()));

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {

                User user=dataSnapshot.getValue(User.class);     //this one
                if(user.getUserId().equals(question.getUserId()))
                {
                    textViewPostedBy.setText("posted by :"+user.getFirstName()+" "+user.getLastName());
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



        textViewid.setText(question.getQuestionId());

        return view;
    }
}
