package class14;

import java.util.HashSet;

/**
 * str只由x和.组成，x表示墙不能放灯，.表示街道可以放灯，一个灯可以照亮自己和左右两边，问照亮所有的街道需要几盏灯
 * 思路：暴力递归可以做对数器，最优解为贪心
 * 贪心流程：
 * 从左往右尝试（最多需要考虑3个长度）
 * 来到了i位置
 * 1. 如果i位置是x，直接来到i+1位置
 * 2. 如果i位置是.，需要看下i+1：
 *  a.如果i+1位置是x，i位置必须放灯，来到i+2位置
 *  b.如果i+1位置是.，需要看下i+2
 *   1）如果i+2位置是x，i、i+1两个地方随便放，来到i+3位置
 *   2）如果i+2位置是.，就放在i+1位置，照亮了i和i+2，来到i+3位置
 * ...
 * 直到来到n-1位置
 */
public class Code01_Light {

	public static int minLight1(String road) {
		if (road == null || road.length() == 0) {
			return 0;
		}
		return process(road.toCharArray(), 0, new HashSet<>());
	}

	// str[index....]位置，自由选择放灯还是不放灯
	// str[0..index-1]位置呢？已经做完决定了，那些放了灯的位置，存在lights里
	// 要求选出能照亮所有.的方案，并且在这些有效的方案中，返回最少需要几个灯
	public static int process(char[] str, int index, HashSet<Integer> lights) {
		if (index == str.length) { // 结束的时候
			for (int i = 0; i < str.length; i++) {
				if (str[i] != 'X') { // 当前位置是点的话
					if (!lights.contains(i - 1) && !lights.contains(i) && !lights.contains(i + 1)) {
						return Integer.MAX_VALUE;
					}
				}
			}
			return lights.size();
		} else { // str还没结束
			// i X .
			int no = process(str, index + 1, lights);
			int yes = Integer.MAX_VALUE;
			if (str[index] == '.') {
				lights.add(index);
				yes = process(str, index + 1, lights);
				lights.remove(index);
			}
			return Math.min(no, yes);
		}
	}

	public static int minLight2(String road) {
		char[] str = road.toCharArray();
		int i = 0;
		int light = 0;
		while (i < str.length) {
			if (str[i] == 'X') {
				/*直接来到i+1*/
				i++;
			} else {
				/*不是x的话，后面这几点肯定要放一盏灯的*/
				light++;
				if (i + 1 == str.length) {
					/*i位置就是最后一个需要放的灯*/
					break;
				} else { // 有i位置  i+ 1   X  .
					if (str[i + 1] == 'X') {
						i = i + 2;
					} else {
						i = i + 3;
					}
				}
			}
		}
		return light;
	}

	public static int minLight3(String road) {
		if(road == null || road.length()==0){
			return 0;
		}
		return process3(road.toCharArray(),0,0,false);
	}

	/**
	 * 已经来到了i位置，前面的灯都放好了，放了lights个
	 * 把后面的都照亮的话，至少一共要放多少个
	 * @param chars
	 * @param i
	 * @param lights
	 * @param isLight
	 * @return
	 */
	private static int process3(char[] chars, int i, int lights, boolean isLight) {
		int length = chars.length;
		if (i==length){
			return lights;
		}
		if (isLight){
			return process3(chars,i+1,lights,false);
		}else if (chars[i]=='X'){
			return process3(chars,i+1,lights,false);
		}else {
			//这里不亮，又是.
			int res = Integer.MAX_VALUE;
			//放灯
			res = Math.min(res,process3(chars, i+1, lights+1, true));
			//不放
			if (i+1<length && chars[i+1]!='X'){
				res = Math.min(res,process3(chars, i+2, lights+1, true));
			}
			return res;
		}
	}


	// for test
	public static String randomString(int len) {
		char[] res = new char[(int) (Math.random() * len) + 1];
		for (int i = 0; i < res.length; i++) {
			res[i] = Math.random() < 0.5 ? 'X' : '.';
		}
		return String.valueOf(res);
	}

	public static void main(String[] args) {
		int len = 20;
		int testTime = 100000;
		for (int i = 0; i < testTime; i++) {
			String test = randomString(len);
			int ans1 = minLight1(test);
			int ans2 = minLight2(test);
			if (ans1 != ans2) {
				System.out.println("oops!");
			}
		}
		System.out.println("finish!");
	}
}
