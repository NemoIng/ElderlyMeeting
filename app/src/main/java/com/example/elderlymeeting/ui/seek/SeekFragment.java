package com.example.elderlymeeting.ui.seek;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.elderlymeeting.ui.friends.FriendsFragment;
import com.example.elderlymeeting.ui.seek.MatchFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elderlymeeting.HomeActivity;
import com.example.elderlymeeting.R;
import com.example.elderlymeeting.ui.registration.RegisterBio;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class SeekFragment extends Fragment implements View.OnClickListener {

    private FragmentActivity myContext;

    private Button matchBtn;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_seek, container, false);

        matchBtn = (Button) view.findViewById(R.id.buttonMatch);
        matchBtn.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        Fragment selectedFragment = null;
        switch (view.getId()) {
            case R.id.buttonMatch:
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new MatchFragment())
                        .commit();
                break;
        }
    }



}