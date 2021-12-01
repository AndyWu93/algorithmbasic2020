package class10;

import java.util.Stack;

/**
 * 非递归先序中序后序遍历
 */
public class Code03_UnRecursiveTraversalBT {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int v) {
			value = v;
		}
	}

	public static void pre(Node head) {
		System.out.print("pre-order: ");
		if (head != null) {
			Stack<Node> stack = new Stack<Node>();
			/*1. 先把头压入*/
			stack.add(head);
			while (!stack.isEmpty()) {
				/*2. 弹出栈顶*/
				head = stack.pop();
				/*此处cur节点业务逻辑*/
				System.out.print(head.value + " ");
				/*3. 先压入cur的右节点，再压入cur的左节点*/
				if (head.right != null) {
					stack.push(head.right);
				}
				if (head.left != null) {
					stack.push(head.left);
				}
			}
		}
		System.out.println();
	}

	/**
	 * 遍历思路：
	 * 整棵树可以被若干个节点的左边界分解掉
	 * 所以来到一个节点后，先把该节点的所有左边界压入栈
	 * 再逐个弹出，看它的右节点，右节点同样也是所有的左边界压入栈
	 * @param cur
	 */
	public static void in(Node cur) {
		System.out.print("in-order: ");
		if (cur != null) {
			Stack<Node> stack = new Stack<Node>();
			/*一开始的时候，栈是空的，cur不为空*/
			while (!stack.isEmpty() || cur != null) {
				if (cur != null) {
					/*1. 以cur为头节点的整条左边界入栈，直到遇到null节点*/
					stack.push(cur);
					cur = cur.left;
				} else {
					/*2. 遇到null节点，弹出栈顶记为cur*/
					cur = stack.pop();
					/*3. 弹出cur的业务*/
					System.out.print(cur.value + " ");
					/*4. cur来到当前节点的右树，之后继续在右树上从1开始*/
					cur = cur.right;
				}
			}
		}
		System.out.println();
	}

	/**
	 * 整体结构类似于先序（但不是先序，是头右左），cur业务部分，将cur压入另一个栈
	 * 最后一个栈弹出，变成了左右头序
	 * @param head
	 */
	public static void pos1(Node head) {
		System.out.print("pos-order: ");
		if (head != null) {
			Stack<Node> s1 = new Stack<Node>();
			Stack<Node> s2 = new Stack<Node>();
			s1.push(head);
			while (!s1.isEmpty()) {
				head = s1.pop(); // 头 右 左
				/*压入*/
				s2.push(head);
				/*先压左*/
				if (head.left != null) {
					s1.push(head.left);
				}
				/*再压右*/
				if (head.right != null) {
					s1.push(head.right);
				}
			}
			// 左 右 头
			while (!s2.isEmpty()) {
				/*弹出*/
				System.out.print(s2.pop().value + " ");
			}
		}
		System.out.println();
	}

	/**
	 * 一个栈解决后序，很难，用的少
	 * @param h
	 */
	public static void pos2(Node h) {
		System.out.print("pos-order: ");
		if (h != null) {
			Stack<Node> stack = new Stack<Node>();
			stack.push(h);
			Node c = null;
			while (!stack.isEmpty()) {
				c = stack.peek();
				if (c.left != null && h != c.left && h != c.right) {
					stack.push(c.left);
				} else if (c.right != null && h != c.right) {
					stack.push(c.right);
				} else {
					System.out.print(stack.pop().value + " ");
					h = c;
				}
			}
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Node head = new Node(1);
		head.left = new Node(2);
		head.right = new Node(3);
		head.left.left = new Node(4);
		head.left.right = new Node(5);
		head.right.left = new Node(6);
		head.right.right = new Node(7);

		pre(head);
		System.out.println("========");
		in(head);
		System.out.println("========");
		pos1(head);
		System.out.println("========");
		pos2(head);
		System.out.println("========");
	}

}
