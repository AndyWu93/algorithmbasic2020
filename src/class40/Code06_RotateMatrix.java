package class40;

/**
 * 矩阵计算技巧之分圈计算。
 *
 * 给定一个正方形矩阵matrix，原地调整成顺时针90度转动的样子
 * a  b  c		g  d  a
 * d  e  f		h  e  b
 * g  h  i		i  f  c
 *
 * 思路：
 * 	矩阵旋转时，从外向里，每一圈里的数转完还在这一圈里。
 * 	所以分圈计算
 *
 */
public class Code06_RotateMatrix {

	public static void rotate(int[][] matrix) {
		int a = 0;
		int b = 0;
		int c = matrix.length - 1;
		int d = matrix[0].length - 1;
		/*两个点定位矩阵里的一个圈，计算完后，向里缩一圈，继续计算*/
		while (a < c) {
			/*
			* (a,b)：圈的左上角
			* (c,d)：圈的右下角
			* */
			rotateEdge(matrix, a++, b++, c--, d--);
		}
	}

	 /*
	 * 在一个圈中，旋转的时候可以将一条边的每个点作为一个组的第一个数。
	 * 这样一条边有几个数就可以分为几组，
	 * 一组内4个位置，4个位置顺移转换
	 * */
	public static void rotateEdge(int[][] m, int a, int b, int c, int d) {
		int tmp = 0;
		for (int i = 0; i < d - b; i++) {
			tmp = m[a][b + i];
			m[a][b + i] = m[c - i][b];
			m[c - i][b] = m[c][d - i];
			m[c][d - i] = m[a + i][d];
			m[a + i][d] = tmp;
		}
	}

	public static void printMatrix(int[][] matrix) {
		for (int i = 0; i != matrix.length; i++) {
			for (int j = 0; j != matrix[0].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		int[][] matrix = { { 1, 2, 3, 4 }, { 5, 6, 7, 8 }, { 9, 10, 11, 12 }, { 13, 14, 15, 16 } };
		printMatrix(matrix);
		rotate(matrix);
		System.out.println("=========");
		printMatrix(matrix);

	}

}
