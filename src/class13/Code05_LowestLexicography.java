package class13;

import java.util.Arrays;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * str数组中所有元素拼在一起，怎么拼字典序最小
 * 字典序概念：
 * 1.长度一样，从高位到低位比较
 * 2.长度不一样，较短的str补0后，一样长后再比较
 *
 * 解法一：贪心
 * 思路：按某种比较方式将arr排列，依次拼接起来即可
 * 按arr[i]+arr[i+1],arr[i+1]+arr[i]的字典序比较
 * 证明：
 * 第一步
 * 按照上面的方式排完以后，因为比较的传递性(传递性证明略)
 * 对于任意的i<j，有arr[i]+arr[j]的字典序<=arr[j]+arr[i]的字典序
 * 第二步
 * 排序好的arr中，任意i，j位置的str交换后，i..j中间的str拼接<=j..i中间的str拼接（证明略）
 *
 * 一般情况下用对数器证明贪心算法
 * 解法二：暴力解
 */
public class Code05_LowestLexicography {

	public static String lowestString1(String[] strs) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		TreeSet<String> ans = process(strs);
		return ans.size() == 0 ? "" : ans.first();
	}

	/*strs中所有字符串全排列，拼接起来返回所有可能的结果*/
	public static TreeSet<String> process(String[] strs) {
		TreeSet<String> ans = new TreeSet<>();
		if (strs.length == 0) {
			/*这里不给的话下面第一个字符串就拼接不上去了*/
			ans.add("");
			return ans;
		}
		for (int i = 0; i < strs.length; i++) {
			/*枚举每个位置的第一个值*/
			String first = strs[i];
			/*删掉该位置，得到剩下的arr*/
			String[] nexts = removeIndexString(strs, i);
			/*剩下的arr全排列组成的各种组合*/
			TreeSet<String> next = process(nexts);
			for (String cur : next) {
				/*再把第一个拼到前面去*/
				ans.add(first + cur);
			}
		}
		return ans;
	}

	/*把index位置的str remove掉，生成新的arr*/
	// {"abc", "cks", "bct"}
	// 0 1 2
	// removeIndexString(arr , 1) -> {"abc", "bct"}
	public static String[] removeIndexString(String[] arr, int index) {
		int N = arr.length;
		String[] ans = new String[N - 1];
		int ansIndex = 0;
		for (int i = 0; i < N; i++) {
			if (i != index) {
				ans[ansIndex++] = arr[i];
			}
		}
		return ans;
	}

	public static class MyComparator implements Comparator<String> {
		@Override
		public int compare(String a, String b) {
			return (a + b).compareTo(b + a);
		}
	}

	/**
	 * 贪心
	 * @param strs
	 * @return
	 */
	public static String lowestString2(String[] strs) {
		if (strs == null || strs.length == 0) {
			return "";
		}
		Arrays.sort(strs, new MyComparator());
		String res = "";
		for (int i = 0; i < strs.length; i++) {
			res += strs[i];
		}
		return res;
	}

	// for test
	public static String generateRandomString(int strLen) {
		char[] ans = new char[(int) (Math.random() * strLen) + 1];
		for (int i = 0; i < ans.length; i++) {
			int value = (int) (Math.random() * 5);
			ans[i] = (Math.random() <= 0.5) ? (char) (65 + value) : (char) (97 + value);
		}
		return String.valueOf(ans);
	}

	// for test
	public static String[] generateRandomStringArray(int arrLen, int strLen) {
		String[] ans = new String[(int) (Math.random() * arrLen) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = generateRandomString(strLen);
		}
		return ans;
	}

	// for test
	public static String[] copyStringArray(String[] arr) {
		String[] ans = new String[arr.length];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = String.valueOf(arr[i]);
		}
		return ans;
	}

	public static void main(String[] args) {
		int arrLen = 6;
		int strLen = 5;
		int testTimes = 10000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			String[] arr1 = generateRandomStringArray(arrLen, strLen);
			String[] arr2 = copyStringArray(arr1);
			if (!lowestString1(arr1).equals(lowestString2(arr2))) {
				for (String str : arr1) {
					System.out.print(str + ",");
				}
				System.out.println();
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");
	}

}
