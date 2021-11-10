package class04;

/**
 * 归并排序
 * 思路：定义一个函数
 * f(arr,L,R)在arr上让L--R有序
 * 只需要f(arr,L,M),f(arr,M+1,R)上各自有序
 * 再将两个排好序的arr merge到一起即可
 * 复杂度：T(N)=2T(N/2)+O(N)两个子递归，加一个merge，merge时指针不回退是O(N)
 * O(N*logN)
 * 相较于O(N^2)的排序，没有浪费之前比较后的结果
 *
 * merge过程：
 * 准备一个辅助数组，两个指针分别指向两边的数组，
 * 从各自的第一数开始，对比，谁小拷贝谁，拷贝后指针向右移动，继续比较
 * 直到一侧越界了，将另一次剩下的都拷贝完
 * 再将辅助数组中的数，放回原数组，原数组即有序了
 *
 * 应用：
 * merge过程中，因为将数组变为有序的，可以在这个过程中做很多事情
 */
public class Code01_MergeSort {

	// 递归方法实现
	public static void mergeSort1(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		process(arr, 0, arr.length - 1);
	}

	// 请把arr[L..R]排有序
	// l...r N
	// T(N) = 2 * T(N / 2) + O(N)
	// O(N * logN)
	public static void process(int[] arr, int L, int R) {
		if (L == R) { // base case
			return;
		}
		int mid = L + ((R - L) >> 1);
		process(arr, L, mid);
		process(arr, mid + 1, R);
		merge(arr, L, mid, R);
	}

	public static void merge(int[] arr, int L, int M, int R) {
		int[] help = new int[R - L + 1];
		/*help数组中的指针*/
		int i = 0;
		/*左组第一个位置*/
		int p1 = L;
		/*右组第一个位置*/
		int p2 = M + 1;
		while (p1 <= M && p2 <= R) {
			help[i++] = arr[p1] <= arr[p2] ? arr[p1++] : arr[p2++];
		}
		// 要么p1越界了，要么p2越界了，以下两个只会发生一个
		while (p1 <= M) {
			/*这时候p1还没有越界，还有剩下的数需要拷贝*/
			help[i++] = arr[p1++];
		}
		while (p2 <= R) {
			/*这时候p2还没有越界，还有剩下的数需要拷贝 */
			help[i++] = arr[p2++];
		}
		for (i = 0; i < help.length; i++) {
			arr[L + i] = help[i];
		}
	}

	/**
	 * 非递归方法实现
	 * 迭代版需要理解步长：
	 * 步长=1，整个数组中1..2,3..4,5..6,...两个数先比较
	 * 接下来步长*2
	 * 步长=2，
	 * 12..34,56..78步长为2的merge
	 * 接下来步长*2
	 * ...
	 * 注意：
	 * 某个步长时，如果最后一组长度不够，只有半组就不用merge，直接步长*2，在下次给merge掉
	 * 复杂度：步长变了O(logN)次，每次比较了一个数组的长度O(N)
	 * O(N*logN)
	 *
	 * @param arr
	 */
	public static void mergeSort2(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		int N = arr.length;
		// 步长
		int mergeSize = 1;
		/*步长不超过数组长度*/
		while (mergeSize < N) { // log N
			// 当前左组的，第一个位置
			int L = 0;
			/*L作为左组第一个位置，一定小于N*/
			while (L < N) {
				/*保证右组是有数的，如果步长大于下个需要merge的长度，就只有左组了，就结束*/
				if (mergeSize >= N - L) {
					break;
				}
				/*左组终点位置*/
				int M = L + mergeSize - 1;
				/*右组终点位置*/
				int R = M + Math.min(mergeSize, N - M - 1);
				merge(arr, L, M, R);
				/*L跳至下一个左组第一个位置*/
				L = R + 1;
			}
			// 防止溢出
			if (mergeSize > N / 2) {
				break;
			}
			/*步长*2，继续*/
			mergeSize <<= 1;
		}
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
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
			mergeSort1(arr1);
			mergeSort2(arr2);
			if (!isEqual(arr1, arr2)) {
				System.out.println("出错了！");
				printArray(arr1);
				printArray(arr2);
				break;
			}
		}
		System.out.println("测试结束");
	}

}
