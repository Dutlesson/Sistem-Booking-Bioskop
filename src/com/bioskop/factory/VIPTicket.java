package com.bioskop.factory;

/**
 * VIPTicket - Concrete Product untuk Factory Method Pattern
 * Tiket VIP dengan harga premium (multiplier 2.0x)
 * <p>
 * Design Pattern: Factory Method Pattern (Concrete Product)
 *
 * @author Nazriel (Member 1)
 * @version 1.0
 */
public class VIPTicket extends Ticket {

    private static final double PRICE_MULTIPLIER = 2.0;

    /**
     * Constructor
     *
     * @param seatNumber nomor kursi
     * @param basePrice  harga dasar
     */
    public VIPTicket(String seatNumber, double basePrice) {
        super(seatNumber, basePrice);
    }

    /**
     * Menghitung harga tiket VIP
     * Formula: basePrice × 2.0 (markup 100%)
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
     * @return "VIP"
     */
    @Override
    public String getTicketType() {
        return "VIP";
    }

    /**
     * Mendapatkan informasi detail tiket VIP
     *
     * @return formatted ticket information
     */
    @Override
    public String getTicketInfo() {
        return String.format("""
                        ┌──────────────────────────────────┐
                        │ VIP TICKET ⭐                    │
                        ├──────────────────────────────────┤
                        │ Seat Number  : %-17s │
                        │ Base Price   : %-17s │
                        │ Multiplier   : x%-16.1f │
                        │ Final Price  : %-17s │
                        ├──────────────────────────────────┤
                        │ Benefits:                        │
                        │ • Premium reclining seats        │
                        │ • Extra legroom                  │
                        │ • Complimentary blanket          │
                        │ • Priority entrance              │
                        │ • Premium sound quality          │
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