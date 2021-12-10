package class16;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 拓扑排序：有向无环图
 * 有了a才能搞出b，有了a，b才能搞出c...最多用的地方是maven构建项目，maven不可以循环依赖，一定是一个工件依赖了一个或者多个其他的工件
 * 思路：
 * 通过入度来实现，入度为0的排在最前面，排好0入度的节点，将该节点对邻居的入度消掉，再找出入度为0的节点
 */
public class Code03_TopologySort {

	/**
	 * 给定一个图，返回拓扑排序的列表
	 * @param graph
	 * @return
	 */
	// directed graph and no loop
	public static List<Node> sortedTopology(Graph graph) {
		// key 某个节点   value 剩余的入度
		HashMap<Node, Integer> inMap = new HashMap<>();
		// 只有剩余入度为0的点，才进入这个队列
		Queue<Node> zeroInQueue = new LinkedList<>();
		for (Node node : graph.nodes.values()) {
			/*入度全注册在map中*/
			inMap.put(node, node.in);
			if (node.in == 0) {
				/*一开始进入zq的一定是最底层依赖的工件*/
				zeroInQueue.add(node);
			}
		}
		List<Node> result = new ArrayList<>();
		while (!zeroInQueue.isEmpty()) {
			Node cur = zeroInQueue.poll();
			/*放入结果*/
			result.add(cur);
			for (Node next : cur.nexts) {
				/*放入结果后，所有的邻居map中的入度-1*/
				inMap.put(next, inMap.get(next) - 1);
				if (inMap.get(next) == 0) {
					/*-1以后所有变成0的节点，放进去*/
					zeroInQueue.add(next);
				}
			}
		}
		return result;
	}
}
