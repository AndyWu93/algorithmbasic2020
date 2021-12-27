package class24;

import java.util.LinkedList;

/**
 * 固定大小为W的窗口，依次划过arr，求每一次滑出状况的最大值
 *
 */
public class Code01_SlidingWindowMaxArray {

	// 暴力的对数器方法
	public static int[] right(int[] arr, int w) {
		if (arr == null || w < 1 || arr.length < w) {
			return null;
		}
		int N = arr.length;
		int[] res = new int[N - w + 1];
		int index = 0;
		int L = 0;
		int R = w - 1;
		while (R < N) {
			int max = arr[L];
			for (int i = L + 1; i <= R; i++) {
				max = Math.max(max, arr[i]);

			}
			res[index++] = max;
			L++;
			R++;
		}
		return res;
	}

	/**
	 * 双端队列
	 * @param arr
	 * @param w
	 * @return
	 */
	public static int[] getMaxWindow(int[] arr, int w) {
		if (arr == null || w < 1 || arr.length < w) {
			return null;
		}
		// qmax 窗口最大值的更新结构
		// 放下标
		LinkedList<Integer> qmax = new LinkedList<Integer>();
		/*结果集长度一定是N-w+1*/
		int[] res = new int[arr.length - w + 1];
		int index = 0;
		/*R:窗口的滑动，往右滑动不回退，这里不需要L，因为窗口固定大小*/
		for (int R = 0; R < arr.length; R++) {
			while (!qmax.isEmpty() && arr[qmax.peekLast()] <= arr[R]) {
				/*q中的尾巴代表的值如果比此时进入的数小或者相等，需要弹出，直到q空了，或者尾巴代表的值大于此时的数*/
				qmax.pollLast();
			}
			/*加入此时的数*/
			qmax.addLast(R);
			/*此时L-1位置（也就是R往左边w个数）的数应该要过期了，看一下q中头部的index是不是要过期的index，是的话弹出*/
			if (qmax.peekFirst() == R - w) {
				qmax.pollFirst();
			}
			/*此时R如果到了w-1，即形成了w大小的窗口，该收集最大值了*/
			if (R >= w - 1) {
				res[index++] = arr[qmax.peekFirst()];
			}
		}
		return res;
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1));
		}
		return arr;
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

	public static void main(String[] args) {
		int testTime = 100000;
		int maxSize = 100;
		int maxValue = 100;
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			int w = (int) (Math.random() * (arr.length + 1));
			int[] ans1 = getMaxWindow(arr, w);
			int[] ans2 = right(arr, w);
			if (!isEqual(ans1, ans2)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
