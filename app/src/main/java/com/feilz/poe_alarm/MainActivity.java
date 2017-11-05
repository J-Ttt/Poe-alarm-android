package com.feilz.poe_alarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RewardedVideoAdListener {

    GraphView graph;
    Linegraph linegraph;
    FloatingActionButton CurrencyIcon,loadtestVideoAd;
    AdView adView;
    private RewardedVideoAd mRewardedVideoAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        CurrencyIcon = (FloatingActionButton)findViewById(R.id.currencyIcon);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        CurrencyIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout)findViewById(R.id.drawer_layout);
                if (!drawer.isDrawerOpen(GravityCompat.START)){
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        graph = (GraphView)findViewById(R.id.graph);
        linegraph = new Linegraph(graph,getString(R.string.chaos_orb),getResources().getColor(R.color.colorText),this);
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        MobileAds.initialize(this,getString(R.string.banner_ad_unit_id));

        adView = (AdView)findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice("C1FB146C50B90BC0745718D2C1E78156").build();
        adView.loadAd(adRequest);

        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);

        loadtestVideoAd = (FloatingActionButton)findViewById(R.id.testButton);
        loadtestVideoAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadRewardedVideoAd();
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
                linegraph.update(getString(R.string.blessed_orb));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.blessed));
                break;
            case R.id.orb_of_alchemy:
                linegraph.update(getString(R.string.orb_of_alchemy));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.alchemy));
                break;
            case R.id.orb_of_alteration:
                linegraph.update(getString(R.string.orb_of_alteration));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.alteration));
                break;
            case R.id.chaos_orb:
                linegraph.update(getString(R.string.chaos_orb));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.chaos));
                break;
            case R.id.orb_of_chance:
                linegraph.update(getString(R.string.orb_of_chance));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.chance));
                break;
            case R.id.cartographers_chisel:
                linegraph.update(getString(R.string.cartographer_s_chisel));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.chisel));
                break;
            case R.id.divine_orb:
                linegraph.update(getString(R.string.divine_orb));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.divine));
                break;
            case R.id.exalted_orb:
                linegraph.update(getString(R.string.exalted_orb));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.exalted));
                break;
            case R.id.orb_of_fusing:
                linegraph.update(getString(R.string.orb_of_fusing));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.fusing));
                break;
            case R.id.gemcutter_prism:
                linegraph.update(getString(R.string.gemcutter_s_prism));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.gcp));
                break;
            case R.id.jewellers_orb:
                linegraph.update(getString(R.string.jeweller_s_orb));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.jewellers));
                break;
            case R.id.regal_orb:
                linegraph.update(getString(R.string.regal_orb));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.regal));
                break;
            case R.id.orb_of_regret:
                linegraph.update(getString(R.string.orb_of_regret));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.regret));
                break;
            case R.id.orb_of_scouring:
                linegraph.update(getString(R.string.orb_of_scouring));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.scouring));
                break;
            case R.id.chromatic_orb:
                linegraph.update(getString(R.string.chromatic_orb));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.chromatic));
                break;
            case R.id.eternal_orb:
                linegraph.update(getString(R.string.eternal_orb));
                CurrencyIcon.setImageDrawable(getDrawable(R.drawable.eternal));
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
