package class17;

import java.util.ArrayList;
import java.util.List;

/**
 * 认识递归
 * 打印全排列
 * 涉及到递归中一种重要场景：恢复现场
 */
public class Code04_PrintAllPermutations {

	public static List<String> permutation1(String s) {
		List<String> ans = new ArrayList<>();
		if (s == null || s.length() == 0) {
			return ans;
		}
		char[] str = s.toCharArray();
		ArrayList<Character> rest = new ArrayList<Character>();
		for (char cha : str) {
			rest.add(cha);
		}
		String path = "";
		f(rest, path, ans);
		return ans;
	}

	/**
	 * 递归含义：
	 * 还剩下的字符在rest里，已经排好的字符在path中，剩下的字符任意排序，排好后将结果收集到ans
	 * @param rest
	 * @param path
	 * @param ans
	 */
	public static void f(ArrayList<Character> rest, String path, List<String> ans) {
		if (rest.isEmpty()) {
			/*字符都决策完了*/
			ans.add(path);
		} else {
			int N = rest.size();
			/*
			* for循环：rest中i位置字符先拿出来，排在path后面，剩下的字符再去做决策。
			* 拿i+1位置的字符前，i位置字符还要放进去，留给后面去做决策
			* */
			for (int i = 0; i < N; i++) {
				char cur = rest.get(i);
				rest.remove(i);
				f(rest, path + cur, ans);
				/*恢复现场*/
				rest.add(i, cur);
			}
		}
	}

	/**
	 * 递归中的可变参数优化版本
	 * @param s
	 * @return
	 */
	public static List<String> permutation2(String s) {
		List<String> ans = new ArrayList<>();
		if (s == null || s.length() == 0) {
			return ans;
		}
		char[] str = s.toCharArray();
		g1(str, 0, ans);
		return ans;
	}

	/**
	 * 递归含义：
	 * str中包含所有字符，index位置的值和后面任意位置的值交换，交换后index来到index+1位置
	 * 直到index来到n位置，将结果收集到ans
	 * @param str
	 * @param index
	 * @param ans
	 */
	public static void g1(char[] str, int index, List<String> ans) {
		if (index == str.length) {
			ans.add(String.valueOf(str));
		} else {
			/*index位置的值和后面任意位置的值交换*/
			for (int i = index; i < str.length; i++) {
				swap(str, index, i);
				g1(str, index + 1, ans);
				/*恢复现场*/
				swap(str, index, i);
			}
		}
	}

	/**
	 * 全排列结果去重（递归剪枝）
	 * @param s
	 * @return
	 */
	public static List<String> permutation3(String s) {
		List<String> ans = new ArrayList<>();
		if (s == null || s.length() == 0) {
			return ans;
		}
		char[] str = s.toCharArray();
		g2(str, 0, ans);
		return ans;
	}

	public static void g2(char[] str, int index, List<String> ans) {
		if (index == str.length) {
			ans.add(String.valueOf(str));
		} else {
			/*去重：index位置的值和后面任意位置的值交换，必须是不同的值。如果相同交换了值还是一样，就达不到去重的效果*/
			boolean[] visited = new boolean[256];
			for (int i = index; i < str.length; i++) {
				if (!visited[str[i]]) {
					visited[str[i]] = true;
					swap(str, index, i);
					g2(str, index + 1, ans);
					swap(str, index, i);
				}
			}
		}
	}

	public static void swap(char[] chs, int i, int j) {
		char tmp = chs[i];
		chs[i] = chs[j];
		chs[j] = tmp;
	}

	public static void main(String[] args) {
		String s = "acc";
		List<String> ans1 = permutation1(s);
		for (String str : ans1) {
			System.out.println(str);
		}
		System.out.println("=======");
		List<String> ans2 = permutation2(s);
		for (String str : ans2) {
			System.out.println(str);
		}
		System.out.println("=======");
		List<String> ans3 = permutation3(s);
		for (String str : ans3) {
			System.out.println(str);
		}

	}

}
