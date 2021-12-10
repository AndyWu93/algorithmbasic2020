package class16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * 求拓扑排序，给定了图的结构
 * 思路1：
 * 对于任意节点a，从它出发走过所有的点（进过重复的点也需要纪1次）的数量为x
 * 对于任意节点b，从它出发走过所有的点（进过重复的点也需要纪1次）的数量为y
 * if x>y then a<=b(拓扑序)
 */
// OJ链接：https://www.lintcode.com/problem/topological-sorting
public class Code03_TopologicalOrderDFS2 {

	// 不要提交这个类
	public static class DirectedGraphNode {
		public int label;
		public ArrayList<DirectedGraphNode> neighbors;

		public DirectedGraphNode(int x) {
			label = x;
			neighbors = new ArrayList<DirectedGraphNode>();
		}
	}

	// 提交下面的
	public static class Record {
		public DirectedGraphNode node;
		public long nodes;

		public Record(DirectedGraphNode n, long o) {
			node = n;
			nodes = o;
		}
	}

	public static class MyComparator implements Comparator<Record> {

		@Override
		public int compare(Record o1, Record o2) {
			return o1.nodes == o2.nodes ? 0 : (o1.nodes > o2.nodes ? -1 : 1);
		}
	}

	/**
	 * 给定度图里只有点
	 * @param graph
	 * @return
	 */
	public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
		HashMap<DirectedGraphNode, Record> order = new HashMap<>();
		for (DirectedGraphNode cur : graph) {
			/*将所有点的点次记到了缓存order里了*/
			f(cur, order);
		}
		/*order里的所有点拿出来，按点次排序*/
		ArrayList<Record> recordArr = new ArrayList<>();
		for (Record r : order.values()) {
			recordArr.add(r);
		}
		recordArr.sort(new MyComparator());
		/*排好后返回*/
		ArrayList<DirectedGraphNode> ans = new ArrayList<DirectedGraphNode>();
		for (Record r : recordArr) {
			ans.add(r.node);
		}
		return ans;
	}

	/**
	 *  当前来到cur点，请返回cur点所到之处，所有的点次！
	 * @param cur
	 * @param order 缓存！！！！！ key : 某一个点的点次，之前算过了！ value : 点次是多少
	 * @return （cur，点次）
	 */
	public static Record f(DirectedGraphNode cur, HashMap<DirectedGraphNode, Record> order) {
		if (order.containsKey(cur)) {
			/*记忆化搜索*/
			return order.get(cur);
		}
		// cur的点次之前没算过！
		long nodes = 0;
		for (DirectedGraphNode next : cur.neighbors) {
			/*所有邻居的点次和*/
			nodes += f(next, order).nodes;
		}
		/*cur点次=所有邻居点次和+1*/
		Record ans = new Record(cur, nodes + 1);
		/*入缓存*/
		order.put(cur, ans);
		return ans;
	}

}
