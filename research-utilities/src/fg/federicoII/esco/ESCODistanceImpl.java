package fg.federicoII.esco;

import java.util.ArrayList;

import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;

import net.vja2.research.distancemetrics.IDistanceMetric;

public class ESCODistanceImpl implements IDistanceMetric<EscoNode> {

	
	private static DefaultTreeModel taxonomy;
	
	
	
	
	public ESCODistanceImpl( DefaultTreeModel taxonomy ) {
		this.taxonomy = taxonomy;
		
	}

	
	@Override
	public double distance(EscoNode a, EscoNode b) {

		return (1 - WuPalmer(a, b) );
	}

	public Double WuPalmer(EscoNode a, EscoNode b) {

		if ( a.equals(b) ) {
			return (double) 1;
		}

		TreeNode[] pathA =  taxonomy.getPathToRoot(a);
		TreeNode[] pathB = taxonomy.getPathToRoot(b);
		
		TreeNode lcs = getLCS(pathA, pathB);
		TreeNode[] pathLCS = taxonomy.getPathToRoot(lcs);
		
		int lengthLCS = pathLCS.length;
		int lengthA = pathA.length;
		int lengthB = pathB.length;

		Double WP = (double) (2 * (double)lengthLCS / ((double)lengthA + (double)lengthB)); 

		//printPath(pathA);
		//printPath(pathB);
		//System.out.println("a: " + a.getPreferredTerm() + " b: " + b.getPreferredTerm() + " LCS: " + ((EscoNode) lcs).getPreferredTerm() );
		//System.out.println("height a: " + lengthA + " height b: " + lengthB  + " height LCS: " + lengthLCS );
		//System.out.println(WP);
		
		return WP;
	}

	private void printPath(TreeNode[] path) {

		for ( int i = 0; i < path.length; i++ ) {
			System.out.println( ((EscoNode)path[i]).getPreferredTerm() );
		}
		
	}


	private TreeNode getLCS(TreeNode[] pathA, TreeNode[] pathB) {
		
		int minPathLen = Math.min(pathA.length, pathB.length);
		TreeNode currentLCS = null;
		
		for ( int i = 0; i < minPathLen; i++ ) {
			
			if ( ((EscoNode) pathA[i]).getUri().equals(((EscoNode) pathB[i]).getUri()) )  {
			
				currentLCS = pathA[i];
			
			} else {
				
				break;
			}
		}
		
		
		return currentLCS;
	}


	

	

}
