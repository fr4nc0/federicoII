package tree;

public class IntegerDistance implements Distance<Integer> {

	@Override
	public Double distance(Integer t1, Integer t2) {
		
		if ( t1.equals(t2)) {
			@SuppressWarnings("unused")
			double d = Math.abs(t1 - t2);
		}
		return (double) Math.abs(t1 - t2);
		
	}

}
