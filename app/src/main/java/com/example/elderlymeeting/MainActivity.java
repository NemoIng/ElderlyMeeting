package com.example.elderlymeeting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.elderlymeeting.ui.login.LoginActivity;


public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView signUpBtn, signInBtn, homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signUpBtn = (TextView) findViewById(R.id.signUp);
        signUpBtn.setOnClickListener(this);

        signInBtn = (TextView) findViewById(R.id.signIn);
        signInBtn.setOnClickListener(this);

        homeBtn = (TextView) findViewById(R.id.home);
        homeBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                startActivity(new Intent(this, HomeActivity.class));
                break;
            case R.id.signIn:
                startActivity(new Intent(this, LoginActivity.class));
                break;
            case R.id.signUp:
                startActivity(new Intent(this, RegisterPage.class));
                break;
        }
    }
}