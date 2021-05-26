package com.example.elderlymeeting.ui.friends;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elderlymeeting.R;
import com.example.elderlymeeting.ui.messaging.MessageFragment;
import com.example.elderlymeeting.ui.seek.MatchFragment;
import com.example.elderlymeeting.ui.users.UserAdapter;
import com.example.elderlymeeting.ui.users.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment implements View.OnClickListener{

    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<Users> mUsers;
    public Button messageButton;

    View view;

    private DatabaseReference FriendsReference;
    private FirebaseAuth mAuth;

    public FriendsFragment(){
        // Empty
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_friends, container, false);

        messageButton = (Button) view.findViewById(R.id.messageButton);
        messageButton.setOnClickListener(this);

      //  readUsers();
        return view;
    }

    private void readUsers(){
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUsers.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Users user = snapshot.getValue(Users.class);

                    assert user != null;
                    if (!user.getId().equals(firebaseUser.getUid())){
                        mUsers.add(user);
                    }

                    userAdapter = new UserAdapter(getContext(), mUsers);
                    recyclerView.setAdapter(userAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.messageButton:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new MessageFragment())
                        .commit();
                break;
        }
    }
}
