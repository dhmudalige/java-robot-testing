package Robots.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class CsvRecorder {

    public static void writeRecordToCSV(String filePath, double[] values) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            // Join measurements with commas and write them as a row in the CSV file
            String row = joinArrayWithCommas(values);
            writer.write(row + "\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String joinArrayWithCommas(double[] array) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            result.append(array[i]);
            if (i < array.length - 1) {
                result.append(",");
            }
        }
        return result.toString();
    }

    // Write an empty row
    public static void addEmptyRowToCSV(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("\n");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void recordExplorations(String filePath, long timestamp, int explored, int unexplored){

        String[] columnNames = {"timestamp", "explored cells", "unexplored cells"};

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(String.join(",", columnNames) + "\n");
            writer.write(timestamp + "," + explored + "," + unexplored + "\n");
            writer.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
