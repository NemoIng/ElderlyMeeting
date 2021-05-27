package com.example.elderlymeeting.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elderlymeeting.R;
import com.example.elderlymeeting.ui.messaging.MessageFragment;
import com.example.elderlymeeting.ui.users.UserAdapter;
import com.example.elderlymeeting.ui.users.Users;
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
import java.util.List;
import java.util.SplittableRandom;

public class FriendsFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> mFriends;
    private List<Users> friends;
    private FriendsAdapter friendsAdapter;
    public Button messageButton;

    String receiver, uID;
    DatabaseReference friendsReference;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    public FriendsFragment(){
        // Empty
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        //get userID of logged in user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        uID = firebaseUser.getUid();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users").child(uID).child("friends");

        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager((new LinearLayoutManager(getContext())));

        mFriends = new ArrayList<>();

        receiver = "test persoon";

        //display all of the users friends
        friendsReference.addListenerForSingleValueEvent( new ValueEventListener() {
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Users user = childDataSnapshot.getValue(Users.class);

                    assert user != null;
                    mFriends.add(user);

                    friendsAdapter = new FriendsAdapter(getContext(), mFriends, false);
                    recyclerView.setAdapter(friendsAdapter);
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) { }
        });

        messageButton = (Button) view.findViewById(R.id.messageButton);
        messageButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.messageButton:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new MessageFragment(receiver))
                        .commit();
                break;
        }
    }
}
