package gobalkrishnanv.gobal1995.frame;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


public class line_ {
int sx,ex,sy,ey;
Color cs,ce;
public line_(int sx,int sy,int ex,int ey) {
  this.sx=sx;
  this.ex=ex;
  this.sy=sy;
  this.ey=ey;
  process();
}
public void color(Color cs,Color ce) {
	this.cs=cs;
	this.ce=ce;
	process();
}
@Override
public String toString() {
	return "line_ " + p ;
}

private int fabs(int x) {
	if(x<0) {
		x=x*-1;
	}
	return x;
}

public List<Point> p=new ArrayList<>();
public List<Color> c=new ArrayList<>();
Point point;
private void process() {
    p.removeAll(p);
   // c.removeAll(c);
	int a=1;
	int xdiff=ex-sx;
	int ydiff=ey-sy;
	
	if(xdiff==0 && ydiff==0) {
		
		point=new Point(sx,sy);
		this.c.add(cs);
	    
		this.p.add(point);
	}
  
	if(fabs(xdiff)>fabs(ydiff)) {
		int xmin,xmax;
		if(sx<ex){
			xmin=sx;
			xmax=ex;
		}else {
			xmin=ex;
			xmax=sx;
		}
		
		float slope=ydiff/(float)xdiff;
		for(float x=xmin;x<=xmax-a;x++) {
			float y= (float) (sy+((x-sx)*slope));
			point=new Point((int)x,(int)y);
			
			this.p.add(point);
		}
		
	}else {
		int ymin,ymax;
		
		if(sy<ey) {
			ymin=sy;
			ymax=ey;
		}else {
			ymin=ey;
			ymax=sy;
		}
		float slope=xdiff/(float)ydiff;
		
		for(float y=ymin;y<=ymax-a;y++) {
			float x=sx+((y-sy)*slope);
			//System.out.println((int)x+":"+(int)y);
			point=new Point((int)x,(int)y);
		    this.p.add(point);
		    
		 
		
		}
	}
}




}
