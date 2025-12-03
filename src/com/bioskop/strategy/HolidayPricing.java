package com.bioskop.strategy;

/**
 * Class: HolidayPricing
 * Concrete strategy for holiday pricing.
 */
public class HolidayPricing implements PricingStrategy {

    private static final double MULTIPLIER = 1.8;

    @Override
    public double calculatePrice(double basePrice) {
        return basePrice * MULTIPLIER;
    }

    @Override
    public String getStrategyName() {
        return "Holiday";
    }

    @Override
    public double getMultiplier() {
        return MULTIPLIER;
    }
}
