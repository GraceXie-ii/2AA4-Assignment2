package ca.mcmaster.se2aa4.island.teamXXX;

class Direction {
  private String direction;
  private static final String[] DIRECTIONS = {"NORTH", "EAST", "SOUTH", "WEST"};

  public Direction(String direction) {
      this.direction = direction;
  }

  public String turn(String turn) {
      if (turn.equals("left")) {
          return turnLeft();
      }
      return turnRight();
  }

  private String turnLeft() {
      int index = findIndex(direction);
      index = (index + 3) % 4; // Move counter-clockwise
      direction = DIRECTIONS[index];
      return direction;
  }

  private String turnRight() {
      int index = findIndex(direction);
      index = (index + 1) % 4; // Move clockwise
      direction = DIRECTIONS[index];
      return direction;
  }

  private int findIndex(String dir) {
      for (int i = 0; i < DIRECTIONS.length; i++) {
          if (DIRECTIONS[i].equals(dir)) {
              return i;
          }
      }
      throw new IllegalArgumentException("Invalid direction: " + dir);
  }
}