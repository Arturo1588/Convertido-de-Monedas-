package com.example;

import java.util.Map;
import com.google.gson.annotations.SerializedName;

public class ExchangeRateResponse {
    @SerializedName("base_code")
    private String base;

    @SerializedName("conversion_rates")
    private Map<String, Double> rates;

    // Getters
    public String getBase() {
        return base;
    }

    public Map<String, Double> getRates() {
        return rates;
    }
}



