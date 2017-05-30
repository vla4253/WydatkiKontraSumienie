package com.example.vladyslav.wydatkikontrasumienie;

import android.content.Intent;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings_activity_xml);
        Preference deleteAllPurchases = findPreference("deleteAllPurchases");

        deleteAllPurchases.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener(){
            public boolean onPreferenceClick(Preference preference) {
                check();
                return true;
            }
        });
    }
    private void check() {
        Intent i = new Intent(this, deletingPurchases.class);
        startActivityForResult(i, 4);
    }
    private void deleting() {
        if(deletingPurchases.answer == true) {

            MainActivity.purchaseDataBase.deleteAllPurchases();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 4:
               deleting();
                this.onBackPressed();
                break;
        }
    }
}
