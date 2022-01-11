package class32;

/**
 * 二维indexTree
 * it[i][j]: matrix[1][1]和matrix[i][j]组成的矩形中的数值之和
 *
 */
// 测试链接：https://leetcode.com/problems/range-sum-query-2d-mutable
// 但这个题是付费题目
// 提交时把类名、构造函数名从Code02_IndexTree2D改成NumMatrix
public class Code02_IndexTree2D {
	private int[][] tree;
	private int[][] nums;
	private int N;
	private int M;

	public Code02_IndexTree2D(int[][] matrix) {
		if (matrix.length == 0 || matrix[0].length == 0) {
			return;
		}
		N = matrix.length;
		M = matrix[0].length;
		tree = new int[N + 1][M + 1];
		nums = new int[N][M];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				update(i, j, matrix[i][j]);
			}
		}
	}

	/**
	 * 求m[1][1] 和 m[row][col] 组成矩形的和
	 * 实现过程只是比一维的sum多一个for维度
	 * 复杂度O(logN * logN)
	 */
	private int sum(int row, int col) {
		int sum = 0;
		/*将indexTree[][]中的多个位置的和加起来即可。哪些位置呢？和一维的sum方法一样，只不过这里是两个index
		* i、j分别都要减去最后侧的1，把这些位置组成的数都加起来
		* */
		for (int i = row + 1; i > 0; i -= i & (-i)) {
			for (int j = col + 1; j > 0; j -= j & (-j)) {
				sum += tree[i][j];
			}
		}
		return sum;
	}

	/**
	 * 将m[row][col] 的值改成val
	 * update可以改成add方法
	 * add方法的实现只是比一维的add多一个for维度
	 * 复杂度O(logN * logN)
	 */
	public void update(int row, int col, int val) {
		if (N == 0 || M == 0) {
			return;
		}
		/*计算出add值*/
		int add = val - nums[row][col];
		/*更新m[][]*/
		nums[row][col] = val;
		/*更新indexTree[][],方法参考一维的add方法*/
		for (int i = row + 1; i <= N; i += i & (-i)) {
			for (int j = col + 1; j <= M; j += j & (-j)) {
				tree[i][j] += add;
			}
		}
	}

	/**
	 * 如何理解？
	 * 假设求sum[3][3]-[4][4]
	 * it[4][4]-it[4][2]-it[2][4]+it[2][2]
	 * 最后为什么要加[2][2],因为该位置代表的区域sum被减了两次，要加回来一次
	 */
	public int sumRegion(int row1, int col1, int row2, int col2) {
		if (N == 0 || M == 0) {
			return 0;
		}
		return sum(row2, col2) + sum(row1 - 1, col1 - 1) - sum(row1 - 1, col2) - sum(row2, col1 - 1);
	}

}
