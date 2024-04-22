
import swarm.configs.MQTTSettings;
import swarm.robot.Robot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import Robots.samples.MappingRobot;
import Robots.samples.MappingRobot3;
import Robots.samples.MappingRobotAlg2;
import Robots.samples.MappingRobotAlg3;
import Robots.samples.MappingRobotAlg4;
import Robots.samples.MappingRobotAlg5;
import Robots.samples.MappingRobotAlg6;
import Robots.samples.MappingRobotFrontierBased1;
import Robots.samples.MappingRobotRandomMoving1;
import Robots.samples.MappingRobotRandomMoving2;
import Robots.samples.MappingRobotRandomMoving3;
import Robots.samples.MappingRobotRandomMoving4;
import Robots.samples.MazeFollowingRobot;
import Robots.samples.ObstacleAvoidRobot;
import Robots.samples.SampleRobot;

import java.util.Random;

public class App {

    private static boolean isObstacle(int x, int y, int[][] obstacles) {
        for (int[] pair : obstacles) {
            if (pair[0] == x && pair[1] == y) {
                return true;
            }
        }
        return false;
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

            // Start a single robot
            Robot robot = new MappingRobotFrontierBased1(10, 27, 27, 90);
            new Thread(robot).start();

            Robot robot1 = new MappingRobotFrontierBased1(11, -27, -27, 90);
            new Thread(robot1).start();

            // // Start a single robot
            // Robot robot = new MazeFollowingRobot(10, 27, 27, 90);
            // new Thread(robot).start();

            // Robot robot = new MappingRobotAlg6(10, -81, -81, 90);
            // new Thread(robot).start();

            // Robot robot1 = new MappingRobotAlg6(9, 63, 63, 90);
            // new Thread(robot1).start();

            //--------------Mapping with unknown initial positions and heading dirctions with 2 robots

            // int[] headingDirections = {-90, 0, 90, 180};            // 0-right, 90-top, -90-bottom, 180-left
            // int[] x = {-81, -63, -45, -27, -9, 9, 27, 45, 63, 81};  // possible x coordinates robots can move
            // int[] y = {-81, -63, -45, -27, -9, 9, 27, 45, 63, 81};  // possible y coordinates robots can move

            // int[][] obstacles = {
            //     {9, 9},
            //     {45, 45},
            //     {-63, 45},
            //     {63, -45},
            //     {-45, -45}
            // };

            // int randomHeading, randomX, randomY;
            
            // // robot 1
            // Random random = new Random();
            // randomHeading = random.nextInt(headingDirections.length);
            // do {
            //     randomX = random.nextInt(x.length);
            //     randomY = random.nextInt(y.length);
            // } while (isObstacle(x[randomX], y[randomY], obstacles));
    
            // System.out.println("Robot1 x:" + x[randomX] + ", y:" + y[randomY] + ", heading:" + headingDirections[randomHeading]);
    
            // Robot robot1 = new MappingRobotRandomMoving4(1, x[randomX], y[randomY], headingDirections[randomHeading]);
            // new Thread(robot1).start();

            // // robot 2
            // randomHeading = random.nextInt(headingDirections.length);
            // do {
            //     randomX = random.nextInt(x.length);
            //     randomY = random.nextInt(y.length);
            // } while (isObstacle(x[randomX], y[randomY], obstacles));

            // System.out.println("Robot2 x:" + x[randomX] + ", y:" + y[randomY] + ", heading:" + headingDirections[randomHeading]);

            // Robot robot2 = new MappingRobotRandomMoving4(2, x[randomX], y[randomY], headingDirections[randomHeading]);
            // new Thread(robot2).start();   
            
            //--------------Mapping with unknown initial positions and heading dirctions with 2 robots
            

            //--------------Nuwan aiya swarm code

            // // Start a swarm of robots
            // int[] robotList = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };

            // int startX = 0;
            // int startY = 0;
            // int startHeading = 90;

            // Robot[] vr = new VirtualRobot[robotList.length];

            // for (int i = 0; i < robotList.length; i++) {
            // vr[i] = new SampleRobot(robotList[i], startX + 40 * i, startY + 50 * i,
            // startHeading + 10 * i);
            // new Thread(vr[i]).start();
            // }

            //--------------Nuwan aiya swarm code

        } catch (FileNotFoundException ex) {
            // file does not exist
            System.out.println("File Not Found !!!");

        } catch (IOException ex) {
            // I/O error
            System.out.println("IO Error !!!");
        }
    }

}
