/*
 * Copyright (c) 2016.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

/**
 * Created by Sashas on 12/6/2016.
 */
public class DateValueFormatter implements IAxisValueFormatter {

    private ArrayList<String> mFormat;

    public DateValueFormatter(ArrayList<String> formattedValues) {
        mFormat = formattedValues;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.get(Math.round(value));
    }
}
