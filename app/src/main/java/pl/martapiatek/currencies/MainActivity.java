package pl.martapiatek.currencies;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Properties;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //definicja składowych odpowiadających widokom w układzie graficznym
    private Button mCalcButton;
    private TextView mConvertedTextView;
    private EditText mAmountEditText;
    private Spinner mForSpinner, mHomSpinner;
    private String[] mCurrencies;

    private static final String FOR = "FOR_CURRENCY";
    private static final String HOM = "HOM_CURRENCY";

    //klucz API
    private String mKey;

    //używane do pobierania obiektu 'rates' w formacie JSON ze strony openexchangerates.org
    public static final String RATES = "rates";
    public static final String URL_BASE = "http://openexchangerates.org/api/latest.json?api_id=";

    //używane do właściwego sformatowania pobranych danych
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("#,##0.00000");





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //rozpakowanie ArrayList z paczki i konwersja na tablicę
        ArrayList<String> arrayList = ((ArrayList<String>) getIntent().getSerializableExtra(SplashActivity.KEY_ARRAYLIST));
        Collections.sort(arrayList);
        mCurrencies = arrayList.toArray(new String[arrayList.size()]);

        //przypisanie referencji do widoków
        mConvertedTextView = (TextView) findViewById(R.id.txt_converted);
        mAmountEditText = (EditText) findViewById(R.id.edt_amount);
        mCalcButton = (Button) findViewById(R.id.btn_calc);
        mForSpinner = (Spinner) findViewById(R.id.spn_for);
        mHomSpinner = (Spinner) findViewById(R.id.spn_hom);

        //kontroler
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                //kontekst
                this,
                //widok
                R.layout.spinner_closed,
                //model
                mCurrencies
        );

        //widok
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //przypisanie adapterów

        mHomSpinner.setAdapter(arrayAdapter);
        mForSpinner.setAdapter(arrayAdapter);

        mHomSpinner.setOnItemSelectedListener(this);
        mForSpinner.setOnItemSelectedListener(this);

        // ustawienie ustawień współdzielonych lub ich pobranie

        if(savedInstanceState == null && (PrefsMgr.getString(this, FOR) == null && PrefsMgr.getString(this, HOM) == null) ){
            mForSpinner.setSelection(findPositionGivenCode("EUR", mCurrencies));
            mHomSpinner.setSelection(findPositionGivenCode("PLN", mCurrencies));

            PrefsMgr.setString(this, FOR, "EUR");
            PrefsMgr.setString(this, HOM, "PLN");
        }else {
            mForSpinner.setSelection(findPositionGivenCode(PrefsMgr.getString(this, FOR), mCurrencies));
            mHomSpinner.setSelection(findPositionGivenCode(PrefsMgr.getString(this, HOM), mCurrencies));
        }


        mCalcButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //TODO zdefiniować zachowanie
            }
        });

        mKey = getKey("open_key");
    }

    public boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if(networkInfo != null && networkInfo.isConnectedOrConnecting()){
            return true;
        }
        return false;
    }

    private void launchBrowser(String strUri){
        if(isOnline()){
            Uri uri = Uri.parse(strUri);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        }
    }

    private void invertCurrencies(){

        int nFor = mForSpinner.getSelectedItemPosition();
        int nHom = mHomSpinner.getSelectedItemPosition();

        mForSpinner.setSelection(nHom);
        mHomSpinner.setSelection(nFor);

        mConvertedTextView.setText("");

        PrefsMgr.setString(this, FOR, extractCodeFromCurrency((String) mForSpinner.getSelectedItem()));
        PrefsMgr.setString(this, HOM, extractCodeFromCurrency((String) mHomSpinner.getSelectedItem()));
    }

    private int findPositionGivenCode(String code, String[] currencies){

        for(int i=0; i < currencies.length; i++){
            if(extractCodeFromCurrency(currencies[i]).equalsIgnoreCase(code)){
                return i;
            }
        }
        return 0;
    }

    private String extractCodeFromCurrency(String currency) {
        return (currency).substring(0,3);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private String getKey(String keyName){

        AssetManager assetManager = this.getResources().getAssets();
        Properties properties = new Properties();

        try{
            InputStream inputStream = assetManager.open("keys.properties");
            properties.load(inputStream);
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return properties.getProperty(keyName);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        switch(id) {
            case R.id.mnu_invert:
                invertCurrencies();
                break;
            case R.id.mnu_codes:
                launchBrowser(SplashActivity.URL_CODES);
                break;
            case R.id.mnu_exit:
                finish();
                break;
        }
            return true;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        switch (parent.getId()){

            case R.id.spn_for:
                PrefsMgr.setString(this, FOR, extractCodeFromCurrency((String) mForSpinner.getSelectedItem()));
                break;
            case R.id.spn_hom:
               PrefsMgr.setString(this, HOM, extractCodeFromCurrency((String) mHomSpinner.getSelectedItem()));
                break;
            default:
                break;

        }

        mConvertedTextView.setText("");

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
