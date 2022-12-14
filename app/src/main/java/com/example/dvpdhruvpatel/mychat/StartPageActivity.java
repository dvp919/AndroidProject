package com.example.dvpdhruvpatel.mychat;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartPageActivity extends AppCompatActivity {


    private Button NeedAccountButton , AlreadyAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_page);

        NeedAccountButton = findViewById(R.id.need_account_button);
        AlreadyAccountButton = findViewById(R.id.already_button);


        NeedAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StartPageActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

        AlreadyAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent1 = new Intent(StartPageActivity.this,LoginActivity.class);
                startActivity(intent1);

            }
        });
    }
}
