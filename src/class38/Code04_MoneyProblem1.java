package class38;

/**
 * 正数数组d：怪兽能力
 * 正数数组p：需要的钱
 * 规则：
 * 自己的能力小于怪兽能力，需要花钱招募怪兽，否则无法通过，
 * 自己的能力大于等于怪兽能力，可以直接过，也可以招募
 * 问通过所有怪兽需要花至少多少钱？
 *
 * 本题为动态规划，需要看规模
 * 方案一：
 * dp[i][j]: 现有能力为j，那从i位置的怪兽开始，一直到n-1的怪兽都通过至少需要花多少钱
 * dp[0][0]就是题解
 * 规模：dp[n+1][sum+1]
 * n：怪兽个数；sum：所有怪兽的总能力
 *
 * 方案二：
 * dp[i][j]: 一路打到了第i个怪兽，如果指定只能花掉j的钱，目前拥有的最大能力是多少，如果只花j达不到，能力就是-1
 * 最后一行里，第一个不是-1的j就是题解
 * 规模: dp[n][sum+1]
 * n：怪兽个数；sum：所有怪兽的总钱数
 *
 * 本题需要看dp的规模，取规模小的模型
 */
public class Code04_MoneyProblem1 {

	// int[] d d[i]：i号怪兽的武力
	// int[] p p[i]：i号怪兽要求的钱
	// ability 当前你所具有的能力
	// index 来到了第index个怪兽的面前

	// 目前，你的能力是ability，你来到了index号怪兽的面前，如果要通过后续所有的怪兽，
	// 请返回需要花的最少钱数
	public static long process(int[] d, int[] p, int ability, int index) {
		if (index == d.length) {
			return 0;
		}
		if (ability < d[index]) {
			return p[index] + process(d, p, ability + d[index], index + 1);
		} else { // 可以贿赂，也可以不贿赂
			return 
					Math.min(
							p[index] + process(d, p, ability + d[index], index + 1),
						    process(d, p, ability, index + 1)
							);
		}
	}

	public static long func1(int[] d, int[] p) {
		return process(d, p, 0, 0);
	}

	/**
	 * 方案一
	 */
	public static long func2(int[] d, int[] p) {
		int sum = 0;
		for (int num : d) {
			//怪兽能力总和
			sum += num;
		}
		/*
		* dp[n][...]:最后一行：已经没有怪兽了，不管能力多少，都不需要花钱了
		* dp自然为0，所省略赋值
		* */
		long[][] dp = new long[d.length + 1][sum + 1];
		for (int cur = d.length - 1; cur >= 0; cur--) {
			for (int hp = 0; hp <= sum; hp++) {
				/*
				* 如果这种情况（拥有的能力总和超过怪兽能力总和）发生，那么这个hp必然是递归过程中不会出现的状态
				* 既然动态规划是尝试过程的优化，尝试过程碰不到的状态，不必计算
				* */
				if (hp + d[cur] > sum) {
					continue;
				}
				if (hp < d[cur]) {
					/*当前能力小于怪兽能力，招募*/
					dp[cur][hp] = p[cur] + dp[cur + 1][hp + d[cur]];
				} else {
					/*当前能力大于，可招募，可通过，取小*/
					dp[cur][hp] = Math.min(p[cur] + dp[cur + 1][hp + d[cur]], dp[cur + 1][hp]);
				}
			}
		}
		return dp[0][0];
	}

	/**
	 * 方案二
	 */
	public static long func3(int[] d, int[] p) {
		int sum = 0;
		for (int num : p) {
			//怪兽价格总和
			sum += num;
		}
		/*
		* dp[i][j]含义：
		* 能经过0～i的怪兽，且花钱为j（花钱的严格等于j）时的武力值最大是多少？
		* 如果dp[i][j]==-1，表示经过0～i的怪兽，花钱为j是无法通过的，或者之前的钱怎么组合也得不到正好为j的钱数
		* */
		int[][] dp = new int[d.length][sum + 1];
		/*初始化默认值为-1*/
		for (int i = 0; i < dp.length; i++) {
			for (int j = 0; j <= sum; j++) {
				dp[i][j] = -1;
			}
		}
		/*第一个怪兽，花了p[0]的钱，有了d[0]的能力，其他位置都为-1，不可能到达*/
		dp[0][p[0]] = d[0];
		for (int i = 1; i < d.length; i++) {
			for (int j = 0; j <= sum; j++) {
				/*
				* 可能性一，为当前怪兽花钱
				* 存在条件：通过上一个怪兽时，正好花掉j-p[i]元(j - p[i]要不越界)，能够通过
				* */
				if (j - p[i] >= 0 && dp[i - 1][j - p[i]] != -1) {
					/*上次积攒的能力+本次怪兽的能力*/
					dp[i][j] = dp[i - 1][j - p[i]] + d[i];
				}
				/*
				* 可能性二，不为当前怪兽花钱
				* 存在条件：通过了i-1的怪兽后，仍然有能力，且该能力大于i怪兽的能力
				* */
				if (dp[i - 1][j] >= d[i]) {
					/*两种可能性中，选武力值最大的*/
					dp[i][j] = Math.max(dp[i][j], dp[i - 1][j]);
				}
			}
		}
		int ans = 0;
		/*
		* dp表最后一行上，dp[N-1][j]代表：
		* 能经过0～N-1的怪兽，且花钱为j（花钱的严格等于j）时的武力值最大是多少？
		* 那么最后一行上，最左侧的不为-1的列数(j)，就是答案
		* */
		for (int j = 0; j <= sum; j++) {
			if (dp[d.length - 1][j] != -1) {
				ans = j;
				break;
			}
		}
		return ans;
	}

	public static int[][] generateTwoRandomArray(int len, int value) {
		int size = (int) (Math.random() * len) + 1;
		int[][] arrs = new int[2][size];
		for (int i = 0; i < size; i++) {
			arrs[0][i] = (int) (Math.random() * value) + 1;
			arrs[1][i] = (int) (Math.random() * value) + 1;
		}
		return arrs;
	}

	public static void main(String[] args) {
		int len = 10;
		int value = 20;
		int testTimes = 1000000;
		for (int i = 0; i < testTimes; i++) {
			int[][] arrs = generateTwoRandomArray(len, value);
			int[] d = arrs[0];
			int[] p = arrs[1];
			long ans1 = func1(d, p);
			long ans2 = func2(d, p);
			long ans3 = func3(d, p);
			if (ans1 != ans2 || ans2 != ans3) {
				System.out.println("oops!");
			}
		}

	}

}
