package com.example.gp_rasta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    Button Login, sign_up;
    EditText email,pass_word;
    CheckBox rememberme;
    FirebaseAuth mAuth;
    TextView forget_password;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    boolean login ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.email);
        pass_word=findViewById(R.id.password_signup);
        sign_up=findViewById(R.id.btn_signup);
        Login=findViewById(R.id.btn_signin);
        rememberme=findViewById(R.id.remember);
        sharedPref = getSharedPreferences("remember me",MODE_PRIVATE);
        forget_password=findViewById(R.id.forget_password);
        mAuth= FirebaseAuth.getInstance();
        //check remember me
        checkLogin();
        //login
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUser();

            }

        });
        //forget password
        forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Forget_Password.class);
                startActivity(intent);
            }
        });
        //sign up
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register.class);
                startActivity(intent);

            }
        });

    }
   public void loginUser()
    {
        String mail = email.getText().toString().trim();
        String password= pass_word.getText().toString().trim();
        if(mail.isEmpty())
        {
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(mail).matches())
        {
            email.setError("Enter the valid email address!");
            email.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            pass_word.setError("password is required!");
            pass_word.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            pass_word.setError("Length of the password should be more than 6");
            pass_word.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user.isEmailVerified())
                    {

                       if(rememberme.isChecked())
                        {
                            keeplogin(mail,password);
                        }
                        Toast.makeText(Login.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Login.this, collaborative_recommend.class);
                        startActivity(intent);
                        /*Intent intent = new Intent(Login.this, Survey.class);
                        intent.putExtra("Email",email.getText().toString());
                        startActivity(intent);*/
                    }
                    else
                    {
                        user.sendEmailVerification();
                        Toast.makeText(Login.this,"check your mail for verification",Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(Login.this,"Logged in successfully",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(Login.this,"Check your email or password",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    protected void keeplogin(String userName, String Upass){
        editor = sharedPref.edit();
        editor.putString("username",userName);
        editor.putString("password",Upass);
        editor.putBoolean("login",true);
        editor.apply();
    }

    protected void checkLogin(){
        login = sharedPref.getBoolean("login",false);
        if(login == true)
        {
            Intent i = new Intent(Login.this,collaborative_recommend.class);
            startActivity(i);
        }
    }
}