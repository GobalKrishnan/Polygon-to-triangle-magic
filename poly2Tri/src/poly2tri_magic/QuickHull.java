package poly2tri_magic;
import java.util.ArrayList;

import gobalkrishnanv.gobal1995.frame.point;
public class QuickHull{
	
	public int pointLocation(point A,point B,point P) {
		int pl=((int)B.get(0)-(int)A.get(0))*((int)P.get(1)-(int)A.get(1))-((int)B.get(1)-(int)A.get(1))*((int)P.get(0)-(int)A.get(0));
		if(pl>0)
			return 1;
		else if(pl==0)
			return 0;
		else
			return -1;
		
	}
	
	public int distance(point A,point B,point C) {
		int num=((int)B.get(0)-(int)A.get(0))*((int)A.get(1)-(int)C.get(1))-((int)B.get(1)-(int)A.get(1))*((int)A.get(0)-(int)C.get(0));
		if(num<0) {
			num=-num;
		}
		return num;
	}
	
	public void hullset(point A,point B,ArrayList<point> set,ArrayList<point> hull) {
	   int insert=hull.indexOf(B);
	   
	   if(set.size()==0) {
		   return;
	   }
	   if(set.size()==1) {
		   point p=set.get(0);
		   set.remove(p);
		   hull.add(insert,p);
	       return;
	   }
	   
	   int dist=Integer.MIN_VALUE;
	   int furtherPoint=-1;
	   
	   for(int i=0;i<set.size();i++) {
		   point p=set.get(i);
		   int distance=distance(A, B, p);
		   
		   if(distance>dist) {
			   dist=distance;
			   furtherPoint=i;
		   }
	   }
	   point p=set.get(furtherPoint);
	   set.remove(furtherPoint);
	   hull.add(insert,p);
	   
	   ArrayList<point> leftsideAp=new ArrayList<>();
	   
	   for(int i=0;i<set.size();i++) {
		   point m=set.get(i);
		   if(pointLocation(A, p, m)==1) {
			   leftsideAp.add(m);
		   }
	   }
	   
	   
	   ArrayList<point> rightsidepB=new ArrayList<>();
	   
	   for(int i=0;i<set.size();i++) {
		   point m=set.get(i);
		   if(pointLocation(p, B, m)==1) {
			   rightsidepB.add(m);
		   }
	   }
	   
	   hullset(A, p, leftsideAp, hull);
	   hullset(p, B, rightsidepB, hull);
	   
	   
	}
	
	public ArrayList<point> reverse(ArrayList<point> p){
		ArrayList<point> r=new ArrayList<point>();
		
		for(int i=0;i<p.size();i++) {
			r.add(0, p.get(i));
		}
		
		return r;
	}
	
	public ArrayList<point> quickhull(ArrayList<point> p){
		
		
		ArrayList<point> convexHull=new ArrayList<>();
	    if(p.size()<3)
	    	return (ArrayList<point>) p.clone();
		  
		int minPoint=-1,maxPoint=-1;
	       int minX=Integer.MAX_VALUE;
	       int maxX=Integer.MIN_VALUE;
	       
	       for(int i=0;i<p.size();i++) {
	    	   if((int)p.get(i).get(0)<minX) {
	    		   minX=(int)p.get(i).get(0);
	    		   minPoint=i;
	    	   }
	    	   if((int)p.get(i).get(0)>maxX) {
	    		   maxX=(int)p.get(i).get(0);
	    		   maxPoint=i;
	    	   }
	       }
		
	       point A=p.get(minPoint);
	       point B=p.get(maxPoint);
	       
	      
	       
	       convexHull.add(A);
	       convexHull.add(B);
	       
	       
	       p.remove(A);
	       p.remove(B);
	       
	       ArrayList<point> leftside=new ArrayList<>();
	       ArrayList<point> rightside=new ArrayList<>();
	       
	       for(int i=0;i<p.size();i++) {
	    	   point P=p.get(i);
	    	   if(pointLocation(A, B, P)==-1) {
	    		   leftside.add(P);
	    	   }
	    	   if(pointLocation(A,B,P)==1) {
	    		   rightside.add(P);
	    	   }
	       }
	     
	     hullset(A, B, rightside, convexHull);
	     hullset(B, A, leftside, convexHull);
	      
		return convexHull;
	}
	
	public QuickHull() {
		//ArrayList<point> p=new ArrayList<>();
		//p.add(new point(1,0));
		//p.add(new point(0,0));
		//p.add(new point(1,1));
		//
	    //
		//p.add(new point(0,1));
	    //
		//ArrayList<point> s=quickhull(p);
		//for(int i=0;i<s.size();i++) {
		//	System.out.println(s.get(i).get(0)+":"+s.get(i).get(1));
		//}
		
	}
	//public static void main(String[] args) {
	//	new QuickHull();
	//}
}