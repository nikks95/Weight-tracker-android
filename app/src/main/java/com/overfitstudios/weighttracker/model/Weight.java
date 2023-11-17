package com.overfitstudios.weighttracker.model;

public class Weight {
    String[] units = {"kg","lbs","gm"};
    public static final int  KG=0;
    public static final int  LBS=1;
    public static final int  GM=2;
    private double weight;
    private int unit;
    private String[] getUnitItems(){
        return units;
    }

    public Weight(double weight){
        this.weight = weight;
        this.unit = KG;
    }
    public Weight(double weight, int unit) {
        this.weight = weight;
        this.unit = unit;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getUnit() {
        return unit;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public String getUnitName(){
        return units[this.unit];
    }
    public int getUnitsArraySize(){
        return units.length;
    }
    public String getUnitByIndex(int idx){
        return units[idx];
    }

    public double getWeightinKg(){
        switch(this.unit){
            case 0: return this.weight;
            case 1: return this.weight*0.453592;
            case 2: return this.weight/1000;
            default: return 0.0;
        }
    }
}
