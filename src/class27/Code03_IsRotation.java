package class27;

/**
 * 判断是s1,s2是否互为旋转串
 * 旋转串含义：
 * 把前面0-n长度的字符拿出来，放到后面，形成s2，那s1s2就互为旋转串
 * 暴力解：从0位置开始依次往后转，再比对
 * 最优解：
 * 将s1自己拼接自己，形成一个长度为2N的str
 * s1的任何旋转串，一定是str的子串。所以直接求s2是不是str的子串就行了
 */
public class Code03_IsRotation {

	public static boolean isRotation(String a, String b) {
		if (a == null || b == null || a.length() != b.length()) {
			return false;
		}
		String b2 = b + b;
		return getIndexOf(b2, a) != -1;
	}

	// KMP Algorithm
	public static int getIndexOf(String s, String m) {
		if (s.length() < m.length()) {
			return -1;
		}
		char[] ss = s.toCharArray();
		char[] ms = m.toCharArray();
		int si = 0;
		int mi = 0;
		int[] next = getNextArray(ms);
		while (si < ss.length && mi < ms.length) {
			if (ss[si] == ms[mi]) {
				si++;
				mi++;
			} else if (next[mi] == -1) {
				si++;
			} else {
				mi = next[mi];
			}
		}
		return mi == ms.length ? si - mi : -1;
	}

	public static int[] getNextArray(char[] ms) {
		if (ms.length == 1) {
			return new int[] { -1 };
		}
		int[] next = new int[ms.length];
		next[0] = -1;
		next[1] = 0;
		int pos = 2;
		int cn = 0;
		while (pos < next.length) {
			if (ms[pos - 1] == ms[cn]) {
				next[pos++] = ++cn;
			} else if (cn > 0) {
				cn = next[cn];
			} else {
				next[pos++] = 0;
			}
		}
		return next;
	}

	public static void main(String[] args) {
		String str1 = "yunzuocheng";
		String str2 = "zuochengyun";
		System.out.println(isRotation(str1, str2));

	}

}
