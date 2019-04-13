package poly2tri_magic;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.NClob;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

import gobalkrishnanv.gobal1995.frame.axis_2;
import gobalkrishnanv.gobal1995.frame.line_;
import gobalkrishnanv.gobal1995.frame.point;



public class poly2triangle_ extends gComponent {
	
	
	public ArrayList<tri_> tr=new ArrayList<tri_>();
	
	public  int numContours;
	public  int[] contours;
	public  int[][] vertices;
	public poly2triangle_() {}
	public void numContour(int c) {
		numContours=c;
	}
	public void contours(int... con) {
		contours=new int[con.length];
		for(int i=0;i<con.length;i++) {
			contours[i]=con[i];
		}
	}
	ArrayList<ArrayList<point>> cls=new ArrayList<>();

	point[] po;
	
	public void points(point... p) {
		cls.removeAll(cls);
		po=new point[p.length];
		for(int i=0;i<po.length;i++) {
			po[i]=p[i];
		}
		vertices=new int[p.length][2];
		//System.out.println(Arrays.asList(p));
		int size=0;
		for(int i=0;i<contours.length;i++) {
			int a=contours[i];
		     size+=a;
		     
			ArrayList<point> lp=new ArrayList<point>();
		   for(int k=(size-a);k<size;k++) {
			   lp.add(p[k]);
		   }
		 //  System.out.println(lp);
			cls.add(lp);
		}
		
	//	System.out.println(cls.size());
		size=0;
		for(int i=0;i<cls.size();i++) {
		     size+=cls.get(i).size();
             int sw=size-cls.get(i).size();
             
		    // System.out.println(sw+":"+size);

			if(i==0 ) {
				QuickHull q=new QuickHull();
				ArrayList<point> t=q.quickhull(cls.get(i));
				ArrayList<point> r=q.reverse(t);
				for(int i1=sw;i1<size;i1++) {
					if(p[i1].t.length==2) {
						vertices[i1]=r.get(i1).t;
					}
				}
				
			}
			
			
			else {
				QuickHull q=new QuickHull();
				ArrayList<point> t=q.quickhull(cls.get(i));
			int ia=0;
				for(int i1=sw;i1<size;i1++) {
					if(p[i1].t.length==2) {
						vertices[i1]=t.get(ia++).t;
					}
				}
				
			}
			
			
			
		}
		
		
	}
	
	BufferedWriter wwe;
	public  void doTriangulation(){	
		 ArrayList triangles = null;

			triangles = Triangulation.triangulate(numContours, contours, vertices);
		
	
		
		
	    //printTriangles(triangles);
	    
	    ArrayList t; int[] xy1 = {0,0}, xy2 = {0,0}, xy3 = {0,0};
	   
	    point a,b,c;
	  
	    if(triangles!=null) {
	    
	    for (int i = 0; i < triangles.size(); ++i){
	    	t = (ArrayList)triangles.get(i);	   
	    	xy1 = vertices[(Integer)t.get(0)];
	    	xy2 = vertices[(Integer)t.get(1)];
	    	xy3 = vertices[(Integer)t.get(2)];
	    	
	    	a=new point(xy1[0],xy1[1]);
	    	b=new point(xy2[0],xy2[1]);
	    	c=new point(xy3[0],xy3[1]);
	    	tri_ w=new tri_(a, b, c);
	    	
	    	triangle_ tra=new triangle_(xy1[0],xy1[1],xy2[0],xy2[1],xy3[0],xy3[1]);
	    	//System.out.println(tra.l);
	        //pts.add((ArrayList<line_>) tra.l);
	     //  System.out.println(tra.l.size()); 	
	       
	        tr.add(w);
	    	
	    }	
	   	    }
	    
	       
	}
	public ArrayList<ArrayList<line_>> pts=new ArrayList<>();
	public ArrayList<point> points(){
		ArrayList<point> p=new ArrayList<point>();
	
		
		 ArrayList triangles = null;

			triangles = Triangulation.triangulate(numContours, contours, vertices);
		
	
		
		
	    //printTriangles(triangles);
	    
	    ArrayList t; int[] xy1 = {0,0}, xy2 = {0,0}, xy3 = {0,0};
	   
	    point a,b,c;
	  
	    if(triangles!=null) {
	    
	    for (int i = 0; i < triangles.size(); ++i){
	    	t = (ArrayList)triangles.get(i);	   
	    	xy1 = vertices[(Integer)t.get(0)];
	    	xy2 = vertices[(Integer)t.get(1)];
	    	xy3 = vertices[(Integer)t.get(2)];
	    	
	    	a=new point(xy1[0],xy1[1]);
	    	b=new point(xy2[0],xy2[1]);
	    	c=new point(xy3[0],xy3[1]);
	    	
	    	triangle_ tra=new triangle_(xy1[0],xy1[1],xy2[0],xy2[1],xy3[0],xy3[1]);

	    	for(int u=0;u<tra.l.size();u++) {
	    		for(int v=0;v<tra.l.get(u).p.size();v++) {
		    		 Point we=tra.l.get(u).p.get(v);
		    		 point ew=new point(we.x,we.y);
		    		 p.add(ew);
		    	}	
	    	}
	    	
	    	//System.out.println(tra.l.size());
	    	
	    }	
	    }
	    
	       
	
		
		
		
		
		return p;
	}
	boolean fill=true;
	public void paint(Graphics g) {
		if(fill) {
			g.setColor(new Color(25,88,255));
			paintfill(g);
			validate();
			revalidate();
			repaint();
		}
		else{
		paintdraw(g,5);
		validate();
		revalidate();
		repaint();
		}
		
	}
	public poly2triangle_ (boolean t) {
		this.fill=t;
	}
	public void paintfill(Graphics g) {
		
		if(vertices !=null && contours!=null) {
		
		doTriangulation();
		 
		
		for(int i=0,j=1;i<tr.size();i++,j++) {
			tri_ gt=tr.get(i);
			
			
			
			int[] ax= {(int) gt.a.t[0],(int)gt.b.t[0],(int)gt.c.t[0]};
			int[] yx= {(int)gt.a.t[1],(int)gt.b.t[1],(int)gt.c.t[1]};
			
			g.fillPolygon(ax, yx, 3);
			repaint();
							
			
		}
	}
	}
	
	private void fill(Graphics g) {

		
		if(vertices !=null && contours!=null) {
		
		doTriangulation();
		 
		
		for(int i=0,j=1;i<tr.size();i++,j++) {
			tri_ gt=tr.get(i);
			
			
			
			int[] ax= {(int) gt.a.t[0],(int)gt.b.t[0],(int)gt.c.t[0]};
			int[] yx= {(int)gt.a.t[1],(int)gt.b.t[1],(int)gt.c.t[1]};
			
			g.fillPolygon(ax, yx, 3);
			repaint();
							
			
		}
	}
	
	}
	
	
public void paintdraw(Graphics g,double h) {
	int size=0;
	
	ArrayList<ArrayList<point>> cls=new ArrayList<>();
    ArrayList<point> cx=new ArrayList<>();
    for(int i=0;i<contours.length;i++) {
		int a=contours[i];
	     size+=a;
	     
		ArrayList<point> lp=new ArrayList<point>();
		ArrayList<Integer> xp=new ArrayList<>();
		ArrayList<Integer> yp=new ArrayList<>();
		int x=0,y=0,dx=0,dy=0;
		for(int k=(size-a);k<size;k++) {
		   lp.add(po[k]);
		   xp.add((int)po[k].get(0));
		   yp.add((int)po[k].get(1));
	   }
	    
		sort(xp);
	    sort(yp);
		int minx=xp.get(0),miny=yp.get(0),maxx=xp.get(xp.size()-1),maxy=yp.get(yp.size()-1);
		
		x=(minx+maxx)/2;
		y=(miny+maxy)/2;
        poi(g,new point(x,y));

	   // System.out.println(x+":"+y);
		// System.out.println(x+":"+y);
		 cx.add(new point(x,y));
		cls.add(lp);
	}
	
    ArrayList<ArrayList<point>> li=new ArrayList<ArrayList<point>>();
    
	for(int i=0;i<cls.size();i++) {
         //System.out.println(cx.get(i)+":"+cls.get(i));
        ArrayList<point> ef=new ArrayList<point>();	

        for(int j=0;j<cls.get(i).size();j++) {
        if(i==0) {
        	point s=cx.get(i);
        	point e=cls.get(i).get(j);
     
            //System.out.println(s+":"+e);
        	axis_2 w=new axis_2(s, e, 5, 0);
        	point we=w.p;
        	//System.out.println(we);
        	ef.add(we);
        }else {

        	point s=cx.get(i);
        	point e=cls.get(i).get(j);
        	axis_2 w=new axis_2(s, e, 0, 5);
        	point we=w.p;
        	 	ef.add(we);
        	       
        
        }
        
        
        }
        li.add(ef);

	}
	
	ArrayList<ArrayList<point>> lio=new ArrayList<ArrayList<point>>();
	   for(int i=0;i<cls.size();i++) {
	   
	   ArrayList<point> e=new ArrayList<point>();
	  if(i==0)  { 
	   for(int j=0;j<cls.get(i).size();j++) {
		   point a= cls.get(i).get(j);
		   e.add(a);
		   
	   }
	   for(int j=0;j<li.get(i).size();j++) {
		   point a= li.get(i).get(j);
		   e.add(a);
	   }
	   }else {
		   for(int j=0;j<li.get(i).size();j++) {
			   point a= li.get(i).get(j);
			   e.add(a);
		   }
		   for(int j=0;j<cls.get(i).size();j++) {
			   point a= cls.get(i).get(j);
			   e.add(a);
			   
		   }
		  
		   
	   }
	   lio.add(e);
	   }
	   
	   poly2triangle_ s=new poly2triangle_();

	   for(int i=0;i<lio.size();i++) {
		   
		   int sie=lio.get(i).size()/2;
		   s.numContour(2);
		   s.contours(sie,sie);
		   
		   ArrayList<point> pois=lio.get(i);
		   point[] lis=new point[pois.size()];
		   for(int j=0;j<lis.length;j++) {
			   lis[j]=pois.get(j);
		   }
		   s.points(lis);
		   s.fill(g);
		   
	   }

	   
   
   
 
}
	

public ArrayList<point> li(ArrayList<Point> o){
	ArrayList<point> e=new ArrayList<point>();
	for(int i=0;i<o.size();i++) {
		Point w=o.get(i);
		e.add(new point(w.x,w.y));
	}
	return e;
}
public void poi(Graphics g,point o) {
	g.setColor(Color.red);
	g.fillOval((int)o.get(0), (int)o.get(1), 5, 5);
}
public void sort(ArrayList<Integer> s){
	
    for(int i=0;i<s.size();i++) {
    	for(int j=0;j<s.size();j++) {
        	
    		int a=s.get(i);
    		int b=s.get(j);
    		if(a<b) {
        		 int t=a;
        		 s.set(i, b);
        		 s.set(j, t);
        	 }
        }	
    }
	
	
	
}

}
