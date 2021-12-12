package class17;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 * 迪克斯特拉算法（有向图，无负权重，可以有环）
 * 给定一个出发点x，要求生成一张表，展示x到所有其他点的最小距离（x到不来的点，不用在这张表中，距离认为是正无穷）
 * 思路：
 * 准备一个map，记录所有的点到x的距离，准备一个set，记录已经结算过的点
 * 先把x放入map中距离是0
 * map中选出最小距离的点cur，距离为l
 * cur所有的to边拿出来，依次更新map中每个to节点的距离
 * 更新方式：如果l+to边结果比当前to节点记录的距离小，就更新，否则不更新
 *
 * 优化：map中选出最小距离的点，且需要排除selectedNodes。该方法可以通过加强堆达到O(logN)
 * 加强堆使用方式：堆中弹出当前最小距离，这时更新堆里该点周边的点的最小距离，且需要自动调整堆结构
 */
// no negative weight
public class Code01_Dijkstra {

	public static HashMap<Node, Integer> dijkstra1(Node from) {
		HashMap<Node, Integer> distanceMap = new HashMap<>();
		distanceMap.put(from, 0);
		// 打过对号的点
		HashSet<Node> selectedNodes = new HashSet<>();
		/*从当前map中选出距离最小的点，需要排除selectedNodes*/
		Node minNode = getMinDistanceAndUnselectedNode(distanceMap, selectedNodes);
		while (minNode != null) {
			// 原始点 -> minNode(跳转点) 最小距离distance
			int distance = distanceMap.get(minNode);
			for (Edge edge : minNode.edges) {
				Node toNode = edge.to;
				if (!distanceMap.containsKey(toNode)) {
					/*不在map中说明原来的距离是正无穷*/
					distanceMap.put(toNode, distance + edge.weight);
				} else { // toNode
					/*经过跳转点，如果距离更小了更新*/
					distanceMap.put(edge.to, Math.min(distanceMap.get(toNode), distance + edge.weight));
				}
			}
			selectedNodes.add(minNode);
			minNode = getMinDistanceAndUnselectedNode(distanceMap, selectedNodes);
		}
		return distanceMap;
	}

	public static Node getMinDistanceAndUnselectedNode(HashMap<Node, Integer> distanceMap, HashSet<Node> touchedNodes) {
		Node minNode = null;
		int minDistance = Integer.MAX_VALUE;
		for (Entry<Node, Integer> entry : distanceMap.entrySet()) {
			Node node = entry.getKey();
			int distance = entry.getValue();
			if (!touchedNodes.contains(node) && distance < minDistance) {
				minNode = node;
				minDistance = distance;
			}
		}
		return minNode;
	}

	public static class NodeRecord {
		public Node node;
		public int distance;

		public NodeRecord(Node node, int distance) {
			this.node = node;
			this.distance = distance;
		}
	}

	public static class NodeHeap {
		// 堆！
		private Node[] nodes;
		// node -> 堆上的什么位置？
		/*当nodes中元素弹出时，该map中对应元素不要删除，而是将值改为-1，表示该元素进来过*/
		private HashMap<Node, Integer> heapIndexMap;
		private HashMap<Node, Integer> distanceMap;
		private int size;

		public NodeHeap(int size) {
			nodes = new Node[size];
			heapIndexMap = new HashMap<>();
			distanceMap = new HashMap<>();
			size = 0;
		}

		public boolean isEmpty() {
			return size == 0;
		}

		// 有一个点叫node，现在发现了一个从源节点出发到达node的距离为distance
		// 判断要不要更新，如果需要的话，就更新
		public void addOrUpdateOrIgnore(Node node, int distance) {
			if (inHeap(node)) { // update
				distanceMap.put(node, Math.min(distanceMap.get(node), distance));
				/*距离只可能变小或者不变，所以只需要向上调整*/
				insertHeapify(node, heapIndexMap.get(node));
			}
			if (!isEntered(node)) { // add
				nodes[size] = node;
				heapIndexMap.put(node, size);
				distanceMap.put(node, distance);
				insertHeapify(node, size++);
			}
			// ignore
		}

		public NodeRecord pop() {
			NodeRecord nodeRecord = new NodeRecord(nodes[0], distanceMap.get(nodes[0]));
			swap(0, size - 1); // 0 > size - 1    size - 1 > 0
			heapIndexMap.put(nodes[size - 1], -1);
			distanceMap.remove(nodes[size - 1]);
			// free C++同学还要把原本堆顶节点析构，对java同学不必
			nodes[size - 1] = null;
			heapify(0, --size);
			return nodeRecord;
		}

		private void insertHeapify(Node node, int index) {
			while (distanceMap.get(nodes[index]) < distanceMap.get(nodes[(index - 1) / 2])) {
				swap(index, (index - 1) / 2);
				index = (index - 1) / 2;
			}
		}

		private void heapify(int index, int size) {
			int left = index * 2 + 1;
			while (left < size) {
				int smallest = left + 1 < size && distanceMap.get(nodes[left + 1]) < distanceMap.get(nodes[left])
						? left + 1
						: left;
				smallest = distanceMap.get(nodes[smallest]) < distanceMap.get(nodes[index]) ? smallest : index;
				if (smallest == index) {
					break;
				}
				swap(smallest, index);
				index = smallest;
				left = index * 2 + 1;
			}
		}

		private boolean isEntered(Node node) {
			return heapIndexMap.containsKey(node);
		}

		private boolean inHeap(Node node) {
			return isEntered(node) && heapIndexMap.get(node) != -1;
		}

		private void swap(int index1, int index2) {
			heapIndexMap.put(nodes[index1], index2);
			heapIndexMap.put(nodes[index2], index1);
			Node tmp = nodes[index1];
			nodes[index1] = nodes[index2];
			nodes[index2] = tmp;
		}
	}

	// 改进后的dijkstra算法
	// 从head出发，所有head能到达的节点，生成到达每个节点的最小路径记录并返回
	public static HashMap<Node, Integer> dijkstra2(Node head, int size) {
		NodeHeap nodeHeap = new NodeHeap(size);
		/*
		* add：之前没有在堆里；
		* update：需要更新最新的距离并调整堆；
		* ignore：之前进过堆已经弹出去了，再遇到时
		* */
		nodeHeap.addOrUpdateOrIgnore(head, 0);
		/*结果集*/
		HashMap<Node, Integer> result = new HashMap<>();
		while (!nodeHeap.isEmpty()) {
			/*弹出的记录一定会收集*/
			NodeRecord record = nodeHeap.pop();
			Node cur = record.node;
			int distance = record.distance;
			/*收集之前把该点周边的点加进去，或者重新计算一下最小距离*/
			for (Edge edge : cur.edges) {
				nodeHeap.addOrUpdateOrIgnore(edge.to, edge.weight + distance);
			}
			result.put(cur, distance);
		}
		return result;
	}

}
