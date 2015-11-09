package fg.mpj.tree;

import javax.swing.text.MaskFormatter;
import javax.swing.text.AbstractDocument.BranchElement;

import mpi.MPI;
import mpi.Request;
import mpi.Status;

public class MpjTreeTestBuf {

	private static Integer numPoints				= 16765000;

	private static final int DISPATCHER 			= 0;
	private static final int ROOT 					= 1;
	
	public static void main(String[] args) throws Exception{

		int numPointInCurrentProcess = 0;

		MPI.Init(args);

		int me = MPI.COMM_WORLD.Rank();

		System.out.println(labelMySelf() + " started.");

		if ( me == DISPATCHER ) {

			// invia a ROOT i punti da eseguire
			int[] points =  new int[numPoints];
			for ( int i = 0; i < numPoints; i++ ) {
				points[i] = i;
			}

			System.out.println(labelMySelf() + " sending points: lenght=" + points.length);
			MPI.COMM_WORLD.Send(points, 0, points.length, MPI.INT, 
					ROOT, 99);

		} else {

			// receive the points  
			System.out.println(labelMySelf() + " waiting for an insert request...");
			int[] points = new int[numPoints];
			MPI.COMM_WORLD.Recv(points, 0, numPoints, MPI.INT, MPI.ANY_SOURCE, 99);
			System.out.println(labelMySelf() + " received an insert request for lenght: " + points.length);

			System.out.println(points[numPoints - 1]);
		}
		MPI.Finalize();
	}

	private static String labelMySelf() {

		String label = "";
		int me = MPI.COMM_WORLD.Rank();
		switch (me) {
		case 0:
			label = "<Dispatcher>";
			break;
		case 1:
			label = "<Root>";
			break;

		default:
			label = "<" + me + ">";
			break;
		}
		return label;
	}


}
