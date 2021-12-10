package class16;

import java.util.HashSet;
import java.util.Stack;

/**
 * 深度优先遍历
 * 要点：借助set来控制该元素只遍历了一次
 * 最佳实现方式：递归
 */
public class Code02_DFS {

	public static void dfs(Node node) {
		if (node == null) {
			return;
		}
		/*这里是迭代的方式，需要自己压栈，栈中的元素就是深度路径*/
		Stack<Node> stack = new Stack<>();
		HashSet<Node> set = new HashSet<>();
		stack.add(node);
		set.add(node);
		/*压栈的时候打印*/
		System.out.println(node.value);
		while (!stack.isEmpty()) {
			Node cur = stack.pop();
			for (Node next : cur.nexts) {
				if (!set.contains(next)) {
					/*找到了一个未注册的邻居，将自己和该邻居都压入栈中*/
					stack.push(cur);
					stack.push(next);
					/*set中注册*/
					set.add(next);
					System.out.println(next.value);
					/*找到了一个未注册的邻居就break*/
					break;
				}
			}
		}
	}
	
	
	

}
