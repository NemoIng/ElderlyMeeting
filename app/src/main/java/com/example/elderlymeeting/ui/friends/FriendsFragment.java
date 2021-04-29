package com.example.elderlymeeting.ui.friends;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elderlymeeting.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FriendsFragment extends Fragment {

    private RecyclerView myFriendsList;

    private DatabaseReference FriendsReference;
    private FirebaseAuth mAuth;

    String user;
    private View myMainView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myMainView = inflater.inflate(R.layout.fragment_friends, container, false);
        myFriendsList = (RecyclerView) myMainView.findViewById(R.id.friend_list);
        user = mAuth.getCurrentUser().getUid();

        FriendsReference = FirebaseDatabase.getInstance().getReference().child("Friends").child(user);

        myFriendsList.setLayoutManager(new LinearLayoutManager(getContext()));

        return myMainView;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerAdapter<Friends, FriendsViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Friends, FriendsViewHolder>() {

        }
    }

    public static class FriendsViewHolder extends RecyclerView.ViewHolder{
        public FriendsViewHolder(View itemView){
            super(itemView);
        }
    }
}