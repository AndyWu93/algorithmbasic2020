package class11;

/**
 * 折痕可以是如下结构
 * 		凹
 * 	   / \
 * 	  凹  凸
 * 	 / \ / \
 * 	凹 凸凹 凸
 * 	所有的折痕就是这棵二叉树的中序遍历
 * 该二叉树的特点：
 * 1.头是凹
 * 2.所有的左子树的头都是凹，所有的右子树的头都是凸
 * 本题难点在于打印一颗想象出来的二叉树
 */
public class Code07_PaperFolding {

	public static void printAllFolds(int N) {
		/*从第一层开始，一共折了N层，第一个节点是凹*/
		process(1, N, true);
		System.out.println();
	}

	// 当前你来了一个节点，脑海中想象的！
	// 这个节点在第i层，一共有N层，N固定不变的
	// 这个节点如果是凹的话，down = T
	// 这个节点如果是凸的话，down = F
	// 函数的功能：中序打印以你想象的节点为头的整棵树！
	public static void process(int i, int N, boolean down) {
		if (i > N) {
			/*层数超过了，直接返回*/
			return;
		}
		/*先去打印左边的头节点为凹的树*/
		process(i + 1, N, true);
		/*再打印自己*/
		System.out.print(down ? "凹 " : "凸 ");
		/*再打印右边头节点为凸的树*/
		process(i + 1, N, false);
	}

	public static void main(String[] args) {
		int N = 4;
		printAllFolds(N);
	}
}
