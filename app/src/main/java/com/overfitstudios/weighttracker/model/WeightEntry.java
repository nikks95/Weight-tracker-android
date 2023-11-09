package com.overfitstudios.weighttracker.model;

import java.text.DecimalFormat;
import java.util.Date;

public class WeightEntry {
    private Date timestamp;
    private Weight weight;
    public WeightEntry(Date timestamp, double weight,int unit){
        this.weight = new Weight(weight,unit);
        this.timestamp = timestamp;
    }
    public WeightEntry(Date timestamp, Weight weight) {
        this.timestamp = timestamp;
        this.weight = weight;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }
    public String getFormattedWeight(double weight){
        DecimalFormat df = new DecimalFormat("0.0");
        return df.format(weight);
    }
}
