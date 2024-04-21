package Robots.placements;

import Robots.algorithms.VoronoiCoverage;
import Robots.models.RobotPosition;
import Robots.samples.SampleRobot;
import swarm.configs.MQTTSettings;
import swarm.robot.Robot;
import swarm.robot.VirtualRobot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.Random;

public class VoronoiCoverageTest {
    public static final double GRID_SPACE = 18.0;
    public static final int GRID_LENGTH = 10;
    public static final double MAX_LENGTH = (GRID_SPACE * GRID_LENGTH) / 8;

    public static final int ROBOTS_COUNT = 5;
    public static final double COVERAGE_RADIUS = 54.0;

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

            // Start a robot swarm
            VoronoiCoverage vc = new VoronoiCoverage();
            List<RobotPosition> rPositions = vc.placeRobots(GRID_LENGTH, ROBOTS_COUNT, COVERAGE_RADIUS);

            int startX = 0, startY = 0, startHeading = 90;

            Robot[] vr = new VirtualRobot[ROBOTS_COUNT];
            Random delta = new Random();
//            Random random = new Random();

            for (int i = 0; i < ROBOTS_COUNT; i++) {
                RobotPosition p = rPositions.get(i);

                double newX = Math.min((startX + p.x), MAX_LENGTH);
                double newY = Math.min((startY - p.y), MAX_LENGTH);

                vr[i] = new SampleRobot(p.robotId, newX, newY,startHeading + delta.nextInt(4) * 90);
//                vr[i].delay(1000);
                System.out.println("Robot "+ p.robotId + ": x=" + newX + ", y=" + newY);

                new Thread(vr[i]).start();
            }

            /*for (int i = 0; i < ROBOTS_COUNT; i++) {
                int delta = random.nextInt() % 18;
                int sign = (random.nextInt() % 2 == 0) ? 1 : -1;

                vr[i] = new SampleRobot(i, startX + Math.pow(sign * delta, 3) % 81, startY - sign * delta * 2,
                        startHeading + delta);
                new Thread(vr[i]).start();
            }*/

        } catch (FileNotFoundException ex) {
            // file does not exist
            System.out.println("File Not Found !!!");

        } catch (IOException ex) {
            // I/O error
            System.out.println("IO Error !!!");
        }
    }

}