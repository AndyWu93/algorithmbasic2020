package class16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

/**
 * 求拓扑排序，给定了图的结构
 * 思路2：
 * 对于任意节点a，从它出发的最大深度为x
 * 对于任意点b，从它出发的最大深度为y
 * if x>y then a<=b(拓扑序)
 */
// OJ链接：https://www.lintcode.com/problem/topological-sorting
public class Code03_TopologicalOrderDFS1 {

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
		public int deep;

		public Record(DirectedGraphNode n, int o) {
			node = n;
			deep = o;
		}
	}

	public static class MyComparator implements Comparator<Record> {

		@Override
		public int compare(Record o1, Record o2) {
			return o2.deep - o1.deep;
		}
	}

	public static ArrayList<DirectedGraphNode> topSort(ArrayList<DirectedGraphNode> graph) {
		HashMap<DirectedGraphNode, Record> order = new HashMap<>();
		for (DirectedGraphNode cur : graph) {
			f(cur, order);
		}
		ArrayList<Record> recordArr = new ArrayList<>();
		for (Record r : order.values()) {
			recordArr.add(r);
		}
		recordArr.sort(new MyComparator());
		ArrayList<DirectedGraphNode> ans = new ArrayList<DirectedGraphNode>();
		for (Record r : recordArr) {
			ans.add(r.node);
		}
		return ans;
	}

	public static Record f(DirectedGraphNode cur, HashMap<DirectedGraphNode, Record> order) {
		if (order.containsKey(cur)) {
			return order.get(cur);
		}
		int follow = 0;
		for (DirectedGraphNode next : cur.neighbors) {
			/*所有邻居中深度的最大值*/
			follow = Math.max(follow, f(next, order).deep);
		}
		/*当前深度节点深度还需要+1*/
		Record ans = new Record(cur, follow + 1);
		order.put(cur, ans);
		return ans;
	}

}
