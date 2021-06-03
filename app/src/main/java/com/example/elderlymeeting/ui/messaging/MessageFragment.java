package com.example.elderlymeeting.ui.messaging;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.elderlymeeting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Objects;

public class MessageFragment extends Fragment {

    private EditText messageInput;
    private String message, date, nameString, sender, receiver;
    private LocalDateTime time;
    View view;
    ListView listView;
    ArrayList<MessageList> messageList = new ArrayList<>();

    //set message receiver to the match you clicked on
    public MessageFragment(String currentMatch) {
        receiver = currentMatch;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_message, container, false);

        //get current user ID
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        assert firebaseUser != null;
        String id = firebaseUser.getUid();

        listView = (ListView) view.findViewById(R.id.messageList);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference chatReference = firebaseDatabase.getReference("Chats");
        DatabaseReference userReference = firebaseDatabase.getReference("Users");

        //get name of sender to display with the messages
        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                nameString = snapshot.child(id).child("fullName").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        //display all sent messages between the current user and a match of theirs,
        // with the name of the sender and the date/time
        chatReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                messageList.clear();
                for(DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if((Objects.equals(childDataSnapshot.child("receiver")
                            .getValue(String.class), receiver)
                            && Objects.equals(childDataSnapshot.child("sender")
                            .getValue(String.class), id))
                            ||
                            (Objects.equals(childDataSnapshot.child("receiver")
                                    .getValue(String.class), id)
                                    && Objects.equals(childDataSnapshot.child("sender")
                                    .getValue(String.class), receiver)) ) {
                        message = childDataSnapshot.child("message").getValue(String.class);
                        date = childDataSnapshot.child("date").getValue(String.class);
                        sender = childDataSnapshot.child("senderName").getValue(String.class);

                        messageList.add(new MessageList(sender, date, message));
                    }
                }
                MessageArrayAdapter messageArrayAdapter =
                        new MessageArrayAdapter(view.getContext(), 0, messageList);
                listView.setAdapter(messageArrayAdapter);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        Button sendButton = (Button) view.findViewById(R.id.sendButton);
        messageInput = (EditText) view.findViewById(R.id.messageInput);

        //send a message to your match
        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                message = messageInput.getText().toString();
                time = LocalDateTime.now();
                //must be input to send a message
                if(!message.equals("")){
                    sendMessage(id, nameString, receiver, message, time);
                }
                //set message input to an empty string
                messageInput.setText("");

            }
        });

        //clear input
        messageInput.setText("");

        return view;
    }

    //push a message to the database
    private void sendMessage(String sender, String senderName, String receiver, String message, LocalDateTime time){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("senderName", senderName);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("date", dtf.format(time));

        reference.child("Chats").push().setValue(hashMap);
    }
}
