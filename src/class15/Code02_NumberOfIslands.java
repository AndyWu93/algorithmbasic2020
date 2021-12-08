package class15;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * 给定一个二维matrix，里面只有'1'，'0'两个字符，把所有的1连成一片岛，问一共有多少个岛
 *
 * 解法一：暴力递归（其实是最优解，时间最短）
 * 遍历matrix，每遇到'1'，计数一次，调用infect方法，把上下左右都感染成0
 * 返回计数个数
 * 复杂度：每个位置最多碰5次，复杂度O(N*M)
 *
 * 解法二：使用并查集
 * 遍历matrix，每个位置都将它上、左的位置调用合并方法，最终代表节点个数就是题解
 * 技巧：每个'1'变成一个实例，一开始他们各自是一个集合，合并后变成一个大集合，每个0变成null，null不参与合并
 *
 * 解法三：并查集进阶
 * 二维数组并查集，使用二维数组记录自己父亲的位置（i,j）
 *
 * 进阶：如果给定的matrix特别大，如何通过并行计算，加快遍历matrix的时间
 * 思路：
 * 涉及如何map和reduce
 * 可以将一个大的matrix切割成任意份，各自求并查集，
 * 然后收集所有切割线两边的所有节点，将这些节点再看能不能合并起来
 */
// 本题为leetcode原题
// 测试链接：https://leetcode.com/problems/number-of-islands/
// 所有方法都可以直接通过
public class Code02_NumberOfIslands {

	/**
	 * 暴力解（最优解）
	 * @param board
	 * @return
	 */
	public static int numIslands3(char[][] board) {
		int islands = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				/*遍历matrix，碰了1次*/
				if (board[i][j] == '1') {
					/*遇到'1'就计数，并感染上下左右*/
					islands++;
					infect(board, i, j);
				}
			}
		}
		return islands;
	}

	// 从(i,j)这个位置出发，把所有练成一片的'1'字符，变成0
	public static void infect(char[][] board, int i, int j) {
		if (i < 0 || i == board.length || j < 0 || j == board[0].length || board[i][j] != '1') {
			/*该位置越界，以及不是'1'，直接返回*/
			return;
		}
		board[i][j] = 0;
		/*上下左右都必须调用*/
		infect(board, i - 1, j);
		infect(board, i + 1, j);
		infect(board, i, j - 1);
		infect(board, i, j + 1);
	}

	/**
	 * 并查集
	 * @param board
	 * @return
	 */
	public static int numIslands1(char[][] board) {
		int row = board.length;
		int col = board[0].length;
		/*生成一个等规模的Dot二维数组*/
		Dot[][] dots = new Dot[row][col];
		List<Dot> dotList = new ArrayList<>();
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				if (board[i][j] == '1') {
					/*'1'都变成了对象，'0'都变成了null*/
					dots[i][j] = new Dot();
					dotList.add(dots[i][j]);
				}
			}
		}
		UnionFind1<Dot> uf = new UnionFind1<>(dotList);
		/*(0,0)既没有上边也没有左边，所以跳过了*/
		/*第一行只合并左边*/
		for (int j = 1; j < col; j++) {
			// (0,j)  (0,0)跳过了  (0,1) (0,2) (0,3)
			if (board[0][j - 1] == '1' && board[0][j] == '1') {
				/*用原数组判断，用dot数组合并*/
				uf.union(dots[0][j - 1], dots[0][j]);
			}
		}
		/*第一列只合并上边*/
		for (int i = 1; i < row; i++) {
			if (board[i - 1][0] == '1' && board[i][0] == '1') {
				uf.union(dots[i - 1][0], dots[i][0]);
			}
		}
		for (int i = 1; i < row; i++) {
			for (int j = 1; j < col; j++) {
				if (board[i][j] == '1') {
					if (board[i][j - 1] == '1') {
						uf.union(dots[i][j - 1], dots[i][j]);
					}
					if (board[i - 1][j] == '1') {
						uf.union(dots[i - 1][j], dots[i][j]);
					}
				}
			}
		}
		return uf.sets();
	}

	public static class Dot {

	}

	public static class Node<V> {

		V value;

		public Node(V v) {
			value = v;
		}

	}

	public static class UnionFind1<V> {
		public HashMap<V, Node<V>> nodes;
		public HashMap<Node<V>, Node<V>> parents;
		public HashMap<Node<V>, Integer> sizeMap;

		public UnionFind1(List<V> values) {
			nodes = new HashMap<>();
			parents = new HashMap<>();
			sizeMap = new HashMap<>();
			for (V cur : values) {
				Node<V> node = new Node<>(cur);
				nodes.put(cur, node);
				parents.put(node, node);
				sizeMap.put(node, 1);
			}
		}

		public Node<V> findFather(Node<V> cur) {
			Stack<Node<V>> path = new Stack<>();
			while (cur != parents.get(cur)) {
				path.push(cur);
				cur = parents.get(cur);
			}
			while (!path.isEmpty()) {
				parents.put(path.pop(), cur);
			}
			return cur;
		}

		public void union(V a, V b) {
			Node<V> aHead = findFather(nodes.get(a));
			Node<V> bHead = findFather(nodes.get(b));
			if (aHead != bHead) {
				int aSetSize = sizeMap.get(aHead);
				int bSetSize = sizeMap.get(bHead);
				Node<V> big = aSetSize >= bSetSize ? aHead : bHead;
				Node<V> small = big == aHead ? bHead : aHead;
				parents.put(small, big);
				sizeMap.put(big, aSetSize + bSetSize);
				sizeMap.remove(small);
			}
		}

		public int sets() {
			return sizeMap.size();
		}

	}

	/**
	 * 进阶并查集，用数组实现并查集，加快常数时间
	 * @param board
	 * @return
	 */
	public static int numIslands2(char[][] board) {
		int row = board.length;
		int col = board[0].length;
		UnionFind2 uf = new UnionFind2(board);
		for (int j = 1; j < col; j++) {
			if (board[0][j - 1] == '1' && board[0][j] == '1') {
				uf.union(0, j - 1, 0, j);
			}
		}
		for (int i = 1; i < row; i++) {
			if (board[i - 1][0] == '1' && board[i][0] == '1') {
				uf.union(i - 1, 0, i, 0);
			}
		}
		for (int i = 1; i < row; i++) {
			for (int j = 1; j < col; j++) {
				if (board[i][j] == '1') {
					if (board[i][j - 1] == '1') {
						uf.union(i, j - 1, i, j);
					}
					if (board[i - 1][j] == '1') {
						uf.union(i - 1, j, i, j);
					}
				}
			}
		}
		return uf.sets();
	}

	public static class UnionFind2 {
		private int[] parent;
		private int[] size;
		private int[] help;
		private int col;
		private int sets;

		public UnionFind2(char[][] board) {
			col = board[0].length;
			sets = 0;
			int row = board.length;
			int len = row * col;
			/*这里没有使用二维数组，而是生成了一个长度为M*N的一维数组。在数据量特别大的情况不用使用一维*/
			parent = new int[len];
			size = new int[len];
			help = new int[len];
			for (int r = 0; r < row; r++) {
				for (int c = 0; c < col; c++) {
					if (board[r][c] == '1') {
						/*算出二维位置在一维下的index*/
						int i = index(r, c);
						parent[i] = i;
						size[i] = 1;
						sets++;
					}
				}
			}
		}

		// (r,c) -> i
		private int index(int r, int c) {
			return r * col + c;
		}

		// 原始位置 -> 下标
		private int find(int i) {
			int hi = 0;
			while (i != parent[i]) {
				help[hi++] = i;
				i = parent[i];
			}
			for (hi--; hi >= 0; hi--) {
				parent[help[hi]] = i;
			}
			return i;
		}

		public void union(int r1, int c1, int r2, int c2) {
			int i1 = index(r1, c1);
			int i2 = index(r2, c2);
			int f1 = find(i1);
			int f2 = find(i2);
			if (f1 != f2) {
				if (size[f1] >= size[f2]) {
					size[f1] += size[f2];
					parent[f2] = f1;
				} else {
					size[f2] += size[f1];
					parent[f1] = f2;
				}
				sets--;
			}
		}

		public int sets() {
			return sets;
		}

	}

	// 为了测试
	public static char[][] generateRandomMatrix(int row, int col) {
		char[][] board = new char[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				board[i][j] = Math.random() < 0.5 ? '1' : '0';
			}
		}
		return board;
	}

	// 为了测试
	public static char[][] copy(char[][] board) {
		int row = board.length;
		int col = board[0].length;
		char[][] ans = new char[row][col];
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				ans[i][j] = board[i][j];
			}
		}
		return ans;
	}

	// 为了测试
	public static void main(String[] args) {
		int row = 0;
		int col = 0;
		char[][] board1 = null;
		char[][] board2 = null;
		char[][] board3 = null;
		long start = 0;
		long end = 0;

		row = 1000;
		col = 1000;
		board1 = generateRandomMatrix(row, col);
		board2 = copy(board1);
		board3 = copy(board1);

		System.out.println("感染方法、并查集(map实现)、并查集(数组实现)的运行结果和运行时间");
		System.out.println("随机生成的二维矩阵规模 : " + row + " * " + col);

		start = System.currentTimeMillis();
		System.out.println("感染方法的运行结果: " + numIslands3(board1));
		end = System.currentTimeMillis();
		System.out.println("感染方法的运行时间: " + (end - start) + " ms");

		start = System.currentTimeMillis();
		System.out.println("并查集(map实现)的运行结果: " + numIslands1(board2));
		end = System.currentTimeMillis();
		System.out.println("并查集(map实现)的运行时间: " + (end - start) + " ms");

		start = System.currentTimeMillis();
		System.out.println("并查集(数组实现)的运行结果: " + numIslands2(board3));
		end = System.currentTimeMillis();
		System.out.println("并查集(数组实现)的运行时间: " + (end - start) + " ms");

		System.out.println();

		row = 10000;
		col = 10000;
		board1 = generateRandomMatrix(row, col);
		board3 = copy(board1);
		System.out.println("感染方法、并查集(数组实现)的运行结果和运行时间");
		System.out.println("随机生成的二维矩阵规模 : " + row + " * " + col);

		start = System.currentTimeMillis();
		System.out.println("感染方法的运行结果: " + numIslands3(board1));
		end = System.currentTimeMillis();
		System.out.println("感染方法的运行时间: " + (end - start) + " ms");

		start = System.currentTimeMillis();
		System.out.println("并查集(数组实现)的运行结果: " + numIslands2(board3));
		end = System.currentTimeMillis();
		System.out.println("并查集(数组实现)的运行时间: " + (end - start) + " ms");

	}

}
