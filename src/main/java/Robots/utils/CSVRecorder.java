package Robots.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import static Robots.utils.SwarmUtils.*;

public class CSVRecorder {
    // Write an empty row
    public static void addCSVHeader(String filePath, String robotType) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write("TIME:" + getTime() + ",ROBOT-TYPE:" + robotType + "\n");

            String[] columnNames = {"timestamp", "cycle", "total-time"};
            writer.write(String.join(",", columnNames) + "\n");
//            writer.write("\n");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void addHeader(String filePath) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
//            String[] columnNames = {"timestamp", "explored cells", "unexplored cells"};
//            writer.write(String.join(",", columnNames) + "\n");
//            writer.write("\n");
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static void recordExplorations(String filePath, String robotType, long timestamp, int count, long timeTaken){
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(timestamp + "," + count + "," + timeTaken + "\n");
            writer.write("\n");

            System.out.println(robotType + " cycle:" + count + ", total-time:" + timeTaken + " ms");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public static void recordExplorations(String filePath, long timestamp, int explored, int unexplored){
//        String[] columnNames = {"timestamp", "explored cells", "unexplored cells"};
//
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
//            writer.write(String.join(",", columnNames) + "\n");
//            writer.write(timestamp + "," + explored + "," + unexplored + "\n");
//            writer.write("\n");
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }

}
