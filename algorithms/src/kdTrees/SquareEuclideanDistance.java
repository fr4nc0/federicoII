package kdTrees;

public class SquareEuclideanDistance implements Distance {

	@Override
	public Double get(Point p1, Point p2) {

		Double square = (double) 0;
		int numCoords = p1.getNumberCoords();

		for ( int i = 0; i < numCoords; i++ ) {
			square = square + Math.pow( (p1.get(i) - p2.get(i) ), 2);
		}
		return square;
	}
}

