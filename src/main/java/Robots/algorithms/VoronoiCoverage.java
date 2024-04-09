package Robots.algorithms;

import Robots.models.RobotPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VoronoiCoverage {
    /*
     * n - Size of the grid
     * p - Number of robots
     * r - Radius of robots
     */
    List<RobotPosition> rPositionList = new ArrayList<>();
    Random random = new Random();

    public List<RobotPosition> placeRobots(int n, int p, double r) {
        // Generate initial random points
        for (int i = 0; i < p; i++) {
            /*
            double delta = random.nextInt() % 18;
            int sign = (random.nextInt() % 2 == 0) ? 1 : -1;
            rPositionList.add(new RobotPosition(i, Math.pow(sign * delta, 3) % 81, sign * delta * 2));
            */

            double x = random.nextDouble() * n;
            double y = random.nextDouble() * n;
            rPositionList.add(new RobotPosition(i, x, y));

//            System.out.println("R" + i + "=(" + x + "," + y + ")");
        }

        // Iteratively refine points
        boolean changed;
        do {
//            changed = false;
            changed = true;
            for (RobotPosition robot : rPositionList) {
                double newX = robot.x;
                double newY = robot.y;

                for (RobotPosition otherRobot : rPositionList) {
                    if (robot != otherRobot) {
                        double X = robot.x;
                        double otherX = otherRobot.x;

                        double Y = robot.y;
                        double otherY = otherRobot.y;

                        double distance = Math.sqrt(Math.pow(X - otherX, 2) + Math.pow(Y - otherY, 2));
                        if (distance < 2 * r) {
                            // Move the robot away from the other robot
                            double moveX = (X - otherX) * (2 * r - distance) / distance;
                            double moveY = (Y - otherY) * (2 * r - distance) / distance;
                            newX += moveX;
                            newY += moveY;
                            System.out.println("(" + newX + "," + newY + ")");
//                            changed = true;
                            changed = false;
                        }
                    }
                }
                // Update robot position
                robot.x = Math.round(newX) % n;
                robot.y = Math.round(newY) % n;
            }
        } while (changed);

        return rPositionList;
    }
}