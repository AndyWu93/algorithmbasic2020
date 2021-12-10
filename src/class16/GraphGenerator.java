package class16;

/**
 * 图有多重表示方法，掌握其中一种即可
 */
public class GraphGenerator {

	// matrix 所有的边
	// N*3 的矩阵
	// [weight, from节点上面的值，to节点上面的值]
	// 
	// [ 5 , 0 , 7]
	// [ 3 , 0,  1]
	// 
	public static Graph createGraph(int[][] matrix) {
		Graph graph = new Graph();
		for (int i = 0; i < matrix.length; i++) {
			 // 拿到每一条边， matrix[i] 
			int weight = matrix[i][0];
			int from = matrix[i][1];
			int to = matrix[i][2];
			/*先把两个点建出来，放到图里*/
			if (!graph.nodes.containsKey(from)) {
				graph.nodes.put(from, new Node(from));
			}
			if (!graph.nodes.containsKey(to)) {
				graph.nodes.put(to, new Node(to));
			}
			Node fromNode = graph.nodes.get(from);
			Node toNode = graph.nodes.get(to);
			/*拿到了两个点，再把边建好*/
			Edge newEdge = new Edge(weight, fromNode, toNode);
			/*建立好from和to的关系*/
			fromNode.nexts.add(toNode);
			fromNode.out++;
			toNode.in++;
			fromNode.edges.add(newEdge);
			/*把边加到图里去*/
			graph.edges.add(newEdge);
		}
		return graph;
	}

}
