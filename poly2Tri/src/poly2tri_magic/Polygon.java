package poly2tri_magic;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;



/**
 * Merged with BDMFile (Boundary Mesh File)
 */
public class Polygon {
	
	/**
	 * Was unsigned int!
	 * Number of contours.
	 */
	protected int _ncontours = 0;
	
	/**
	 * vector<unsigned int>    _nVertices;   // 
	 */     
    protected int[]	_nVertices = null;
    
    /**
     * typedef map<unsigned int, Pointbase*>           PointbaseMap;
     * all vertices ... is needed as normal array,
     * map in C++ code probably because od adding into map
     * ... see _pointsKeys
     */
	protected HashMap _points = new HashMap();
	
	/**
	 * Initialized in initialize() method ... number of points
	 * doesn't change during iteration - all iteration in C++
	 * code are done from smaller to bigger ... HashMap.keySet().iterator()
	 * isn't returning keys in natural order.
	 */
	protected int[] _pointsKeys = null;
	
	/**
	 * typedef map<unsigned int, Linebase*>            LineMap;
	 * all edges
	 */
	protected HashMap _edges = new HashMap();
	
	/**
	 * See _pointsKeys ... same for _edges.
	 * 				---{ Today, it's you }---
	 * 					Terry Pratchett's Death
	 * Right now I'm not sure wether number of edges can't change...
	 * ... better call initializeEdgesKeys() all the time ;)
	 */
	protected int[] _edgesKeys = null;
	
	/**
	 * typedef priority_queue<Pointbase> PQueue; ... use PointbaseComparatorCoordinatesReverse! (Jakub Gemrot)
	 * priority queue for event points
	 */
	private PriorityQueue _qpoints = new PriorityQueue(30, new PointbaseComparatorCoordinatesReverse());
	
	/**
	 * typedef SplayTree<Linebase*, double>            EdgeBST;
	 * edge binary searching tree (splaytree)
	 */
	private SplayTree _edgebst = new SplayTree(); 

	/**
	 * typedef list<Monopoly>          Monopolys;
	 * typedef list<unsigned int>      Monopoly;
	 * all monotone polygon piece list;
	 */ 
	private ArrayList _mpolys = new ArrayList();
	
	/**
	 *  all triangle list; 
	 *	typedef list<Triangle>                          Triangles;
	 *	typedef vector<unsigned int>                    Triangle;
	 */
	private ArrayList _triangles = new ArrayList();                          

	/**
	 * typedef map<unsigned int, set<unsigned int> >   AdjEdgeMap;
	 * data for monotone piece searching purpose;
	 */
	 private HashMap _startAdjEdgeMap = new HashMap();
	 
	 /**
	  * typedef map<unsigned int, Linebase*>            LineMap;
	  * added diagonals to partition polygon to
	  * monotont pieces, not all diagonals of
	  * given polygon
	  */
	 private HashMap _diagonals = new HashMap(); 
		             		
	 /**
	  * debug option;
	  */
	
	 /**
	  * This is used to change key of all items in SplayTree.
	  */
	 private UpdateKey updateKey = new UpdateKey();

	 /**
	  * If _debug == true, file with this name will be used to log the messages.
	  */
	 private String _debugFileName = "polygon_triangulation_log.txt";
	
	 public HashMap points(){
		 return _points; 
	 }
	
	 public HashMap edges(){
		 return _edges; 
	 }
	 
	 /**
	  * For params see contructor Polygon(int, int[], double[][])
	  * @param numContours
	  * @param numVerticesInContures
	  * @param vertices
	  * 
	  * ---{ CLEAR }---
	  */
	 private void initPolygon(int numContours, int[] numVerticesInContours, int[][] vertices){
		 int i, j; int nextNumber = 1;
		 
		 _ncontours = numContours;
		 _nVertices = new int[_ncontours];
		 for (i = 0; i < numContours; ++i){
			 for (j = 0; j < numVerticesInContours[i]; ++j){
				_points.put(nextNumber, new point(nextNumber, vertices[nextNumber-1][0], vertices[nextNumber-1][1], Poly2TriUtils.INPUT));
				++nextNumber;
			 } 
		 }
		 _nVertices[0] = numVerticesInContours[0];
		 for (i = 1; i < _ncontours; ++i){
			 _nVertices[i] = _nVertices[i-1] + numVerticesInContours[i];
		 }
		 i = 0; j = 1; 
		 int first = 1; 
		 line edge;
		 
		 while (i < _ncontours){
			 for( ; j+1 <= _nVertices[i]; ++j){
				 edge = new line((point)_points.get(j), (point)_points.get(j+1), Poly2TriUtils.INPUT); 
				_edges.put(Poly2TriUtils.l_id, edge);
			 }
			 edge = new line((point)_points.get(j), (point)_points.get(first), Poly2TriUtils.INPUT); 
			_edges.put(Poly2TriUtils.l_id, edge);
			 
			 j = _nVertices[i]+1;
			 first = _nVertices[i]+1;
			 ++i;
		 }	 
		 Poly2TriUtils.p_id = _nVertices[_ncontours-1];
	 }

	 /**
	  * numContures == number of contures of polygon (1 OUTER + n INNER)
	  * numVerticesInContures == array numVerticesInContures[x] == number of vertices in x. contures, 0-based
	  * vertices == array of vertices, each item of array contains doubl[2] ~ {x,y}
	  * First conture is OUTER -> vertices must be COUNTER CLOCKWISE!
	  * Other contures must be INNER -> vertices must be CLOCKWISE!
	  * Example:
	  * 	numContures = 1 (1 OUTER CONTURE, 1 INNER CONTURE)
	  * 	numVerticesInContures = { 3, 3 } // triangle with inner triangle as a hol
	  *     vertices = { {0, 0}, {7, 0}, {4, 4}, // outer conture, counter clockwise order
	  *                  {2, 2}, {2, 3}, {3, 3}  // inner conture, clockwise order
	  *     		   }
	  * @param numContures number of contures of polygon (1 OUTER + n INNER)
	  * @param numVerticesInContures array numVerticesInContures[x] == number of vertices in x. contures, 0-based
	  * @param vertices array of vertices, each item of array contains doubl[2] ~ {x,y}
	  */
	 Polygon(int numContures, int[] numVerticesInContures, int[][] vertices){
		 Poly2TriUtils.initPoly2TriUtils();
		 initPolygon(numContures, numVerticesInContures, vertices);
		 initializate();
	 }
	 
	
	 public point getPoint(int index){
		 return (point)_points.get(index);
	 }
	 
	 public line getEdge(int index){
		 return (line)_edges.get(index);
	 }
	 
	 public point qpointsTop(){
		 return (point)_qpoints.peek();
	 }
	 
	 public point qpointsPop(){
		 return (point)_qpoints.poll();
	 }

	 public void destroy(){
	 }
	 
	 public boolean is_exist(double x, double y){
		 Iterator iter = _points.keySet().iterator();
		 point pb;
		 while (iter.hasNext()){
			 pb = getPoint((Integer)iter.next());
			 if ((pb.x == x) && (pb.y == y)) return true;
		 }
		 return false;
	 }
	 
	 /**
	  * return the previous point (or edge) id for a given ith point (or edge);
	  * 
	  * was all UNSIGNED (ints)
	  */ 	
	 private int prev(int i){
		 int j = 0, prevLoop = 0, currentLoop = 0;
		  
		  while ( i > _nVertices[currentLoop] ) {
		     prevLoop=currentLoop;
		     currentLoop++;     
		  }
		 
		  if( i==1 || (i==_nVertices[prevLoop]+1) ) j=_nVertices[currentLoop];
		  else if( i <= _nVertices[currentLoop] ) j=i-1;

		  return j;
	 }
	 
	 /**
	  * return the next point (or edge) id for a given ith point (or edge);
	  * was all UNSIGNED!
	  */ 
	 private int next(int i){
		 int j = 0, prevLoop = 0, currentLoop = 0;

		 while (i > _nVertices[currentLoop]){
			 prevLoop=currentLoop;	   
			 currentLoop++;  
		 }

		 if( i < _nVertices[currentLoop] ) j=i+1;
		 else 
			 if ( i==_nVertices[currentLoop] ){
				 if( currentLoop==0) j=1;
				 else j=_nVertices[prevLoop]+1;
			 }

		 return j;
	 }
	 
	 /**
	  * rotate input polygon by angle theta, not used;
	  */ 
	 private void rotate(double theta){
	    for(int i = 0; i < _pointsKeys.length; ++i)
	    	(getPoint(_pointsKeys[i])).rotate(theta);
	 }
	 
	 private int[] getSorted(Set s){
		 Object[] temp = s.toArray();
		 int[] result = new int[temp.length];
		 for (int i = 0; i < temp.length; ++i){
			 result[i] = ((Integer)temp[i]).intValue();
		 }
		 Arrays.sort(result);
		 return result;
	 }
	 
	 private void initializePointsKeys(){
		 _pointsKeys = getSorted(_points.keySet());		 
	 }
	 
	 private void initializeEdgesKeys(){
		 _edgesKeys = getSorted(_edges.keySet());		 
	 }
	 
	 private Set getSetFromStartAdjEdgeMap(int index){
		 Set s = (Set)_startAdjEdgeMap.get(index);
		 if (s != null) return s;
		 s = new HashSet();
		 _startAdjEdgeMap.put(index, s);
		 return s;
	 }
	 
	 /**
	  * polygon initialization;
	  * to find types of all polygon vertices;
	  *	create a priority queue for all vertices;
	  *	construct an edge set for each vertex (the set holds all edges starting from 
	  * the vertex, only for loop searching purpose). 
	  * 
	  * ---{ CLEAR }---
	  */
	 private void initializate(){
		 initializePointsKeys();
		 
		 int id, idp, idn; 
		 point p, pnext, pprev; // was Pointbase p, pnext, pprev; ... COPY CONTRUCTOR whenever =
		 double area;
		 
		 for (int i = 0; i < _pointsKeys.length; ++i){
			id  = _pointsKeys[i];
		    idp = prev(id);
		    idn = next(id);
		    
		    p     = getPoint(id);
		    pnext = getPoint(idn);		    
		    pprev = getPoint(idp);
		   
		    if( (p.compareTo(pnext) > 0) && (pprev.compareTo(p) > 0))
			   p.type = Poly2TriUtils.REGULAR_DOWN;
		    else 
		    if ( (p.compareTo(pprev) > 0) && (pnext.compareTo(p) > 0) )
			   p.type = Poly2TriUtils.REGULAR_UP;
		    else{
		       area = Poly2TriUtils.orient2d(new double[]{pprev.x, pprev.y},
		    		                         new double[]{p.x,     p.y},
		    		                         new double[]{pnext.x, pnext.y});

		       if( (pprev.compareTo(p) > 0) && (pnext.compareTo(p) > 0)) 
		    	   p.type = (area > 0) ? Poly2TriUtils.END   : Poly2TriUtils.MERGE;
		       if( (pprev.compareTo(p) < 0) && (pnext.compareTo(p) < 0)) 
		    	   p.type = (area > 0) ? Poly2TriUtils.START : Poly2TriUtils.SPLIT;		       
		     }
		    
		    // C++ code: _qpoints.push(*(it.second));
		    // must use copy constructor!
		    _qpoints.add(new point(p));

		    getSetFromStartAdjEdgeMap(id).add(id);
		 }
	 }
	 
	 /**
	  * Add a diagonal from point id i to j
	  * 
	  * C++ code: was all unsigned (i,j)
	  */
	 private void addDiagonal(int i, int j){
		 int type = Poly2TriUtils.INSERT;
		 
		 line diag = new line(getPoint(i), 
				                      getPoint(j),
				                      type);
		 _edges.put(diag.id(), diag);

		 getSetFromStartAdjEdgeMap(i).add(diag.id());
		 getSetFromStartAdjEdgeMap(j).add(diag.id());

		 _diagonals.put(diag.id(), diag);     

	 }
	 
	 /**
	  * handle event vertext according to vertex type;
	  * Handle start vertex
	  * 
	  * C++ code: was all UNSIGNED
	  */
	 private void handleStartVertex(int i){
		
		 double y = ((point)_points.get(i)).y;	
		 
		 _edgebst.inOrder(updateKey, y); // ya ... some special things happens ... see Linebase.setKeyValue()

		 line edge = getEdge(i);
		 edge.setHelper(i);
		 edge.setKeyValue(y);
		 
		 _edgebst.insert(edge);

		
	 }
	 
	 /**
	  * handle event vertext according to vertex type;
	  * Handle end vertex
	  * 
	  * C++ code: param i was unsigned
	  */
	 private void handleEndVertex(int i){
		 double y = getPoint(i).y;
		 
		 _edgebst.inOrder(updateKey, y);

		 int previ = prev(i);
		 line edge = getEdge(previ);		 
		 int helper = edge.helper();

		 if(getPoint(helper).type == Poly2TriUtils.MERGE)
			 addDiagonal(i, helper);
		 _edgebst.delete(edge.value());

	
	 }
	 
	 /**
	  * handle event vertext according to vertex type;
	  * Handle split vertex
	  * C++ code: i was unsigned, helper was unsigned
	  */
	 private void handleSplitVertex(int i){
		 point point = getPoint(i);
		 double x = point.x, y = point.y;
		 
		 _edgebst.inOrder(updateKey, y);

		 node leftnode = _edgebst.findMaxSmallerThan(x);		
		 line  leftedge = (line)leftnode.data();

		 int helper = leftedge.helper();
		 addDiagonal(i, helper);

		

		 leftedge.setHelper(i);
		 line edge = getEdge(i);
		 edge.setHelper(i);
		 edge.setKeyValue(y);
		 _edgebst.insert(edge);  
	 }
	 
	 /**
	  * handle event vertext according to vertex type;
	  * 		Handle merge vertex
	  * C++ code: i was unsigned, previ + helper also unsigned 
	  */
	 private void handleMergeVertex(int i){
		 point point = getPoint(i);
		 double x = point.x, y = point.y;
		 
		 _edgebst.inOrder(updateKey, y);

		 int previ = prev(i);
		 line previEdge = getEdge(previ);
		 int helper = previEdge.helper();
		 
		 point helperPoint = getPoint(helper);
		 
		 if (helperPoint.type == Poly2TriUtils.MERGE) 
			 addDiagonal(i, helper);
		 
		 _edgebst.delete(previEdge.value());
		 
		

		 node leftnode = _edgebst.findMaxSmallerThan(x);		 
		 line  leftedge = (line)leftnode.data();

		 helper = leftedge.helper();
		 helperPoint = getPoint(helper);
		 if(helperPoint.type == Poly2TriUtils.MERGE) 
			 addDiagonal(i, helper);

		 leftedge.setHelper(i);
		 
		 
	 }
	 
	 /**
	  * handle event vertext according to vertex type;
	  * Handle regular down vertex
	  * C++ code: i was unsigned, previ + helper also unsigned
	  */
	 private void handleRegularVertexDown(int i){
		 point point = getPoint(i);
		 
		 double y = point.y;
		 
		 _edgebst.inOrder(updateKey, y);

		 int previ  = prev(i);
		 
		 line previEdge = getEdge(previ);
		 
		 int helper = previEdge.helper();
		 
		 point helperPoint = getPoint(helper);
		 
		 if(helperPoint.type == Poly2TriUtils.MERGE)
			 addDiagonal(i, helper); 

		 _edgebst.delete(previEdge.value());
		 
		 line edge = getEdge(i);
		 edge.setHelper(i);
		 edge.setKeyValue(y);
		 _edgebst.insert(edge);

		 
	 }
	 
	 /**
	  * handle event vertext according to vertex type;
	  * Handle regular up vertex
	  * C++ code: i was unsigned, helper also unsigned
	  */
	 private void handleRegularVertexUp(int i){
		 point point = getPoint(i);
		 
		 double x = point.x, y = point.y;
		 
		 _edgebst.inOrder(updateKey, y);

		 node leftnode = _edgebst.findMaxSmallerThan(x);
		 
		 line leftedge = (line)leftnode.data();

		 int helper=leftedge.helper();
		 point helperPoint = getPoint(helper);
		 if(helperPoint.type == Poly2TriUtils.MERGE) addDiagonal(i, helper);
		 leftedge.setHelper(i);

		
	 }
	 
	 /**
	  * main member function for polygon triangulation;
	  * partition polygon to monotone polygon pieces
	  * C++ code: id was unsigned
	  * @return success
	  */ 	
	 public boolean partition2Monotone(){
		 if (qpointsTop().type != Poly2TriUtils.START){ 
			 System.out.println("Please check your input polygon:\n1)orientations?\n2)duplicated points?\n");
			 System.out.println("poly2tri stopped.\n");
			 return false;
		 }

		 int id;
		 while(_qpoints.size() > 0){
			 point vertex= qpointsPop();
			 
			 id = vertex.id;

			

			 switch(vertex.type){
			 	case Poly2TriUtils.START:        handleStartVertex(id);       break;
			 	case Poly2TriUtils.END:          handleEndVertex(id);         break;
			 	case Poly2TriUtils.MERGE:        handleMergeVertex(id);       break;
			 	case Poly2TriUtils.SPLIT:        handleSplitVertex(id);       break;
			 	case Poly2TriUtils.REGULAR_UP:   handleRegularVertexUp(id);   break;
			 	case Poly2TriUtils.REGULAR_DOWN: handleRegularVertexDown(id); break;
			 	default:
			 		System.out.println("No duplicated points please! poly2tri stopped\n");
			 		return false;                         
			 }
		 }
		 return true;
	 }
	 
	 /**
	  * angle ABC for three given points, for monotone polygon searching purpose;
	  * calculate angle B for A, B, C three given points
	  * auxiliary function to find monotone polygon pieces
	  */
	 private double angleCosb(double[] pa, double[] pb, double[] pc){
		 double dxab = pa[0] - pb[0];
		 double dyab = pa[1] - pb[1];

		 double dxcb = pc[0] - pb[0];
		 double dycb = pc[1] - pb[1];

		 double dxab2 = dxab * dxab;
		 double dyab2 = dyab * dyab;
		 double dxcb2 = dxcb * dxcb;
		 double dycb2 = dycb * dycb;
		 double ab = dxab2 + dyab2;
		 double cb = dxcb2 + dycb2;

		 double cosb = dxab * dxcb + dyab * dycb;
		 double denom = Math.sqrt( ab * cb);

		 cosb/=denom;

		 return cosb;
	 }
	 
	 /**
	  * find the next edge, for monotone polygon searching purpose;
	  * for any given edge, find the next edge we should choose when searching for
	  *	monotone polygon pieces;
	  * auxiliary function to find monotone polygon pieces
	  * C++ code: return unsigned int, eid also unsigned, same for nexte_ccw, nexte_cw
	  */
	 private int selectNextEdge(line edge){
		 int eid= edge.endPoint(1).id;
		 Set edges = getSetFromStartAdjEdgeMap(eid);
		 
		 assert(edges.size() != 0);

		 int nexte=0;
		 
		 if (edges.size() == 1)  
			 nexte = (Integer)(edges.iterator().next());
		 else{
			 int[] edgesKeys = getSorted(edges);
			 
			 int nexte_ccw = 0, nexte_cw = 0;
			 double max = -2.0, min = 2.0; // max min of cos(alfa)
			 line iEdge;
			 			 
			 Iterator iter = edges.iterator(); int it;
			 while (iter.hasNext()){
				 it = (Integer)iter.next();
				 if (it == edge.id()) continue;
				 
				 iEdge = getEdge(it);
				      
				 double[] A = {0,0}, B = {0,0}, C = {0,0};
				 A[0]=edge.endPoint(0).x;        A[1]=edge.endPoint(0).y;
				 B[0]=edge.endPoint(1).x;        B[1]=edge.endPoint(1).y;

				 if (!edge.endPoint(1).equals(iEdge.endPoint(0))) iEdge.reverse();
				 C[0] = iEdge.endPoint(1).x; C[1] = iEdge.endPoint(1).y;

				 double area = Poly2TriUtils.orient2d(A, B, C);
				 double cosb = angleCosb(A, B, C);

				 if     ( area > 0 && max < cosb ) { nexte_ccw = it; max=cosb; }
				 else if( min  > cosb )            { nexte_cw  = it; min=cosb; }
			 }

			 nexte = (nexte_ccw != 0) ? nexte_ccw : nexte_cw;
		 } 

		 return nexte; 
	 }
	 
	 /**
	  * searching all monotone pieces;
	  * C++ code: unsigned - nexte
	  * @return success
	  */
	 public boolean searchMonotones(){
		 int loop = 0;

		 HashMap edges = (HashMap)_edges.clone();
		 
		 ArrayList poly;
		 int[] edgesKeys;
		 int i; int it; line itEdge;
		 
		 point startp, endp; line next;
		 int nexte;

		 while( edges.size() > _diagonals.size() )
		 {
			 loop++;
			 // typedef list<unsigned int> Monopoly;
			 poly = new ArrayList();
			 
			 edgesKeys = getSorted(edges.keySet());
			 
			 it = edgesKeys[0];
			 itEdge = (line)edges.get(it);
			 
			 // Pointbase* startp=startp=it.second.endPoint(0); // ??? startp=startp :-O
			 startp = itEdge.endPoint(0);
			 endp = null;
			 next = itEdge;

			 poly.add(startp.id);

			 for(;;){
				 
				 endp = next.endPoint(1);
				 
				 if(next.type() != Poly2TriUtils.INSERT){ 
					 edges.remove(next.id());
					 getSetFromStartAdjEdgeMap(next.endPoint(0).id).remove(next.id());
				 }
				 if (endp == startp) break; 
				 poly.add(endp.id);


				 nexte = selectNextEdge(next);

				 if(nexte==0){
					 System.out.println("Please check your input polygon:\n");
					 System.out.println("1)orientations?\n2)with duplicated points?\n3)is a simple one?\n");
					 System.out.println("poly2tri stopped.\n");
					 return false;
				 }

				 next = (line)edges.get(nexte);
				 if (!(next.endPoint(0).equals(endp))) next.reverse(); 
			 }


			 _mpolys.add(poly);
		 }
		 return true;
	 }
	 
	 /**
	  * triangulate a monotone polygon piece;
	  *      void triangulateMonotone(Monopoly& mpoly);
	  *      Monopoly == list<Monopoly>   
	  */                        
	 private void triangulateMonotone(ArrayList mpoly){
		 PriorityQueue qvertex = new PriorityQueue(30, new PointbaseComparatorCoordinatesReverse());
		 // is it realy ID?
		 
		 int i, it, itnext; 
		 point point;     // C++ code: Pointbase point;     -> must do copy contructor!
		 point pointnext; // C++ code: Pointbase pointnext; -> must do copy contructor!
		 for(it = 0; it < mpoly.size(); it++) 
		 {
			 itnext = it+1;
			 if (itnext == mpoly.size()) itnext = 0;
			 point     = new point(getPoint((Integer)mpoly.get(it)));
			 pointnext = new point(getPoint((Integer)mpoly.get(itnext)));
			 point.left = (point.compareTo(pointnext) > 0) ? true : false;
			 qvertex.add(point);
		 }

		 Stack spoint = new Stack();
		 
		 for(i = 0; i < 2; i++) spoint.push(qvertex.poll());

		 point topQueuePoint; // C++ code: Pontbase topQueuePoint; -> must do copy constructor!
		 point topStackPoint; // C++ code: Pontbase topStackPoint; -> must do copy constructor!
		 point p1, p2;		  // again copy constructor !
		 point stack1Point, stack2Point; // again copy ...
		 
		 double[] pa = {0,0}, pb = {0,0}, pc = {0,0};
		 double area;
		 boolean left;
		 ArrayList v; // typedef vector<unsigned int> Triangle;
		 
		 // TODO -> doesn't seem that copy constructors are needed here
		 //		    nothing is changing ... we're wasting time here!
		 //			YES ... if you look through the code, there's no need 
		 //				    to create new instances, changed
		 while ( qvertex.size() > 1 ){
			 
			 topQueuePoint = (point)qvertex.peek();
			 topStackPoint = (point)spoint.peek();

			 if(topQueuePoint.left != topStackPoint.left){
				 
				 while ( spoint.size()  > 1 ){
					 
					 p1 = (point)spoint.peek();
					 spoint.pop(); 
					 p2 = (point)spoint.peek();
					 
					 // typedef vector<unsigned int> Triangle;
					 v = new ArrayList(3);
					 v.add(topQueuePoint.id-1);
					 v.add(p1.id-1);
					 v.add(p2.id-1);
					 _triangles.add(v);


				 }
				 spoint.pop();
				 spoint.push(topStackPoint);
				 spoint.push(topQueuePoint);
				 
			 } else {
				 
				 while( spoint.size() > 1 ){
					 
					 stack1Point = (point)spoint.peek();
					 spoint.pop(); 
					 stack2Point = (point)spoint.peek();
					 spoint.push(stack1Point);
					 
					 pa[0] = topQueuePoint.x; pa[1] = topQueuePoint.y;
					 pb[0] = stack2Point.x;   pb[1] = stack2Point.y;
					 pc[0] = stack1Point.x;   pc[1] = stack1Point.y;

					

					 area = Poly2TriUtils.orient2d(pa, pb, pc);
					 left = stack1Point.left;
					 
					 if( (area > 0 && left) || (area < 0 && !left ) ){
						 v = new ArrayList(3);
						 v.add(topQueuePoint.id-1);
						 v.add(stack2Point.id-1);
						 v.add(stack1Point.id-1);
						 _triangles.add(v);
						 spoint.pop();
					 } else 
						 break;
				 }
				 spoint.push(topQueuePoint); 
			 }
			 qvertex.poll();
		 }

		 point lastQueuePoint = (point)qvertex.peek();
		 point topPoint, top2Point; // C++ code ... copy construtors
		 while( spoint.size() !=1 ){
			 topPoint = (point)spoint.peek();
			 spoint.pop();
			 top2Point = (point)spoint.peek();

			 v = new ArrayList(3);
			 v.add(lastQueuePoint.id-1);
			 v.add(topPoint.id-1);
			 v.add(top2Point.id-1);
			 _triangles.add(v);

		 }
	 }
 
	 /**
	  * main triangulation function;
	  * In _triangles is result -> polygon triangles.
	  * @return success
	  */
	 public boolean triangulation(){							  
		 if (!partition2Monotone()) return false;
		 if (!searchMonotones())    return false;
		 
		 for (int i = 0; i < _mpolys.size(); ++i){
			 triangulateMonotone((ArrayList)_mpolys.get(i));
		 }
		 
		 
		 return true;
	 }

	 /**
	  * return all triangles
	  */
	 public ArrayList triangles(){ 
		 return _triangles; 
	 }

	 /**
	  * output file format;
	  */

}
