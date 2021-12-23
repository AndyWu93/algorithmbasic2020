package class22;

/**
 * 将一个数n分裂成任意个数，要求分裂之后，后面的数不能比前面的数小，可以相等
 * 问有几种分裂方法
 *
 */
public class Code03_SplitNumber {

	// n为正数
	public static int ways(int n) {
		if (n < 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		/*第一个数选最小的裂开1，因为后面的都要比1大或者相等*/
		return process(1, n);
	}

	// 上一个拆出来的数是pre
	// 还剩rest需要去拆
	// 返回拆解的方法数
	public static int process(int pre, int rest) {
		if (rest == 0) {
			/*成功按规则将rest裂开没有剩余*/
			return 1;
		}
		if (pre > rest) {
			/*未能成功裂开*/
			return 0;
		}
		int ways = 0;
		for (int first = pre; first <= rest; first++) {
			ways += process(first, rest - first);
		}
		return ways;
	}

	public static int dp1(int n) {
		if (n < 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		int[][] dp = new int[n + 1][n + 1];
		for (int pre = 1; pre <= n; pre++) {
			dp[pre][0] = 1;
			/*对角线的位置都是1，通常需要先求出对角线位置，然后往后推*/
			dp[pre][pre] = 1;
		}
		/*最后一行填过了，第0行不用填无效值*/
		for (int pre = n - 1; pre >= 1; pre--) {
			for (int rest = pre + 1; rest <= n; rest++) {
				int ways = 0;
				/*
				* 通过观察得知
				* dp[i][j] 等于 dp[i][j-i]和dp[i][j-i]左下角一条斜线的和
				* dp[i+1][j] 等于 dp[i+1][j-i-1]和dp[i+1][j-i-1]左下角一条斜线的和
				* 而dp[i+1][j-i-1]恰好就是dp[i][j-i]的左下角
				* 分析得到dp[i][j] = dp[i+1][j] + dp[i][j-i]
				 * */
				for (int first = pre; first <= rest; first++) {
					ways += dp[first][rest - first];
				}
				dp[pre][rest] = ways;
			}
		}
		return dp[1][n];
	}

	public static int dp2(int n) {
		if (n < 0) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		int[][] dp = new int[n + 1][n + 1];
		for (int pre = 1; pre <= n; pre++) {
			dp[pre][0] = 1;
			dp[pre][pre] = 1;
		}
		for (int pre = n - 1; pre >= 1; pre--) {
			for (int rest = pre + 1; rest <= n; rest++) {
				dp[pre][rest] = dp[pre + 1][rest];
				dp[pre][rest] += dp[pre][rest - pre];
			}
		}
		return dp[1][n];
	}

	public static void main(String[] args) {
		int test = 39;
		System.out.println(ways(test));
		System.out.println(dp1(test));
		System.out.println(dp2(test));
	}

}
