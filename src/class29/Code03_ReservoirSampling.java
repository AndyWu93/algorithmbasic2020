package class29;

/**
 * 蓄水池算法（对一个size不断增加的集合挑选k个元素，保证每个元素被选中的概率均等）
 *
 * 假设有一个源源吐出不同球的机器，
 * 只有装下10个球的袋子，每一个吐出的球，要么放入袋子，要么永远扔掉
 * 如何做到机器吐出每一个球之后，所有吐出的球都等概率被放进袋子里
 * 流程：
 * 1-10号球，全部进袋子。
 * 后面假设吐出了i号球，
 * 定义一个函数，boolean f(x),即x中选中1-10的概率。
 * 调用f(i),
 * 返回true，将袋子中的球，等概率扔掉一个，位置留给i
 * 返回false，i直接扔掉
 *
 * 证明
 * 1）拿3号球举例，
 * 	1-10号球来了，3号球存货概率1
 * 	11号球来了，11号球用f函数被选中的概率10/11,3号球的位置被选中的概率1/10,所以，本轮3号球死亡概率为 10/11 * 1/10 = 1/11,存活概率为10/11;3号球累积存活概率：1 * 10/11 = 10/11
 * 	12号球来了，12号球用f函数被选中的概率10/12,3号球的位置被选中的概率1/10,所以，本轮3号球死亡概率为 10/12 * 1/10 = 1/12,存活概率为11/12;3号球累积存活概率：1 * 10/11 * 11/12 = 10/12
 * 	13号球来了，13号球用f函数被选中的概率10/13,3号球的位置被选中的概率1/10,所以，本轮3号球死亡概率为 10/13 * 1/10 = 1/13,存活概率为12/13;3号球累积存活概率：1 * 10/11 * 11/12 * 12/13 = 10/13
 * 	...
 * 	1729号球来了，3号球累积存活概率：1 * 10/11 * 11/12 * 12/13 * ... * 1728/1729 = 10/1729
 * 2）同理拿17号球举例，
 * 	17号球来了，17号球活下来的概率10/17
 * 	18号球来了，17号球累积存活概率：10/17 * 17/18 = 10/18
 * 	19号球来了，17号球累积存活概率：10/17 * 17/18 * 18/19 = 10/19
 * 	1729号球来了，17号球累积存活概率：10/17 * 17/18 * 18/19 * ... * 1728/1729 = 10/1729
 *
 * 算法用途举例：
 * 全球用户某一段时间100名抽奖活动
 * 只需要记录这段时间，每个用户抽奖的编号，该编号是递增的
 *
 *
 */
public class Code03_ReservoirSampling {

	public static class RandomBox {
		private int[] bag;
		private int N;
		private int count;

		public RandomBox(int capacity) {
			bag = new int[capacity];
			N = capacity;
			count = 0;
		}

		private int rand(int max) {
			return (int) (Math.random() * max) + 1;
		}

		public void add(int num) {
			count++;
			if (count <= N) {
				bag[count - 1] = num;
			} else {
				if (rand(count) <= N) {
					bag[rand(N) - 1] = num;
				}
			}
		}

		public int[] choices() {
			int[] ans = new int[N];
			for (int i = 0; i < N; i++) {
				ans[i] = bag[i];
			}
			return ans;
		}

	}

	// 请等概率返回1~i中的一个数字
	public static int random(int i) {
		return (int) (Math.random() * i) + 1;
	}

	public static void main(String[] args) {
		/***********蓄水池算法举例start***************/
		System.out.println("hello");
		int test = 10000;
		int ballNum = 17;
		int[] count = new int[ballNum + 1];//统计test次测试后，每个球被选中的次数
		for (int i = 0; i < test; i++) {//测试多少次
			int[] bag = new int[10];
			int bagi = 0;
			for (int num = 1; num <= ballNum; num++) {
				if (num <= 10) {
					/*前10个全放进去*/
					bag[bagi++] = num;
				} else { // num > 10
					if (random(num) <= 10) { // 一定要把num球入袋子
						/*后面的如果中了，就在bag中随机一个位置放进去*/
						bagi = (int) (Math.random() * 10);
						bag[bagi] = num;
					}
				}

			}
			for (int num : bag) {
				count[num]++;
			}
		}
		for (int i = 0; i <= ballNum; i++) {
			System.out.println(count[i]);
		}
		/***********蓄水池算法举例end***************/


		System.out.println("hello");
		int all = 100;
		int choose = 10;
		int testTimes = 50000;
		int[] counts = new int[all + 1];
		for (int i = 0; i < testTimes; i++) {
			RandomBox box = new RandomBox(choose);
			for (int num = 1; num <= all; num++) {
				box.add(num);
			}
			int[] ans = box.choices();
			for (int j = 0; j < ans.length; j++) {
				counts[ans[j]]++;
			}
		}

		for (int i = 0; i < counts.length; i++) {
			System.out.println(i + " times : " + counts[i]);
		}

	}
}
