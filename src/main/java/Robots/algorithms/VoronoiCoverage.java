package Robots.algorithms;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class VoronoiCoverage {
    public static class RobotPosition {
        int robotId;
        double x, y;

        RobotPosition(int robotId, double x, double y) {
            this.robotId = robotId;
            this.x = x;
            this.y = y;
        }
    }

    /*
     * n - Size of the grid
     * p - Number of robots
     * r - Radius of robots
     */
    public static List<RobotPosition> placeRobots(int n, int p, double r) {
        List<RobotPosition> rPositionList = new ArrayList<>();

        Random random = new Random();

        // Generate initial random points
        for (int i = 0; i < p; i++) {
            /*double delta = random.nextInt() % 18;
            int sign = (random.nextInt() % 2 == 0) ? 1 : -1;
            rPositionList.add(new RobotPosition(i, Math.pow(sign * delta, 3) % 81, sign * delta * 2));*/
            double x = random.nextDouble() * n;
            double y = random.nextDouble() * n;
            rPositionList.add(new RobotPosition(i, x, y));
        }

        // Iteratively refine points
        boolean changed;
        do {
            changed = false;
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
                            changed = true;
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