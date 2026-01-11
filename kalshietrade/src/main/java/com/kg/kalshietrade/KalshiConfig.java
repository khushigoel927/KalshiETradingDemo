package com.kg.kalshietrade;

public class KalshiConfig {
    public static final String BASE_URL = "https://demo-api.kalshi.com";
    public static final String API_KEY = "d343e12c-5c68-4594-b2b1-660063208df1";
    public static final String PRIVATE_KEY_PATH = "/Users/khushigoel/Downloads/KhushiGoelAPI.pem";


    public static final String SERIES_TICKER = "KXNCAAMBGAME";

    public static int MAX_CONTRACTS_PER_GAME = 100;
    public static int MAX_ACTIVE_TEAMS = 4;

    public static final int RUN_TIME_MINUTES = 60;
    public static final int POLL_INTERVAL_SECONDS = 30;
    public static final KalshiSigner SIGNER = new KalshiSigner(PRIVATE_KEY_PATH);
}
