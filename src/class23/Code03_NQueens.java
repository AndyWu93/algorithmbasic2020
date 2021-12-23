package class23;

/**
 * 无法dp的暴力递归（记忆化搜索也不行）
 * 在N*N的棋盘上放N个皇后，要求所有皇后不同行，不同列，不共斜线
 * 思路：
 * 从第一行开始往下放，每行只放一个，保证了不共行
 * 放好的每个皇后记下位置(i,j)，用与后续皇后的位置比对
 * 复杂度：每一行都可以有n种决策，O(N^N)
 *
 * 进阶：
 * 如果N<=32,可以用int的位代表皇后的位置，以及限制
 */
public class Code03_NQueens {

	public static int num1(int n) {
		if (n < 1) {
			return 0;
		}
		int[] record = new int[n];
		return process1(0, record, n);
	}

	/*
	* 递归含义：
	* 当前来到第i行，一共是0~N-1行
	* 之前的皇后都已近放好了，保存在int[] record ：record[x] = y 之前的第x行的皇后，放在了y列上
	* 当前行需要放一个皇后，必须要保证跟之前所有的皇后不打架
	* 返回：不关心i以上发生了什么，i.... 后续有多少合法的方法数
	* */
	public static int process1(int i, int[] record, int n) {
		if (i == n) {
			/*已经来到了第n行，表示之前的皇后都放完了，放完了n个，收集一种有效方法*/
			return 1;
		}
		int res = 0;
		/*当前行的皇后放在哪一列？枚举每个位置*/
		for (int j = 0; j < n; j++) {
			if (isValid(record, i, j)) {
				/*如果该位置和之前的皇后都不打架，放好以后去下一行放皇后*/
				record[i] = j;
				res += process1(i + 1, record, n);
				/*
				* 放完回来了，再试该行的下个位置j+1的时候，record为什么不需要还原现场？
				* 因为isValid方法比对的时候，只用到该行（i）之前的数据，该行后面的脏数据不需要比对
				* */
			}
		}
		return res;
	}

	/*
	* 如何比对？
	* 数组中i位置之前的数，表示i行之前的所有皇后，都需要参与比对
	* 如何比对不共斜线？
	* (x,y) (i,j)
	* Math.abs(x - i) == Math.abs(y - j) ? 共斜线 ：不共斜线
	 * */
	public static boolean isValid(int[] record, int i, int j) {
		// 0..i-1
		for (int k = 0; k < i; k++) {
			if (j == record[k] || Math.abs(record[k] - j) == Math.abs(i - k)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 进阶：位运算
	 * @param n
	 * @return
	 */
	// 请不要超过32皇后问题
	public static int num2(int n) {
		if (n < 1 || n > 32) {
			return 0;
		}
		/*
		* limit：如果你是13皇后问题，limit 最右13个1，其他都是0
		* 后面有用
		* */
		int limit = n == 32 ? -1 : (1 << n) - 1;
		return process2(limit, 0, 0, 0);
	}

	/*
	* 递归：
	* limit：固定参数
	* 以下3个限制均是由之前各行的皇后影响综合而来
	* colLim：当前行的列限制，有1的地方不能放皇后
	* leftDiaLim：当前行的左斜线限制，有1的地方不能放皇后
	* rightDiaLim：当前行的右斜线限制，有1的地方不能放皇后
	* */
	public static int process2(int limit, int colLim, int leftDiaLim, int rightDiaLim) {
		if (colLim == limit) {
			/*当前行的列限制中都是1了，表示皇后填满N个了，收集到一种有效方法*/
			return 1;
		}
		/*计算出可以尝试皇后的位置：pos中所有是1的位置*/
		int pos = limit & (~(colLim | leftDiaLim | rightDiaLim));
		int mostRightOne = 0;
		/*如果跳过了while，返回0表示之前点的位置有问题，后面的皇后点不了了*/
		int res = 0;
		while (pos != 0) {
			/*pos最右侧的1：x & -x*/
			mostRightOne = pos & (~pos + 1);
			/*把pos最后侧的1消掉，表示这个位置点了皇后了，直到pos没有1了，退出while*/
			pos = pos - mostRightOne;
			/*点了mostRightOne 1位置的皇后*/
			res += process2(
					limit,
					/*对下一行列的影响：该位置也要加上1*/
					colLim | mostRightOne,
					/*
					* 对下一行左斜线位置的影响：先把这个位置的1点上去，然后整体左移
					* 为什么要整体左移？因为目前左斜线不能共线的位置，到了下一行，就是整体左移后不能共线的位置
					* */
					(leftDiaLim | mostRightOne) << 1,
					/*
					* 下一行右斜线同理
					* 另：不存在<<<符号
					* */
					(rightDiaLim | mostRightOne) >>> 1);
		}
		return res;
	}

	public static void main(String[] args) {
		int n = 15;

		long start = System.currentTimeMillis();
		System.out.println(num2(n));
		long end = System.currentTimeMillis();
		System.out.println("cost time: " + (end - start) + "ms");

		start = System.currentTimeMillis();
		System.out.println(num1(n));
		end = System.currentTimeMillis();
		System.out.println("cost time: " + (end - start) + "ms");

	}
}
