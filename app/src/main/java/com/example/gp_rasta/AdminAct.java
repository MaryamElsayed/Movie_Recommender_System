package com.example.gp_rasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class AdminAct extends AppCompatActivity {
    Button b_add;
    EditText Name,Desc;
    DatabaseReference db;
    Query lastQuery;
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = FirebaseDatabase.getInstance().getReference("Movies");
        Name = findViewById(R.id.txt);
        Desc = findViewById(R.id.desc);
        b_add= findViewById(R.id.btn);
        Query lastQuery = db.orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    //if you call methods on dataSnapshot it gives you the required values
                    String key = data.getKey(); // then it has the value "4:"
                    temp = Integer.parseInt(key);
                    temp++;
                    //as per your given snapshot of firebase database data
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        b_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertData();
            }
        });

    }



    public void InsertData()
    {
        String name = Name.getText().toString();
        String desc = Desc.getText().toString();
        Movie movie = new Movie(name,desc);
        movie.setName(name);
        movie.setDescription(desc);
        String S_temp=String.valueOf(temp);

        db.child(S_temp).setValue(movie);
        Toast.makeText(this, "Movie added", Toast.LENGTH_SHORT).show();

    }
    }
