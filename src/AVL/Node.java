package AVL;

public class Node {
	public int key;
	public int bf;
	public int height;
	public Node parent;
	public Node right;
	public Node left;
	public int size;
	public String data;

	public Node(int key) {
		this.key = key;
		this.height=0;
		this.bf=0;
	}
	


}
