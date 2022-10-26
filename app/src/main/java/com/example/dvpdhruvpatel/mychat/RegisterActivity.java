package com.example.dvpdhruvpatel.mychat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class RegisterActivity extends AppCompatActivity {


    private FirebaseAuth mAuth;
    private DatabaseReference storeUserDeafultDataReference;

    private Toolbar mToolbar;
    private ProgressDialog lodingBaar;

    private EditText RegisterUsername;
    private EditText RegisterUseremail;
    private EditText RegisterUserPassword;
    private Button CreateAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();


        mToolbar = findViewById(R.id.regsiter_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("SIGN UP");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RegisterUsername = findViewById(R.id.register_name);
        RegisterUseremail = findViewById(R.id.register_email);
        RegisterUserPassword = findViewById(R.id.register_password);
        CreateAccountButton = findViewById(R.id.Register_btn);

        lodingBaar = new ProgressDialog(this);

        CreateAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                final String name = RegisterUsername.getText().toString();
                String email = RegisterUseremail.getText().toString();
                String password = RegisterUserPassword.getText().toString();

                RegisterAccount(name, email, password);
            }
        });

    }



    private void RegisterAccount(final String name, String email, String password)
    {

        if (TextUtils.isEmpty(name))
        {
            Toast.makeText(RegisterActivity.this,"Please Write Your Name....",Toast.LENGTH_LONG).show();
        }

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(RegisterActivity.this,"Please Write Your Email....",Toast.LENGTH_LONG).show();
        }

        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(RegisterActivity.this,"Please Write Your Password....",Toast.LENGTH_LONG).show();
        }

        else
        {
            lodingBaar.setTitle("Creating New Account..");
            lodingBaar.setMessage("Please Wait a Sec...");
            lodingBaar.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {
                    if (task.isSuccessful())
                    {
                        String device_tokens = FirebaseInstanceId.getInstance().getToken();
                        String current_User_Id = mAuth.getCurrentUser().getUid();
                        storeUserDeafultDataReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_User_Id);

                        storeUserDeafultDataReference.child("user_name").setValue(name);
                        storeUserDeafultDataReference.child("user_status").setValue("Hello, Welcome To DVP Chat developed by Dhruv Patel(DVP)");
                        storeUserDeafultDataReference.child("user_image").setValue("deafult_profile");
                        storeUserDeafultDataReference.child("device_token").setValue(device_tokens);
                        storeUserDeafultDataReference.child("user_thumb_image").setValue("deafult_image").addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task)
                            {
                                if (task.isSuccessful())
                                {
                                    Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }
                    else
                        {
                            Toast.makeText(RegisterActivity.this,"Error Occured, Please Try Again",Toast.LENGTH_SHORT).show();
                        }

                        lodingBaar.dismiss();
                }
            });
        }


    }
}
