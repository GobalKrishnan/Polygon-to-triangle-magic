package gobalkrishnanv.gobal1995.frame;

import java.io.Serializable;

public class Color implements Serializable{

int red,green,blue,alpha;
float red_f,green_f,blue_f,alpha_f;
double red_d,green_d,blue_d,alpha_d;

public float convf_255_to_1(int i) {
	return i/255f;
}
public int convf_1_to_255(float i) {
	return (int) (255*i);
}

public double convd_255_to_1(int i) {
	return i/255d;
}
public int convd_1_to_255(double i) {
	return (int) (255*i);
}

public int argb;

public Color(int a,int r,int g,int b) {
	alpha=a;
	red=r;
	green=g;
	blue=b;
   toARGBint();
}
public Color(float a,float r,float g,float b) {
	alpha=convf_1_to_255(a);
	red=convf_1_to_255(r);
	green=convf_1_to_255(g);
	blue=convf_1_to_255(b);
	toARGBint();
}	

public Color(double a,double r,double g,double b) {
	alpha=convd_1_to_255(a);
	red=convd_1_to_255(r);
	green=convd_1_to_255(g);
	blue=convd_1_to_255(b);
	toARGBint();
}


public Color(int rgb) {
	// TODO Auto-generated constructor stub
	argb=rgb;
	toARGB();
}
private void toARGBint() {
	
	argb=(alpha<<24)|(red<<16)|(green<<8)|blue;
}
private void toARGB() {
	alpha=(argb>>24) & 0xff;
	red  =(argb>>16) & 0xff;
	green =(argb>>8) & 0xff;
	blue =(argb>>0)  & 0xff;
}

}
