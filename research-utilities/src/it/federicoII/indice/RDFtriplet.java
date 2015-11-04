package it.federicoII.indice;

public class RDFtriplet {
	private String subject;
	private String object;
	private String predicate;
	private String id;
	private String requisiteId;
	
	
	
	
	@Override
	public String toString() {
		//return id + " <" + subject + ", " + object + ", " + predicate + ">";
		return id ;
	}

	public RDFtriplet(String requisiteId, String id, String subject, String predicate, String object ) {
		super();
		this.subject = subject;
		this.object = object;
		this.predicate = predicate;
		this.id = id;
		this.requisiteId = requisiteId;
	}
	
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getObject() {
		return object;
	}
	public void setObject(String object) {
		this.object = object;
	}
	public String getPredicate() {
		return predicate;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRequisiteId() {
		return requisiteId;
	}
	public void setRequisiteId(String requisiteId) {
		this.requisiteId = requisiteId;
	}
	
	
}
