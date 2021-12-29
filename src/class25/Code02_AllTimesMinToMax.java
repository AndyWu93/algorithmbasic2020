package class25;

import java.util.Stack;

/**
 * 给定一个正数数组arr，以arr中任意一个子数组的和*它的最小值为指标，求指标的最大值
 * 暴力解：枚举子数组，再求和*最小值，O(N^3)，可以作对数器
 * 最优解O(N)思路：
 * 遍历arr，来到i位置，找出以i位置为最小值的最长（因为都是正数）子数组，求和乘以arr[i]，得到指标x，收集最大值。
 * 如何遍历一次，就得到以任意位置为最小值的最长子数组？单调栈O(N)
 * 如何求子数组的和，前缀和预处理数组O(N)+O(1)
 *
 */
public class Code02_AllTimesMinToMax {

	/**
	 * 暴力解
	 */
	public static int max1(int[] arr) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j < arr.length; j++) {
				int minNum = Integer.MAX_VALUE;
				int sum = 0;
				for (int k = i; k <= j; k++) {
					sum += arr[k];
					minNum = Math.min(minNum, arr[k]);
				}
				max = Math.max(max, minNum * sum);
			}
		}
		return max;
	}

	/**
	 * 最优解
	 */
	public static int max2(int[] arr) {
		int size = arr.length;
		int[] sums = new int[size];
		sums[0] = arr[0];
		for (int i = 1; i < size; i++) {
			/*前缀和预处理数组*/
			sums[i] = sums[i - 1] + arr[i];
		}

		int max = Integer.MIN_VALUE;
		/*
		* 单调栈中只能放一个值，如何处理相等的情况？
		* 单调栈中依然保持着从底到顶部严格从小到大，弹出栈顶的时候，结算子数组的特性
		* 当遇到一个相等的数将自己弹出，自己结算的特性可能是错的，
		* 不过不要紧，最后一个与自己相等的数一定是扩到了最大的长度的子数组，一定是对的。
		* 本题只需要求特性的最大值，哪些求错的特性不会影响最后的最大值
		* */
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < size; i++) {
			/*遇到相等的弹出*/
			while (!stack.isEmpty() && arr[stack.peek()] >= arr[i]) {
				int j = stack.pop();
				/*
				* 弹出后栈是空的：i位置是以j位置为最小值时扩不到的位置，栈又是空的，所以此时的子数组长度为[0,i-1]
				* 求和方式为sum[i-1]
				* 弹出后栈不是空的：i位置是以j位置为最小值时扩不到的位置，栈不空，所以此时的子数组长度为[stack.peek()+1,i-1]
				* 求和方式为sum[i-1] - sum[stack.peek()]
				* */
				max = Math.max(max, (stack.isEmpty() ? sums[i - 1] : (sums[i - 1] - sums[stack.peek()])) * arr[j]);
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			int j = stack.pop();
			max = Math.max(max, (stack.isEmpty() ? sums[size - 1] : (sums[size - 1] - sums[stack.peek()])) * arr[j]);
		}
		return max;
	}

	public static int[] gerenareRondomArray() {
		int[] arr = new int[(int) (Math.random() * 20) + 10];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * 101);
		}
		return arr;
	}

	public static void main(String[] args) {
		int testTimes = 2000000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			int[] arr = gerenareRondomArray();
			if (max1(arr) != max2(arr)) {
				System.out.println("FUCK!");
				break;
			}
		}
		System.out.println("test finish");
	}

}
