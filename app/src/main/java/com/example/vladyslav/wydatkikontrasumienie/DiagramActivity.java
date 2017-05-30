package com.example.vladyslav.wydatkikontrasumienie;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.Vector;
public class DiagramActivity extends AppCompatActivity {
    double noGuiltSum, lowGuiltSum, mediumGuiltSum, highGuiltSum, asHellGuiltSum, totalCoast;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diagram);
        Resources r = getResources();
        LinearLayout guiltDiagram = (LinearLayout) findViewById(R.id.guiltDiagram);
        float[] dp;
        dp = calculateDp(calculatePer());
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp[0], r.getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Math.round(px), LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout first = new LinearLayout(this);
        first.setBackgroundResource(R.color.FIRST);
        guiltDiagram.addView(first, params);
        LinearLayout second = new LinearLayout(this);
        second.setBackgroundResource(R.color.SECOND);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp[1], r.getDisplayMetrics());
        params = new LinearLayout.LayoutParams(Math.round(px), LinearLayout.LayoutParams.MATCH_PARENT);
        guiltDiagram.addView(second, params);
        LinearLayout third = new LinearLayout(this);
        third.setBackgroundResource(R.color.THIRD);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp[2], r.getDisplayMetrics());
        params = new LinearLayout.LayoutParams(Math.round(px), LinearLayout.LayoutParams.MATCH_PARENT);
        guiltDiagram.addView(third, params);
        LinearLayout fourth = new LinearLayout(this);
        fourth.setBackgroundResource(R.color.FOURTH);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp[3], r.getDisplayMetrics());
        params = new LinearLayout.LayoutParams(Math.round(px), LinearLayout.LayoutParams.MATCH_PARENT);
        guiltDiagram.addView(fourth, params);
        LinearLayout fifth = new LinearLayout(this);
        fifth.setBackgroundResource(R.color.FIFTH);
        px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp[4], r.getDisplayMetrics());
        params = new LinearLayout.LayoutParams(Math.round(px), LinearLayout.LayoutParams.MATCH_PARENT);
        guiltDiagram.addView(fifth, params);
        LinearLayout purchasesList = (LinearLayout) findViewById(R.id.purchasesList);
        TextView totalSpent = (TextView) findViewById(R.id.totalSpent);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        TextView noGuiltPercent = (TextView) findViewById(R.id.noGuiltPercent);
        TextView lowGuiltPercent = (TextView) findViewById(R.id.lowGuiltPercent);
        TextView mediumGuiltPercent = (TextView) findViewById(R.id.mediumGuiltPercent);
        TextView highGuiltPercent = (TextView) findViewById(R.id.highGuiltPercent);
        TextView asHellGuiltPercent = (TextView) findViewById(R.id.asHellGuiltPercent);
        float[] per;
        per = calculatePer();
        String[] parts = ("" + per[0]).replace(".", ",").split(",");
        if(parts[1].equals("0")) {
            noGuiltPercent.setText(parts[0] + "%");
        }
        else {
            noGuiltPercent.setText(parts[0] + "," + parts[1].substring(0, 2) + "%");
        }

        parts = ("" + per[1]).replace(".", ",").split(",");
        if(parts[1].equals("0")) {

            lowGuiltPercent.setText(parts[0] + "%");
        }
        else {
            lowGuiltPercent.setText(parts[0] + "," + parts[1].substring(0, 2) + "%");
        }
        parts = ("" + per[2]).replace(".", ",").split(",");
        if(parts[1].equals("0")) {
            mediumGuiltPercent.setText(parts[0] + "%");
        }
        else {
            mediumGuiltPercent.setText(parts[0] + "," + parts[1].substring(0, 2) + "%");
        }
        parts = ("" + per[3]).replace(".", ",").split(",");

        if(parts[1].equals("0")) {
            highGuiltPercent.setText(parts[0] + "%");
        }
        else {
            highGuiltPercent.setText(parts[0] + "," + parts[1].substring(0, 2) + "%");
        }

        parts = ("" + per[4]).replace(".", ",").split(",");
        if(parts[1].equals("0")) {
            asHellGuiltPercent.setText(parts[0] + "%");
        }
        else {
            asHellGuiltPercent.setText(parts[0] + "," + parts[1].substring(0, 2) + "%");
        }
        TextView noGuiltCoast = (TextView) findViewById(R.id.noGuiltCoast);
        TextView lowGuiltCoast = (TextView) findViewById(R.id.lowGuiltCoast);
        TextView mediumGuiltCoast = (TextView) findViewById(R.id.mediumGuiltCoast);
        TextView highGuiltCoast = (TextView) findViewById(R.id.highGuiltCoast);
        TextView asHellGuiltCoast = (TextView) findViewById(R.id.asHellGuiltCoast);
        if((sharedPrefs.getString("pref_periodSet", "2")).equals("1")) {
            Vector<Purchase> purchases;
            purchases = MainActivity.purchaseDataBase.getPurchasesFrom("week");
            totalCoast = 0;
            noGuiltSum = 0;
            lowGuiltSum = 0;
            mediumGuiltSum = 0;
            highGuiltSum = 0;
            asHellGuiltSum = 0;
            for(Purchase purchase : purchases) {
                calculateSum(purchase);
                totalCoast += purchase.getPrice();
            }
        }
        if((sharedPrefs.getString("pref_periodSet", "2")).equals("2")) {
            Vector<Purchase> purchases;
            purchases = MainActivity.purchaseDataBase.getPurchasesFrom("month");
            totalCoast = 0;
            noGuiltSum = 0;
            lowGuiltSum = 0;
            mediumGuiltSum = 0;
            highGuiltSum = 0;
            asHellGuiltSum = 0;
            for(Purchase purchase : purchases) {
                calculateSum(purchase);
                totalCoast += purchase.getPrice();
            }
        }
        if((sharedPrefs.getString("pref_periodSet", "2")).equals("3")) {
            Vector<Purchase> purchases;
            purchases = MainActivity.purchaseDataBase.getPurchasesFrom("year");
            totalCoast = 0;
            noGuiltSum = 0;
            lowGuiltSum = 0;
            mediumGuiltSum = 0;
            highGuiltSum = 0;
            asHellGuiltSum = 0;
            for(Purchase purchase : purchases) {
                calculateSum(purchase);
                totalCoast += purchase.getPrice();
            }
        }
        parts = ("" + totalCoast).replace(".", ",").split(",");
        if(parts[1].equals("0")) {
            totalSpent.setText(parts[0] + " PLN");
        }
        else {
            totalSpent.setText(parts[0] + "," + parts[1].substring(0, 2) + " PLN");
        }
        parts = ("" + noGuiltSum).replace(".", ",").split(",");
        if(parts[1].equals("0")) {
            noGuiltCoast.setText(parts[0] + " PLN");
        }
        else {
            noGuiltCoast.setText(parts[0] + "," + parts[1].substring(0, 2) + " PLN");
        }
        parts = ("" + lowGuiltSum).replace(".", ",").split(",");
        if(parts[1].equals("0")) {
            lowGuiltCoast.setText(parts[0] + " PLN");
        }
        else {
            lowGuiltCoast.setText(parts[0] + "," + parts[1].substring(0, 2) + " PLN");
        }
        parts = ("" + mediumGuiltSum).replace(".", ",").split(",");
        if(parts[1].equals("0")) {
            mediumGuiltCoast.setText(parts[0] + " PLN");
        }
        else {
            mediumGuiltCoast.setText(parts[0] + "," + parts[1].substring(0, 2) + " PLN");
        }
        parts = ("" + highGuiltSum).replace(".", ",").split(",");
        if(parts[1].equals("0")) {
            highGuiltCoast.setText(parts[0] + " PLN");
        }
        else {
            highGuiltCoast.setText(parts[0] + "," + parts[1].substring(0, 2) + " PLN");
        }
        parts = ("" + asHellGuiltSum).replace(".", ",").split(",");
        if(parts[1].equals("0")) {
            asHellGuiltCoast.setText(parts[0] + " PLN");
        }
        else {
            asHellGuiltCoast.setText(parts[0] + "," + parts[1].substring(0, 2) + " PLN");
        }
    }
    public void calculateSum(Purchase purchase) {
        if(purchase.getColor() == 1) {
            noGuiltSum += purchase.getPrice();
        }
        if(purchase.getColor() == 2) {
            lowGuiltSum += purchase.getPrice();
        }
        if(purchase.getColor() == 3) {
            mediumGuiltSum += purchase.getPrice();
        }
        if(purchase.getColor() == 4) {
            highGuiltSum += purchase.getPrice();
        }
        if(purchase.getColor() == 5) {
            asHellGuiltSum += purchase.getPrice();
        }
    }
    public float[] calculatePer() {
        Vector<Purchase> purchases;
        float[] percents = new float[5];
        float first = 0, second = 0, third = 0, fourth = 0, fifth = 0, count = 0, firstPer, secondPer, thirdPer, fourthPer, fifthPer;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        TextView period = (TextView) findViewById(R.id.period);
        if ((sharedPrefs.getString("pref_periodSet", "2")).equals("1")) {
            period.setText("Zakupy za tydzień");
            count = 0;
            purchases = MainActivity.purchaseDataBase.getPurchasesFrom("week");
            for (Purchase purchase : purchases) {
                if (purchase.getColor() == 1) {
                    first++;
                }
                if (purchase.getColor() == 2) {
                    second++;
                }
                if (purchase.getColor() == 3) {
                    third++;
                }
                if (purchase.getColor() == 4) {
                    fourth++;
                }
                if (purchase.getColor() == 5) {
                    fifth++;
                }
                count++;
            }
            firstPer = first / count * 100;
            secondPer = second / count * 100;
            thirdPer = third / count * 100;
            fourthPer = fourth / count * 100;
            fifthPer = fifth / count * 100;
            percents[0] = firstPer;
            percents[1] = secondPer;
            percents[2] = thirdPer;
            percents[3] = fourthPer;
            percents[4] = fifthPer;
            return percents;
        }
        if((sharedPrefs.getString("pref_periodSet", "2")).equals("2")) {
            period.setText("Zakupy za ten miesiąc");
            count = 0;
            purchases = MainActivity.purchaseDataBase.getPurchasesFrom("month");
            for (Purchase purchase : purchases) {
                if (purchase.getColor() == 1) {
                    first++;
                }
                if (purchase.getColor() == 2) {
                    second++;
                }
                if (purchase.getColor() == 3) {
                    third++;
                }
                if (purchase.getColor() == 4) {
                    fourth++;
                }
                if (purchase.getColor() == 5) {
                    fifth++;
                }
                count++;
            }
            firstPer = first / count * 100;
            secondPer = second / count * 100;
            thirdPer = third / count * 100;
            fourthPer = fourth / count * 100;
            fifthPer = fifth / count * 100;
            percents[0] = firstPer;
            percents[1] = secondPer;
            percents[2] = thirdPer;
            percents[3] = fourthPer;
            percents[4] = fifthPer;
            return percents;
        }
        if((sharedPrefs.getString("pref_periodSet", "3")).equals("3")) {
            period.setText("Zakupy za ten rok");
            count = 0;
            purchases = MainActivity.purchaseDataBase.getPurchasesFrom("year");
            for (Purchase purchase : purchases) {
                if (purchase.getColor() == 1) {
                    first++;
                }
                if (purchase.getColor() == 2) {
                    second++;
                }
                if (purchase.getColor() == 3) {
                    third++;
                }
                if (purchase.getColor() == 4) {
                    fourth++;
                }
                if (purchase.getColor() == 5) {
                    fifth++;
                }
                count++;
            }
            firstPer = first / count * 100;
            secondPer = second / count * 100;
            thirdPer = third / count * 100;
            fourthPer = fourth / count * 100;
            fifthPer = fifth / count * 100;
            percents[0] = firstPer;
            percents[1] = secondPer;
            percents[2] = thirdPer;
            percents[3] = fourthPer;
            percents[4] = fifthPer;
            return percents;
        }
        return null;
    }
    public float[] calculateDp(float[] percents) {
        float firstDp, secondDp, thirdDp, fourthDp, fifthDp;
            firstDp = percents[0]*3.3f;
            secondDp = percents[1]*3.3f;
            thirdDp = percents[2]*3.3f;
            fourthDp = percents[3]*3.3f;
            fifthDp = percents[4]*3.3f;
            percents[0] = firstDp;
            percents[1] = secondDp;
            percents[2] = thirdDp;
            percents[3] = fourthDp;
            percents[4] = fifthDp;
            return percents;
    }
}
