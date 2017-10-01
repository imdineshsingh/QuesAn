package com.quesan.app;


import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quesan.app.adapters.QuestionAdapter;
import com.quesan.app.model.Question;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class QuestionListFragment extends Fragment {


    private ListView listView;
    private ArrayList<Question> list;
    private Context context;
    private FragmentManager fragmentManager;
    private QuestionAdapter adapter;
    private DatabaseReference reference;
    private String title;


    public QuestionListFragment()
    {
        // Required empty public constructor

        context = getActivity();
        list = new ArrayList<Question>();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_question_list, container, false);

        final ProgressDialog progressDialog=new ProgressDialog(getActivity());
        progressDialog.setTitle("QuesAn\n Keep Calm");
        progressDialog.setMessage("Fetching Data..");
        progressDialog.show();
        progressDialog.setCancelable(false);



        Bundle b=getArguments();
        if(b!=null && b.getString("title")!=null)
        {
            getActivity().setTitle(b.getString("title"));
            title=b.getString("title");
        }
        else
        {
            getActivity().setTitle("Home");
            title="home";
        }

        //getActivity().setTitle("Home");

        listView = (ListView) view.findViewById(R.id.listViewQuestions);

        reference=FirebaseDatabase.getInstance().getReference("Questions");
        reference.keepSynced(true);


        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                progressDialog.dismiss();
                Question question=dataSnapshot.getValue(Question.class);
                if(title!=null && title.equals("My Question"))
                {
                    if(question.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))list.add(question);
                    adapter.notifyDataSetChanged();
                }
                else
                    {
                        list.add(question);
                        Log.d("Question Fragment",question.getTitle());
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


        adapter=new QuestionAdapter(getActivity(),list);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView textViewQid=(TextView)view.findViewById(R.id.txtQuestionid);
                TextView textViewTitle=(TextView)view.findViewById(R.id.txtQuestionTitle);

                String qid=textViewQid.getText().toString();
                String questitle=textViewTitle.getText().toString();

              changeFragment(new AnswerListFragment(),qid,questitle);

            }
        });
        return view;
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

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume()
    {
        super.onResume();
       // new DatabaseOperation().execute();
    }

}
    /*
    private class DatabaseOperation extends AsyncTask<Void,Void,Void>
    {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list =new ArrayList<>();

            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Loading");
            progressDialog.setMessage("Please Wait");
            progressDialog.show();

        }

        @Override
        protected Void doInBackground(Void... params) {

            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Questions");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Question question=dataSnapshot.getValue(Question.class);
                    list.add(question);
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
        protected void onPostExecute(Void aVoid)
        {
            progressDialog.dismiss();
            super.onPostExecute(aVoid);
            listView.setAdapter(new QuestionAdapter(getActivity(),list));
        }
    }

*/

