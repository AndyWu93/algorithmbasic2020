package class29;

import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * 认识bfprt
 *
 * 求第K小的数
 * 暴力解：排序后看下标O(N*logN)
 *
 * 最优解1：荷兰国旗O(N)
 * 随机选一个数a，荷兰国际过后，看中间等于a的数下标区域是否包含了k，没有的话再根据对比K的大小去左边或者右边荷兰国旗
 * 为什么比快排快？快排需要两侧都要掉递归，这里只需要掉一侧，所以更快
 *
 * 最优解2：bfprt（荷兰国旗的优化，不是随机选择一个pivot，严格保证了复杂度O(N)）
 * 该算法地位比价高，是因为它的算法思想：
 * 通过某种机制，淘汰掉了一部分明显不符合要求的数，从而节省了遍历范围，严格保证了复杂度O(N)。
 * 流程：
 * int bfprt(arr,L,R,k):arr中返回第K小的数
 * 1. 将arr划分为5个数一组（逻辑概念，没有复杂度）
 * 2. 每个小组内排序（小组排序O(1) * 整体规模O(N/5)，即O(N)）
 * 3. 拿出每一组的上中位数，组成了中位数数组m，（O(N)）
 * 4. 递归调用bfprt(m,0,m.length-1,m.length/2),获得m数组的中位数，设为pivot(m规模为N/5,复杂度为T(N/5))
 * 5. 使用pivot进行partition（O(N)）
 * 6. pivot的下标没有命中k，进入一侧递归调用bfprt函数（规模：最差情况T(7/10*N)）
 * 		为什么是T(7/10*N)？
 * 		因为此时进入的一侧，假设是大于pivot的一侧：
 * 			m数组中小于等于pivot的数量为m.length/2,因为m.length=5/N，所以arr中小于等于pivot的数量为1/10 * N
 * 			又因为，m数组中每个数x，所代表的5个数中，有另外2个大于等于x，所以arr中小于等于pivot的数量需要再加上两个1/10 * N
 * 			进而得到arr中小于等于pivot的数量至少为3/10 * N,这个保底数量是确定的
 * 			所以arr中大于pivot的规模为7/10 * N，即T(7/10*N)
 * 		如果进入的是小于pivot的一侧，同理可以求出大于等于pivot的规模至少是3/10 * N，小于pivot的规模，最多7/10 * N，即T(7/10*N)
 *
 * 所以整个复杂度公式为 T(N) = T(N/5) + T(7/10*N) + O(N)
 * 该公式不能通过master公式求出复杂度，只能通过其他数学方式求出复杂度为O(N)
 *
 * 参考：为什么是5个数一组，如果3个数、7个数会怎么样？
 * 复杂度不会变化，5个一组是bfprt约定的
 */
public class Code01_FindMinKth {

	public static class MaxHeapComparator implements Comparator<Integer> {

		@Override
		public int compare(Integer o1, Integer o2) {
			return o2 - o1;
		}

	}

	// 利用大根堆，时间复杂度O(N*logK)
	public static int minKth1(int[] arr, int k) {
		PriorityQueue<Integer> maxHeap = new PriorityQueue<>(new MaxHeapComparator());
		for (int i = 0; i < k; i++) {
			maxHeap.add(arr[i]);
		}
		for (int i = k; i < arr.length; i++) {
			if (arr[i] < maxHeap.peek()) {
				maxHeap.poll();
				maxHeap.add(arr[i]);
			}
		}
		return maxHeap.peek();
	}

	/**
	 * 改写快排，时间复杂度O(N)
	 * 第K小：k >= 1
	 */
	public static int minKth2(int[] array, int k) {
		int[] arr = copyArray(array);
		/*这里k-1，因为函数返回的是第index小的数，index从0开始*/
		return process2(arr, 0, arr.length - 1, k - 1);
	}

	public static int[] copyArray(int[] arr) {
		int[] ans = new int[arr.length];
		for (int i = 0; i != ans.length; i++) {
			ans[i] = arr[i];
		}
		return ans;
	}

	/**
	 * master公式：T(N) = T(N/2) + O(N)
	 * 求得复杂度O(N)
	 * process递归改成迭代？
	 * @see class29.Code02_MaxTopK#minKth(int[], int)
	 */
	// arr 第k小的数
	// process2(arr, 0, N-1, k-1)
	// arr[L..R]  范围上，如果排序的话(不是真的去排序)，找位于index的数
	// index [L..R]
	public static int process2(int[] arr, int L, int R, int index) {
		if (L == R) { // L = =R ==INDEX
			/*index一定在[L,R]范围内*/
			return arr[L];
		}
		// 不止一个数  L +  [0, R -L]
		int pivot = arr[L + (int) (Math.random() * (R - L + 1))];
		/*O(N)*/
		int[] range = partition(arr, L, R, pivot);
		if (index >= range[0] && index <= range[1]) {
			return arr[index];
		} else if (index < range[0]) {
			return process2(arr, L, range[0] - 1, index);
		} else {
			return process2(arr, range[1] + 1, R, index);
		}
	}

	public static int[] partition(int[] arr, int L, int R, int pivot) {
		int less = L - 1;
		int more = R + 1;
		int cur = L;
		while (cur < more) {
			if (arr[cur] < pivot) {
				swap(arr, ++less, cur++);
			} else if (arr[cur] > pivot) {
				swap(arr, cur, --more);
			} else {
				cur++;
			}
		}
		return new int[] { less + 1, more - 1 };
	}

	public static void swap(int[] arr, int i1, int i2) {
		int tmp = arr[i1];
		arr[i1] = arr[i2];
		arr[i2] = tmp;
	}

	/**
	 * 利用bfprt算法，时间复杂度O(N)
	 */
	public static int minKth3(int[] array, int k) {
		int[] arr = copyArray(array);
		return bfprt(arr, 0, arr.length - 1, k - 1);
	}

	// arr[L..R]  如果排序的话，位于index位置的数，是什么，返回
	public static int bfprt(int[] arr, int L, int R, int index) {
		if (L == R) {
			return arr[L];
		}
		// L...R  每五个数一组
		// 每一个小组内部排好序
		// 小组的中位数组成新数组
		// 这个新数组的中位数返回
		int pivot = medianOfMedians(arr, L, R);
		int[] range = partition(arr, L, R, pivot);
		if (index >= range[0] && index <= range[1]) {
			return arr[index];
		} else if (index < range[0]) {
			/*T(7/10 *N)*/
			return bfprt(arr, L, range[0] - 1, index);
		} else {
			/*T(7/10 *N)*/
			return bfprt(arr, range[1] + 1, R, index);
		}
	}

	// arr[L...R]  五个数一组
	// 每个小组内部排序
	// 每个小组中位数领出来，组成marr
	// marr中的中位数，返回
	public static int medianOfMedians(int[] arr, int L, int R) {
		int size = R - L + 1;
		int offset = size % 5 == 0 ? 0 : 1;
		/*m数组的规模：N/5，如果不能整除，就再加个1*/
		int[] mArr = new int[size / 5 + offset];
		for (int team = 0; team < mArr.length; team++) {
			/*每个team的开头*/
			int teamFirst = L + team * 5;
			// L ... L + 4
			// L +5 ... L +9
			// L +10....L+14
			/*每5个一组，求中位数，最后一组不能越界*/
			mArr[team] = getMedian(arr, teamFirst, Math.min(R, teamFirst + 4));
		}
		// marr中，找到中位数
		// marr(0, marr.len - 1,  mArr.length / 2 )
		/*T(5/N)*/
		return bfprt(mArr, 0, mArr.length - 1, mArr.length / 2);
	}

	public static int getMedian(int[] arr, int L, int R) {
		insertionSort(arr, L, R);
		return arr[(L + R) / 2];
	}

	public static void insertionSort(int[] arr, int L, int R) {
		for (int i = L + 1; i <= R; i++) {
			for (int j = i - 1; j >= L && arr[j] > arr[j + 1]; j--) {
				swap(arr, j, j + 1);
			}
		}
	}

	// for test
	public static int[] generateRandomArray(int maxSize, int maxValue) {
		int[] arr = new int[(int) (Math.random() * maxSize) + 1];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = (int) (Math.random() * (maxValue + 1));
		}
		return arr;
	}

	public static void main(String[] args) {
		int testTime = 1000000;
		int maxSize = 100;
		int maxValue = 100;
		System.out.println("test begin");
		for (int i = 0; i < testTime; i++) {
			int[] arr = generateRandomArray(maxSize, maxValue);
			int k = (int) (Math.random() * arr.length) + 1;
			int ans1 = minKth1(arr, k);
			int ans2 = minKth2(arr, k);
			int ans3 = minKth3(arr, k);
			if (ans1 != ans2 || ans2 != ans3) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
