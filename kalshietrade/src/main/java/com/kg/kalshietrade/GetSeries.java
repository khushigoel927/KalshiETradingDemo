package com.kg.kalshietrade;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.json.JSONObject;
import org.json.JSONArray;


public class GetSeries {
    public static void main(String[] args) {
        HttpResponse<String> response = Unirest.get("https://demo-api.kalshi.co/trade-api/v2/series/KXNCAAMBGAME")
                .asString();
        if (response.getStatus() != 200) {
            System.out.println("Error: " + response.getStatus());
            return;
        } else {
            System.out.println("Success");
        }
        System.out.println(response.getBody());
        JSONObject jresponse = new JSONObject(response.getBody());

        // After fetching series, run the debug helper to find the UCF @ Kansas State market
        fetchUcfKansasState();
    }

    // Debug helper: queries markets endpoint and prints the JSON for the UCF at Kansas State game (if found)
    private static void fetchUcfKansasState() {
        try {
            KalshiClient client = new KalshiClient();
            JSONObject marketsResp = client.getEvents(); // This now queries /markets

            JSONArray markets = marketsResp.optJSONArray("markets");
            if (markets == null || markets.length() == 0) {
                System.out.println("[DEBUG] No markets returned.");
                return;
            }

            for (int i = 0; i < markets.length(); i++) {
                JSONObject market = markets.getJSONObject(i);
                String title = market.optString("title", "").toLowerCase();
                // match titles like "UCF at Kansas State", "UCF at Kansas St.", etc.
                if (title.contains("ucf") && title.contains("kansas")) {
                    System.out.println("\n[DEBUG] Found UCF at Kansas State market:");
                    System.out.println(market.toString(2)); // pretty-print JSON
                    return;
                }
            }

            System.out.println("[DEBUG] UCF at Kansas State market not found in current open markets.");
        } catch (Exception ex) {
            System.err.println("[DEBUG] Error while searching markets: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
