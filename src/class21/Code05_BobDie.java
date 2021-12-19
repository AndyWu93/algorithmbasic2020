package class21;

/**
 * 给定一个地图N*M,将醉汉bob空降到(row,col)位置，他等概率4个方法走，走出地图就死了，问严格走k步后生还的概率
 * 思路：本题为动态规划，尝试模型不好划分，同跳马问题
 * 首先计算出走k步后落在某个位置，一共多少种可能性。每个点都是4个方向，一共走k个点，一共4^k个可能性
 * 再计算出生还的可能性数
 * 两者相除就是题解
 */
public class Code05_BobDie {

	public static double livePosibility1(int row, int col, int k, int N, int M) {
		/*生还的次数/总次数*/
		return (double) process(row, col, k, N, M) / Math.pow(4, k);
	}

	// 目前在row，col位置，还有rest步要走，走完了如果还在棋盘中就获得1个生存点，返回总的生存点数
	public static long process(int row, int col, int rest, int N, int M) {
		if (row < 0 || row == N || col < 0 || col == M) {
			/*越界了，收集0个*/
			return 0;
		}
		// 还在棋盘中！
		if (rest == 0) {
			/*走完了k步，还在地图里，收集1种*/
			return 1;
		}
		// 还在棋盘中！还有步数要走
		long up = process(row - 1, col, rest - 1, N, M);
		long down = process(row + 1, col, rest - 1, N, M);
		long left = process(row, col - 1, rest - 1, N, M);
		long right = process(row, col + 1, rest - 1, N, M);
		return up + down + left + right;
	}

	/**
	 * 分析依赖关系：
	 * 3个可变参数，第三个参数都是rest-1，三维空间的dp，都是依赖下一层的dp，本层没有相互依赖
	 */
	public static double livePosibility2(int row, int col, int k, int N, int M) {
		long[][][] dp = new long[N][M][k + 1];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < M; j++) {
				/*最底层：rest=0的情况，都是1*/
				dp[i][j][0] = 1;
			}
		}
		for (int rest = 1; rest <= k; rest++) {
			/*从倒数第二层开始，从下往上推*/
			for (int r = 0; r < N; r++) {
				for (int c = 0; c < M; c++) {
					/*直接拿值，越界情况拿到的是0*/
					dp[r][c][rest] = pick(dp, N, M, r - 1, c, rest - 1);
					dp[r][c][rest] += pick(dp, N, M, r + 1, c, rest - 1);
					dp[r][c][rest] += pick(dp, N, M, r, c - 1, rest - 1);
					dp[r][c][rest] += pick(dp, N, M, r, c + 1, rest - 1);
				}
			}
		}
		return (double) dp[row][col][k] / Math.pow(4, k);
	}

	public static long pick(long[][][] dp, int N, int M, int r, int c, int rest) {
		if (r < 0 || r == N || c < 0 || c == M) {
			return 0;
		}
		return dp[r][c][rest];
	}

	public static void main(String[] args) {
		System.out.println(livePosibility1(6, 6, 10, 50, 50));
		System.out.println(livePosibility2(6, 6, 10, 50, 50));
	}

}
