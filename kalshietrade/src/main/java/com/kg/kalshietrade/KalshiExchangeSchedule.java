package com.kg.kalshietrade;
import org.json.JSONObject;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;


public class KalshiExchangeSchedule {
    public static void main(String[] args) {

        HttpResponse<String> response =
                Unirest.get("https://api.elections.kalshi.com/trade-api/v2/exchange/schedule")
                        .asString();

        if (response.getStatus() != 200) {
            System.out.println("Error: " + response.getStatus());
            return;
        }

        String json = response.getBody();

        printDay(json, "monday");
        printDay(json, "tuesday");
        printDay(json, "wednesday");
        printDay(json, "thursday");
        printDay(json, "friday");
        printDay(json, "saturday");
        printDay(json, "sunday");
    }
    private static void printDay(String json, String day) {
        System.out.println(day.substring(0, 1).toUpperCase() + day.substring(1) + ":");
        String key = "\"" + day + "\":";
        int start = json.indexOf(key);
        if (start == -1) {
            System.out.println("  Not found\n");
            return;
        }
        int arrayStart = json.indexOf("[", start);
        int arrayEnd = json.indexOf("]", arrayStart);

        String dayArray = json.substring(arrayStart + 1, arrayEnd);
        String[] sessions = dayArray.split("\\},\\{");

        for (String s : sessions) {
            String open = extractValue(s, "open_time");
            String close = extractValue(s, "close_time");
            if (open.equals("00:00") && close.equals("00:00")) {
                System.out.println("Closed");
            }
            else {
                System.out.println("  " + open + " → " + close);
            }
        }
        System.out.println();
    }
    private static String extractValue(String text, String key) {
        String pattern = "\"" + key + "\":\"";
        int start = text.indexOf(pattern);
        if (start == -1) return "N/A";
        start += pattern.length();
        int end = text.indexOf("\"", start);
        return text.substring(start, end);
    }
}