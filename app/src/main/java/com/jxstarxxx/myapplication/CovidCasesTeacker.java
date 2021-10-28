package com.jxstarxxx.myapplication;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.jxstarxxx.myapplication.DTO.CaseTrackerDTO;
import com.jxstarxxx.myapplication.myUtils.ApiUtils;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CovidCasesTeacker extends AppCompatActivity {

    private TextView confirm, active, recovered, death, tests;
    private TextView increaseConfirm, increaseRecovered, increaseDeath;
    private TextView trackerDate;
    private PieChart mPieChart;

    List<CaseTrackerDTO> caseTrackerDTOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_covid_cases_teacker);

        confirm = findViewById(R.id.confirm);
        active = findViewById(R.id.active);
        recovered = findViewById(R.id.recovered);
        death = findViewById(R.id.death);
        tests = findViewById(R.id.test);

        increaseConfirm = findViewById(R.id.increase_confirm);
        increaseRecovered = findViewById(R.id.increase_recovered);
        increaseDeath = findViewById(R.id.increase_death);

        trackerDate = findViewById(R.id.tracker_date);

        mPieChart = findViewById(R.id.pieChart);

        ApiUtils.getApi().getCountryData()
                .enqueue(new Callback<List<CaseTrackerDTO>>() {
                    @Override
                    public void onResponse(Call<List<CaseTrackerDTO>> call, Response<List<CaseTrackerDTO>> response) {
                        caseTrackerDTOList = new ArrayList<>(response.body());
                        for (CaseTrackerDTO caseTracker : caseTrackerDTOList) {
                            if ("Australia".equals(caseTracker.getCountry())) {
                                Log.i("Parser", caseTracker.toString());
                                int tempConfirm = Integer.parseInt(caseTracker.getCases());
                                int tempActive = Integer.parseInt(caseTracker.getActive());
                                int tempRecovered = Integer.parseInt(caseTracker.getRecovered());
                                int tempDeath = Integer.parseInt(caseTracker.getDeaths());
                                int tempTests = Integer.parseInt(caseTracker.getTests());

                                confirm.setText(NumberFormat.getInstance().format(tempConfirm));
                                active.setText(NumberFormat.getInstance().format(tempActive));
                                recovered.setText(NumberFormat.getInstance().format(tempRecovered));
                                death.setText(NumberFormat.getInstance().format(tempDeath));
                                tests.setText(NumberFormat.getInstance().format(tempTests));

                                increaseConfirm.setText("+(" + NumberFormat.getInstance().format(Integer.parseInt(caseTracker.getTodayCases())) + ")");
                                increaseRecovered.setText("+(" + NumberFormat.getInstance().format(Integer.parseInt(caseTracker.getTodayRecovered())) + ")");
                                increaseDeath.setText("+(" + NumberFormat.getInstance().format(Integer.parseInt(caseTracker.getTodayDeaths())) + ")");

                                setTrackerDate(caseTracker.getUpdated());

                                mPieChart.addPieSlice(new PieModel("Confirm", tempConfirm, getResources().getColor(R.color.yellow_pie)));
                                mPieChart.addPieSlice(new PieModel("Active", tempActive, getResources().getColor(R.color.blue_pie)));
                                mPieChart.addPieSlice(new PieModel("Recovered", tempRecovered, getResources().getColor(R.color.green_pie)));
                                mPieChart.addPieSlice(new PieModel("Death", tempDeath, getResources().getColor(R.color.red_pie)));

                                mPieChart.startAnimation();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CaseTrackerDTO>> call, Throwable t) {
                        Toast.makeText(CovidCasesTeacker.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT);
                    }
                });

   }

   private void setTrackerDate (String update) {
       DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

       long milliseconds = Long.parseLong(update);

       Calendar calendar = Calendar.getInstance();
       calendar.setTimeInMillis(milliseconds);

       trackerDate.setText("Updated at " + format.format(calendar.getTime()));
    }
}