package com.jxstarxxx.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jxstarxxx.myapplication.DTO.CaseTrackerDTO;
import com.jxstarxxx.myapplication.myUtils.ApiUtils;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CovidCasesTracker extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private static final String DEFAULT_COUNTRY = "Australia";
    private TextView confirm, active, recovered, death, tests;
    private TextView increaseConfirm, increaseRecovered, increaseDeath;
    private TextView trackerDate;
    private PieChart mPieChart;
    private Spinner spinner;
    private ProgressDialog progressDialog;
    private ArrayAdapter<String> arrayAdapter;

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
        caseTrackerDTOList = new ArrayList<>();

        spinner = (Spinner)findViewById(R.id.country_select);
        spinner.setDropDownWidth(600);
        spinner.setDropDownHorizontalOffset(100);
        spinner.setDropDownVerticalOffset(300);
        arrayAdapter = new ArrayAdapter<String>(CovidCasesTracker.this,
        R.layout.spinner_style_covid_tracker, getResources().getStringArray(R.array.areaList));
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(CovidCasesTracker.this);
        // default position, Australia
        spinner.setSelection(6);

        progressDialog = new ProgressDialog(CovidCasesTracker.this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        ApiUtils.getApi().getCountryData()
                .enqueue(new Callback<List<CaseTrackerDTO>>() {
                    @Override
                    public void onResponse(Call<List<CaseTrackerDTO>> call, Response<List<CaseTrackerDTO>> response) {
                        caseTrackerDTOList.addAll(response.body());
                        fillChart(DEFAULT_COUNTRY);
                        progressDialog.dismiss();
                    }
                    @Override
                    public void onFailure(Call<List<CaseTrackerDTO>> call, Throwable t) {
                        Toast.makeText(CovidCasesTracker.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT);
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

    public void fillChart (String country) {
        if(country == null){
            return;
        }
        for (CaseTrackerDTO caseTracker : caseTrackerDTOList) {
            if (country.equals(caseTracker.getCountry())) {
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

                mPieChart.clearChart();
                mPieChart.addPieSlice(new PieModel("Confirm", tempConfirm, getResources().getColor(R.color.yellow_pie)));
                mPieChart.addPieSlice(new PieModel("Active", tempActive, getResources().getColor(R.color.blue_pie)));
                mPieChart.addPieSlice(new PieModel("Recovered", tempRecovered, getResources().getColor(R.color.green_pie)));
                mPieChart.addPieSlice(new PieModel("Death", tempDeath, getResources().getColor(R.color.red_pie)));
                mPieChart.startAnimation();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String area = arrayAdapter.getItem(position);
        Log.i("T",String.valueOf(caseTrackerDTOList.size()));
        fillChart(area);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}