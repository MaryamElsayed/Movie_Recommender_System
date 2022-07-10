package com.example.gp_rasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Register extends AppCompatActivity {
    Button signup;
    EditText first_name, last_name, email, pass_word, confirm_pass, phone_no;
    FirebaseAuth mAuth;
    TextView login;
    FirebaseDatabase rootNode;
    DatabaseReference reference,ref1,ref2;
    FirebaseUser users;
    List<User> user_list = new ArrayList<User>();
    int id;
    private String user_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        first_name = findViewById(R.id.first_name);
        last_name = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        pass_word = findViewById(R.id.password_signup);
        confirm_pass = findViewById(R.id.confirm_password);
        phone_no = findViewById(R.id.phoneNo);
        signup = findViewById(R.id.btn_signup);
        login = findViewById(R.id.Login);
        reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_list.clear();
                for(DataSnapshot snapshot1:snapshot.getChildren())
                {
                    user_list.add(snapshot1.getValue(User.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        mAuth = FirebaseAuth.getInstance();
        //register
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
        //login
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });
    }


    public void registerUser() {
        //user2= new User();
        String firstName = first_name.getText().toString();
        String lastName = last_name.getText().toString();
        String mail = email.getText().toString();
        String password = pass_word.getText().toString().trim();
        String confirmPassword = confirm_pass.getText().toString().trim();
        String phoneNo = phone_no.getText().toString().trim();
        id=user_list.size();
        id++;
        // reference.child(user_ID).setValue(user);

        if (firstName.isEmpty()) {
            first_name.setError("first name is required!");
            email.requestFocus();
            return;
        }
        if (lastName.isEmpty()) {
            last_name.setError("Last name is required!");
            last_name.requestFocus();
            return;
        }
        if (mail.isEmpty()) {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            email.setError("Enter the valid email address!");
            email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            pass_word.setError("password is required!");
            pass_word.requestFocus();
            return;
        }
        if (password.length() < 6) {
            pass_word.setError("Length of the password should be more than 6");
            pass_word.requestFocus();
            return;
        }
        if (!confirmPassword.equals(password)) {
            Toast.makeText(Register.this, "passwords don't match!", Toast.LENGTH_SHORT).show();
        }

        users = FirebaseAuth.getInstance().getCurrentUser();
        reference = rootNode.getInstance().getReference("User");
        mAuth.createUserWithEmailAndPassword(mail, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    //user_ID=user.getUid();
                   // Toast.makeText(Register.this, "ID: " + user_ID, Toast.LENGTH_SHORT).show();
                    User user2 = new User(firstName, lastName, mail, phoneNo,id);
                    user2.setFirstName(firstName);
                    user2.setLastName(lastName);
                    user2.setEmail(mail);
                    user2.setPhoneNo(phoneNo);
                    user2.setId(id);
                    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                    reference.child(currentFirebaseUser.getUid()).setValue(user2);
                    //create object from class user
                    Toast.makeText(Register.this, "Registerd Successfully ", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(Register.this, Login.class);
//                    intent.putExtra("Username",firstName);
//                    intent.putExtra("Email",mail);
                    startActivity(intent);

                } else {
                    Toast.makeText(Register.this, "You Have already registerd by this email,Or this is a wrong email??!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}