package com.quesan.app;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
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
import com.quesan.app.adapters.UserAdapter;
import com.quesan.app.model.User;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment
{
    private View view;
    private ListView usersListView;
    private ArrayList<User> userArrayList;
    private ProgressDialog progressDialog;
    private DatabaseReference databaseReference;
    private UserAdapter userAdapter;


    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflating the layout for this fragment
        view=inflater.inflate(R.layout.fragment_user,container,false);
        getActivity().setTitle("Users");

        userArrayList=new ArrayList<>();
        usersListView=(ListView)view.findViewById(R.id.listViewUsers);

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                TextView userid= (TextView) view.findViewById(R.id.user_id);

                String receiver=userid.getText().toString();
                String sender=FirebaseAuth.getInstance().getCurrentUser().getUid();
                Fragment messageFragment=new MessageFragment();
                Bundle b=new Bundle();
                b.putString("sender",sender);
                b.putString("receiver",receiver);
                messageFragment.setArguments(b);
                getFragmentManager().beginTransaction().replace(R.id.placeholder,messageFragment).commit();

            }
        });


        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {

             User user=dataSnapshot.getValue(User.class);     //this one

                userArrayList.add(user);
                userAdapter.notifyDataSetChanged();
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

        userAdapter=new UserAdapter(userArrayList,getActivity());
        usersListView.setAdapter(userAdapter);


        return view;

    }


}
