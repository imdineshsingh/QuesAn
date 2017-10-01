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
import com.quesan.app.model.Answer;
import com.quesan.app.model.Question;
import com.quesan.app.model.User;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Dinesh Singh on 8/7/2017.
 */

public class AnswerAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Answer> list;
    DatabaseReference databaseReference;


    public AnswerAdapter(Context context, ArrayList<Answer> list) {
        this.context = context;
        this.list = list;
    }

  //  public AnswerAdapter() {super(); }


    @Override
    public int getCount()
    {
        return list.size();
    }

    @Override
    public Object getItem(int position)
    {
        return list.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        //inflating view object
        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view=inflater.inflate(R.layout.answer_row_layout,parent,false);


        TextView textViewAnsweredOn= (TextView) view.findViewById(R.id.textViewAnsweredOn);
        final TextView txtPostedBy= (TextView) view.findViewById(R.id.txtPostedBy);


        final Answer answer=(Answer) getItem(position);


        textViewAnsweredOn.setText("posted on :"+new java.sql.Date(answer.getPostDate()));

        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {

                User user=dataSnapshot.getValue(User.class);     //this one
                if(user.getUserId().equals(answer.getUserId()))
                {
                    txtPostedBy.setText("posted by :"+user.getFirstName()+" "+user.getLastName());
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


        //initializing
      //  final TextView answer_id=(TextView)view.findViewById(R.id.txtAnswerId);

        TextView textViewId=(TextView) view.findViewById(R.id.txtAnswerId);


        TextView textViewTitle=(TextView)view.findViewById(R.id.txtAnswerTitle);
        TextView textViewPostedOn=(TextView)view.findViewById(R.id.txtPostedDate);
        TextView textViewPostedBy=(TextView)view.findViewById(R.id.txtPostedBy);



        //setting properties
       // Answer answer= (Answer) getItem(position);
        textViewTitle.setText(answer.getAnswer());
        /*textViewDescription.setText(question.getDescription());*/
        textViewPostedBy.setText("Posted by : "+answer.getUserId());

     //   textViewPostedOn.setText("Posted On :"+new Date(answer.getPostDate()));

        textViewId.setText(answer.getAnswerId());



        return view;
    }

}
