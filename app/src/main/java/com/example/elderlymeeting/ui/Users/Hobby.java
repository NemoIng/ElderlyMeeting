package com.example.elderlymeeting.ui.Users;

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

import static java.lang.Integer.parseInt;

public class Hobby extends AppCompatActivity {

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
        editHobby5 = (EditText) findViewById(R.id.editHobby5);
        editHobby6 = (EditText) findViewById(R.id.editHobby6);
    }

    private void submitHobby(){
        String hobby1, hobby2, hobby3, hobby4, hobby5, hobby6;

        hobby1 = editHobby1.getText().toString().trim();
        hobby2 = editHobby2.getText().toString().trim();
        hobby3 = editHobby3.getText().toString().trim();
        hobby4 = editHobby4.getText().toString().trim();
        hobby5 = editHobby5.getText().toString().trim();
        hobby6 = editHobby6.getText().toString().trim();

        if(hobby1.isEmpty()){
            editHobby1.setError("You need to add at least one hobby!");
            editHobby1.requestFocus();
            return;
        }
        if(!isAlphabet(hobby1)){
            editHobby1.setError("Please only use letters!");
            editHobby1.requestFocus();
            return;
        }
        if(!isAlphabet(hobby2)){
            editHobby2.setError("Please only use letters!");
            editHobby2.requestFocus();
            return;
        }
        if(!isAlphabet(hobby3)){
            editHobby3.setError("Please only use letters!");
            editHobby3.requestFocus();
            return;
        }
        if(!isAlphabet(hobby4)){
            editHobby4.setError("Please only use letters!");
            editHobby4.requestFocus();
            return;
        }
        if(!isAlphabet(hobby5)){
            editHobby5.setError("Please only use letters!");
            editHobby5.requestFocus();
            return;
        }
        if(!isAlphabet(hobby6)){
            editHobby6.setError("Please only use letters!");
            editHobby6.requestFocus();
            return;
        }

        Hobbys hobbys = new Hobbys(hobby1, hobby2, hobby3, hobby4, hobby5, hobby6);
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        String id = firebaseUser.getUid();

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("Users").child(id).child("hobbys");

        myRef.setValue(hobbys).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){

                    Intent i = new Intent(Hobby.this, Bio.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );

                    startActivity(i);
                    finish();
                }
            }
        });

    }

    public boolean isAlphabet(String name) {
        char[] chars = name.toCharArray();

        for (char c : chars) {
            if(!Character.isLetter(c)) {
                return false;
            }
        }

        return true;
    }
}