package Robots.utils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class SwarmUtils {
    public static Date date = new Date();

    public static void printDate(String filePath){
        System.out.println("<SwarmUtils> #" + date + "# \nStarting Swarm....");
    }
}
