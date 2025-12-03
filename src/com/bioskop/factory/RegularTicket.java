package com.bioskop.factory;

/**
 * RegularTicket - Concrete Product untuk Factory Method Pattern
 * Tiket reguler dengan harga normal (multiplier 1.0x)
 *
 * Design Pattern: Factory Method Pattern (Concrete Product)
 *
 * @author Nazriel (Member 1)
 * @version 1.0
 */
public class RegularTicket extends Ticket {

    private static final double PRICE_MULTIPLIER = 1.0;

    /**
     * Constructor
     *
     * @param seatNumber nomor kursi
     * @param basePrice harga dasar
     */
    public RegularTicket(String seatNumber, double basePrice) {
        super(seatNumber, basePrice);
    }

    /**
     * Menghitung harga tiket reguler
     * Formula: basePrice × 1.0 (harga normal)
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
     * @return "Regular"
     */
    @Override
    public String getTicketType() {
        return "Regular";
    }

    /**
     * Mendapatkan informasi detail tiket reguler
     *
     * @return formatted ticket information
     */
    @Override
    public String getTicketInfo() {
        return String.format("""
                ┌──────────────────────────────────┐
                │ REGULAR TICKET                   │
                ├──────────────────────────────────┤
                │ Seat Number  : %-17s │
                │ Base Price   : %-17s │
                │ Multiplier   : x%-16.1f │
                │ Final Price  : %-17s │
                ├──────────────────────────────────┤
                │ Benefits:                        │
                │ • Standard seating               │
                │ • Regular service                │
                └──────────────────────────────────┘
                """,
                seatNumber,
                formatPrice(basePrice),
                PRICE_MULTIPLIER,
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
}