package poly2tri_magic;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import gobalkrishnanv.gobal1995.frame.line_;

public class triangle_ {
	
	int x1,y1,x2,y2,x3,y3;

	
	public List<Point> p=new ArrayList<>();
	public List<line_> l=new ArrayList<>();
	

	public triangle_(int x1,int y1,int x2,int y2,int x3,int y3) {
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
		this.x3=x3;
		this.y3=y3;
		
		process();
	
	}	


int i=0,j=1;
Point temp;
public void process() {
	p.removeAll(p);
	l.removeAll(l);
	line_ l1=new line_(x1, y1, x2, y2);
	line_ l2=new line_(x2, y2, x3, y3);
	line_ l3=new line_(x3, y3, x1, y1);
	
	
	
	p.addAll(l1.p);
	p.addAll(l2.p);
	p.addAll(l3.p);
	
	
	
	//remove((ArrayList<Point>) p);
	
	
	for(int i=0;i<p.size();i++) {
		for(int j=0;j<p.size();j++) {
	 
			 int a=p.get(i).y;
			 int b=p.get(j).y;
			 
			 if(a<b) {
				 Point t=p.get(i);
				 p.set(i, p.get(j));
				 p.set(j, t);
			 }
			
        	}
		}
	

	
//	System.out.println(p.size());
	for(i=0,j=1;i<p.size();i++,j++) {
		//System.out.println(p.get(i).y);
		if(j==p.size()) {
			j=0;
		}
		Point a=p.get(i);
		Point b=p.get(j);
		
	//	if((a.x-b.x)!=1 || (a.x-b.x)!=-1)
		{
			//System.out.println(a+"::"+b);

			if(a.y==b.y && (a.x!=b.x)) 
			{
			  line_ l4=new line_(a.x,a.y,b.x,b.y);
				l.add(l4);
			
			  }
		}
		
	}
	//System.out.println("          d=============================================");
	//System.out.println(i+":"+j);
	
	
	//l.add(l1);
//	l.add(l2);/
	//l.add(l3);
	
}

public  ArrayList<Point> remove(ArrayList<Point> rp){
	ArrayList<Point> n=new ArrayList<>();
	
	for(Point s:rp) {
		if(!n.contains(s)) {
			n.add(s);
		}
	}
	
	
	rp.removeAll(rp);
	p=n;
	return n;
	
} 
   
	
}
