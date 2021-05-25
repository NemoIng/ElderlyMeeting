package com.example.elderlymeeting.ui.messaging;

import java.util.HashMap;
import java.util.Scanner;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.elderlymeeting.R;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MessageActivity extends AppCompatActivity {

    private EditText messageInput;
    private String message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        assert firebaseUser != null;
        String id = firebaseUser.getUid();

        Button sendButton = (Button) findViewById(R.id.sendButton);
        messageInput = (EditText) findViewById(R.id.messageInput);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                message = messageInput.getText().toString();
                //must be input to send a message
                if(!message.equals("")){
                    sendMessage(id, "testtesttesttest", message);
                }
                //set message input to an empty string
                messageInput.setText("");
            }
        });

        //clear input
        messageInput.setText("");
    }

    private void sendMessage(String sender, String receiver, String message){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);

        reference.child("Chats").push().setValue(hashMap);
    }
}