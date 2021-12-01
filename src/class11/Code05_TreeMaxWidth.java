package class11;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 求最大宽度
 * 思路：
 * 在宽度优先遍历的过程中，用4个变量记录数据
 * 1. curLevelNodes 当前层节点数，当节点弹出时++
 * 2. curEnd 当前层结尾，首次设为头节点，后期来源于nextEnd
 * 3. nextEnd 下一层结尾，遍历当前层时，需要为下一层准备结尾
 * 4. max 全局收集max
 */
public class Code05_TreeMaxWidth {

	public static class Node {
		public int value;
		public Node left;
		public Node right;

		public Node(int data) {
			this.value = data;
		}
	}

	/**
	 * 用map记录每个节点的层数，不推荐
	 * @param head
	 * @return
	 */
	public static int maxWidthUseMap(Node head) {
		if (head == null) {
			return 0;
		}
		Queue<Node> queue = new LinkedList<>();
		queue.add(head);
		// key 在 哪一层，value
		HashMap<Node, Integer> levelMap = new HashMap<>();
		levelMap.put(head, 1);
		int curLevel = 1; // 当前你正在统计哪一层的宽度
		int curLevelNodes = 0; // 当前层curLevel层，宽度目前是多少
		int max = 0;
		while (!queue.isEmpty()) {
			Node cur = queue.poll();
			int curNodeLevel = levelMap.get(cur);
			if (cur.left != null) {
				levelMap.put(cur.left, curNodeLevel + 1);
				queue.add(cur.left);
			}
			if (cur.right != null) {
				levelMap.put(cur.right, curNodeLevel + 1);
				queue.add(cur.right);
			}
			if (curNodeLevel == curLevel) {
				curLevelNodes++;
			} else {
				max = Math.max(max, curLevelNodes);
				curLevel++;
				curLevelNodes = 1;
			}
		}
		max = Math.max(max, curLevelNodes);
		return max;
	}

	/**
	 * 空间复杂度O(1),强烈推荐
	 * @param head
	 * @return
	 */
	public static int maxWidthNoMap(Node head) {
		if (head == null) {
			return 0;
		}
		Queue<Node> queue = new LinkedList<>();
		queue.add(head);
		/*先把头节点放进去*/
		Node curEnd = head; // 当前层，最右节点是谁
		Node nextEnd = null; // 下一层，最右节点是谁
		int max = 0;
		int curLevelNodes = 0; // 当前层的节点数
		while (!queue.isEmpty()) {
			Node cur = queue.poll();
			if (cur.left != null) {
				queue.add(cur.left);
				/*为下一层做准备*/
				nextEnd = cur.left;
			}
			if (cur.right != null) {
				queue.add(cur.right);
				/*刷新nextEnd*/
				nextEnd = cur.right;
			}
			/*弹出时++*/
			curLevelNodes++;
			if (cur == curEnd) {
				/*cur来到结尾了，先收集一下最大*/
				max = Math.max(max, curLevelNodes);
				/*再将当前统计清零*/
				curLevelNodes = 0;
				/*收集好的下一层结尾顶上去*/
				curEnd = nextEnd;
			}
		}
		return max;
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
		int maxLevel = 10;
		int maxValue = 100;
		int testTimes = 1000000;
		for (int i = 0; i < testTimes; i++) {
			Node head = generateRandomBST(maxLevel, maxValue);
			if (maxWidthUseMap(head) != maxWidthNoMap(head)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");

	}

}
