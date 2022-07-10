package com.example.gp_rasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class deleteMovie extends AppCompatActivity {
    EditText movieId;
    Button deleteBtn;
    DatabaseReference db;
    int temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_movie);
        db = FirebaseDatabase.getInstance().getReference("Movies");
        movieId=findViewById(R.id.id_txt);
        deleteBtn=findViewById(R.id.d_btn);
        Query lastQuery = db.orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot data : snapshot.getChildren())
                {
                    //if you call methods on dataSnapshot it gives you the required values
                    String key = data.getKey(); // then it has the value "4:"
                     temp = Integer.parseInt(key);
                    //as per your given snapshot of firebase database data
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteMovie();
            }
        });

    }
    public void DeleteMovie()
    {
        String MOVIE_ID = movieId.getText().toString();
        int input_id = Integer.parseInt(MOVIE_ID);
        if(input_id>temp)
        {
            Toast.makeText(this, "movie not found", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this, "movie deleted", Toast.LENGTH_SHORT).show();
            db.child(MOVIE_ID).removeValue();
        }
    }
}