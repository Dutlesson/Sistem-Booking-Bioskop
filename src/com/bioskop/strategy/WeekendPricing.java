package com.bioskop.strategy;

/**
 * Class: WeekendPricing
 * Concrete strategy for weekend pricing.
 */
public class WeekendPricing implements PricingStrategy {

    private static final double MULTIPLIER = 1.4;

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice * MULTIPLIER;
    }

    @Override
    public String getStrategyName() {
        return "Weekend";
    }

    @Override
    public double getMultiplier() {
        return MULTIPLIER;
    }
}
