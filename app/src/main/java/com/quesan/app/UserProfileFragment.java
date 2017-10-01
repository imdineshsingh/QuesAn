package com.quesan.app;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.quesan.app.model.User;

import java.io.File;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFragment extends Fragment
{

    ImageView userImage;
    EditText userFirstName,userLastName,userMobileNo,userProfession,userAddress;
    Button btnSubmit;
    private Uri filePath;
    private User user;

    public UserProfileFragment()
    { // Required empty public constructor
         }


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View view= inflater.inflate(R.layout.fragment_user_profile, container, false);

        userImage= (ImageView) view.findViewById(R.id.userImage);
        userFirstName= (EditText) view.findViewById(R.id.userFirstName);
        userLastName=(EditText)view.findViewById(R.id.userLastName);
        userProfession=(EditText) view.findViewById(R.id.userProfession);
        userAddress= (EditText) view.findViewById(R.id.userAddress);
        userMobileNo=(EditText)view.findViewById(R.id.userMobileNo);
        Button btnSubmit=(Button)view.findViewById(R.id.btnSubmit);


        //getting userinfo. on textFields


        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference("Users");

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                User user=dataSnapshot.getValue(User.class);
                if(user.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()));
                {
                    userFirstName.setText(user.getFirstName());
                    userLastName.setText(user.getLastName());
                    userMobileNo.setText(user.getPhone());
                    userAddress.setText(user.getAddress());
                    userProfession.setText(user.getProfession());
                   // userImage.setImageBitmap(user.getPicture());
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

            //getting user Image

        StorageReference reference= FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
        try
        {
            final File localFile = File.createTempFile("images", "jpg");

            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                {
                    Bitmap bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    userImage.setImageBitmap(bitmap);


                }
            }).addOnFailureListener(new OnFailureListener()
            {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    e.printStackTrace();
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }




        btnSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        //validation
                        User user=new User();
                        user.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        user.setFirstName(userFirstName.getText().toString());
                        user.setLastName(userLastName.getText().toString());
                        user.setAddress(userAddress.getText().toString());
                        user.setPhone(userMobileNo.getText().toString());
                        user.setProfession(userProfession.getText().toString());

                        user.setUpdatedDate(new Date().getTime());
                        user.setPicture(user.getUserId()+".jpg");
                        new UpdateProfile().execute(user);
                    }
                });

        userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
             Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"),100);
            }
        });

            return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&resultCode==RESULT_OK && data!=null && data.getData()!=null)
        {
            filePath=data.getData();
            try
            {
                Bitmap bitmap= MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),filePath);
                userImage.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private class UpdateProfile extends AsyncTask<User,Void,Boolean>
    {
        private Boolean b;
        ProgressDialog progressDialog;
        User user;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            b=false;
            progressDialog=new ProgressDialog(getActivity());
            progressDialog.setTitle("QuesAn");
            progressDialog.setMessage("Updating");
            progressDialog.show();

        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            progressDialog.dismiss();
            if(aBoolean)
            {
                Toast.makeText(getActivity(), "profile Updated", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(getActivity(),"Check YOur internet Connection",Toast.LENGTH_LONG).show();
            }
        }

        @Override
        protected Boolean doInBackground(User... params)
        {
            user=params[0];
            FirebaseStorage firebaseStorage=FirebaseStorage.getInstance();


            StorageReference storagereference=firebaseStorage.getReference(user.getPicture());

            if(filePath!=null) {
                storagereference.putFile(filePath).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                        }
                    }
                });
            }
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
            reference.child(user.getUserId()).setValue(user);
            return true;

        }
    }


    /*
    //fetching detatils
     DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Users");


    reference.addChildEventListener(new ChildEventListener() {
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        User user=dataSnapshot.getValue(User.class);
        if(user.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

            firstName.setText(user.getFirstName());
            lastName.setText(user.getLastName());
            phno.setText(user.getPhone());
            address.setText(user.getAddress());


        }



*/

    }
