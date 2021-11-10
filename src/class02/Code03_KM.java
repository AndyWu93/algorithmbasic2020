package class02;

import java.util.HashMap;
import java.util.HashSet;

/**
 * arr中只有两种数，出现了k次和m次，找出出现了k次的数(M>1,K<M)
 * 要求额外空间复杂度O(1),时间复杂度O(N)
 * 总体思路：
 * 挑出所有数1-32位上的1，1-32位分别将这些1加起来，每一位的总数：
 * 一定是m的倍数，如果不是，一定是k的倍数(只出现了K次的数有1)，或者%m后等于k（出现k次和m次的位上都有1）
 */
public class Code03_KM {

	public static int test(int[] arr, int k, int m) {
		HashMap<Integer, Integer> map = new HashMap<>();
		for (int num : arr) {
			if (map.containsKey(num)) {
				map.put(num, map.get(num) + 1);
			} else {
				map.put(num, 1);
			}
		}
		for (int num : map.keySet()) {
			if (map.get(num) == k) {
				return num;
			}
		}
		return -1;
	}

	public static HashMap<Integer, Integer> map = new HashMap<>();

	// 请保证arr中，只有一种数出现了K次，其他数都出现了M次
	public static int onlyKTimes(int[] arr, int k, int m) {
		if (map.size() == 0) {
			/*
			* 生成一个map，
			* key:在二进制下，一个位上是1，其他都是0（如10，100，1000，10000）
			* value:这个1的位在第几位[0,31]
			* */
			mapCreater(map);
		}
		int[] t = new int[32];
		// t[0] 0位置的1出现了几个
		// t[i] i位置的1出现了几个
		for (int num : arr) {
			while (num != 0) {
				/*拿到num最右边的1*/
				int rightOne = num & (-num);
				t[map.get(rightOne)]++;
				/*将num该位上的1变成0，后继续直到num所有的位上都是0*/
				num ^= rightOne;
			}
		}
		/*下面是把这个数每个位上的1找到，拼起来*/
		int ans = 0;
		for (int i = 0; i < 32; i++) {
			/*出现m次的不用看了*/
			if (t[i] % m != 0) {
				if (t[i] % m == k) {
					/*因为k<m，这里找到了出现k次以及出现了k次也出现了m次的数*/
					ans |= (1 << i);
				} else {
					return -1;
				}
			}
		}
		if (ans == 0) {
			/*如果结果是0，再确认一下，这里出现了k次的数是不是0*/
			int count = 0;
			for (int num : arr) {
				if (num == 0) {
					count++;
				}
			}
			if (count != k) {
				return -1;
			}
		}
		return ans;
	}

	public static void mapCreater(HashMap<Integer, Integer> map) {
		int value = 1;
		for (int i = 0; i < 32; i++) {
			map.put(value, i);
			value <<= 1;
		}
	}

	public static int[] randomArray(int maxKinds, int range, int k, int m) {
		int ktimeNum = randomNumber(range);
		// 真命天子出现的次数
		int times = Math.random() < 0.5 ? k : ((int) (Math.random() * (m - 1)) + 1);
		// 2
		int numKinds = (int) (Math.random() * maxKinds) + 2;
		// k * 1 + (numKinds - 1) * m
		int[] arr = new int[times + (numKinds - 1) * m];
		int index = 0;
		for (; index < times; index++) {
			arr[index] = ktimeNum;
		}
		numKinds--;
		HashSet<Integer> set = new HashSet<>();
		set.add(ktimeNum);
		while (numKinds != 0) {
			int curNum = 0;
			do {
				curNum = randomNumber(range);
			} while (set.contains(curNum));
			set.add(curNum);
			numKinds--;
			for (int i = 0; i < m; i++) {
				arr[index++] = curNum;
			}
		}
		// arr 填好了
		for (int i = 0; i < arr.length; i++) {
			// i 位置的数，我想随机和j位置的数做交换
			int j = (int) (Math.random() * arr.length);// 0 ~ N-1
			int tmp = arr[i];
			arr[i] = arr[j];
			arr[j] = tmp;
		}
		return arr;
	}

	// [-range, +range]
	public static int randomNumber(int range) {
		return ((int) (Math.random() * range) + 1) - ((int) (Math.random() * range) + 1);
	}

	public static void main(String[] args) {
		int kinds = 5;
		int range = 30;
		int testTime = 100000;
		int max = 9;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int a = (int) (Math.random() * max) + 1; // a 1 ~ 9
			int b = (int) (Math.random() * max) + 1; // b 1 ~ 9
			int k = Math.min(a, b);
			int m = Math.max(a, b);
			// k < m
			if (k == m) {
				m++;
			}
			int[] arr = randomArray(kinds, range, k, m);
			int ans1 = test(arr, k, m);
			int ans2 = onlyKTimes(arr, k, m);
			if (ans1 != ans2) {
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println("出错了！");
			}
		}
		System.out.println("测试结束");

	}

}
