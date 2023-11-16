package com.overfitstudios.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private LineChart lineChart=null;
    private FloatingActionButton addWeightButton;
    private LinearLayout formLayout;
    private ImageView closeButton;
    private Button cancelButton;
    TextView dateSelect;

    private boolean isSlide = false;
    private void initComponents(){
        lineChart = findViewById(R.id.lineChart1);
        addWeightButton = findViewById(R.id.addWeightfButton);

        formLayout = findViewById(R.id.formLinearLayout);
        closeButton = findViewById(R.id.closeButton);
        cancelButton = findViewById(R.id.cancelButton);

        dateSelect = findViewById(R.id.dateSelector);
        //Set Listeners at end only
        setListeners();
        //slideUp();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        setupLineChart(getLineData(),lineChart);

    }

    void setListeners(){
        addWeightButton.setOnClickListener(view -> {
            //animate button
                animateButton(view);
            //todo action on the button
                showWeightAddMenu();
        });

        cancelButton.setOnClickListener( view -> slideDown());

        closeButton.setOnClickListener(view -> slideDown());

        dateSelect.setOnClickListener(view-> openDatePicker());


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
        //addWeightButton.setVisibility(View.INVISIBLE);
        isSlide = true;
    }
    private void slideDown(){
        slide(formLayout,formLayout.getHeight());
        //addWeightButton.setVisibility(View.VISIBLE);
        isSlide = false;
    }
    private void animateButton(View view){
        ViewPropertyAnimator animator = view.animate()
                .setStartDelay(100)
                .rotation(45f)
                .setInterpolator(new AccelerateInterpolator());
        animator.withEndAction(() -> view.setRotation(45f));
        animator.start();
    }
    private void showWeightAddMenu(){
        //slideDown();
        toggleFormLayout();
    }
    LineData getLineData(){
        List<Entry> entries = new ArrayList<>();
        for(int i=0;i<10;i++){
            int y = (int)(Math.random()*1000)%100;
         entries.add(new Entry(i,y));
        }
        LineDataSet dataSet = new LineDataSet(entries,"Random Sampling");
        dataSet.setColor(Color.rgb(135,16,156));
        dataSet.setValueTextColor(Color.WHITE);
        return new LineData(dataSet);
    }
    void setupLineChart(LineData data,LineChart chart){
            chart.setData(data);
            chart.invalidate();
    }
}