package com.quesan.app.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.quesan.app.R;
import com.quesan.app.model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Dinesh Singh on 8/19/2017.
 */

public class UserAdapter extends BaseAdapter

{
private ArrayList<User> userArrayList;
private Context context;

    public UserAdapter(ArrayList<User> userArrayList, Context context) {
        this.userArrayList = userArrayList;
        this.context = context;
    }


    @Override
    public int getCount()
    {
        return userArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return userArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {

    //inflating the View object
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view=inflater.inflate(R.layout.layout_row_user,parent,false);


        //initializing
        final ImageView user_image=(ImageView)view.findViewById(R.id.user_image);
        TextView user_name=(TextView)view.findViewById(R.id.user_name);

        TextView user_id=(TextView)view.findViewById(R.id.user_id);

        //setting the properties
        User user=(User)getItem(position);
        user_name.setText(user.getFirstName()+""+user.getLastName());
        user_id.setText(user.getUserId());

        StorageReference reference= FirebaseStorage.getInstance().getReference().child(user.getUserId()+".jpg");

        try {
            final File localFile = File.createTempFile("images", "jpg");
            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    user_image.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    e.printStackTrace();
                }
            });
        }
         catch (IOException e)
         {
             e.printStackTrace();
         }

         return view;

    }
}
