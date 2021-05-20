package com.example.elderlymeeting.ui.seek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.elderlymeeting.HomeActivity;
import com.example.elderlymeeting.R;
import com.example.elderlymeeting.ui.login.LoginActivity;
import com.example.elderlymeeting.ui.registration.RegisterPage;
import com.google.firebase.database.DatabaseReference;

public class MatchActivity extends AppCompatActivity implements View.OnClickListener {

    private String myUID, matchUID;

    private DatabaseReference mDatabase;

    private Button homeBtn, nextBtn, matchBtn;

    private TextView hobby, bio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match);

        homeBtn = (Button) findViewById(R.id.homeButton);
        homeBtn.setOnClickListener(this);

        nextBtn = (Button) findViewById(R.id.nextButton);
        nextBtn.setOnClickListener(this);

        matchBtn = (Button) findViewById(R.id.matchButton);
        matchBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.homeButton:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.nextButton:
                startActivity(new Intent(this, MatchActivity.class));
                break;
            case R.id.matchButton:
                startActivity(new Intent(this, MatchActivity.class));
                break;
        }
    }
}