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
					target.size++;
					System.out.printf(
							"Inserting node %s as the right child of %s\n",
							node.key, target.key);
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
						rotateLeft(target);
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
					target.size++;
					System.out.printf(
							"Inserting node %s as the left child of %s\n",
							node.key, target.key);
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
				System.out.printf("\nCalling bottomUp(%s)\n", target.key);
				bottomUp(target);
			}

		} else {
			root = node;
			node.size = 1;
		}
	}

	public void bottomUp(Node x) {
		System.out.printf("Calling updateHeightBalance(%s)\n", x.key);
		updateHeightBalance(x);
		if (Math.abs(x.bf) == 2) {
			System.out.printf("balanceFactor for Node %s = %d\n", x.key, x.bf);
			System.out.printf("Calling rotate(%s)\n", x.key);
			rotate(x);
		}
		while (x != root && newHeight == true) {
			/*
			 * walk straight up the tree incrementing the heights and checking
			 * balance factors of the ancestors
			 */
			bottomUp(x.parent);
		}

		newHeight = false;
		System.out.printf("Setting newHeight false from node %s\n", x.key);
	}

	public void updateSize(Node x){
		int left = (x.left!=null) ? x.left.size : 0;
		int right = (x.right!=null) ? x.right.size : 0;
		x.size = left + right;
	}
	
	public void updateHeightBalance(Node x) {
		int left = (x.left == null) ? -1 : x.left.height;
		int right = (x.right == null) ? -1 : x.right.height;
		x.bf = left - right;
		x.height = Math.max(left, right) + 1;
		updateSize(x);
	}

	public void rotate(Node x) {
		System.out.println("Begin Rotations for the sub-tree rooted at "
				+ x.key);
		// determine which subtree is tallest
		if (x.bf < 0) {// negative, so we are looking at the right subtree
			if (x.right.bf < 0) {// Left Left rotation
				System.out.println("Simple Left rotation");
				rotateLeft(x);
			} else {
				System.out.println("Right-Left rotation");
				rotateRight(x.right);
				rotateLeft(x);
			}
		} else {// left subtree
			if (x.left.bf > 0) {
				System.out.println("Simple Right rotation");
				rotateRight(x);
			} else {
				System.out.println("Left-Right rotation");
				rotateLeft(x.left);
				rotateRight(x);
			}
		}
	}

	public void rotateLeft(Node x) {
		System.out.printf("rotateLeft(x.%s)\n", x.key);
		Node y = x.right;
		System.out.printf("Setting %s.right = null\n", x.key);
		x.right = null;
		if (y.left != null) {
			System.out.println("Node " + y.key + " has a left subtree\n");
			Node z = y.left;
			System.out.printf("Setting %s.left = null\n", y.left.key);
			y.left = null;
			System.out.printf("Setting %s.parent = %s\n", z.parent.key, x.key);
			z.parent = x;
			System.out.printf("Setting %s.right = %s\n", x.key, z.key);
			x.right = z;
		}
		if (x != root) {
			System.out.printf("Node %s was not root\n", x.key);
			y.parent = x.parent;
			System.out.printf("%s.parent = %s.parent\n", y.key, x.key);
			if (x.parent.left == x) {
				y.parent.left = y;
				System.out.printf("%s.parent.left = %s //identity\n",
						y.parent.left.key, y.key);
			} else {
				y.parent.right = y;
				System.out.printf("%s.parent.right = %s //identity\n",
						y.parent.right.key, y.key);
			}

		} else {
			root = y;
			System.out.printf("%s is now root\n", y.key);
			y.parent = null;
			System.out.printf("%s.parent set to null\n", y.key);
		}
		x.parent = y;
		System.out.printf("%s.parent = %s\n", x.key, y.key);
		y.left = x;
		System.out.printf("%s.left = %s\n", y.key, x.key);
		// x.height -=1;
		// y.height +=1;
		updateHeightBalance(x);

	}

	public void rotateRight(Node x) {
		System.out.printf("rotateRight(x.%s)\n", x.key);
		Node y = x.left;
		System.out.printf("Setting %s.left = null\n", x.key);
		x.left = null;
		if (y.right != null) {
			System.out.println("Node " + y.key + " has a right subtree\n");
			Node z = y.right;
			System.out.printf("Setting %s.right = null\n", y.key);
			y.right = null;
			System.out.printf("Setting %s.parent = %s\n", z.key, x.key);
			z.parent = x;
			System.out.printf("Setting %s.left = %s\n", x.key, z.key);
			x.left = z;
		}
		if (x != root) {
			System.out.printf("Node %s was not root\n", x.key);
			y.parent = x.parent;
			System.out.printf("%s.parent = %s.parent = %s\n", y.key, x.key,
					y.parent.key);

			if (x.parent.left == x) {
				y.parent.left = y;
				System.out.printf("%s.parent.left = %s //identity\n",
						y.parent.left.key, y.key);
			} else {
				y.parent.right = y;
				System.out.printf("%s.parent.right = %s //identity\n",
						y.parent.right.key, y.key);
			}
		} else {
			root = y;
			System.out.printf("%s is now root\n", y.key);
			y.parent = null;
			System.out.printf("%s.parent set to null\n", y.key);
		}
		x.parent = y;
		System.out.printf("%s.parent = %s\n", x.key, y.key);
		y.right = x;
		System.out.printf("%s.right = %s\n", y.key, x.key);
		// x.height -=1;
		// y.height +=2;
		updateHeightBalance(x);
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
		StringBuffer sb = new StringBuffer();
		sb.append(node.key);
		sb.append((node == root) ? ("<--ROOT-->") : "          ");
		sb.append((node.left != null) ? " left:  " + node.left.key
				: "           ");
		sb.append((node.right != null) ? " right: " + node.right.key
				: "           ");
		sb.append(node != root ? " parent: " + node.parent.key : "           ");
		sb.append(" bf: " + node.bf);
		sb.append(" height: " + node.height);
		sb.append(" size: "+node.size);
		sb.append("\n");
		System.out.print(sb.toString());

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
		}
		Node y = node.parent;
		while (y != null && node == y.right) {
			node = y;
			y = y.parent;
		}
		return y;
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



}
