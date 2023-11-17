package com.overfitstudios.weighttracker.formatter;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.overfitstudios.weighttracker.model.WeightEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class XAxisFormatter extends ValueFormatter{
    private final List<WeightEntry> weightEntries;

    @Override
    public String getAxisLabel(float value, AxisBase axis) {
        int index = (int) value;
        if(index>=0 && index < weightEntries.size()){
            long timestamp = weightEntries.get(index).getTimestamp().getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(timestamp);
            int year = calendar.get(Calendar.YEAR);

            if(index==0 || year!=Calendar.getInstance().get(Calendar.YEAR)){
                return String.valueOf(year);
            }
            else{
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d", Locale.getDefault());
                return sdf.format(calendar.getTime());
            }
        }
        return "";
    }

    public XAxisFormatter(List<WeightEntry> weightEntries) {
        this.weightEntries = weightEntries;
    }

}
