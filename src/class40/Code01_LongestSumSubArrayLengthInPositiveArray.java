package class40;

/**
 * 数组一连
 * 给定一个正整数组成的无序数组arr，给定一个正整数值K
 * 找到arr的所有子数组里，哪个子数组的累加和等于K，并且是长度最大的返回其长度
 * 解题流程：
 * 	使用窗口L.R，
 * 	当发现窗口内的数sum小于k时，R++
 * 	当发现窗口内的数sum大于k时，L++
 * 	当发现窗口内的数sum等于k时，收集答案，后R++
 * 思考1：这个流程为什么对？
 * 	其实流程做的事情就是
 * 	求以arr[0]位置开头的最长子数组长度
 * 	求以arr[1]位置开头的最长子数组长度
 * 	求以arr[2]位置开头的最长子数组长度
 * 	...
 * 思考2：这个流程如何兼容0？
 * 	当发现窗口内的数sum等于k时，收集答案，后R++
 * 	这里让R++，如果后面是0，那下一次还会进这个条件，且长度加1
 * 思考3：为什么能够用窗口来解题？
 * 	因为arr都是正数，存在单调性，窗口变大，sum变大，窗口变小，sum变小
 * 总结：
 * 	存在单调性的问题可以用窗口
 */
public class Code01_LongestSumSubArrayLengthInPositiveArray {

	public static int getMaxLength(int[] arr, int K) {
		if (arr == null || arr.length == 0 || K <= 0) {
			return 0;
		}
		int left = 0;
		int right = 0;
		int sum = arr[0];
		int len = 0;
		while (right < arr.length) {
			if (sum == K) {
				len = Math.max(len, right - left + 1);
				/*这里都是正数，相等的时候可以L++*/
				sum -= arr[left++];
			} else if (sum < K) {
				right++;
				if (right == arr.length) {
					break;
				}
				sum += arr[right];
			} else {
				sum -= arr[left++];
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
	public static int[] generatePositiveArray(int size, int value) {
		int[] ans = new int[size];
		for (int i = 0; i != size; i++) {
			ans[i] = (int) (Math.random() * value) + 1;
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
			int[] arr = generatePositiveArray(len, value);
			int K = (int) (Math.random() * value) + 1;
			int ans1 = getMaxLength(arr, K);
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
