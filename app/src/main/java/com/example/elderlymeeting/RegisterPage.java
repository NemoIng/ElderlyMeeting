package com.example.elderlymeeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.elderlymeeting.ui.login.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Integer.parseInt;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextTextEmailAddress, editTextTextPassword, editTextTextPersonName, editTextNumber, editTextTextPassword2;
    private Button register, backToMain;

    private FirebaseAuth mAuth;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();

        backToMain = (Button) findViewById(R.id.backToMain);
        backToMain.setOnClickListener(this);

        register = (Button) findViewById(R.id.register);
        register.setOnClickListener(this);

        editTextNumber = (EditText) findViewById(R.id.editTextNumber);
        editTextTextEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        editTextTextPassword2 = (EditText) findViewById(R.id.editTextTextPassword2);
        editTextTextPersonName = (EditText) findViewById(R.id.editTextTextPersonName);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    private void registerUser() {
        String email = editTextTextEmailAddress.getText().toString().trim();
        String password = editTextTextPassword.getText().toString().trim();
        String password2 = editTextTextPassword2.getText().toString().trim();
        String fullName = editTextTextPersonName.getText().toString().trim();
        String age = editTextNumber.getText().toString().trim();

        if(fullName.isEmpty()){
            editTextTextPersonName.setError("Full name is required!");
            editTextTextPersonName.requestFocus();
            return;
        }
        if(age.isEmpty()){
            editTextNumber.setError("you must enter your age!");
            editTextNumber.requestFocus();
            return;
        }
        if(parseInt(age) < 18){
            editTextNumber.setError("you must be 18 years or older!");
            editTextNumber.requestFocus();
            return;
        }
        if(password.isEmpty()){
            editTextTextPassword.setError("Please enter a password!");
            editTextTextPassword.requestFocus();
            return;
        }
        if(password2.isEmpty()){
            editTextTextPassword2.setError("Please enter a password!");
            editTextTextPassword2.requestFocus();
            return;
        }
        if(!password2.equals(password)){
            editTextTextPassword2.setError("Passwords do not match!");
            editTextTextPassword2.requestFocus();
            return;
        }
        if(email.isEmpty()){
            editTextTextEmailAddress.setError("Email is required!");
            editTextTextEmailAddress.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextTextEmailAddress.setError("Please provide a valid email!");
            editTextTextEmailAddress.requestFocus();
            return;
        }
        if(password.length() < 6){
            editTextTextPassword.setError("You need a password of at least 6 character!");
            editTextTextPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(fullName, age, email);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>(){
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterPage.this, "User has been Registered succesfully!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);

                                        // redirect to login
                                    }
                                    else{
                                        Toast.makeText(RegisterPage.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                                        progressBar.setVisibility(View.GONE);
                                    }
                                }
                            });
                        }
                        else{
                            Toast.makeText(RegisterPage.this, "Failed to register! Try again!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.backToMain:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.register:
                registerUser();
                break;
        }
    }
}