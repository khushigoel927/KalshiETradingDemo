package com.kg.kalshietrade;
import org.json.*;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import java.util.*;

import static com.kg.kalshietrade.KalshiConfig.TIME_IN_MINUTES;

public class KalshiBot {
    private final KalshiClient client = new KalshiClient();
    private final long startTime = System.currentTimeMillis();
    public void run() {
        System.out.println("=== BOT STARTED ===");
        while (true) {
            if (System.currentTimeMillis() - startTime > TIME_IN_MINUTES * 60 * 1000) {
                System.out.println("Time elapsed. Shutting down.");
                System.exit(0);
            }
            try {
                JSONArray markets = client.getMarkets().optJSONArray("markets");
                if (markets != null) {
                    for (int i = 0; i < markets.length(); i++) {
                        JSONObject m = markets.getJSONObject(i);
                        int yesPrice = m.optInt("yes_ask", 101);
                        int noPrice = m.optInt("no_ask", 101);

                        if (yesPrice + noPrice < 95 && yesPrice > 0 && noPrice > 0) {
                            executeBatch(m.getString("ticker"), yesPrice, noPrice);
                        }
                    }
                }
                Thread.sleep(5000);
            } catch (Exception e) { e.printStackTrace(); }
        }
    }
    private void executeBatch(String ticker, int yP, int nP) {
        if ((yP + nP) >= 95) {
            System.out.println("   [SKIPPED] Price moved during calculation: " + (yP + nP));
            return;
        }

        JSONObject yLeg = createLeg(ticker, "yes", 10, yP);

        JSONObject nLeg = createLeg(ticker, "no", 10, nP);

        String body = new JSONObject()
                .put("orders", new JSONArray().put(yLeg).put(nLeg))
                .toString();

        client.sendOrder("/trade-api/v2/portfolio/orders/batched", body);
    }
    private JSONObject createLeg(String ticker, String side, int count, int price) {
        return new JSONObject()
                .put("ticker", ticker)
                .put("action", "buy")
                .put("side", side)
                .put("count", count)
                .put("type", "limit")
                .put(side + "_price", price)
                .put("time_in_force", "fok")
                .put("client_order_id", UUID.randomUUID().toString());
    }
    public static void main(String[] args) { new KalshiBot().run(); }
}
