package ca.mcmaster.se2aa4.island.teamXXX;

enum Direction {
  NORTH, SOUTH, EAST, WEST;

  public Direction turnLeft() {
    switch (this) {
        case NORTH: return WEST;
        case WEST: return SOUTH;
        case SOUTH: return EAST;
        case EAST: return NORTH;
        default: throw new IllegalStateException("Unexpected value: " + this);
    }
}

// Turning right (clockwise)
public Direction turnRight() {
    switch (this) {
        case NORTH: return EAST;
        case EAST: return SOUTH;
        case SOUTH: return WEST;
        case WEST: return NORTH;
        default: throw new IllegalStateException("Unexpected value: " + this);
    }
}

}

