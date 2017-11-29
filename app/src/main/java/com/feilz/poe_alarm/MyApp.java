package com.feilz.poe_alarm;

import android.app.Application;
import android.content.res.Configuration;



public class MyApp extends Application {
    private LeagueSpinnerAdapter leagueChoice;

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig){
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onLowMemory(){
        super.onLowMemory();
    }

    @Override
    public void onTerminate(){
        super.onTerminate();
    }

    public MyApp(){}

    public void setLeagueChoice(LeagueSpinnerAdapter spinnerAdapter){
        this.leagueChoice = spinnerAdapter;
    }
    public LeagueSpinnerAdapter getLeagueChoice(){return this.leagueChoice;}



}
