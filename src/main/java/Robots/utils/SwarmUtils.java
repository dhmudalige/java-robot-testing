package Robots.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SwarmUtils {
    public static Date date = new Date();
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    // Format the current date
    public static String formattedDate = dateFormat.format(date);

    public static void printDate(String filePath){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true));
            String dateString = "<SwarmUtils> " + formattedDate;
            writer.write(dateString + "\n");
            System.out.println(dateString + " \nStarting PeraSwarm....");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
