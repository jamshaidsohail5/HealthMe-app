package com.hayyaalassalah.faizanahmad.healthyme;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by Faizan Ahmad on 12/3/2016.
 */
public class BmiActivity extends Activity {

    private SeekBar height;
    private SeekBar weight;
    private TextView heightText;
    private TextView weightText;
    int step = 1;

    protected void onCreate(Bundle savedInstanceState) {
        //creating main page
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bmi);

        height = (SeekBar) findViewById(R.id.height);
        weight = (SeekBar) findViewById(R.id.weight);
        heightText = (TextView) findViewById(R.id.progressHeight);
        weightText = (TextView) findViewById(R.id.progressWeight);


        //weight.setMax((maxWeight - minWeight) / step );

        height.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //double value = 130 + (progress * step);
                System.out.println(progress);
                heightText.setText(Integer.toString(progress) + " cm");
                height.setProgress(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        //weight.setMax((maxWeight - minWeight) / step );

        weight.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                //double value = 130 + (progress * step);
                System.out.println("Weight " + progress);
                weightText.setText(Integer.toString(progress) + " kg");
                weight.setProgress(progress);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


    }

    public void calculateBMI(View v)
    {
        TextView resultText = (TextView) findViewById(R.id.bmi);
        double weightValue = weight.getProgress();
        double heightValue = height.getProgress();

        if(heightValue > 0) {
            double heightDouble = heightValue / 100f;
            System.out.println("Height is " + heightDouble);

            double heightValueRequired = heightDouble * heightDouble;
            double weightValueRequired = weightValue;

            double bmi = weightValueRequired / heightValueRequired;

            DecimalFormat twoDForm = new DecimalFormat("#.##");
            bmi = Double.valueOf(twoDForm.format(bmi));

            System.out.println("bmi IS " + bmi);

            String result = "";

            if (bmi < 18.5) {
                result = "You are Underweight and your bmi is " + Double.toString(bmi);
            } else if (bmi >= 18.5 && bmi < 24.9) {
                result = "You are Normal and your bmi is " + Double.toString(bmi);
            } else if (bmi >= 24.9 && bmi < 29.9) {
                result = "You are Overweight and your bmi is " + Double.toString(bmi);
            } else if (bmi >= 29.9) {
                result = "You are Obese and your bmi is " + Double.toString(bmi);
            }

            resultText.setText(result);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
