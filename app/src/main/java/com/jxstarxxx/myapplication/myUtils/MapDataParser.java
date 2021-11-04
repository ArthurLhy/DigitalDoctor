package com.jxstarxxx.myapplication.myUtils;

import com.jxstarxxx.myapplication.DTO.HospitalsDTO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MapDataParser {

    public List<HospitalsDTO> parse (String jsonData) {

        List<HospitalsDTO> hospitalsDTOS = new ArrayList<>();
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject hospitalJson = jsonArray.getJSONObject(i);
                HospitalsDTO hospitalsDTO = new HospitalsDTO();

                if(hospitalJson.getString("name") != null) {
                    hospitalsDTO.setHospitalName(hospitalJson.getString("name"));
                } else {
                    hospitalsDTO.setHospitalName("Not Available");
                }

                try {
                    hospitalsDTO.setRating(String.valueOf(hospitalJson.getDouble("rating")));
                } catch (Exception e){
                    hospitalsDTO.setRating("Not Available");
                }

                try {
                    if (hospitalJson.getJSONObject("opening_hours").getBoolean("open_now")) {
                        hospitalsDTO.setOpeningHours("Opened");
                    } else {
                        hospitalsDTO.setOpeningHours("Closed");
                    }
                } catch (Exception e) {
                    hospitalsDTO.setOpeningHours("Not Available");
                }

                hospitalsDTO.setAddress(hospitalJson.getString("vicinity"));
                hospitalsDTO.setLatitude(hospitalJson.getJSONObject("geometry").getJSONObject("location").getDouble("lat"));
                hospitalsDTO.setLongitude(hospitalJson.getJSONObject("geometry").getJSONObject("location").getDouble("lng"));

                hospitalsDTOS.add(hospitalsDTO);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return hospitalsDTOS;
    }
}
