package poly2tri_magic;

public class node {
	
    protected item item     = null;
    protected node     left     = null;
    protected node     right    = null;
    protected boolean       visited  = false;
	  
    public node(){
    }
    
    public node(item data, node left, node right){
    	item = data;
    	left = left;
    	right = right;
    }
    	
    public item data(){ return item; }

    public node left(){ return left; }
    
    public node right(){ return right; }
    
    void setVisited(boolean visited){ visited = visited; }
    
    boolean getVisited(){ return visited; }
    
    Comparable keyValue(){ return item.value(); }
 	
}
