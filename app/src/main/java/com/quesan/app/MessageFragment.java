package com.quesan.app;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quesan.app.model.Message;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


    public MessageFragment()
    {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_message, container, false);

        getActivity().setTitle("Message");
        final EditText message= (EditText) view.findViewById(R.id.message);
        Button btnsend= (Button) view.findViewById(R.id.btnsend);
        final Bundle b=getArguments();
        btnsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (message.getText().toString().length() == 0)
                {
                    message.setError("message can't be empty");
                }
                else
                {
                    Message message1=new Message();
                    message1.setSender(b.getString("sender"));
                    message1.setReceiver(b.getString("receiver"));
                    message1.setMessage(message.getText().toString());
                    message1.setDate(new Date().getTime());



                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Messages");
                    reference.child(reference.push().getKey()).setValue(message1);

                    Toast.makeText(getActivity(),"Message Sent",Toast.LENGTH_LONG).show();

                }

            }
        });

            return view;

    }

}
