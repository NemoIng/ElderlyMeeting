package com.example.elderlymeeting.ui.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.elderlymeeting.HomeActivity;
import com.example.elderlymeeting.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterBio extends AppCompatActivity {

    private EditText bioEdit;

    private Button bioConfirmBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bio);

        mAuth = FirebaseAuth.getInstance();

        bioConfirmBtn = (Button) findViewById(R.id.bioConfirm);
        bioConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitBio();
            }
        });

        bioEdit = (EditText) findViewById(R.id.bioEdit);

    }

    private void submitBio(){
        String bio;

        bio = bioEdit.getText().toString().trim();

        if(bio.isEmpty()){
            bioEdit.setError("Please tell us something about yourself!");
            bioEdit.requestFocus();
            return;
        }
        if(isDigit(bio)){
            bioEdit.setError("Please only use letters!");
            bioEdit.requestFocus();
            return;
        }
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String id = firebaseUser.getUid();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child(id).child("bio");

        myRef.setValue(bio).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Intent i = new Intent(RegisterBio.this, HomeActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );

                    startActivity(i);
                    finish();
                }
            }
        });

    }

    public boolean isDigit(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(Character.isDigit(c)){
                return true;
            }
        }

        return false;
    }
}