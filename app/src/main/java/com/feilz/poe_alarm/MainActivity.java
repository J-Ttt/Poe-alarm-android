package com.feilz.poe_alarm;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import org.w3c.dom.Text;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RewardedVideoAdListener {

    BackgroundService bgService;
    private final static String prefs = "poe-alarm-prefs";
    private final static String currCurrency="currCurrency";
    public String currency;
    boolean isBound = false;
    GraphView graph;
    Linegraph linegraph;
    FloatingActionButton CurrencyIcon,loadtestVideoAd;
    AdView adView;
    private RewardedVideoAd mRewardedVideoAd;
    EditText myEditText;
    Boolean ads = false;
    TextView minVal,maxVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        minVal = (TextView)findViewById(R.id.minVal);
        maxVal = (TextView)findViewById(R.id.maxVal);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        CurrencyIcon = (FloatingActionButton)findViewById(R.id.currencyIcon);
        CurrencyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });

        SharedPreferences settings = getSharedPreferences(prefs,0);
        currency = settings.getString(currCurrency,getString(R.string.chaos_orb));

        graph = (GraphView)findViewById(R.id.graph);
        linegraph = new Linegraph(graph,currency,getResources().getColor(R.color.colorText),this,minVal,maxVal);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);

        MobileAds.initialize(this,getString(R.string.banner_ad_unit_id));
        if (ads) {
            adView = (AdView) findViewById(R.id.adView);
            AdRequest adRequest = new AdRequest.Builder().addTestDevice("C1FB146C50B90BC0745718D2C1E78156").build();
            adView.loadAd(adRequest);

            mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
            mRewardedVideoAd.setRewardedVideoAdListener(this);
        }
        Intent i = new Intent(MainActivity.this, BackgroundService.class);
        bindService(i,bgServiceConnection,Context.BIND_AUTO_CREATE);

        loadtestVideoAd = (FloatingActionButton)findViewById(R.id.testButton);
        loadtestVideoAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //loadRewardedVideoAd();
                Map<Date,Double> newData = bgService.newRandomData();
                linegraph.update(newData);
            }
        });

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    //still need to check the data in the graph on start
    @Override
    protected void onStart(){
        super.onStart();
        linegraph.update(currency);

    }
    protected void onResume(){
        super.onResume();
        /* Don't yet know where exactly to put this.
        Map<Date,Double> newData = bgService.newRandomData();
        linegraph.update(newData);*/
    }
    @Override
    public void onStop(){
        super.onStop();
        SharedPreferences settings = getSharedPreferences(prefs,0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(currCurrency,currency);
        editor.apply();
    }


    private void loadRewardedVideoAd(){
        mRewardedVideoAd.loadAd("ca-app-pub-3940256099942544/5224354917",
                new AdRequest.Builder().addTestDevice("C1FB146C50B90BC0745718D2C1E78156").build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("ResourceType")
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.blessed_orb:
                newSelection(getString(R.string.blessed_orb),getDrawable(R.drawable.blessed));
                break;
            case R.id.orb_of_alchemy:
                newSelection(getString(R.string.orb_of_alchemy),getDrawable(R.drawable.alchemy));
                break;
            case R.id.orb_of_alteration:
                newSelection(getString(R.string.orb_of_alteration),getDrawable(R.drawable.alteration));
                break;
            case R.id.chaos_orb:
                newSelection(getString(R.string.chaos_orb),getDrawable(R.drawable.chaos));
                break;
            case R.id.orb_of_chance:
                newSelection(getString(R.string.orb_of_chance),getDrawable(R.drawable.chance));
                break;
            case R.id.cartographers_chisel:
                newSelection(getString(R.string.cartographer_s_chisel),getDrawable(R.drawable.chisel));
                break;
            case R.id.divine_orb:
                newSelection(getString(R.string.divine_orb),getDrawable(R.drawable.divine));
                break;
            case R.id.exalted_orb:
                newSelection(getString(R.string.exalted_orb),getDrawable(R.drawable.exalted));
                break;
            case R.id.orb_of_fusing:
                newSelection(getString(R.string.orb_of_fusing),getDrawable(R.drawable.fusing));
                break;
            case R.id.gemcutter_prism:
                newSelection(getString(R.string.gemcutter_s_prism),getDrawable(R.drawable.gcp));
                break;
            case R.id.jewellers_orb:
                newSelection(getString(R.string.jeweller_s_orb),getDrawable(R.drawable.jewellers));
                break;
            case R.id.regal_orb:
                newSelection(getString(R.string.regal_orb),getDrawable(R.drawable.regal));
                break;
            case R.id.orb_of_regret:
                newSelection(getString(R.string.orb_of_regret),getDrawable(R.drawable.regret));
                break;
            case R.id.orb_of_scouring:
                newSelection(getString(R.string.orb_of_scouring),getDrawable(R.drawable.scouring));
                break;
            case R.id.chromatic_orb:
                newSelection(getString(R.string.chromatic_orb),getDrawable(R.drawable.chromatic));
                break;
            case R.id.eternal_orb:
                newSelection(getString(R.string.eternal_orb),getDrawable(R.drawable.eternal));
                break;
            case R.id.appr_cartographer:
                newSelection(getString(R.string.apprentice_cartographer_s_sextant),getDrawable(R.drawable.apprcarto));
                break;
            case R.id.armourer_scrap:
                newSelection(getString(R.string.armourer_s_scrap),getDrawable(R.drawable.armourer));
                break;
            case R.id.orb_of_augmentation:
                newSelection(getString(R.string.orb_of_augmentation),getDrawable(R.drawable.augmentation));
                break;
            case R.id.glassblower_bauble:
                newSelection(getString(R.string.glassblower_s_bauble),getDrawable(R.drawable.glassblower));
                break;
            case R.id.journeyman_cartographer:
                newSelection(getString(R.string.journeyman_cartographer_s_sextant),getDrawable(R.drawable.journeycarto));
                break;
            case R.id.master_cartographer:
                newSelection(getString(R.string.master_cartographer_s_sextant),getDrawable(R.drawable.mastercarto));
                break;
            case R.id.mirror_of_kalandra:
                newSelection(getString(R.string.mirror_of_kalandra),getDrawable(R.drawable.mirror));
                break;
            case R.id.scroll_of_portal:
                newSelection(getString(R.string.scroll_of_portal),getDrawable(R.drawable.scrollportal));
                break;
            case R.id.scroll_of_wisdom:
                newSelection(getString(R.string.scroll_of_wisdom),getDrawable(R.drawable.scrollwisdom));
                break;
            case R.id.silver_coin:
                newSelection(getString(R.string.silver_coin),getDrawable(R.drawable.silver));
                break;
            case R.id.orb_of_transmutation:
                newSelection(getString(R.string.orb_of_transmutation),getDrawable(R.drawable.transmutation));
                break;
            case R.id.vaal_orb:
                newSelection(getString(R.string.vaal_orb),getDrawable(R.drawable.vaal));
                break;
            case R.id.blacksmiths_whetstone:
                newSelection(getString(R.string.blacksmith_s_whetstone),getDrawable(R.drawable.whetstone));
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void newSelection(String curr, Drawable drawable){
        currency = curr;
        linegraph.update(currency);
        Map<Date,Double> newData = bgService.newRandomData();
        linegraph.update(newData);
        CurrencyIcon.setImageDrawable(drawable);
    }


    @Override
    public void onRewardedVideoAdLoaded() {
        Toast.makeText(this, "onRewardedVideoAdLoaded", Toast.LENGTH_SHORT).show();
        if (mRewardedVideoAd.isLoaded()){
            mRewardedVideoAd.show();
        }
    }

    @Override
    public void onRewardedVideoAdOpened() {
        Toast.makeText(this, "onRewardedVideoAdOpened", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoStarted() {
        Toast.makeText(this, "onRewardedVideoStarted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdClosed() {
        Toast.makeText(this, "onRewardedVideoAdClosed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        Toast.makeText(this, "onRewarded!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        Toast.makeText(this, "onRewardedVideoAdLeftApplication",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        Toast.makeText(this, "onRewardedVideoAdFailedToLoad", Toast.LENGTH_SHORT).show();
    }

    private ServiceConnection bgServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            BackgroundService.MyLocalBinder binder = (BackgroundService.MyLocalBinder) iBinder;
            bgService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };
    /*
    //Attempted to manually handle different screen sizes, didn't realize until about 5h later that
    //constraintLayout actually does it by itself.... :(
    //Leaving this here as a reminder to myself... read up on stuff better in advance.
    public void initConfig(int orientation) {

        // Log.d("getRight",Integer.toString(callingActivity.getWindow().getDecorView().getRight()));
        // Log.d("getHeight",Integer.toString(callingActivity.getWindow().getDecorView().getHeight()));
        if (orientation == Configuration.ORIENTATION_LANDSCAPE){
            screenHeight = this.getWindow().getDecorView().getBottom();
            screenWidth = this.getWindow().getDecorView().getRight();
            newH = screenHeight * 0.6;
            newW = screenWidth * 0.8;
            constSet.setVerticalBias(R.id.currencyIcon,(float) landscapeBias);
        } else if (orientation == Configuration.ORIENTATION_PORTRAIT){
            screenHeight = this.getWindow().getDecorView().getRight();
            screenWidth = this.getWindow().getDecorView().getBottom();
            newH = screenHeight / 2.2;
            newW = screenHeight * 0.9;
            constSet.setVerticalBias(R.id.currencyIcon,(float) portraitBias);
        }
        constSet.applyTo(mConstLayout);
        linegraph.setGraphSize((int)newW,(int)newH);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //Log.d("getRight",Integer.toString(this.getWindow().getDecorView().getRight()));
        //Log.d("getHeight",Integer.toString(this.getWindow().getDecorView().getHeight()));
        // Checks the orientation of the screen
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            newH = screenHeight * 0.6;
            newW = screenWidth * 0.8;
            constSet.setVerticalBias(R.id.currencyIcon,(float) landscapeBias);
        } else if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            newH = screenHeight / 2.2;
            newW = screenHeight * 0.9;
            constSet.setVerticalBias(R.id.currencyIcon,(float) portraitBias);
        }
        constSet.applyTo(mConstLayout);
        linegraph.setGraphSize((int)newW,(int)newH);
    }
    */
}
