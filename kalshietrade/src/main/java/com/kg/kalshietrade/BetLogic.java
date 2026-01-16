package com.kg.kalshietrade;

public class BetLogic {
    public static int getTargetContracts(int minutesLeft, boolean isSecondHalf, int lead) {
        if (!isSecondHalf || minutesLeft > 10) {
            return 0;
        }
        if (minutesLeft <= 10 && minutesLeft > 5) {
            if (lead >= 16) return 100;
            if (lead >= 13) return 50;
        }
        else if (minutesLeft <= 5 && minutesLeft > 3) {
            if (lead >= 11) return 100;
            if (lead >= 8) return 50;
        }
        else if (minutesLeft <= 3 && minutesLeft > 1) {
            if (lead >= 9) return 100;
            if (lead >= 6) return 50;
        }
        else if (minutesLeft <= 1) {
            if (lead >= 7) return 100;
            if (lead >= 5) return 50;
        }

        return 0;
    }
}
