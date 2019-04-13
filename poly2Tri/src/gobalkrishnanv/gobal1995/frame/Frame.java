package gobalkrishnanv.gobal1995.frame;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.*;

import poly2tri_magic.gComponent;
import poly2tri_magic.poly2triangle_;


public class Frame extends JFrame  {
  public Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
	
	
    int short_width,short_height;
    double ds=1.2;
	public Frame() {

		setUndecorated(true);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	    short_height=(int) (d.getHeight()/ds);
	    short_width=(int) (d.getWidth()/ds);
	    setSize(1000,short_height);
	}
	
	@Override
		public void paint(Graphics g) {
			// TODO Auto-generated method stub
			super.paint(g);
			
			g.setColor(new Color(100,0,255));
			g.fillRect(0,0,1000,700);
			
			poly2triangle_ e=(poly2triangle_) se.get(0);
			g.setColor(java.awt.Color.orange);
			e.paintfill(g);

			poly2triangle_ e1=(poly2triangle_) se.get(0);
			e1.paintdraw(g,5);
			g.setColor(java.awt.Color.cyan);
			g.setFont(new Font(FontFamily.Comic_Sans_MS,0,50));
			g.drawString("Gobal krishnan V", 550, 50);
			g.setFont(new Font("Arial",0,50));
			g.setFont(new Font(FontFamily.Comic_Sans_MS,0,30));

			g.drawString("Date of Birth = 18-06-1995", 550, 100);
			g.drawString("polygon to triangle => ", 550, 130);
			g.drawString("with holes", 850, 130);
			
			g.setFont(new Font(FontFamily.Comic_Sans_MS,0,20));
            g.setColor(Color.red);
			g.drawString("donate me a electronic,mechanical,", 550, 160);
			g.drawString("chemical components and equipments", 550, 190);
			g.drawString("to make robot for space travel", 550, 220);
            g.drawImage(new ImageIcon("D:/image/gobalkrishnan_v.jpg").getImage(), 580, 250, null);

		}
	ArrayList<gComponent> se=new ArrayList<gComponent>(); 
	public void put(gComponent s) {
		se.add(s);
	}

	
	
}
