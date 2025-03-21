package ca.mcmaster.se2aa4.island.teamXXX;

import java.util.ArrayList;
import java.util.List;

public class Position {

    // Initialize drone coordinates; creeks ID and coordinates; sites ID and coordinates
    private int x, y;
    private List<String> creeksID, sitesID;
    private List<int[]> creeksCoordinates, sitesCoordinates;

    public Position() {
        // Initialize drone coordinates to (1, 1)
        this.x = 1; 
        this.y = 1; 

        // Initialize creeks and sites - ID and coordinates
        this.creeksID = new ArrayList<String>();
        this.sitesID = new ArrayList<String>();
        this.creeksCoordinates = new ArrayList<int[]>();
        this.sitesCoordinates = new ArrayList<int[]>();
    }

    // return drone coordinates
    public int[] getCoordinates() {
        return new int[] {this.x, this.y}; // return drone coordinates
    }

    // get creeks ID
    public List<String> getCreeksID() {
        return this.creeksID; // return creeks ID
    }

    // get sites ID
    public List<String> getSitesID() {
        return this.sitesID; // return sites ID
    }

    // Find nearest creek index to the site
    public int findNearestCreekIndex() {
        
        return 0;

    }

    // update drone position
    public void updatePosition(int[] direction_vector) {
        this.x += direction_vector[0];
        this.y += direction_vector[1];
    }

    // update creeks ID and coordinates
    public void addCreek(String id, int[] coordinates) {
        this.creeksID.add(id);
        this.creeksCoordinates.add(coordinates);
    }

    // update sites ID and coordinates
    public void addSite(String id, int[] coordinates) {
        this.sitesID.add(id);
        this.sitesCoordinates.add(coordinates);
    }
    
}
