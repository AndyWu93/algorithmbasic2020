package class08;

import java.util.Arrays;

/**
 * 基数排序
 * 样本要求：10进制非负数（若有负数，可以统一都加成非负数再排序，排完再减掉）
 * 流程：
 * 遍历一遍arr，找到最大的数，看最大数是几位，遍历arr，将位数不足的数前面补0
 * 准备编号为0~9的10个FIFO的桶
 * 从个位开始，遍历arr，个位是几，就进几号桶，结束后，从0号桶开始，将数都倒出来
 * 再到十位，重复以上流程
 * ...
 * 最终成为有序
 */
public class Code04_RadixSort {

	// only for no-negative value
	public static void radixSort(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		radixSort(arr, 0, arr.length - 1, maxbits(arr));
	}

	/*求最大的数是几位数*/
	public static int maxbits(int[] arr) {
		int max = Integer.MIN_VALUE;
		for (int i = 0; i < arr.length; i++) {
			max = Math.max(max, arr[i]);
		}
		int res = 0;
		while (max != 0) {
			res++;
			max /= 10;
		}
		return res;
	}

	/**
	 * 在arr上index为L..R范围内排序
	 * @param arr
	 * @param L
	 * @param R
	 * @param digit 最大位数，固定参数
	 */
	// arr[L..R]排序  ,  最大值的十进制位数digit
	public static void radixSort(int[] arr, int L, int R, int digit) {
		final int radix = 10;
		int i = 0, j = 0;
		// 有多少个数准备多少个辅助空间
		int[] help = new int[R - L + 1];
		/*
		* 从个位开始，实现入桶、出桶
		* 出入桶优化：
		* 1.准备一个长度为10的数组count，遍历arr，给该位上的数（0~9）统计频次
		* 2.将count加工成累加和（前缀和）数组sum
		* sum[i]：arr中当前位小于等于i的有几个
		* 3.准备一个结果数组res，从后向前遍历arr，
		* 来到某个数x，假设当前遍历的数的当前位的值时a
		* 去sum中拿到sum[a],表示arr中当前位小于等于a的个数记为k，范围是0~k-1，
		* 因为从后往前遍历，所以x应该放在这个范围的最后一个，即k-1
		* 放完以后sum[a]--
		* */
		for (int d = 1; d <= digit; d++) { // 有多少位就进出几次
			// 10个空间
		    // count[0] 当前位(d位)是0的数字有多少个
			// count[1] 当前位(d位)是(0和1)的数字有多少个
			// count[2] 当前位(d位)是(0、1和2)的数字有多少个
			// count[i] 当前位(d位)是(0~i)的数字有多少个
			int[] count = new int[radix]; // count[0..9]
			for (i = L; i <= R; i++) {
				// 103  1   3
				// 209  1   9
				/*统计词频*/
				j = getDigit(arr[i], d);
				count[j]++;
			}
			for (i = 1; i < radix; i++) {
				/*转为累加和数组*/
				count[i] = count[i] + count[i - 1];
			}
			/*反向遍历*/
			for (i = R; i >= L; i--) {
				/*当前位*/
				j = getDigit(arr[i], d);
				/*当前位的数应该在哪个位置*/
				help[count[j] - 1] = arr[i];
				/*--*/
				count[j]--;
			}
			/*help数组中的数拷贝回去，准备开始下个位*/
			for (i = L, j = 0; i <= R; i++, j++) {
				arr[i] = help[j];
			}
		}
	}

	public static int getDigit(int x, int d) {
		return ((x / ((int) Math.pow(10, d - 1))) % 10);
	}

	// for test
	public static void comparator(int[] arr) {
		Arrays.sort(arr);
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random());
		}
		return arr;
	}

	// for test
	public static int[] copyArray(int[] arr) {
		if (arr == null) {
			return null;
		}
		int[] res = new int[arr.length];
		for (int i = 0; i < arr.length; i++) {
			res[i] = arr[i];
		}
		return res;
	}

	// for test
	public static boolean isEqual(int[] arr1, int[] arr2) {
		if ((arr1 == null && arr2 != null) || (arr1 != null && arr2 == null)) {
			return false;
		}
		if (arr1 == null && arr2 == null) {
			return true;
		}
		if (arr1.length != arr2.length) {
			return false;
		}
		for (int i = 0; i < arr1.length; i++) {
			if (arr1[i] != arr2[i]) {
				return false;
			}
		}
		return true;
	}

	// for test
	public static void printArray(int[] arr) {
		if (arr == null) {
			return;
		}
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// for test
	public static void main(String[] args) {
		int testTime = 500000;
		int maxSize = 100;
		int maxValue = 100000;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
			int[] arr2 = copyArray(arr1);
			radixSort(arr1);
			comparator(arr2);
			if (!isEqual(arr1, arr2)) {
				succeed = false;
				printArray(arr1);
				printArray(arr2);
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");

		int[] arr = generateRandomArray(maxSize, maxValue);
		printArray(arr);
		radixSort(arr);
		printArray(arr);

	}

}
