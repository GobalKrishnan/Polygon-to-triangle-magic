package gobalkrishnanv.gobal1995.frame;

public class axis_2 {

	public point p;
	public axis_2(point c,point p,int in,int out) {
		
		int cx=c.t[0];
		int cy=c.t[1];
		
		int px=p.t[0];
		int py=p.t[1];
		
		//System.out.println(cx+"::"+cy+"::"+px+"::"+py);
		if(in>0) {
		if(px<cx && py<cy) {
			this.p=new point(px+in,py+in);
		}
		if(px>cx && py<cy ) {
			this.p=new point(px-in,py+in);
		}
		if(px>cx && py >cy) {
			this.p=new point(px-in,py-in);
					
		}
		if(px<cx && py>cy) {
			this.p=new point(px+in,py-in);
			
		}
		}
		
		if(out>0) {

			if(px<cx && py<cy) {
				this.p=new point(px-out,py-out);
			}
			if(px>cx && py<cy ) {
				this.p=new point(px+out,py-out);
			}
			if(px>cx && py >cy) {
				this.p=new point(px+out,py+out);
						
			}
			if(px<cx && py>cy) {
				this.p=new point(px-out,py+out);
				
			}
			
		}
		
	}
}
