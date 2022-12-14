package com.example.dvpdhruvpatel.mychat;

import android.app.Application;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttp3Downloader;
import com.squareup.picasso.Picasso;

public class dvpChat_offline extends Application
{

    private DatabaseReference userReference;
    private FirebaseAuth mAuth;
    private FirebaseUser currentuser;


    @Override
    public void onCreate()
    {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);


        //loac pictrue offline Picasso
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttp3Downloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(true);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);


        mAuth = FirebaseAuth.getInstance();
        currentuser = mAuth.getCurrentUser();

        if (currentuser != null)
        {
            String online_user_id = mAuth.getCurrentUser().getUid();

            userReference = FirebaseDatabase.getInstance().getReference().child("Users").child(online_user_id);

            userReference.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    userReference.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);

                }

                @Override
                public void onCancelled(DatabaseError databaseError)
                {

                }
            });
        }
    }
}
