package com.overfitstudios.weighttracker.formatter;

import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.Locale;

public class YAxisFormatter extends ValueFormatter {
    @Override
    public String getFormattedValue(float value) {
        String result = String.format(Locale.getDefault(),"%.2f",value);
        return result;
    }
}
