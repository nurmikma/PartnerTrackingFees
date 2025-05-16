package com.evocon.partnertracking.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Calculations {

    private static final int SCALE = 10;

    public static BigDecimal divide(BigDecimal dividend, int divisor) {
        return dividend.divide(BigDecimal.valueOf(divisor), SCALE, RoundingMode.HALF_UP);
    }

    public static BigDecimal multiplyByPercentage(BigDecimal amount, BigDecimal percentage) {
        return amount.multiply(percentage.divide(BigDecimal.valueOf(100)));
    }

    public static BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    public static BigDecimal roundToTwoDecimals(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateTotalLicenseAmount(int quantity, BigDecimal pricePerLicense) {
        if (pricePerLicense == null || quantity < 0) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(quantity).multiply(pricePerLicense).setScale(2, RoundingMode.HALF_UP);
    }
}
