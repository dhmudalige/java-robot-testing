package Robots.placements;

import Robots.samples.SampleRobot;
import swarm.configs.MQTTSettings;
import swarm.robot.Robot;
import swarm.robot.VirtualRobot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;
import java.util.Random;

public class VoronoiCoverageTest {

    public static final int GRID_LENGTH = 10;
    public static final double COVERAGE_RADIUS = 18.0;

    public void placeRobots(Robot[] robots, int n, int p, double r) {

        // Iteratively refine points
        boolean changed;
        do {
            changed = false;
            for (Robot robot : robots) {
                double newX = robot.coordinates.getX();
                double newY = robot.coordinates.getY();

                for (Robot otherRobot : robots) {
                    if (robot != otherRobot) {
                        double X = robot.coordinates.getX();
                        double otherX = otherRobot.coordinates.getX();

                        double Y = robot.coordinates.getY();
                        double otherY = otherRobot.coordinates.getY();

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
                // robot.x = newX;
                // robot.y = newY;
                robot.communicationInterrupt("<Voronoi-Test> Updated ROBOT" + robot.getId() + " position");
            }
        } while (changed);

    }

    public static void main(String[] args) {

        try {
            // COMPLETE THIS BEFORE RUN
            // Read config properties from the file, src/resources/config/mqtt.properties
            // If it isn't there, please make one, as given sample in the
            // 'sample_mqtt.properties' file

            File configFile = new File("src/resources/config/mqtt.properties");
            FileReader reader = new FileReader(configFile);
            Properties props = new Properties();
            props.load(reader);

            MQTTSettings.server = props.getProperty("server");
            MQTTSettings.port = Integer.parseInt(props.getProperty("port", "1883"));
            MQTTSettings.userName = props.getProperty("username");
            MQTTSettings.password = props.getProperty("password");
            MQTTSettings.channel = props.getProperty("channel", "v1");
            reader.close();

            // // Start a single robot
            // Robot robot = new MazeFollowingRobot(10, 9, 9, 90);
            // new Thread(robot).start();

            // Start a swarm of robots
            Random random = new Random();

            int[] robotIDList = {0, 1, 2, 3, 4};

            int startX = 0, startY = 0, startHeading = 90;

            Robot[] vr = new VirtualRobot[robotIDList.length];

            for (int i = 0; i < robotIDList.length; i++) {
                int delta = random.nextInt() * i;
                vr[i] = new SampleRobot(robotIDList[i], startX + delta, startY + delta + 10,
                        startHeading + delta * i);
                new Thread(vr[i]).start();
            }

        } catch (FileNotFoundException ex) {
            // file does not exist
            System.out.println("File Not Found !!!");

        } catch (IOException ex) {
            // I/O error
            System.out.println("IO Error !!!");
        }
    }

}
