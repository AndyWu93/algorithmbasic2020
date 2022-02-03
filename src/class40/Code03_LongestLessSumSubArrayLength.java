package class40;

/**
 * 数组三连：
 * 数组二连中，找到子数组小于等于k时，最长的子数组长度
 * 解题：
 * 	该题最优解仍然可以做到O(N)，想要做到O(N)，求解过程中就需要舍弃一些可能性，以达到不回退O(N)
 * 两个重要的辅助数组：
 * 	minSums[i]：必须以arr[i]开头，累加和最小的子数组sum
 * 	minSumEnds[i]：必须以arr[i]开头，累加和最小的子数组右边界位置
 * 解题思路：
 * 	使用窗口，枚举arr[i]作为窗口的开头位置，
 * 	来到到了i位置，通过minSums的信息看该窗口能否往右扩，
 * 		能扩：扩到不能扩了，收集子数组长度
 * 		不能扩：以i位置开头的结果可以舍弃不用求了，
 * 			为什么？因为i-1位置的结果肯定比i位置的结果长。
 * 	窗口开头来到i+1位置..
 */
public class Code03_LongestLessSumSubArrayLength {

	public static int maxLengthAwesome(int[] arr, int k) {
		if (arr == null || arr.length == 0) {
			return 0;
		}
		int[] minSums = new int[arr.length];
		int[] minSumEnds = new int[arr.length];
		/*
		* 这两个数组从后往前求，为什么？因为i位置的值依赖i+1位置的值
		* minSums[n-1] = arr[n-1] : 必须以arr[n-1]开头的最小累加和子数组sum就是它自己
		* */
		minSums[arr.length - 1] = arr[arr.length - 1];
		minSumEnds[arr.length - 1] = arr.length - 1;
		/*从n-2位置往前遍历*/
		for (int i = arr.length - 2; i >= 0; i--) {
			if (minSums[i + 1] < 0) {
				/*如果i+1位置的sum小于0，求i位置是应该吧i+1位置的结果加进来，因为加加进来以后sum更小了*/
				minSums[i] = arr[i] + minSums[i + 1];
				/*i的右边界也就可以取i+1的右边界*/
				minSumEnds[i] = minSumEnds[i + 1];
			} else {
				/*否则就只有自己*/
				minSums[i] = arr[i];
				minSumEnds[i] = i;
			}
		}
		// 迟迟扩不进来那一块儿的开头位置
		/*无法进入窗口的开头位置*/
		int end = 0;
		/*窗口内的累加和*/
		int sum = 0;
		/*收集的结果*/
		int ans = 0;
		for (int i = 0; i < arr.length; i++) {
			// while循环结束之后：
			// 1) 如果以i开头的情况下，累加和<=k的最长子数组是arr[i..end-1]，看看这个子数组长度能不能更新res；
			// 2) 如果以i开头的情况下，累加和<=k的最长子数组比arr[i..end-1]短，更新还是不更新res都不会影响最终结果；
			/*end未越界，且end开头的最小累加和子数组能够加进来*/
			while (end < arr.length && sum + minSums[end] <= k) {
				sum += minSums[end];
				end = minSumEnds[end] + 1;
			}
			/*搜集一下答案，后面窗口要换开头了*/
			ans = Math.max(ans, end - i);
			if (end > i) { // 还有窗口，哪怕窗口没有数字 [i~end) [4,4)
				/*还有窗口：窗口左边i位置出窗口*/
				sum -= arr[i];
			} else { // i == end,  即将 i++, i > end, 此时窗口概念维持不住了，所以end跟着i一起走
				/*没有窗口了，换了开头，结尾也一起换掉*/
				end = i + 1;
			}
		}
		return ans;
	}

	public static int maxLength(int[] arr, int k) {
		int[] h = new int[arr.length + 1];
		int sum = 0;
		h[0] = sum;
		for (int i = 0; i != arr.length; i++) {
			sum += arr[i];
			h[i + 1] = Math.max(sum, h[i]);
		}
		sum = 0;
		int res = 0;
		int pre = 0;
		int len = 0;
		for (int i = 0; i != arr.length; i++) {
			sum += arr[i];
			pre = getLessIndex(h, sum - k);
			len = pre == -1 ? 0 : i - pre + 1;
			res = Math.max(res, len);
		}
		return res;
	}

	public static int getLessIndex(int[] arr, int num) {
		int low = 0;
		int high = arr.length - 1;
		int mid = 0;
		int res = -1;
		while (low <= high) {
			mid = (low + high) / 2;
			if (arr[mid] >= num) {
				res = mid;
				high = mid - 1;
			} else {
				low = mid + 1;
			}
		}
		return res;
	}

	// for test
	public static int[] generateRandomArray(int len, int maxValue) {
		int[] res = new int[len];
		for (int i = 0; i != res.length; i++) {
			res[i] = (int) (Math.random() * maxValue) - (maxValue / 3);
		}
		return res;
	}

	public static void main(String[] args) {
		System.out.println("test begin");
		for (int i = 0; i < 10000000; i++) {
			int[] arr = generateRandomArray(10, 20);
			int k = (int) (Math.random() * 20) - 5;
			if (maxLengthAwesome(arr, k) != maxLength(arr, k)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
