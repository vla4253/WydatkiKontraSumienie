package com.example.vladyslav.wydatkikontrasumienie;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

public class SelectingRate extends AppCompatActivity {

    CheckBox[] checkBox = new CheckBox[MainActivity.allRates.size()];
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecting_rate);
        LinearLayout checkboxesLayout = (LinearLayout) findViewById(R.id.checkboxes);
        final SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        for (int i = 0; i < MainActivity.allRates.size(); i++) {
            checkBox[i] = new CheckBox(this);
            checkBox[i].setId(i);
            checkBox[i].setText(MainActivity.allRates.get(i).RateName);
            checkBox[i].setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                           if (isChecked) {
                                                               SharedPreferences.Editor edit = sharedPrefs.edit();
                                                               edit.putString("pref_selectedRateCode", buttonView.getText().toString());
                                                               edit.commit();
                                                               uncheck();
                                                           }
                                                       }
                                                   }
            );
            checkboxesLayout.addView(checkBox[i]);
        }

    }

    public void uncheck() {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        for(CheckBox check : checkBox) {
            if(!((check.getText().toString()).equals(sharedPrefs.getString("pref_selectedRateCode", "PLN")))) {
                check.setChecked(false);
            }
            else {
                for(selectedRate rate : MainActivity.allRates) {
                    if(rate.RateName.equals(sharedPrefs.getString("pref_selectedRateCode", "PLN"))) {

                        MainActivity.SelectedRate = rate;

                        CreatingPurchase.RateButton.setText(MainActivity.SelectedRate.RateCode);
                    }
                }
            }
        }

    }


}
