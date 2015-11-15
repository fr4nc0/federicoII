package fg.mpj.mpjBinaryTree;

import java.util.ArrayList;

import binaryTrees.BucketBinaryTree;
import binaryTrees.BucketNode;
import fg.mpj.tree.Direction;
import graphics.Node;
import mpi.MPI;
import mpi.Status;
import mpjdev.javampjdev.MPJDev;

public class MpjBucketBinaryTree {

	private static boolean insert 					= true;
	private static Integer nextIDProcess 			= 2;
	private static Integer maxIDProcess;
	private static Integer maxNumNode				= 20;
	private static Integer numPoints				= 100;


	private static final int DISPATCHER 			= 0;
	private static final int ROOT 					= 1;
	private static final int NULL_PROCESS_ID		= -1;


	private static final int MSG_INSERT_START				= 10;
	private static final int MSG_REQUIRED_ID				= 20;
	private static final int MSG_SENT_ID					= 30;
	private static final int MSG_INSERT_COMPLETE			= 40;
	private static final int MSG_NO_REQUIRED_ID				= 50;
	private static final int MSG_CLONE_SUBTREE_START		= 60;
	private static final int MSG_CLONE_SUBTREE_COMPLETE		= 70;
	private static final int MSG_PRINT_POINTS				= 80;
	private static final int MSG_INSERT_BLOCKED				= 90;

	private static BucketBinaryTree localTree;


	public static void main(String[] args) throws Exception{

		MPI.Init(args);

		int me = MPI.COMM_WORLD.Rank();
		maxIDProcess  = MPI.COMM_WORLD.Size();

		System.out.println(labelMySelf() + " started.");

		if ( me == DISPATCHER ) {

			dispatcherBehaviour();

		} else {

			while (true) {

				processBehaviour();
			}
		}

		MPI.Finalize();
	}

	private static void processBehaviour() {

		// waiting for a message  
		int[] points = new int[1];
		Status status = MPI.COMM_WORLD.Recv(points, 0, 1, MPI.INT, 
				MPI.ANY_SOURCE, MPI.ANY_TAG);
		printMessage(MPI.COMM_WORLD.Rank(), Direction.receive, status.source, 
				points, status.tag, "");

		int newPoint = points[0];
		
		switch (status.tag) {
		case MSG_INSERT_START:

			if ( ! reachedLimitCondition() ) {
				
				localTree.insert(newPoint);
			
			} else {
				
				int nextProcess = getNextProcessFromDispatcher(newPoint);
				BucketNode cutTree = cutMaxLocalRightSubTree(nextProcess);
				sendCloneSubtreeTo(nextProcess, cutTree, newPoint);
				
			}

			break;

		case MSG_PRINT_POINTS:

			printLocalTree();
			break;

		default:
			displayUMO(status);
			break;
		}
	}

	private static int getNextProcessFromDispatcher(int newPoint) {

		// require the id of a new process
		int[] requireID = new int[1];
		requireID[0] = newPoint;
		printMessage(MPI.COMM_WORLD.Rank(), Direction.send, DISPATCHER, requireID, MSG_REQUIRED_ID, "");
		MPI.COMM_WORLD.Send(requireID, 0, 1, MPI.INT, 
				DISPATCHER, MSG_REQUIRED_ID);	
		
		// wait for the id of the new process
		int[] nextID = new int[1] ;
		MPI.COMM_WORLD.Recv(nextID, 0, 1, MPI.INT, 
				DISPATCHER,	MSG_SENT_ID);
		printMessage(MPI.COMM_WORLD.Rank(), Direction.receive, DISPATCHER, nextID, MSG_SENT_ID, "");
		int nextProcess = nextID[0];
		return nextProcess;
	}

	private static void sendCloneSubtreeTo(int nextProcess, BucketNode cutTree, int newPoint) {
		// TODO Auto-generated method stub
		
	}

	private static BucketNode cutMaxLocalRightSubTree(int nextProcess) {

		/*
		 * starts at the root and go left until right child is not null
		 */
		BucketNode rootRightSubtree = (BucketNode) getRightSubTreeToCut(localTree.getRoot());
		return null;
	}

	private static Object getRightSubTreeToCut(NodeMinMax node) {
		if ( !(node.getRight().isProxy) ) {
			return node.getRight();
		} else {
			
		}
		return null;
	}

	private static boolean reachedLimitCondition() {
		boolean res = false;
		if ( localTree.allNodes.size() > maxNumNode ) {
			res = true;
		}
		return res;
	}

	private static void printLocalTree() {
		// TODO Auto-generated method stub
		
	}

	private static void dispatcherBehaviour() {

		// send to ROOT the point to insert
		for (Integer i = 0; i < numPoints; i++) {
			if ( insert ) {

				int[] points =  new int[1];
				points[0] = i;

				printMessage(MPI.COMM_WORLD.Rank(), Direction.send ,ROOT, points, MSG_INSERT_START, "");
				MPI.COMM_WORLD.Send(points, 0, points.length, MPI.INT, 
						ROOT, MSG_INSERT_START);

			} else {

				System.out.println(labelMySelf() + " insert stopped at i:" + i);
				break;
			}

			// Dispatcher waits for insert complete or insert blocked before insert next point
			int receivedTag = 0;
			while ( ! (receivedTag == MSG_INSERT_COMPLETE ||
					receivedTag == MSG_INSERT_BLOCKED) ) {

				int[] message = new int[1];
				Status status = MPI.COMM_WORLD.Recv(message, 0, message.length, MPI.INT, 
						MPI.ANY_SOURCE, MPI.ANY_TAG);

				receivedTag = status.tag;	
				elaborateMessage(status, message);
				printMessage(MPI.COMM_WORLD.Rank(), Direction.receive, status.source,  message, status.tag, "");
			}

		}

		int[] printMsg = new int[1];
		for( int i = 1; i < maxIDProcess; i++) {

			int[] points = new int[1];
			points[0] = -1;
			printMessage(MPI.COMM_WORLD.Rank(), Direction.isend,  i, points, MSG_PRINT_POINTS, " print");
			MPI.COMM_WORLD.Isend(printMsg, 0, printMsg.length, MPI.INT, 
					i, MSG_PRINT_POINTS);
		}
	}

	private static void displayUMO(Status status) {

		//int[] points = new int[1];
		//points[0] = -1;
		//printMessage(me, status.tag, confirmNoNewProcess, MSG_NO_REQUIRED_ID, " ");
		String msgType = getMessageType(status.tag);
		System.out.println(labelMySelf() + " received UMO ---------> from:" + labelProcess(status.source) + " tag:" + msgType);
	}

	private static String getMessageType(int tag) {

		String msgType = "";
		switch (tag) {
		case MSG_INSERT_START:
			msgType = "MSG_INSERT_START";
			break;

		case MSG_REQUIRED_ID:
			msgType = "MSG_REQUIRED_ID";
			break;

		case MSG_SENT_ID:
			msgType = "MSG_SENT_ID";
			break;

		case MSG_INSERT_COMPLETE:
			msgType = "MSG_INSERT_COMPLETE";
			break;

		case MSG_NO_REQUIRED_ID:
			msgType = "MSG_NO_REQUIRED_ID";
			break;

		case MSG_CLONE_SUBTREE_START:
			msgType = "MSG_CLONE_SUBTREE_START";
			break;

		case MSG_CLONE_SUBTREE_COMPLETE:
			msgType = "MSG_CLONE_SUBTREE_COMPLETE";
			break;

		case MSG_PRINT_POINTS:
			msgType = "MSG_PRINT_POINTS";
			break;

		case MSG_INSERT_BLOCKED:
			msgType = "MSG_INSERT_BLOCKED";
			break;

		default:
			msgType = "<<<UMO>>>";
			break;
		}
		return msgType;
	}

	private static String labelMySelf() {

		int me = MPI.COMM_WORLD.Rank();
		return labelProcess(me);
	}

	private static String labelProcess(int rank) {

		String label = "";
		switch (rank) {
		case 0:
			label = "<Dispatcher>";
			break;
		case 1:
			label = "<Root>";
			break;

		default:
			label = "<" + rank + ">";
			break;
		}
		return label;
	}

	private static void elaborateMessage(Status status, int[] message) {

		switch (status.tag) {

		case MSG_INSERT_COMPLETE:

			// do nothing and continue
			break;

		case MSG_INSERT_BLOCKED:

			// do nothing and continue
			insert = false;
			break;

		case MSG_NO_REQUIRED_ID:

			// do nothing and continue
			break;

		case MSG_REQUIRED_ID:

			int[] nextID =  new int[1];
			int requiringProcess = status.source;

			if ( nextIDProcess < maxIDProcess ) {

				nextID[0] = nextIDProcess;
				printMessage(DISPATCHER, Direction.send, requiringProcess, 
						nextID, MSG_SENT_ID, "");
				MPI.COMM_WORLD.Send(nextID, 0, nextID.length, MPI.INT, 
						requiringProcess, MSG_SENT_ID);
				nextIDProcess++;

			} else {

				insert = false;
				nextID[0] = NULL_PROCESS_ID;
				printMessage(DISPATCHER, Direction.send, requiringProcess, 
						nextID, MSG_SENT_ID, "reached max num. process: sent NULL PROCESS ID");
				MPI.COMM_WORLD.Send(nextID, 0, nextID.length, MPI.INT, 
						requiringProcess, MSG_SENT_ID);
			}

			break;

		default:

			displayUMO(status);
			break;
		}
	}

	private static void printMessage(int owner, Direction direction, int other,
			int[] point, int tag, String text) {

		String direct = "";
		if ( direction.equals(Direction.send)) {
			direct = " send to ";
		} if ( direction.equals(Direction.isend)) {
			direct = " i-send to ";
		} else {
			direct = " received from ";
		}


		if ( owner == DISPATCHER ) {
			System.out.println(labelProcess(owner) + direct + labelProcess(other) +
					" - " + getMessageType(tag) + " [point:" + point[0] + "] "  + text);	
		}

	}

}
