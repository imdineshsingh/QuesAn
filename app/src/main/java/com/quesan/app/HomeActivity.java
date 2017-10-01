package com.quesan.app;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.quesan.app.model.User;

import java.io.File;
import java.security.Key;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener

{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //displaying qustion list on home screen
        changeFragment(new QuestionListFragment());

        new CheckInternet().execute();  //executing checkInternet Method

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                changeFragment(new PostFragment());

            }
        });


        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();



        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);


        View view= navigationView.getHeaderView(0);

        //for UserProfileFragment
        TextView textViewEmail= (TextView) view.findViewById(R.id.textViewEmail);
        textViewEmail.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
        final ImageView profilePic=(ImageView)view.findViewById(R.id.imageView);
        ImageView imageView=(ImageView)view.findViewById(R.id.btnEditProfile);


        //Fetching UserName

        final TextView userName= (TextView) view.findViewById(R.id.userName);
        final DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s)
            {
                User u= dataSnapshot.getValue(User.class);
                if(u.getUserId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid()))
                {
                    userName.setText(u.getFirstName()+" "+u.getLastName());
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

        StorageReference reference= FirebaseStorage.getInstance().getReference().child(FirebaseAuth.getInstance().getCurrentUser().getUid()+".jpg");
        try
        {
            final File localFile = File.createTempFile("images", "jpg");

            reference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot)
                {
                    Bitmap bitmap= BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    profilePic.setImageBitmap(bitmap);


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

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                    changeFragment(new UserProfileFragment());
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);


            }
        });

        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public void onBackPressed()
    {
        /*
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        */

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QusAn");
        builder.setMessage("Are You Sure you want to exit");
        builder.setIcon(R.drawable.logo);

        builder.setCancelable(false);
        builder.setPositiveButton("yes", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                HomeActivity.this.finish();

            }
        }));

        builder.setNegativeButton("NO", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }));

        builder.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
          if (id    == R.id.action_logout) {
              FirebaseAuth.getInstance().signOut();
      Intent intent = new Intent(HomeActivity.this, MainActivity.class);
      startActivity(intent);
      finish();

  }
  else if(id==R.id.action_message)
          {
              Fragment f=new InboxFragment();
              Bundle b=new Bundle();
              b.putString("title","Inbox");
              f.setArguments(b);
              changeFragment(f);

          }
        else if(id==R.id.changePassword)
          {

          }

        return super.onOptionsItemSelected(item);
    }

 /*   @Override
    public void onPause()
    {
        finish();
    }
*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        if (id == R.id.nav_home)
        {
            changeFragment(new QuestionListFragment());

        }
        else if (id == R.id.nav_post)
        {
                changeFragment(new PostFragment());
        }
        else if (id == R.id.nav_unanswered)
        {
            changeFragment(new UnansweredQuestionFragment());
        }
        else if (id == R.id.nav_myQuestion)
        {
            Fragment f=new QuestionListFragment();
            Bundle b=new Bundle();
            b.putString("title","My Question");
            f.setArguments(b);
            changeFragment(f);

        }

        else if (id == R.id.nav_users)
        {
                changeFragment(new UserFragment());
        }
        else if (id == R.id.nav_about)
        {
        }
        else if(id==R.id.nav_rateus)
        {

            changeFragment(new RateFragment());
        }
        else if(id==R.id.nav_share)
        {

            Intent intent=new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            String shareBody="https://play.google.com/store/apps/details?id=com.quesan.app";
            String shareSub="Your subject is here";
            intent.putExtra(Intent.EXTRA_SUBJECT,shareSub);
            intent.putExtra(Intent.EXTRA_TEXT,shareBody);
            startActivityForResult(Intent.createChooser(intent,"Share Via"),123);

        }
        else if(id==R.id.sentMessages)
        {
            Fragment   fragment=new InboxFragment();
            Bundle bundle=new Bundle();
            bundle.putString("title","Outbox");
            fragment.setArguments(bundle);
            changeFragment(fragment);
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==123&&resultCode==RESULT_OK)
        {
            Toast.makeText(HomeActivity.this,"sent",Toast.LENGTH_LONG).show();
            //data.getDataString()
        }
    }

    public void changeFragment(Fragment fragment)
    {
        getFragmentManager().beginTransaction().replace(R.id.placeholder,fragment).commit();
    }


// check internet Connection

    private class CheckInternet extends AsyncTask<Void,Void,Boolean>
    {
        ProgressDialog pd;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd=new ProgressDialog(HomeActivity.this);
            pd.setMessage("Checking Internet Connection");
            pd.setTitle("Qusec");
            pd.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean)
        {
            super.onPostExecute(aBoolean);
            pd.dismiss();
           if(!aBoolean)

               Toast.makeText(HomeActivity.this, "Check your Internet Connection", Toast.LENGTH_LONG).show();

        }

        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {

                ConnectivityManager manager=(ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);

                return  manager.getActiveNetworkInfo().isConnected();


                  /*  URL url = new URL("https://www.google.com");
                    HttpURLConnection conn=(HttpURLConnection) url.openConnection();
                    InputStream stream= conn.getInputStream();
                    if(stream!=null && stream.available()>0)
                        return true;
                    return false;*/

            }
            catch (Exception ex)
            {
                Log.d("Exception",ex.toString());

            }

            return false;
        }
    }
/*
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
    if(!hasFocus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("QusAn");
        builder.setMessage("Are You Sure you want to exit");
        builder.setCancelable(false);
        builder.setPositiveButton("yes", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                HomeActivity.this.finish();

            }
        }));

        builder.setNegativeButton("NO", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }));

        builder.show();

    }

    }
*/
    //  Method for Envoking the alert dialog to leave the app
/*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

    if(keyCode== KeyEvent.KEYCODE_BACK )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this,R.style.myDialogTheme);
        builder.setTitle("QusAn");
        builder.setMessage("Are You Sure you want to exit");
        builder.setIcon(R.drawable.logo);
        builder.setCancelable(false);
        builder.setPositiveButton("yes", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                HomeActivity.this.finish();

            }
        }));

        builder.setNegativeButton("NO", (new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }));

        builder.show();
        return super.onKeyDown(keyCode, event);

    }
    else
    {
        return super.onKeyDown(keyCode,event);
    }

    }

  */




}



