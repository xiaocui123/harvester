package com.harvester.generator.helper.nutrient;

import com.google.common.base.MoreObjects;

/**
 * Created by cui on 2017/11/7.
 */
public class MeaturedDataType {

    //水层
    private double level;
    private double po4;
    private double no3;
    private double sio3;

    public double getLevel() {
        return level;
    }

    public void setLevel(double level) {
        this.level = level;
    }

    public double getPo4() {
        return po4;
    }

    public void setPo4(double po4) {
        this.po4 = po4;
    }

    public double getNo3() {
        return no3;
    }

    public void setNo3(double no3) {
        this.no3 = no3;
    }

    public double getSio3() {
        return sio3;
    }

    public void setSio3(double sio3) {
        this.sio3 = sio3;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("水层", level).add("PO4", po4).add("NO3", no3).add("SIO3", sio3).toString();
    }
}
