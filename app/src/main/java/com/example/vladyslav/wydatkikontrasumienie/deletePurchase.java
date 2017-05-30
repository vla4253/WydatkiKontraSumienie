package com.example.vladyslav.wydatkikontrasumienie;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
public class deletePurchase extends AppCompatActivity {
    public static boolean answer;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_purchase);
        TextView question = (TextView) findViewById(R.id.question);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        question.setText("Chcesz usunąć " + prefs.getString("deleteName", "") + " z bazy danych?");
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
