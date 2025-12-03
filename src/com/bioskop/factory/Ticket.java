package com.bioskop.factory;

/**
 * Ticket - Abstract Product class untuk Factory Method Pattern
 * Base class untuk semua tipe tiket (Regular, VIP, Student)
 *
 * Design Pattern: Factory Method Pattern (Product)
 *
 * @author Nazriel (Member 1)
 * @version 1.0
 */
public abstract class Ticket {

    protected String seatNumber;
    protected double basePrice;

    /**
     * Constructor
     *
     * @param seatNumber nomor kursi (ex: A1, B5, C10)
     * @param basePrice harga dasar sebelum perhitungan tipe tiket
     */
    public Ticket(String seatNumber, double basePrice) {
        this.seatNumber = seatNumber;
        this.basePrice = basePrice;
    }

    // Getters
    public String getSeatNumber() {
        return seatNumber;
    }

    public double getBasePrice() {
        return basePrice;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    /**
     * Abstract method untuk menghitung harga final tiket
     * Setiap concrete class harus implement method ini
     *
     * @return harga final setelah perhitungan
     */
    public abstract double calculatePrice();

    /**
     * Abstract method untuk mendapatkan informasi tiket
     *
     * @return String informasi detail tiket
     */
    public abstract String getTicketInfo();

    /**
     * Abstract method untuk mendapatkan tipe tiket
     *
     * @return tipe tiket (Regular/VIP/Student)
     */
    public abstract String getTicketType();

    /**
     * Get formatted price untuk display
     *
     * @param price harga yang akan diformat
     * @return formatted price (ex: Rp 50.000)
     */
    protected String formatPrice(double price) {
        return String.format("Rp %,.0f", price);
    }

    /**
     * Basic toString implementation
     */
    @Override
    public String toString() {
        return String.format("%s - Seat: %s - %s",
                getTicketType(),
                seatNumber,
                formatPrice(calculatePrice()));
    }
}