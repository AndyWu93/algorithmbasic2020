package class04;

/**
 * 针对数组中的任意数x，统计右边所有2倍之后依然小于x的数，求总个数
 * 思路：对于任意数x，需要知道右边比自己的一半还小的数
 * merge的时候，先统计一下个数，再进行merge
 * 统计方式：左右各一个指针i,j，两个指针都不回退
 * 来到i位置，看j能否往右滑动，然后结算右组滑过的所有数
 * i++时每次累加，就是题解
 */
public class Code04_BiggerThanRightTwice {

	public static int biggerTwice(int[] arr) {
		if (arr == null || arr.length < 2) {
			return 0;
		}
		return process(arr, 0, arr.length - 1);
	}

	public static int process(int[] arr, int l, int r) {
		if (l == r) {
			return 0;
		}
		// l < r
		int mid = l + ((r - l) >> 1);
		return process(arr, l, mid) + process(arr, mid + 1, r) + merge(arr, l, mid, r);
	}

	public static int merge(int[] arr, int L, int m, int r) {
		// [L....M]   [M+1....R]
		
		int ans = 0;
		// 目前囊括进来的数，是从[M+1, windowR)
		/*windowR是指针，代表该数不符合要求了*/
		int windowR = m + 1;
		/*遍历L..M，左组的数*/
		for (int i = L; i <= m; i++) {
			while (windowR <= r && arr[i] > (arr[windowR] * 2)) {
				/*windowR没有++前是符合要求的，最后一次++后不符合要求了*/
				windowR++;
			}
			/*所以windowR需要减掉当前指针的数*/
			ans += windowR - m - 1;
		}
		
		
		int[] help = new int[r - L + 1];
		int i = 0;
		int p1 = L;
		int p2 = m + 1;
		while (p1 <= m && p2 <= r) {
			help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
		}
		while (p1 <= m) {
			help[i++] = arr[p1++];
		}
		while (p2 <= r) {
			help[i++] = arr[p2++];
		}
		for (i = 0; i < help.length; i++) {
			arr[L + i] = help[i];
		}
		return ans;
	}

	// for test
	public static int comparator(int[] arr) {
		int ans = 0;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				if (arr[i] > (arr[j] << 1)) {
					ans++;
				}
			}
		}
		return ans;
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) ((maxValue + 1) * Math.random());
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
		int maxValue = 100;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
			int[] arr2 = copyArray(arr1);
			if (biggerTwice(arr1) != comparator(arr2)) {
				System.out.println("Oops!");
				printArray(arr1);
				printArray(arr2);
				break;
			}
		}
		System.out.println("测试结束");
	}

}
