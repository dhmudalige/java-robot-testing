package Robots.samples;

import swarm.robot.VirtualRobot;

public class MappingRobotAlg5 extends VirtualRobot {
    
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
    int[] cellValuesNESW = new int[4];

    public MappingRobotAlg5(int id, double x, double y, double heading) {
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

    int rRow;
    int rCol;

    public static void printArray(int[][] array) {
        for (int[] row : array) {
            for (int element : row) {
                System.out.printf("%3d", element);
            }
            System.out.println();
        }
        System.out.println();
    }


    public static void printArraySimplified(int[][] array) {
        for (int[] row : array) {
            for (int element : row) {
                if (element > 0) {
                    System.out.print("  1");
                } else {
                    System.out.printf("%3d", element);
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static String arrayToString(int[][] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                sb.append(arr[i][j]);
                if (j < arr[i].length - 1) {
                    sb.append(" "); // Add space between elements in the same row
                }
            }
            if (i < arr.length - 1) {
                sb.append("\n"); // Add newline between rows
            }
        } 
        return sb.toString();
    }

    public static int[][] stringToArray(String arrayAsString) {
        String[] rows = arrayAsString.split("\n");
        int numRows = rows.length;
        int[][] array = new int[numRows][];
        for (int i = 0; i < numRows; i++) {
            String[] elements = rows[i].split(" ");
            int numCols = elements.length;
            array[i] = new int[numCols];
            for (int j = 0; j < numCols; j++) {
                array[i][j] = Integer.parseInt(elements[j]);
            }
        }
        return array;
    }

    public static int[][] getMergedMap(int[][] arr1, int[][] arr2) {
        int rows = arr1.length;
        int cols = arr1[0].length;

        int[][] mergedMap = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                mergedMap[i][j] = Math.max(arr1[i][j], arr2[i][j]);
            }
        }
        return mergedMap;
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

        rRow = numRows-1-(int)robotRow;
        rCol = (int)robotCol;

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
            


            // give a high value to cells robot moved
            for (int i = 0; i < occupancyGrid.length; i++) {
                for (int j = 0; j < occupancyGrid[i].length; j++) {
                    if (occupancyGrid[i][j] == -2) {
                        occupancyGrid[i][j] = 8;
                    }
                }
            }




            // start: mark explored cells and obstacles ----------------------------------------------------------------------
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
            else {
                // Mark obstacles
                switch (direction) {
                    case 0: // Facing north
                        if (rCol != numCols-1 && occupancyGrid[rRow][rCol+1] == 0){
                            occupancyGrid[rRow][rCol+1] = -1;
                        }
                        break;
                    case 1: // Facing east
                        if ((int)robotRow != 0 && occupancyGrid[rRow+1][rCol] == 0){
                            occupancyGrid[rRow+1][rCol] = -1;
                        }
                        break;
                    case 2: // Facing south
                        if (rCol != 0 && occupancyGrid[rRow][rCol-1] == 0){
                            occupancyGrid[rRow][rCol-1] = -1;
                        }
                        break;
                    case 3: // Facing west
                        if ((int)robotRow != numRows-1 && occupancyGrid[rRow-1][rCol] == 0){
                            occupancyGrid[rRow-1][rCol] = -1;
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
            else {
                // Mark obstacles
                switch (direction) {
                    case 0: // Facing north
                        if ((int)robotRow != numRows-1 && occupancyGrid[rRow-1][rCol] == 0){
                            occupancyGrid[rRow-1][rCol] = -1;
                        }
                        break;
                    case 1: // Facing east
                        if (rCol != numCols-1 && occupancyGrid[rRow][rCol+1] == 0){
                            occupancyGrid[rRow][rCol+1] = -1;
                        }
                        break;
                    case 2: // Facing south
                        if ((int)robotRow != 0 && occupancyGrid[rRow+1][rCol] == 0){
                            occupancyGrid[rRow+1][rCol] = -1;
                        }
                        break;
                    case 3: // Facing west
                        if (rCol != 0 && occupancyGrid[rRow][rCol-1] == 0){
                            occupancyGrid[rRow][rCol-1] = -1;
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
            else {
                // Mark obstacles
                switch (direction) {
                    case 0: // Facing north
                        if (rCol != 0 && occupancyGrid[rRow][rCol-1] == 0){
                            occupancyGrid[rRow][rCol-1] = -1;
                        }
                        break;
                    case 1: // Facing east
                        if ((int)robotRow != numRows-1 && occupancyGrid[rRow-1][rCol] == 0){
                            occupancyGrid[rRow-1][rCol] = -1;
                        }
                        break;
                    case 2: // Facing south
                        if (rCol != numCols-1 && occupancyGrid[rRow][rCol+1] == 0){
                            occupancyGrid[rRow][rCol+1] = -1;
                        }
                        break;
                    case 3: // Facing west
                        if ((int)robotRow != 0 && occupancyGrid[rRow+1][rCol] == 0){
                            occupancyGrid[rRow+1][rCol] = -1;
                        }
                        break;
                } 
            }            

            // printArray(occupancyGrid);
            // end: mark explored cells and obstacles ----------------------------------------------------------------------






            // start: get the min cellValue (finding the cell the robot should move in the next step)----------------------------------------------------------------------
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

            // end: get the min cellValue (finding the cell the robot should move in the next step)----------------------------------------------------------------------








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
            
   
            // printArray(occupancyGrid);
            // printArraySimplified(occupancyGrid);

            simpleComm.sendMessage(arrayToString(occupancyGrid), 200);
        }
    }

    public void communicationInterrupt(String msg) {
        // System.out.println("Robot ID: " + robotId + " communicationInterrupt on " + id + " with msg:\n" + msg);
        // System.out.println();

        int[][] array = stringToArray(msg);
        // printArray(array);

        occupancyGrid = getMergedMap(occupancyGrid, array);
        // printArray(occupancyGrid);
        printArraySimplified(occupancyGrid);
        System.out.println();
    }
}
