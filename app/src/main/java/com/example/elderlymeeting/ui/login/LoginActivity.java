package com.example.elderlymeeting.ui.login;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elderlymeeting.HomeActivity;
import com.example.elderlymeeting.R;
import com.example.elderlymeeting.ui.registration.RegisterPage;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {
    Button registerBtn;
    Button loginBtn;

    EditText emailEditText, passwordEditText;
    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    //if user is still logged in, go to the home screen
    protected void onStart() {
        super.onStart();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null){
            Intent i = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(i);
            finish();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Edit fields
        emailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);

        //if the user has no account yet, go to the register page
        registerBtn = findViewById(R.id.register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterPage.class);
                startActivity(i);
            }
        }
        );

        auth = FirebaseAuth.getInstance();

        //if the user has an account, but is not logged in: log in
        loginBtn = findViewById(R.id.login);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //set email and password
                String emailText = emailEditText.getText().toString();
                String passwordText = passwordEditText.getText().toString();

                //check if all fields are filled in
                if (TextUtils.isEmpty(emailText) || TextUtils.isEmpty(passwordText)) {
                    Toast.makeText(LoginActivity.this, "Please fill in all Fields",
                            Toast.LENGTH_SHORT).show();
                } else {
                    //use firebase authentication to log in
                    auth.signInWithEmailAndPassword(emailText, passwordText)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Intent i = new Intent(LoginActivity.this,
                                        HomeActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                        Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this,
                                        "Login credentials unknown", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });


    }
}