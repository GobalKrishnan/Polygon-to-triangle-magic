package poly2tri_magic;

import java.util.Comparator;

public class PointbaseComparatorCoordinatesReverse implements Comparator {

	public int compare(Object o1, Object o2) {
		point pb1 = (point)o1;
		point pb2 = (point)o2;
		return (-pb1.compareTo(pb2));
	}

}
