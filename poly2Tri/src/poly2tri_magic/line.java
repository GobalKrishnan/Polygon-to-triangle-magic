package poly2tri_magic;


public class line implements item {
	
	 /**
	  * Was unsigned int!
	  * id of a line segment;
	  */
	 protected int _id = -1;
	 
	 /**
	  * two end points;
	  */
     protected point[] _endp = {null, null};      
     
     /**
      * type of a line segement, input/insert
      * Type...
      */
     protected int _type = Poly2TriUtils.UNKNOWN;
     
     /**
      * key of a line segment for splay tree searching
      */
     protected double _key = 0;
     
     /**
      * Was unsigned int!
      * helper of a line segemnt
      */
     protected int _helper = -1;      
		  
	 public line(){
		 for(int i=0; i<2; i++) _endp[i] = null;
		 _id=0;
     }
	
	 public line(point ep1, point ep2, int iType){
		 _endp[0]=ep1;
	     _endp[1]=ep2;
	     _id=(int)++Poly2TriUtils.l_id;
	     _type = iType;
	 }
	 
	 public line(line line){
		 this._id = line._id;
	     this._endp[0] = line._endp[0];
	     this._endp[1] = line._endp[1];
	     this._key=line._key;
	     this._helper=line._helper; 
	 }	 

	 public int id(){
		 return _id; 
	 }
	      
	 public point endPoint(int i){ 
		 return _endp[i]; 
	 }
	 
	 public int type(){
		 return _type; 
	 }
	 
	 public Comparable value(){
		 return _key; 
	 }
	 
	 public void setKeyValue(double y){
		 if(_endp[1].y == _endp[0].y )  
			 _key = _endp[0].x < _endp[1].x ? _endp[0].x : _endp[1].x;
		 else  
			 _key = (y - _endp[0].y) * (_endp[1].x - _endp[0].x) / (_endp[1].y - _endp[0].y) + _endp[0].x; 	 
	 }
	      
	 /**
	  * reverse a directed line segment; reversable only for inserted diagonals
	  */
	 public void reverse(){
	 	assert(_type==Poly2TriUtils.INSERT); 
	 	point tmp=_endp[0]; 
		_endp[0]=_endp[1];
		_endp[1]=tmp;
	 }

	  
	 /**
	  * set and return helper of a directed line segment
	  */
	 public void setHelper(int i){ 
		 _helper=i; 
	 }
	 
	 public int helper(){
		 return _helper; 
	 }

	 public String toString(){
		 StringBuffer sb = new StringBuffer();
		 sb.append("Linebase(");
		 sb.append("ID = "+_id);
		 sb.append(", "+Poly2TriUtils.typeToString(_type));
		 sb.append(", [");
		 sb.append(_endp[0]);
		 sb.append(", ");
		 sb.append(_endp[1]);
		 sb.append("], type = "+_type);
		 sb.append(", Value ="+value());
		 return sb.toString();
	 }
	     
	/**
	 * slightly increased the key to avoid duplicated key for searching tree. 
	 */
	public void increase_value(double delta) {
		_key += delta;
	}

}
