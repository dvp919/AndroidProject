package com.example.dvpdhruvpatel.mychat;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllUsersActivity extends AppCompatActivity {

     private Toolbar mToolbar;
     private RecyclerView allUsersList;

     private DatabaseReference allUsersDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        mToolbar = findViewById(R.id.all_users_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("All Users");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        allUsersList = findViewById(R.id.all_users_list);
        allUsersList.setHasFixedSize(true);
        allUsersList.setLayoutManager(new LinearLayoutManager(this));

        allUsersDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        allUsersDatabaseReference.keepSynced(true);
    }


    @Override
    protected void onStart() {
        super.onStart();



        FirebaseRecyclerAdapter<AllUsers,AllUsersViewHolder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<AllUsers, AllUsersViewHolder>
                        (AllUsers.class,
                        R.layout.all_users_display_layout,
                        AllUsersViewHolder.class,
                        allUsersDatabaseReference)
                {

                    @Override
                    protected void populateViewHolder(AllUsersViewHolder viewholder, AllUsers model, final int position) {
                        viewholder.setUser_name(model.getUser_name());
                        viewholder.setUser_status(model.getUser_status());
                        viewholder.setUser_thumb_image(getApplicationContext(),model.getUser_thumb_image());


                        viewholder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view)
                            {
                                String visit_user_id = getRef(position).getKey();

                                Intent profileIntent = new Intent(AllUsersActivity.this,ProfileActivity.class);
                                profileIntent.putExtra("visit_user_id",visit_user_id);
                                startActivity(profileIntent);
                            }
                        });
                    }
                };
        allUsersList.setAdapter(firebaseRecyclerAdapter);

    }

    public static class  AllUsersViewHolder extends RecyclerView.ViewHolder
    {
        View mView;

        public AllUsersViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setUser_name(String user_name)
        {
            TextView name = (TextView) mView.findViewById(R.id.all_users_username);
            name.setText(user_name);
        }

        public void  setUser_status(String user_status)
        {
            TextView status = (TextView) mView.findViewById(R.id.all_users_current_status);
            status.setText(user_status);
        }

        public void setUser_thumb_image(Context ctx,final String user_thumb_image)
        {
            final CircleImageView thumb_image = (CircleImageView) mView.findViewById(R.id.all_users_profile_image);





            Picasso.get().load(user_thumb_image).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.welcome_page)
                    .into(thumb_image, new Callback()
                    {
                        @Override
                        public void onSuccess()
                        {

                        }

                        @Override
                        public void onError(Exception e)
                        {
                            Picasso.get().load(user_thumb_image).placeholder(R.drawable.welcome_page).into(thumb_image);
                        }
                    });

        }
    }
}
