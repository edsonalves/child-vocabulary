package WordOntoMain;

import java.util.ArrayList;
import java.util.List;

public class Category {

	  // Attributes
		
	  private String label;
	  private int level;
	  private Category father;
	  private List<Category> children;
	  
	  
	  // Constructor
	  
	  public Category() {

	  }

	  
	  // Methods

	  public String getLabel() {
		
	    return label;

	  }

	  public void setLabel(String label) {
		
	    this.label = label;

	  }

	  public int getLevel() {
		
	    return level;

	  }

	  public void setLevel(int level) {
		
	    this.level = level;

	  }

	  public Category getFather() {
		
	    return father;

	  }

	  public void setFather(Category father) {
		
	    this.father = father;

	  }

	  public List<Category> getChildren() {
		
	    return children;

	  }

	  public void setChildren(List<Category> children) {
		
	    this.children = children;

	  }
	  
	  public void addChild(Category testeCategory) {
		  
	    if( this.children == null ) {
	    	
	      this.children = new ArrayList<Category>();
	      
	    }
	    
	    this.children.add( testeCategory );
	    
	  }
	  
	}

