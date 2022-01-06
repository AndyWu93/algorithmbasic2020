package class30;

/**
 * morris遍历：
 * 遍历二叉树，能做到额外空间复杂度O(1)
 * 过程：
 * 假设来到当前节点cur，开始时cur来到头节点位置
 * 1）如果cur没有左孩子，cur向右移动(cur = cur.right)
 * 2）如果cur有左孩子，找到左子树上最右的节点mostRight：
 * 	a.如果mostRight的右指针指向空，让其指向cur，
 * 	然后cur向左移动(cur = cur.left)
 * 	b.如果mostRight的右指针指向cur，让其指向null，
 * 	然后cur向右移动(cur = cur.right)
 * 3）cur为空时遍历停止
 *
 * 该过程的遍历顺序？
 * 遍历顺序为morris序，特点：
 * 对于任意节点如果有左树，会达到两次，第二次的时候该节点的左树已经遍历完了
 * 对于任意节点如果没有左树，会达到一次
 * morris序实质：
 * 用当前节点左树的最右节点的右指针当前的状态，来标记是第一次还是第二次来到当前节点
 *
 * morris序加工成先序、中序
 * 先序：对于任意有左树的节点，第一次到达自己的时候处理
 * 中序：对于任意有左树的节点，第二次到达自己的时候处理
 */
public class Code01_MorrisTraversal {

	public static class Node {
		public int value;
		Node left;
		Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	public static void process(Node root) {
		if (root == null) {
			return;
		}
		// 1
		process(root.left);
		// 2
		process(root.right);
		// 3
	}

	/**
	 * morris
	 */
	public static void morris(Node head) {
		if (head == null) {
			return;
		}
		/*morris只需要连个变量，cur先指向头节点*/
		Node cur = head;
		Node mostRight = null;
		/*只有cur不等于空，就可以继续遍历*/
		while (cur != null) {
			mostRight = cur.left;
			/*左树不是空*/
			if (mostRight != null) {
				/*
				* 先往右找到最右节点，此时最后节点的右指针可能是两个状态：指向空或者指向cur
				* 该行为不会改变O(N)的复杂度，因为这里是过了每个有左树的右边界两次，仍然是O(N)
				* */
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					/*指向空，那cur到左树，后继续*/
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					/*指向cur，就恢复成null，cur到右树*/
					mostRight.right = null;
				}
			}
			/*左树是空，cur到右树*/
			cur = cur.right;
		}
	}

	/**
	 * 先序
	 * 递归序中，第一次来到自己为先序
	 * morris序，有左树，第一次来到自己时；加了两行代码
	 */
	public static void morrisPre(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					/*业务代码：有左树，第一次来到自己时*/
					System.out.print(cur.value + " ");
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			} else {
				/*业务代码：无左树，直接来*/
				System.out.print(cur.value + " ");
			}
			cur = cur.right;
		}
		System.out.println();
	}

	/**
	 * 递归序中，第二次来到自己为中序
	 * morris序，有左树，第二次来到自己时；仅一行代码
	 */
	public static void morrisIn(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			}
			/*业务代码：有左树：第二次来到自己时；无左树：直接来*/
			System.out.print(cur.value + " ");
			cur = cur.right;
		}
		System.out.println();
	}

	/**
	 * 递归序中，第三次来到自己为中序
	 * morris序没有第三次，所以有点复杂
	 * 处理时机都是在有左树，且第二次来到自己的时候，
	 * 此时，逆序遍历左树的右边界
	 * 最后一个能够到达自己两次的节点遍历结束后，逆序打印整棵树的右边界
	 * 问题：
	 * 如何O(1)的打印左树的右边界？
	 * 反转链表
	 */
	public static void morrisPos(Node head) {
		if (head == null) {
			return;
		}
		Node cur = head;
		Node mostRight = null;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
					/*业务代码：第二次到达自己时，逆序打印左树的右边界*/
					printEdge(cur.left);
				}
			}
			cur = cur.right;
		}
		/*业务代码：morris结束后，逆序打印整颗树的右边界*/
		printEdge(head);
		System.out.println();
	}

	public static void printEdge(Node head) {
		/*先链表反转*/
		Node tail = reverseEdge(head);
		Node cur = tail;
		while (cur != null) {
			System.out.print(cur.value + " ");
			cur = cur.right;
		}
		/*搞完了以后，再反转回去*/
		reverseEdge(tail);
	}

	public static Node reverseEdge(Node from) {
		Node pre = null;
		Node next = null;
		while (from != null) {
			next = from.right;
			from.right = pre;
			pre = from;
			from = next;
		}
		return pre;
	}

	// for test -- print tree
	public static void printTree(Node head) {
		System.out.println("Binary Tree:");
		printInOrder(head, 0, "H", 17);
		System.out.println();
	}

	public static void printInOrder(Node head, int height, String to, int len) {
		if (head == null) {
			return;
		}
		printInOrder(head.right, height + 1, "v", len);
		String val = to + head.value + to;
		int lenM = val.length();
		int lenL = (len - lenM) / 2;
		int lenR = len - lenM - lenL;
		val = getSpace(lenL) + val + getSpace(lenR);
		System.out.println(getSpace(height * len) + val);
		printInOrder(head.left, height + 1, "^", len);
	}

	public static String getSpace(int num) {
		String space = " ";
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < num; i++) {
			buf.append(space);
		}
		return buf.toString();
	}

	public static boolean isBST(Node head) {
		if (head == null) {
			return true;
		}
		Node cur = head;
		Node mostRight = null;
		Integer pre = null;
		boolean ans = true;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				while (mostRight.right != null && mostRight.right != cur) {
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) {
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else {
					mostRight.right = null;
				}
			}
			/*就加了这4行代码。因为涉及到改树的指针，一定要把morris跑完*/
			if (pre != null && pre >= cur.value) {
				ans = false;
			}
			pre = cur.value;

			cur = cur.right;
		}
		return ans;
	}

	public static void main(String[] args) {
		Node head = new Node(4);
		head.left = new Node(2);
		head.right = new Node(6);
		head.left.left = new Node(1);
		head.left.right = new Node(3);
		head.right.left = new Node(5);
		head.right.right = new Node(7);
		printTree(head);
		morrisIn(head);
		morrisPre(head);
		morrisPos(head);
		printTree(head);

	}

}
