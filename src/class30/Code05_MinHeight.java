package class30;

/**
 * 求一个二叉树，以head为头节点的最小深度
 * 常规解法：递归套路
 *
 * morris解法：
 * 遍历过程中，记录深度，且来到叶节点时，收集全局最小深度。
 * 1. 如何记录每个节点的深度？
 * 	用一个变量pre，记录上一个节点，
 * 	用一个变量level，记录当前节点的深度，
 * 	1）当前节点左树的右边界最右不是pre：level += 1
 * 	2）当前节点左树的右边界最右是pre：level -= 当前节点左树的右边界长度
 * 2. 如果判断当前来到了叶节点？
 * 	第二次来到有左树的节点后，恢复树结构，回头看一下pre，看pre是不是叶节点
 * 	如果是，顺着左树右边界计算一下pre的level，收集最小值
 * 3. 最后不能忘记看一下，整棵树的最右是不是叶节点
 *
 * 总结：
 * 需要x节点左树、右树完整信息的，只能递归套路解
 * 不需要x节点左树、右树完整信息的，递归套路和morris都可以解（本题只需要当前节点左树信息）
 */
public class Code05_MinHeight {

	public static class Node {
		public int val;
		public Node left;
		public Node right;

		public Node(int x) {
			val = x;
		}
	}

	public static int minHeight1(Node head) {
		if (head == null) {
			return 0;
		}
		return p(head);
	}

	// 返回x为头的树，最小深度是多少
	public static int p(Node x) {
		if (x.left == null && x.right == null) {
			return 1;
		}
		// 左右子树起码有一个不为空
		int leftH = Integer.MAX_VALUE;
		if (x.left != null) {
			leftH = p(x.left);
		}
		int rightH = Integer.MAX_VALUE;
		if (x.right != null) {
			rightH = p(x.right);
		}
		return 1 + Math.min(leftH, rightH);
	}

	// 根据morris遍历改写
	public static int minHeight2(Node head) {
		if (head == null) {
			return 0;
		}
		Node cur = head;
		Node mostRight = null;
		int curLevel = 0;
		int minHeight = Integer.MAX_VALUE;
		while (cur != null) {
			mostRight = cur.left;
			if (mostRight != null) {
				int rightBoardSize = 1;
				while (mostRight.right != null && mostRight.right != cur) {
					rightBoardSize++;
					mostRight = mostRight.right;
				}
				if (mostRight.right == null) { // 第一次到达
					curLevel++;
					mostRight.right = cur;
					cur = cur.left;
					continue;
				} else { // 第二次到达
					if (mostRight.left == null) {
						/*第二次到达时，看下左树最右节点有没有左树，没有的话说明是叶节点，收集深度*/
						minHeight = Math.min(minHeight, curLevel);
					}
					/*当前节点的深度，为当前level减去一个当前节点左树右边界长度*/
					curLevel -= rightBoardSize;
					mostRight.right = null;
				}
			} else { // 只有一次到达
				curLevel++;
			}
			cur = cur.right;
		}
		int finalRight = 1;
		cur = head;
		while (cur.right != null) {
			finalRight++;
			cur = cur.right;
		}
		if (cur.left == null && cur.right == null) {
			/*整棵树的最后还要看一下是不是叶，是的话收集一下深度*/
			minHeight = Math.min(minHeight, finalRight);
		}
		return minHeight;
	}

	// for test
	public static Node generateRandomBST(int maxLevel, int maxValue) {
		return generate(1, maxLevel, maxValue);
	}

	// for test
	public static Node generate(int level, int maxLevel, int maxValue) {
		if (level > maxLevel || Math.random() < 0.5) {
			return null;
		}
		Node head = new Node((int) (Math.random() * maxValue));
		head.left = generate(level + 1, maxLevel, maxValue);
		head.right = generate(level + 1, maxLevel, maxValue);
		return head;
	}

	public static void main(String[] args) {
		int treeLevel = 7;
		int nodeMaxValue = 5;
		int testTimes = 100000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			Node head = generateRandomBST(treeLevel, nodeMaxValue);
			int ans1 = minHeight1(head);
			int ans2 = minHeight2(head);
			if (ans1 != ans2) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish!");

	}

}
