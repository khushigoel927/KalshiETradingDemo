package com.kg.kalshietrade;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONObject;


public class GetSeries {
    public static void main(String[] args) {
        HttpResponse<String> response = Unirest.get("https://api.elections.kalshi.com/trade-api/v2/series/KXNCAAMBGAME")
                .asString();
        if (response.getStatus() != 200) {
            System.out.println("Error: " + response.getStatus());
            return;
        } else {
            System.out.println("Success");
        }
        System.out.println(response.getBody());
        JSONObject jresponse = new JSONObject(response.getBody());
    }
}
