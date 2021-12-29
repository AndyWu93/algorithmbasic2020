package class25;

import java.util.Stack;

/**
 * 二维数组matrix，其中的值不是0就是1。返回全部由1组成的最大子矩形，内部有多少个1
 * 暴力解：
 * 枚举所有的子矩阵O(N^4)，遍历矩阵看是否都是1,O(N^2),复杂度O(N^6)
 * 如何枚举所有的子矩阵？
 * 在矩阵中随意点一个点A,O(N^2),再随意点一个点B,O(N^2)；
 * A、B作为对角线连接起来一个矩阵（可能会有重复，但也只重复枚举一次，如，下次找到了B、A连成的矩阵）。复杂度O(N^4)
 * 枚举子矩阵是在4个for循环中枚举出两个点
 *
 * 最优解：压缩数组+单调栈，复杂度O(N^2)
 * 思路流程：枚举每一行作底，形成的子矩阵，压缩成直方图数组，求数组中最大矩形格子数量
 * 先拿出第一行数组，参考上一题，求出最多的矩形格子的数量.O(N)
 * 再拿出第二行数组，与第一行累加，得到数组sum，累加法则：自己是0加任何数都是0。求出最多的矩形格子的数量.O(N)
 * 再拿出第三行数组，与上一行得到sum数组累积，得到新数组sum，累加法则：自己是0加任何数都是0。求出最多的矩形格子的数量.O(N)
 * ...
 * 收集出这些数量的最大值即题解
 */
// 测试链接：https://leetcode.com/problems/maximal-rectangle/
public class Code04_MaximalRectangle {

	public static int maximalRectangle(char[][] map) {
		if (map == null || map.length == 0 || map[0].length == 0) {
			return 0;
		}
		int maxArea = 0;
		/*存储压缩数组*/
		int[] height = new int[map[0].length];
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				/*该位置是0，直接归零，否则原有值+1*/
				height[j] = map[i][j] == '0' ? 0 : height[j] + 1;
			}
			/*得到sum数组后，求出最多矩形格子数量，收集最大值*/
			maxArea = Math.max(maxRecFromBottom(height), maxArea);
		}
		return maxArea;
	}

	/*
	* 求出height中最多矩形格子数量，参考class25.Code03_LargestRectangleInHistogram
	* */
	// height是正方图数组
	public static int maxRecFromBottom(int[] height) {
		if (height == null || height.length == 0) {
			return 0;
		}
		int maxArea = 0;
		Stack<Integer> stack = new Stack<Integer>();
		for (int i = 0; i < height.length; i++) {
			while (!stack.isEmpty() && height[i] <= height[stack.peek()]) {
				int j = stack.pop();
				int k = stack.isEmpty() ? -1 : stack.peek();
				int curArea = (i - k - 1) * height[j];
				maxArea = Math.max(maxArea, curArea);
			}
			stack.push(i);
		}
		while (!stack.isEmpty()) {
			int j = stack.pop();
			int k = stack.isEmpty() ? -1 : stack.peek();
			int curArea = (height.length - k - 1) * height[j];
			maxArea = Math.max(maxArea, curArea);
		}
		return maxArea;
	}

}
