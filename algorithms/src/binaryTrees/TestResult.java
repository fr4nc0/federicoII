package binaryTrees;

public class TestResult {

	public Integer numRoot = 0, numNoRoot = 0;
	public Double  percNumRoot = (double) 0, percNumNoRoot = (double) 0;

	public Integer skippedElabs = 0, totElabs = 0;

	public void calculatePercentages() {

		int denominatore = this.totElabs - this.skippedElabs; 	//tranne nodo radice che non viene elaborato
		this.percNumRoot   =  (((double)this.numRoot   * 100) / denominatore );
		this.percNumNoRoot =  (((double)this.numNoRoot * 100) / denominatore );

	}

	public void printResults(String label) {
		System.out.println (label + " elabs:" + this.totElabs + " skipped:" + this.skippedElabs 
				+ " - noRoot:" 
				+ this.numNoRoot + "(" + this.percNumNoRoot + "%) - Root:" 
				+ this.numRoot   + "(" + this.percNumRoot   + "%)"); 


	}

}
