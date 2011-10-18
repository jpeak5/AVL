package AVL;

public class AVL {

	public Node root;
	public boolean newHeight = false;

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
					/*
					 * now we need to determine how or if target (aka the parent
					 * of the node just inserted) balance factor should be
					 * updated and what should happen as a result
					 */
					if (target.bf == 1) { // left-heavy subtree
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
						newHeight = true; // this should demand a left rotation
						rotateLeft(target);
						newHeight = false;

					} else if (target.bf == 0) {// single left-rotation
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
				}
				/*
				 * now we need to determine how or if target (aka the parent of
				 * the node just inserted) balance factor should be updated and
				 * what should happen as a result
				 */
				if (target.bf == 1) { // left-heavy subtree
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
					newHeight = false;
				} else if (target.bf == 0) {// single left-rotation
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
				updateSize(target);
			}

		} else {
			root = node;
			node.size = 1;
		}
	}

	public void bottomUp(Node x) {
		
		updateHeightBalance(x);
		if (Math.abs(x.bf) == 2) {
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
	}

	public void updateSize(Node x) {
		do {
			int left = (x.left != null) ? x.left.size : 0;
			int right = (x.right != null) ? x.right.size : 0;
			x.size = 1 + left + right;
			x = x.parent;
		} while (x != null);
	}

	public void updateHeightBalance(Node x) {
		int left = (x.left == null) ? -1 : x.left.height;
		int right = (x.right == null) ? -1 : x.right.height;
		x.bf = left - right;
		x.height = Math.max(left, right) + 1;
	}

	public void rotate(Node x) {
		// determine which subtree is tallest
		if (x.bf < 0) {// negative, so we are looking at the right subtree
			if (x.right.bf < 0) {// Left Left rotation
				rotateLeft(x);
			} else {
				rotateRight(x.right);
				rotateLeft(x);
			}
		} else {// left subtree
			if (x.left.bf > 0) {
				rotateRight(x);
			} else {
				rotateLeft(x.left);
				rotateRight(x);
			}
		}
	}

	public void rotateLeft(Node x) {	
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
			if (x.parent.left == x) {
				y.parent.left = y;
			} else {
				y.parent.right = y;
			}
		} else {
			root = y;
			y.parent = null;
		}
		x.parent = y;
		y.left = x;
		updateHeightBalance(x);
	}

	public void rotateRight(Node x) {	
		Node y = x.left;
		x.left = null;
		if (y.right != null) {
			Node z = y.right;
			y.right = null;
			z.parent = x;
			x.left = z;
		}
		if (x != root) {
			y.parent = x.parent;
			if (x.parent.left == x) {
				y.parent.left = y;
			} else {
				y.parent.right = y;
			}
		} else {
			root = y;
			y.parent = null;
		}
		x.parent = y;
		y.right = x;
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
		sb.append(rank(node.key) + " ");
		sb.append(node.key);
		sb.append((node == root) ? ("<--ROOT-->") : "          ");
		sb.append((node.left != null) ? " left:  " + node.left.key
				: "           ");
		sb.append((node.right != null) ? " right: " + node.right.key
				: "           ");
		sb.append(node != root ? " parent: " + node.parent.key : "           ");
		sb.append(" bf: " + node.bf);
		sb.append(" height: " + node.height);
		sb.append(" size: " + node.size);
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

	public Node select(Node node, int i, int r) {
		if (i <= root.size) {
			if (node.left != null && node.left.size + r >= i) {// look
				// left
				return select(node.left, i, r);
			} else if ((node.left != null ? node.left.size : 0) + r + 1 < i) {
				return select(node.right, i, 1 + r + node.left.size);
			} else {
				return node;
			}
		} else {
			return null;// index i out of bounds
		}
	}

	public int rank(int key) {
		Node min = min(root);
		Node node = get(root, key);
		int rank = 0;
		while (node != null) {
			Node pr = predecessor(node.key);
			if (node.left != null) {
				rank += node.left.size + 1;
				node = predecessor(min(node.left).key);
			} else if (pr != null && pr.left == null) {
				node = pr;
				rank++;
			} else {
				node = pr;
				rank++;
			}

		}
		return rank;
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
		}
		Node y = node.parent;
		while (y != null && node == y.left) {
			node = y;
			y = y.parent;
		}
		return y;
	}


}
