package com.quesan.app;


import android.app.FragmentManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quesan.app.adapters.QuestionAdapter;
import com.quesan.app.model.Answer;
import com.quesan.app.model.Question;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class UnansweredQuestionFragment extends Fragment

{

    private ListView listView;
    private ArrayList<Question> list;
    private QuestionAdapter adapter;
    private Set<String> l;
    private DatabaseReference reference,reference1;


    public UnansweredQuestionFragment() {
        // Required empty public constructor
    list=new ArrayList<>();
        l=new HashSet<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_unanswered_question, container, false);

        getActivity().setTitle("UnAnswered Questions");
        adapter=new QuestionAdapter(getActivity(),list);
        listView= (ListView) view.findViewById(R.id.listViewUnAnsQuestions);

        new LoadUnAnswered().execute();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textViewQId= (TextView) view.findViewById(R.id.txtQuestionid);
                TextView textViewtitle= (TextView) view.findViewById(R.id.txtQuestionTitle);

                String qid=textViewQId.getText().toString();
                String questitle=textViewtitle.getText().toString();
                changeFragment(new AnswerListFragment(),qid,questitle);

            }
        });
    }



    public void changeFragment(Fragment fragment,String quesid,String ques_title)
    {
        Bundle bundle=new Bundle();
        bundle.putString("id",quesid);
        bundle.putString("title",ques_title);
        fragment.setArguments(bundle);
        FragmentManager fragmentManager=getFragmentManager();
        FragmentTransaction fragmentTransaction=getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.placeholder,fragment).commit();

    }

        private class LoadUnAnswered extends AsyncTask<Void,Void,Void>
        {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... params)
            {
                reference1= FirebaseDatabase.getInstance().getReference("Answers");
                reference1.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Answer ans=dataSnapshot.getValue(Answer.class);
                        Log.d("Answer QID :",ans.getQuestionId());
                        l.add(ans.getQuestionId());
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

                return null;

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            reference=FirebaseDatabase.getInstance().getReference("Questions");
                reference.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        Question q= dataSnapshot.getValue(Question.class);
                        Log.d("Qustin Qid:",q.getQuestionId());
                        if(l.size()>0&&!l.contains(q.getQuestionId()))
                        {
                            Log.d("Unanswered",q.getTitle());
                            list.add(q);
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

                listView.setAdapter(adapter);

            }
        }

}
