package class19;

import java.util.HashMap;

/**
 * 用字符串贴纸数组arr中的任意贴纸，每个贴纸有无数张，拼成一个给定的字符串str，贴纸可以剪开。问需要几张贴纸至少
 * 本题用与来理解从左往后尝试模型的优化
 * 本题无法改为位置依赖的dp，只能使用记忆化搜索
 */
// 本题测试链接：https://leetcode.com/problems/stickers-to-spell-word
public class Code03_StickersToSpellWord {

	public static int minStickers1(String[] stickers, String target) {
		int ans = process1(stickers, target);
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	/*
	* 尝试方案：
	* 枚举所有的贴纸作为第一张贴纸的时候，一共需要多少张贴纸
	* stickers ：所有贴纸，每一种贴纸都有无穷张
	* target
	* 返回 最少张数
	* */
	public static int process1(String[] stickers, String target) {
		if (target.length() == 0) {
			/*没有字符串了，不需要贴纸了*/
			return 0;
		}
		int min = Integer.MAX_VALUE;
		/*每张贴纸作为第一张*/
		for (String first : stickers) {
			/*搞定了target中的字符，还有rest没搞定*/
			String rest = minus(target, first);
			/*搞定了x个字符，rest长度就会比target少x*/
			if (rest.length() != target.length()) {
				/*后续还要几张，算出来*/
				min = Math.min(min, process1(stickers, rest));
			}
		}
		/*
		* min == Integer.MAX_VALUE：一个字符也搞不定
		* 否则再加上第一张贴纸
		* */
		return min + (min == Integer.MAX_VALUE ? 0 : 1);
	}

	public static String minus(String s1, String s2) {
		char[] str1 = s1.toCharArray();
		char[] str2 = s2.toCharArray();
		/*这个数组先统计s1的所有字母次数，再减掉s2字母次数，删下的拼起来就是s1-s2的*/
		int[] count = new int[26];
		for (char cha : str1) {
			count[cha - 'a']++;
		}
		for (char cha : str2) {
			count[cha - 'a']--;
		}
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < 26; i++) {
			if (count[i] > 0) {
				for (int j = 0; j < count[i]; j++) {
					builder.append((char) (i + 'a'));
				}
			}
		}
		return builder.toString();
	}

	public static int minStickers2(String[] stickers, String target) {
		int N = stickers.length;
		// 关键优化(用词频表替代贴纸数组)
		int[][] counts = new int[N][26];
		for (int i = 0; i < N; i++) {
			char[] str = stickers[i].toCharArray();
			for (char cha : str) {
				counts[i][cha - 'a']++;
			}
		}
		int ans = process2(counts, target);
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	/*
	* 尝试的优化：
	* 1. 枚举所有贴纸中包含target第一个字符的贴纸作为第一张贴纸（剪枝技巧）
	* 2. 将贴纸和target都做成词频统计，词频直接减
	* */
	// stickers[i] 数组，当初i号贴纸的字符统计 int[][] stickers -> 所有的贴纸
	public static int process2(int[][] stickers, String t) {
		if (t.length() == 0) {
			return 0;
		}
		/*将target做成词频统计*/
		// target  aabbc  2 2 1..
		//                0 1 2..
		char[] target = t.toCharArray();
		int[] tcounts = new int[26];
		for (char cha : target) {
			tcounts[cha - 'a']++;
		}
		int N = stickers.length;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < N; i++) {
			// 尝试第一张贴纸是谁
			int[] sticker = stickers[i];
			// 最关键的优化(重要的剪枝!这一步也是贪心!)
			/*该贴纸中关于target第一个字符的词频统计不为0，表示一定存在target[0]的字符*/
			if (sticker[target[0] - 'a'] > 0) {
				StringBuilder builder = new StringBuilder();
				for (int j = 0; j < 26; j++) {
					if (tcounts[j] > 0) {
						int nums = tcounts[j] - sticker[j];
						for (int k = 0; k < nums; k++) {
							builder.append((char) (j + 'a'));
						}
					}
				}
				String rest = builder.toString();
				min = Math.min(min, process2(stickers, rest));
			}
		}
		return min + (min == Integer.MAX_VALUE ? 0 : 1);
	}

	/**
	 * 最优解：
	 * 是第二种尝试+记忆化搜索
	 * 为什么用记忆化搜索？可变参数为string，无法列出变化范围
	 * @param stickers
	 * @param target
	 * @return
	 */
	public static int minStickers3(String[] stickers, String target) {
		int N = stickers.length;
		int[][] counts = new int[N][26];
		for (int i = 0; i < N; i++) {
			char[] str = stickers[i].toCharArray();
			for (char cha : str) {
				counts[i][cha - 'a']++;
			}
		}
		HashMap<String, Integer> dp = new HashMap<>();
		dp.put("", 0);
		int ans = process3(counts, target, dp);
		return ans == Integer.MAX_VALUE ? -1 : ans;
	}

	public static int process3(int[][] stickers, String t, HashMap<String, Integer> dp) {
		if (dp.containsKey(t)) {
			return dp.get(t);
		}
		char[] target = t.toCharArray();
		int[] tcounts = new int[26];
		for (char cha : target) {
			tcounts[cha - 'a']++;
		}
		int N = stickers.length;
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < N; i++) {
			int[] sticker = stickers[i];
			if (sticker[target[0] - 'a'] > 0) {
				StringBuilder builder = new StringBuilder();
				for (int j = 0; j < 26; j++) {
					if (tcounts[j] > 0) {
						int nums = tcounts[j] - sticker[j];
						for (int k = 0; k < nums; k++) {
							builder.append((char) (j + 'a'));
						}
					}
				}
				String rest = builder.toString();
				min = Math.min(min, process3(stickers, rest, dp));
			}
		}
		int ans = min + (min == Integer.MAX_VALUE ? 0 : 1);
		dp.put(t, ans);
		return ans;
	}

}
