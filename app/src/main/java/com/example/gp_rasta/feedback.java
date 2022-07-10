package com.example.gp_rasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class feedback extends AppCompatActivity {
    EditText tweet;
    Button submit;
    int id;
    String user_ID;
    int temp;
    int userId;

    FirebaseDatabase database;
    DatabaseReference ref,ref2,ref3;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        getIntent() ;
        Intent myIntent = getIntent(); // gets the previously created intent
        String moviename = myIntent.getStringExtra("name");

        //Toast.makeText(this, moviename, Toast.LENGTH_SHORT).show();
         ref= FirebaseDatabase.getInstance().getReference().child("Movies");
        tweet=findViewById(R.id.tweet);
        submit=findViewById(R.id.submit_btn);
        ref.addValueEventListener(new ValueEventListener(){
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    String Name=data.child("original_title").getValue().toString();
                    if(Name.equals(moviename))
                    {
                        id=Integer.parseInt(data.child("movieId").getValue().toString());
                        //Toast.makeText(feedback.this, "id"+id, Toast.LENGTH_SHORT).show();
                        break;
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        ref= FirebaseDatabase.getInstance().getReference().child("Rating");
        Query lastQuery = ref.orderByKey().limitToLast(1);
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InsertData();
            }
        });

    }

    public void InsertData()
    {

        user= FirebaseAuth.getInstance().getCurrentUser();
        ref=database.getInstance().getReference("User");
        ref2 = ref.child(user.getUid());
        ref3 = ref2.child("userId");
        ref3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_ID= String.valueOf(snapshot.getValue(long.class));
               // Toast.makeText(feedback.this, "string"+user_ID, Toast.LENGTH_SHORT).show();
                userId=Integer.parseInt(user_ID);
               // Toast.makeText(feedback.this, "int"+userId, Toast.LENGTH_SHORT).show();
                String Tweet=tweet.getText().toString() ;
                int Polarity_Score=0;
                int movieId = id;
                int rating=0;
                //int U = userId;
                Rank rank = new Rank(Tweet,Polarity_Score,movieId,rating,userId);
                rank.setMovieTweets(Tweet);
                rank.setPolarity_Score(Polarity_Score);
                rank.setMovieId(movieId);
                rank.setUserId(userId);
                String S_temp=String.valueOf(temp);
                ref=database.getInstance().getReference("Rating");
                ref.child(S_temp).setValue(rank);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                error.toException();

            }
        });
        Toast.makeText(this, "Thank you for your feedback", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(feedback.this, collaborative_recommend.class);
        startActivity(intent);


    }
}