
package com.example.vladyslav.wydatkikontrasumienie;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Vector;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.w3c.dom.Node;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
public class MainActivity extends AppCompatActivity {
    public static PurchaseDataBase purchaseDataBase;
    private final String KEY_POZYCJA = "pozycja";
    private final String KEY_NAZWA_WALUTY = "nazwa_waluty";
    private final String KEY_PRZELICZNIK = "przelicznik";
    private final String KEY_KOD_WALUTY = "kod_waluty";
    private final  String KEY_KURS_SREDNI = "kurs_sredni";
    public static Vector<selectedRate> allRates = new Vector<selectedRate>();
    public static selectedRate SelectedRate;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPassword();
        String url = "http://www.nbp.pl/kursy/xml/a101z170526.xml";
        new Rates().execute(url);
        selectedRate Rate = new selectedRate(1, 1, "PLN", "polski złoty");
        allRates.add(Rate);
        SelectedRate = Rate;
        purchaseDataBase = new PurchaseDataBase(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("creating", true);
        editor.commit();
        refresh();
    }
    private void refresh() {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        if((sharedPrefs.getString("pref_periodSet", "2")).equals("1")) {
            Vector<Purchase> purchases = new Vector<Purchase>();
            purchases = purchaseDataBase.getPurchasesFrom("week");
            for(Purchase purchase : purchases) {
                creatingNewPurchase(purchase.getDate());
            }
        }
        if((sharedPrefs.getString("pref_periodSet", "2")).equals("2")) {
            Vector<Purchase> purchases = new Vector<Purchase>();
            purchases = purchaseDataBase.getPurchasesFrom("month");
            for(Purchase purchase : purchases) {
                creatingNewPurchase(purchase.getDate());
            }
        }
        if((sharedPrefs.getString("pref_periodSet", "2")).equals("3")) {
            Vector<Purchase> purchases = new Vector<Purchase>();
            purchases = purchaseDataBase.getPurchasesFrom("year");
            for(Purchase purchase : purchases) {
                creatingNewPurchase(purchase.getDate());
            }
        }
    }
    private void checkPassword() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(!(prefs.getString("pref_password", "").equals(""))) {
            Intent i = new Intent(this, CheckingPassword.class);
            startActivityForResult(i, 2);
        }
    }
    private void checkingPassword() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String inputedPassword = CheckingPassword.inputedPassword.getText().toString();
        if(!(inputedPassword.equals(prefs.getString("pref_password", "")))) {
            Toast.makeText(getApplicationContext(), "Brak dostępu", Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, CheckingPassword.class);
            startActivityForResult(i, 2);
        }
        else {
            Toast.makeText(getApplicationContext(), "Dostęp dozwolony", Toast.LENGTH_LONG).show();
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setings:
                Intent i = new Intent(this, SettingsActivity.class);
                startActivityForResult(i, 5);
                break;
        }
        return true;
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                creatingNewPurchase(purchaseDataBase.added);
                break;
            case 2:
                checkingPassword();
                break;
            case 3:
                removingPurchases();
                deleting();
                refresh();
                break;
            case 5:
                removingPurchases();
                Toast.makeText(getApplicationContext(), "Wszystkie zakupy zostały usunięte", Toast.LENGTH_LONG).show();
                break;
        }
    }
    public void deleting() {
        if(deletePurchase.answer == true) {
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
            Toast.makeText(getApplicationContext(), "Zakup " + purchaseDataBase.getPurchase(prefs.getString("deleteDate", "")).getName() + " został usunięty", Toast.LENGTH_LONG).show();
            purchaseDataBase.deletePurchase(prefs.getString("deleteDate", ""));
        }
    }
    public void newPurchase(View view) {
        Intent i = new Intent(this, CreatingPurchase.class);
        startActivityForResult(i, 1);
    }
    public void deletePurchase(String name, String date) {
        Intent i = new Intent(this, deletePurchase.class);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("deleteName", name);
        editor.putString("deleteDate", date);
        editor.commit();
        startActivityForResult(i, 3);
    }
    public void removingPurchases() {
        LinearLayout purchasesLayout = (LinearLayout) findViewById(R.id.purchasesLayout);
        if(purchasesLayout.getChildCount() > 0) {
            purchasesLayout.removeAllViews();
        }
    }
    public void creatingNewPurchase(String date) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        if(prefs.getBoolean("creating", false)) {
            String sign;
            Resources r = getResources();
            sign = " PLN";
            LinearLayout purchasesLayout = (LinearLayout) findViewById(R.id.purchasesLayout);
            LinearLayout purchaseLayout = new LinearLayout(this);
            purchaseLayout.setOrientation(LinearLayout.HORIZONTAL);
            purchaseLayout.setTag(date);
            purchaseLayout.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    String[] tag = view.getTag().toString().split("/");
                    Toast.makeText(getApplicationContext(), "Zakup był zrobiony: " + tag[3] + "." + tag[4] + "." + tag[5] + "\n" + "o " + tag[2] + ":" + tag[1], Toast.LENGTH_LONG).show();
                }
            });
            purchaseLayout.setOnLongClickListener(new View.OnLongClickListener() {
                public boolean onLongClick(View view) {
                    deletePurchase(purchaseDataBase.getPurchase(view.getTag().toString()).getName(), view.getTag().toString());
                    return true;
                }
            });
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
            params.setMargins(0, 0, 0, Math.round(px));
            purchasesLayout.addView(purchaseLayout, params);
            TextView name = new TextView(this);
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            name.setBackgroundResource(R.color.purchaseName);
            name.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            float px1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
            float px2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, r.getDisplayMetrics());
            name.setPadding(Math.round(px2), Math.round(px1), Math.round(px2), Math.round(px1));
            name.setText("" + purchaseDataBase.getPurchase(date).getName());
            purchaseLayout.addView(name, params);
            LinearLayout priceLayout = new LinearLayout(this);
            priceLayout.setOrientation(LinearLayout.HORIZONTAL);
            px1 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, r.getDisplayMetrics());
            px2 = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, r.getDisplayMetrics());
            priceLayout.setPadding(Math.round(px2), Math.round(px1), Math.round(px1), Math.round(px1));
            params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            if (purchaseDataBase.getPurchase(date).getColor() == 1) {
                priceLayout.setBackgroundResource(R.color.FIRST);
            }
            if (purchaseDataBase.getPurchase(date).getColor() == 2) {
                priceLayout.setBackgroundResource(R.color.SECOND);
            }
            if (purchaseDataBase.getPurchase(date).getColor() == 3) {
                priceLayout.setBackgroundResource(R.color.THIRD);
            }
            if (purchaseDataBase.getPurchase(date).getColor() == 4) {
                priceLayout.setBackgroundResource(R.color.FOURTH);
            }
            if (purchaseDataBase.getPurchase(date).getColor() == 5) {
                priceLayout.setBackgroundResource(R.color.FIFTH);
            }
            purchaseLayout.addView(priceLayout, params);
            TextView price = new TextView(this);
            price.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            String[] splitedPrice = ("" + purchaseDataBase.getPurchase(date).getPrice()).replace(".", ",").split(",");
            if (splitedPrice[1].length() > 2) {
                price.setText(splitedPrice[0] + "." + (splitedPrice[1]).substring(0, 2) + sign);
            } else {
                price.setText("" + purchaseDataBase.getPurchase(date).getPrice() + sign);
            }
            price.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            priceLayout.addView(price);
        }
    }
    public void diagram(View view) {
        if(purchaseDataBase.getPurchasesCount() == 0) {
            Toast.makeText(getApplicationContext(), "Nie zrobiono żadnego zakupu", Toast.LENGTH_LONG).show();
        }
        else {
            Intent i = new Intent(this, DiagramActivity.class);
            startActivity(i);
        }
    }
    class Rates extends AsyncTask<String, Void, String> {
        protected void onPreExecute() {
        }
        protected String doInBackground(String... urls) {
            String response = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                InputStream is = conn.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                String s = "";
                while ((s = br.readLine()) != null) {
                    response += s;
                }
            }
            catch (Exception error) {
                error.printStackTrace();
            }
            return response;
        }
        protected void onPostExecute(String result) {
            XMLParser parser = new XMLParser();
            Document doc = parser.getDomElement(result);
            ArrayList<HashMap<String, String>> menuItems = new ArrayList<HashMap<String, String>>();
            NodeList nl = doc.getElementsByTagName(KEY_POZYCJA);
            for(int i = 0; i < nl.getLength(); i++) {
                HashMap<String, String> map = new HashMap<String, String>();
                Element e = (Element) nl.item(i);
                map.put("/", "/");
                map.put(KEY_NAZWA_WALUTY, parser.getValue(e, KEY_NAZWA_WALUTY));
                map.put(KEY_PRZELICZNIK, parser.getValue(e, KEY_PRZELICZNIK));
                map.put(KEY_KOD_WALUTY, parser.getValue(e, KEY_KOD_WALUTY));
                map.put(KEY_KURS_SREDNI, parser.getValue(e, KEY_KURS_SREDNI));
                menuItems.add(map);
            }
            String rates = menuItems.toString();
            rates = rates.replace("[", "");
            rates = rates.replace("]", "");
            rates = rates.replace("{", "");
            rates = rates.replace("}", "");
            String[] rate = rates.split("/=/");
            for(int i = 1; i < rate.length; i++)
            {
                rate[i] = rate[i].substring(2);
            }
            for(int i = 0; i < rate.length; i++) {
                String ModRate[] = rate[i].split(",");

                selectedRate Rate = new selectedRate(Double.parseDouble((ModRate[3] + "." + ModRate[4]).substring(13)), Integer.parseInt(ModRate[1].substring(13)), ModRate[0].substring(11), ModRate[2].substring(14));
                allRates.add(Rate);
            }
        }
    }
    public double converter(double price) {
        return price * SelectedRate.RateExchange / SelectedRate.RateConverter;
    }
}
class selectedRate {
    public double RateExchange;
    public int RateConverter;
    public String RateCode;
    public String RateName;
    public selectedRate(double selectedRateExchange, int selectedRateConverter, String selectedRateCode, String selectedRateName) {
        this.RateExchange = selectedRateExchange;
        this.RateConverter = selectedRateConverter;
        this.RateCode = selectedRateCode;
        this.RateName = selectedRateName;
    }
}
class Purchase {
    private String name;
    private int color;
    private double price;
    private String date;
    public Purchase(String name, int color, double price, String date) {
        this.name = name;
        this.color = color;
        this.price = price;
        this.date = date;
    }
    public String getName() {
        return name;
    }
    public int getColor() {
        return color;
    }
    public double getPrice() {
        return price;
    }
    public String getDate() { return date; }
}
class PurchaseDataBase extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String NAME = "purchasesBase";
    private static final String PURCHASES_TABLE = "purchases";
    private static final String ID_FIELD_PT = "id";
    private static final String NAME_FIELD_PT = "name";
    private static final String COLOR_FIELD_PT = "color";
    private static final String PRICE_FIELD_PT = "price";
    private static final String DATE_FIELD_PT = "date";
    private SQLiteDatabase purchasesBase;
    String added;
    public PurchaseDataBase(Context context) {
        super(context, NAME, null, VERSION);
        purchasesBase = this.getWritableDatabase();
    }
    public void onCreate(SQLiteDatabase purchasesBase) {
        purchasesBase.execSQL("CREATE TABLE " + PURCHASES_TABLE + "(" + ID_FIELD_PT + " INTEGER PRIMARY KEY," + NAME_FIELD_PT + " TEXT," + COLOR_FIELD_PT + " INTEGER," + PRICE_FIELD_PT + " FLOAT," + DATE_FIELD_PT + " TEXT)");
    }
    public void onUpgrade(SQLiteDatabase purchasesBase, int oldVersion, int newVersion) {
        purchasesBase.execSQL("DROP TABLE IF EXISTS " + PURCHASES_TABLE);
        onCreate(purchasesBase);
    }
    public void addPurchase(Purchase purchase) {
        ContentValues values = new ContentValues();
        values.put(NAME_FIELD_PT, purchase.getName());
        values.put(COLOR_FIELD_PT, purchase.getColor());
        values.put(PRICE_FIELD_PT, purchase.getPrice());
        values.put(DATE_FIELD_PT, purchase.getDate());
        purchasesBase.insert(PURCHASES_TABLE, null, values);
        added = purchase.getDate();
    }
    public Purchase getPurchase(String date) {
        Cursor cursor = purchasesBase.query(PURCHASES_TABLE, new String[] {ID_FIELD_PT, NAME_FIELD_PT, COLOR_FIELD_PT, PRICE_FIELD_PT, DATE_FIELD_PT}, DATE_FIELD_PT + "=?", new String[] {date}, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }
        Purchase purchase = new Purchase(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
        return purchase;
    }
    public Vector<Purchase> getPurchasesFrom(String period) {
        Vector<Purchase> purchases = new Vector<Purchase>();
        String selectQuery;
        Calendar calendar = Calendar.getInstance();
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        int currentMonth = (calendar.get(Calendar.MONTH))+1;
        System.out.println("---!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!---1");
        System.out.println(currentMonth);
        System.out.println("---!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!---1");
        int currentYear = calendar.get(Calendar.YEAR);
        if(period.equals("week")) {
            if((currentDay - 6) < 1) {
                if((currentDay-6) == 0) {
                    selectQuery = "SELECT * FROM " + PURCHASES_TABLE + " WHERE " + DATE_FIELD_PT + " LIKE '%" + currentDay + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-1) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-2) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-3) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-4) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-5) + "/" + currentMonth + "/" + currentYear + "'";
                    Cursor cursor = purchasesBase.rawQuery(selectQuery, null);
                    if(cursor.moveToFirst()) {
                        do {
                            Purchase purchase = new Purchase(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
                            purchases.add(purchase);
                        }
                        while (cursor.moveToNext());
                    }
                    return purchases;
                }
                if((currentDay-6) == (-1)) {
                    selectQuery = "SELECT * FROM " + PURCHASES_TABLE + " WHERE " + DATE_FIELD_PT + " LIKE '%" + currentDay + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-1) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-2) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-3) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-4) + "/" + currentMonth + "/" + currentYear + "'";
                    Cursor cursor = purchasesBase.rawQuery(selectQuery, null);
                    if(cursor.moveToFirst()) {
                        do {
                            Purchase purchase = new Purchase(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
                            purchases.add(purchase);
                        }
                        while (cursor.moveToNext());
                    }
                    return purchases;
                }
                if((currentDay-6) == (-2)) {
                    selectQuery = "SELECT * FROM " + PURCHASES_TABLE + " WHERE " + DATE_FIELD_PT + " LIKE '%" + currentDay + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-1) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-2) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-3) + "/" + currentMonth + "/" + currentYear + "'";
                    Cursor cursor = purchasesBase.rawQuery(selectQuery, null);
                    if(cursor.moveToFirst()) {
                        do {
                            Purchase purchase = new Purchase(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
                            purchases.add(purchase);
                        }
                        while (cursor.moveToNext());
                    }
                    return purchases;
                }
                if((currentDay-6) == (-3)) {
                    selectQuery = "SELECT * FROM " + PURCHASES_TABLE + " WHERE " + DATE_FIELD_PT + " LIKE '%" + currentDay + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-1) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-2) + "/" + currentMonth + "/" + currentYear + "'";
                    Cursor cursor = purchasesBase.rawQuery(selectQuery, null);
                    if(cursor.moveToFirst()) {
                        do {
                            Purchase purchase = new Purchase(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
                            purchases.add(purchase);
                        }
                        while (cursor.moveToNext());
                    }
                    return purchases;
                }
                if((currentDay-6) == (-4)) {
                    selectQuery = "SELECT * FROM " + PURCHASES_TABLE + " WHERE " + DATE_FIELD_PT + " LIKE '%" + currentDay + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-1) + "/" + currentMonth + "/" + currentYear + "'";
                    Cursor cursor = purchasesBase.rawQuery(selectQuery, null);
                    if(cursor.moveToFirst()) {
                        do {
                            Purchase purchase = new Purchase(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
                            purchases.add(purchase);
                        }
                        while (cursor.moveToNext());
                    }
                    return purchases;
                }
                if((currentDay-6) == (-5)) {
                    selectQuery = "SELECT * FROM " + PURCHASES_TABLE + " WHERE " + DATE_FIELD_PT + " LIKE '%" + currentDay + "/" + currentMonth + "/" + currentYear + "'";
                    Cursor cursor = purchasesBase.rawQuery(selectQuery, null);
                    if(cursor.moveToFirst()) {
                        do {
                            Purchase purchase = new Purchase(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
                            purchases.add(purchase);
                        }
                        while (cursor.moveToNext());
                    }
                    return purchases;
                }
            }
            else {
                selectQuery = "SELECT * FROM " + PURCHASES_TABLE + " WHERE " + DATE_FIELD_PT + " LIKE '%" + currentDay + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-1) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-2) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-3) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-4) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-5) + "/" + currentMonth + "/" + currentYear + "' OR " + DATE_FIELD_PT + " LIKE '%" + (currentDay-6) + "/" + currentMonth + "/" + currentYear + "'";
                Cursor cursor = purchasesBase.rawQuery(selectQuery, null);
                if(cursor.moveToFirst()) {
                    do {
                        Purchase purchase = new Purchase(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
                        purchases.add(purchase);
                    }
                    while (cursor.moveToNext());
                }
                return purchases;
            }
        }
        if(period.equals("month")) {

            selectQuery = "SELECT * FROM " + PURCHASES_TABLE + " WHERE " + DATE_FIELD_PT + " LIKE '%" + currentMonth + "/" + currentYear + "'";
            System.out.println(selectQuery);
            Cursor cursor = purchasesBase.rawQuery(selectQuery, null);
            if(cursor.moveToFirst()) {
                do {
                    Purchase purchase = new Purchase(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
                    purchases.add(purchase);
                }
                while (cursor.moveToNext());
            }
            return purchases;
        }
        if(period.equals("year")) {
            selectQuery = "SELECT * FROM " + PURCHASES_TABLE + " WHERE " + DATE_FIELD_PT + " LIKE '%" + currentYear + "'";
            Cursor cursor = purchasesBase.rawQuery(selectQuery, null);
            if(cursor.moveToFirst()) {
                do {
                    Purchase purchase = new Purchase(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
                    purchases.add(purchase);
                }
                while (cursor.moveToNext());
            }
            return purchases;
        }
        return purchases;
    }
    public Vector<Purchase> getAllPurchases() {
        Vector<Purchase> purchases = new Vector<Purchase>();
        String selectQuery = "SELECT * FROM " + PURCHASES_TABLE;
        Cursor cursor = purchasesBase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                Purchase purchase = new Purchase(cursor.getString(1), Integer.parseInt(cursor.getString(2)), Double.parseDouble(cursor.getString(3)), cursor.getString(4));
                purchases.add(purchase);
            }
            while (cursor.moveToNext());
        }
        return purchases;
    }
    public int getPurchasesCount() {
        String query = "SELECT * FROM " + PURCHASES_TABLE;
        Cursor cursor = purchasesBase.rawQuery(query, null);
        return cursor.getCount();
    }
    public void deletePurchase(String date) {
        String query = "DELETE FROM " + PURCHASES_TABLE + " WHERE " + DATE_FIELD_PT + " ='" + date + "'";
        purchasesBase.execSQL(query);
    }
    public void deleteAllPurchases() {
        String query = "DELETE FROM " + PURCHASES_TABLE;
        purchasesBase.execSQL(query);
    }
}
class XMLParser {
    public Document getDomElement(String xml) {
        Document doc = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource();
            is.setCharacterStream(new StringReader(xml));
            doc = db.parse(is);
        }
        catch (ParserConfigurationException error) {
            Log.e("Error: ", error.getMessage());
            return null;
        }
        catch (SAXException error) {
            Log.e("Error: ", error.getMessage());
            return null;
        }
        catch (IOException error) {
            Log.e("Error: ", error.getMessage());
            return null;
        }
        return doc;
    }
    public String getElementValue(Node elem) {
        Node child;
        if (elem != null) {
            if(elem.hasChildNodes()) {
                for(child = elem.getFirstChild(); child != null; child = child.getNextSibling()) {
                    if(child.getNodeType() == Node.TEXT_NODE) {
                        return child.getNodeValue();
                    }
                }
            }
        }
        return "";
    }
    public String getValue(Element item, String str) {
        NodeList n = item.getElementsByTagName(str);
        return this.getElementValue(n.item(0));
    }
}