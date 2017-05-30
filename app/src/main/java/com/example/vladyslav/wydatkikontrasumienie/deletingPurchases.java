package com.example.vladyslav.wydatkikontrasumienie;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
public class deletingPurchases extends AppCompatActivity {
    public static boolean answer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deleting_purchases);
    }
    public void yes(View view) {
        answer = true;
        this.onBackPressed();
    }
    public void no(View view) {
        answer = false;
        this.onBackPressed();
    }
}
