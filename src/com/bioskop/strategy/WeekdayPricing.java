package com.bioskop.strategy;

/**
 * Class: WeekdayPricing
 * Concrete strategy for weekday pricing.
 */
public class WeekdayPricing implements PricingStrategy {

    private static final double MULTIPLIER = 1.0;

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice * MULTIPLIER;
    }

    @Override
    public String getStrategyName() {
        return "Weekday";
    }

    @Override
    public double getMultiplier() {
        return MULTIPLIER;
    }
}
