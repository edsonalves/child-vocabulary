package WordOntoMain;


public class Utils {

  public static void printHierarchy(Category Category, int level) {
		  
    if( Category == null || Category.getLabel() == null ) {
		    	
      return;
		      
    }
		    
    for(int i = 0; i < level; i++) {
			
      System.out.print( " " );
			  
    }
			
    System.out.print( Category.getLabel() + "\n" );
		    
    if( Category.getChildren() != null ) {
		    	
      for(Category child: Category.getChildren()) {
		      
        Utils.printHierarchy( child, level + 2 );
		      
      }
		      
    }
		    
  }
		  
  public static void mergeCategories(Category category1, Category category2) {

    if( category1.getLabel() == null ) {
		    	
      category1.setLabel( category2.getLabel() );
		      
      category1.setChildren( category2.getChildren() );
		      
      category1.setFather( category2.getFather() );
		      
      category1.setLevel( category2.getLevel() );
		      
    } else {
		    	
      if( category1.getLabel().equalsIgnoreCase( category2.getLabel() ) ) {
		    	  
    	if( category1.getChildren() == null || category2.getChildren() == null ) {
		    		
    	  return;
		    	  
    	}

    	for(int i = 0; i < category2.getChildren().size(); i++){

          boolean isFound = false;

          Category childCategory2 = category2.getChildren().get( i );
          Category childCategory1 = null;
		          
          for(int j = 0; j < category1.getChildren().size(); j++) {
		        	 
        	childCategory1 = category1.getChildren().get( j );
		      	
            if( childCategory2.getLabel().equalsIgnoreCase( childCategory1.getLabel() ) ) {
		        	  
        	  isFound = true;
		        	
        	  break;
		          
            }
		        
          } // FOR CHILDREN CATEGORY 2

          if( isFound ) {
		        
            Utils.mergeCategories( childCategory1, childCategory2 );
		        
          } else {
		        	 
            category1.addChild( childCategory2  );

          }
		          
        } // FOR CHILDREN CATEGORY 1
		        
      } else {
		    	  
       category1.addChild( category2 );
		        
      }
		      
    }
		    
  }

  public static Category getTopFatherCategory(Category category2) {

    Category topFatherCategory = category2;  
		  
    while( topFatherCategory.getFather() != null ) {
		    	
      topFatherCategory = topFatherCategory.getFather();
		      
    }
		    
    return topFatherCategory;
		        
  }
}
