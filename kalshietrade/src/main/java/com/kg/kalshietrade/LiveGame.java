package com.kg.kalshietrade;

public class LiveGame {
    public final String ticker;
    public final int minutesRemaining;
    public final int pointDiff;

    public LiveGame(String ticker, int minutesRemaining, int pointDiff) {
        this.ticker = ticker;
        this.minutesRemaining = minutesRemaining;
        this.pointDiff = pointDiff;
    }
}
