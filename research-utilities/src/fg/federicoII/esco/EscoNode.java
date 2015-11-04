package fg.federicoII.esco;

import javax.swing.tree.DefaultMutableTreeNode;

public class EscoNode extends DefaultMutableTreeNode  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String uri;
	private String preferredTerm;
	private boolean hasParent;
	

	public EscoNode(String uri) {
		
		this.uri = uri;
		this.preferredTerm = "root";
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getPreferredTerm() {
		return preferredTerm;
	}

	public void setPreferredTerm(String preferredTerm) {
		this.preferredTerm = preferredTerm;
	}

	public boolean isHasParent() {
		return hasParent;
	}

	public void setHasParent(boolean hasParent) {
		this.hasParent = hasParent;
	}

	
	
	
	
	
	
}
