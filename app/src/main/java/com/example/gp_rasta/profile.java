package com.example.gp_rasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class profile extends AppCompatActivity {
    private FirebaseUser user;
    private DatabaseReference reference;
    private String user_ID;
    FirebaseDatabase database;
    //User u=new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database=FirebaseDatabase.getInstance();
        Intent intent = getIntent();

        //String Email = intent.getStringExtra("Email");
        //String Name=intent.getStringExtra("Username");

        Button f = findViewById(R.id.follow);
        final TextView Full_Name=(TextView)findViewById(R.id.username);
        final TextView E_mail=(TextView)findViewById(R.id.mail);
        //String first=u.firstName;
        f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        user= FirebaseAuth.getInstance().getCurrentUser();
        reference=database.getInstance().getReference("User");
        user_ID=user.getUid();
        reference.child(user_ID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user_profile=snapshot.getValue(User.class);
                if(user_profile!=null)
                {
                    String name=user_profile.firstName;
                    String email=user_profile.Email;
                    Full_Name.setText(name);
                    E_mail.setText(email);
                }
                else
                {
                    Toast.makeText(profile.this, "No Information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.Home_Page:
                Intent Homepage_intent = new Intent(profile.this,MainActivity.class);
                startActivity(Homepage_intent);
                return true;
            case  R.id.Log_Out:
                Intent Logout_intent = new Intent(profile.this,Login.class);
                startActivity(Logout_intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}