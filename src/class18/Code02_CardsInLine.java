package class18;

/**
 * 两个选手AB，从纸牌arr中拿牌，A先手，假设AB都绝顶聪明，问最终赢家的分数
 * 思路：本题是动态规划，范围尝试模型
 * 两张dp表，相互依赖，规模：dp1[n][n],dp2[n][n],因为是范围尝试模型，所以左下角位置都是无效位置
 * 求值max(dp1[0][n-1],dp2[0][n-1])
 * 分析依赖关系：
 * dp1[i][j]依赖dp2[i+1][j]、dp2[i][j-1]
 * dp2[i][j]依赖dp1[i+1][j]、dp1[i][j-1]
 * 两张表对角线的位置可以先求出
 * 在根据自己对角线位置求对方第二条对角线位置的值
 */
public class Code02_CardsInLine {

	/**
	 * 递归设计思路：
	 * 应当将整个流程抽象化，不应该包含业务信息。所以本题流程中有先手、后手，且他们的角色都存在角色互换。
	 * 所以设计中抽象出先手后手，他们互相调用
	 * @param arr
	 * @return
	 */
	// 根据规则，返回获胜者的分数
	public static int win1(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int first = f1(arr, 0, arr.length - 1);
		int second = g1(arr, 0, arr.length - 1);
		return Math.max(first, second);
	}

	 /*
	 * 先手流程：arr[L..R],先手获得的最好分数返回
	 */
	public static int f1(int[] arr, int L, int R) {
		if (L == R) {
			return arr[L];
		}
		/*拿完牌后，角色转换为后手*/
		int p1 = arr[L] + g1(arr, L + 1, R);
		int p2 = arr[R] + g1(arr, L, R - 1);
		return Math.max(p1, p2);
	}

	/*
	* 后手流程：arr[L..R]，后手获得的最好分数返回
	* */
	public static int g1(int[] arr, int L, int R) {
		if (L == R) {
			return 0;
		}
		/*对方拿完牌后，角色转换为先手*/
		int p1 = f1(arr, L + 1, R); // 对手拿走了L位置的数
		int p2 = f1(arr, L, R - 1); // 对手拿走了R位置的数
		/*对手留给我的选择，对手在选择前已经算好了他拿走L或者R，剩下牌的2个最优解，留给我的一定是小的那个，所以是最小值*/
		return Math.min(p1, p2);
	}

	/**
	 * 记忆化搜索
	 * @param arr
	 * @return
	 */
	public static int win2(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int N = arr.length;
		int[][] fmap = new int[N][N];
		int[][] gmap = new int[N][N];
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < N; j++) {
				/*没算过的都是-1*/
				fmap[i][j] = -1;
				gmap[i][j] = -1;
			}
		}
		int first = f2(arr, 0, arr.length - 1, fmap, gmap);
		int second = g2(arr, 0, arr.length - 1, fmap, gmap);
		return Math.max(first, second);
	}

	// arr[L..R]，先手获得的最好分数返回
	public static int f2(int[] arr, int L, int R, int[][] fmap, int[][] gmap) {
		if (fmap[L][R] != -1) {
			return fmap[L][R];
		}
		/*不在缓存中*/
		int ans = 0;
		if (L == R) {
			ans = arr[L];
		} else {
			int p1 = arr[L] + g2(arr, L + 1, R, fmap, gmap);
			int p2 = arr[R] + g2(arr, L, R - 1, fmap, gmap);
			ans = Math.max(p1, p2);
		}
		fmap[L][R] = ans;
		return ans;
	}

	// // arr[L..R]，后手获得的最好分数返回
	public static int g2(int[] arr, int L, int R, int[][] fmap, int[][] gmap) {
		if (gmap[L][R] != -1) {
			return gmap[L][R];
		}
		int ans = 0;
		if (L != R) {
			int p1 = f2(arr, L + 1, R, fmap, gmap); // 对手拿走了L位置的数
			int p2 = f2(arr, L, R - 1, fmap, gmap); // 对手拿走了R位置的数
			ans = Math.min(p1, p2);
		}
		gmap[L][R] = ans;
		return ans;
	}

	public static int win3(int[] arr) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int N = arr.length;
		int[][] fmap = new int[N][N];
		int[][] gmap = new int[N][N];
		for (int i = 0; i < N; i++) {
			/*f对角线的值先设置，g对角线都是0不用设置*/
			fmap[i][i] = arr[i];
		}
		/*
		* 填对角线技巧
		* 枚举每个对角线开始的位置，因为row每次开始都是0不用枚举，column是1~n-1，遍历一下
		* 拿到开始位置后，通过row++,column++遍历完该条对角线
		* */
		for (int startCol = 1; startCol < N; startCol++) {
			/*对角线开始位置[0][j]*/
			int L = 0;
			int R = startCol;
			while (R < N) {
				fmap[L][R] = Math.max(arr[L] + gmap[L + 1][R], arr[R] + gmap[L][R - 1]);
				gmap[L][R] = Math.min(fmap[L + 1][R], fmap[L][R - 1]);
				/*行列++遍历对角线，直到列越界，右上角位置肯定是列先越界*/
				L++;
				R++;
			}
		}
		return Math.max(fmap[0][N - 1], gmap[0][N - 1]);
	}

	public static void main(String[] args) {
		int[] arr = { 5, 7, 4, 5, 8, 1, 6, 0, 3, 4, 6, 1, 7 };
		System.out.println(win1(arr));
		System.out.println(win2(arr));
		System.out.println(win3(arr));

	}

}
