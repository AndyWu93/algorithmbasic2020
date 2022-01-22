package class37;

import java.util.HashSet;

/**
 * 求累加和在某个范围内的子数组个数
 * 归并排序解法：
 * 见class05.Code01_CountOfRangeSum
 *
 * 有序表的解法：
 * 思路和归并一样：（所有子数组问题，都可以通过枚举以i位置结尾的子数组进行讨论）
 * 	首先生成一个前缀和预处理数组sum
 * 	遍历数组arr（注意这里是arr，不是sum），来到i位置，求以i位置结尾的子数组中，有多少个累加和落在lower,upper]上
 * 	相当于求0..以i位置之前的数结尾的子数组中，有多少个累加和落在[sum[i]-upper,sum[i]-lower]上，以及以0..i位置的数结尾的子数组累加和是否落在[lower,upper]上
 * 使用有序表快速统计：
 * 	维护一个前缀和变量sum
 * 	维护一个统计变量count
 * 	有序表中添加一个sum：0，表示前缀和是0
 * 	遍历arr，来到i位置，计算出此时的sum，以及查找范围：[sum-upper,sum-lower]
 * 	在有序表中查出范围在[sum-upper,sum-lower]的数有几个，加到count中去
 * 	有序表添加此时的sum，继续遍历arr，来到i+1位置
 * 这里的有序表需要的功能：
 * 	范围内有几个数（具体实现方式见代码）
 * 	可以加重复的key
 */
public class Code01_CountofRangeSum {

	public static int countRangeSum1(int[] nums, int lower, int upper) {
		int n = nums.length;
		long[] sums = new long[n + 1];
		for (int i = 0; i < n; ++i)
			sums[i + 1] = sums[i] + nums[i];
		return countWhileMergeSort(sums, 0, n + 1, lower, upper);
	}

	private static int countWhileMergeSort(long[] sums, int start, int end, int lower, int upper) {
		if (end - start <= 1)
			return 0;
		int mid = (start + end) / 2;
		int count = countWhileMergeSort(sums, start, mid, lower, upper)
				+ countWhileMergeSort(sums, mid, end, lower, upper);
		int j = mid, k = mid, t = mid;
		long[] cache = new long[end - start];
		for (int i = start, r = 0; i < mid; ++i, ++r) {
			while (k < end && sums[k] - sums[i] < lower)
				k++;
			while (j < end && sums[j] - sums[i] <= upper)
				j++;
			while (t < end && sums[t] < sums[i])
				cache[r++] = sums[t++];
			cache[r] = sums[i];
			count += j - k;
		}
		System.arraycopy(cache, 0, sums, start, t - start);
		return count;
	}

	public static class SBTNode {
		public long key;
		public SBTNode l;
		public SBTNode r;
		/*平衡因子*/
		public long size; // 不同key的size
		/*自己加的项，表示包含重复值的加入过的总key数*/
		public long all; // 总的size

		public SBTNode(long k) {
			key = k;
			size = 1;
			all = 1;
		}
	}

	public static class SizeBalancedTreeSet {
		private SBTNode root;
		/*用于记录一个key之前有没有进来过*/
		private HashSet<Long> set = new HashSet<>();

		/**
		 * 调平衡时，size改了，不要忘记自己加的变量all也需要改
		 */
		private SBTNode rightRotate(SBTNode cur) {
			/*先查出cur节点上的key，有几个，cur的all，减去左树和右树的all，就得到了自己有多少个key了*/
			long same = cur.all - (cur.l != null ? cur.l.all : 0) - (cur.r != null ? cur.r.all : 0);
			SBTNode leftNode = cur.l;
			cur.l = leftNode.r;
			leftNode.r = cur;
			leftNode.size = cur.size;
			cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
			// all modify
			leftNode.all = cur.all;
			/*此时cur.all的计算：左节点的all+右节点的all，再加自己相同的key数*/
			cur.all = (cur.l != null ? cur.l.all : 0) + (cur.r != null ? cur.r.all : 0) + same;
			return leftNode;
		}

		private SBTNode leftRotate(SBTNode cur) {
			long same = cur.all - (cur.l != null ? cur.l.all : 0) - (cur.r != null ? cur.r.all : 0);
			SBTNode rightNode = cur.r;
			cur.r = rightNode.l;
			rightNode.l = cur;
			rightNode.size = cur.size;
			cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
			// all modify
			rightNode.all = cur.all;
			cur.all = (cur.l != null ? cur.l.all : 0) + (cur.r != null ? cur.r.all : 0) + same;
			return rightNode;
		}

		private SBTNode maintain(SBTNode cur) {
			if (cur == null) {
				return null;
			}
			long leftSize = cur.l != null ? cur.l.size : 0;
			long leftLeftSize = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
			long leftRightSize = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
			long rightSize = cur.r != null ? cur.r.size : 0;
			long rightLeftSize = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
			long rightRightSize = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
			if (leftLeftSize > rightSize) {
				cur = rightRotate(cur);
				cur.r = maintain(cur.r);
				cur = maintain(cur);
			} else if (leftRightSize > rightSize) {
				cur.l = leftRotate(cur.l);
				cur = rightRotate(cur);
				cur.l = maintain(cur.l);
				cur.r = maintain(cur.r);
				cur = maintain(cur);
			} else if (rightRightSize > leftSize) {
				cur = leftRotate(cur);
				cur.l = maintain(cur.l);
				cur = maintain(cur);
			} else if (rightLeftSize > leftSize) {
				cur.r = rightRotate(cur.r);
				cur = leftRotate(cur);
				cur.l = maintain(cur.l);
				cur.r = maintain(cur.r);
				cur = maintain(cur);
			}
			return cur;
		}

		private SBTNode add(SBTNode cur, long key, boolean contains) {
			if (cur == null) {
				return new SBTNode(key);
			} else {
				/*表示进来了一个key*/
				cur.all++;
				if (key == cur.key) {
					return cur;
				} else { // 还需要左滑或者右滑
					if (!contains) {
						/*没有这个key，size++*/
						cur.size++;
					}
					if (key < cur.key) {
						cur.l = add(cur.l, key, contains);
					} else {
						cur.r = add(cur.r, key, contains);
					}
					return maintain(cur);
				}
			}
		}

		/**
		 * 可以加入重复值
		 */
		public void add(long sum) {
			/*先看这个key之前有没有进来过*/
			boolean contains = set.contains(sum);
			/*调用自己实现的有序表添加方法，传入contains*/
			root = add(root, sum, contains);
			/*记录一下这个key进来过了*/
			set.add(sum);
		}

		/**
		 * 有序表中小于key的数有几个
		 */
		public long lessKeySize(long key) {
			SBTNode cur = root;
			long ans = 0;
			/*cur只要不是空，就可以左划或者右划*/
			while (cur != null) {
				if (key == cur.key) {
					/*找打可相同的key，左节点都是比key小的数，加进去，就可以返回了，不用再滑了*/
					return ans + (cur.l != null ? cur.l.all : 0);
				} else if (key < cur.key) {
					/*找到了比key小的key，滑过去*/
					cur = cur.l;
				} else {
					/*
					* 找到了比key大的key，滑过去之前结算一下，cur上相同的key和cur左树所有的key，
					* 用cur.all-cur.r.all就可以得到
					* */
					ans += cur.all - (cur.r != null ? cur.r.all : 0);
					cur = cur.r;
				}
			}
			return ans;
		}

		// > 7 8...
		// <8 ...<=7
		public long moreKeySize(long key) {
			return root != null ? (root.all - lessKeySize(key + 1)) : 0;
		}

	}

	/**
	 * 有序表解法
	 * @param nums
	 * @param lower
	 * @param upper
	 * @return
	 */
	public static int countRangeSum2(int[] nums, int lower, int upper) {
		// 黑盒，加入数字（前缀和），不去重，可以接受重复数字
		// < num , 有几个数？
		SizeBalancedTreeSet treeSet = new SizeBalancedTreeSet();
		long sum = 0;
		int ans = 0;
		/*有序表中加入初始值0，表示：一个数都没有的时候，就已经有一个前缀和累加和为0*/
		treeSet.add(0);
		for (int i = 0; i < nums.length; i++) {
			sum += nums[i];
			// [sum - upper, sum - lower]
			// [10, 20] ?
			// < 10 ?  < 21 ?
			/*
			* 求有序表中有多少个数在[a,b]
			* 化解为：
			* 查有多少个数小于b+1,假设x个,
			* 查有多少个数小于a,假设y个,
			* 结果就是y-x
			* */
			long a = treeSet.lessKeySize(sum - lower + 1);
			long b = treeSet.lessKeySize(sum - upper);
			ans += a - b;
			/*将前缀和加入有序表*/
			treeSet.add(sum);
		}
		return ans;
	}

	// for test
	public static void printArray(int[] arr) {
		for (int i = 0; i < arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}

	// for test
	public static int[] generateArray(int len, int varible) {
		int[] arr = new int[len];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * varible);
		}
		return arr;
	}

	public static void main(String[] args) {
		int len = 200;
		int varible = 50;
		for (int i = 0; i < 10000; i++) {
			int[] test = generateArray(len, varible);
			int lower = (int) (Math.random() * varible) - (int) (Math.random() * varible);
			int upper = lower + (int) (Math.random() * varible);
			int ans1 = countRangeSum1(test, lower, upper);
			int ans2 = countRangeSum2(test, lower, upper);
			if (ans1 != ans2) {
				printArray(test);
				System.out.println(lower);
				System.out.println(upper);
				System.out.println(ans1);
				System.out.println(ans2);
			}
		}

	}

}
