package com.example.zahid.homeautomation;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.zahid.homeautomation.Model.Month;
import com.example.zahid.homeautomation.Utill.Common;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LineChartActivity extends AppCompatActivity {

    LineChart mChart;
    Query monthTable;
    List<String> moUnits, moMonths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        mChart = findViewById(R.id.lineChart);
        moUnits = new ArrayList<>();
        moMonths = new ArrayList<>();
        monthTable = FirebaseDatabase.getInstance().getReference(Common.STR_Month)
                .orderByChild("devicemac")
                .equalTo("asd1234");

        fetchingDataFromDb();

    }

    private void fetchingDataFromDb() {

        monthTable.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                moUnits.clear();
                for (DataSnapshot monthDataSnapShot : dataSnapshot.getChildren()) {
                    Month monthData = monthDataSnapShot.getValue(Month.class);
                    if (monthData != null) {
                        moUnits.add(monthData.getMounits());
                        moMonths.add(monthData.getMonth());
                    }
                }
                settingUnits();
                settingMonth();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LineChartActivity.this, (CharSequence) databaseError, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void settingUnits() {
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

//        LimitLine limitLine = new LimitLine(85f, "Danger");
//        limitLine.setLineWidth(4f);
//        limitLine.enableDashedLine(10f, 10f, 0f);
//        limitLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_TOP);
//        limitLine.setTextSize(12f);

        YAxis leftAxis = mChart.getAxisLeft();
//        leftAxis.addLimitLine(limitLine);
        leftAxis.setDrawLimitLinesBehindData(true);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);

        YAxis rightAxis = mChart.getAxisRight();

        ArrayList<Entry> yValues = new ArrayList<>();

//        int units = 10;
//        Random r = new Random();

        for (int i = 0; i <= moUnits.size() - 1; i++) {
//            yValues.add(new Entry(i, units));
//            units = r.nextInt(100 - 30) + 30;
            yValues.add(new Entry(i, Float.parseFloat(moUnits.get(i))));
        }


        LineDataSet set1 = new LineDataSet(yValues, "Units");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(2f);
        set1.setValueTextSize(10f);
        set1.setMode(LineDataSet.Mode.LINEAR);


        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        LineData data = new LineData(dataSets);
        mChart.setData(data);
    }

    private void settingMonth() {
        String[] values = new String[12];

        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter(moMonths));
        xAxis.setGranularity(1);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

    }

    public class MyXAxisValueFormatter implements IAxisValueFormatter {
//        private String[] mValues;
        private List<String> Months = new ArrayList<>();

        private MyXAxisValueFormatter(List<String> Months) {
            this.Months = Months;
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return Months.get((int) value);
        }
    }
}
