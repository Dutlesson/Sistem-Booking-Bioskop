package com.bioskop.util;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/**
 * FileManager - Utility class untuk mengelola operasi file TXT
 * Menyediakan method untuk read, write, append, delete, dan update file
 *
 * @author Nazriel (Member 1)
 * @version 1.0
 */
public class FileManager {

    private static final String DATA_FOLDER = "data/";

    /**
     * Membaca seluruh isi file dan mengembalikan sebagai List<String>
     *
     * @param filename nama file yang akan dibaca
     * @return List berisi setiap baris dari file
     */
    public static List<String> readFile(String filename) {
        List<String> lines = new ArrayList<>();
        String filepath = DATA_FOLDER + filename;

        try {
            // Cek apakah file exists, jika tidak buat file kosong
            File file = new File(filepath);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
                return lines;
            }

            lines = Files.readAllLines(Paths.get(filepath));
        } catch (IOException e) {
            System.err.println("Error reading file " + filename + ": " + e.getMessage());
        }

        return lines;
    }

    /**
     * Menulis List<String> ke file (overwrite)
     *
     * @param filename nama file tujuan
     * @param content List berisi baris-baris yang akan ditulis
     */
    public static void writeFile(String filename, List<String> content) {
        String filepath = DATA_FOLDER + filename;

        try {
            File file = new File(filepath);
            file.getParentFile().mkdirs();

            Files.write(Paths.get(filepath), content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.err.println("Error writing file " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Menulis file dan mengembalikan status berhasil / gagal.
     */
    public static boolean writeLines(String filename, List<String> content) {
        String filepath = DATA_FOLDER + filename;

        try {
            File file = new File(filepath);
            file.getParentFile().mkdirs();

            Files.write(Paths.get(filepath), content,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            return true;

        } catch (IOException e) {
            System.err.println("Error writing file " + filename + ": " + e.getMessage());
            return false;
        }
    }



    /**
     * Menambahkan satu baris ke akhir file
     *
     * @param filename nama file tujuan
     * @param line baris yang akan ditambahkan
     */
    public static void appendFile(String filename, String line) {
        String filepath = DATA_FOLDER + filename;

        try {
            File file = new File(filepath);
            file.getParentFile().mkdirs();

            if (!file.exists()) {
                file.createNewFile();
            }

            Files.write(Paths.get(filepath),
                    (line + System.lineSeparator()).getBytes(),
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.err.println("Error appending to file " + filename + ": " + e.getMessage());
        }
    }

    /**
     * Menghapus baris pada index tertentu (0-based index)
     *
     * @param filename nama file
     * @param index index baris yang akan dihapus
     */
    public static void deleteLine(String filename, int index) {
        List<String> lines = readFile(filename);

        if (index >= 0 && index < lines.size()) {
            lines.remove(index);
            writeFile(filename, lines);
        }
    }

    /**
     * Mengupdate baris pada index tertentu dengan baris baru
     *
     * @param filename nama file
     * @param index index baris yang akan diupdate
     * @param newLine baris baru pengganti
     */
    public static void updateLine(String filename, int index, String newLine) {
        List<String> lines = readFile(filename);

        if (index >= 0 && index < lines.size()) {
            lines.set(index, newLine);
            writeFile(filename, lines);
        }
    }

    /**
     * Menghapus baris yang mengandung string tertentu
     *
     * @param filename nama file
     * @param searchString string yang dicari untuk dihapus
     * @return true jika berhasil menghapus, false jika tidak ditemukan
     */
    public static boolean deleteLineContaining(String filename, String searchString) {
        List<String> lines = readFile(filename);
        boolean removed = lines.removeIf(line -> line.contains(searchString));

        if (removed) {
            writeFile(filename, lines);
        }

        return removed;
    }

    /**
     * Mencari baris yang dimulai dengan string tertentu
     *
     * @param filename nama file
     * @param startsWith string awal yang dicari
     * @return baris yang ditemukan, atau null jika tidak ada
     */
    public static String findLineStartsWith(String filename, String startsWith) {
        List<String> lines = readFile(filename);

        for (String line : lines) {
            if (line.startsWith(startsWith)) {
                return line;
            }
        }

        return null;
    }

    /**
     * Mendapatkan ID terbesar dari file untuk auto-increment
     * Asumsi: ID ada di kolom pertama dengan format: id|...
     *
     * @param filename nama file
     * @return ID terbesar + 1
     */
    public static int getNextId(String filename) {
        List<String> lines = readFile(filename);
        int maxId = 0;

        for (String line : lines) {
            if (line.trim().isEmpty()) continue;

            String[] parts = line.split("\\|");
            try {
                int id = Integer.parseInt(parts[0]);
                if (id > maxId) {
                    maxId = id;
                }
            } catch (NumberFormatException e) {
                // Skip header atau baris invalid
                continue;
            }
        }

        return maxId + 1;
    }

    public static Set<String> readHolidayList() {
        List<String> lines = readFile("holidays.txt");
        Set<String> set = new HashSet<>();
        for (String line : lines) {
            String d = line.trim();
            if (!d.isEmpty()) set.add(d);
        }
        return set;
    }

    /**
     * Append a line to a file
     * @param fileName File name (will be saved in data folder)
     * @param content Content to append
     * @return true if successful
     */
    public static boolean appendToFile(String fileName, String content) {
        try {
            ensureDataFolderExists();

            File file = new File(DATA_FOLDER + fileName);

            // Create file if doesn't exist
            if (!file.exists()) {
                file.createNewFile();
            }

            // Append content with newline
            java.nio.file.Files.write(
                    file.toPath(),
                    (content + System.lineSeparator()).getBytes(),
                    java.nio.file.StandardOpenOption.APPEND
            );

            System.out.println("✓ Data appended to: " + file.getPath());
            return true;

        } catch (Exception e) {
            System.err.println("✗ Error appending to file: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Cek apakah file exists
     *
     * @param filename nama file
     * @return true jika file ada
     */
    public static boolean fileExists(String filename) {
        return new File(DATA_FOLDER + filename).exists();
    }

    /**
     * Membuat folder data jika belum ada
     */
    public static void ensureDataFolderExists() {
        File dataDir = new File(DATA_FOLDER);
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }
}

