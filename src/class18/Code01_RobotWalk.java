package class18;

/**
 * 长度为N的路，robot开始在start位置，要来到aim位置，规定只能走K步，问有几种走法
 * 规定，1位置只能往2走，N位置只能往N-1走
 * 思路：本题为动态规划，从左往右的尝试模型
 * dp[i][j]含义：从i位置出发，走j步到达aim位置的方法数
 * 规模dp[n+1][k+1]:可以从1~n位置出发，可以走0~k步。dp[0][0..k]无效位置不需要使用
 */
public class Code01_RobotWalk {

	public static int ways1(int N, int start, int aim, int K) {
		if (N < 2 || start < 1 || start > N || aim < 1 || aim > N || K < 1) {
			return -1;
		}
		return process1(start, K, aim, N);
	}

	// 机器人当前来到的位置是cur，
	// 机器人还有rest步需要去走，
	// 最终的目标是aim，
	// 有哪些位置？1~N
	// 返回：机器人从cur出发，走过rest步之后，最终停在aim的方法数，是多少？
	public static int process1(int cur, int rest, int aim, int N) {
		if (rest == 0) { // 如果已经不需要走了，走完了！
			return cur == aim ? 1 : 0;
		}
		// (cur, rest)
		if (cur == 1) { // 1 -> 2
			return process1(2, rest - 1, aim, N);
		}
		// (cur, rest)
		if (cur == N) { // N-1 <- N
			return process1(N - 1, rest - 1, aim, N);
		}
		// (cur, rest)
		return process1(cur - 1, rest - 1, aim, N) + process1(cur + 1, rest - 1, aim, N);
	}

	public static int ways2(int N, int start, int aim, int K) {
		if (N < 2 || start < 1 || start > N || aim < 1 || aim > N || K < 1) {
			return -1;
		}
		int[][] dp = new int[N + 1][K + 1];
		for (int i = 0; i <= N; i++) {
			for (int j = 0; j <= K; j++) {
				dp[i][j] = -1;
			}
		}
		// dp就是缓存表
		// dp[cur][rest] == -1 -> process1(cur, rest)之前没算过！
		// dp[cur][rest] != -1 -> process1(cur, rest)之前算过！返回值，dp[cur][rest]
		// N+1 * K+1
		return process2(start, K, aim, N, dp);
	}

	// cur 范: 1 ~ N
	// rest 范：0 ~ K
	public static int process2(int cur, int rest, int aim, int N, int[][] dp) {
		if (dp[cur][rest] != -1) {
			return dp[cur][rest];
		}
		// 之前没算过！
		int ans = 0;
		if (rest == 0) {
			ans = cur == aim ? 1 : 0;
		} else if (cur == 1) {
			ans = process2(2, rest - 1, aim, N, dp);
		} else if (cur == N) {
			ans = process2(N - 1, rest - 1, aim, N, dp);
		} else {
			ans = process2(cur - 1, rest - 1, aim, N, dp) + process2(cur + 1, rest - 1, aim, N, dp);
		}
		dp[cur][rest] = ans;
		return ans;

	}

	public static int ways3(int N, int start, int aim, int K) {
		if (N < 2 || start < 1 || start > N || aim < 1 || aim > N || K < 1) {
			return -1;
		}
		int[][] dp = new int[N + 1][K + 1];
		/*从aim位置出发，走了0步到aim位置，有1种方法，其他位置出发dp[0..n][0]=0*/
		dp[aim][0] = 1;
		/*
		* 填表顺序：从上往下，从左往右
		* 第一行填过了不用填
		* 第一行和最后一行，根据题意dp[1][j] <=> dp[2][j-1],dp[n][j] <=> dp[n-1][j-1]
		* */
		for (int rest = 1; rest <= K; rest++) {
			/*第一行*/
			dp[1][rest] = dp[2][rest - 1];
			for (int cur = 2; cur < N; cur++) {
				/*普遍位置：可以往左或者往右，方法数求和*/
				dp[cur][rest] = dp[cur - 1][rest - 1] + dp[cur + 1][rest - 1];
			}
			/*最后一行*/
			dp[N][rest] = dp[N - 1][rest - 1];
		}
		return dp[start][K];
	}

	public static void main(String[] args) {
		System.out.println(ways1(5, 2, 4, 6));
		System.out.println(ways2(5, 2, 4, 6));
		System.out.println(ways3(5, 2, 4, 6));
	}

}
