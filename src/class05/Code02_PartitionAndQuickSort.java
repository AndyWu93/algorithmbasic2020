package class05;

/**
 * 荷兰国旗问题的解法
 * 1.要求<=x的在左边，>x的在右边
 * 随机找一个标准x，定义小于等于区域
 * 遍历数组，来到i位置，
 * 如果arr[i]<=x，将arr[i]跟小于等于区域下一个数交换，然后小于等于区域+1，来到i+1位置
 * 如果arr[i]>x，来到i+1位置
 * 2.要求<x的在左边，>x的在右边，=x的在中间
 * 随机找一个标准x，定义小于区域、大于区域
 * 遍历数组，来到i位置，
 * 如果arr[i]<x，将arr[i]跟小于区域下一个数交换，然后小于区域+1，来到i+1位置
 * 如果arr[i]>x，将arr[i]跟大于区域前一个数交换，然后大于区域-1，停在i位置
 * 如果arr[i]=x，来到i+1位置
 * 1、2的时间复杂度O(N)，空间复杂度O(1)
 */
public class Code02_PartitionAndQuickSort {

	public static void swap(int[] arr, int i, int j) {
		int tmp = arr[i];
		arr[i] = arr[j];
		arr[j] = tmp;
	}

	/**
	 * 要求<=x的在左边，>x的在右边
	 * 找一个标准x，这里是arr[R],定义小于等于区域
	 * 遍历数组到R-1，来到i位置
	 * 如果arr[i]<=x,arr[i]跟小于等于区域下一个数交换，然后小于等于区域+1，来到i+1位置
	 * 如果arr[i]>x，来到i+1位置
	 * 最后将arr[R]放在小于等于区域后面一个数上
	 */
	// arr[L..R]上，以arr[R]位置的数做划分值
	// <= X > X
	// <= X X
	public static int partition(int[] arr, int L, int R) {
		if (L > R) {
			return -1;
		}
		if (L == R) {
			return L;
		}
		int lessEqual = L - 1;
		int index = L;
		while (index < R) {
			if (arr[index] <= arr[R]) {
				swap(arr, index, ++lessEqual);
			}
			index++;
		}
		/*标准最终放在了中间*/
		swap(arr, ++lessEqual, R);
		return lessEqual;
	}

	/**
	 * 要求<x的在左边，>x的在右边，=x的在中间
	 * 找一个标准x，这里是arr[R]，定义小于区域、大于区域
	 * 遍历数组到R-1，来到i位置，
	 * 如果arr[i]<x，将arr[i]跟小于区域下一个数交换，然后小于区域+1，来到i+1位置
	 * 如果arr[i]>x，将arr[i]跟大于区域前一个数交换，然后大于区域-1，停在i位置
	 * 如果arr[i]=x，来到i+1位置
	 * 最后将arr[R]放在小于区域后面一个数上
	 * @return 等于x的下标[L..R]
	 */
	// arr[L...R] 玩荷兰国旗问题的划分，以arr[R]做划分值
	// <arr[R] ==arr[R] > arr[R]
	public static int[] netherlandsFlag(int[] arr, int L, int R) {
		if (L > R) { // L...R L>R
			/*无效范围*/
			return new int[] { -1, -1 };
		}
		if (L == R) {
			return new int[] { L, R };
		}
		int less = L - 1; // < 区 右边界
		/*大于区域因为r位置的数暂不参与，所以直接归到大于区域里*/
		int more = R; // > 区 左边界
		/*来到了index位置*/
		int index = L;
		while (index < more) { // 当前位置，不能和 >区的左边界撞上
			if (arr[index] == arr[R]) {
				index++;
			} else if (arr[index] < arr[R]) {
//				swap(arr, less + 1, index);
//				less++;
//				index++;						
				swap(arr, index++, ++less);
			} else { // >
				swap(arr, index, --more);
			}
		}
		/*R位置的数归位：和大于区域的第一个数交换*/
		swap(arr, more, R); // <[R]   =[R]   >[R]
		/*less是小于区域的最后一个位置，more是R换过来后的位置*/
		return new int[] { less + 1, more };
	}

	/**
	 * 一次搞定一个数：<=x的在左边，>x的在右边
	 * 时间复杂度O(N^2)：最差情况[1,2,3,4,5,6,7]
	 * 按7为标准，过了6个数，按6为标准，过了5个数...
	 * @param arr
	 */
	public static void quickSort1(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		process1(arr, 0, arr.length - 1);
	}

	public static void process1(int[] arr, int L, int R) {
		if (L >= R) {
			return;
		}
		// L..R partition arr[R] [ <=arr[R] arr[R] >arr[R] ]
		int M = partition(arr, L, R);
		process1(arr, L, M - 1);
		process1(arr, M + 1, R);
	}





	/**
	 * 一次搞定一个数：<x的在左边，>x的在右边，=x的在中间
	 * 时间复杂度O(N^2)：最差情况[1,2,3,4,5,6,7]
	 * 按7为标准，过了6个数，按6为标准，过了5个数...
	 * @param arr
	 */
	public static void quickSort2(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		process2(arr, 0, arr.length - 1);
	}

	// arr[L...R] 排有序，快排2.0方式
	public static void process2(int[] arr, int L, int R) {
		if (L >= R) {
			return;
		}
		// [ equalArea[0]  ,  equalArea[0]]
		int[] equalArea = netherlandsFlag(arr, L, R);
		process2(arr, L, equalArea[0] - 1);
		process2(arr, equalArea[1] + 1, R);
	}


	/**
	 * 基于2.0，仅加一行代码
	 * 时间复杂度O(N*logN)
	 * 空间复杂度
	 * 记住了一个中间位置的，用数组记住，
	 * 最差：不复用数组O(N)
	 * 最好：复用数组O(logN)
	 * @param arr
	 */
	public static void quickSort3(int[] arr) {
		if (arr == null || arr.length < 2) {
			return;
		}
		process3(arr, 0, arr.length - 1);
	}

	public static void process3(int[] arr, int L, int R) {
		if (L >= R) {
			return;
		}
		/*随机选一个数*/
		swap(arr, L + (int) (Math.random() * (R - L + 1)), R);
		int[] equalArea = netherlandsFlag(arr, L, R);
		process3(arr, L, equalArea[0] - 1);
		process3(arr, equalArea[1] + 1, R);
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
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr1 = generateRandomArray(maxSize, maxValue);
			int[] arr2 = copyArray(arr1);
			int[] arr3 = copyArray(arr1);
			quickSort1(arr1);
			quickSort2(arr2);
			quickSort3(arr3);
			if (!isEqual(arr1, arr2) || !isEqual(arr2, arr3)) {
				succeed = false;
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Oops!");

	}

}
