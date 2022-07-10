package com.example.gp_rasta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class add_or_delete extends AppCompatActivity {
    Button add,delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_delete);
        add=findViewById(R.id.add_moviebtn);
        delete=findViewById(R.id.delete_moviebtn);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_or_delete.this, AdminAct.class);
                startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(add_or_delete.this, deleteMovie.class);
                startActivity(intent);
            }
        });
    }
}