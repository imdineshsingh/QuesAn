package com.quesan.app;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.quesan.app.model.Question;

import java.util.Date;


/**
 * A simple {@link Fragment} subclass.
 */
public class PostFragment extends Fragment {


    private EditText txtTitle, txtDescription;
    private Button btnPost;

    public PostFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post, container, false);

        getActivity().setTitle("Post Query");
        txtTitle = (EditText) view.findViewById(R.id.txtTitle);
        txtDescription = (EditText) view.findViewById(R.id.txtDescription);
        btnPost = (Button) view.findViewById(R.id.btnPost);

        btnPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = txtTitle.getText().toString().trim();
                String description = txtDescription.getText().toString().trim();

                if (title.length() == 0)
                    txtTitle.setError("can not be blank");
                else if (description.length() == 0)
                    txtDescription.setError("can not be blank");

                else
                    new DatabaseOperation().execute(title, description);
            }
        });
        return view;

    }

    private class DatabaseOperation extends AsyncTask<String, String, Boolean>
    {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("QuesAn");
            progressDialog.setMessage("Saving...");
            progressDialog.show();
        }


        @Override
        protected Boolean doInBackground(String... params)
        {
            Question ques=new Question();
            ques.setTitle(params[0]);
            ques.setDescription(params[1]);
            ques.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
            ques.setPostDate(new Date().getTime());

            FirebaseDatabase database=FirebaseDatabase.getInstance();
            DatabaseReference reference=database.getReference("Questions");
            ques.setQuestionId(reference.push().getKey());
            reference.child(ques.getQuestionId()).setValue(ques);

            return true;


        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);


            progressDialog.dismiss();
            if (aBoolean)
            {
                txtTitle.setText("");
                txtDescription.setText("");
                txtTitle.requestFocus();
                Toast.makeText(getActivity(), "Post Successful", Toast.LENGTH_LONG).show();

            }
        }

    }
}
