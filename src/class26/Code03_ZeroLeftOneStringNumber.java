package class26;

/**
 * 给定一个数N，想象只由0和1两种字符，组成的所有长度为N的字符串
 * 如果某个字符串,任何0字符的左边都有1紧挨着,认为这个字符串达标
 * 返回有多少达标的字符串
 * 举例，T:达标，F:不达标
 * N=1
 * 0F 1T
 * N=2
 * 00F 01F 10T 11T
 * N=3
 * 000F 001F 010F 011F 100F 101T 110T 111T
 * ...
 * 最后得到的数
 * 1 2 3 5 8 13 21...
 *
 * 解法一，观察得知
 * f(n) = f(n-1) + f(n-2)
 * 其中f1=1,f2=2；即1，2开头的斐波那契数列问题
 *
 * 解法二，通过尝试，得到递推式
 * 定义一个函数 int f(int i)，含义：
 * 假设现在还剩i个格子需要填好1或者0，前提之前的一个格子一定是1
 * a)第一个格子填1，后续f(i-1)种可能
 * b)第一个格子填1,那第二个格子必须填1，后续f(i-2)种可能
 * 所以f(i) = f(i-1) + f(i-2)
 *
 */
public class Code03_ZeroLeftOneStringNumber {

	public static int getNum1(int n) {
		if (n < 1) {
			return 0;
		}
		return process(1, n);
	}

	public static int process(int i, int n) {
		if (i == n - 1) {
			return 2;
		}
		if (i == n) {
			return 1;
		}
		return process(i + 1, n) + process(i + 2, n);
	}

	public static int getNum2(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1) {
			return 1;
		}
		int pre = 1;
		int cur = 1;
		int tmp = 0;
		for (int i = 2; i < n + 1; i++) {
			tmp = cur;
			cur += pre;
			pre = tmp;
		}
		return cur;
	}

	public static int getNum3(int n) {
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
	
	
	
	
	
	
	public static int fi(int n) {
		if (n < 1) {
			return 0;
		}
		if (n == 1 || n == 2) {
			return 1;
		}
		int[][] base = { { 1, 1 }, 
				         { 1, 0 } };
		int[][] res = matrixPower(base, n - 2);
		return res[0][0] + res[1][0];
	}

	
	
	
	public static int[][] matrixPower(int[][] m, int p) {
		int[][] res = new int[m.length][m[0].length];
		for (int i = 0; i < res.length; i++) {
			res[i][i] = 1;
		}
		int[][] tmp = m;
		for (; p != 0; p >>= 1) {
			if ((p & 1) != 0) {
				res = muliMatrix(res, tmp);
			}
			tmp = muliMatrix(tmp, tmp);
		}
		return res;
	}

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

	public static void main(String[] args) {
		for (int i = 0; i != 20; i++) {
			System.out.println(getNum1(i));
			System.out.println(getNum2(i));
			System.out.println(getNum3(i));
			System.out.println("===================");
		}

	}
}
