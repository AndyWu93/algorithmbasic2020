package class16;

public class Edge {
	/*边的权重，或者距离等*/
	public int weight;
	/*两个点*/
	public Node from;
	public Node to;

	public Edge(int weight, Node from, Node to) {
		this.weight = weight;
		this.from = from;
		this.to = to;
	}

}
