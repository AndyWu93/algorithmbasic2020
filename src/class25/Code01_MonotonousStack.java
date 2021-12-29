package class25;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

/**
 * 单调栈
 */
public class Code01_MonotonousStack {

	/**
	 * 无重复值arr的单调栈
	 *
	 * 返回一个N*2的二维数组
	 * 保存了arr中每个位置i：
	 * 左边最近比arr[i]小的数，右边最近比arr[i]小的数
	 */
	// arr = [ 3, 1, 2, 3]
	//         0  1  2  3
	//  [
	//     0 : [-1,  1]
	//     1 : [-1, -1]
	//     2 : [ 1, -1]
	//     3 : [ 2, -1]
	//  ]
	public static int[][] getNearLessNoRepeat(int[] arr) {
		int[][] res = new int[arr.length][2];
		// 只存位置！
		Stack<Integer> stack = new Stack<>();
		for (int i = 0; i < arr.length; i++) { // 当遍历到i位置的数，arr[i]
			while (!stack.isEmpty() && arr[stack.peek()] > arr[i]) {
				/*i压不进去，需要弹出当前栈顶*/
				int j = stack.pop();
				/*结算左边：新的栈顶的位置*/
				int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
				res[j][0] = leftLessIndex;
				/*结算右边：让j弹出的i*/
				res[j][1] = i;
			}
			/*直达i能够压进去*/
			stack.push(i);
		}
		/*arr遍历完了，结算栈中的数*/
		while (!stack.isEmpty()) {
			int j = stack.pop();
			/*结算左边：新的栈顶的位置*/
			int leftLessIndex = stack.isEmpty() ? -1 : stack.peek();
			res[j][0] = leftLessIndex;
			/*结算右边：无*/
			res[j][1] = -1;
		}
		return res;
	}

	/**
	 * 有重复值
	 */
	public static int[][] getNearLess(int[] arr) {
		int[][] res = new int[arr.length][2];
		Stack<List<Integer>> stack = new Stack<>();
		for (int i = 0; i < arr.length; i++) { // i -> arr[i] 进栈
			/*跟当前栈顶链表的任意位置比较即可*/
			while (!stack.isEmpty() && arr[stack.peek().get(0)] > arr[i]) {
				/*i压不进去，弹出栈顶的链表*/
				List<Integer> popIs = stack.pop();
				/*结算左边：下面一个链表的最后一个位置*/
				int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
				for (Integer popi : popIs) {
					/*弹出链表中的所有位置的结算信息都是一样的*/
					res[popi][0] = leftLessIndex;
					/*结算右边：让当前链表弹出的i*/
					res[popi][1] = i;
				}
			}
			/*不用弹出了：此时栈顶代表的数可能等于arr[i],也可能大于arr[i]*/
			if (!stack.isEmpty() && arr[stack.peek().get(0)] == arr[i]) {
				/*等于，加到链表最后*/
				stack.peek().add(Integer.valueOf(i));
			} else {
				/*大于：开一个新链表，压进去*/
				ArrayList<Integer> list = new ArrayList<>();
				list.add(i);
				stack.push(list);
			}
		}
		while (!stack.isEmpty()) {
			List<Integer> popIs = stack.pop();
			int leftLessIndex = stack.isEmpty() ? -1 : stack.peek().get(stack.peek().size() - 1);
			for (Integer popi : popIs) {
				res[popi][0] = leftLessIndex;
				res[popi][1] = -1;
			}
		}
		return res;
	}

	// for test
	public static int[] getRandomArrayNoRepeat(int size) {
		int[] arr = new int[(int) (Math.random() * size) + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = i;
		}
		for (int i = 0; i < arr.length; i++) {
			int swapIndex = (int) (Math.random() * arr.length);
			int tmp = arr[swapIndex];
			arr[swapIndex] = arr[i];
			arr[i] = tmp;
		}
		return arr;
	}

	// for test
	public static int[] getRandomArray(int size, int max) {
		int[] arr = new int[(int) (Math.random() * size) + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
		}
		return arr;
	}

	// for test
	public static int[][] rightWay(int[] arr) {
		int[][] res = new int[arr.length][2];
		for (int i = 0; i < arr.length; i++) {
			int leftLessIndex = -1;
			int rightLessIndex = -1;
			int cur = i - 1;
			while (cur >= 0) {
				if (arr[cur] < arr[i]) {
					leftLessIndex = cur;
					break;
				}
				cur--;
			}
			cur = i + 1;
			while (cur < arr.length) {
				if (arr[cur] < arr[i]) {
					rightLessIndex = cur;
					break;
				}
				cur++;
			}
			res[i][0] = leftLessIndex;
			res[i][1] = rightLessIndex;
		}
		return res;
	}

	// for test
	public static boolean isEqual(int[][] res1, int[][] res2) {
		if (res1.length != res2.length) {
			return false;
		}
		for (int i = 0; i < res1.length; i++) {
			if (res1[i][0] != res2[i][0] || res1[i][1] != res2[i][1]) {
				return false;
			}
		}

		return true;
	}

	// for test
	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	public static void main(String[] args) {
		int size = 10;
		int max = 20;
		int testTimes = 2000000;
		System.out.println("测试开始");
		for (int i = 0; i < testTimes; i++) {
			int[] arr1 = getRandomArrayNoRepeat(size);
			int[] arr2 = getRandomArray(size, max);
			if (!isEqual(getNearLessNoRepeat(arr1), rightWay(arr1))) {
				System.out.println("Oops!");
				printArray(arr1);
				break;
			}
			if (!isEqual(getNearLess(arr2), rightWay(arr2))) {
				System.out.println("Oops!");
				printArray(arr2);
				break;
			}
		}
		System.out.println("测试结束");
	}
}
