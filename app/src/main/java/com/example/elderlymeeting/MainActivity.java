package com.example.elderlymeeting;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.elderlymeeting.ui.registration.RegisterPage;
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

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signIn:
                //go to the log in tab
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.signUp:
                //go to the register tab
                startActivity(new Intent(this, RegisterPage.class));
                finish();
                break;
        }
    }
}