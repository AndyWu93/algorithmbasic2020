package class28;

/**
 * 长度为N字符串str，返回最长回文子串的长度
 * 暴力解
 * 依次遍历str，来到i位置，对比两边的数，看相等的区域能扩到最大长度，收集回文子串的长度，取max
 * 问题：回文是偶数个怎么解决？
 * 解答：将str处理一下，在前后都加一个特殊字符#,中间每个字符之间都加一个特殊字符。
 * 		再遍历str，求出的解/2，自动向下取整了，就是该位置的回文子串的长度，遍历完取max
 * 问题：如果str中有#字符会不会影响最终答案？
 * 解答：不会，因为每一个加入的特殊字符不会与str中的字符产生比较，都是特殊字符与特殊字符比较，也就是虚的比虚的
 * 		不会影响最终结果
 * 暴力解复杂度：列举最差情况，假设都是a，遍历每个位置时需要扩的次数为
 *  # a # a # a # a # a #
 *  0 1 2 3 4 5 4 3 2 1 0
 * 等差数列，所以必然O(N^2)
 *
 * Manacher算法：O(N)
 * 借用了KMP的理念，即前面求出来的值能够指导后面的求值，从而加速
 * 一、几个重要概念
 * 1. 回文直径：回文的长度
 * 	  回文半径：回文中心到一侧的长度，中心是实轴需要包含。r=(l+1)/2
 * 2. 回文半径数组parr：求str每个位置的回文长度时，将半径放到该位置对应的数组中
 * 3. 最右回文边界：一个变量R，计算每个位置的回文时，记录此时能够扩到最后边的位置，该位置不回退。初始值为-1
 * 4. 变量R的伴生变量C，记录是在求什么位置的回文半径时让R更新的,记录该位置的index,R不更新时，C也不用更新。初始值-1
 *
 * 二、流程
 * 流程大体参照暴力解流程，细节作了加速
 * 1. R<i
 * i位置完全暴力两边扩
 *
 * 2. R>=i
 * 此时，必然存在拓扑关系：C一定小于（或等于）i，左边界为R关于C的对称点L，i关于C的对称点i'一定大于（或等于）L
 * 即如下
 * L.....i'.....C.....i.....R
 * 	1）i'的回文区域（可由parr[i']获得）在[L,R]内部
 * 	直接得到i的回文区域，parr[i]=parr[i'],因为都是关于C对称
 *
 * 	2）i'的回文区域（可由parr[i']获得）在[L,R]外部
 * 	(..L.....i'.....L'..)..C..R'......i.......R
 * 	 ---i'回文直径-------      ---i回文直径------
 * 	直接得到i的回文半径，即i->R
 * 	证明：因为L,R关于C对称，那[L,L'],[R',R]一定关于C对称，又因为[L,L']关于i'对称，所以[R,R']关于i对称
 * 		 此外，因为L的左边不等于R的右边，又因为，L的左边==L'的右边==R'左边，所以R'左边不等于R的右边
 * 		 即i的回文半径即[i,R]
 *
 *  3)i'的回文区域（可由parr[i']获得）与L压线
 * 	(L.....i'.....L')..C...R'......i.......R
 * 	 ---i'回文直径--       ?----i回文直径-----?
 *  [R',R]一定在i的回文区域内，但是R'左边和R右边是否相等，需要验证，所以i的回文区域需要自己尝试扩一部分
 *
 * 复杂度分析：
 * 1.i向外扩推高R，最多成功N次，最多失败N次
 * 2.
 *	1）直接拿O(1)
 *	2）直接拿O(1)
 *	3）i接着R继续向外扩，推高R，最多成功N次，最多失败N次
 * R的变化范围0-N，且不回退。
 * 复杂度O(N)
 */
public class Code01_Manacher {

	public static int manacher(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		// "12132" -> "#1#2#1#3#2#"
		char[] str = manacherString(s);
		// 回文半径的大小
		int[] pArr = new int[str.length];
		int C = -1;
		/*
		* 讲述中：R代表最右的扩成功的位置
		* coding：最右的扩成功位置的，再下一个位置
		* */
		int R = -1;
		int max = Integer.MIN_VALUE;
		/*从0位置开始，依次往右扩*/
		for (int i = 0; i < str.length; i++) { // 0 1 2
			// R第一个违规的位置，i>= R
			// i位置扩出来的答案，i位置扩的区域，至少是多大。
			/*
			* 1. 如果i在R内，讲述中是i<=R,这里i<R
			* 原则上来讲是分3中情况，这里是先求出一个依靠前面的值能够得到的最小长度
			* Math.min(pArr[2 * C - i], R - i)：
			* 2 * C - i：就是i'的位置，即i关于C的对称点，这里是i'的回文区域在L内的情况
			* R-i：就是i到R的距离，是当i'的回文范围大于L（就是R-i），或者压线（至少R-i）
			* 这两个值谁小取谁，因为谁更小，就代表了当前是哪种情况，关于压线的后面再继续扩
			* 2. 如果i在R外，那此时i的回文半径至少是1，就是他自己。
			* */
			pArr[i] = R > i ? Math.min(pArr[2 * C - i], R - i) : 1;
			/*
			* 上面一步做完后，i位置的半径再继续扩，上面几种情况，这里统一处理
			* 如果不相等，直接就break了，节省代码量
			* */
			while (i + pArr[i] < str.length && i - pArr[i] > -1) {
				/*i左右半径的长度的字符对比*/
				if (str[i + pArr[i]] == str[i - pArr[i]])
					/*相等就++*/
					pArr[i]++;
				else {
					break;
				}
			}
			/*求出了i位置的回文半径，需要记录R和C的位置*/
			if (i + pArr[i] > R) {
				/*这里的R是i回文半径的下一个位置*/
				R = i + pArr[i];
				C = i;
			}
			/*收集最大半径*/
			max = Math.max(max, pArr[i]);
		}
		/*去掉了str中特殊字符后的回文直径*/
		return max - 1;
	}

	public static char[] manacherString(String str) {
		char[] charArr = str.toCharArray();
		char[] res = new char[str.length() * 2 + 1];
		int index = 0;
		for (int i = 0; i != res.length; i++) {
			res[i] = (i & 1) == 0 ? '#' : charArr[index++];
		}
		return res;
	}

	// for test
	public static int right(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] str = manacherString(s);
		int max = 0;
		for (int i = 0; i < str.length; i++) {
			int L = i - 1;
			int R = i + 1;
			while (L >= 0 && R < str.length && str[L] == str[R]) {
				L--;
				R++;
			}
			max = Math.max(max, R - L - 1);
		}
		return max / 2;
	}

	// for test
	public static String getRandomString(int possibilities, int size) {
		char[] ans = new char[(int) (Math.random() * size) + 1];
		for (int i = 0; i < ans.length; i++) {
			ans[i] = (char) ((int) (Math.random() * possibilities) + 'a');
		}
		return String.valueOf(ans);
	}

	public static void main(String[] args) {
		int possibilities = 5;
		int strSize = 20;
		int testTimes = 5000000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			String str = getRandomString(possibilities, strSize);
			if (manacher(str) != right(str)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
