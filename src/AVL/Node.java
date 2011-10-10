package AVL;

public class Node {
	public int key;
	public int bf;
	public String data;
	public int height;
	public Node parent;
	public Node right;
	public Node left;

	public Node(int key) {
		this.key = key;
	}

}
