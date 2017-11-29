package com.feilz.poe_alarm;

public class TrackedCurrency {
    public String currency;
    public Boolean lessThan;
    public Double value;
    public String league;
    public TrackedCurrency(String curr, Boolean lt, Double val, String leag){
        this.currency=curr;
        this.lessThan=lt;
        this.value=val;
        this.league = leag;
    }
}
