package class38;

/**
 * 解题技巧之计数器找规律
 *
 * 定义一种数：可以表示成若干（数量>1）连续正数和的数
 * 比如:
 * 5 = 2+3，5就是这样的数
 * 12 = 3+4+5，12就是这样的数
 * 1不是这样的数，因为要求数量大于1个、连续正数和
 * 2 = 1 + 1，2也不是，因为等号右边不是连续正数
 * 给定一个参数N，返回是不是可以表示成若干连续正数和的数
 */
public class Code03_MSumToN {

	public static boolean isMSum1(int num) {
		/*枚举开头*/
		for (int start = 1; start <= num; start++) {
			int sum = start;
			for (int j = start + 1; j <= num; j++) {
				/*累加之后大于目标值，换个开头重来*/
				if (sum + j > num) {
					break;
				}
				if (sum + j == num) {
					/*累加之后正好等于目标值*/
					return true;
				}
				sum += j;
			}
		}
		return false;
	}

	/**
	 * 规律解题，只要num是2的某次方，返回true
	 * 如何看出一个数是否是2的某次方？
	 * 	这个数的二进制只有1个1
	 */
	public static boolean isMSum2(int num) {
//		/*提取最右侧的1，与上自己等于自己*/
//		return num == (num & (~num + 1));
//		
//		return num == (num & (-num));
//		
//		/*减一以后，唯一的1会被打散成若干个，与上自己是0*/
		return (num & (num - 1)) != 0;
	}

	public static void main(String[] args) {
		for (int num = 1; num < 200; num++) {
			System.out.println(num + " : " + isMSum1(num));
		}
		System.out.println("test begin");
		for (int num = 1; num < 5000; num++) {
			if (isMSum1(num) != isMSum2(num)) {
				System.out.println("Oops!");
			}
		}
		System.out.println("test end");

	}
}
