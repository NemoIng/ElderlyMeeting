package com.example.elderlymeeting.ui.myProfile;

import android.content.Intent;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.elderlymeeting.MainActivity;
import com.example.elderlymeeting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MyProfileFragment extends Fragment {

    Button logoutBtn;
    View view;

    ImageView profilePicture, circle2, circle3, circle4;
    TextView fullName, age, email, bio, hobby1, hobby2, hobby3, hobby4;

    DatabaseReference databaseReference;
    private FirebaseAuth mAuth;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        view = inflater.inflate(R.layout.fragment_my_profile, container, false);

        //log the user out
        logoutBtn = (Button) view.findViewById(R.id.logoutBtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        String myId = firebaseUser.getUid();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        //set values for the current users profile
        fullName = (TextView) view.findViewById(R.id.fullName);
        email = (TextView) view.findViewById(R.id.email);
        profilePicture = (ImageView) view.findViewById(R.id.profilePicture);
        bio = (TextView) view.findViewById(R.id.biography);
        age = (TextView) view.findViewById(R.id.age);

        hobby1 = (TextView) view.findViewById(R.id.hobby1);
        hobby2 = (TextView) view.findViewById(R.id.hobby2);
        hobby3 = (TextView) view.findViewById(R.id.hobby3);
        hobby4 = (TextView) view.findViewById(R.id.hobby4);

        //show the current users profile
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                String fullNameString = snapshot.child(myId).child("fullName")
                        .getValue(String.class);
                fullName.setText(fullNameString);

                String ageString = Objects.requireNonNull(snapshot.child(myId).child("age")
                        .getValue())
                        .toString();
                age.setText(ageString + " years old");

                String emailString = snapshot.child(myId).child("email").getValue(String.class);
                email.setText(emailString);

                String link = snapshot.child(myId).child("profilePicture").getValue(String.class);
                Glide.with(view)
                        .load(link)
                        .centerCrop()
                        .override(300, 300)
                        .into(profilePicture);

                String bioString = snapshot.child(myId).child("bio").getValue(String.class);
                bio.setText(bioString);

                String hobby1String = snapshot.child(myId).child("hobbys").child("hobby1")
                        .getValue(String.class);
                hobby1.setText(hobby1String);

                String hobby2String = snapshot.child(myId).child("hobbys").child("hobby2")
                        .getValue(String.class);
                circle2 = (ImageView) view.findViewById(R.id.circle2);
                assert hobby2String != null;
                if(!hobby2String.isEmpty()){
                    circle2.setVisibility(view.VISIBLE);
                }
                hobby2.setText(hobby2String);

                String hobby3String = snapshot.child(myId).child("hobbys").child("hobby3").getValue(String.class);
                circle3 = (ImageView) view.findViewById(R.id.circle3);
                assert hobby3String != null;
                if(!hobby3String.isEmpty()){
                    circle3.setVisibility(view.VISIBLE);
                }
                hobby3.setText(hobby3String);

                String hobby4String = snapshot.child(myId).child("hobbys").child("hobby4").getValue(String.class);
                circle4 = (ImageView) view.findViewById(R.id.circle4);
                assert hobby4String != null;
                if(!hobby4String.isEmpty()){
                    circle4.setVisibility(view.VISIBLE);
                }
                hobby4.setText(hobby4String);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    public void logOut(){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }
}