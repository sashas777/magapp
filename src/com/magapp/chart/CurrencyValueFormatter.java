/*
 * Copyright (c) 2016.  Sashas IT  Support
 * http://www.sashas.org
 */

package com.magapp.chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
/**
 * Created by Sashas on 12/6/2016.
 */
public class CurrencyValueFormatter implements IAxisValueFormatter {

    private DecimalFormat mFormat;

    public CurrencyValueFormatter() {
        mFormat = new DecimalFormat("###,###,###,##0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mFormat.format(value) + " $";
    }
}
