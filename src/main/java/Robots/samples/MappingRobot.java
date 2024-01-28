package Robots.samples;

import swarm.robot.VirtualRobot;

public class MappingRobot extends VirtualRobot {
    
    // Size of a grid cell
    private final double GRID_SPACE = 18.000;

    // The default movement speed
    private final int defaultMoveSpeed = 200;

    // The default rotate speed
    private final int defaultRotateSpeed = 200;

    // Proximity Sensor options
    // Angles for left,front and right side rotating
    private int[] proximityAngles = { -90, 0, 90 };

    // Index to get left proximity angle
    public static int PROXIMITY_LEFT = 0;

    // Index to get front proximity angle
    public static int PROXIMITY_FRONT = 1;

    // Index to get right proximity angle
    public static int PROXIMITY_RIGHT = 2;

    public MappingRobot(int id, double x, double y, double heading) {
        super(id, x, y, heading);
    }


    int numRows=10;
    int numCols=10;
    int[][] occupancyGrid = new int[numRows][numCols];
    
    // Robot's initial position
    int robotX = 0;
    int robotY = 0;

    int rightTurns=0;
    int leftTurns=0;

    public void setup() {
        System.out.println("My Test Robot Started");

        super.setup();

        // Setup proximity sensor with 3 angles
        proximitySensor.setAngles(proximityAngles);

        // Start immediately after the setup
        state = robotState.RUN;


        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                occupancyGrid[row][col] = 0; // set to unoccupied
            }
        }

        // Mark the starting cell as visited
        occupancyGrid[numRows-1-robotX][robotY] = 1;

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
            // Get present distances from robot's left,front and right
            int[] d = proximitySensor.getProximity().distances();

            // Robot rotating way :- if distance from (any side +6) > GRID_SPACE then robot
            // will rotate that side.

            if (d[PROXIMITY_RIGHT] + 6 > GRID_SPACE) {
                // Right
                motion.rotateDegree(defaultRotateSpeed, 90);
                rightTurns++;

            } else if (d[PROXIMITY_FRONT] + 6 > GRID_SPACE) {
                // Front

            } else if (d[PROXIMITY_LEFT] + 6 > GRID_SPACE) {
                // Turn Left
                motion.rotateDegree(defaultRotateSpeed, -90);
                leftTurns++;

            } else {
                // If robot can't go left,right and front then robot will rotate to back.
                motion.rotateDegree(defaultRotateSpeed, 180);
            }

            // Robot move
            motion.moveDistance(defaultMoveSpeed, GRID_SPACE);
            delay(1000);

            if (rightTurns%4==1 && leftTurns%4==0){
                robotY++;
            } else if (rightTurns%4==1 && leftTurns%4==1){
                robotX++;
            } else if (rightTurns%4==1 && leftTurns%4==2){
                robotY--;
            } else if (rightTurns%4==1 && leftTurns%4==3){
                robotX--;
            }

            occupancyGrid[numRows-1-robotX][robotY] = 1;
            
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
