package class32;

/**
 * index tree 树状数组
 * 用来快速求出某个范围内的累加和。
 * 范围内的累加和也可以用前缀数组快速求出，但是当涉及到元数据的更新后再求范围累加和，前缀数组就涉及到大量的更新，就不适用了
 * 涉及单点更新、范围求和问题，线段树也可以做，但是index tree更优（线段树只针对单点更新，indexTree可以是二维的）
 * index tree设计
 * 将arr中的数，以小游戏2048的形式累加起来
 * 元数组下标：       [1,2,3,4,5,6,7,8]
 * indexTree累加后：sum[1],sum[1..2],sum[3],sum[1..4],sum[5],sum[5..6],sum[7],sum[1..8]
 * 1. 对于任意i，有
 * 假设indexTree[i],i的二进制为0 1011 1000，那indexTree[i]=sum[x..i],x的二进制为0 1011 0001，即i最右的1变成0后再加1
 * 2. 求sum[1..i]
 * sum[1..i]=indexTree[i]+indexTree[i1=i最右侧的1变成0]+indexTree[i2=i1最右侧的1变成0]+...+indexTree[in只剩1个1了]
 * 举例求sum[1..01 0110 0110]
 * = it[01 0110 0110] + it[01 0110 0100] + it[01 0110 0000] +...+ it[01 0000 0000]
 * 为什么？将这些数按照第1点的规则拆开，就能看到一共是sum[1..01 0110 0110]
 * 3. 当arr[1001 1000]的值变了，
 * it[1001 1000]一定变了，还要哪些位置要变？
 * i + 最右侧的1 得到i1，即1001 1000 + 0000 1000 -> it[1010 0000]
 * i1 + 最右侧的1 得到i2，即1010 0000 + 0010 0000 -> it[1100 0000]
 * ...
 * 直到in越界
 *
 * 以上方法复杂度？
 * index的位数决定复杂度，位数是多少？logN
 */
public class Code01_IndexTree {

	// 下标从1开始！
	public static class IndexTree {

		private int[] tree;
		private int N;

		// 0位置弃而不用！
		public IndexTree(int size) {
			N = size;
			tree = new int[N + 1];
		}

		/**
		 * sum方法
		 * 1~index 累加和是多少？
		 */
		public int sum(int index) {
			int ret = 0;
			while (index > 0) {
				/*index位置先加上去*/
				ret += tree[index];
				/*sum是多个位置的和，位置如何到，减去最右侧的1*/
				index -= index & -index;
			}
			return ret;
		}

		/**
		 * 在arr的index位置增加值d
		 * indexTree中受影响的位置都要改变
		 */
		// index & -index : 提取出index最右侧的1出来
		// index :           0011001000
		// index & -index :  0000001000
		public void add(int index, int d) {
			while (index <= N) {
				/*现在该位置加上d*/
				tree[index] += d;
				/*受影响的位置如何得到：index加上最右侧的1，直到越界*/
				index += index & -index;
			}
		}
	}

	public static class Right {
		private int[] nums;
		private int N;

		public Right(int size) {
			N = size + 1;
			nums = new int[N + 1];
		}

		public int sum(int index) {
			int ret = 0;
			for (int i = 1; i <= index; i++) {
				ret += nums[i];
			}
			return ret;
		}

		public void add(int index, int d) {
			nums[index] += d;
		}

	}

	public static void main(String[] args) {
		int N = 100;
		int V = 100;
		int testTime = 2000000;
		IndexTree tree = new IndexTree(N);
		Right test = new Right(N);
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int index = (int) (Math.random() * N) + 1;
			if (Math.random() <= 0.5) {
				int add = (int) (Math.random() * V);
				tree.add(index, add);
				test.add(index, add);
			} else {
				if (tree.sum(index) != test.sum(index)) {
					System.out.println("Oops!");
				}
			}
		}
		System.out.println("test finish");
	}

}
