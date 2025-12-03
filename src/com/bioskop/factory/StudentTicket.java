package com.bioskop.factory;

/**
 * StudentTicket - Concrete Product untuk Factory Method Pattern
 * Tiket student dengan diskon (multiplier 0.75x)
 *
 * Design Pattern: Factory Method Pattern (Concrete Product)
 *
 * @author Nazriel (Member 1)
 * @version 1.0
 */
public class StudentTicket extends Ticket {

    private static final double PRICE_MULTIPLIER = 0.75;

    /**
     * Constructor
     *
     * @param seatNumber nomor kursi
     * @param basePrice harga dasar
     */
    public StudentTicket(String seatNumber, double basePrice) {
        super(seatNumber, basePrice);
    }

    /**
     * Menghitung harga tiket student
     * Formula: basePrice × 0.75 (diskon 25%)
     *
     * @return harga final
     */
    @Override
    public double calculatePrice() {
        return basePrice * PRICE_MULTIPLIER;
    }

    /**
     * Mendapatkan tipe tiket
     *
     * @return "Student"
     */
    @Override
    public String getTicketType() {
        return "Student";
    }

    /**
     * Mendapatkan informasi detail tiket student
     *
     * @return formatted ticket information
     */
    @Override
    public String getTicketInfo() {
        double discount = basePrice - calculatePrice();

        return String.format("""
                ┌──────────────────────────────────┐
                │ STUDENT TICKET 🎓                │
                ├──────────────────────────────────┤
                │ Seat Number  : %-17s │
                │ Base Price   : %-17s │
                │ Multiplier   : x%-16.2f │
                │ Discount     : %-17s │
                │ Final Price  : %-17s │
                ├──────────────────────────────────┤
                │ Benefits:                        │
                │ • 25%% student discount           │
                │ • Standard seating               │
                │ • Valid student ID required      │
                └──────────────────────────────────┘
                """,
                seatNumber,
                formatPrice(basePrice),
                PRICE_MULTIPLIER,
                formatPrice(discount),
                formatPrice(calculatePrice()));
    }

    /**
     * Get multiplier untuk keperluan display/reporting
     *
     * @return price multiplier
     */
    public double getMultiplier() {
        return PRICE_MULTIPLIER;
    }

    /**
     * Get discount amount
     *
     * @return jumlah diskon
     */
    public double getDiscountAmount() {
        return basePrice - calculatePrice();
    }

    /**
     * Get discount percentage
     *
     * @return persentase diskon
     */
    public double getDiscountPercentage() {
        return (1.0 - PRICE_MULTIPLIER) * 100;
    }
}