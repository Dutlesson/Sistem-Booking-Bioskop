package com.bioskop.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * Utility class untuk file operations
 * Handles reading and writing TXT files
 *
 * @author Nazriel (Temporary by Fiandra)
 * @version 1.0
 */
public class FileManager {

    // Base directory untuk data files
    private static final String DATA_DIR = "data/";

    /**
     * Read all lines from a file
     * @param filename Nama file (e.g., "seats.txt")
     * @return List of lines, atau empty list jika error
     */
    public static List<String> readFile(String filename) {
        try {
            Path path = Paths.get(DATA_DIR + filename);

            // Cek apakah file ada
            if (!Files.exists(path)) {
                System.out.println("⚠️ File not found: " + filename);
                return new ArrayList<>();
            }

            return Files.readAllLines(path);

        } catch (IOException e) {
            System.out.println("❌ Error reading file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Write lines to a file (overwrite)
     * @param filename Nama file
     * @param lines List of lines to write
     */
    public static void writeFile(String filename, List<String> lines) {
        try {
            Path path = Paths.get(DATA_DIR + filename);

            // Create parent directory jika belum ada
            Files.createDirectories(path.getParent());

            // Write to file
            Files.write(path, lines, StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            System.out.println("✓ File saved: " + filename);

        } catch (IOException e) {
            System.out.println("❌ Error writing file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Append a line to file
     * @param filename Nama file
     * @param line Line to append
     */
    public static void appendFile(String filename, String line) {
        try {
            Path path = Paths.get(DATA_DIR + filename);

            // Create file jika belum ada
            if (!Files.exists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }

            // Append line
            Files.write(path, (line + "\n").getBytes(),
                    StandardOpenOption.APPEND);

        } catch (IOException e) {
            System.out.println("❌ Error appending to file: " + e.getMessage());
        }
    }

    /**
     * Check if file exists
     */
    public static boolean fileExists(String filename) {
        return Files.exists(Paths.get(DATA_DIR + filename));
    }

    // Test method
    public static void main(String[] args) {
        System.out.println("=== Testing FileManager ===\n");

        // Test write
        List<String> testData = Arrays.asList(
                "Line 1: Test",
                "Line 2: Test",
                "Line 3: Test"
        );

        writeFile("test.txt", testData);

        // Test read
        List<String> readData = readFile("test.txt");
        System.out.println("\nRead " + readData.size() + " lines:");
        for (String line : readData) {
            System.out.println("  " + line);
        }

        // Test append
        appendFile("test.txt", "Line 4: Appended");

        System.out.println("\n✓ FileManager working!");
    }
}