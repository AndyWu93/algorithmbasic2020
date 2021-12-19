package class21;

/**
 * 枚举情况的dp依赖关系，可以从依赖关系中找出去枚举的依赖关系
 *
 * 用面值数组arr中的货币组成aim，有几种方法（一个面值的货币可以取n张）
 * 普遍位置：i位置用0张，1张，2张，...
 * dp[i][j] = dp[i+1][j] + dp[i+1][j-arr[i]] + dp[i+1][j-2*arr[i]] + ...
 * 此时必须优化。通过分析依赖关系将枚举变成1个位置
 *
 * 总结：
 * 记忆化搜索和dp表，在没有枚举情况依赖关系时时间复杂度一样
 *
 */
public class Code03_CoinsWayNoLimit {

	public static int coinsWay(int[] arr, int aim) {
		if (arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		return process(arr, 0, aim);
	}

	// arr[index....] 所有的面值，每一个面值都可以任意选择张数，组成正好rest这么多钱，方法数多少？
	public static int process(int[] arr, int index, int rest) {
		if (index == arr.length) { // 没钱了
			return rest == 0 ? 1 : 0;
		}
		int ways = 0;
		/*每个面值都可以用n张，但是不能超出rest的值*/
		for (int zhang = 0; zhang * arr[index] <= rest; zhang++) {
			ways += process(arr, index + 1, rest - (zhang * arr[index]));
		}
		return ways;
	}

	public static int dp1(int[] arr, int aim) {
		if (arr == null || arr.length == 0 || aim < 0) {
			return 0;
		}
		int N = arr.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 1;
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				int ways = 0;
				/*出现了枚举形式的依赖，一定存在依赖的优化，可以从dp表中去寻找依赖关系*/
				for (int zhang = 0; zhang * arr[index] <= rest; zhang++) {
					ways += dp[index + 1][rest - (zhang * arr[index])];
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
		int N = arr.length;
		int[][] dp = new int[N + 1][aim + 1];
		dp[N][0] = 1;
		for (int index = N - 1; index >= 0; index--) {
			for (int rest = 0; rest <= aim; rest++) {
				/*
				 * 分析得到，枚举中出现的下一行众位置的和（除了正下方），
				 * 其实就是本行左边某一个位置的值，因为他们的求值方式是一样的
				 * */
				dp[index][rest] = dp[index + 1][rest];/*正下方的先加上去*/
				if (rest - arr[index] >= 0) {
					dp[index][rest] += dp[index][rest - arr[index]];/*本行左边某个位置*/
				}
			}
		}
		return dp[0][aim];
	}

	// 为了测试
	public static int[] randomArray(int maxLen, int maxValue) {
		int N = (int) (Math.random() * maxLen);
		int[] arr = new int[N];
		boolean[] has = new boolean[maxValue + 1];
		for (int i = 0; i < N; i++) {
			do {
				arr[i] = (int) (Math.random() * maxValue) + 1;
			} while (has[arr[i]]);
			has[arr[i]] = true;
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
		int maxValue = 30;
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
