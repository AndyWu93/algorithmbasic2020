package class26;

/**
 * 求数组arr中,所有子数组最小值的累加和
 *
 * 思路:假设数组中i位置的数作为最小值,首先可以求出以i位置为最小值的最长子数组
 * 找到这个子数组以后,再求出该子数组中有多少个子数组以i位置为最小值,假设x个,那x*arr[i]，就得出了所有以i位置为最小值的子数组的最小值的累加和
 * 1.如何求出以i位置为最小值的最长子数组？单调栈
 * 2.如何求出该子数组中有多少个子数组以i位置为最小值？
 * 一、arr中没有重复值
 * 假设以i位置为最小值的最长子数组,左边到a，右边到b，情况如下：
 *  4 10 8 9 6 7 12 5
 *  3 4  5 6 7 8 9 10
 *    --------
 *           -----
 *       4个   3
 *  7位置为最小值，3、10是到不了的位置。
 *  那以i位置为最小值的子数组包括：
 *  [4,7][4,8][4,9]
 *  [5,7][5,8][5,9]
 *  [6,7][6,8][6,9]
 *  [7,7][7,8][7,9]
 *  即(7-3)*(10-7)，抽象化即(i-a)*(b-i)
 *  产生的累加和就是(i-a)*(b-i)*arr[i]
 * 如此，将每个位置作为最小值的累加和加起来就是题解
 * 一、arr中有重复值
 *  单调栈中，遇到与自己相等的数，就弹出，结算弹出位置作为最小值子数组最小值累加和
 *  将相等的数的位置放进去，下次遇到相等的数，就弹出，同上。
 *  也就是遇到相等的数，需要结算一次
 *  举例：
 *  2 4 5 3 6 6 6 3 7 8 6  3   5  3  2
 *  0 1 2 3 4 5 6 7 8 9 10 11 12 13 14
 *  第一个3的结算范围[1,6]
 *  第二个3的结算范围[1,10]
 *  第三个3的结算范围[1,12]
 *  第四个3的结算范围[1,13]
 *
 */
// 测试链接：https://leetcode.com/problems/sum-of-subarray-minimums/
// subArrayMinSum1是暴力解
// subArrayMinSum2是最优解的思路
// sumSubarrayMins是最优解思路下的单调栈优化
// Leetcode上不要提交subArrayMinSum1、subArrayMinSum2方法，因为没有考虑取摸
// Leetcode上只提交sumSubarrayMins方法，时间复杂度O(N)，可以直接通过
public class Code01_SumOfSubarrayMinimums {

	public static int subArrayMinSum1(int[] arr) {
		int ans = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j < arr.length; j++) {
				int min = arr[i];
				for (int k = i + 1; k <= j; k++) {
					min = Math.min(min, arr[k]);
				}
				ans += min;
			}
		}
		return ans;
	}

	/**
	 * 没有用单调栈
	 * 但是整体是这个思路
	 */
	public static int subArrayMinSum2(int[] arr) {
		// left[i] = x : arr[i]左边，离arr[i]最近，<=arr[i]，位置在x
		int[] left = leftNearLessEqual2(arr);
		// right[i] = y : arr[i]右边，离arr[i]最近，< arr[i],的数，位置在y
		int[] right = rightNearLess2(arr);
		int ans = 0;
		for (int i = 0; i < arr.length; i++) {
			/*
			* 求出arr中以i位置最为最小值的子数组个数
			* 再乘arr[i]
			* */
			int start = i - left[i];
			int end = right[i] - i;
			ans += start * end * arr[i];
		}
		return ans;
	}

	public static int[] leftNearLessEqual2(int[] arr) {
		int N = arr.length;
		int[] left = new int[N];
		for (int i = 0; i < N; i++) {
			int ans = -1;
			for (int j = i - 1; j >= 0; j--) {
				if (arr[j] <= arr[i]) {
					ans = j;
					break;
				}
			}
			left[i] = ans;
		}
		return left;
	}

	public static int[] rightNearLess2(int[] arr) {
		int N = arr.length;
		int[] right = new int[N];
		for (int i = 0; i < N; i++) {
			int ans = N;
			for (int j = i + 1; j < N; j++) {
				if (arr[i] > arr[j]) {
					ans = j;
					break;
				}
			}
			right[i] = ans;
		}
		return right;
	}

	/**
	 * 用单调栈求出left和right数组
	 */
	public static int sumSubarrayMins(int[] arr) {
		int[] stack = new int[arr.length];
		int[] left = nearLessEqualLeft(arr, stack);
		int[] right = nearLessRight(arr, stack);
		long ans = 0;
		for (int i = 0; i < arr.length; i++) {
			long start = i - left[i];
			long end = right[i] - i;
			ans += start * end * (long) arr[i];
			/*
			* 冷知识：(a+b+c)%d = a%d + b%d + c%d
			* */
			ans %= 1000000007;
		}
		return (int) ans;
	}

	public static int[] nearLessEqualLeft(int[] arr, int[] stack) {
		int N = arr.length;
		int[] left = new int[N];
		int size = 0;
		/*反向遍历，这样就不用看栈下面的数，只需要看谁让自己弹出的*/
		for (int i = N - 1; i >= 0; i--) {
			while (size != 0 && arr[i] <= arr[stack[size - 1]]) {
				/*遇到比自己小或者相等的数，自己弹出，并在结果中记录是谁将自己弹出的，谁就是arr左边与自己相等或者比自己小的数*/
				left[stack[--size]] = i;
			}
			/*否则压栈*/
			stack[size++] = i;
		}
		while (size != 0) {
			/*栈中的每个位置，在arr左边都没有比自己小的数了*/
			left[stack[--size]] = -1;
		}
		return left;
	}

	public static int[] nearLessRight(int[] arr, int[] stack) {
		int N = arr.length;
		int[] right = new int[N];
		int size = 0;
		for (int i = 0; i < N; i++) {
			while (size != 0 && arr[stack[size - 1]] > arr[i]) {
				/*遇到严格比自己小的数，自己弹出，并在结果中记录是谁将自己弹出的，谁就是arr右边比自己小的数*/
				right[stack[--size]] = i;
			}
			stack[size++] = i;
		}
		while (size != 0) {
			/*栈中的每个位置，在arr右边都没有比自己小的数了*/
			right[stack[--size]] = N;
		}
		return right;
	}

	public static int[] randomArray(int len, int maxValue) {
		int[] ans = new int[len];
		for (int i = 0; i < len; i++) {
			ans[i] = (int) (Math.random() * maxValue) + 1;
		}
		return ans;
	}

	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int maxLen = 100;
		int maxValue = 50;
		int testTime = 100000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * maxLen);
			int[] arr = randomArray(len, maxValue);
			int ans1 = subArrayMinSum1(arr);
			int ans2 = subArrayMinSum2(arr);
			int ans3 = sumSubarrayMins(arr);
			if (ans1 != ans2 || ans1 != ans3) {
				printArray(arr);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println(ans3);
				System.out.println("出错了！");
				break;
			}
		}
		System.out.println("测试结束");
	}

}
