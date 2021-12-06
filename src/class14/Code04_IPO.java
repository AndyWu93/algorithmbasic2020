package class14;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 正数数组costs、正数数组profits，现有启动资金m，最多做k个项目，输出获得的最大钱数
 * 思路：暴力递归可以做对数器，最优解为贪心
 * 1.把costs和profits整合成一个个对象
 * 2.准备一个小根堆a按cost比较，把所有对象放入a中
 * 3.准备一个大根堆b按profit比较
 * 4.弹出a中cost小于m的项目（解锁项目），放入b中
 * 5.做掉b中堆顶的项目，启动资金累加到sum，第一轮结束（一轮做一个，做完启动资金增加后，看能不能解锁新项目）
 * 6.第二轮同上
 * ...直到做完k个
 */
public class Code04_IPO {

	// 最多K个项目
	// W是初始资金
	// Profits[] Capital[] 一定等长
	// 返回最终最大的资金
	public static int findMaximizedCapital(int K, int W, int[] Profits, int[] Capital) {
		PriorityQueue<Program> minCostQ = new PriorityQueue<>(new MinCostComparator());
		PriorityQueue<Program> maxProfitQ = new PriorityQueue<>(new MaxProfitComparator());
		for (int i = 0; i < Profits.length; i++) {
			/*所有项目放入小根堆*/
			minCostQ.add(new Program(Profits[i], Capital[i]));
		}
		/*做k个项目*/
		for (int i = 0; i < K; i++) {
			while (!minCostQ.isEmpty() && minCostQ.peek().c <= W) {
				/*小根堆堆顶能否解锁？能的话弹出进入大根堆*/
				maxProfitQ.add(minCostQ.poll());
			}
			if (maxProfitQ.isEmpty()) {
				/*都解锁完了发现已经无项目可做了，要么现有的项目都是成本太高，要么就是项目总共就没几个，就提前结束吧*/
				return W;
			}
			/*做大根堆堆顶的项目，W累加，结束下一轮*/
			W += maxProfitQ.poll().p;
		}
		return W;
	}

	public static class Program {
		public int p;
		public int c;

		public Program(int p, int c) {
			this.p = p;
			this.c = c;
		}
	}

	public static class MinCostComparator implements Comparator<Program> {

		@Override
		public int compare(Program o1, Program o2) {
			return o1.c - o2.c;
		}

	}

	public static class MaxProfitComparator implements Comparator<Program> {

		@Override
		public int compare(Program o1, Program o2) {
			return o2.p - o1.p;
		}

	}

}
