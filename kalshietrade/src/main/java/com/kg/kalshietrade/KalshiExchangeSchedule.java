package com.kg.kalshietrade;
import org.json.JSONArray;
import org.json.JSONObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;


public class KalshiExchangeSchedule {
    public static void main(String[] args) {

        HttpResponse<String> response =
                Unirest.get("https://api.elections.kalshi.com/trade-api/v2/exchange/schedule")
                        .asString();

        if (response.getStatus() != 200) {
            System.out.println("Error: " + response.getStatus());
            return;
        } else {
            System.out.println("Success");
        }
        System.out.println(response.getBody());
        JSONObject jresponse = new JSONObject(response.getBody());

        JSONObject schedule = jresponse.getJSONObject("schedule");
        JSONArray standHours = schedule.getJSONArray("standard_hours");
        printDay(standHours, "monday");
        printDay(standHours, "tuesday");
        printDay(standHours, "wednesday");
        printDay(standHours, "thursday");
        printDay(standHours, "friday");
        printDay(standHours, "saturday");
        printDay(standHours, "sunday");
    }

    private static void printDay(JSONArray hours, String day) {
        System.out.println(day.substring(0, 1).toUpperCase() + day.substring(1) + ":");
        JSONObject period = hours.getJSONObject(0);

        JSONArray sessions = period.getJSONArray(day);

        for (int i = 0; i < sessions.length(); i++) {
            JSONObject session = sessions.getJSONObject(i);
            String open = session.getString("open_time");
            String close = session.getString("close_time");
            if (open.equals("00:00") && close.equals("00:00")) {
                System.out.println("Open All Day");
            }
            else {
                System.out.println("  " + open + " to " + close);
            }
        }

        System.out.println();
    }
}