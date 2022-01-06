package class31;

/**
 * 线段树 ：区间更新区间查询O(logN)
 * 为了解决，在一个arr中，某个区间内的数的高效增改查，有多高效？增改查都是O(logN)
 * 线段树核心设计理念：
 * 1. arr[]: 原arr的值copy到arr[]中，0位置忽略，从1开始使用
 * 2. 区间树：
 * 	用区间构成的二叉树结构，用数组表示。0位置忽略，从1开始使用；区间大小一般为arr的4倍
 * 	举例：区间和sum[]
 * 		sum[i]=sum[2*i] + sum[2*i+1]
 * 		假设arr的范围是[1,6],sum结构如下
 * 		          sum[1..6]
 * 		       /          \
 *        sum[1..3]       sum[4..6]
 *       /      \        /       \
 *   sum[1..1] sum[2..3] sum[4..4] sum[5..6]
 *            /    \            /      \
 *       sum[2..2] sum[3..3]  sum[5..5] sum[6..6]
 * 3. lazy[]: 未下发的add任务，长度和区间数组一致。
 * 	1) lazy[i] = 6，指的是，i代表的区间有一个全部加6的任务，暂未执行
 * 	2) 当来了一个任务，add(1,3,3): arr[1..3]区间内都加3
 * 		这个任务的范围包括了[1..3]的整个区域，所以不会执行，会在lazy[]中存放任务信息，即lazy[2] = 3
 * 		但是，lazy[2]能够存放任务的前提是lazy[2]之前，以及他自己原来存放的任务都已经下发执行，直到能够被lazy[2]后面的位置揽住为止
 * 4. change[]、update[]:未下发的更新任务
 * 	1) change记录将要更新的值
 * 	2) update记录该值是否有效
 * 5. 线段树的使用范围：
 * 父区间的值可以通过左右两个子区间简单的计算得到
 * 	不适用的情况：比如统计区间内哪个数字出现的次数最多，这个不能通过简单计算得到，所以不适用线段树
 */
public class Code01_SegmentTree {

	public static class SegmentTree {
		private int MAXN;
		/*arr里从1开始记录了原序列从0开始的信息*/
		private int[] arr;
		/*区间的树形结构：区间的和*/
		private int[] sum;
		/*区间的树形结构：对应sum区间和未下发的懒任务*/
		private int[] lazy;
		/*区间的树形结构：区间内所有的值更新成当前值*/
		private int[] change;
		/*区间的树形结构：change数组的当前值是否是有效的更新值，即使是0也是有效的*/
		private boolean[] update;

		public SegmentTree(int[] origin) {
			MAXN = origin.length + 1;
			arr = new int[MAXN]; // arr[0] 不用 从1开始使用
			for (int i = 1; i < MAXN; i++) {
				arr[i] = origin[i - 1];
			}
			sum = new int[MAXN << 2]; // 用来支持脑补概念中，某一个范围的累加和信息
			lazy = new int[MAXN << 2]; // 用来支持脑补概念中，某一个范围沒有往下傳遞的纍加任務
			change = new int[MAXN << 2]; // 用来支持脑补概念中，某一个范围有没有更新操作的任务
			update = new boolean[MAXN << 2]; // 用来支持脑补概念中，某一个范围更新任务，更新成了什么
		}

		/* sum[i] = sum[2*i] + sum[2*i+1] */
		private void pushUp(int rt) {
			sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
		}

		/**
		 * 最核心方法之：下发 之前的，所有懒增加，和懒更新，从父范围，发给左右两个子范围
		 * rt：区间树的index
		 * ln: rt左树区间内多少个元素
		 * rn: rt右树区间内多少个元素
		 *
		 * 一定是先下发update，在下发add（lazy）
		 * 为什么？
		 * 当一个节点既有更新信息，又有累加信息
		 * 因为更新任务来到时，会将lazy的数据清空，
		 * 如果更新任务存在的情况下，还有lazy任务，只能说明lazy任务发生在update之后
		 */
		private void pushDown(int rt, int ln, int rn) {
			if (update[rt]) {
				/*下发更新的数据*/
				update[rt << 1] = true;
				update[rt << 1 | 1] = true;
				change[rt << 1] = change[rt];
				change[rt << 1 | 1] = change[rt];
				/*同时，清空lazy，并重新计算sum*/
				lazy[rt << 1] = 0;
				lazy[rt << 1 | 1] = 0;
				sum[rt << 1] = change[rt] * ln;
				sum[rt << 1 | 1] = change[rt] * rn;
				/*释放该位置的update，只有需要改update数组，change数组保留了没关系*/
				update[rt] = false;
			}
			/*rt懒的任务往下发*/
			if (lazy[rt] != 0) {
				/*左树揽住*/
				lazy[rt << 1] += lazy[rt];
				/*左树的sum更新*/
				sum[rt << 1] += lazy[rt] * ln;
				/*右树揽住*/
				lazy[rt << 1 | 1] += lazy[rt];
				/*右树sum更新*/
				sum[rt << 1 | 1] += lazy[rt] * rn;
				/*当前懒住的任务下发完成了，清空掉*/
				lazy[rt] = 0;
			}
		}

		/**
		* 1.初始化阶段，先把sum数组，填好
		* 把arr[l~r]范围上的数，build到sum数组中（需要从叶节点到跟节点）
		* rt : arr[l~r]范围在sum中的下标
		* */
		public void build(int l, int r, int rt) {
			if (l == r) {
				/*叶节点了，直接到arr中拿值*/
				sum[rt] = arr[l];
				return;
			}
			/*sum树中向下build*/
			int mid = (l + r) >> 1;
			build(l, mid, rt << 1);
			build(mid + 1, r, rt << 1 | 1);
			/*sum树下面的数有了，该位置就是左右两个孩子的和*/
			pushUp(rt);
		}

		/**
		 * 3.核心方法之，在[L..R]范围内所有的数变成C
		 * L,R,C:任务信息
		 * l,r,rt:当前的区间是[l..r]，对应区间树的index为rt
		 */
		public void update(int L, int R, int C, int l, int r, int rt) {
			if (L <= l && r <= R) {
				/*该范围内都变成C*/
				update[rt] = true;
				change[rt] = C;
				/*该范围的和，重新计算一下*/
				sum[rt] = C * (r - l + 1);
				/*该范围原来懒住的信息，全部清掉*/
				lazy[rt] = 0;
				return;
			}
			// 当前任务躲不掉，无法懒更新，要往下发
			int mid = (l + r) >> 1;
			pushDown(rt, mid - l + 1, r - mid);
			if (L <= mid) {
				update(L, R, C, l, mid, rt << 1);
			}
			if (R > mid) {
				update(L, R, C, mid + 1, r, rt << 1 | 1);
			}
			pushUp(rt);
		}

		/**
		 * 2.核心方法之，在[L..R]范围内加一个数C
		 * L,R,C:任务信息
		 * l,r,rt:当前的区间是[l..r]，对应区间树的index为rt
		 */
		public void add(int L, int R, int C, int l, int r, int rt) {
			// 任务如果把此时的范围全包了！
			if (L <= l && r <= R) {
				/*sum树该范围的和更新一下*/
				sum[rt] += C * (r - l + 1);
				/*懒住了C*/
				lazy[rt] += C;
				return;
			}
			// 任务没有把你全包！
			// l  r  mid = (l+r)/2
			int mid = (l + r) >> 1;
			/*rt左子树、右子树的元素个数求法*/
			pushDown(rt, mid - l + 1, r - mid);
			// L~R
			/*将任务发给了rt的两个子树*/
			if (L <= mid) {
				add(L, R, C, l, mid, rt << 1);
			}
			if (R > mid) {
				add(L, R, C, mid + 1, r, rt << 1 | 1);
			}
			/*rt的两个子树更新完了以后，sum[rt]就可以更新了*/
			pushUp(rt);
		}

		/**
		 * 3.核心方法之，查询[L..R]范围内的累加和
		 * L,R,C:任务信息
		 * l,r,rt:当前的区间是[l..r]，对应区间树的index为rt
		 */
		public long query(int L, int R, int l, int r, int rt) {
			if (L <= l && r <= R) {
				/*要查询的范围完全包住了当前范围，直接返回去*/
				return sum[rt];
			}
			/*包不住？先下发任务*/
			int mid = (l + r) >> 1;
			pushDown(rt, mid - l + 1, r - mid);
			/*再把范围分给左、右去查，最后累加起来返回*/
			long ans = 0;
			if (L <= mid) {
				ans += query(L, R, l, mid, rt << 1);
			}
			if (R > mid) {
				ans += query(L, R, mid + 1, r, rt << 1 | 1);
			}
			return ans;
		}

	}

	public static class Right {
		public int[] arr;

		public Right(int[] origin) {
			arr = new int[origin.length + 1];
			for (int i = 0; i < origin.length; i++) {
				arr[i + 1] = origin[i];
			}
		}

		public void update(int L, int R, int C) {
			for (int i = L; i <= R; i++) {
				arr[i] = C;
			}
		}

		public void add(int L, int R, int C) {
			for (int i = L; i <= R; i++) {
				arr[i] += C;
			}
		}

		public long query(int L, int R) {
			long ans = 0;
			for (int i = L; i <= R; i++) {
				ans += arr[i];
			}
			return ans;
		}

	}

	public static int[] genarateRandomArray(int len, int max) {
		int size = (int) (Math.random() * len) + 1;
		int[] origin = new int[size];
		for (int i = 0; i < size; i++) {
			origin[i] = (int) (Math.random() * max) - (int) (Math.random() * max);
		}
		return origin;
	}

	public static boolean test() {
		int len = 100;
		int max = 1000;
		int testTimes = 5000;
		int addOrUpdateTimes = 1000;
		int queryTimes = 500;
		for (int i = 0; i < testTimes; i++) {
			int[] origin = genarateRandomArray(len, max);
			SegmentTree seg = new SegmentTree(origin);
			int S = 1;
			int N = origin.length;
			int root = 1;
			seg.build(S, N, root);
			Right rig = new Right(origin);
			for (int j = 0; j < addOrUpdateTimes; j++) {
				int num1 = (int) (Math.random() * N) + 1;
				int num2 = (int) (Math.random() * N) + 1;
				int L = Math.min(num1, num2);
				int R = Math.max(num1, num2);
				int C = (int) (Math.random() * max) - (int) (Math.random() * max);
				if (Math.random() < 0.5) {
					seg.add(L, R, C, S, N, root);
					rig.add(L, R, C);
				} else {
					seg.update(L, R, C, S, N, root);
					rig.update(L, R, C);
				}
			}
			for (int k = 0; k < queryTimes; k++) {
				int num1 = (int) (Math.random() * N) + 1;
				int num2 = (int) (Math.random() * N) + 1;
				int L = Math.min(num1, num2);
				int R = Math.max(num1, num2);
				long ans1 = seg.query(L, R, S, N, root);
				long ans2 = rig.query(L, R);
				if (ans1 != ans2) {
					return false;
				}
			}
		}
		return true;
	}

	public static void main(String[] args) {
		int[] origin = { 2, 1, 1, 2, 3, 4, 5 };
		SegmentTree seg = new SegmentTree(origin);
		int S = 1; // 整个区间的开始位置，规定从1开始，不从0开始 -> 固定
		int N = origin.length; // 整个区间的结束位置，规定能到N，不是N-1 -> 固定
		int root = 1; // 整棵树的头节点位置，规定是1，不是0 -> 固定
		int L = 2; // 操作区间的开始位置 -> 可变
		int R = 5; // 操作区间的结束位置 -> 可变
		int C = 4; // 要加的数字或者要更新的数字 -> 可变
		// 区间生成，必须在[S,N]整个范围上build
		seg.build(S, N, root);
		// 区间修改，可以改变L、R和C的值，其他值不可改变
		seg.add(L, R, C, S, N, root);
		// 区间更新，可以改变L、R和C的值，其他值不可改变
		seg.update(L, R, C, S, N, root);
		// 区间查询，可以改变L和R的值，其他值不可改变
		long sum = seg.query(L, R, S, N, root);
		System.out.println(sum);

		System.out.println("对数器测试开始...");
		System.out.println("测试结果 : " + (test() ? "通过" : "未通过"));

	}

}
