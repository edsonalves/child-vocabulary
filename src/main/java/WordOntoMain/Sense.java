package WordOntoMain;

import java.util.HashSet;
import java.util.Set;

import edu.mit.jwi.item.ISynset;

public class Sense {

	// VARIÁVEIS
	
	private String id;
	private String individualID;
	private String description;
	private String label;
	private double score;
	private Set<String> relatedTerms;
	private String source;
	private ISynset iSynset;
	
	// CONSTRUTOR
	
	public Sense(){
		
	}

	
	// MÉTODOS
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getIndividualID() {
		return individualID;
	}

	public void setIndividualID(String individualID) {
		this.individualID = individualID;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public Set<String> getRelatedTerms() {
		return relatedTerms;
	}

	public void setRelatedTerms(Set<String> relatedTerms) {
		this.relatedTerms = relatedTerms;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public ISynset getiSynset() {
		return iSynset;
	}

	public void setiSynset(ISynset iSynset) {
		this.iSynset = iSynset;
	}
	
	public boolean addRelatedTerm(String relatedTerm) {
		  
	    if( relatedTerm == null ) {
	    	
	      return false;
	      
	    }
	    
	    if( this.relatedTerms == null ) {
	    	
	      this.relatedTerms = new HashSet<String>();
	      
	    }
	    
	    return this.relatedTerms.add( relatedTerm );
	    
	    
	  }
	
}
