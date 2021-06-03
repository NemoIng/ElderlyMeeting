package com.example.elderlymeeting.ui.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.elderlymeeting.MainActivity;
import com.example.elderlymeeting.R;
import com.example.elderlymeeting.ui.users.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Integer.parseInt;

public class RegisterPage extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextTextEmailAddress, editTextTextPassword, editTextTextPersonName, editTextNumber, editTextTextPassword2;
    private Button registerBtn, backToMainBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        mAuth = FirebaseAuth.getInstance();

        backToMainBtn = (Button) findViewById(R.id.backToMain);
        backToMainBtn.setOnClickListener(this);

        registerBtn = (Button) findViewById(R.id.register);
        registerBtn.setOnClickListener(this);

        editTextNumber = (EditText) findViewById(R.id.editTextNumber);
        editTextTextEmailAddress = (EditText) findViewById(R.id.editTextTextEmailAddress);
        editTextTextPassword = (EditText) findViewById(R.id.editTextTextPassword);
        editTextTextPassword2 = (EditText) findViewById(R.id.editTextTextPassword2);
        editTextTextPersonName = (EditText) findViewById(R.id.editTextTextPersonName);
    }

    private void registerUser() {
        String email, password, password2, fullName, age;

        //set values for user profile
        email = editTextTextEmailAddress.getText().toString().trim();
        password = editTextTextPassword.getText().toString().trim();
        password2 = editTextTextPassword2.getText().toString().trim();
        fullName = editTextTextPersonName.getText().toString().trim();
        age = editTextNumber.getText().toString().trim();

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
        if(parseInt(age) > 150){
            editTextNumber.setError("please fill in your real age!");
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
        //both passwords must match
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
        //password must be of sufficient length
        if(password.length() < 6){
            editTextTextPassword.setError("You need a password of at least 6 character!");
            editTextTextPassword.requestFocus();
            return;
        }
        //push users account to database
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(auth -> {
                    if(auth.isSuccessful()){
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        String id = firebaseUser.getUid();
                        Users users = new Users(id, fullName, age, email, null, null,
                                null);

                        DatabaseReference myRef = FirebaseDatabase.getInstance()
                                .getReference("Users").child(id);

                                myRef.setValue(users)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegisterPage.this,
                                                    "User has been Registered succesfully!",
                                                    Toast.LENGTH_LONG).show();

                                            //pick a profile picture
                                            Intent i = new Intent(RegisterPage.this,
                                                    RegisterPicture.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent
                                                    .FLAG_ACTIVITY_NEW_TASK );
                                            startActivity(i);
                                            finish();
                                        }
                                    }
                                });

                     }
                    else{
                        Toast.makeText(RegisterPage.this,
                                "Failed to register! Try again!", Toast.LENGTH_LONG).show();
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
