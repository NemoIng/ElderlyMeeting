    package com.example.elderlymeeting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.elderlymeeting.ui.friends.FriendsFragment;
import com.example.elderlymeeting.ui.myProfile.MyProfileFragment;
import com.example.elderlymeeting.ui.seek.SeekFragment;
import com.example.elderlymeeting.ui.settings.SettingsFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

    public class HomeActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        /*
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        */
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SeekFragment()).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()){
                        case R.id.friends:
                            selectedFragment = new FriendsFragment();
                            break;
                        case R.id.seek:
                            selectedFragment = new SeekFragment();
                            break;
                        case R.id.settings:
                            selectedFragment = new SettingsFragment();
                            break;
                        case R.id.myProfile:
                            selectedFragment = new MyProfileFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().
                            replace(R.id.fragment_container, selectedFragment).commit();

                    return true;
                }
            };
}