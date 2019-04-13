package poly2tri_magic;

import gobalkrishnanv.gobal1995.frame.point;

public class tri_ {
point a,b,c;

public point getA() {
	return a;
}

public void setA(point a) {
	this.a = a;
}

public point getB() {
	return b;
}

public void setB(point b) {
	this.b = b;
}

public point getC() {
	return c;
}

public void setC(point c) {
	this.c = c;
}

@Override
public String toString() {
	return String.format("tri_ [a=%s, b=%s, c=%s]", a, b, c);
}

public tri_(point a, point b, point c) {
	super();
	this.a = a;
	this.b = b;
	this.c = c;
}

}
