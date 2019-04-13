package poly2tri_magic;

public class SplayTree {
	
	private node root;
	private long      size;    
	
	public SplayTree(){
		root = null;
		size = 0;
	}
	 
	public SplayTree(SplayTree rhs){
		SplayTree st = SplayTree.clone(rhs);
		root = st.root;
		size = st.size;
	}
	
	void makeEmpty(){
		root = null;
		size = 0;
	}
	
	boolean isEmpty(){
		return size == 0;
	}
	
	long size(){ 
		return size; 
	}
	
	public node root() { 
		return root;
	}    
	      
	//void SplayTree<T, KeyType>::Insert( const T & x )
	public void insert(item x){
		
	     node newNode = new node();
	     newNode.item=x;
	    
	     if( root == null ){
	          root = newNode; size++; 
	     } else {
	    	 Comparable keys = x.value();
	    	 while (true){ // to avoid recursion	    		
	    		 root = splay( keys, root );
	    		 Comparable rootk = root.keyValue();
	    		 if( keys.compareTo(rootk) < 0 ){
	    			 newNode.left = root.left;
	    			 newNode.right = root;
	    			 root.left = null;
	    			 root = newNode; 
	    			 size++;
	    			 return;
	    		 }
	    		 else if( keys.compareTo(rootk) > 0 ){	              
	    			 newNode.right = root.right;
	    			 newNode.left = root;
	    			 root.right = null;
	    			 root = newNode;
	    			 size++;
	    			 return;
	    		 }
	    		 else{
	    			 //slight incresed the keyvalue to avoid duplicated keys
	    			 //try to insert again (do the loop)
	    			 x.increase_value(1.0e-10);
	    			 keys = x.value();		     
	    		 }         
	    	 }
	     }
	}
		
	//void Delete( KeyType keys, BTreeNode<T, KeyType>* &res);
	public node delete(Comparable keys){
		
		node newTree;

	    root = splay( keys, root );
	    
	    if( !(root.keyValue()).equals(keys) ){ 
	    	return null;
	    } // Item not found; do nothing
	 
	    node result = root;

	    if( root.left == null )
	         newTree = root.right;
	    else{
	        // Find the maximum in the _left subtree
	        // Splay it to the root; and then attach _right child
	        newTree = root.left;
	        newTree = splay( keys, newTree );
	        newTree.right = root.right;
	    }

	    root = newTree;
	    size--; 
	    return result;
	}
		
	/**
	 * Returns deleted max. BTreeNode.
	 * @return
	 */
	public node deleteMax(){
		if(isEmpty()) return null;
		 
		double keys=Double.MAX_VALUE;
		root = splay( keys, root );
		 
		node maxResult = root;
		  
		node newTree;
		if( root.left == null ) newTree = root.right;
		else{
		    newTree = root.left;
		    newTree = splay( keys, newTree );
		    newTree.right = root.right;
		}
		size--;
		root = newTree;
		return maxResult;
	}

	//const SplayTree & operator=( const SplayTree & rhs );
	public static SplayTree clone(SplayTree rhs){
		SplayTree st = new SplayTree();
		st.root = rhs.clone(rhs.root);
		st.size = rhs.size;
		return st;
	}
	
	public node find(Comparable keys){
	      if (isEmpty()) return null;
	      root = splay(keys, root);
	      if(!root.keyValue().equals(keys) ){ return null; }
	      else return root;
	}
	
	/**	
	 *	Find the maximum node smaller than or equal to the given key.
	 *	This function specially designed for polygon Triangulation to
	 *	find the direct left edge at event vertex;
	 */
	//void FindMaxSmallerThan( const KeyType& keys, BTreeNode<T, KeyType>* &res);
	public node findMaxSmallerThan(Comparable keys){
	      if(isEmpty()) return null;
	      
	      root = splay( keys, root );
	      
	      if( root.data().value().compareTo(keys) < 0) return root; 
	      else if(root.left != null) 
	      {       
		      node result = root.left;
		      while(result.right != null) result = result.right;
		      return result;
	      }
	      else 
	      {
		      assert(false);
		      return null;
	      }
	}
	
	      
	//void InOrder( void(*Visit)(BTreeNode<T,KeyType>*u, double y), double y)
	public void inOrder(action action, double y){ 
		inOrder(action, root, y); 
	}

		
	//height of root
	public int height(){ 
		return height(root);
	}  
	  
//	Height of subtree t;
	public int height(node t){
		if (t == null) return 0;
		int lh = height(t.left);
		int rh = height(t.right);
		   
		return (lh>rh)?(++lh):(++rh);   
	}
	
	
	public node left(node node){
		return node.left(); 
	}
	
	public node right(node node){
		return node.right(); 
	}     

	private node clone( node t ){
		if (t == null)
				return null;
		// TODO ... find out what that means
		if( t == t.left )  // Cannot test against NULLNode!!!
	            return null;
	         
	    return new node( t.item, clone( t.left ), clone( t.right ) ); 
	}

	private void inOrder(action action, node t, double y){
		if(t != null){
			inOrder(action, t.left, y);
			action.action(t, y);
			inOrder(action, t.right, y);
		}
	}
	      
	
    // Tree manipulations
    
    //void rotateWithLeftChild( BTreeNode<T, KeyType> * & k2 ) const;
	private node rotateWithLeftChild(node k2){
		node k1 = k2.left;
	    k2.left = k1.right;
	    k1.right = k2;
	    return k1;
	}
	
	//void rotateWithRightChild( BTreeNode<T, KeyType> * & k1 ) const;
	private node rotateWithRightChild(node k1){
		node k2 = k1.right;
	    k1.right = k2.left;
	    k2.left = k1;
	    return k2;
	}
	
	private static node header = new node();
	
	/**
	 * Internal method to perform a top-down splay.
	 * x is the key of target node to splay around.
     * t is the root of the subtree to splay.
	 * @param keys
	 * @param t
	 * @return
	 */
	//void splay( KeyType keys, BTreeNode<T, KeyType> * & t ) const;
	private node splay(Comparable keys, node t){
		node _leftTreeMax, _rightTreeMin;
	    
	    header.left = header.right = null;
	    _leftTreeMax = _rightTreeMin = header;

	    for( ; ; ){
	        Comparable rKey = t.keyValue();
	        if( keys.compareTo(rKey) < 0 ){
	        	if(t.left == null) break;
	            if( keys.compareTo(t.left.keyValue()) < 0 ) t = rotateWithLeftChild(t);
	            if( t.left == null ) break;
	               
	            // Link Right
	            _rightTreeMin.left = t;
	            _rightTreeMin = t;
	            t = t.left;
	        }
	        else if( keys.compareTo(rKey) > 0){
	        	if( t.right == null ) break;
	            if( keys.compareTo(t.right.keyValue()) > 0) t = rotateWithRightChild(t);
	            if( t.right == null ) break;    
	 
	            // Link Left
	            _leftTreeMax.right = t;
	            _leftTreeMax = t;
	            t = t.right;
	        } else break;
	   }

	   _leftTreeMax.right = t.left;
	   _rightTreeMin.left = t.right;
	   t.left = header.right;
	   t.right = header.left;
	   return t;
	}
	
}
