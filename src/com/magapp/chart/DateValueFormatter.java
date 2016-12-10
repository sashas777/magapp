/*
 * @category     Sashas
 * @package      com.magapp
 * @author       Sashas IT Support <support@sashas.org>
 * @copyright    2007-2016 Sashas IT Support Inc. (http://www.sashas.org)
 * @license      http://opensource.org/licenses/GPL-3.0  GNU General Public License, version 3 (GPL-3.0)
 * @link         https://play.google.com/store/apps/details?id=com.magapp.main
 *
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
