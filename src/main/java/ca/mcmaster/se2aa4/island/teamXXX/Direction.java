package ca.mcmaster.se2aa4.island.teamXXX;

public class Direction {
    // Initialize drone direction, final list of directions
    private String droneDir;
    private static final String DIRECTIONS = "NESW";
    private static final int[][] DIRECTION_VECTOR = new int[][]{{0, -10}, {10, 0}, {0, 10}, {-10, 0}};

    // Constructor to initialize drone direction
    public Direction(String direction) {
        this.droneDir = direction;
    }

    // get current drone direction 
    public String getDroneDir() {
        return this.droneDir;
    }

    // get current drone direction vector = [x, y] for fly
    public int[] getDirectionVector() {
        return DIRECTION_VECTOR[DIRECTIONS.indexOf(this.droneDir)];
    }

    // get new direction vector after heading
    public int[] getDirectionVector(String turn) {
        // calculate new direction vector = fly forward + fly in turn direction
        String newDir = ifTurn(turn); // get direction after turn 
        int x = DIRECTION_VECTOR[DIRECTIONS.indexOf(this.droneDir)][0] + DIRECTION_VECTOR[DIRECTIONS.indexOf(newDir)][0];
        int y = DIRECTION_VECTOR[DIRECTIONS.indexOf(this.droneDir)][1] + DIRECTION_VECTOR[DIRECTIONS.indexOf(newDir)][1];

        // return new direction vector after turn
        int[] vector = new int[] {x, y};
        return vector;
    }

    // for heading
    public String turn(String turn) {

        // calculate new direction, update drone direction
        if (turn.equals("L")) {
            this.droneDir = DIRECTIONS.charAt((DIRECTIONS.indexOf(this.droneDir) + 3) % 4) + "";
        } else if (turn.equals("R")) {
            this.droneDir = DIRECTIONS.charAt((DIRECTIONS.indexOf(this.droneDir) + 1) % 4) + "";
        } 

        // return new direction
        return this.droneDir;
    }

    // for echo
    public String ifTurn(String turn) {

        // Initialize a dummy direction
        String direction = "";

        // calculate left/right/front direction, else print invalid direction
        if (turn.equals("LEFT")) {
            direction = DIRECTIONS.charAt((DIRECTIONS.indexOf(this.droneDir) + 3) % 4) + "";
        } else if (turn.equals("RIGHT")) {
            direction = DIRECTIONS.charAt((DIRECTIONS.indexOf(this.droneDir) + 1) % 4) + "";
        } else if (turn.equals("FRONT")) {
            direction = DIRECTIONS.charAt(DIRECTIONS.indexOf(this.droneDir)) + "";
        } else {
            System.out.println("Invalid direction");
        }

        // return left or right direction
        return direction;
    }

}