package com.example.dashboardmobileapp.Domain;

import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dashboardmobileapp.Activities.SearchActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchWallet {
    private String cryptoName;
    private String cryptoSymbol;
    private Double cryptoPrice;
    private Double changePercent;
    private ArrayList<Integer> lineData;
    private Double propertyAmount;
    private Double propertySize;
    private Boolean starterpage;
    private String type;

    public SearchWallet(String cryptoName, Double cryptoPrice, Double changePercent, ArrayList<Integer> lineData, Boolean starterpage, String type) {
        this.cryptoName = cryptoName;
        this.cryptoPrice = cryptoPrice;
        this.changePercent = changePercent;
        this.lineData = lineData;
        this.starterpage = starterpage;
        this.type = type;
    }

    public String getType(){return type;}
    public void setType(String type){this.type = type;}

    public String getCryptoName() {
        return cryptoName;
    }

    public void setCryptoName(String cryptoName) {
        this.cryptoName = cryptoName;
    }

    public String getCryptoSymbol() {
        return cryptoSymbol;
    }

    public void setCryptoSymbol(String cryptoSymbol) {
        this.cryptoSymbol = cryptoSymbol;
    }

    public Double getCryptoPrice() {
        return cryptoPrice;
    }

    public void setCryptoPrice(Double cryptoPrice) {
        this.cryptoPrice = cryptoPrice;
    }

    public Double getChangePercent() {
        return changePercent;
    }

    public void setChangePercent(Double changePercent) {
        this.changePercent = changePercent;
    }

    public ArrayList<Integer> getLineData() {
        return lineData;
    }

    public void setLineData(ArrayList<Integer> lineData) {
        this.lineData = lineData;
    }

    public Double getPropertyAmount() {
        return propertyAmount;
    }

    public void setPropertyAmount(Double propertyAmount) {
        this.propertyAmount = propertyAmount;
    }

    public Double getPropertySize() {
        return propertySize;
    }

    public void setPropertySize(Double propertySize) {
        this.propertySize = propertySize;
    }

    public Boolean getStarterpage() {
        return starterpage;
    }

    public void setStarterpage(Boolean starterpage) {
        this.starterpage = starterpage;
    }

}
