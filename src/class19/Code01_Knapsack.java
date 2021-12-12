package class19;

/**
 * 经典背包问题：给定等长数组w,v,分别代表重量、价值，给定背包容量bag，代表最大载重，问该背包能够容下的最大价值
 * 思路：动态规划，从左往右的尝试模型
 * dp[i][j]:从i位置货物开始自由选择，背包还剩下j的重量空着，在不超过背包重量下，货物最大总价值是多少
 * 规模：dp[n+1][bag+1]
 * n行：没有货物了，没有可选的，总价值是0
 * 0列：选择的货物总重不超过0，确实可能有一些货物，重量为0，但是有价值
 * 0列往左：无效区域，可以默认为-1
 * 普遍位置dp[i][j]
 * a.不要当前货物：p1=dp[i+1][j]
 * b.要了当前货物：j-w[i]<0? 是：空间不够了；否：p2=dp[i+1][j-w[i]]
 * dp[i][j]=max(a,b)
 * 所以只依赖下一行的位置，从下往上填写dp
 */
public class Code01_Knapsack {

	// 所有的货，重量和价值，都在w和v数组里
	// 为了方便，其中没有负数
	// bag背包容量，不能超过这个载重
	// 返回：不超重的情况下，能够得到的最大价值
	public static int maxValue(int[] w, int[] v, int bag) {
		if (w == null || v == null || w.length != v.length || w.length == 0) {
			return 0;
		}
		// 尝试函数！
		return process(w, v, 0, bag);
	}

	/*
	* 来到的index位置货物，此时背包容量为rest
	* 从index往后自由哦选择，返回从index往后能够收集到的最大价值
	* */
	// index 0~N
	// rest 负~bag
	public static int process(int[] w, int[] v, int index, int rest) {
		if (rest < 0) {
			/*rest<0,表示index上个位置已经不能选了，选了就超重，为无效方案*/
			return -1;
		}
		if (index == w.length) {
			return 0;
		}
		/*没要当前的货物，后续能够产生的最大价值*/
		int p1 = process(w, v, index + 1, rest);
		int p2 = 0;
		/*要了当前的货物，后续能够产生的最大价值*/
		int next = process(w, v, index + 1, rest - w[index]);
		if (next != -1) {
			/*返回-1表示当前货物不能选，否则当前货物价值加上去*/
			p2 = v[index] + next;
		}
		/*选最优方案*/
		return Math.max(p1, p2);
	}

	public static int dp(int[] w, int[] v, int bag) {
		if (w == null || v == null || w.length != v.length || w.length == 0) {
			return 0;
		}
		int N = w.length;
		int[][] dp = new int[N + 1][bag + 1];
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= bag; rest++) {
				int p1 = dp[index + 1][rest];
				int p2 = 0;
				int next = rest - w[index] < 0 ? -1 : dp[index + 1][rest - w[index]];
				if (next != -1) {
					p2 = v[index] + next;
				}
				dp[index][rest] = Math.max(p1, p2);
			}
		}
		return dp[0][bag];
	}

	public static void main(String[] args) {
		int[] weights = { 3, 2, 4, 7, 3, 1, 7 };
		int[] values = { 5, 6, 3, 19, 12, 4, 2 };
		int bag = 15;
		System.out.println(maxValue(weights, values, bag));
		System.out.println(dp(weights, values, bag));
	}

}
