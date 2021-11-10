package class01;

import java.util.Arrays;

/**
 * 有序数组中，查找num是否存在
 * 二分思路：只要能正确构建左右两侧的淘汰逻辑，你就可以二分；比如无序数组partition查找一个第k大的数
 * 定义两个变量L,R来控制范围
 * 开始while循环，循环中不断缩小L，R之间的距离
 * 在一个范围中先找到中间点mid，
 * 如果mid位置的数比num大，那要找的num一定在num左边，R=mid-1
 * 如果mid位置的数比num小，那要找的num一定在num右边，R=mid+1
 * 直到相等了返回true
 */
public class Code04_BSExist {

	public static boolean exist(int[] sortedArr, int num) {
		if (sortedArr == null || sortedArr.length == 0) {
			return false;
		}
		int L = 0;
		int R = sortedArr.length - 1;
		int mid = 0;
		// L..R
		while (L < R) { // L..R 至少两个数的时候
			mid = L + ((R - L) >> 1);
			if (sortedArr[mid] == num) {
				return true;
			} else if (sortedArr[mid] > num) {
				R = mid - 1;
			} else {
				L = mid + 1;
			}
		}
		/*因为L,R上有两个数，剩一个还没验*/
		return sortedArr[L] == num;
	}
	
	// for test
	public static boolean test(int[] sortedArr, int num) {
		for(int cur : sortedArr) {
			if(cur == num) {
				return true;
			}
		}
		return false;
	}
	
	
	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
		}
		return arr;
	}
	
	public static void main(String[] args) {
		int testTime = 500000;
		int maxSize = 10;
		int maxValue = 100;
		boolean succeed = true;
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			Arrays.sort(arr);
			int value = (int) ((maxValue + 1) * Math.random()) - (int) (maxValue * Math.random());
			if (test(arr, value) != exist(arr, value)) {
				succeed = false;
				break;
			}
		}
		System.out.println(succeed ? "Nice!" : "Fucking fucked!");
	}

}
