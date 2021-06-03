package com.example.elderlymeeting.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.elderlymeeting.R;
import com.example.elderlymeeting.ui.messaging.MessageFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FriendsFragment extends Fragment{

    private RelativeLayout recyclerView;

    String receiver, myID;

    String fullName, profilePicture, age;

    ArrayList<FriendsList> customList = new ArrayList<>();
    ArrayList<String> friendsList = new ArrayList<>();

    ListView listView;

    public FriendsFragment(){
        // Empty
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_friends, container, false);

        //get userID of logged in user
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();
        assert firebaseUser != null;
        myID = firebaseUser.getUid();
        listView = (ListView) view.findViewById(R.id.main_listview);

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference friendsReference = firebaseDatabase.getReference("Users")
                .child(myID)
                .child("friends");
        DatabaseReference userReference = firebaseDatabase.getReference("Users");

        friendsReference.addListenerForSingleValueEvent( new ValueEventListener() {
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                        friendsList.add(childDataSnapshot.getValue(String.class));
                }
                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        for (int i = 0; i<friendsList.size(); i++) {
                            fullName = snapshot.child(friendsList.get(i)).child("fullName")
                                    .getValue(String.class);
                            age = Objects.requireNonNull(snapshot.child(friendsList.get(i))
                                    .child("age")
                                    .getValue())
                                    .toString();
                            profilePicture = snapshot.child(friendsList.get(i))
                                    .child("profilePicture")
                                    .getValue(String.class);
                            customList.add(new FriendsList(profilePicture, fullName, age));
                        }
                        FriendArrayAdapter friendArrayAdapter =
                                new FriendArrayAdapter(view.getContext(), 0, customList);
                        listView.setAdapter(friendArrayAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                    });
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                receiver = friendsList.get(position);
                assert getFragmentManager() != null;
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new MessageFragment(receiver))
                        .commit();
            }
        });

        return view;
    }

}
