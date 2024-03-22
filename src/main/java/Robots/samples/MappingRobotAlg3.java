package Robots.samples;

import swarm.robot.VirtualRobot;

public class MappingRobotAlg3 extends VirtualRobot {
    
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

    // Robot's initial position
    double robotRow = 0;
    double robotCol = 0;
    int robotId = 0;
    int cellValue = 0;
    int[] cellValuesNESW = new int[4];

    public MappingRobotAlg3(int id, double x, double y, double heading) {
        super(id, x, y, heading);
        robotRow=(x+81)/18;
        robotCol=(y+81)/18;
        robotId=id;
    }

    int numRows=10;
    int numCols=10;
    int[][] occupancyGrid = new int[numRows][numCols];

    int rightTurns=0;
    int leftTurns=0;

    int rRow = numRows-1-(int)robotRow;
    int rCol = (int)robotCol;

    public static void printArray(int[][] array) {
        for (int[] row : array) {
            for (int element : row) {
                System.out.printf("%3d", element);
            }
            System.out.println();
        }
        System.out.println();
    }


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
        occupancyGrid[rRow][rCol] = -2;

        printArray(occupancyGrid);
    }

    public void loop() throws Exception {
        super.loop();

        if (state == robotState.RUN) {
            // Get present distances from robot's left,front and right
            int[] d = proximitySensor.getProximity().distances();

            // Determine the movement based on the sum of right and left turns modulo 4
            int direction = (rightTurns - leftTurns) % 4;
            
            // Adjust direction to ensure it's within the range of 0 to 3
            if (direction < 0) {
                direction += 4;
            }
            
            
            // get the cellValues
            if (rRow!=0){
                cellValuesNESW[0]=occupancyGrid[rRow-1][rCol];
                // System.out.println(occupancyGrid[rRow-1][rCol]);
            } else {
                cellValuesNESW[0]=-1;  // obstacle
            }

            if (rCol!=numCols-1){
                cellValuesNESW[1]=occupancyGrid[rRow][rCol+1];
            } else {
                cellValuesNESW[1]=-1;  // obstacle
            }

            if (rRow!=numRows-1){
                cellValuesNESW[2]=occupancyGrid[rRow+1][rCol];
            } else {
                cellValuesNESW[2]=-1;  // obstacle
            }

            if (rCol!=0){
                cellValuesNESW[3]=occupancyGrid[rRow][rCol-1];
            } else {
                cellValuesNESW[3]=-1;  // obstacle
            }

            int minCellValue = Integer.MAX_VALUE; // Initialize with a very large value
            int minCellValueIndex =0;

            for (int i = 0; i < cellValuesNESW.length; i++) {
                // System.out.println(cellValuesNESW[i]);
                if (cellValuesNESW[i] >= 0 && cellValuesNESW[i] < minCellValue) {
                    minCellValue = cellValuesNESW[i];
                    minCellValueIndex = i;
                }
            }

            // System.out.println(minCellValue + " " + minCellValueIndex);



            cellValue = occupancyGrid[rRow][rCol];

            // Change entries with value 3 to 1
            for (int i = 0; i < occupancyGrid.length; i++) {
                for (int j = 0; j < occupancyGrid[i].length; j++) {
                    if (occupancyGrid[i][j] == -2) {
                        occupancyGrid[i][j] = 3;
                    }
                }
            }






            // Robot rotating way :- if distance from (any side +6) > GRID_SPACE then robot
            // will rotate that side.
            if (d[PROXIMITY_RIGHT] + 6 > GRID_SPACE) {
                
                // System.out.println(d[PROXIMITY_RIGHT]);
                
                // Mark free spaces
                switch (direction) {
                    case 0: // Facing north
                        int count=0;
                        for (int i = 0; i < (d[PROXIMITY_RIGHT] + 6) / GRID_SPACE; i++) {
                            // System.out.println(i);
                            occupancyGrid[rRow][rCol+(i)] += 1;
                            count++;
                        }
                        if (rCol+(count)+1<numCols && occupancyGrid[rRow][rCol+(count)] == 0) {
                            occupancyGrid[rRow][rCol+(count)] = -1;
                        }
                        break;
                    case 1: // Facing east
                        count=0;
                        for (int i = 0; i < (d[PROXIMITY_RIGHT] + 6) / GRID_SPACE; i++) {
                            occupancyGrid[rRow+i][rCol] += 1;
                            count++;
                        }
                        if (rRow+count<numRows && occupancyGrid[rRow+count][rCol]  == 0) {
                            occupancyGrid[rRow+count][rCol] = -1;
                        }
                        break;
                    case 2: // Facing south
                        count=0;
                        for (int i = 0; i < (d[PROXIMITY_RIGHT] + 6) / GRID_SPACE; i++) {
                            occupancyGrid[rRow][rCol-(i)] += 1;
                            count++;
                        }
                        if (rCol-(count)>0 && occupancyGrid[rRow][rCol-(count)] == 0) {
                            occupancyGrid[rRow][rCol-(count)] = -1;
                        }
                        break;
                    case 3: // Facing west
                        count=0;
                        for (int i = 0; i < (d[PROXIMITY_RIGHT] + 6) / GRID_SPACE; i++) {
                            occupancyGrid[rRow-i][rCol] += 1;
                            count++;
                        }
                        if (rRow-count>0 && occupancyGrid[rRow-count][rCol] == 0) {
                            occupancyGrid[rRow-count][rCol] = -1;
                        }
                        break;
                } 
            } 
            
            if (d[PROXIMITY_FRONT] + 6 > GRID_SPACE) {

                // Mark free spaces
                switch (direction) {
                    case 0: // Facing north
                        int count=0;
                        for (int i = 0; i < (d[PROXIMITY_FRONT] + 6) / GRID_SPACE; i++) {
                            occupancyGrid[rRow-i][rCol] += 1;
                            count++;
                        }
                        if (rRow-count>0 && occupancyGrid[rRow-count][rCol] == 0) {
                            occupancyGrid[rRow-count][rCol] = -1;
                        }
                        break;
                    case 1: // Facing east
                        count=0;
                        for (int i = 0; i < (d[PROXIMITY_FRONT] + 6) / GRID_SPACE; i++) {
                            occupancyGrid[rRow][rCol+i] += 1;
                            count++;
                        }                        
                        if (rCol+count<numCols && occupancyGrid[rRow][rCol+count] == 0) {
                            occupancyGrid[rRow][rCol+count] = -1;
                        }
                        break;
                    case 2: // Facing south
                        count=0;
                        for (int i = 0; i < (d[PROXIMITY_FRONT] + 6) / GRID_SPACE; i++) {
                            occupancyGrid[rRow+i][rCol] += 1;
                            count++;
                        }       
                        if (rRow+count<numRows && occupancyGrid[rRow+count][rCol] == 0) {
                            occupancyGrid[rRow+count][rCol] = -1;
                        }
                        break;
                    case 3: // Facing west
                        count=0;
                        for (int i = 0; i < (d[PROXIMITY_FRONT] + 6) / GRID_SPACE; i++) {
                            occupancyGrid[rRow][rCol-i] += 1;
                            count++;
                        }       
                        if (rCol-count>0 && occupancyGrid[rRow][rCol-count] == 0) {
                            occupancyGrid[rRow][rCol-count] = -1;
                        }
                        break;
                } 
            } 
            
            if (d[PROXIMITY_LEFT] + 6 > GRID_SPACE) {

                // Mark free spaces
                switch (direction) {
                    case 0: // Facing north
                        int count=0;
                        for (int i = 0; i < (d[PROXIMITY_LEFT] + 6) / GRID_SPACE; i++) {
                            occupancyGrid[rRow][rCol-i] += 1;
                            count++;
                        }       
                        if (rCol-count>0 && occupancyGrid[rRow][rCol-count] == 0) {
                            occupancyGrid[rRow][rCol-count] = -1;
                        }
                        break;
                    case 1: // Facing east
                        count=0;
                        for (int i = 0; i < (d[PROXIMITY_LEFT] + 6) / GRID_SPACE; i++) {
                            occupancyGrid[rRow-i][rCol] += 1;
                            count++;
                        }       
                        if (rRow-count>0 && occupancyGrid[rRow-count][rCol] == 0) {
                            occupancyGrid[rRow-count][rCol] = -1;
                        }
                        break;
                    case 2: // Facing south
                        count=0;
                        for (int i = 0; i < (d[PROXIMITY_LEFT] + 6) / GRID_SPACE; i++) {
                            occupancyGrid[rRow][rCol+i] += 1;
                            count++;
                        }       
                        if (rCol+count<numCols && occupancyGrid[rRow][rCol+count] == 0) {
                            occupancyGrid[rRow][rCol+count] = -1;
                        }
                        break;
                    case 3: // Facing west
                        count=0;
                        for (int i = 0; i < (d[PROXIMITY_LEFT] + 6) / GRID_SPACE; i++) {
                            occupancyGrid[rRow+i][rCol] += 1;
                            count++;
                        }       
                        if (rRow+count<numRows && occupancyGrid[rRow+count][rCol] == 0) {
                            occupancyGrid[rRow+count][rCol] = -1;
                        }
                        break;
                } 
            } 
            
            // printArray(occupancyGrid);










            // Move based on the calculated direction
            switch (direction) {
                case 0: // Facing north
                    switch (minCellValueIndex) {
                        case 0: // north
                            robotRow++;
                            break;
                        case 1: // east
                            motion.rotateDegree(defaultRotateSpeed, 90);
                            rightTurns++;
                            robotCol++;
                            break;
                        case 2: // south
                            motion.rotateDegree(defaultRotateSpeed, 180);
                            rightTurns++;rightTurns++;
                            robotRow--;
                            break;
                        case 3: // west
                            motion.rotateDegree(defaultRotateSpeed, -90);
                            leftTurns++;
                            robotCol--;
                            break;
                    }
                    break;
                case 1: // Facing east
                    switch (minCellValueIndex) {
                        case 0: // north
                            motion.rotateDegree(defaultRotateSpeed, -90);
                            leftTurns++;
                            robotRow++;
                            break;
                        case 1: // east
                            robotCol++;
                            break;
                        case 2: // south
                            motion.rotateDegree(defaultRotateSpeed, 90);
                            rightTurns++;
                            robotRow--;
                            break;
                        case 3: // west
                            motion.rotateDegree(defaultRotateSpeed, 180);
                            rightTurns++;rightTurns++;
                            robotCol--;
                            break;
                    }
                    break;
                case 2: // Facing south
                    switch (minCellValueIndex) {
                        case 0: // north
                            motion.rotateDegree(defaultRotateSpeed, 180);
                            rightTurns++;rightTurns++;
                            robotRow++;
                            break;
                        case 1: // east
                            motion.rotateDegree(defaultRotateSpeed, -90);
                            leftTurns++;
                            robotCol++;
                            break;
                        case 2: // south
                            robotRow--;
                            break;
                        case 3: // west
                            motion.rotateDegree(defaultRotateSpeed, 90);
                            rightTurns++;
                            robotCol--;
                            break;
                    }
                    break;
                case 3: // Facing west
                    switch (minCellValueIndex) {
                        case 0: // north
                            motion.rotateDegree(defaultRotateSpeed, 90);
                            rightTurns++;
                            robotRow++;
                            break;
                        case 1: // east
                            motion.rotateDegree(defaultRotateSpeed, 180);
                            rightTurns++;rightTurns++;
                            robotCol++;
                            break;
                        case 2: // south
                            motion.rotateDegree(defaultRotateSpeed, -90);
                            leftTurns++;
                            robotRow--;
                            break;
                        case 3: // west
                            robotCol--;
                            break;
                    }
                    break;
            } 


            // Robot move
            motion.moveDistance(defaultMoveSpeed, GRID_SPACE);
            delay(1000);


            rRow = numRows-1-(int)robotRow;
            rCol = (int)robotCol;



            
            occupancyGrid[rRow][rCol] = -2;
            
            printArray(occupancyGrid);
        }
    }
}
