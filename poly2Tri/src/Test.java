



import gobalkrishnanv.gobal1995.frame.Frame;
import gobalkrishnanv.gobal1995.frame.point;
import poly2tri_magic.poly2triangle_;


public class Test {

	public Test(String[] args) {
    	
	Frame f=new Frame();
   	poly2triangle_ w=new poly2triangle_();
	w.numContour(4);
	w.contours(4,4,4,4);
	w.points(new point(0,0),new point(500,0),new point(0,500),new point(500,500),
             new point(50,50),new point(50,200),new point(200,50),new point(200,200),
             new point(300,300),new point(300,450),new point(450,300),new point(450,450),
             new point(300,30),new point(300,200),new point(450,30),new point(450,200)
			);
	f.put(w);
	
	}
	
	public static void main(String[] args)  {

		new Test(args);


 
	}
	}
