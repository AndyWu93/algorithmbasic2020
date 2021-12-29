package class25;

/**
 * 本题难度爆表，但是今天的面试题算中等难度
 * 给定一个二维数组matrix，其中的值不是0就是1，返回全部由1组成的子矩形数量
 * 思路：
 * 枚举每一行作底，形成的子矩阵，求出该子矩阵中全部由1组成的子矩形数量
 * 因为每一行为底的子矩阵都彼此不一样，所以只要将这些子矩阵中全部由1组成的子矩形数量加起来，就是题解
 * 如何结算当前行全部由1组成的子矩形数量？
 * 计算当前行为底的子矩阵中的全部由1组成的子矩形的时候需要注意，全部是1的子矩阵一定都要以当前行的某一个位置为底
 * 当前行为底的子矩阵压缩成的直方图数组为arr；
 * 通过单调栈，找的任意位置i，左右最近比自己矮的高度，假设分别为x，y；
 *
 * 1.此时找到了一个区域为[x+1,y-1]高度为arr[i]的矩阵，内部全是1；
 * 结算该区域中的子矩阵数量，条件：必须以当前的任意位置为底。结算方式：
 * 	1).求出高度为arr[i]的所有子矩阵数量
 * 		求出当前区域长度为l=y-x-1
 *  	高度都一样，枚举出所有的底部[x+1,x+1]、[x+1,x+2]、[x+1,x+3]...[x+2,x+2]、[x+2,x+3]、[x+2,x+4]...
 *  	结果是1+2+3+...+l,即 l*(1+l)/2
 * 	2).求出高度为arr[i]-1的所有子矩阵数量，同上
 * 	3).求出高度为arr[i]-2的所有子矩阵数量，同上
 * 	...
 * 	直到求出高度为max(arr[x],arr[y])的所有子矩阵数量,高度求到两边比自己矮的高度中较高的那一个就可以截止了
 * 	因为各自的高度，需要自己求
 * 	所以最终的结果为 l*(1+l)/2 * (arr[i]-max(arr[x],arr[y]))
 * 2.如果单调栈中让自己被弹出的值正好与自己相等，此时可以不用结算该高度的子矩阵。高度相等的结算一次就可以了
 * 3.每一次弹出一个高度，结算的值加起来，就是该行为底时，所有由1组成的子矩形数量
 *
 * 枚举每一行为底，将结果都加起来，就是题解
 */
// 测试链接：https://leetcode.com/problems/count-submatrices-with-all-ones
public class Code05_CountSubmatricesWithAllOnes {

	public static int numSubmat(int[][] mat) {
		if (mat == null || mat.length == 0 || mat[0].length == 0) {
			return 0;
		}
		int nums = 0;
		/*每一行作底，形成一个直方图数组*/
		int[] height = new int[mat[0].length];
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				/*出现了0，直接为0*/
				height[j] = mat[i][j] == 0 ? 0 : height[j] + 1;
			}
			/*每一个直方图数组结算一下，求和*/
			nums += countFromBottom(height);
		}
		return nums;

	}

	// 比如
	//              1
	//              1
	//              1         1
	//    1         1         1
	//    1         1         1
	//    1         1         1
	//             
	//    2  ....   6   ....  9
	// 如上图，假设在6位置，1的高度为6
	// 在6位置的左边，离6位置最近、且小于高度6的位置是2，2位置的高度是3
	// 在6位置的右边，离6位置最近、且小于高度6的位置是9，9位置的高度是4
	// 此时我们求什么？
	// 1) 求在3~8范围上，必须以高度6作为高的矩形，有几个？
	// 2) 求在3~8范围上，必须以高度5作为高的矩形，有几个？
	// 也就是说，<=4的高度，一律不求
	// 那么，1) 求必须以位置6的高度6作为高的矩形，有几个？
	// 3..3  3..4  3..5  3..6  3..7  3..8
	// 4..4  4..5  4..6  4..7  4..8
	// 5..5  5..6  5..7  5..8
	// 6..6  6..7  6..8
	// 7..7  7..8
	// 8..8
	// 这么多！= 21 = (9 - 2 - 1) * (9 - 2) / 2
	// 这就是任何一个数字从栈里弹出的时候，计算矩形数量的方式
	public static int countFromBottom(int[] height) {
		if (height == null || height.length == 0) {
			return 0;
		}
		int nums = 0;
		/*这里用数组模拟栈结构*/
		int[] stack = new int[height.length];
		int si = -1;
		for (int i = 0; i < height.length; i++) {
			/*来到了i位置，如果栈不为空，且i位置的数比栈顶小或者相等，表示此时需要弹出栈顶了*/
			while (si != -1 && height[stack[si]] >= height[i]) {
				/*弹出栈顶*/
				int cur = stack[si--];
				/*被相等值的位置弹出不用结算*/
				if (height[cur] > height[i]) {
					/*左边：栈空的话就是-1，否则栈顶peek一下*/
					int left = si == -1 ? -1 : stack[si];
					/*此时需要结算的l长度*/
					int n = i - left - 1;
					/*需要结算的高度：两边矮的数中较大的那个，如果左边位置是-1，高度就算作0*/
					int down = Math.max(left == -1 ? 0 : height[left], height[i]);
					/*需要算的高度数量：height[cur] - down；乘以每个高度的子矩阵数*/
					nums += (height[cur] - down) * num(n);
				}

			}
			/*否则直接压栈*/
			stack[++si] = i;
		}
		/*遍历结束了，栈中还有数，依次弹出并结算，此时右边是数组的末尾*/
		while (si != -1) {
			int cur = stack[si--];
			int left = si == -1  ? -1 : stack[si];
			int n = height.length - left - 1;
			int down = left == -1 ? 0 : height[left];
			nums += (height[cur] - down) * num(n);
		}
		return nums;
	}

	public static int num(int n) {
		/*当前区域长度为n，有多少个子矩阵*/
		return ((n * (1 + n)) >> 1);
	}

}
