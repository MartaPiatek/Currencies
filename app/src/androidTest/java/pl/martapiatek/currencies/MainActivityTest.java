package pl.martapiatek.currencies;


import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Marta on 18.07.2017.
 */

public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mActivity;
    private Button mCalcButton;
    private TextView mConvertedTextView;
    private EditText mAmountEditText;
    private Spinner mForSpinner, mHomSpinner;





    public MainActivityTest() {
        super(MainActivity.class);

    }


    @Override
    public void setUp() throws Exception {
        super.setUp();

        //pobierz testowana aktywnosc
        mActivity = getActivity();

        //przypisz rewferencje do widokow

        mCalcButton = (Button) mActivity.findViewById(R.id.btn_calc);
        mConvertedTextView = (TextView) mActivity.findViewById(R.id.txt_converted);
        mAmountEditText = (EditText) mActivity.findViewById(R.id.edt_amount);
        mForSpinner = (Spinner) mActivity.findViewById(R.id.spn_for);
        mHomSpinner = (Spinner) mActivity.findViewById(R.id.spn_hom);



    }


    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }


}
