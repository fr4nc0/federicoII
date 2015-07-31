package kdTrees;

import java.util.Arrays;

public class Point {
	Double[] coordinates;
	
	
	public Point(int numCoordinates) {
		coordinates = new Double[numCoordinates];
	}
	
	public Point(Double x, Double y) {
		coordinates = new Double[2];
		coordinates[0] = x;
		coordinates[1] = y;
	}
	
	public Point(int x, int y) {
		coordinates = new Double[2];
		coordinates[0] = (double) x;
		coordinates[1] = (double) y;
	}


	public Double get( int i ) {
		return coordinates[i];
	}
	
	public void set( int i, Double newValue ) {
		coordinates[i] = newValue;
	}
	
	public int getNumberCoords() {
		return coordinates.length;
	}


	@Override
	public String toString() {
		return "(" + coordinates[0] + ", " + coordinates[1] + ")";
	}
	
	
}
