package com.overfitstudios.weighttracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.AccelerateInterpolator;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.overfitstudios.weighttracker.dbhandler.WeightItemDBHandler;
import com.overfitstudios.weighttracker.model.Weight;
import com.overfitstudios.weighttracker.model.WeightEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LineChart lineChart=null;
    private FloatingActionButton addWeightButton;
    private void initComponents(){
        lineChart = findViewById(R.id.lineChart1);
        addWeightButton = findViewById(R.id.addWeightfButton);

        //Set Listeners at end only
        setListeners();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initComponents();
        setupLineChart(getLineData(),lineChart);
    }

    void setListeners(){
        addWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //animate button
                    animateButton(view);
                //todo action on the button
                    showWeightAddMenu(getApplicationContext(),view);
            }
        });


    }
    private void animateButton(View view){
        ViewPropertyAnimator animator = view.animate()
                .setStartDelay(300)
                .rotation(45f)
                .setInterpolator(new AccelerateInterpolator());
        animator.withEndAction(() -> view.setRotation(45f));
        Toast.makeText(getApplicationContext(),"Rotation",Toast.LENGTH_LONG).show();
        animator.start();
    }
    private void showWeightAddMenu(Context context, View view){

    }
    LineData getLineData(){
        List<Entry> entries = new ArrayList<>();
        for(int i=0;i<10;i++){
         int x = i;
         int y = (int)(Math.random()*1000)%100;
         entries.add(new Entry(x,y));
        }
        LineDataSet dataSet = new LineDataSet(entries,"Random Sampling");
        dataSet.setColor(Color.rgb(135,16,156));
        dataSet.setValueTextColor(Color.WHITE);
        LineData lineData = new LineData(dataSet);
        return lineData;
    }
    void setupLineChart(LineData data,LineChart chart){
            chart.setData(data);
            chart.invalidate();
    }
}