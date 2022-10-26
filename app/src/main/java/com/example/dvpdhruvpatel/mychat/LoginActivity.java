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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class LoginActivity extends AppCompatActivity {


    private Toolbar mToolbar;
    private EditText LoginEmail;
    private EditText LoginPassword;
    private Button LoginButton;

    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    private DatabaseReference userReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        userReference = FirebaseDatabase.getInstance().getReference().child("Users");

        mToolbar = findViewById(R.id.login_app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Sign In");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LoginButton = findViewById(R.id.Login_btn);
        LoginEmail = findViewById(R.id.EmailLogin);
        LoginPassword = findViewById(R.id.PassLogin);
        loadingBar = new ProgressDialog(this);

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                String email = LoginEmail.getText().toString();
                String password  = LoginPassword.getText().toString();

                LoginUserAccount(email,password);

            }
        });
    }

    private void LoginUserAccount(String email, String password)
    {

        if (TextUtils.isEmpty(email))
        {
            Toast.makeText(LoginActivity.this,"Please Fill Email Field..",Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(password))
        {
            Toast.makeText(LoginActivity.this,"Please Fill password Field..",Toast.LENGTH_LONG).show();
        }

        else
        {
            loadingBar.setTitle("Login Account..");
            loadingBar.setMessage("Please Wait While We Are Verifing Email and Password..");
            loadingBar.show();
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task)
                {

                    if (task.isSuccessful())
                    {

                        String online_user_id = mAuth.getCurrentUser().getUid();
                        String device_tokens = FirebaseInstanceId.getInstance().getToken();

                        userReference.child(online_user_id).child("device_token").setValue(device_tokens)
                                 .addOnSuccessListener(new OnSuccessListener<Void>()
                        {
                            @Override
                            public void onSuccess(Void aVoid)
                            {
                                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            }
                        });


                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Wrong Email and Password, Please Check Email And Password Again..",Toast.LENGTH_LONG).show();
                    }

                    loadingBar.dismiss();
                }
            });

        }

    }
}
