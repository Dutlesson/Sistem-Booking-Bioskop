package com.bioskop.strategy;

/**
 * Interface: PricingStrategy
 *
 * Strategy interface for calculating ticket prices.
 * Implementations should provide their own multiplier logic.
 *
 * Methods:
 *  - calculatePrice(double basePrice): double
 *  - getStrategyName(): String
 *  - getMultiplier(): double
 *
 * This is an interface file.
 */
public interface PricingStrategy {
    /**
     * Calculate final price from a base price using the strategy's multiplier.
     *
     * @param basePrice the base price to apply multiplier to
     * @return the calculated final price
     */
    double calculatePrice(double basePrice);

    /**
     * Get human-readable strategy name.
     *
     * @return strategy name (e.g., "Weekday", "Weekend", "Holiday")
     */
    String getStrategyName();

    /**
     * Get numeric multiplier used by this strategy.
     *
     * @return multiplier (e.g., 1.0, 1.4, 1.8)
     */
    double getMultiplier();
}
