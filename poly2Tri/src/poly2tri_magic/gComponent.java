package poly2tri_magic;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

import javax.swing.JComponent;


public class gComponent extends JComponent{

	public String name;

	public void name(String name) {this.name=name;}
	public String name() {return name;}
	public String id;
	public void id(String id) {this.id=id;}
	public String id() {return id;}
  
	public String type;
	public void type(String type) {this.type=type;}
	public String type() {return type;}
	
	List<gComponent> list=new ArrayList<>();
	  int max1;
		public void put(gComponent g) {
}
	    public void put(int i,gComponent g) {
	    	list.add(0,g);
	    	
	    	remove();
	        add();
	    	
	    	
	    	repaint();
	    }
		
		
		
		int ka=0;
		public gComponent get(int i) {
			
			
			return (gComponent) getComponents()[i];
		}
		
		private void add() {
			for(gComponent gc:list) {
				 if(gc!=null) {

					this.add(gc);
					}
				}

		   repaints();
		}
		
		private void repaints() {
			
		}
		private void remove() {
			try {
			for(gComponent gc:list) {
				 if(gc!=null) {
					this.remove(gc);
					}
				}
			  repaint();
			 repaints();
			}catch(ConcurrentModificationException |NullPointerException e) {
				
			}
			}
		
		
		public void moveg(int i,int r) {moves(i, r);}
		
		private void moves(int i,int r) {

			gComponent g=(gComponent) getComponents()[i];
			list.remove(g);
			list.add(r,g);
			
			remove();
			add();
			

			for(gComponent d:list) {
			//System.out.println(d);
			}
			System.out.println("");

	        
		
		}
		
		public void putTop(gComponent g) {
		
	        put(0,g);
			max1=list.size()-1;
			System.out.println("");
		}
		
		int xc=0;

		public void undo() {
			if(list.size()>0) {
			
			gComponent gc=list.get(max1--);
			remove(gc);
			repaint();
			}
		}
		
		public void redo() {
			if(list.size()>0) {

			  System.out.println();
			gComponent gc=list.get(++max1);
			add(gc);
			repaint();
			}
		}

		public void gremove(gComponent c) {
			
			list.remove(c);
			remove(c);
			repaints();
		}
	
}
