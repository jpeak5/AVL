package AVL;

public class AVL {

	public Node root;
	public boolean newHeight = false;// the global flag that marks when height

	// has

	public void insert(Node node, Node target) {
		if (root != null) {

			if (node.key > target.key) {
				/*
				 * is the node to insert bigger than the target? go right
				 */
				if (target.right != null) {
					/*
					 * is there already a child on the right of the target?
					 */
					insert(node, target.right);
					/*
					 * retry the insertion on target's right child
					 */
				} else {
					/*
					 * there is space available for us to insert, perform all
					 * actions required to complete insertion
					 */
					target.right = node;
					node.parent = target;
					/*
					 * now we need to determine how or if target (aka the parent
					 * of the node just inserted) balance factor should be
					 * updated and what should happen as a result
					 */
					if (target.bf == 1) { // left-heavy subtree
						System.out.println("Case 1, bf = 1 => 0");
						newHeight = false;
						/*
						 * since we are adding to the right, we are correcting
						 * the existing imbalance; with that done, the balance
						 * factor of the root of this subtree will be neutral
						 */

					} else if (target.bf == -1) {
						/*
						 * target is already heavy on the right side, so by
						 * inserting on the right, we will be increasing the
						 * height and we will need to try to fix this state by
						 * performing a rotation
						 */
						System.out.println("Case 2, bf = -1 => -2");
						newHeight = true; // this should demand a left rotation
						System.out.println("Left Rotation needed");
						rotateLeftLeft(target);
						newHeight = false;

					} else if (target.bf == 0) {// single left-rotation
						System.out
								.println("0-balanced subtree is now right-heavy");
						System.out.println("Case 3, bf = 0 => -1");
						newHeight = true;
						/*
						 * the height of this subtree has just increased by 1,
						 * which is fine unless it pushes ancestors' heights
						 * over 1
						 */

					}
				}
			} else {
				/*
				 * if we didn't insert to the right, then we will try to do so
				 * on the left
				 */
				if (target.left != null) {
					insert(node, target.left);
				} else {
					// we have found an appropriate empty spot
					target.left = node;
					node.parent = target;
				}
				/*
				 * now we need to determine how or if target (aka the parent of
				 * the node just inserted) balance factor should be updated and
				 * what should happen as a result
				 */
				if (target.bf == 1) { // left-heavy subtree
					System.out.println("Case 4, bf = 1 => 2");
					newHeight = true;
					/*
					 * since we are adding to the left, we are exacerbating the
					 * existing imbalance
					 */

				} else if (target.bf == -1) {
					/*
					 * we are bringing the subtree to equilibrium bf will be 0
					 * after insertion
					 */
					System.out.println("Case 5, bf = -1 => 0");
					newHeight = false;
					System.out.println("Left Rotation needed");

				} else if (target.bf == 0) {// single left-rotation
					System.out.println("Case 6, bf = 0 => 1");
					/*
					 * the height of this subtree has just increased by 1, which
					 * is fine unless it pushes ancestors' heights over 1
					 */
					newHeight = true;
				}
			}
			if (newHeight = true) {
				bottomUp(target);
			}

		} else {
			root = node;
		}
	}

	public void bottomUp(Node x) {
		x = updateHeightBalance(x);
		if (Math.abs(x.bf) == 2) {
			x = rotate(x);
		}
		while (x!=root && newHeight == true) {
			/*
			 * walk straight up the tree incrementing the heights and checking
			 * balance factors of the ancestors
			 */
			bottomUp(x.parent);

		}

	}

	public Node updateHeightBalance(Node x) {
		int left = (x.left == null) ? -1 : x.left.height;
		int right = (x.right == null) ? -1 : x.right.height;
		x.bf = left - right;
		x.height = Math.max(left, right) + 1;
		return x;
	}

	public Node rotate(Node x) {
		// determine which subtree is tallest
		if (x.bf < 0) {// negative, so we are looking at the right subtree
			if (x.right.bf < 0) {// Left Left rotation
				System.out.println("rotate LL on the right side");
				x = rotateLeftLeft(x);
			}

		}
		return x;
	}

	public Node rotateLeftLeft(Node x) {

		Node y = x.right;
		x.right = null;
		if (y.left != null) {
			Node z = y.left;
			y.left = null;
			z.parent = x;
			x.right = z;
		}
		if (x != root) {
			y.parent = x.parent;
		} else {
			root = y;
			y.parent = null;
		}
		x.parent = y;
		y.left = x;

		x.height = x.height - 2;
		System.out.println("Rotating node " + x.key + " left");

		newHeight = false;
		return x;
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
		} else if (node.parent != null) {
			if (node.parent.right == node) {// look up and left
				return node.parent;
			} else {
				return predecessor(node.parent.key);
			}
		}
		return node = null;
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
