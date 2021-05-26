package com.example.elderlymeeting.ui.seek;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.elderlymeeting.R;
import com.example.elderlymeeting.ui.messaging.MessageFragment;
import com.example.elderlymeeting.ui.registration.RegisterPage;
import com.example.elderlymeeting.ui.users.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class MatchFragment extends Fragment {
    View view;

    ImageView profilePicture, circle2, circle3, circle4, circle5, circle6;
    TextView fullName, email, bio, hobby1, hobby2, hobby3, hobby4, hobby5, hobby6, noMatches;
    Button matchBtn, nextBtn;

    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    String myID;

    ArrayList<String> IDs = new ArrayList<>();
    ListIterator<String> idList;

    int FRIENDLIMIT = 50;
    int friendListSize = 0;

    ArrayList<String> friends = new ArrayList<>();
    ListIterator<String> friendsList = friends.listIterator();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_match, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        myID = firebaseUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        fullName = (TextView) view.findViewById(R.id.fullName);
        email = (TextView) view.findViewById(R.id.email);
        profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        bio = (TextView) view.findViewById(R.id.biography);

        hobby1 = (TextView) view.findViewById(R.id.hobby1);
        hobby2 = (TextView) view.findViewById(R.id.hobby2);
        hobby3 = (TextView) view.findViewById(R.id.hobby3);
        hobby4 = (TextView) view.findViewById(R.id.hobby4);
        hobby5 = (TextView) view.findViewById(R.id.hobby5);
        hobby6 = (TextView) view.findViewById(R.id.hobby6);

        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if (!childDataSnapshot.getKey().equals(myID)){
                        IDs.add(childDataSnapshot.getKey());
                        idList = IDs.listIterator();
                        setProfile(idList.next());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });



        matchBtn = (Button) view.findViewById(R.id.matchBtn);
        matchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchUser();
            }
        });

        nextBtn = (Button) view.findViewById(R.id.nextBtn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idList.hasNext()){
                    setProfile(idList.next());
                }
                else{
                    noMatches = (TextView) view.findViewById(R.id.noMatches);
                    noMatches.setVisibility(view.VISIBLE);
                }
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    private void setProfile(String id){
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String fullNameString = snapshot.child(id).child("fullName").getValue(String.class);
                fullName.setText(fullNameString);

                String link = snapshot.child(id).child("profilePicture").getValue(String.class);
                Glide.with(view)
                        .load(link)
                        .centerCrop()
                        .override(300, 300)
                        .into(profilePicture);

                String bioString = snapshot.child(id).child("bio").getValue(String.class);
                bio.setText(bioString);

                String hobby1String = snapshot.child(id).child("hobbys").child("hobby1").getValue(String.class);
                hobby1.setText(hobby1String);

                String hobby2String = snapshot.child(id).child("hobbys").child("hobby2").getValue(String.class);
                circle2 = (ImageView) view.findViewById(R.id.circle2);
                if(!hobby2String.isEmpty()){
                    circle2.setVisibility(view.VISIBLE);
                }
                hobby2.setText(hobby2String);

                String hobby3String = snapshot.child(id).child("hobbys").child("hobby3").getValue(String.class);
                circle3 = (ImageView) view.findViewById(R.id.circle3);
                if(!hobby3String.isEmpty()){
                    circle3.setVisibility(view.VISIBLE);
                }
                hobby3.setText(hobby3String);

                String hobby4String = snapshot.child(id).child("hobbys").child("hobby4").getValue(String.class);
                circle4 = (ImageView) view.findViewById(R.id.circle4);
                if(!hobby4String.isEmpty()){
                    circle4.setVisibility(view.VISIBLE);
                }
                hobby4.setText(hobby4String);

                String hobby5String = snapshot.child(id).child("hobbys").child("hobby5").getValue(String.class);
                circle5 = (ImageView) view.findViewById(R.id.circle5);
                if(!hobby5String.isEmpty()){
                    circle5.setVisibility(view.VISIBLE);
                }
                hobby5.setText(hobby5String);

                String hobby6String = snapshot.child(id).child("hobbys").child("hobby6").getValue(String.class);
                circle6 = (ImageView) view.findViewById(R.id.circle6);
                if(!hobby6String.isEmpty()){
                    circle6.setVisibility(view.VISIBLE);
                }
                hobby6.setText(hobby6String);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    private void matchUser(){
        friendListSize = 0;
        String currentMatch = idList.previous();
        DatabaseReference friendsReference = databaseReference.child(myID).child("friends");
        friendsReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot datasnapshot) {
                for (DataSnapshot childDataSnapshot : datasnapshot.getChildren()) {
                    friendListSize++;
                }
                        friendsReference.child("friend" + (friendListSize+1)).setValue(currentMatch).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                toChat(currentMatch);
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    private void toChat(String currentMatch){
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new MessageFragment(currentMatch))
                .commit();
    }
}

