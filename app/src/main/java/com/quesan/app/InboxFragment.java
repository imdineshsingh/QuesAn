package com.quesan.app;


import android.os.Bundle;
import android.app.Fragment;
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
import com.quesan.app.adapters.MessageAdapter;
import com.quesan.app.model.Message;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment
{
    private MessageAdapter adapter;
    private ListView listView;
    private ArrayList<Message> list;
    private String title;

    public InboxFragment()

    {
        list=new ArrayList<>();
        this.title=title;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_inbox, container, false);

        adapter=new MessageAdapter(getActivity(),list);
        listView= (ListView) view.findViewById(R.id.message_list);

        Bundle b=getArguments();
        getActivity().setTitle(b.getString("title"));

        final String title=getActivity().getTitle().toString();
        if(title!=null)
        {
                getActivity().setTitle(title);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView userid= (TextView) view.findViewById(R.id.tv_sender);
                String receiver=userid.getText().toString();
                String sender =FirebaseAuth.getInstance().getCurrentUser().getUid();

                Fragment messageFragment=new MessageFragment();
                Bundle b=new Bundle();
                b.putString("sender",sender);
                b.putString("receiver",receiver);
                messageFragment.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.placeholder,messageFragment).commit();

            }
        });


        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Messages");

        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {

             Message message=dataSnapshot.getValue(Message.class);
                if(title.equals("Inbox")) {

                    if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getReceiver())) {
                        list.add(message);
                        adapter.notifyDataSetChanged();
                    }
                }
                else
                {
                    if(FirebaseAuth.getInstance().getCurrentUser().getUid().equals(message.getSender()))
                    {
                        list.add(message);
                        adapter.notifyDataSetChanged();
                    }
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
        return view;


    }

}
