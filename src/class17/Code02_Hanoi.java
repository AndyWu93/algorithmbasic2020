package class17;

import java.util.Stack;

/**
 * 认识递归
 * 汉诺塔问题：
 * 给了abc3个柱子，把a柱子上面所有的盘子移动到c去，一次只能移动一个盘子，而且只能大盘子在小盘子小面，问给定n个盘子，打印移动轨迹
 *
 * 复杂度：n层，最优解移动了2^n-1步，O(2^N-1)
 *
 * 设计递归技巧：
 * 设计一个函数，把这个函数当成黑盒来用，接下来就是黑盒要有明确的定义，以及黑盒该怎么用
 */
public class Code02_Hanoi {

	/**
	 * 思路：
	 * 把1-n从a到c,
	 * 那就写个方法f1，把1~n-1从a到b
	 * 然后把n从a到c
	 * 再写个方法f2，把1~n-2从b到c
	 *
	 * f1，f2该怎么写呢？使用同样的思路。最终6个方法相互嵌套就能解决问题
	 * fa2c()
	 * fa2b()
	 * fb2c()
	 * fb2a()
	 * fc2a()
	 * fc2b()
	 * @param n
	 */
	public static void hanoi1(int n) {
		leftToRight(n);
	}

	// 请把1~N层圆盘 从左 -> 右
	public static void leftToRight(int n) {
		if (n == 1) { // base case
			System.out.println("Move 1 from left to right");
			return;
		}
		leftToMid(n - 1);
		System.out.println("Move " + n + " from left to right");
		midToRight(n - 1);
	}

	// 请把1~N层圆盘 从左 -> 中
	public static void leftToMid(int n) {
		if (n == 1) {
			System.out.println("Move 1 from left to mid");
			return;
		}
		leftToRight(n - 1);
		System.out.println("Move " + n + " from left to mid");
		rightToMid(n - 1);
	}

	public static void rightToMid(int n) {
		if (n == 1) {
			System.out.println("Move 1 from right to mid");
			return;
		}
		rightToLeft(n - 1);
		System.out.println("Move " + n + " from right to mid");
		leftToMid(n - 1);
	}

	public static void midToRight(int n) {
		if (n == 1) {
			System.out.println("Move 1 from mid to right");
			return;
		}
		midToLeft(n - 1);
		System.out.println("Move " + n + " from mid to right");
		leftToRight(n - 1);
	}

	public static void midToLeft(int n) {
		if (n == 1) {
			System.out.println("Move 1 from mid to left");
			return;
		}
		midToRight(n - 1);
		System.out.println("Move " + n + " from mid to left");
		rightToLeft(n - 1);
	}

	public static void rightToLeft(int n) {
		if (n == 1) {
			System.out.println("Move 1 from right to left");
			return;
		}
		rightToMid(n - 1);
		System.out.println("Move " + n + " from right to left");
		midToLeft(n - 1);
	}

	/**
	 * 优化：
	 * 6个方法相互嵌套，可以通过增加参数的方式，使递归函数功能更强大
	 * 于是，6个方法抽像成了1个方法
	 * @param n
	 */
	public static void hanoi2(int n) {
		if (n > 0) {
			func(n, "left", "right", "mid");
		}
	}

	public static void func(int N, String from, String to, String other) {
		if (N == 1) { // base
			System.out.println("Move 1 from " + from + " to " + to);
		} else {
			/*先把1~n-1移到other去*/
			func(N - 1, from, other, to);
			/*再将n移到了to*/
			System.out.println("Move " + N + " from " + from + " to " + to);
			/*最后把other上的1~n-1移动to去*/
			func(N - 1, other, to, from);
		}
	}

	public static class Record {
		public boolean finish1;
		public int base;
		public String from;
		public String to;
		public String other;

		public Record(boolean f1, int b, String f, String t, String o) {
			finish1 = false;
			base = b;
			from = f;
			to = t;
			other = o;
		}
	}

	public static void hanoi3(int N) {
		if (N < 1) {
			return;
		}
		Stack<Record> stack = new Stack<>();
		stack.add(new Record(false, N, "left", "right", "mid"));
		while (!stack.isEmpty()) {
			Record cur = stack.pop();
			if (cur.base == 1) {
				System.out.println("Move 1 from " + cur.from + " to " + cur.to);
				if (!stack.isEmpty()) {
					stack.peek().finish1 = true;
				}
			} else {
				if (!cur.finish1) {
					stack.push(cur);
					stack.push(new Record(false, cur.base - 1, cur.from, cur.other, cur.to));
				} else {
					System.out.println("Move " + cur.base + " from " + cur.from + " to " + cur.to);
					stack.push(new Record(false, cur.base - 1, cur.other, cur.to, cur.from));
				}
			}
		}
	}

	public static void main(String[] args) {
		int n = 3;
		hanoi1(n);
		System.out.println("============");
		hanoi2(n);
//		System.out.println("============");
//		hanoi3(n);
	}

}
