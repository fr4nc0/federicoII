package it.federicoII.indice;

public class NearestNeighborInWordnet {
	
	String word;
	Double extension;
	
	public NearestNeighborInWordnet(String word, Double extension) {
		super();
		this.word = word;
		this.extension = extension;
	}
	
	
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public Double getExtension() {
		return extension;
	}
	public void setExtension(Double extension) {
		this.extension = extension;
	}
	
	
}
