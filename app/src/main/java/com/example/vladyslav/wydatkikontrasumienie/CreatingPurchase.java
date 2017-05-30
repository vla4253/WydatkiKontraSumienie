package com.example.vladyslav.wydatkikontrasumienie;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;
public class CreatingPurchase extends AppCompatActivity {
    LinearLayout firstT, secondT, thirdT, fourthT, fifthT;
    EditText purchaseName, purchaseDate, purchasePrice;
    int color;
    Calendar calendar;
    static Button RateButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creating_purchase);
        RateButton = (Button) findViewById(R.id.RateButton);
        RateButton.setText(MainActivity.SelectedRate.RateCode.toString());
        purchaseName = (EditText) findViewById(R.id.purchaseName);
        calendar = Calendar.getInstance();
        purchaseDate = (EditText) findViewById(R.id.purchaseDate);
        purchaseDate.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.YEAR));
        purchasePrice = (EditText) findViewById(R.id.purchasePrice);
        firstT = (LinearLayout) findViewById(R.id.firstT);
        secondT = (LinearLayout) findViewById(R.id.secondT);
        thirdT = (LinearLayout) findViewById(R.id.thirdT);
        fourthT = (LinearLayout) findViewById(R.id.fourthT);
        fifthT = (LinearLayout) findViewById(R.id.fifthT);
    }
    public void onBackPressed() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("creating", false);
        editor.commit();
        finish();
    }
    public void selectRate(View view) {
        Intent i = new Intent(this, SelectingRate.class);
        startActivity(i);
    }
    public void first(View view) {
        LinearLayout[] onFirst = new LinearLayout[] {secondT, thirdT, fourthT, fifthT};
        for(LinearLayout lL : onFirst) {
            lL.setVisibility(View.INVISIBLE);
        }
        firstT.setVisibility(View.VISIBLE);
        color = 1;
    }
    public void second(View view) {
        LinearLayout[] onFirst = new LinearLayout[] {firstT, thirdT, fourthT, fifthT};
        for(LinearLayout lL : onFirst) {
            lL.setVisibility(View.INVISIBLE);
        }
        secondT.setVisibility(View.VISIBLE);
        color = 2;
    }
    public void third(View view) {
        LinearLayout[] onFirst = new LinearLayout[] {secondT, firstT, fourthT, fifthT};
        for(LinearLayout lL : onFirst) {
            lL.setVisibility(View.INVISIBLE);
        }
        thirdT.setVisibility(View.VISIBLE);
        color = 3;
    }
    public void fourth(View view) {
        LinearLayout[] onFirst = new LinearLayout[] {secondT, thirdT, firstT, fifthT};
        for(LinearLayout lL : onFirst) {
            lL.setVisibility(View.INVISIBLE);
        }
        fourthT.setVisibility(View.VISIBLE);
        color = 4;
    }
    public void fifth(View view) {
        LinearLayout[] onFirst = new LinearLayout[] {secondT, thirdT, fourthT, firstT};
        for(LinearLayout lL : onFirst) {
            lL.setVisibility(View.INVISIBLE);
        }
        fifthT.setVisibility(View.VISIBLE);
        color = 5;
    }
    public double converter(double price) {
        return price * MainActivity.SelectedRate.RateExchange / MainActivity.SelectedRate.RateConverter;
    }
    public boolean checking() {
        String name = purchaseName.getText().toString();
        if(name.equals("")) {
            Toast.makeText(getApplicationContext(), "Proszę podać nazwę zakupu", Toast.LENGTH_LONG).show();
            return false;
        }
        if((purchasePrice.getText().toString()).equals("")) {
            Toast.makeText(getApplicationContext(), "Proszę podać cenę zakupu", Toast.LENGTH_LONG).show();
            return false;
        }
        String date = purchaseDate.getText().toString();
        if(date.equals("")) {
            Toast.makeText(getApplicationContext(), "Proszę podać datę zakupu w postaci dd/mm/rrrr", Toast.LENGTH_LONG).show();
            return false;
        }
        if(color == 0) {
            Toast.makeText(getApplicationContext(), "Proszę podać poziom wyrzutów sumienia przy danym zakupie", Toast.LENGTH_LONG).show();
            return false;
        }
        if(Pattern.matches("[0-9]+", name)) {
            Toast.makeText(getApplicationContext(), "Nazwa została podana niepoprawnie", Toast.LENGTH_LONG).show();
            return false;
        }
        if(!(Pattern.matches("[0-9]+", purchasePrice.getText().toString()))) {
            if(!(Pattern.matches("[0-9]+\\.[0-9]+", purchasePrice.getText().toString()))) {
                Toast.makeText(getApplicationContext(), "Cena została podana niepoprawnie", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        if(!(Pattern.matches("[0-9]{1,2}/[0-9]{1,2}/[0-9]{4}", date))) {
            Toast.makeText(getApplicationContext(), "Data została podana niepoprawnie", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
    public void save(View view) {
        if(checking() == true) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putBoolean("creating", true);
            editor.commit();
            String name = purchaseName.getText().toString();
            double price = Double.parseDouble(purchasePrice.getText().toString());
            price = converter(price);
            String date = purchaseDate.getText().toString();
            Purchase newPurchase = new Purchase(name, color, price, calendar.get(Calendar.SECOND) + "/" + calendar.get(Calendar.MINUTE) + "/" + calendar.get(Calendar.HOUR) + "/" + date);
            MainActivity.purchaseDataBase.addPurchase(newPurchase);
            finish();
        }
    }
}
