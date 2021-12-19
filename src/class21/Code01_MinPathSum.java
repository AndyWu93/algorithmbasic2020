package class21;

/**
 * 空间压缩技巧的使用（复杂度不变，省空间。小技巧可以不做）
 *
 * 一张matrix距离表，求从m[0][0]位置出发，只能往下后往右走，到m[n-1][m-1]最短路径
 * dp[i][j]:从m[0][0]到m[i][j]的最短路径，
 * 普遍位置：
 * dp[i][j] = min(dp[i-1][j],dp[i][j-1])+m[i][j]
 * 依赖左边位置和上边位置。dp[n-1][m-1]为题解
 *
 * 进阶：如何省空间？dp数组压缩技巧
 * 分析：因为求dp[i][j]时，只需要左边和上边的值，dp[i-2]行往上的值都不需要了，所以存在空间优化的可能性。
 * 使用两个数组重复使用，从上往下就能够推出dp[n-1][m-1];
 * 再精简：一个数组即可，用上一次计算出来的值推出这一次计算出来的值。
 *
 * 推广：
 * 1.依赖左边和上边位置的dp都可以压缩
 * 2.依赖上边和左上：同样的压缩，从右往左求
 * 3.依赖上边和左上和左上边：需要增加一个临时变量，在更新该位置时，记住该位置原来的值，给后面的位置使用（即左上的值）
 *
 * 进阶2：
 * 压缩选择？行和列，哪个短用哪个最为压缩数组长度
 */
public class Code01_MinPathSum {

	/**
	 * 普通dp表
	 * @param m
	 * @return
	 */
	public static int minPathSum1(int[][] m) {
		if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
			return 0;
		}
		int row = m.length;
		int col = m[0].length;
		int[][] dp = new int[row][col];
		dp[0][0] = m[0][0];
		for (int i = 1; i < row; i++) {
			dp[i][0] = dp[i - 1][0] + m[i][0];
		}
		for (int j = 1; j < col; j++) {
			dp[0][j] = dp[0][j - 1] + m[0][j];
		}
		for (int i = 1; i < row; i++) {
			for (int j = 1; j < col; j++) {
				dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + m[i][j];
			}
		}
		return dp[row - 1][col - 1];
	}

	/**
	 * 压缩后
	 * @param m
	 * @return
	 */
	public static int minPathSum2(int[][] m) {
		if (m == null || m.length == 0 || m[0] == null || m[0].length == 0) {
			return 0;
		}
		int row = m.length;
		int col = m[0].length;
		/*开始压缩*/
		int[] dp = new int[col];
		dp[0] = m[0][0];
		for (int j = 1; j < col; j++) {
			/*首行只依赖左边*/
			dp[j] = dp[j - 1] + m[0][j];
		}
		/*从第二行开始填，填到最后一行*/
		for (int i = 1; i < row; i++) {
			/*每行第一个值，仅依赖上面的位置，dp[0]原来的值就是上面的位置*/
			dp[0] += m[i][0];
			/*本行第一列的值已经有了，从第二列开始往后推，依赖dp左边的位置，和dp该位置原先的值*/
			for (int j = 1; j < col; j++) {
				dp[j] = Math.min(dp[j - 1], dp[j]) + m[i][j];
			}
		}
		return dp[col - 1];
	}

	// for test
	public static int[][] generateRandomMatrix(int rowSize, int colSize) {
		if (rowSize < 0 || colSize < 0) {
			return null;
		}
		int[][] result = new int[rowSize][colSize];
		for (int i = 0; i != result.length; i++) {
			for (int j = 0; j != result[0].length; j++) {
				result[i][j] = (int) (Math.random() * 100);
			}
		}
		return result;
	}

	// for test
	public static void printMatrix(int[][] matrix) {
		for (int i = 0; i != matrix.length; i++) {
			for (int j = 0; j != matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int rowSize = 10;
		int colSize = 10;
		int[][] m = generateRandomMatrix(rowSize, colSize);
		System.out.println(minPathSum1(m));
		System.out.println(minPathSum2(m));

	}
}
