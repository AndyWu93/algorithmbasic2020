package class26;

public class Code02_FibonacciProblem {

	public static int f1(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return 1;
		}
		return f1(n - 1) + f1(n - 2);
	}

	public static int f2(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return 1;
		}
		int res = 1;
		int pre = 1;
		int tmp = 0;
		for (int i = 3; i <= n; i++) {
			tmp = res;
			res = res + pre;
			pre = tmp;
		}
		return res;
	}

	/**
	 * 斐波那契数列，求f(n)
	 */
	// O(logN)
	public static int f3(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return 1;
		}
		/*斐波那契数列的常数矩阵*/
		// [ 1 ,1 ]
		// [ 1, 0 ]
		int[][] base = { 
				{ 1, 1 }, 
				{ 1, 0 } 
				};
		/*求出了矩阵^n-2*/
		int[][] res = matrixPower(base, n - 2);
		/*根据公式（见从斐波那契数列到严格递推式）f(n)= f2 * m[0][0] + f1 * m[1][0] */
		return res[0][0] + res[1][0];
	}

	public static int[][] matrixPower(int[][] m, int p) {
		/*储存结果的矩阵*/
		int[][] res = new int[m.length][m[0].length];
		for (int i = 0; i < res.length; i++) {
			/*先把res变成单位矩阵，即对角线都是1*/
			res[i][i] = 1;
		}
		/*t:矩阵1次方*/
		int[][] t = m;
		/*每次结束后p右移一位，因为最后那个位的值已经处理结束了*/
		for (; p != 0; p >>= 1) {
			/*(p & 1) != 0:说明p最右位是1*/
			if ((p & 1) != 0) {
				/*那就把此时的t乘进来*/
				res = muliMatrix(res, t);
			}
			/*t与自己相乘*/
			t = muliMatrix(t, t);
		}
		return res;
	}

	// 两个矩阵乘完之后的结果返回
	public static int[][] muliMatrix(int[][] m1, int[][] m2) {
		int[][] res = new int[m1.length][m2[0].length];
		for (int i = 0; i < m1.length; i++) {
			for (int j = 0; j < m2[0].length; j++) {
				for (int k = 0; k < m2.length; k++) {
					res[i][j] += m1[i][k] * m2[k][j];
				}
			}
		}
		return res;
	}

	public static int s1(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return n;
		}
		return s1(n - 1) + s1(n - 2);
	}

	public static int s2(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return n;
		}
		int res = 2;
		int pre = 1;
		int tmp = 0;
		for (int i = 3; i <= n; i++) {
			tmp = res;
			res = res + pre;
			pre = tmp;
		}
		return res;
	}

	public static int s3(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return n;
		}
		int[][] base = { { 1, 1 }, { 1, 0 } };
		int[][] res = matrixPower(base, n - 2);
		return 2 * res[0][0] + res[1][0];
	}

	public static int c1(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2 || n == 3) {
			return n;
		}
		return c1(n - 1) + c1(n - 3);
	}

	public static int c2(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2 || n == 3) {
			return n;
		}
		int res = 3;
		int pre = 2;
		int prepre = 1;
		int tmp1 = 0;
		int tmp2 = 0;
		for (int i = 4; i <= n; i++) {
			tmp1 = res;
			tmp2 = pre;
			res = res + prepre;
			pre = tmp1;
			prepre = tmp2;
		}
		return res;
	}

	/**
	 * 面试题
	 * 第一年农场有1只成熟的母牛A，往后的每年：
	 * 1）每一只成熟的母牛都会生一只母牛
	 * 2）每一只新出生的母牛都在出生的第三年成熟
	 * 3）每一只母牛永远不会死
	 * 返回N年后牛的数量
	 *
	 * 递推式为
	 * f(n) = f(n-1) + f(n-3)
	 * 含义：去年所有的牛都活下来了，3年前的牛（f(n-3)）都成熟了生了牛
	 * 前面几项
	 * f1 = 1
	 * f2 = 1 1的宝宝2
	 * f3 = 1 2 1的宝宝3
	 * f4 = 1 2 3 1的宝宝4
	 * f5 = 1 2 3 4 1的宝宝5 2的宝宝6
	 * f6 = 1 2 3 4 5 6 1的宝宝7 2的宝宝8 3的宝宝9
	 *
	 * 扩展，如果牛5年后会死则
	 * f(n) = f(n-1) + f(n-3) - f(n-5)
	 */
	public static int c3(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2 || n == 3) {
			return n;
		}
		int[][] base = { 
				{ 1, 1, 0 }, 
				{ 0, 0, 1 }, 
				{ 1, 0, 0 } };
		int[][] res = matrixPower(base, n - 3);
		return 3 * res[0][0] + 2 * res[1][0] + res[2][0];
	}

	public static void main(String[] args) {
		int n = 19;
		System.out.println(f1(n));
		System.out.println(f2(n));
		System.out.println(f3(n));
		System.out.println("===");

		System.out.println(s1(n));
		System.out.println(s2(n));
		System.out.println(s3(n));
		System.out.println("===");

		System.out.println(c1(n));
		System.out.println(c2(n));
		System.out.println(c3(n));
		System.out.println("===");

	}

}
