package class14;

import java.util.Arrays;
import java.util.Comparator;

/**
 * 给定一批会议的开始和结束时间（int）,求最多能安排多少场
 * 思路：暴力递归可以做对数器，最优解为贪心
 * 贪心方式：在所有会议中先安排结束时间最早的
 * 流程：
 * 先将arr中end从小到大排序
 * 选了第一个数组[s,e]，arr中开始时间比e早的都删了
 * 再选下一个[s',e']，arr中开始时间比e'早的都删了
 * ...
 */
public class Code03_BestArrange {

	public static class Program {
		public int start;
		public int end;

		public Program(int start, int end) {
			this.start = start;
			this.end = end;
		}
	}

	/**
	 * 对数器
	 * 贪心必须要写对数器
	 * @param programs
	 * @return
	 */
	// 暴力！所有情况都尝试！
	public static int bestArrange1(Program[] programs) {
		if (programs == null || programs.length == 0) {
			return 0;
		}
		/*从0号时间点开始，目前安排了0场*/
		return process(programs, 0, 0);
	}

	/*
	* 递归含义：
	* 还剩下的会议都放在programs里
	* done：之前已经安排了多少会议的数量
	* timeLine：目前来到的时间点是什么
	* 目前来到timeLine的时间点，已经安排了done多的会议，剩下的会议programs可以自由安排
	* 返回能安排的最多会议数量
	* */
	public static int process(Program[] programs, int done, int timeLine) {
		if (programs.length == 0) {
			/*没有剩下的会议了，就返回之前安排好的数量*/
			return done;
		}
		/*还有会议可选*/
		int max = done;
		/*枚举每一个会议都为接下来的第一个会议*/
		for (int i = 0; i < programs.length; i++) {
			if (programs[i].start >= timeLine) {
				/*当前会议可选，就把它从数组里删掉返回一个新的*/
				Program[] next = copyButExcept(programs, i);
				/*后续递归调用，与当前值比较取max*/
				max = Math.max(max, process(next, done + 1, programs[i].end));
			}
		}
		return max;
	}

	public static Program[] copyButExcept(Program[] programs, int i) {
		Program[] ans = new Program[programs.length - 1];
		int index = 0;
		for (int k = 0; k < programs.length; k++) {
			if (k != i) {
				ans[index++] = programs[k];
			}
		}
		return ans;
	}

	/**
	 * 贪心
	 * @param programs
	 * @return
	 */
	// 会议的开始时间和结束时间，都是数值，不会 < 0
	public static int bestArrange2(Program[] programs) {
		/*排个序*/
		Arrays.sort(programs, new ProgramComparator());
		/*开始时间点*/
		int timeLine = 0;
		int result = 0;
		/*依次遍历每一个会议，结束时间早的会议先遍历*/
		for (int i = 0; i < programs.length; i++) {
			if (timeLine <= programs[i].start) {
				/*如果当前可用时间早于当前会议的开始时间，可以安排，当前可用时间推至当前会议结束时间*/
				result++;
				timeLine = programs[i].end;
			}
		}
		return result;
	}

	public static class ProgramComparator implements Comparator<Program> {

		@Override
		public int compare(Program o1, Program o2) {
			return o1.end - o2.end;
		}

	}

	// for test
	public static Program[] generatePrograms(int programSize, int timeMax) {
		Program[] ans = new Program[(int) (Math.random() * (programSize + 1))];
		for (int i = 0; i < ans.length; i++) {
			int r1 = (int) (Math.random() * (timeMax + 1));
			int r2 = (int) (Math.random() * (timeMax + 1));
			if (r1 == r2) {
				ans[i] = new Program(r1, r1 + 1);
			} else {
				ans[i] = new Program(Math.min(r1, r2), Math.max(r1, r2));
			}
		}
		return ans;
	}

	public static void main(String[] args) {
		int programSize = 12;
		int timeMax = 20;
		int timeTimes = 1000000;
		for (int i = 0; i < timeTimes; i++) {
			Program[] programs = generatePrograms(programSize, timeMax);
			if (bestArrange1(programs) != bestArrange2(programs)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("finish!");
	}

}
