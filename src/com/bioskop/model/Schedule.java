package com.bioskop.model;

import com.bioskop.strategy.*;
import com.bioskop.util.FileManager;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Schedule {

    private int scheduleId;
    private int movieId;
    private String studioName;
    private LocalDate showDate;
    private LocalTime showTime;
    private int totalSeats;
    private int availableSeats;

    private PricingStrategy pricingStrategy;

    private static final String FILE = "schedule.txt";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("HH:mm");

    // List hari libur hard code
    private static final Set<String> HOLIDAYS = Set.of("2025-01-01", "2025-12-25", "2025-05-01");

    public Schedule(int scheduleId, int movieId, String studioName,
                    LocalDate showDate, LocalTime showTime,
                    int totalSeats, int availableSeats) {

        this.scheduleId = scheduleId;
        this.movieId = movieId;
        this.studioName = studioName;
        this.showDate = showDate;
        this.showTime = showTime;
        this.totalSeats = totalSeats;
        this.availableSeats = availableSeats;
        // tidak menentukan strategy disini
    }

    // Penentuan strategy berdasarkan data dari file
    public void determinePricingStrategy() {
        if (HOLIDAYS.contains(showDate.format(DATE_FMT))) {
            pricingStrategy = new HolidayPricing();
            return;
        }
        switch (showDate.getDayOfWeek()) {
            case SATURDAY, SUNDAY -> pricingStrategy = new WeekendPricing();
            default -> pricingStrategy = new WeekdayPricing();
        }
    }

    public PricingStrategy getPricingStrategy() {
        return pricingStrategy;
    }

    public double calculatePrice(double basePrice) {
        if (pricingStrategy == null) determinePricingStrategy();
        return pricingStrategy.calculatePrice(basePrice);
    }

    // Load semua schedule dari file (tanpa logic hard-coded)
    public static List<Schedule> loadFromFile() {
        List<String> lines = FileManager.readFile(FILE);
        List<Schedule> list = new ArrayList<>();

        for (String line : lines) {
            if (line.isBlank()) continue;

            String[] p = line.split("\\|");
            int sid = Integer.parseInt(p[0]);
            int mid = Integer.parseInt(p[1]);
            String studio = p[2];
            LocalDate date = LocalDate.parse(p[3], DATE_FMT);
            LocalTime time = LocalTime.parse(p[4], TIME_FMT);
            int total = Integer.parseInt(p[5]);
            int available = Integer.parseInt(p[6]);

            Schedule s = new Schedule(sid, mid, studio, date, time, total, available);
            s.determinePricingStrategy();
            list.add(s);
        }
        return list;
    }

    public static List<Schedule> getAllSchedules() {
        return loadFromFile();
    }


    public static Schedule getScheduleById(int id) {
        return loadFromFile().stream()
                .filter(s -> s.scheduleId == id)
                .findFirst().orElse(null);
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public int getMovieId() {
        return movieId;
    }

    public String getInfo() {
        Movie movie = Movie.getMovieById(this.movieId);
        String movieName = (movie != null) ? movie.getTitle() : "Unknown Movie";

        return "Schedule " + scheduleId +
                " | " + movieName +
                " | Studio " + studioName +
                " | " + showDate + " " + showTime +
                " | Strategy: " + pricingStrategy.getStrategyName();
    }

    public double calculateFinalPrice() {
        com.bioskop.model.Movie movie = com.bioskop.model.Movie.getMovieById(this.movieId);

        if (movie == null) {
            System.err.println("ERROR: Movie ID " + movieId + " not found!");
            return 0;
        }

        double basePrice = movie.getBasePrice();  // ambil HARGA film dari movies.txt
        return calculatePrice(basePrice);         // dihitung pakai Strategy Pattern
    }


}