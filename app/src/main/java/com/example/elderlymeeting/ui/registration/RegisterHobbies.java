package com.example.elderlymeeting.ui.registration;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.elderlymeeting.R;
import com.example.elderlymeeting.ui.users.Hobbies;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Integer.parseInt;

public class RegisterHobbies extends AppCompatActivity {

    private EditText editHobby1, editHobby2, editHobby3, editHobby4, editHobby5, editHobby6;

    private Button hobbyConfirmBtn;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hobby);

        mAuth = FirebaseAuth.getInstance();

        hobbyConfirmBtn = (Button) findViewById(R.id.hobbyConfirm);
        hobbyConfirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitHobby();
            }
            });

        editHobby1 = (EditText) findViewById(R.id.editHobby1);
        editHobby2 = (EditText) findViewById(R.id.editHobby2);
        editHobby3 = (EditText) findViewById(R.id.editHobby3);
        editHobby4 = (EditText) findViewById(R.id.editHobby4);
    }

    //let user write down a maximum of 6 and a minimum of 1 hobbies
    private void submitHobby(){
        String hobby1, hobby2, hobby3, hobby4;

        hobby1 = editHobby1.getText().toString().trim();
        hobby2 = editHobby2.getText().toString().trim();
        hobby3 = editHobby3.getText().toString().trim();
        hobby4 = editHobby4.getText().toString().trim();

        if(hobby1.isEmpty()){
            editHobby1.setError("You need to add at least one hobby!");
            editHobby1.requestFocus();
            return;
        }
        if(isDigit(hobby1)){
            editHobby1.setError("Please only use letters!");
            editHobby1.requestFocus();
            return;
        }
        if(isDigit(hobby2)){
            editHobby2.setError("Please only use letters!");
            editHobby2.requestFocus();
            return;
        }
        if(isDigit(hobby3)){
            editHobby3.setError("Please only use letters!");
            editHobby3.requestFocus();
            return;
        }
        if(isDigit(hobby4)){
            editHobby4.setError("Please only use letters!");
            editHobby4.requestFocus();
            return;
        }

        Hobbies hobbies = new Hobbies(hobby1, hobby2, hobby3, hobby4);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String id = firebaseUser.getUid();
        //push to database
        DatabaseReference myRef = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(id)
                .child("hobbys");

        myRef.setValue(hobbies).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Intent i = new Intent(RegisterHobbies.this, RegisterBio.class);
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