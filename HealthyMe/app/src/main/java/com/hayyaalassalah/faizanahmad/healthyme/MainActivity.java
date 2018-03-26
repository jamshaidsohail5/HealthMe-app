package com.hayyaalassalah.faizanahmad.healthyme;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookDialog;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.AdActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity
{
    private long date;
    private static final String tag="MIT";
    private TextView totalCount;
    private TextView totalRunningCount;
    private int totalStepsToday = 0;
    ProgressBar _progressBar;
    private AdView mAdView;
    private TextView totalSteps;
    private AdView mAdMobAdView;
    InterstitialAd mAdMobInterstitialAd;
    private ShareDialog shareDialog;
    private CallbackManager callbackManager;




    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        //creating main page
        FacebookSdk.sdkInitialize(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        callbackManager=CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        getSupportActionBar().hide();


        loginfb();

        totalSteps = (TextView) findViewById(R.id.totalSteps);
        _progressBar = (ProgressBar)findViewById (R.id.circularProgressBar);
        _progressBar.setProgress(0);
        MyDbHelper dbhelper = new MyDbHelper(getApplicationContext());
        SQLiteDatabase sqlRead = dbhelper.getReadableDatabase();
        String date = getTodayDate();
        String query = "Select count from steps where date=" + date;
        Cursor c = sqlRead.rawQuery(query,null);
        if(c.moveToNext())
        {
            int steps = c.getInt(c.getColumnIndex("count"));
            _progressBar.setProgress(steps);
            totalStepsToday = steps;
            int percentage = (int)((((float)totalStepsToday)/8000f)*100);
            totalSteps.setText(Integer.toString(percentage) + "%");
        }

        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(
                mMessageReceiver, new IntentFilter("MySteps"));

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        String dayOfTheWeek = sdf.format(d);


        makeGraph();


        startService(new Intent(getBaseContext(), StepCounterService.class));


        totalCount = (TextView) findViewById(R.id.totalSteps);
        //totalRunningCount = (TextView) findViewById(R.id.totalrunning);
        SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
        String currentDateandTime = datef.format(new Date(date));
        TextView set_date = (TextView) findViewById(R.id.date);
        Calendar ca = Calendar.getInstance();
        SimpleDateFormat  format = new SimpleDateFormat("dd/MM/yyyy");
        assert set_date != null;
        set_date.setText(dayOfTheWeek.toUpperCase() + " , " + format.format(ca.getTime()));  //setting today's date*/

    }   //this function is completely okay now


    public void makeGraph() {
        /* GRAPHING */
        Calendar calendar = Calendar.getInstance();
        int d1 = calendar.get(Calendar.DAY_OF_MONTH);
        int d2 = d1-1;
        int d3 = d1-2;
        int d4 = d1-3;
        int d5 = d1-4;
        int d6 = d1-5;
        int d7 = d1-6;

        int d1Steps = daysCount(getTodayDate());
        int d2Steps = daysCount(getPreviousDate(-1));
        int d3Steps = daysCount(getPreviousDate(-2));
        int d4Steps = daysCount(getPreviousDate(-3));
        int d5Steps = daysCount(getPreviousDate(-4));
        int d6Steps = daysCount(getPreviousDate(-5));
        int d7Steps = daysCount(getPreviousDate(-6));

        System.out.println(d1Steps + " " + d2Steps + " " + d3Steps);



        GraphView graph = (GraphView) findViewById(R.id.graph);
        System.out.println("Dates" + d1 + d3 + d4);
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
                new DataPoint(d7, d7Steps),
                new DataPoint(d6, d6Steps),
                new DataPoint(d5, d5Steps),
                new DataPoint(d4, d4Steps),
                new DataPoint(d3, d3Steps),
                new DataPoint(d2, d2Steps),
                new DataPoint(d1, d1Steps)
        });
        //graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getApplicationContext()));
        graph.getGridLabelRenderer().setNumHorizontalLabels(7);
        graph.addSeries(series);
        series.setColor(Color.rgb(52,152,219));
        series.setAnimated(true);
        series.setBackgroundColor(Color.WHITE);
        series.setThickness(10);
        series.setTitle("Step count");
        series.setDrawAsPath(true);
        graph.clearSecondScale();
        graph.getGridLabelRenderer().setTextSize(28);



       /* series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb(44,62,80);
            }
        });*/

        //series.setSpacing(20);
        graph.getViewport().setScalable(true);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void walkfunction(View v)    //start walking activity page
    {
        Intent i = new Intent(this, StepsActivity.class);
        startActivityForResult(i, 0);
    }

    public void runningfunction(View V) //start RunningActivity activity page
    {
        Intent i = new Intent(this, RunningActivity.class);
        startActivityForResult(i, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        MyDbHelper dbhelper = new MyDbHelper(getApplicationContext());
        SQLiteDatabase db = dbhelper.getWritableDatabase();
        System.out.println("Result code is " + resultCode);
        System.out.println("Request code is " + requestCode);
        switch(requestCode){
            case 0: // Do your stuff here...
                if(resultCode == 1){
                    //THIS IS WALKING INTENT GIVING DATA BACK
                    String steps=data.getStringExtra("steps");
                    String time=data.getStringExtra("time");
                    String km=data.getStringExtra("kilometers");
                    System.out.println(steps);
                   // totalCount = (TextView) findViewById(R.id.totalSteps);
                   // totalCount.setText("Total running count is " + steps);

                    int stepsCount = Integer.parseInt(steps);
                    totalStepsToday = totalStepsToday + stepsCount;
                    _progressBar.setProgress(totalStepsToday);
                    float totalKilometers = Float.parseFloat(km);
                    int totalTime = Integer.parseInt(time);
                    SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
                    String currentDateandTime = datef.format(new Date(date));
                    //TextView set_date = (TextView) findViewById(R.id.date);
                    Calendar ca = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String date = format.format(ca.getTime()).toString();
                    System.out.println("insert into DailySteps values (" + stepsCount + "," + totalKilometers + "," + date + "," + totalTime + "," + "'walking'" +  ")");
                     db.execSQL("insert into DailySteps values (" + stepsCount + "," + totalKilometers + "," + date + "," + totalTime + "," + "'walking'" +  ")");
                    //Log.i(tag,result);
                }

                break;
            case 1: // Do your other stuff here...
                if (resultCode == 2) {
                    //THIS IS RUNNING INTENT GIVING DATA BACK
                    //Write your code if there's no result
                    String steps=data.getStringExtra("steps");
                    String time=data.getStringExtra("time");
                    String km=data.getStringExtra("kilometers");
                    System.out.println(steps);
                    //totalRunningCount = (TextView) findViewById(R.id.totalrunning);
                    //totalRunningCount.setText("Total running count is " + steps);

                    int stepsCount = Integer.parseInt(steps);
                    totalStepsToday = totalStepsToday + stepsCount;
                    _progressBar.setProgress(totalStepsToday);
                    float totalKilometers = Float.parseFloat(km);
                    int totalTime = Integer.parseInt(time);
                    SimpleDateFormat datef = new SimpleDateFormat("dd/MM/yyyy");
                    String currentDateandTime = datef.format(new Date(date));
                   // TextView set_date = (TextView) findViewById(R.id.date);
                    Calendar ca = Calendar.getInstance();
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    String date = format.format(ca.getTime()).toString();
                    System.out.println("insert into DailySteps values (" + stepsCount + "," + totalKilometers + "," + date + "," + totalTime + "," + "'running'" +  ")");
                    db.execSQL("insert into DailySteps values (" + stepsCount + "," + totalKilometers + "," + date + "," + totalTime + "," + "'running'" + ")");

                }
                 break;
        }

        if (requestCode == 1) {
        }

    }//onActivityResult

    public void DetailsActivity(View v)  //get all the graphs
    {
        Intent i = new Intent(this, DetailsActivity.class);
        startActivity(i);
    }

    public void share(View v)
    {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "This is the text that will be shared.");
        startActivity(Intent.createChooser(sharingIntent, "Share using"));
    }

    public void startService(View v)
    {
        startService(new Intent(getBaseContext(), StepCounterService.class));
    }

    public void stopService(View v)
    {
        stopService(new Intent(getBaseContext(), StepCounterService.class));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String steps = intent.getStringExtra("steps");
            totalStepsToday = (int)Float.parseFloat(steps);
            _progressBar.setProgress(totalStepsToday);

            int percentage = (int)((((float)totalStepsToday)/8000f)*100);
            totalSteps.setText(Integer.toString(percentage) + "%");
            Toast.makeText(context, steps, Toast.LENGTH_LONG).show();


            MyDbHelper dbhelper = new MyDbHelper(getApplicationContext());
            SQLiteDatabase sql = dbhelper.getWritableDatabase();
            SQLiteDatabase sqlRead = dbhelper.getReadableDatabase();

            String date = getTodayDate();
            String query = "select * from steps where date='" + date + "'";
            Cursor c = sqlRead.rawQuery(query, null);
            if(c.moveToNext())
            {
                String q = "update steps set count=" + totalStepsToday + " where date='" + date + "'";
                System.out.println(q);
                sql.execSQL(q);
            }
            else
            {
                String q = "Insert into steps values (" + totalStepsToday + ",'" + date + "')";
                System.out.println(q);
                sql.execSQL(q);
            }

        }
    };

    public void otherActivity(View v)
    {
        Intent i = new Intent(this, ActivityOther.class);
        startActivity(i);
    }

    public void bmi(View v)
    {
        Intent i = new Intent(this, BmiActivity.class);
        startActivity(i);
    }

    public void water(View v)
    {
        Intent i = new Intent(this, WaterActivity.class);
        startActivity(i);
    }

    public void goals(View v)
    {
        Intent i = new Intent(this, Goals.class);
        startActivity(i);
    }

    //facebookProfile
    public void facebookProfile(View v)
    {
        Intent i = new Intent(this, MyProfile.class);
        startActivity(i);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    public void gotoad(View view)
    {
        Log.i(tag,"Agaya");
        Intent i = new Intent(MainActivity.this, AdAct.class);
        startActivity(i);
    }

    public String getTodayDate()
    {

        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, -1);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(ca.getTime()).toString();
        return date;
    }

    public String getPreviousDate(int days)
    {

        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, days);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(ca.getTime()).toString();
        System.out.println(date);
        return date;
    }

    public int daysCount(String date)
    {
        MyDbHelper dbhelper = new MyDbHelper(getApplicationContext());
        SQLiteDatabase sqlRead = dbhelper.getReadableDatabase();
        String query = "select * from steps where date='" + date + "'";
        System.out.println(query);
        Cursor c = sqlRead.rawQuery(query,null);
        int mysteps = 0;
        if(c.moveToNext())
        {
            System.out.println("Came into movetonext");
            mysteps = c.getInt(c.getColumnIndex("count"));

        }
        System.out.println("Steps are " + mysteps );
        return mysteps;
    }

    public void setfbstatus(View view) {
        //MyDbHelper dbhelper = new MyDbHelper(getApplicationContext());
        //SQLiteDatabase sqlRead = dbhelper.getReadableDatabase();
        //String date = getTodayDate();
        //String query = "Select count from steps where date=" + date;
        //Cursor c = sqlRead.rawQuery(query,null);
        int steps=totalStepsToday;
        String st="You have reached ";
        //if(c.moveToNext()) {
            //steps = c.getInt(c.getColumnIndex("count"));
             //st=st+steps;
        //}else {
          //  st=st+"0";
        //}
        if(totalStepsToday==0){
            st=st+"0";
        }else {
            st=st+steps;
        }
        st=st+" steps today";



        if (ShareDialog.canShow(ShareLinkContent.class)) {
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                  .setContentTitle("Healthy Me !")
                  .setContentDescription(st)
                  .setImageUrl(Uri.parse("https://goo.gl/AeLbLY"))
                .build();





            shareDialog.show(linkContent);
        }
        // this part is optional

                //}


    }
    public void loginfb(){


        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

}
