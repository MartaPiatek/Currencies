package pl.martapiatek.currencies;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class SplahActivity extends Activity {

    // adres URL kodów walu wykorzystywanych w aplikacji

    public static final String URL_CODES = "http://openexchangerates.org/api/currencies.json";

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
                finish();
            }catch (JSONException e){
                Toast.makeText(SplahActivity.this, "Wyjątek w danych JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
                finish();
            }


        }
    }

}
