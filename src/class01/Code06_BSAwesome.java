package class01;

/**
 * 局部最小
 * 定义：
 * 在0位置，如果1位置比0位置的数大，0位置的数局部最小
 * 在n-1位置，如果n-2位置比n-1位置的数大，n-1位置的数局部最小
 * 中间任意i位置，如果i-1、i+1位置的数比i位置都大，i位置局部最下
 * 思路：
 * 先处理掉0和n-1位置，如果两边向中间是下降的，中间必存在局部最小，再来到中间mid位置
 * 如果mid比右边的大，将右边界定义到mid左边
 * 如果mid比左边的大，将左边界定义到mid右边
 * 如果mid比两边的大，mid位置局部最小
 *
 */
public class Code06_BSAwesome {

	public static int getLessIndex(int[] arr) {
		if (arr == null || arr.length == 0) {
			return -1; // no exist
		}
		if (arr.length == 1 || arr[0] < arr[1]) {
			return 0;
		}
		if (arr[arr.length - 1] < arr[arr.length - 2]) {
			return arr.length - 1;
		}
		int left = 1;
		int right = arr.length - 2;
		int mid = 0;
		while (left < right) {
			mid = (left + right) / 2;
			if (arr[mid] > arr[mid - 1]) {
				right = mid - 1;
			} else if (arr[mid] > arr[mid + 1]) {
				left = mid + 1;
			} else {
				return mid;
			}
		}
		return left;
	}

}
