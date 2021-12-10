package class16;

import java.util.ArrayList;

// 点结构的描述
public class Node {
	public int value;
	/*入度：有几条边指向该点*/
	public int in;
	/*出度：该点有几条边指向别人*/
	public int out;
	/*该点指向的所有邻居*/
	public ArrayList<Node> nexts;
	/*该点出度的所有边*/
	public ArrayList<Edge> edges;

	public Node(int value) {
		this.value = value;
		in = 0;
		out = 0;
		nexts = new ArrayList<>();
		edges = new ArrayList<>();
	}
}
