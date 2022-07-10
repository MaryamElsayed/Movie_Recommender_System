package com.example.gp_rasta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class adminOrUser extends AppCompatActivity {
    Button b_admin,b_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_or_user);
        b_admin=findViewById(R.id.admin);
        b_user=findViewById(R.id.user);
        b_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminOrUser.this, Login.class);
                startActivity(intent);
            }
        });
        b_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(adminOrUser.this, admin_login.class);
                startActivity(intent);
            }
        });
    }
}