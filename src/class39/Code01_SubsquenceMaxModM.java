package class39;

import java.util.HashSet;
import java.util.TreeSet;

/**
 * 给定一个非负数组arr，和一个正数m。
 * 返回arr的所有子序列中累加和%m之后的最大值。
 *
 * 本题需要看数据量，决定用什么方案:总数据量要控制在10^8
 * 一、arr.length不大，arr[i]也不大，但是m特别大
 * 思路：背包类型的动态规划
 * dp[i][j]:arr[0..i]位置的数自由组合，能否搞出sum为j
 * 规模dp[N][sum+1]
 * dp[i][j]转移方程：
 * 	1. 不用arr[i]：dp[i][j] = dp[i-1][j]
 * 	2. 用arr[i]: dp[i][j] = dp[i-1][j-arr[i]]
 *
 * 二、arr.length很大，arr[i]也很大，但是m不大
 * 思路：动态规划
 * dp[i][j]:arr[0..i]随意选择，是否得到一个和%m最大余数为j
 * 规模dp[N][m]
 * dp[i][j]转移方程：
 * 	1.不用arr[i]: dp[i][j] = dp[i-1][j]
 * 	2.用arr[i]
 * 		1) arr[i]<=j: 在前面看一下余数为j-arr[i]的组合，看能否得到
 * 		2) arr[i]>j: 在前面看一下余数为j+m-arr[i]的组合，看能否得到
 *
 * 三、arr.length不大，arr[i]很大，m也很大
 * 思路：只有arr.length不大，首先想到分治
 * 	将arr分成两半，左右两边各暴力求出所有的余数，遍历左边的余数，到右边寻找一个余数加起来<m最大的数
 * 复杂度：
 * 	左右两边暴力：O(2^arr.length/2)
 * 	左右求匹配：O(N*logN)
 */
// 给定一个非负数组arr，和一个正数m。 返回arr的所有子序列中累加和%m之后的最大值。
public class Code01_SubsquenceMaxModM {

	public static int max1(int[] arr, int m) {
		HashSet<Integer> set = new HashSet<>();
		process(arr, 0, 0, set);
		int max = 0;
		for (Integer sum : set) {
			max = Math.max(max, sum % m);
		}
		return max;
	}

	public static void process(int[] arr, int index, int sum, HashSet<Integer> set) {
		if (index == arr.length) {
			set.add(sum);
		} else {
			process(arr, index + 1, sum, set);
			process(arr, index + 1, sum + arr[index], set);
		}
	}

	/**
	 * 第一种动态规划
	 */
	public static int max2(int[] arr, int m) {
		int sum = 0;
		int N = arr.length;
		for (int i = 0; i < N; i++) {
			sum += arr[i];
		}
		boolean[][] dp = new boolean[N][sum + 1];
		for (int i = 0; i < N; i++) {
			/*无论随机选择几个数，加起来和是0一定可以，只有一个数也不选就好了*/
			dp[i][0] = true;
		}
		/*只有一个数的时候，一定能搞出和就是这个数*/
		dp[0][arr[0]] = true;
		for (int i = 1; i < N; i++) {
			for (int j = 1; j <= sum; j++) {
				/*不用arr[i]*/
				dp[i][j] = dp[i - 1][j];
				if (j - arr[i] >= 0) {
					/*不越界的情况下，用arr[i],结果或上之前的值*/
					dp[i][j] |= dp[i - 1][j - arr[i]];
				}
			}
		}
		/*找出符合条件的最大的j*/
		int ans = 0;
		for (int j = 0; j <= sum; j++) {
			if (dp[N - 1][j]) {
				ans = Math.max(ans, j % m);
			}
		}
		return ans;
	}

	/**
	 * 第二种动态规划
	 */
	public static int max3(int[] arr, int m) {
		int N = arr.length;
		// 0...m-1
		boolean[][] dp = new boolean[N][m];
		for (int i = 0; i < N; i++) {
			/*如果什么都不选，拿到的数的和为0，%m以后也是0*/
			dp[i][0] = true;
		}
		/*
		* 只有1个数是，就选1个数，%m以后，得到j，这个位置是true
		* dp[0][0]也是true，dp[0][其他位置]都是false
		* */
		dp[0][arr[0] % m] = true;
		for (int i = 1; i < N; i++) {
			for (int j = 1; j < m; j++) {
				// dp[i][j] T or F
				/*i位置的数不要*/
				dp[i][j] = dp[i - 1][j];
				int cur = arr[i] % m;
				if (cur <= j) {
					/*看之前sum%m等于j-cur的，为什么是'|='?之前是true的话，这里得到的也应该是true */
					dp[i][j] |= dp[i - 1][j - cur];
				} else {
					/*cur超过j，那就给j加一个m*/
					dp[i][j] |= dp[i - 1][m + j - cur];
				}
			}
		}
		/*其实可以从后往前遍历，找到第一个true就break，返回j*/
		int ans = 0;
		for (int i = 0; i < m; i++) {
			if (dp[N - 1][i]) {
				ans = i;
			}
		}
		return ans;
	}

	/**
	 * 第三种情况，采用分治
	 */
	// 如果arr的累加和很大，m也很大
	// 但是arr的长度相对不大
	public static int max4(int[] arr, int m) {
		if (arr.length == 1) {
			return arr[0] % m;
		}
		/*将arr分成两半*/
		int mid = (arr.length - 1) / 2;
		/*左边收集余数*/
		TreeSet<Integer> sortSet1 = new TreeSet<>();
		process4(arr, 0, 0, mid, m, sortSet1);
		/*右边收集余数*/
		TreeSet<Integer> sortSet2 = new TreeSet<>();
		process4(arr, mid + 1, 0, arr.length - 1, m, sortSet2);
		int ans = 0;
		/*
		* 遍历左边，到右边找一个合适的数，加起来比m小但是离m最近
		* 使用有序表，找起来更快
		* */
		for (Integer leftMod : sortSet1) {
			ans = Math.max(ans, leftMod + sortSet2.floor(m - 1 - leftMod));
		}
		return ans;
	}

	/*
	* 之前选好的数加在sum里，从index到end自由选择
	* 将结果放入sortSet中
	* */
	// 从index出发，最后有边界是end+1，arr[index...end]
	public static void process4(int[] arr, int index, int sum, int end, int m, TreeSet<Integer> sortSet) {
		if (index == end + 1) {
			sortSet.add(sum % m);
		} else {
			/*不选index*/
			process4(arr, index + 1, sum, end, m, sortSet);
			/*选index*/
			process4(arr, index + 1, sum + arr[index], end, m, sortSet);
		}
	}

	public static int[] generateRandomArray(int len, int value) {
		int[] ans = new int[(int) (Math.random() * len) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (int) (Math.random() * value);
		}
		return ans;
	}

	public static void main(String[] args) {
		int len = 10;
		int value = 100;
		int m = 76;
		int testTime = 500000;
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(len, value);
			int ans1 = max1(arr, m);
			int ans2 = max2(arr, m);
			int ans3 = max3(arr, m);
			int ans4 = max4(arr, m);
			if (ans1 != ans2 || ans2 != ans3 || ans3 != ans4) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish!");

	}

}
