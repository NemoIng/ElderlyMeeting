package com.example.elderlymeeting.ui.seek;

import android.os.Bundle;
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
import com.example.elderlymeeting.ui.registration.RegisterPicture;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.ListIterator;
import java.util.Objects;

public class MatchFragment extends Fragment {
    View view;

    ImageView profilePicture, circle1, circle2, circle3, circle4;
    TextView fullName, age, email, bio, hobbyText, hobby1, hobby2, hobby3, hobby4, hobby5, hobby6,
            noMatches;
    Button matchBtn, nextBtn, chatBtn, nextBtn2, backBtn;

    DatabaseReference databaseReference, friendsReference;
    private FirebaseAuth mAuth;
    String myID, currentMatch;

    ArrayList<String> IDs = new ArrayList<>();
    ListIterator<String> idList;

    int friendListSize = 0;
    int i;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_match, container, false);

        //get current user ID
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        myID = firebaseUser.getUid();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        friendsReference = databaseReference.child(myID).child("friends");

        fullName = (TextView) view.findViewById(R.id.fullName);
        age = (TextView) view.findViewById(R.id.age);
        email = (TextView) view.findViewById(R.id.email);
        profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        bio = (TextView) view.findViewById(R.id.biography);

        noMatches = (TextView) view.findViewById(R.id.noMatches);

        hobbyText = (TextView) view.findViewById(R.id.hobbies);
        hobby1 = (TextView) view.findViewById(R.id.hobby1);
        hobby2 = (TextView) view.findViewById(R.id.hobby2);
        hobby3 = (TextView) view.findViewById(R.id.hobby3);
        hobby4 = (TextView) view.findViewById(R.id.hobby4);

        chatBtn = (Button) view.findViewById(R.id.chatBtn);
        chatBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toChat(currentMatch);
            }
        });

        matchBtn = (Button) view.findViewById(R.id.matchBtn);
        matchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                matchUser();
            }
        });

        nextBtn2 = (Button) view.findViewById(R.id.nextBtn2);
        nextBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextBtn2.setVisibility(view.GONE);
                nextBtn.setVisibility(view.getVisibility());
                chatBtn.setVisibility(view.GONE);
                matchBtn.setVisibility(view.getVisibility());
                setProfile(idList.next());
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
                    noMatches.setVisibility(view.VISIBLE);
                }
            }
        });

        backBtn = (Button) view.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                assert getFragmentManager() != null;
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new SeekFragment())
                        .commit();
            }
        });
        //show all users located in the database
        databaseReference.addListenerForSingleValueEvent( new ValueEventListener() {
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    if (!Objects.equals(childDataSnapshot.getKey(), myID)) {
                        IDs.add(childDataSnapshot.getKey());
                    }
                }

                //remove users you are already friends with
                friendsReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (i=0; i<IDs.size(); i++) {
                            for (DataSnapshot childDataSnapshot : snapshot.getChildren()) {
                                if (Objects.equals(childDataSnapshot.getValue(), IDs.get(i))) {
                                    IDs.remove(i);
                                }

                            }
                        }
                        //shuffles the list of users
                        Collections.shuffle(IDs);

                        idList = IDs.listIterator();
                        if (idList.hasNext()){
                            setProfile(idList.next());
                        } else{
                            circle1 = (ImageView) view.findViewById(R.id.circle1);
                            circle1.setVisibility(view.GONE);
                            bio.setVisibility(view.GONE);
                            hobbyText.setVisibility(view.GONE);
                            matchBtn.setVisibility(view.GONE);
                            nextBtn.setVisibility(view.GONE);
                            noMatches.setVisibility(view.VISIBLE);
                            backBtn.setVisibility(view.VISIBLE);
                        }
                    }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) { }
                        });
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) { }
        });



        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    //show the profiles of potential matches
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

                String ageString = Objects.requireNonNull(snapshot.child(id).child("age").getValue())
                        .toString();
                age.setText(ageString + " years old");

                String bioString = snapshot.child(id).child("bio").getValue(String.class);
                bio.setText(bioString);

                String hobby1String = snapshot.child(id).child("hobbys").child("hobby1")
                        .getValue(String.class);
                hobby1.setText(hobby1String);

                String hobby2String = snapshot.child(id).child("hobbys").child("hobby2")
                        .getValue(String.class);
                circle2 = (ImageView) view.findViewById(R.id.circle2);
                assert hobby2String != null;
                if(!hobby2String.isEmpty()){
                    circle2.setVisibility(view.VISIBLE);
                }
                else{
                    circle2.setVisibility(view.INVISIBLE);
                }
                hobby2.setText(hobby2String);

                String hobby3String = snapshot.child(id).child("hobbys").child("hobby3").getValue(String.class);
                circle3 = (ImageView) view.findViewById(R.id.circle3);
                assert hobby3String != null;
                if(!hobby3String.isEmpty()){
                    circle3.setVisibility(view.VISIBLE);
                }
                else{
                    circle3.setVisibility(view.INVISIBLE);
                }
                hobby3.setText(hobby3String);

                String hobby4String = snapshot.child(id).child("hobbys").child("hobby4").getValue(String.class);
                circle4 = (ImageView) view.findViewById(R.id.circle4);
                assert hobby4String != null;
                if(!hobby4String.isEmpty()){
                    circle4.setVisibility(view.VISIBLE);
                }
                else{
                    circle4.setVisibility(view.INVISIBLE);
                }
                hobby4.setText(hobby4String);


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }

    //add a user to the current users friend list and add current user to the other users friend list
    private void matchUser(){
        friendListSize = 0;
        currentMatch = idList.previous();
        DatabaseReference otherReference = databaseReference.child(currentMatch).child("friends");
        friendsReference.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot datasnapshot) {
                for (DataSnapshot childDataSnapshot : datasnapshot.getChildren()) {
                    friendListSize++;
                }
                        friendsReference.child("friend" + (friendListSize+1))
                                .setValue(currentMatch)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                otherReference.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                    @Override
                                    public void onDataChange(
                                            @NonNull @NotNull DataSnapshot snapshot) {
                                        friendListSize=0;
                                        for (DataSnapshot childDataSnapshot :
                                                datasnapshot.getChildren()) {
                                            friendListSize++;
                                        }
                                        otherReference.child("friend" + (friendListSize+1))
                                                .setValue(myID)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(
                                                            @NonNull @NotNull Task<Void> task) {
                                                        //open chat with newly matched friend
                                                        chatButton();
                                                    }
                                                });
                                    }
                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            }
                        });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }

    //move to chat environment with current match
    private void toChat(String currentMatch){
        assert getFragmentManager() != null;
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, new MessageFragment(currentMatch))
                .commit();
    }

    private void chatButton(){
        matchBtn.setVisibility(view.GONE);
        chatBtn.setVisibility(view.VISIBLE);
        nextBtn.setVisibility(view.GONE);
        idList.next();
        if (!idList.hasNext()){
            if (noMatches.getVisibility() != view.VISIBLE){
                noMatches.setVisibility(view.VISIBLE);
            }
        }
        else{
            nextBtn2.setVisibility(view.VISIBLE);
        }
    }

}

