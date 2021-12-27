package class24;

import java.util.LinkedList;

/**
 * 有一个环形的公路，给定两个数组：
 * gas：公路上各个加油站的油量
 * cost：从当前加油站到下个加油站需要耗费的油量
 * 返回从当前加油站开始，能否跑完一圈。返回一个boolean数组
 *
 * 该题解题思路：
 * 走完全程，意味着一个区间内不能存在中途没有油的情况，
 * 整个过程可以不回退，就可以可以用上窗口的概念。
 * 此外，尽量用上窗口内的最大/小值，窗口内最薄弱的点，就是窗口内的最小值
 * 解题流程：
 * 1.将gas-cost数组，得到数组arr。
 * 假设得到的arr是[-2,-5,2,6,-1],意味着0、1位置都是熄火的，只有从2、3位置开始才是一个好的开始，后面累加不会出现负数，即熄火的情况
 * 2.将arr加工成累加和数组sum，sum后面再加一个N长度的累加和数组，把arr再累加一遍
 * 得到2N长度的累加和数组sum
 * 3.准备一个N长度的窗口，在sum中移动。假设窗口L来到i位置
 * 窗口内的每一个数，减去sum[i-1],得到数组，就是以arr中i位置为起始位置，回到i位置的累加和数组
 * 只要该数组的最小值>=0,那么i位置最为开头就可以跑完一圈，否则不能
 */
// 测试链接：https://leetcode.com/problems/gas-station
public class Code03_GasStation {

	// 这个方法的时间复杂度O(N)，额外空间复杂度O(N)
	public static int canCompleteCircuit(int[] gas, int[] cost) {
		boolean[] good = goodArray(gas, cost);
		for (int i = 0; i < gas.length; i++) {
			if (good[i]) {
				return i;
			}
		}
		return -1;
	}

	public static boolean[] goodArray(int[] g, int[] c) {
		int N = g.length;
		int M = N << 1;
		/*准备一个2n长度的数组*/
		int[] arr = new int[M];
		for (int i = 0; i < N; i++) {
			/*数组中，第一个n长度和第二个n长度的数组的值为g，c两个数组的值相减得到*/
			arr[i] = g[i] - c[i];
			arr[i + N] = g[i] - c[i];
		}
		for (int i = 1; i < M; i++) {
			/*将arr加工为累加和数组*/
			arr[i] += arr[i - 1];
		}
		/*n长度窗口中的最小值*/
		LinkedList<Integer> w = new LinkedList<>();
		for (int i = 0; i < N; i++) {
			while (!w.isEmpty() && arr[w.peekLast()] >= arr[i]) {
				w.pollLast();
			}
			w.addLast(i);
		}
		boolean[] ans = new boolean[N];
		/*
		* 维护了一个n长度窗口[i,j),以及该窗口前一位置的值offset
		* for循环内部动作：i位置的数出窗口，j位置的数进窗口
		* 然后赋值offset，且i++,j++
		* */
		for (int offset = 0, i = 0, j = N; j < M; offset = arr[i++], j++) {
			/*结算，此时窗口内的最小值，减去offset大于等于0，表示整个环形内没有负数，即熄火的情况*/
			if (arr[w.peekFirst()] - offset >= 0) {
				ans[i] = true;
			}
			/*i位置即将过期*/
			if (w.peekFirst() == i) {
				w.pollFirst();
			}
			while (!w.isEmpty() && arr[w.peekLast()] >= arr[j]) {
				w.pollLast();
			}
			/*j位置加进来*/
			w.addLast(j);
		}
		return ans;
	}

}
