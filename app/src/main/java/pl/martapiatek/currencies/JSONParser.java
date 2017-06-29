package pl.martapiatek.currencies;

import android.os.StrictMode;
import android.util.Log;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class JSONParser {

    public static final String URL_CODES = "http://openexchangerates.org/api/currencies.json";
    static JSONObject sReturnJsonObject = null;
    static String sRawJsonString = "";

    JSONObject getJSONFromUrl(String text) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {

            URL url = new URL(URL_CODES);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Accept", "application/json");
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                bufferedReader.close();

                sRawJsonString = stringBuilder.toString();
            } catch (Exception e) {
                Log.e("Błąd odczytu z Buffer: " + e.toString(),
                        this.getClass().getSimpleName());
            }

            try {
                sReturnJsonObject = new JSONObject(sRawJsonString);
            } catch (JSONException e) {
                Log.e("Parser", "Błąd w trakcie parsowania danych " + e.toString());
            }




        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // zwrócenie obiektu JSON
        return sReturnJsonObject;
    }

}
