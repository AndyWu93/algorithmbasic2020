package class19;

/**
 * 给定一个数组（0~9）组成的str，将数字（1~26）转化为字母（a~z），问有多少种转化方式
 * 思路：动态dp，从左往右的尝试模型
 * dp[i]:i位置之前的字符都转好了，str[i..n-1]有多少种转化方式
 * 规模：dp[n+1]
 * dp[n]=1：没有字符了，收集到了1种转化结果
 * 普遍位置dp[i]:
 * a.str[i]=='0' dp[i]=0:以0开头的字符串无法转
 * b.str[i]!='0' dp[i]=dp[i+1]+dp[i+2] (dp[i+2]需要判断一下i和i+1位置组合起立的数字是否小于26才加上去)
 * 从左往右填dp
 */
public class Code02_ConvertToLetterString {

	// str只含有数字字符0~9
	// 返回多少种转化方案
	public static int number(String str) {
		if (str == null || str.length() == 0) {
			return 0;
		}
		return process(str.toCharArray(), 0);
	}

	// str[0..i-1]转化无需过问
	// str[i.....]去转化，返回有多少种转化方法
	public static int process(char[] str, int i) {
		if (i == str.length) {
			/*没有字符了，表示所有字符全部都转化完了，收集到1种方法*/
			return 1;
		}
		// i没到最后，说明有字符
		if (str[i] == '0') { // 之前的决定有问题
			/*来到的i位置不能是0，说明之前的决定是无效方案*/
			return 0;
		}
		// str[i] != '0'
		/*可能性一，i坐好了决定：单转*/
		int ways = process(str, i + 1);
		if (i + 1 < str.length && (str[i] - '0') * 10 + str[i + 1] - '0' < 27) {
			/*可能性二：i和i+1位置组合起来转，上面是成立条件*/
			ways += process(str, i + 2);
		}
		return ways;
	}

	// 从右往左的动态规划
	// 就是上面方法的动态规划版本
	// dp[i]表示：str[i...]有多少种转化方式
	public static int dp1(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		int[] dp = new int[N + 1];
		dp[N] = 1;
		for (int i = N - 1; i >= 0; i--) {
			/*str[i]=='0'时，dp[i]=0*/
			if (str[i] != '0') {
				int ways = dp[i + 1];
				if (i + 1 < str.length && (str[i] - '0') * 10 + str[i + 1] - '0' < 27) {
					ways += dp[i + 2];
				}
				dp[i] = ways;
			}
		}
		return dp[0];
	}

	// 从左往右的动态规划
	// dp[i]表示：str[0...i]有多少种转化方式
	public static int dp2(String s) {
		if (s == null || s.length() == 0) {
			return 0;
		}
		char[] str = s.toCharArray();
		int N = str.length;
		if (str[0] == '0') {
			return 0;
		}
		int[] dp = new int[N];
		dp[0] = 1;
		for (int i = 1; i < N; i++) {
			if (str[i] == '0') {
				// 如果此时str[i]=='0'，那么他是一定要拉前一个字符(i-1的字符)一起拼的，
				// 那么就要求前一个字符，不能也是‘0’，否则拼不了。
				// 前一个字符不是‘0’就够了嘛？不够，还得要求拼完了要么是10，要么是20，如果更大的话，拼不了。
				// 这就够了嘛？还不够，你们拼完了，还得要求str[0...i-2]真的可以被分解！
				// 如果str[0...i-2]都不存在分解方案，那i和i-1拼成了也不行，因为之前的搞定不了。
				if (str[i - 1] == '0' || str[i - 1] > '2' || (i - 2 >= 0 && dp[i - 2] == 0)) {
					return 0;
				} else {
					dp[i] = i - 2 >= 0 ? dp[i - 2] : 1;
				}
			} else {
				dp[i] = dp[i - 1];
				if (str[i - 1] != '0' && (str[i - 1] - '0') * 10 + str[i] - '0' <= 26) {
					dp[i] += i - 2 >= 0 ? dp[i - 2] : 1;
				}
			}
		}
		return dp[N - 1];
	}

	// 为了测试
	public static String randomString(int len) {
		char[] str = new char[len];
		for (int i = 0; i < len; i++) {
			str[i] = (char) ((int) (Math.random() * 10) + '0');
		}
		return String.valueOf(str);
	}

	// 为了测试
	public static void main(String[] args) {
		int N = 30;
		int testTime = 1000000;
		System.out.println("测试开始");
		for (int i = 0; i < testTime; i++) {
			int len = (int) (Math.random() * N);
			String s = randomString(len);
			int ans0 = number(s);
			int ans1 = dp1(s);
			int ans2 = dp2(s);
			if (ans0 != ans1 || ans0 != ans2) {
				System.out.println(s);
				System.out.println(ans0);
				System.out.println(ans1);
				System.out.println(ans2);
				System.out.println("Oops!");
				break;
			}
		}
		System.out.println("测试结束");
	}

}
