package kdTrees;

import java.util.Arrays;

public class QueryStartCondition {

	private boolean[] verifiedCoords;
	
	public QueryStartCondition(int numCoordinates) {
		verifiedCoords = new boolean[numCoordinates];
		Arrays.fill(verifiedCoords, false);
	}
	
	public boolean isVerified() {
		
		for (int i = 0; i < verifiedCoords.length; i++) {
			if ( verifiedCoords[i] == false ) {
				return false;
			}
		}
		return true;
	}

	public void setVerifiedCoord(int coord) {
		verifiedCoords[coord] = true;
	}
}
