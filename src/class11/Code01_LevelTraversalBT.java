package class11;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 二叉树的宽度遍历
 * 用队列实现
 */
public class Code01_LevelTraversalBT {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int v) {
			value = v;
		}
	}

	public static void level(Node head) {
		if (head == null) {
			return;
		}
		Queue<Node> queue = new LinkedList<>();
		/*1. 先把头放进去*/
		queue.add(head);
		while (!queue.isEmpty()) {
			/*2. 弹出*/
			Node cur = queue.poll();
			/*3. cur业务*/
			System.out.println(cur.value);
			/*4. cur有左入左*/
			if (cur.left != null) {
				queue.add(cur.left);
			}
			/*5. cur有右入右*/
			if (cur.right != null) {
				queue.add(cur.right);
			}
		}
	}

	public static void main(String[] args) {
		Node head = new Node(1);
		head.left = new Node(2);
		head.right = new Node(3);
		head.left.left = new Node(4);
		head.left.right = new Node(5);
		head.right.left = new Node(6);
		head.right.right = new Node(7);

		level(head);
		System.out.println("========");
	}

}
