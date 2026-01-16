package com.kg.kalshietrade;

import org.json.*;
import java.util.*;

public class NCAABettingBot {
    private final KalshiClient client = new KalshiClient();
    private final Map<String, Integer> exposure = new HashMap<>();
    private final long startTime = System.currentTimeMillis();

    public void run() {
        System.out.println("=== NCAA LIVE MONITOR STARTED ===");
        while (true) {
            if (System.currentTimeMillis() - startTime > 60 * 60 * 1000) {
                System.out.println("\n[SHUTDOWN] 60 minutes reached.");
                System.exit(0);
            }

            try {
                JSONArray markets = client.getEvents().optJSONArray("markets");
                System.out.println("\n--- Global Sync: " + new Date() + " ---");

                if (markets != null && markets.length() > 0) {
                    for (int i = 0; i < markets.length(); i++) {
                        handleGame(markets.getJSONObject(i));
                    }
                } else {
                    System.out.println("No active NCAA markets found.");
                }

                Thread.sleep(20000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void handleGame(JSONObject market) {
        // Extract fields from market object (not event/series)
        String ticker = market.optString("ticker", "unknown-ticker");
        String title = market.optString("title", "Unknown Title");
        String status = market.optString("status", "unknown");
        String category = market.optString("category", null);
        String eventTickerId = market.optString("event_ticker", null);

        // Print a concise, usable summary for the market
        System.out.println("\nMarket: " + title);
        System.out.println("  ticker: " + ticker + " | status: " + status);
        System.out.println("  event_ticker: " + (eventTickerId != null ? eventTickerId : "n/a"));
        System.out.println("  category: " + (category != null ? category : "n/a"));

        // Use ticker as the exposure key (stable identifier for this market)
        String exposureKey = ticker;
        int owned = exposure.getOrDefault(exposureKey, 0);

        // Trading guards: only attempt when we have an open status
        boolean isOpen = "open".equalsIgnoreCase(status);

        // For now, use a simple heuristic: buy a small position if open
        int target = isOpen ? 10 : 0;
        int buyCount = Math.min(Math.max(0, target - owned), KalshiConfig.MAX_CONTRACTS_PER_GAME - owned);

        if (isOpen && buyCount > 0) {
            System.out.println(" -> ACTION: Placing Order for " + buyCount + " contracts on market " + ticker);
            executeTrade(ticker, buyCount, exposureKey, owned);
        } else {
            System.out.println(" -> STATUS: No trade (isOpen=" + isOpen + ", target=" + target + ", owned=" + owned + ")");
        }
    }

    private void executeTrade(String ticker, int count, String eTicker, int currentOwned) {
        String body = new JSONObject()
                .put("ticker", ticker)
                .put("action", "buy")
                .put("side", "yes")
                .put("count", count)
                .put("type", "limit")
                .put("yes_price", KalshiConfig.MAX_YES_PRICE)
                .put("time_in_force", "fok")
                .put("client_order_id", UUID.randomUUID().toString())
                .toString();

        JSONObject response = client.sendOrder("/trade-api/v2/portfolio/orders", body);
        if (response != null) {
            JSONObject order = response.optJSONObject("order");
            if (order != null) {
                String status = order.optString("status");
                int fillCount = order.optInt("fill_count", 0);

                if (status.equals("filled")) {
                    System.out.println("   >>> SUCCESS: Filled " + fillCount + " contracts at " + ticker);
                    exposure.put(eTicker, currentOwned + fillCount);
                } else {
                    System.out.println("   >>> CANCELED: Order " + status + " (Price slipped or no liquidity)");
                }
            }
        }
    }


    public static void main(String[] args) {
        new NCAABettingBot().run();
    }
}