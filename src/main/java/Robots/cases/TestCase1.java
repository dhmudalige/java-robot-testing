package Robots.cases;

import Robots.samples.RotatingRobot;
import Robots.samples.SampleRobot;
import swarm.configs.MQTTSettings;
import swarm.robot.Robot;
import swarm.robot.VirtualRobot;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class TestCase1 {

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

            Robot robot1 = new RotatingRobot(1, -45, -45, 90);
            new Thread(robot1).start();

            Robot robot2 = new RotatingRobot(2, 81, 81, 90);
            new Thread(robot2).start();

//            // Start a swarm of robots
//            int robotCount = 4;
//            ArrayList<Integer> robotList = new ArrayList<>(robotCount);
//            for (int i = 0; i < robotCount; i++) {
//                robotList.add(i);
//            }
//
//             int startX = 0;
//             int startY = 0;
//             int startHeading = 90;
//
//             Robot[] vr = new VirtualRobot[robotCount];
//
//             for (int i = 0; i < robotCount; i++) {
//                 vr[i] = new SampleRobot(robotList.get(i), startX + 40 * i, startY + 50 * i,
//                 startHeading + 10 * i);
//                 new Thread(vr[i]).start();
//             }

        } catch (FileNotFoundException ex) {
            // file does not exist
            System.out.println("File Not Found !!!");

        } catch (IOException ex) {
            // I/O error
            System.out.println("IO Error !!!");
        }
    }

}
