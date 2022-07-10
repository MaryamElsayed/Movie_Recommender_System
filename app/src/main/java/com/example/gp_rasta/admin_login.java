package com.example.gp_rasta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class admin_login extends AppCompatActivity {
    EditText userName,passWord;
    Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);
        userName=findViewById(R.id.admin_username);
        passWord=findViewById(R.id.admin_pasword);
        login=findViewById(R.id.Login_Admin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String USERNAME = userName.getText().toString().trim();
                String PASSWORD= passWord.getText().toString().trim();
                if(USERNAME.isEmpty())
                {
                    userName.setError("Email is required!");
                    userName.requestFocus();
                    return;
                }

                if(PASSWORD.isEmpty())
                {
                    passWord.setError("password is required!");
                    passWord.requestFocus();
                    return;
                }
                if(USERNAME.equals("admin")&&PASSWORD.equals("123"))
                {
                    Intent intent = new Intent(admin_login.this, add_or_delete.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(admin_login.this, "Check username or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}