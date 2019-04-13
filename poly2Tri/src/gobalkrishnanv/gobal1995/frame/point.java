package gobalkrishnanv.gobal1995.frame;

public class point {
public int[] t;
public double[] d;
public float[] f;
public  short[] s;
public long[] l;
public byte[] b;
public boolean[] bool;
public char[] c;
public String[] st;

public Object get(int a) {
	
	if(l!=null){
		return l[a];
	}
	if(b!=null) {
		return b[a];
	}
	if(bool!=null){
		return bool[a];
	}
	if(c!=null) {
		return c[a];
	}
	if(st!=null) {
		return st[a];
	}
	if(t!=null) {
		return t[a];
	}
	if(d!=null) {
		return d[a];
	}
	if(f!=null) {
		return f[a];
	}
	if(s!=null) {
		return s[a];
	}
	return "not have data";
}

public point(int... ts) {
	t=new int[ts.length];
	for(int i=0;i<t.length;i++) {
		t[i]=ts[i];
	}
}
public point(long... ts) {
	l=new long[ts.length];
	for(int i=0;i<ts.length;i++) {
		l[i]=ts[i];
	}
}
public point(byte... ts) {
	b=new byte[ts.length];
	for(int i=0;i<ts.length;i++) {
		b[i]=ts[i];
	}
}
public point(boolean... ts) {
	bool=new boolean[ts.length];
	for(int i=0;i<ts.length;i++) {
		bool[i]=ts[i];
	}
}
public point(double... ts) {
	d=new double[ts.length];
	for(int i=0;i<ts.length;i++) {
		d[i]=ts[i];
	}
}
public point(float... ts) {
	f=new float[ts.length];
	for(int i=0;i<ts.length;i++) {
		f[i]=ts[i];
	}
}
public point(short... ts) {
	s=new short[ts.length];
	for(int i=0;i<ts.length;i++) {
		s[i]=ts[i];
	}
}
public point(char... ts) {
	c=new char[ts.length];
	for(int i=0;i<ts.length;i++) {
		c[i]=ts[i];
	}
}
public point(String... ts) {
	st=new String[ts.length];
	for(int i=0;i<ts.length;i++) {
		st[i]=ts[i];
	}
}

public point(point p) {
	t=p.t;
	d=p.d;
	f=p.f;
	s=p.s;
	l=p.l;
	b=p.b;
	bool=p.bool;
	c=p.c;
	st=p.st;
}

public String toString() {
	int aa=0;
	StringBuilder s=new StringBuilder();
	if(c!=null) {
		for(char a:c) {
			
			if(aa==this.c.length-1) {
			s.append(a);
			}else {
				s.append(a+",");	
			}
			aa++;
		}
		}
	if(st!=null) {
		for(String a:st) {
			
			if(aa==this.st.length-1) {
			s.append(a);
			}else {
				s.append(a+",");	
			}
			aa++;
		}
		}
	
	
	
	if(bool!=null) {
		for(boolean a:bool) {
			
			if(aa==this.bool.length-1) {
			s.append(a);
			}else {
				s.append(a+",");	
			}
			aa++;
		}
		}
	
	if(b!=null) {
		for(byte a:b) {
			
			if(aa==this.b.length-1) {
			s.append(a);
			}else {
				s.append(a+",");	
			}
			aa++;
		}
		}
	
	if(l!=null) {
		for(long a:l) {
			
			if(aa==this.l.length-1) {
			s.append(a);
			}else {
				s.append(a+",");	
			}
			aa++;
		}
		}
	
	
	if(this.s!=null) {
		for(short a:this.s) {
			
			if(aa==this.s.length-1) {
			s.append(a);
			}else {
				s.append(a+",");	
			}
			aa++;
		}
		}
	if(f!=null) {
		for(float a:f) {
			
			if(aa==this.f.length-1) {
			s.append(a);
			}else {
				s.append(a+",");	
			}
			aa++;
		}
		}
	if(t!=null) {
	for(int a:t) {
		
		if(aa==this.t.length-1) {
		s.append(a);
		}else {
			s.append(a+",");	
		}
		aa++;
	}
	}
	
	if(d!=null) {
		for(double a:d) {
			
			if(aa==this.d.length-1) {
			s.append(a);
			}else {
				s.append(a+",");	
			}
			aa++;
		}
		}
	return s.toString();
}

}
