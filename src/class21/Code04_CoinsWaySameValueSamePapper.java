package class21;

import java.util.HashMap;
import java.util.Map.Entry;

/**
 * 用货币数组arr中的货币组成aim，有几种方法（面值一样的货币都认为是相同的货币）
 * 该题是上一题的进阶，上一题每个面值是无穷张，这一题每个面值规定了固定张数
 *
 * 本题依旧会出现枚举的依赖关系
 * dp[i][j]: 来到第i位置货币，假设i位置货币有y张。
 * 分析位置可以看到，dp[i][j-arr[i]]和dp[i][j]都是下一行Y位置个dp的值相加而来
 * p[i][j]正好比dp[i][j-arr[i]]多了正下方的值即dp[i+1][j],少了y+1个位置的值即dp[i+1][j-(y+1)*arr[i]]
 * 所以
 * dp[i][j] = dp[i][j-arr[i]] + dp[i+1][j] - dp[i+1][j-(y+1)*arr[i]]
 */
public class Code04_CoinsWaySameValueSamePapper {

	public static class Info {
		public int[] coins;
		public int[] zhangs;

		public Info(int[] c, int[] z) {
			coins = c;
			zhangs = z;
		}
	}

	/*arr转化为面值及张数的方法*/
	public static Info getInfo(int[] arr) {
		HashMap<Integer, Integer> counts = new HashMap<>();
		for (int value : arr) {
			if (!counts.containsKey(value)) {
				counts.put(value, 1);
			} else {
				counts.put(value, counts.get(value) + 1);
			}
		}
		int N = counts.size();
		int[] coins = new int[N];
		int[] zhangs = new int[N];
		int index = 0;
		for (Entry<Integer, Integer> entry : counts.entrySet()) {
			coins[index] = entry.getKey();
			zhangs[index++] = entry.getValue();
		}
		return new Info(coins, zhangs);
	}

	public static int coinsWay(int[] arr, int aim) {
		if (arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		Info info = getInfo(arr);
		return process(info.coins, info.zhangs, 0, aim);
	}

	// coins 面值数组，正数且去重
	// zhangs 每种面值对应的张数
	/*
	* 从index位置到最后，组成rest的方法数，规定每个面值的张数
	* */
	public static int process(int[] coins, int[] zhangs, int index, int rest) {
		if (index == coins.length) {
			return rest == 0 ? 1 : 0;
		}
		int ways = 0;
		/*
		* 限制1：使用的张数不能超过rest
		* 限制2：使用的张数不能超过规定好的
		* */
		for (int zhang = 0; zhang * coins[index] <= rest && zhang <= zhangs[index]; zhang++) {
			ways += process(coins, zhangs, index + 1, rest - (zhang * coins[index]));
		}
		return ways;
	}

	public static int dp1(int[] arr, int aim) {
		if (arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		Info info = getInfo(arr);
		int[] coins = info.coins;
		int[] zhangs = info.zhangs;
		int N = coins.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 1;
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				int ways = 0;
				/*枚举形式的依赖需要优化*/
				for (int zhang = 0; zhang * coins[index] <= rest && zhang <= zhangs[index]; zhang++) {
					ways += dp[index + 1][rest - (zhang * coins[index])];
				}
				dp[index][rest] = ways;
			}
		}
		return dp[0][aim];
	}

	public static int dp2(int[] arr, int aim) {
		if (arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		Info info = getInfo(arr);
		int[] coins = info.coins;
		int[] zhangs = info.zhangs;
		int N = coins.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 1;
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				dp[index][rest] = dp[index + 1][rest];
				if (rest - coins[index] >= 0) {
					/*左边dp[i][j-arr[i]]位置不越界，加上去*/
					dp[index][rest] += dp[index][rest - coins[index]];
				}
				if (rest - coins[index] * (zhangs[index] + 1) >= 0) {
					/*下一行，左边dp[i+1][j-(y+1)*arr[i]]位置不越界，减掉*/
					dp[index][rest] -= dp[index + 1][rest - coins[index] * (zhangs[index] + 1)];
				}
			}
		}
		return dp[0][aim];
	}

	// 为了测试
	public static int[] randomArray(int maxLen, int maxValue) {
		int N = (int) (Math.random() * maxLen);
		int[] arr = new int[N];
		for (int i = 0; i < N; i++) {
			arr[i] = (int) (Math.random() * maxValue) + 1;
		}
		return arr;
	}

	// 为了测试
	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// 为了测试
	public static void main(String[] args) {
		int maxLen = 10;
		int maxValue = 20;
		int testTime = 1000000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr = randomArray(maxLen, maxValue);
			int aim = (int) (Math.random() * maxValue);
			int ans1 = coinsWay(arr, aim);
			int ans2 = dp1(arr, aim);
			int ans3 = dp2(arr, aim);
			if (ans1 != ans2 || ans1 != ans3) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println(aim);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println(ans3);
				break;
			}
		}
		System.out.println("测试结束");
	}

}
