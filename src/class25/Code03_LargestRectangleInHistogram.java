package class25;

import java.util.Stack;

/**
 * 给定一个非负数组arr，代表直方图。返回直方图的最大长方形面积
 * 题意解析：
 * 给定一个高度的数组arr，每个位置代表该位置格子的高度，求哪个位置左右能够包含的长方形的格子最多，返回格子数量
 * 解题思路：
 * 对于arr中的任意一个位置，找出左右最近的比它小的高度，那是到不了的位置，结算格子数
 * 栈中依然可以放一个值，当遇到与自己相等的时候弹出，结算。最后一个与自己相等的位置弹出时结算的值一定是对的
 */
// 测试链接：https://leetcode.com/problems/largest-rectangle-in-histogram
public class Code03_LargestRectangleInHistogram {

	public static int largestRectangleArea1(int[] height) {
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

	public static int largestRectangleArea2(int[] height) {
		if (height == null || height.length == 0) {
			return 0;
		}
		int N = height.length;
		int[] stack = new int[N];
		int si = -1;
		int maxArea = 0;
		for (int i = 0; i < height.length; i++) {
			while (si != -1 && height[i] <= height[stack[si]]) {
				int j = stack[si--];
				int k = si == -1 ? -1 : stack[si];
				int curArea = (i - k - 1) * height[j];
				maxArea = Math.max(maxArea, curArea);
			}
			stack[++si] = i;
		}
		while (si != -1) {
			int j = stack[si--];
			int k = si == -1 ? -1 : stack[si];
			int curArea = (height.length - k - 1) * height[j];
			maxArea = Math.max(maxArea, curArea);
		}
		return maxArea;
	}

}
