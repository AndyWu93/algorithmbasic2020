package class15;

/**
 * 给定一个二维矩阵，代表人物关系。行、列都是从0..n，代表n个人
 * 其中：
 * matrix[x][x]=1,对角线的位置一定是1，自己一定认识自己
 * if matrix[i][j]=1 then matrix[j][i]=1, i认识j，j一定认识i
 * 问该矩阵中有多少个朋友圈
 *
 * 思路：用并查集
 * 如果matrix[i][j]=1，把i和j的集合合并到一起
 * 最后看有几个集合
 * 注意：只需要遍历对角线右上半区域
 */
// 本题为leetcode原题
// 测试链接：https://leetcode.com/problems/friend-circles/
// 可以直接通过
public class Code01_FriendCircles {

	public static int findCircleNum(int[][] M) {
		int N = M.length;
		// {0} {1} {2} {N-1}
		UnionFind unionFind = new UnionFind(N);
		for (int i = 0; i < N; i++) {
			for (int j = i + 1; j < N; j++) {
				/*j从i+1开始，只遍历矩阵右上半区域*/
				if (M[i][j] == 1) { // i和j互相认识
					unionFind.union(i, j);
				}
			}
		}
		return unionFind.sets();
	}

	public static class UnionFind {
		// parent[i] = k ： i的父亲是k
		private int[] parent;
		// size[i] = k ： 如果i是代表节点，size[i]才有意义，否则无意义
		// i所在的集合大小是多少
		private int[] size;
		// 辅助结构
		private int[] help;
		// 一共有多少个集合
		private int sets;

		public UnionFind(int N) {
			parent = new int[N];
			size = new int[N];
			help = new int[N];
			sets = N;
			for (int i = 0; i < N; i++) {
				parent[i] = i;
				size[i] = 1;
			}
		}

		// 从i开始一直往上，往上到不能再往上，代表节点，返回
		// 这个过程要做路径压缩
		private int find(int i) {
			int hi = 0;
			/*通过parent数组找到父亲就是自己的节点，即代表节点*/
			while (i != parent[i]) {
				/*沿途所有的节点加入到help中去*/
				help[hi++] = i;
				i = parent[i];
			}
			for (hi--; hi >= 0; hi--) {
				/*help中所有的节点父亲指向代表节点*/
				parent[help[hi]] = i;
			}
			return i;
		}

		public void union(int i, int j) {
			int f1 = find(i);
			int f2 = find(j);
			/*代表节点不一样才合并，合并是小挂大*/
			if (f1 != f2) {
				if (size[f1] >= size[f2]) {
					size[f1] += size[f2];
					parent[f2] = f1;
				} else {
					size[f2] += size[f1];
					parent[f1] = f2;
				}
				/*合并完了，代表节点个数-1*/
				sets--;
			}
		}

		public int sets() {
			return sets;
		}
	}

}
