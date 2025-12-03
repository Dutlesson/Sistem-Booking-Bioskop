package com.bioskop.observer;

/**
 * Observer interface untuk monitoring perubahan status seat
 * Implements Observer Pattern - Observer role
 *
 * Design Pattern: Observer Pattern
 * Role: Observer Interface
 *
 * @author Fiandra
 * @version 1.0
 */
public interface SeatObserver {

    /**
     * Method yang dipanggil ketika seat status berubah
     * Method ini akan dipanggil oleh Subject (Seat) saat terjadi perubahan
     *
     * @param seatNumber Nomor kursi yang berubah (e.g., "A1", "B3")
     * @param isBooked Status booking (true = booked, false = available)
     * @param scheduleId ID schedule yang terkait dengan seat
     */
    void update(String seatNumber, boolean isBooked, int scheduleId);

    /**
     * Mendapatkan nama observer untuk identification dan logging
     *
     * @return Nama observer (e.g., "BookingObserver-User123")
     */
    String getObserverName();
}