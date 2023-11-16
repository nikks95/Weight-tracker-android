package com.overfitstudios.weighttracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.overfitstudios.weighttracker.dbhandler.DBHandler;
import com.overfitstudios.weighttracker.dbhandler.WeightItemDBHandler;
import com.overfitstudios.weighttracker.formatter.XAxisFormatter;
import com.overfitstudios.weighttracker.formatter.YAxisFormatter;
import com.overfitstudios.weighttracker.model.Weight;
import com.overfitstudios.weighttracker.model.WeightEntry;
import com.overfitstudios.weighttracker.util.DateUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LineChart lineChart=null;
    private FloatingActionButton addWeightButton;
    private LinearLayout formLayout;
    private ImageView closeButton;
    private Button cancelButton;
    private Button submitButton;
    private TextInputEditText weightInputBox;
    TextView dateSelect;

    private boolean isSlide = false;
    List<WeightEntry> entries;
    private void initComponents(){
        lineChart = findViewById(R.id.lineChart1);
        addWeightButton = findViewById(R.id.addWeightfButton);

        formLayout = findViewById(R.id.formLinearLayout);
        closeButton = findViewById(R.id.closeButton);
        cancelButton = findViewById(R.id.cancelButton);
        submitButton = findViewById(R.id.submitButton);

        weightInputBox = findViewById(R.id.inputWeight);

        dateSelect = findViewById(R.id.dateSelector);
        dateSelect.setInputType(InputType.TYPE_NULL);
        //defaults
        setDefaults();
        entries = populateData();
        //Set Listeners at end only
        setListeners();
        //slideUp();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        populateChart();

    }
    private void populateChart(){
        entries = populateData();
        setupLineChart(getLineData(entries),lineChart);
    }
    private void setDefaults(){
        weightInputBox.setText("");
        weightInputBox.setHint("Weight");

        dateSelect.setText("");
        dateSelect.setHint("dd/mm/yyyy");
    }
    private void setListeners(){
        addWeightButton.setOnClickListener(view -> {
            //animate button
                animateButton(view);
            //todo action on the button
                showWeightAddMenu();
        });

        cancelButton.setOnClickListener( view -> slideDown());

        closeButton.setOnClickListener(view -> slideDown());

        dateSelect.setOnClickListener(view-> openDatePicker());

        submitButton.setOnClickListener(view->saveData());


    }
    private void saveData(){

        double weight  = Double.parseDouble(weightInputBox.getText().toString().trim());
        Date date = DateUtils.getDateFromString(dateSelect.getText().toString().trim());
        WeightEntry weightEntry = new WeightEntry(date,new Weight(weight));
        WeightItemDBHandler dbHandler = new WeightItemDBHandler(getApplicationContext());
        boolean status = dbHandler.addToWeightTable(weightEntry);
        if(status==false) ;
        populateChart();
        slideDown();
    }
    private void openDatePicker(){
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year_, month_, day_) -> {
            calendar.set(Calendar.YEAR,year_);
            calendar.set(Calendar.MONTH,month_);
            calendar.set(Calendar.DAY_OF_MONTH,day_);
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            String formattedDate = sdf.format(calendar.getTime());
            dateSelect.setText(formattedDate);
        },year,month,day
        );
        datePickerDialog.show();
    }
    private void slide(View view,float height){
        ObjectAnimator slideUpAnimator = ObjectAnimator.ofFloat(view,"translationY",height);
        slideUpAnimator.setDuration(500);
        slideUpAnimator.setInterpolator(new AccelerateInterpolator());
        slideUpAnimator.start();
    }
    private  void toggleFormLayout(){
        if(isSlide){
            slideDown();
        }
        else{
            slideUp();
        }

        
    }
    private void slideUp(){
        slide(formLayout,0f);
        addWeightButton.setVisibility(View.INVISIBLE);
        isSlide = true;
    }
    private void slideDown(){
        slide(formLayout,formLayout.getHeight());
        addWeightButton.setVisibility(View.VISIBLE);
        isSlide = false;
        setDefaults();


    }
    private void animateButton(View view){
//        ViewPropertyAnimator animator = view.animate()
//                .setStartDelay(100)
//                .rotation(45f)
//                .setInterpolator(new AccelerateInterpolator());
//        animator.withEndAction(() -> view.setRotation(45f));
//        animator.start();
    }
    private void showWeightAddMenu(){
        //slideDown();
        toggleFormLayout();
    }
    private LineData getLineData(List<WeightEntry> weightEntries){
        List<Entry> entries = new ArrayList<>();
        int x = 0;
        for(WeightEntry entry:weightEntries){
            entries.add(new Entry(x,(float) (0.0f+entry.getWeight().getWeightinKg())));
            Log.d("WEIGHTTAG",""+entry);
            x+=1;
        }
        LineDataSet dataSet = new LineDataSet(entries,"Weight Tracker");
        dataSet.setColor(ContextCompat.getColor(getApplicationContext(),R.color.purple_700));
        dataSet.setValueTextColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        return new LineData(dataSet);

    }

    private List<WeightEntry> populateData(){
        WeightItemDBHandler dbHandler = new WeightItemDBHandler(getApplicationContext());
        List<WeightEntry> weightEntries = dbHandler.getAllWeightEntries();
        Collections.sort(weightEntries, (entry1, entry2) -> DateUtils.compareDate(entry1.getTimestamp(),entry2.getTimestamp()));
//          return getSampleWeightEntries();
        return weightEntries;
    }
    void setupLineChart(LineData data,LineChart chart){
        chart.setData(data);

        XAxis xAxis = chart.getXAxis();
        xAxis.setValueFormatter(new XAxisFormatter(entries));
        xAxis.setGranularity(1f);

        YAxis yAxis = chart.getAxisLeft();
        yAxis.setValueFormatter(new YAxisFormatter());

        chart.getDescription().setText("Weight Tracker Chart");

        chart.invalidate();
    }
    private List<WeightEntry> getSampleWeightEntries() {
        List<WeightEntry> entries = new ArrayList<>();
        entries.add(new WeightEntry(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000, 70.5));
        entries.add(new WeightEntry(System.currentTimeMillis() - 6 * 24 * 60 * 60 * 1000, 71.2));
        entries.add(new WeightEntry(System.currentTimeMillis() - 5 * 24 * 60 * 60 * 1000, 70.8));
        entries.add(new WeightEntry(System.currentTimeMillis() - 4 * 24 * 60 * 60 * 1000, 69.9));
        entries.add(new WeightEntry(System.currentTimeMillis() - 3 * 24 * 60 * 60 * 1000, 70.7));
        entries.add(new WeightEntry(System.currentTimeMillis() - 2 * 24 * 60 * 60 * 1000, 71.5));
        entries.add(new WeightEntry(System.currentTimeMillis() - 24 * 60 * 60 * 1000, 2.0));

        return entries;
    }
}