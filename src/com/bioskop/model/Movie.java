package com.bioskop.model;

import com.bioskop.util.FileManager;
import java.util.ArrayList;
import java.util.List;

/**
 * Movie model class
 */
public class Movie {

    private int movieId;
    private String title;
    private String genre;
    private int durationMinutes;
    private double rating;
    private double basePrice; // <-- TAMBAHAN BARU

    public Movie(int movieId, String title, String genre, int durationMinutes, double rating, double basePrice) {
        this.movieId = movieId;
        this.title = title;
        this.genre = genre;
        this.durationMinutes = durationMinutes;
        this.rating = rating;
        this.basePrice = basePrice;
    }

    public int getMovieId() { return movieId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public int getDurationMinutes() { return durationMinutes; }
    public double getRating() { return rating; }
    public double getBasePrice() { return basePrice; }

    public void setBasePrice(double basePrice) { this.basePrice = basePrice; }

    public String getMovieInfo() {
        return movieId + " | " + title + " | " + genre + " | " +
                durationMinutes + " mins | Rating " + rating +
                " | Base Price: " + basePrice;
    }

    // ================= FILE OPERATIONS ================= //

    public static List<Movie> loadFromFile() {
        List<Movie> movies = new ArrayList<>();
        List<String> lines = FileManager.readFile("movies.txt");

        for (String line : lines) {
            if (line.trim().isEmpty() || line.startsWith("/*")) continue;

            String[] p = line.split("\\|");
            if (p.length < 6) continue;

            movies.add(new Movie(
                    Integer.parseInt(p[0]),
                    p[1],
                    p[2],
                    Integer.parseInt(p[3]),
                    Double.parseDouble(p[4]),
                    Double.parseDouble(p[5])  // <-- basePrice
            ));
        }

        return movies;
    }

    public static List<Movie> getAllMovies() {
        return loadFromFile();
    }

    public static Movie getMovieById(int id) {
        for (Movie m : loadFromFile()) {
            if (m.getMovieId() == id) return m;
        }
        return null;
    }

    public static void addMovie(Movie movie) {
        String line = movie.movieId + "|" + movie.title + "|" + movie.genre + "|" +
                movie.durationMinutes + "|" + movie.rating + "|" + movie.basePrice;

        FileManager.appendFile("movies.txt", line);
    }

    public static void updateMovie(Movie movie) {
        List<String> lines = FileManager.readFile("movies.txt");

        for (int i = 0; i < lines.size(); i++) {
            String[] p = lines.get(i).split("\\|");

            if (p.length > 0 && p[0].equals(String.valueOf(movie.movieId))) {
                lines.set(i,
                        movie.movieId + "|" + movie.title + "|" + movie.genre + "|" +
                                movie.durationMinutes + "|" + movie.rating + "|" + movie.basePrice);
            }
        }

        FileManager.writeFile("movies.txt", lines);
    }

    public static void deleteMovie(int id) {
        FileManager.deleteLineContaining("movies.txt", id + "|");
    }
}
