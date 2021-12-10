package class16;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 宽度优先遍历
 * 要点：借助set来控制进队列的元素一定是第一次进入队列的
 */
public class Code01_BFS {

	// 从node出发，进行宽度优先遍历
	public static void bfs(Node start) {
		if (start == null) {
			return;
		}
		Queue<Node> queue = new LinkedList<>();
		HashSet<Node> set = new HashSet<>();
		queue.add(start);
		set.add(start);
		while (!queue.isEmpty()) {
			Node cur = queue.poll();
			System.out.println(cur.value);
			/*所有的邻居*/
			for (Node next : cur.nexts) {
				if (!set.contains(next)) {
					set.add(next);
					queue.add(next);
				}
			}
		}
	}

}
