package avlTrees.copy;


// Basic node stored in AVL trees
// Note that this class is not accessible outside
// of package DataStructures

public class AvlNode
{
        // Constructors
    AvlNode( Comparable theElement )
    {
        this( theElement, null, null );
    }

    AvlNode( Comparable theElement, AvlNode lt, AvlNode rt )
    {
        element  = theElement;
        left     = lt;
        right    = rt;
        height   = 0;
    }

        // Friendly data; accessible by other package routines
    Comparable element;      // The data in the node
    AvlNode    left;         // Left child
    AvlNode    right;        // Right child
    int        height;       // Height
    
    
    
    public AvlNode getLeft() {
		return left;
	}

	public AvlNode getRight() {
		return right;
	}

	public String getLabelNode() {
		String label = "";
		/*
		if ( this.isLeaf() ) {
			label = "bucket: " + this.getBucket() + " h: " + this.height;
		} else {
			label = "split: " + this.splitValue + " h: " + this.height;
		}
		*/
		label = " split: " + element + " h: " + height;
		return label ;
	}
}
