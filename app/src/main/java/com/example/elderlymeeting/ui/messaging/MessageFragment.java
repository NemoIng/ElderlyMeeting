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
import androidx.fragment.app.FragmentTransaction;

import com.example.elderlymeeting.R;
import com.example.elderlymeeting.ui.friends.FriendArrayAdapter;
import com.example.elderlymeeting.ui.friends.FriendsList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;
import java.util.Objects;

public class MessageFragment extends Fragment {

    private EditText messageInput;
    private String message, date, sender, senderID, messageSender;
    private LocalDateTime time;
    View view;

    ArrayList<MessageList> messageList = new ArrayList<>();

    String receiver;
    ListView listView;

    public MessageFragment(String currentMatch) {
        receiver = currentMatch;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = inflater.inflate(R.layout.fragment_message, container, false);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        assert firebaseUser != null;
        String id = firebaseUser.getUid();

        listView = (ListView) view.findViewById(R.id.messageList);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference chatReference = firebaseDatabase.getReference("Chats");
        DatabaseReference userReference = firebaseDatabase.getReference("Users");

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
                        senderID = childDataSnapshot.child("sender").getValue(String.class);
                        userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                messageSender = snapshot.child(senderID).child("fullName")
                                        .getValue(String.class);
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });
                        messageList.add(new MessageList(messageSender, date, message));
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

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                message = messageInput.getText().toString();
                time = LocalDateTime.now();
                //must be input to send a message
                if(!message.equals("")){
                    sendMessage(id, receiver, message, time);
                }
                //set message input to an empty string
                messageInput.setText("");

            }
        });

        //clear input
        messageInput.setText("");

        return view;
    }

    private void sendMessage(String sender, String receiver, String message, LocalDateTime time){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm dd/MM/yyyy");

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender", sender);
        hashMap.put("receiver", receiver);
        hashMap.put("message", message);
        hashMap.put("date", dtf.format(time));

        reference.child("Chats").push().setValue(hashMap);
    }
}
