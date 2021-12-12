package class17;

import java.util.Stack;

/**
 * 认识递归：
 * 逆序一个栈，要求除递归外额外空间复杂度O(1)
 */
public class Code05_ReverseStackUsingRecursive {

	public static void reverse(Stack<Integer> stack) {
		if (stack.isEmpty()) {
			return;
		}
		/*每一层缓存了栈底的一个元素，直到栈空了*/
		int i = f(stack);
		reverse(stack);
		/*从最后一层开始压入缓存的之前的栈底，直到第一层。结束*/
		stack.push(i);
	}

	// 栈底元素移除掉
	// 上面的元素盖下来
	// 返回移除掉的栈底元素
	public static int f(Stack<Integer> stack) {
		int result = stack.pop();
		if (stack.isEmpty()) {
			/*这里在最后一层返回了栈底元素*/
			return result;
		} else {
			/*倒数第二层，接住了栈底，依次往上层传递*/
			int last = f(stack);
			/*从倒数第二层往上到第一层，每层压入的都是第一次到这层弹出来的值*/
			stack.push(result);
			/*依次往上层传递栈底，直到方法结束返回了该值*/
			return last;
		}
	}

	public static void main(String[] args) {
		Stack<Integer> test = new Stack<Integer>();
		test.push(1);
		test.push(2);
		test.push(3);
		test.push(4);
		test.push(5);
		reverse(test);
		while (!test.isEmpty()) {
			System.out.println(test.pop());
		}

	}

}
