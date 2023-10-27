package org.um.feri.ears.individual.btdemo.gp.behaviour;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Property {
    String name;
    int minValue;
    int maxValue;
    int currentValue;

    public Property( String name, int minValue, int maxValue, int currentValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.currentValue = currentValue;
        this.name = name;
    }

    @JsonProperty("Name")
    public String getName() {
        return name;
    }
    @JsonProperty("Name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonIgnore
    public int getMinValue() {
        return minValue;
    }
    @JsonIgnore
    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }
    @JsonIgnore
    public int getMaxValue() {
        return maxValue;
    }
    @JsonIgnore
    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }
    @JsonProperty("Value")
    public int getCurrentValue() {
        return currentValue;
    }
    @JsonProperty("Value")
    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;
    }

}
