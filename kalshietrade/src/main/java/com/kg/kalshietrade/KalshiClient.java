package com.kg.kalshietrade;
import kong.unirest.*;
import org.json.JSONObject;

public class KalshiClient {
    private final KalshiSigner signer = new KalshiSigner(KalshiConfig.PEM_PATH);

    public JSONObject getEvents() {
        // Query markets endpoint to get live basketball games (markets, not events/series)
        String path = "/trade-api/v2/markets";
        return makeGetRequest(path, "series_ticker", KalshiConfig.SERIES_TICKER, "status", "open");
    }

    public JSONObject getMarkets() {
        String path = "/trade-api/v2/markets";
        return makeGetRequest(path, "series_ticker", KalshiConfig.SERIES_TICKER, "status", "open");
    }
    public JSONObject sendOrder(String path, String jsonBody) {
        long ts = System.currentTimeMillis();
        String sig = signer.sign(ts + "POST" + path + jsonBody);

        HttpResponse<String> resp = Unirest.post(KalshiConfig.BASE_URL + path)
                .header("Content-Type", "application/json")
                .header("KALSHI-ACCESS-KEY", KalshiConfig.API_KEY)
                .header("KALSHI-ACCESS-TIMESTAMP", String.valueOf(ts))
                .header("KALSHI-ACCESS-SIGNATURE", sig)
                .body(jsonBody).asString();

        if (resp.getStatus() >= 200 && resp.getStatus() < 300) {
            return new JSONObject(resp.getBody());
        } else {
            System.err.println("   [ERROR] " + resp.getStatus() + ": " + resp.getBody());
            return null;
        }
    }
    private JSONObject makeGetRequest(String path, String... params) {
        long ts = System.currentTimeMillis();
        String sig = signer.sign(ts + "GET" + path);
        GetRequest req = Unirest.get(KalshiConfig.BASE_URL + path);
        for (int i = 0; i < params.length; i += 2) {
            req.queryString(params[i], params[i+1]);
        }
        HttpResponse<String> resp = req.header("KALSHI-ACCESS-KEY", KalshiConfig.API_KEY)
                .header("KALSHI-ACCESS-TIMESTAMP", String.valueOf(ts))
                .header("KALSHI-ACCESS-SIGNATURE", sig)
                .asString();
        return resp.getStatus() == 200 ? new JSONObject(resp.getBody()) : new JSONObject();
    }
}