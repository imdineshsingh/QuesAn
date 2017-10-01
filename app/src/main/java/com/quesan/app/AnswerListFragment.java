package com.quesan.app;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quesan.app.adapters.AnswerAdapter;
import com.quesan.app.model.Answer;
import com.quesan.app.model.AnswerRating;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class AnswerListFragment extends Fragment

{

    private ListView listView;
    private ArrayList<Answer> answerArrayList;
    private EditText txtAnswer;
    private TextView questionTitle, questionId;
    private DatabaseReference reference, reference1;

    private Button replyQuery;
    private AnswerAdapter adapter;
 //   private RatingBar ratingBar;
   // private ArrayList<AnswerRating> ratings;


    public AnswerListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_answer_list, container, false);

        answerArrayList = new ArrayList<>();
        questionTitle = (TextView) view.findViewById(R.id.textViewQuestionTitle);
        questionId = (TextView) view.findViewById(R.id.textViewQuestionid);
        txtAnswer = (EditText) view.findViewById(R.id.editTextAnswer);
        replyQuery = (Button) view.findViewById(R.id.btnAnswer);
        listView = (ListView) view.findViewById(R.id.listViewAnswers);

        Bundle bundle = getArguments();
        questionTitle.setText(bundle.getString("title"));
        questionId.setText(bundle.getString("id"));


        reference = FirebaseDatabase.getInstance().getReference("Answers");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                Answer answer = dataSnapshot.getValue(Answer.class);
                if (answer.getQuestionId().equals(questionId.getText().toString()) && !answerArrayList.contains(answer))
                {
                    answerArrayList.add(answer);
                    adapter.notifyDataSetChanged();
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


            adapter = new AnswerAdapter(getActivity(), answerArrayList);
        //adapter=new AnswerAdapter(answerArrayList,getActivity());

        replyQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (questionId.getText().length() > 0 && txtAnswer.getText().length() > 0) {
                    Answer answer = new Answer();
                    answer.setAnswer(txtAnswer.getText().toString());
                    answer.setQuestionId(questionId.getText().toString());
                    answer.setPostDate(new Date().getTime());
                    answer.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    answer.setAnswerId(reference.push().getKey());
                    reference.child(answer.getAnswerId()).setValue(answer);
                    Toast.makeText(getActivity(), "Posted", Toast.LENGTH_LONG).show();
                    txtAnswer.setText("");
                    adapter.notifyDataSetChanged();
                } else
                    {

                        txtAnswer.setError("Empty Answer Not allowed");

                    }

            }
        });

        return view;
    }


    @Override
    public void onStart()
    {
        super.onStart();
        listView.setAdapter(adapter);
    }

}

/*

    @Override
    public void onStart()
    {
        super.onStart();
        list=new ArrayList<>();
        new LoadAnswers().execute(textView1.getText().toString());
    }


private class LoadAnswers extends AsyncTask<String,Void,String>
    {
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String s) {

        if(s!=null&& s.equals("posted"))
        {
            Toast.makeText(getActivity(),"Thanks for reply",Toast.LENGTH_LONG).show();
            txtAnswer.setText("");
        }
        else if(list.size()>0)
            listView.setAdapter(new AnswerAdapter(getActivity(),list));
    }

    @Override
    protected String doInBackground(String... params)
    {
     final String qid=params[0];
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Answers");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
             Answer answer=dataSnapshot.getValue(Answer.class);
                if(answer.getQuestionId().equals(qid) &&! list.contains(answer))
                {
                    list.add(answer);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s)
            {

            }

            @Override
            public void onCancelled(DatabaseError databaseError)
            {

            }
        });
        if(params.length>1 && params[0]!=null && params[1]!=null)
        {
            Answer answer=new Answer();
            answer.setAnswer(params[1]);
            answer.setQuestionId(params[0]);
            answer.setPostDate(new Date().getTime());
            answer.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

            answer.setAnswerId(reference.push().getKey());
            reference.child(answer.getAnswerId()).setValue(answer);

            return "posted";

        }

        return null;
      }
    }
*/

