package class22;

/**
 * 一个怪兽N滴血，砍一刀掉[0,M]滴血，问砍K次砍死怪兽的概率
 * 解析：该题属于样本对应模型，有两个注意项
 * 1. 该题在改dp表的时候，有一些值跑到表的外边去了，但这些位置逻辑上是有意义的值
 * 因此为了保证所有的值都在表格之内，递归需要剪枝，表外面的值即可调用剪枝的公式获得
 * 2. dp中存在枚举行为，需要优化。其中仍然会出现表外面的值需要处理
 */
public class Code01_KillMonster {

	public static double right(int N, int M, int K) {
		if (N < 1 || M < 1 || K < 1) {
			return 0;
		}
		/*所有的情况：(M+1)^k */
		long all = (long) Math.pow(M + 1, K);
		long kill = process(K, M, N);
		return (double) ((double) kill / (double) all);
	}

	// 怪兽还剩hp点血
	// 每次的伤害在[0~M]范围上
	// 还有times次可以砍
	// 返回砍死的情况数！
	public static long process(int times, int M, int hp) {
		if (times == 0) {
			return hp <= 0 ? 1 : 0;
		}
		/*
		* hp<=0的情况，这里如果不写，递归仍然是正确的
		* 但是下面在改dp的时候，发现hp小于等于0就越界了
		* 所以这里剪枝，提前结束递归：
		* 剪枝含义：怪兽没血了，我要times刀可以砍，结算我存活的情况数，即 (M+1)^k
		* */
		if (hp <= 0) {
			return (long) Math.pow(M + 1, times);
		}
		long ways = 0;
		for (int i = 0; i <= M; i++) {
			/*
			* 这里hp-i小于0了，循环还要继续往后遍历
			* 因为base case 中hp小于0同样返回了1，表示收集到1中情况下怪兽已经死了
			* */
			ways += process(times - 1, M, hp - i);
		}
		return ways;
	}

	public static double dp1(int N, int M, int K) {
		if (N < 1 || M < 1 || K < 1) {
			return 0;
		}
		long all = (long) Math.pow(M + 1, K);
		long[][] dp = new long[K + 1][N + 1];
		/*base case*/
		dp[0][0] = 1;
		/*第0行base case中算好了*/
		for (int times = 1; times <= K; times++) {
			/*递归中剪枝的情况*/
			dp[times][0] = (long) Math.pow(M + 1, times);
			/*第0列，在上面算了*/
			for (int hp = 1; hp <= N; hp++) {
				long ways = 0;
				/*
				* 这里存在枚举，即存在优化的可能
				* 观察得到
				* dp[i][j-1] = dp[i-1][j-1-m..j-1-0]
				* dp[i][j] = dp[i-1][j-m..j-0]
				* 所以
				* dp[i][j] = dp[i][j-1] + dp[i-1][j] - dp[i-1][j-1-m]
				* 其中j-1-m可能会越界，如果越界了，将dp[i-1][<0]用公式求出
				* dp[i-1][<0]含义：怪兽已经没血了，我有i-1刀
				* */
				for (int i = 0; i <= M; i++) {
					if (hp - i >= 0) {
						/*这里可以直接拿值，因为第0列上面一开始已经写过了*/
						ways += dp[times - 1][hp - i];
					} else {
						/*
						* 再往前拿不到值了，可以直接结算生存情况数
						* 此处含义：怪兽已经没血了，我还有times-1刀没砍
						* */
						ways += (long) Math.pow(M + 1, times - 1);
					}
				}
				dp[times][hp] = ways;
			}
		}
		long kill = dp[K][N];
		return (double) ((double) kill / (double) all);
	}

	public static double dp2(int N, int M, int K) {
		if (N < 1 || M < 1 || K < 1) {
			return 0;
		}
		long all = (long) Math.pow(M + 1, K);
		long[][] dp = new long[K + 1][N + 1];
		dp[0][0] = 1;
		for (int times = 1; times <= K; times++) {
			dp[times][0] = (long) Math.pow(M + 1, times);
			for (int hp = 1; hp <= N; hp++) {
				/*枚举的优化*/
				dp[times][hp] = dp[times][hp - 1] + dp[times - 1][hp];
				/*含义见上面dp1方法*/
				if (hp - 1 - M >= 0) {
					dp[times][hp] -= dp[times - 1][hp - 1 - M];
				} else {
					dp[times][hp] -= Math.pow(M + 1, times - 1);
				}
			}
		}
		long kill = dp[K][N];
		return (double) ((double) kill / (double) all);
	}

	public static void main(String[] args) {
		int NMax = 10;
		int MMax = 10;
		int KMax = 10;
		int testTime = 200;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int N = (int) (Math.random() * NMax);
			int M = (int) (Math.random() * MMax);
			int K = (int) (Math.random() * KMax);
			double ans1 = right(N, M, K);
			double ans2 = dp1(N, M, K);
			double ans3 = dp2(N, M, K);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("Oops!");
				break;
			}
		}
		System.out.println("测试结束");
	}

}
