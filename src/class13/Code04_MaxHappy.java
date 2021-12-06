package class13;

import java.util.ArrayList;
import java.util.List;

/**
 * 多叉树问题
 * 整个公司是一个多叉树，这个公司要办party，你可以决定哪些员工来，哪些员工不来，规则：
 * 1.不能同时邀请直接上下级
 * 2.派对的整体快乐值是所有到场员工快乐值得累加
 * 求派对的最大快乐值
 * 思路：递归套路
 * 对任意节点x可能性：
 * 1.x来了
 * x.value + 直接下级树中直接下级不来的values
 * 2.x没来
 * 直接下级树中每个下级 max（直接下级来，不来）
 * INFO
 * 1.头节点来时的max value
 * 2.头节点不来时的max value
 */
public class Code04_MaxHappy {

	public static class Employee {
		public int happy;
		public List<Employee> nexts;

		public Employee(int h) {
			happy = h;
			nexts = new ArrayList<>();
		}

	}

	public static int maxHappy1(Employee boss) {
		if (boss == null) {
			return 0;
		}
		return process1(boss, false);
	}

	// 当前来到的节点叫cur，
	// up表示cur的上级是否来，
	// 该函数含义：
	// 如果up为true，表示在cur上级已经确定来，的情况下，cur整棵树能够提供最大的快乐值是多少？
	// 如果up为false，表示在cur上级已经确定不来，的情况下，cur整棵树能够提供最大的快乐值是多少？
	public static int process1(Employee cur, boolean up) {
		if (up) { // 如果cur的上级来的话，cur没得选，只能不来
			int ans = 0;
			for (Employee next : cur.nexts) {
				ans += process1(next, false);
			}
			return ans;
		} else { // 如果cur的上级不来的话，cur可以选，可以来也可以不来
			int p1 = cur.happy;
			int p2 = 0;
			for (Employee next : cur.nexts) {
				p1 += process1(next, true);
				p2 += process1(next, false);
			}
			return Math.max(p1, p2);
		}
	}

	/**
	 * 递归套路
	 * @param head
	 * @return
	 */
	public static int maxHappy2(Employee head) {
		Info allInfo = process(head);
		/*返回 max(boss来，boss不来)*/
		return Math.max(allInfo.no, allInfo.yes);
	}

	public static class Info {
		/*头节点不来时的max value*/
		public int no;
		/*头节点来时的max value*/
		public int yes;

		public Info(int n, int y) {
			no = n;
			yes = y;
		}
	}

	public static Info process(Employee x) {
		if (x == null) {
			/*null的情况下都是0*/
			return new Info(0, 0);
		}
		int no = 0;
		/*yes初始化为x的value*/
		int yes = x.happy;
		for (Employee next : x.nexts) {
			/*每个下级都拿个信息*/
			Info nextInfo = process(next);
			/*no累加下级max(来，不来)*/
			no += Math.max(nextInfo.no, nextInfo.yes);
			/*yes累加下级的no*/
			yes += nextInfo.no;

		}
		return new Info(no, yes);
	}

	// for test
	public static Employee genarateBoss(int maxLevel, int maxNexts, int maxHappy) {
		if (Math.random() < 0.02) {
			return null;
		}
		Employee boss = new Employee((int) (Math.random() * (maxHappy + 1)));
		genarateNexts(boss, 1, maxLevel, maxNexts, maxHappy);
		return boss;
	}

	// for test
	public static void genarateNexts(Employee e, int level, int maxLevel, int maxNexts, int maxHappy) {
		if (level > maxLevel) {
			return;
		}
		int nextsSize = (int) (Math.random() * (maxNexts + 1));
		for (int i = 0; i < nextsSize; i++) {
			Employee next = new Employee((int) (Math.random() * (maxHappy + 1)));
			e.nexts.add(next);
			genarateNexts(next, level + 1, maxLevel, maxNexts, maxHappy);
		}
	}

	public static void main(String[] args) {
		int maxLevel = 4;
		int maxNexts = 7;
		int maxHappy = 100;
		int testTimes = 100000;
		for (int i = 0; i < testTimes; i++) {
			Employee boss = genarateBoss(maxLevel, maxNexts, maxHappy);
			if (maxHappy1(boss) != maxHappy2(boss)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");
	}

}
