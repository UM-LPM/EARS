package org.um.feri.ears.individual.representations.gp.behaviour;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.um.feri.ears.util.Util;

import java.io.Serializable;

public class Property implements Cloneable, Serializable {
    String name;
    int minValue;
    int maxValue;
    int value;

    public Property( String name, int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = Util.rnd.nextInt(maxValue - minValue) + minValue;
        this.name = name;
    }

    public Property( String name, int minValue, int maxValue, int currentValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = currentValue;
        this.name = name;
    }

    public void setRandomValue(){
        this.value = Util.rnd.nextInt(maxValue - minValue) + minValue;
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
    public int getValue() {
        return value;
    }
    @JsonProperty("Value")
    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public Property clone() {
        try {
            return (Property) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen
        }
    }
}
