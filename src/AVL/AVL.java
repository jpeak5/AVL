package AVL;

public class AVL {

	public Node root;

	public void insert(Node node, Node target) {
		boolean newHeight = false;// the global flag that marks when height has
									// changed
		if (root == null) {
			root = node;
			node.bf = 0;
		} else if (node.key > target.key) {// go right
			if (target.right != null) {
				insert(node, target.right);// recurse
			} else {// space available
				target.right = node;
				node.parent = target;
				node.bf = 0;// initial BF will be 0
				if (target.bf == 1) {
					newHeight = false;
					target.bf = 0;

				} else if (target.bf == -1) {
					newHeight = true;
					System.out.println("Left Rotation needed");
				} else if (target.bf == 0) {// single left-rotation
					System.out.println("Rotation needed, left");
					newHeight = true;
					target.bf = -1;

					// walk straight up the tree incrementing the heights of the
					// ancestors

					Node x = target;

					while (x != root && x != null) {
						x.height++;
						int xRT = (x.right == null) ? -1 : x.right.height;
						int xLF = (x.left == null) ? -1 : x.left.height;
						x.bf = xLF - xRT;
						if(x.bf>=Math.abs(2)){
							System.out.println("OMG, rotate");
						}
						x = x.parent;

					}// end ancestor walk

					// set the value of the root.height
					int rRT = (root.right == null) ? -1 : root.right.height; 
					int rLF = (root.left == null) ? -1 : root.left.height;
					root.bf = rLF - rRT;
					root.height = Math.max(rRT, rLF) + 1;
					// end set root value
				}

			}
		} else {
			if (target.left != null) {
				insert(node, target.left);
			} else {
				target.left = node;
				node.bf = 0;// initial BF will be 0
				node.parent = target;

			}
		}
	}

	public Node min(Node node) {
		if (node.left != null) {
			return min(node.left);
		} else {
			return node;
		}
	}

	public Node max(Node node) {
		if (node.right != null) {
			return max(node.right);
		} else {
			return node;
		}
	}

	// inorder walk of the tree, checking for null children
	public void inorder(Node node) {
		if (node.left != null) {
			inorder(node.left);
		}
		System.out.print(node.key + "(bf: " + node.bf + ")");
		if (node.left != null)
			System.out.print("(left: " + node.left.key + ")");
		if (node.right != null)
			System.out.print("(right: " + node.right.key + ")");
		System.out.print("(height: " + node.height + ")");
		System.out.print("\n");
		if (node.right != null) {
			inorder(node.right);
		}
	}

	public boolean search(Node node, int key) {
		if (node != null) {
			if (key > node.key) {
				return search(node.right, key);
			} else if (key < node.key) {
				return search(node.left, key);
			} else {
				return true;
			}
		}
		return false;
	}

	private Node get(Node node, int key) {// utility method to get a reference
		// to a node with key x
		if (node != null) {
			if (key > node.key) {
				return get(node.right, key);
			} else if (key < node.key) {
				return get(node.left, key);
			} else {
				return node;
			}
		}
		return node = null;
	}

	public Node successor(int key) {
		Node node = get(root, key);
		if (node.right != null) {// go down and right
			return min(node.right);
		} else if (node.parent.left == node) {// look up and right
			return node.parent;
		} else {
			return successor(node.parent.key);
		}

	}

	public Node predecessor(int key) {
		Node node = get(root, key);
		if (node.left != null) {// go down and right
			return max(node.left);
		} else if (node.parent.right == node) {// look up and left
			return node.parent;
		} else {
			return predecessor(node.parent.key);
		}
	}

	//
	// public void select(){
	//
	// }
	// public void rank(){
	//
	// }

	private void transplant(Node oroot, Node nroot) {
		if (oroot.left == nroot) {// right rotation
			if (oroot == root) {
				root = nroot;
				nroot.parent = null;
			} else {
				nroot.parent = oroot.parent;
			}
			oroot.parent = nroot;
			if (nroot.right != null) {
				nroot.right.parent = oroot;
				oroot.left = nroot.right;
			}
			nroot.right = oroot;
		} else {// left
			if (oroot == root) {
				root = nroot;
				nroot.parent = null;
			} else {
				nroot.parent = oroot.parent;
			}
			oroot.parent = nroot;
			if (nroot.left != null) {
				nroot.left.parent = oroot;
				oroot.right = nroot.left;
			}
			nroot.left = oroot;
		}
	}

	private void rRotate(Node node) {
		System.out.println("Right rotate:\n");
		transplant(node, node.left);
	}

	private void lRotate(Node node) {
		System.out.println("Left Rotate: \n");
		transplant(node, node.right);
	}
}
