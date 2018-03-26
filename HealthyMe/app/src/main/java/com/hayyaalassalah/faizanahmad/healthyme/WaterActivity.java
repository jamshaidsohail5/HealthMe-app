package com.hayyaalassalah.faizanahmad.healthyme;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Faizan Ahmad on 12/3/2016.
 */
public class WaterActivity extends Activity {

    ProgressBar _progressBar;
    TextView waterGlasses;
    protected void onCreate(Bundle savedInstanceState) {
        //creating main page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_water);
        _progressBar = (ProgressBar)findViewById (R.id.glassOfWater);
        _progressBar.setProgress(0);


        waterGlasses = (TextView) findViewById(R.id.numberofglasses);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        int glasses = preferences.getInt("Glasses", 12);
        if(glasses != 12)
        {
            waterGlasses.setText(Integer.toString(glasses));
            _progressBar.setProgress(glasses-12);
        }





    }

    public void substractGlass(View v)
    {
        String glasses = waterGlasses.getText().toString();
        int integerGlasses = Integer.parseInt(glasses);
        if(integerGlasses > 0) {
            integerGlasses = integerGlasses - 1;
            waterGlasses.setText(Integer.toString(integerGlasses));
            _progressBar.setProgress(12-integerGlasses);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("Glasses", 12 - integerGlasses);
            editor.apply();
        }
        else
        {
            _progressBar.setProgress(0);
            waterGlasses.setText("12");
            //cout krwa do ke we have done our today's challenge
        }



    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
