package com.example.vladyslav.wydatkikontrasumienie;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CheckingPassword extends AppCompatActivity {
    public static EditText inputedPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checking_password);
        inputedPassword = (EditText) findViewById(R.id.inputtedPassword);
    }
    public void passwordCheck(View view) {
        this.onBackPressed();
    }
}
