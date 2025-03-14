package ca.mcmaster.se2aa4.island.teamXXX;


public class Battery {

  private int battery_power;

  private int fly_cost = 2;
  private int heading_cost = 4;
  private int echo_cost = 1;
  private int scan_cost = 2;

  public Battery(int battery_power) {
    this.battery_power = battery_power;
  }

  public void fly() {
    battery_power -= fly_cost;
    batteryCheck();
  }

  public void heading() {
    battery_power -= heading_cost;
    batteryCheck();
  }

  public void echo() {
    battery_power = battery_power - echo_cost;
    batteryCheck();
  }

  public void scan() {
    battery_power -= scan_cost;
    batteryCheck();
  }

  public void stop() {
    //might require to pass in position
  }

  private boolean batteryCheck() {
    if (battery_power > 0) {
      return true;
    }
    return false;
  }
}
