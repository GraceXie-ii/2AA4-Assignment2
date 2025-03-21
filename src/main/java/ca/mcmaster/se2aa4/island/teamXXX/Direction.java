package ca.mcmaster.se2aa4.island.teamXXX;

public class Direction {
    // Initialize drone direction, final list of directions
    private String droneDir;
    private static final String DIRECTIONS = "NESW";

    // Constructor to initialize drone direction
    public Direction(String direction) {
        this.droneDir = direction;
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