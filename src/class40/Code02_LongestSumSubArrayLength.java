package class40;

import java.util.HashMap;

/**
 * 数组二连：
 * 数组一连中的arr存在正数、负数和0，这时怎么解？
 * 解题思路：
 * 	没有单调性，窗口不可用
 * 	所以子数组问题的经典解法：枚举每个位置结尾情况下的子数组，分别求解
 * 用前缀和加速
 * 	来到i位置，需要求以i位置结尾的子数组sum=k最长多大，就等价于求arr[0..?]首次出现sum[?]=sum[i]-k,求出？的位置，具体流程：
 * 	准备一个map，key：sum，value：该sum出现的最早位置'
 * 	准备一个变量sum，沿途累加
 * 	遍历来到i位置，
 * 	sum累加，
 * 	求sum-k出现的最早位置
 * 	存在该位置p,收集答案i-p，(sum，i)如果不在map里加进去
 * 	不存在该位置p,(sum，i)如果不在map里加进去
 * 以上流程有什么问题？
 * 	求sum-k出现的最早位置时，如果sum-k==0，将找不到该位置，而这个位置就是-1，
 * 	所有要把(0,-1)提前加入到map中，以免错过任何以0开头的子数组长度
 *
 * 扩展：arr中存在正，负，0，求-1和1的数量是一样多的最长子数组长度
 * 	这道题可以转化：将arr中除了1、-1，其他数都转为0，这样就相当于求arr'中累加和为0的最长子数组长度
 */
public class Code02_LongestSumSubArrayLength {

	public static int maxLength(int[] arr, int k) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		// key:前缀和
		// value : 0~value这个前缀和是最早出现key这个值的
		HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
		/*非常重要*/
		map.put(0, -1); // important
		int len = 0;
		int sum = 0;
		for (int i = 0; i < arr.length; i++) {
			sum += arr[i];
			/*找sum-k首次出现的位置，找到以后，收集答案*/
			if (map.containsKey(sum - k)) {
				len = Math.max(i - map.get(sum - k), len);
			}
			/*看一下map中优美sum这个key，有的话不用看了，没有再加进去*/
			if (!map.containsKey(sum)) {
				map.put(sum, i);
			}
		}
		return len;
	}

	// for test
	public static int right(int[] arr, int K) {
		int max = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i; j < arr.length; j++) {
				if (valid(arr, i, j, K)) {
					max = Math.max(max, j - i + 1);
				}
			}
		}
		return max;
	}

	// for test
	public static boolean valid(int[] arr, int L, int R, int K) {
		int sum = 0;
		for (int i = L; i <= R; i++) {
			sum += arr[i];
		}
		return sum == K;
	}

	// for test
	public static int[] generateRandomArray(int size, int value) {
		int[] ans = new int[(int) (Math.random() * size) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (int) (Math.random() * value) - (int) (Math.random() * value);
		}
		return ans;
	}

	// for test
	public static void printArray(int[] arr) {
		for (int i = 0; i != arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int len = 50;
		int value = 100;
		int testTime = 500000;

		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(len, value);
			int K = (int) (Math.random() * value) - (int) (Math.random() * value);
			int ans1 = maxLength(arr, K);
			int ans2 = right(arr, K);
			if (ans1 != ans2) {
				System.out.println("Oops!");
				printArray(arr);
				System.out.println("K : " + K);
				System.out.println(ans1);
				System.out.println(ans2);
				break;
			}
		}
		System.out.println("test end");

	}

}
