package poly2tri_magic;


public class UpdateKey implements action {

	public void action(node node, double y) {
		((line)node.data()).setKeyValue(y);
	}

}
