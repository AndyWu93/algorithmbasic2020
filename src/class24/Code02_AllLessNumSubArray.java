package class24;

import java.util.LinkedList;

/**
 * arr的子数组subArray，当subArray的最大值-最小值<=num，为达标子数组，求达标子数组的数量
 * 暴力解：
 * 枚举所有子数组O(N^2),每个子数组求max，min O(N),判断是否达标，一共O(N^3)
 *
 * 本题可以优化到O(N)
 * 准备两个双端队列qmax(窗口内最大值),qmin(窗口内最小值)
 * 维护一个窗口[l,r],l从0开始，试着让l向右扩，同时更新qmax,qmin,直到窗口不达标了，
 * 此时，以l位置为开头的达标子数组数量为r-l,收集数量，
 * l++,更新qmax,qmin，
 * 在尝试扩r，直到不达标，收集数量
 * ...
 */
public class Code02_AllLessNumSubArray {

	// 暴力的对数器方法
	public static int right(int[] arr, int sum) {
		if (arr == null || arr.length == 0 || sum < 0) {
			return 0;
		}
		int N = arr.length;
		int count = 0;
		for (int L = 0; L < N; L++) {
			for (int R = L; R < N; R++) {
				int max = arr[L];
				int min = arr[L];
				for (int i = L + 1; i <= R; i++) {
					max = Math.max(max, arr[i]);
					min = Math.min(min, arr[i]);
				}
				if (max - min <= sum) {
					count++;
				}
			}
		}
		return count;
	}

	/**
	 * 最优解
	 * @param arr
	 * @param sum
	 * @return
	 */
	public static int num(int[] arr, int sum) {
		if (arr == null || arr.length == 0 || sum < 0) {
			return 0;
		}
		int N = arr.length;
		int count = 0;
		/*准备两个双段队列*/
		LinkedList<Integer> maxWindow = new LinkedList<>();
		LinkedList<Integer> minWindow = new LinkedList<>();
		/*R一直向右，不回退，所以共用一个R*/
		int R = 0;
		/*L从0开始，作为子数组的左端，枚举所有子数组的左端*/
		for (int L = 0; L < N; L++) {
			while (R < N) {
				/*维护qmax*/
				while (!maxWindow.isEmpty() && arr[maxWindow.peekLast()] <= arr[R]) {
					maxWindow.pollLast();
				}
				maxWindow.addLast(R);
				/*维护qmin*/
				while (!minWindow.isEmpty() && arr[minWindow.peekLast()] >= arr[R]) {
					minWindow.pollLast();
				}
				minWindow.addLast(R);
				/*当遇到首个R，让子数组不达标了，终止*/
				if (arr[maxWindow.peekFirst()] - arr[minWindow.peekFirst()] > sum) {
					break;
				} else {
					/*否则R往右扩*/
					R++;
				}
			}
			/*R到了首个不达标的位置，收集达标的数量*/
			count += R - L;
			/*L即将++了，看下两个双端队列的首位是否即将过期*/
			if (maxWindow.peekFirst() == L) {
				maxWindow.pollFirst();
			}
			if (minWindow.peekFirst() == L) {
				minWindow.pollFirst();
			}
		}
		return count;
	}

	// for test
	public static int[] generateRandomArray(int maxLen, int maxValue) {
		int len = (int) (Math.random() * (maxLen + 1));
		int[] arr = new int[len];
		for (int i = 0; i < len; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1)) - (int) (Math.random() * (maxValue + 1));
		}
		return arr;
	}

	// for test
	public static void printArray(int[] arr) {
		if (arr != null) {
			for (int i = 0; i < arr.length; i++) {
				System.out.print(arr[i] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int maxLen = 100;
		int maxValue = 200;
		int testTime = 100000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxLen, maxValue);
			int sum = (int) (Math.random() * (maxValue + 1));
			int ans1 = right(arr, sum);
			int ans2 = num(arr, sum);
			if (ans1 != ans2) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println(sum);
				System.out.println(ans1);
				System.out.println(ans2);
				break;
			}
		}
		System.out.println("测试结束");

	}

}
