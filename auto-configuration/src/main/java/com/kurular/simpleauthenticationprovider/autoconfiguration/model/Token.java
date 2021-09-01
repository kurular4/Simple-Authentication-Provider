package com.kurular.simpleauthenticationprovider.autoconfiguration.model;

import java.time.Duration;

public class Token {
    private String secretKey;

    /**
     * PT1.5S       = 1.5 Seconds
     * PT60S        = 60 Seconds
     * PT3M         = 3 Minutes
     * PT2H         = 2 Hours
     * P3DT5H40M30S = 3Days, 5Hours, 40 Minutes and 30 Seconds
     */
    private Duration duration;
}
