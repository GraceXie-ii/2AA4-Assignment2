package ca.mcmaster.se2aa4.island.teamXXX;

import org.junit.jupiter.api.Test;

import ca.mcmaster.se2aa4.island.teamXXX.Actions.Move;
import ca.mcmaster.se2aa4.island.teamXXX.Actions.PhotoScanner;
import ca.mcmaster.se2aa4.island.teamXXX.Actions.Radar;
import ca.mcmaster.se2aa4.island.teamXXX.Datas.Direction;
import ca.mcmaster.se2aa4.island.teamXXX.Datas.Position;
import ca.mcmaster.se2aa4.island.teamXXX.Strategies.DroneStrategy;

import static org.junit.jupiter.api.Assertions.*;


public class ExampleTest {

    // get subclasses
    private Direction droneDir = new Direction("East");
    private Move move = new Move();
    private Radar radar = new Radar();
    private PhotoScanner scanner = new PhotoScanner();
    private DroneStrategy strategy = new DroneStrategy("bruteSearch");
    private Position position = new Position();

    /* 
    @Test
    public void sampleTest() {
        assertTrue(1 == 1);
    }
    */
    // ---------------------------------Testing Move subclass-----------------------------------------------------
    @Test
    public void testFlyActionToString() { // check if fly outputs action fly correctly
        assertTrue(move.fly().equals("{\"action\":\"fly\"}")); // JSON Object toString does not contain spaces
    }

    @Test
    public void testHeadingActionToString() { // check if heading outputs JSON decision correctly
        assertTrue(move.heading("S").equals("{\"action\":\"heading\",\"parameters\":{\"direction\":\"S\"}}")); // still no spaces
    }

    @Test
    public void testStopActionToString() { // check if stop outputs decision correctly
        assertTrue(move.stop().equals("{\"action\":\"stop\"}"));
    }

    // ---------------------------------Testing Position subclass-------------------------------------------------
    // MOCK of Position - private findDistance method: get distance between a creek and a site
    private double MOCK_findDistance(double x1, double y1, double x2, double y2) {
        double xSide = Math.pow(x2-x1, 2);
        double ySide = Math.pow(y2-y1, 2);
        double total = Math.sqrt(xSide + ySide);
        return total;
    }

    @Test
    public void testDistanceCalculation() {

        double sampleX1 = 2, sampleY1 = 3, sampleX2 = 4, sampleY2 = 5, result;
        
        result = MOCK_findDistance(sampleX1, sampleY1, sampleX2, sampleY2);

        // result should be = sqrt((4-2) ^ 2 + (5-3) ^ 2) = sqrt(4+4) = sqrt(8) is approx 2.828
        assertTrue(2.82 < result && result < 2.83);
    }


}
