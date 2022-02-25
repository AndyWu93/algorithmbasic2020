package class27;

/**
 * 高阶算法入门之KMP，KMP启蒙了ac自动机。本篇幅代码要求能够默写出来
 *
 * 快速查找出一个str1中str2的位置，返回第一个匹配到的index
 * 暴力解：遍历str1，来到i位置，从i位置开始依次与str2比对，复杂度O(N*M)
 * KMP算法：能够做到O(N)
 *
 * 1. 首先来了解一种str的信息处理：next数组
 * 即不含自己的前面字符串中，前缀和后缀相等的最大长度（不能包含整体）是多少
 * 比如
 * a b c a b c k
 * k位置的信息就是3，因为k位置前面的前缀和后缀相等的最大长度是3即"abc"
 * 规定：
 * 字符串第一个位置的信息是-1，因为没有前缀
 * 字符串第二个位置的信息是0，因为前缀不能取整体
 * next数组怎么求？
 * a.第0位置和第1位置的值一定是-1和0；
 * b.后面的位置上的值可以根据前面位置上的值求出来。
 * 具体流程：
 * 假设求到了i位置，i-1位置的值时b，next[i-1]=7
 * 说明0-6位置为相等的前缀(这里相等的前缀是指从0开始的前缀和i-1位置往前的后缀相等)，看7位置上的值和i-1位置的值是否相等
 * 	相等：next[i]=7+1=8
 * 	不相等：看next[7]的值，假设为3
 * 		说明0-2位置为相等的前缀，那就看3位置上的值和i-1位置的值是否相等
 * 		相等：next[i]=3+1=4
 * 		不相等：看next[3]的值
 * 			...
 *
 * 2. 将str2生成next数组
 * 遍历str1，来到i位置，与str2比较，到str1的x位置、str2的y位置比对失败
 * s1    ....i..........o.........j...........x......
 *                                ------a-----
 *                                |
 * s2        0 1 2.......k........l...........y......
 *           ------a------        ------a-----
 * y位置的信息：a，表示y位置之前，有a长度的前缀和后缀相等，即[0,k]==[l,y-1]==[j,x-1]
 * o是s1的i位置到j位置中间的任意位置，从o开始的str，和s2比对一定会失败，为什么呢？
 * 假设o位置开始能够比对成功，那[o,x]位置一定能和s2的[0,某个大于k的位置]比对成功。
 * 而原先已经证明了s1[o,x-1]与s2[某个小于l的位置,y-1]是一一对应的，
 * 从而得到s2[0,某个大于k的位置]==s2[某个小于l的位置,y-1],与y位置的信息不一致。所以，s1[i,j]不可能求出解
 * 于是，s2右移，0位置与s1的j位置对其，
 * 又因为s2[l,y-1]==s1[j,x-1]所以，从s1的x位置与s2的k+1位置继续比对
 *
 * s1    ....i..........o.........j...........x......
 *                                |           |
 * s2        ------>              0 1 2......k+1.....
 *
 * 重复上述流程，直到s2右移到0位置与s1的x位置比对，0位置的信息是-1，如果还是失败，不能够右移了
 * 继续遍历str1，来到x+1位置
 * 关于s2右移的快速实现：原来是x位置和y位置对比，失败后x位置与k+1位置对比，s2的y的信息就是k+1，
 * 所以直接与s2当前位置的信息的位置对比就实现了右移
 */
public class Code01_KMP {

	public static int getIndexOf(String s1, String s2) {
		if (s1 == null || s2 == null || s2.length() < 1 || s1.length() < s2.length()) {
			return -1;
		}
		char[] str1 = s1.toCharArray();
		char[] str2 = s2.toCharArray();
		/*x:s1当前比对到的位置，y:s2当前比对到的位置*/
		int x = 0;
		/*跳跃指针*/
		int y = 0;
		// O(M) m <= n
		int[] next = getNextArray(str2);
		/*
		* O(N)推理过程
		* 下面这个循环在进行过程中，关注连个两个量的变化：x,x-y
		* x:最大值为s1的长度，x-y：最大值也是s1的长度
		* 循环过程中  x    x-y
		* 情况1      ↑    不变
		* 情况2      ↑     ↑
		* 情况3     不变    ↑
		* 3种情况下，x和x-y的变化范围都没有超过N，所以复杂度O(N)
		* */
		while (x < str1.length && y < str2.length) {
			if (str1[x] == str2[y]) {
				/*1.相等，两个指针都右移*/
				x++;
				y++;
			} else if (next[y] == -1) { // y == 0
				/*2.不相等，但是y已经往前推到了0位置，y指针不可能再往前了，所以x指针++*/
				x++;
			} else {
				/*3.不相等，y往前跳到前缀长度的后一个位置*/
				y = next[y];
			}
		}
		/*
		* y越界：s2全部比对成功，x往前推y个长度就是x中的首个index，
		* x越界：s1到最后都没有找到一个和s2相等的str
		* */
		return y == str2.length ? x - y : -1;
	}

	public static int[] getNextArray(char[] str2) {
		if (str2.length == 1) {
			return new int[] { -1 };
		}
		int[] next = new int[str2.length];
		/*前面连个位置的值是固定的*/
		next[0] = -1;
		next[1] = 0;
		/*目前在哪个位置上求next数组的值*/
		int i = 2;
		/*
		* 跳跃指针：cn一直都是在从0开始往后的位置，在这些范围内跳跃
		* 当前是哪个位置的值再和i-1位置的字符比较
		* 初始值为什么是0？因为此时i位置前面就两个数
		* */
		int cn = 0;
		 /*
		 * 复杂度O(M)
		 * 证明，观察两个变量：i, i-cn
		 * */
		while (i < next.length) {
			if (str2[i - 1] == str2[cn]) { // 配成功的时候
				/*
				* 首先i来到下个位置，
				* 然后，下次应该是i-1位置的数和i-1位置的信息代表的位置（用变量cn记录）的数比较
				* 那就将cn的值更新成i-1位置的信息
				* */
				next[i++] = ++cn;
			} else if (cn > 0) {
				/*只要cn大于0，还可以往前跳*/
				cn = next[cn];
			} else {
				/*cn不能往前了，那当前的信息就是0，表示没有前缀和后缀相等的情况*/
				next[i++] = 0;
			}
		}
		return next;
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
		int matchSize = 5;
		int testTimes = 5000000;
		System.out.println("test begin");
		for (int i = 0; i < testTimes; i++) {
			String str = getRandomString(possibilities, strSize);
			String match = getRandomString(possibilities, matchSize);
			if (getIndexOf(str, match) != str.indexOf(match)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test finish");
	}

}
