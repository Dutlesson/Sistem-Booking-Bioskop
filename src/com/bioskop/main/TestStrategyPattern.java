package com.bioskop.main;

import com.bioskop.model.Movie;
import com.bioskop.model.Schedule;

import java.util.List;
import java.util.Scanner;

public class TestStrategyPattern {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("========================================");
        System.out.println("        STRATEGY PATTERN DEMO CLI       ");
        System.out.println("========================================\n");

        // === STEP 1: TAMPILKAN LIST FILM ===
        List<Movie> movies = Movie.getAllMovies();

        if (movies.isEmpty()) {
            System.out.println("Tidak ditemukan film di movies.txt");
            return;
        }

        System.out.println("=== LIST FILM ===");
        for (Movie m : movies) {
            System.out.println(m.getMovieId() + ". " + m.getTitle() +
                    " (Base Price: " + m.getBasePrice() + ")");
        }

        System.out.print("\nPilih Film (masukkan Movie ID): ");
        int pickedMovieId = sc.nextInt();

        Movie selectedMovie = Movie.getMovieById(pickedMovieId);

        if (selectedMovie == null) {
            System.out.println("Movie tidak ditemukan!");
            return;
        }

        System.out.println("\nAnda memilih: " + selectedMovie.getTitle());
        System.out.println("----------------------------------------");


        // === STEP 2: TAMPILKAN SCHEDULE FILM TERSEBUT ===
        List<Schedule> schedules = Schedule.getAllSchedules();

        System.out.println("\n=== JADWAL UNTUK: " + selectedMovie.getTitle() + " ===");

        int nomor = 1;
        boolean adaSchedule = false;

        for (Schedule s : schedules) {
            if (s.getMovieId() == selectedMovie.getMovieId()) {
                adaSchedule = true;
                System.out.println(nomor + ". " + s.getInfo());
                nomor++;
            }
        }

        if (!adaSchedule) {
            System.out.println("Tidak ada jadwal untuk film ini.");
            return;
        }

        System.out.print("\nPilih Jadwal (nomor): ");
        int schedulePick = sc.nextInt();

        if (schedulePick < 1 || schedulePick >= nomor) {
            System.out.println("Pilihan jadwal tidak valid!");
            return;
        }

        // Mencari schedule berdasarkan urutan tampilan
        nomor = 1;
        Schedule selectedSchedule = null;

        for (Schedule s : schedules) {
            if (s.getMovieId() == selectedMovie.getMovieId()) {
                if (nomor == schedulePick) {
                    selectedSchedule = s;
                    break;
                }
                nomor++;
            }
        }

        if (selectedSchedule == null) {
            System.out.println("Gagal memilih schedule!");
            return;
        }

        // === STEP 3: HITUNG HARGA AKHIR (Strategy Pattern) ===
        double finalPrice = selectedSchedule.calculateFinalPrice();

        System.out.println("\n========================================");
        System.out.println("              HASIL PERHITUNGAN         ");
        System.out.println("========================================");
        System.out.println("Film      : " + selectedMovie.getTitle());
        System.out.println("Base Price: " + selectedMovie.getBasePrice());
        System.out.println("Strategy  : " + (selectedSchedule.getPricingStrategy() != null
                ? selectedSchedule.getPricingStrategy().getStrategyName() : "N/A"));
        System.out.println("Multiplier: " + (selectedSchedule.getPricingStrategy() != null
                ? selectedSchedule.getPricingStrategy().getMultiplier() : 1.0));
        System.out.println("----------------------------------------");
        System.out.println("Final Price (after Strategy): " + finalPrice);
        System.out.println("========================================\n");

    }
}
