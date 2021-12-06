package class14;

import java.util.PriorityQueue;

/**
 * 给定一个数组arr，整个数组的和为sum，一个长度为sum的金条，切成arr的小段（无关顺序，如果需要按照arr顺序切，解法为四边形不等式难度爆表），
 * 每次切割话费当前切割的金条长度，问怎么切花费最少
 * 思路：暴力递归可以做对数器，最优解为贪心
 * 贪心流程（哈夫曼编码）
 * 利用小根堆建一棵树
 * 1.将arr:[2,1,7,3,4,2,1]放入小根堆heap中，变为[1,1,2,2,3,4,7]
 * 2.弹出两个数：1，1，将1+1得到的数2*放入heap中,[2*,2,2,3,4,7]
 *      2*
 *     / \
 *    1  1
 * 3.弹出两个数：2*，2，将2*+2得到的数4*放入heap中,[2,3,4*,4,7]
 *        4*
 *       / \
 *      2* 2
 *     / \
 *    1  1
 * 4.弹出两个数：2，3，将2+3得到的数5*放入heap中,[4*,4,5*,7]
 *        4*     5*
 *       / \    / \
 *      2* 2   2  3
 *     / \
 *    1  1
 * 5.弹出两个数：4*，4，将4*+4得到的数8*放入heap中,[5*,7,8*]
 *       8*
 *      / \
 *     4  4*     5*
 *       / \    / \
 *      2* 2   2  3
 *     / \
 *    1  1
 * 6.弹出两个数：5*，7，将5*+7得到的数12*放入heap中,[8*,12*]
 *       8*        12*
 *      / \       / \
 *     4  4*     5* 7
 *       / \    / \
 *      2* 2   2  3
 *     / \
 *    1  1
 * 7.弹出两个数：8*，12*，将8*+12*得到的数20*放入heap中,[20*]
 *            20*
 *        /       \
 *       8*        12*
 *      / \       / \
 *     4  4*     5* 7
 *       / \    / \
 *      2* 2   2  3
 *     / \
 *    1  1
 */
public class Code02_LessMoneySplitGold {

	/**
	 * 暴力递归
	 * @param arr
	 * @return
	 */
	// 纯暴力！
	public static int lessMoney1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		return process(arr, 0);
	}

	/*
	* 递归含义：将arr中的数如何合并在一起代价最小
	* arr: 等待合并的数都在arr里
	* pre: 之前的合并行为产生了多少总代价
	* arr中只剩一个数字的时候，表示都合并到一起了，停止合并，返回最小的总代价
	* */
	public static int process(int[] arr, int pre) {
		if (arr.length == 1) {
			return pre;
		}
		int ans = Integer.MAX_VALUE;
		for (int i = 0; i < arr.length; i++) {
			for (int j = i + 1; j < arr.length; j++) {
				/*枚举任意拿出来的两个数字合并起来，放回arr中，代价加上合并的两个值，给后续递归*/
				ans = Math.min(ans, process(copyAndMergeTwo(arr, i, j), pre + arr[i] + arr[j]));
			}
		}
		return ans;
	}

	public static int[] copyAndMergeTwo(int[] arr, int i, int j) {
		int[] ans = new int[arr.length - 1];
		int ansi = 0;
		for (int arri = 0; arri < arr.length; arri++) {
			if (arri != i && arri != j) {
				ans[ansi++] = arr[arri];
			}
		}
		ans[ansi] = arr[i] + arr[j];
		return ans;
	}

	/**
	 * 贪心
	 * @param arr
	 * @return
	 */
	public static int lessMoney2(int[] arr) {
		PriorityQueue<Integer> pQ = new PriorityQueue<>();
		for (int i = 0; i < arr.length; i++) {
			pQ.add(arr[i]);
		}
		/*一开始代价为0*/
		int sum = 0;
		int cur = 0;
		while (pQ.size() > 1) {
			cur = pQ.poll() + pQ.poll();
			/*cur一定是带*的，累加到sum中去*/
			sum += cur;
			/*放回当前两个数的和*/
			pQ.add(cur);
		}
		return sum;
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) ((maxSize + 1) * Math.random())];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1));
		}
		return arr;
	}

	public static void main(String[] args) {
		int testTime = 100000;
		int maxSize = 6;
		int maxValue = 1000;
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			if (lessMoney1(arr) != lessMoney2(arr)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");
	}

}
