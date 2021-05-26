package com.example.elderlymeeting.ui.messaging;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

import com.example.elderlymeeting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MessageFragment extends Fragment {

    private EditText messageInput;
    private String message;
    View view;

    String receiver;

    public MessageFragment(String currentMatch) {
        receiver = currentMatch;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_message, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        assert firebaseUser != null;
        String id = firebaseUser.getUid();

        Button sendButton = (Button) view.findViewById(R.id.sendButton);
        messageInput = (EditText) view.findViewById(R.id.messageInput);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                message = messageInput.getText().toString();
                //must be input to send a message
                if(!message.equals("")){
                    sendMessage(id, receiver, message);
                }
                //set message input to an empty string
                messageInput.setText("");
            }
        });

        //clear input
        messageInput.setText("");

        return view;
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
