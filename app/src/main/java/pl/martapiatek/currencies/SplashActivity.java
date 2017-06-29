package pl.martapiatek.currencies;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SplashActivity extends Activity {

    // adres URL kodów walu wykorzystywanych w aplikacji

    public static final String URL_CODES = "http://openexchangerates.org/api/currencies.json";
    public static final String KEY_ARRAYLIST = "key_arraylist";

    // obiekt ArrayList z walutami, które zostaną pobrane i przekazane do MainActivity

    private ArrayList<String> mCurrencies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splah);

        new FetchCodesTask().execute(URL_CODES);
    }



    private class FetchCodesTask extends AsyncTask<String, Void, JSONObject>{

        @Override
        protected JSONObject doInBackground(String... params) {
            return new JSONParser().getJSONFromUrl(params[0]);
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {

            try{
                if(jsonObject == null){
                    throw new JSONException("brak danych");

                }
                Iterator iterator = jsonObject.keys();
                String key = "";
                mCurrencies =  new ArrayList<String>();
                while (iterator.hasNext()){
                    key = (String)iterator.next();
                    mCurrencies.add(key + " | " + jsonObject.getString(key));
                }

                Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                mainIntent.putExtra(KEY_ARRAYLIST, mCurrencies);
                startActivity(mainIntent);

                finish();
            }catch (JSONException e){
                Toast.makeText(SplashActivity.this, "Wyjątek w danych JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
                finish();
            }


        }
    }

}
