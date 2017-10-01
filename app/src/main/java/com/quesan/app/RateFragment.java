package com.quesan.app;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quesan.app.model.Rate;


/**
 * A simple {@link Fragment} subclass.
 */
public class RateFragment extends Fragment {
    private RatingBar ratingBar,overall;
    private EditText txtComment;
    private Button btnRate;
    private TextView txtOverall;
    private float overallpoints;
    private int count;
    private Rate r;
    public RateFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_rate, container, false);
        ratingBar=(RatingBar) view.findViewById(R.id.ratingBar);
        overall=(RatingBar) view.findViewById(R.id.overall);
        txtOverall=(TextView)view.findViewById(R.id.txtoverall);
        btnRate=(Button) view.findViewById(R.id.btnRate);
        txtComment=(EditText) view.findViewById(R.id.txtComment);
        new LoadRatings().execute();


        btnRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ratingBar.getRating()>0 && txtComment.getText().length()>=4)
                {

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Ratings");
                    Rate rate=new Rate();
                    rate.setRating(ratingBar.getRating());
                    rate.setComment(txtComment.getText().toString());
                    rate.setUserKey(FirebaseAuth.getInstance().getCurrentUser().getUid());
                    rate.setKey(reference.push().getKey());
                    reference.child(rate.getKey()).setValue(rate);
                    AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                    builder.setTitle("Qusec");
                    builder.setMessage("Thank you for your valuable rating..");
                    builder.setCancelable(true);
                    builder.show();
                    new LoadRatings().execute();
                }
            }
        });

        return view;

    }

    public void overall()
    {
        Log.d("Overall Points : ",overallpoints+"");
        Log.d("Overall Count : ",count+"");
        if(r!=null)
        {
            ratingBar.setRating(r.getRating());
            txtComment.setText(r.getComment());
            btnRate.setEnabled(false);
        }

        if(overallpoints>0 && count>0)
        {
            float rating=overallpoints/count;
            overall.setRating(rating);
            txtOverall.setText("Overall Rating : "+rating);
        }
    }
    private  class LoadRatings extends AsyncTask<Void,Void,Void>
    {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            overallpoints=0;
            count=0;
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("Qusec");
            progressDialog.setMessage("Loading...");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            overall();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Ratings");
            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s)
                {
                    Rate rate= dataSnapshot.getValue(Rate.class);
                    Log.d("Rating : ",rate.getRating()+"");


                    overallpoints+=rate.getRating();
                    count++;


                    if(rate.getUserKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                    {
                        r = rate;
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
            return null;
        }
    }


}
