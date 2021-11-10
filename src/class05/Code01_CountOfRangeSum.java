package class05;

/**
 * 求累加和在某个范围内的子数组个数
 * 暴力解：枚举所有的子数组O(N^2)，求和O(N)对比范围
 * 复杂度：O(N^3)
 *
 * 优化：使用mergeSort
 * 首先生成一个前缀和预处理数组sum
 * 遍历数组arr，来到i位置，求以i位置结尾的子数组中，有多少个累加和落在lower,upper]上
 * 相当于求0..以i位置之前的数结尾的子数组中，有多少个累加和落在[sum[i]-upper,sum[i]-lower]上，以及以0..i位置的数结尾的子数组累加和是否落在[lower,upper]上
 * 借用归并排序加速
 * 在merge时：
 * 对于右组sum[m+1..r]上的每一个数sum[j],都求左组中有多少个数落在[sum[j]-upper,sum[j]-lower]上
 * 其中因为sum[j]单调递增，所以sum[j]-upper,sum[j]-lowe单调递增，可以对左组使用窗口，两个指针都不回退
 *
 * 将这些个数加起来就是题解
 *
 * 优化2：通过有序表解决
 */
// 这道题直接在leetcode测评：
// https://leetcode.com/problems/count-of-range-sum/
public class Code01_CountOfRangeSum {

	public static int countRangeSum(int[] nums, int lower, int upper) {
		if (nums == null || nums.length == 0) {
			return 0;
		}
		/*前缀和数组*/
		long[] sum = new long[nums.length];
		sum[0] = nums[0];
		for (int i = 1; i < nums.length; i++) {
			sum[i] = sum[i - 1] + nums[i];
		}
		return process(sum, 0, sum.length - 1, lower, upper);
	}

	/*
	* 递归含义：sum数组中,L..R范围上，任意一个数x，左边多少个数落在[x-upper,x-lower]上
	* */
	public static int process(long[] sum, int L, int R, int lower, int upper) {
		if (L == R) {
			/*范围上只有一个值：表示原数组nums中0..L的数组累加和，所以求其是不是在[lower,upper]上，在的话就收集了1个子数组*/
			return sum[L] >= lower && sum[L] <= upper ? 1 : 0;
		}
		int M = L + ((R - L) >> 1);
		return process(sum, L, M, lower, upper) + process(sum, M + 1, R, lower, upper)
				+ merge(sum, L, M, R, lower, upper);
	}

	/*
	* mergeSort之前，对每个右组中的每个x，求左组中有多少个数位于[x-upper,x-lower]中
	* 之所以只遍历右组，因为业务是需要求每个数左边的数是否符合要求，merge以后左组的数一定在右组的数左边
	* 因为左组的数已经有序，具有单调性，用窗口解决O(N)
	* */
	public static int merge(long[] arr, int L, int M, int R, int lower, int upper) {
		int ans = 0;
		/*窗口中的两个指针，表示的数范围[windowL,windowR) */
		int windowL = L;
		int windowR = L;
		// [windowL, windowR)
		/*只遍历右组*/
		for (int i = M + 1; i <= R; i++) {
			/*每个x都有自己的指标*/
			long min = arr[i] - upper;
			long max = arr[i] - lower;
			while (windowR <= M && arr[windowR] <= max) {
				/*调整窗口右指针，最终在max+1位置*/
				windowR++;
			}
			while (windowL <= M && arr[windowL] < min) {
				/*调整窗口左指针，最终在min位置*/
				windowL++;
			}
			/*收集结果，就是窗口内的数个数*/
			ans += windowR - windowL;
		}
		long[] help = new long[R - L + 1];
		int i = 0;
		int p1 = L;
		int p2 = M + 1;
		while (p1 <= M && p2 <= R) {
			help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
		}
		while (p1 <= M) {
			help[i++] = arr[p1++];
		}
		while (p2 <= R) {
			help[i++] = arr[p2++];
		}
		for (i = 0; i < help.length; i++) {
			arr[L + i] = help[i];
		}
		return ans;
	}

}
