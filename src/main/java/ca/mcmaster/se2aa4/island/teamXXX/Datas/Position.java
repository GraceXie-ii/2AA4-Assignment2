package ca.mcmaster.se2aa4.island.teamXXX.Datas;

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

    // get creeks coordinates
    public List<int[]> getCreeksCoordinates() {
        return this.creeksCoordinates;
    }

    // get sites coordinates
    public List<int[]> getSitesCoordinates() {
        return this.sitesCoordinates;
    }

    // check if all POIS are found
    public boolean allFound() {
        return this.creeksID.size() == 10 && this.sitesID.size() == 1;
    }

    // Find nearest creek index to the site
    public double[] findNearestCreek() {

        // temporary variables
        double closestDistance = 0, currentDistance, closestIndex = 0, creekX, creekY, siteX, siteY;

        // get site coordinates
        siteX = this.sitesCoordinates.get(0)[0];
        siteY = this.sitesCoordinates.get(0)[1];
        
        for (int i = 0; i < this.creeksCoordinates.size(); i++) {
            // get creek coordinates
            creekX = this.creeksCoordinates.get(i)[0];
            creekY = this.creeksCoordinates.get(i)[1];

            // find creek distance to site
            currentDistance = findDistance(creekX, creekY, siteX, siteY);

            // set if first creek, else compare with closest
            if (i == 0) {
                closestDistance = currentDistance;
            } else if (currentDistance < closestDistance) {
                closestDistance = currentDistance;
                closestIndex = i;
            }
        }
        
        // return closest creek index in list, and its distance to site
        return new double[] {closestIndex, closestDistance};

    }

    // get distance between a creek and a site
    private double findDistance(double x1, double y1, double x2, double y2) {
        double xSide = Math.pow(x2-x1, 2);
        double ySide = Math.pow(y2-y1, 2);
        double total = Math.sqrt(xSide + ySide);
        return total;
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
