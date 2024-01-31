package Robots.samples;

import swarm.robot.VirtualRobot;

import java.util.ArrayList;
import java.util.Random;

public class RotatingRobot extends VirtualRobot {

    // Size of a grid cell
    private static final double GRID_SPACE = 18.000;

    // The default rotate speed
    private static final int defaultRotateSpeed = 100;

    // Proximity Sensor options
    // Angles for left,front and right side rotating
    private int[] proximityAngles = { -90, 0, 90 };

    // Index to get left proximity angle
    public static int PROXIMITY_LEFT = 0;

    // Index to get front proximity angle
    public static int PROXIMITY_FRONT = 1;

    // Index to get right proximity angle
    public static int PROXIMITY_RIGHT = 2;

    // Robot's initial position
    double robotRow = 0;
    double robotCol = 0;

    public RotatingRobot(int id, double x, double y, double heading) {
        super(id, x, y, heading);
        robotRow=(x+81)/18;
        robotCol=(y+81)/18;
    }

    int numRows=10;
    int numCols=10;
    int[][] occupancyGrid = new int[numRows][numCols];

    int rightTurns=0;
    int leftTurns=0;

    public void setup() {
        System.out.println("My Test Rotating Robot Started");

        super.setup();

        // Setup proximity sensor with 3 angles
        proximitySensor.setAngles(proximityAngles);

        state = robotState.RUN;

        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                occupancyGrid[row][col] = 0; // set to unoccupied
            }
        }

        // Mark the starting cell as visited
        occupancyGrid[numRows-1-(int)robotRow][(int)robotCol] = 3;

        // Print the array
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                System.out.print(occupancyGrid[row][col] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    @Override
    public void loop() throws Exception {
        super.loop();

        if (state == robotState.RUN) {
            motion.rotate(defaultRotateSpeed);

            // Get present distances from robot's left,front and right
            int[] d = proximitySensor.getProximity().distances();

            // Determine the movement based on the sum of right and left turns modulo 4
            int direction = (rightTurns - leftTurns) % 4;

            ArrayList<Integer> intList = new ArrayList<>();

            // Robot rotating way :- if distance from (any side +6) > GRID_SPACE then robot
            // will rotate that side.
            if (d[PROXIMITY_RIGHT] + 6 > GRID_SPACE) {
                intList.add(1);

                // Mark free spaces
                switch (direction) {
                    case 0: // Facing north
                        occupancyGrid[numRows-1-(int)robotRow][(int)robotCol+1] = 1;
                        break;
                    case 1: // Facing east
                        occupancyGrid[numRows-1-((int)robotRow-1)][(int)robotCol] = 1;
                        break;
                    case 2: // Facing south
                        occupancyGrid[numRows-1-(int)robotRow][(int)robotCol-1] = 1;
                        break;
                    case 3: // Facing west
                        occupancyGrid[numRows-1-((int)robotRow+1)][(int)robotCol] = 1;
                        break;
                }
            } else {
                // Mark obstacles
                switch (direction) {
                    case 0: // Facing north
                        if ((int)robotCol != numCols-1){
                            occupancyGrid[numRows-1-(int)robotRow][(int)robotCol+1] = 2;
                        }
                        break;
                    case 1: // Facing east
                        if ((int)robotRow != 0){
                            occupancyGrid[numRows-1-((int)robotRow-1)][(int)robotCol] = 2;
                        }
                        break;
                    case 2: // Facing south
                        if ((int)robotCol != 0){
                            occupancyGrid[numRows-1-(int)robotRow][(int)robotCol-1] = 2;
                        }
                        break;
                    case 3: // Facing west
                        if ((int)robotRow != numRows-1){
                            occupancyGrid[numRows-1-((int)robotRow+1)][(int)robotCol] = 2;
                        }
                        break;
                }
            }

            if (d[PROXIMITY_FRONT] + 6 > GRID_SPACE) {
                intList.add(2);

                // Mark free spaces
                switch (direction) {
                    case 0: // Facing north
                        occupancyGrid[numRows-1-((int)robotRow+1)][(int)robotCol] = 1;
                        break;
                    case 1: // Facing east
                        occupancyGrid[numRows-1-(int)robotRow][(int)robotCol+1] = 1;
                        break;
                    case 2: // Facing south
                        occupancyGrid[numRows-1-((int)robotRow-1)][(int)robotCol] = 1;
                        break;
                    case 3: // Facing west
                        occupancyGrid[numRows-1-(int)robotRow][(int)robotCol-1] = 1;
                        break;
                }
            } else {
                // Mark obstacles
                switch (direction) {
                    case 0: // Facing north
                        if ((int)robotRow != numRows-1){
                            occupancyGrid[numRows-1-((int)robotRow+1)][(int)robotCol] = 2;
                        }
                        break;
                    case 1: // Facing east
                        if ((int)robotCol != numCols-1){
                            occupancyGrid[numRows-1-(int)robotRow][(int)robotCol+1] = 2;
                        }
                        break;
                    case 2: // Facing south
                        if ((int)robotRow != 0){
                            occupancyGrid[numRows-1-((int)robotRow-1)][(int)robotCol] = 2;
                        }
                        break;
                    case 3: // Facing west
                        if ((int)robotCol != 0){
                            occupancyGrid[numRows-1-(int)robotRow][(int)robotCol-1] = 2;
                        }
                        break;
                }
            }

            if (d[PROXIMITY_LEFT] + 6 > GRID_SPACE) {
                intList.add(3);

                // Mark free spaces
                switch (direction) {
                    case 0: // Facing north
                        occupancyGrid[numRows-1-(int)robotRow][(int)robotCol-1] = 1;
                        break;
                    case 1: // Facing east
                        occupancyGrid[numRows-1-((int)robotRow+1)][(int)robotCol] = 1;
                        break;
                    case 2: // Facing south
                        occupancyGrid[numRows-1-(int)robotRow][(int)robotCol+1] = 1;
                        break;
                    case 3: // Facing west
                        occupancyGrid[numRows-1-((int)robotRow-1)][(int)robotCol] = 1;
                        break;
                }
            } else {
                // Mark obstacles
                switch (direction) {
                    case 0: // Facing north
                        if ((int)robotCol != 0){
                            occupancyGrid[numRows-1-(int)robotRow][(int)robotCol-1] = 2;
                        }
                        break;
                    case 1: // Facing east
                        if ((int)robotRow != numRows-1){
                            occupancyGrid[numRows-1-((int)robotRow+1)][(int)robotCol] = 2;
                        }
                        break;
                    case 2: // Facing south
                        if ((int)robotCol != numCols-1){
                            occupancyGrid[numRows-1-(int)robotRow][(int)robotCol+1] = 2;
                        }
                        break;
                    case 3: // Facing west
                        if ((int)robotRow != 0){
                            occupancyGrid[numRows-1-((int)robotRow-1)][(int)robotCol] = 2;
                        }
                        break;
                }
            }

            occupancyGrid[numRows-1-(int)robotRow][(int)robotCol] = 3;

            // Print the array
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    System.out.print(occupancyGrid[row][col] + " ");
                }
                System.out.println();
            }
            System.out.println();
        }
    }
}
