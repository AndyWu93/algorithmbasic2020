package class11;

import java.util.ArrayList;
import java.util.List;

/**
 * 多叉树以二叉树的形式encode和decode
 * 思路：
 * 将多叉树的孩子们，转为二叉树左树的右边界
 *    a
 *   /|\
 *  b c d
 *   转为
 *    a
 *   / \
 *  b  null
 *  \
 *   c
 *    \
 *     d
 * 所以一个二叉树的一个节点里，左树右边界挂着一条自己的孩子们，右树一路都是自己的兄弟们
 */
// 本题测试链接：https://leetcode.com/problems/encode-n-ary-tree-to-binary-tree
public class Code03_EncodeNaryTreeToBinaryTree {

	// 提交时不要提交这个类
	public static class Node {
		public int val;
		public List<Node> children;

		public Node() {
		}

		public Node(int _val) {
			val = _val;
		}

		public Node(int _val, List<Node> _children) {
			val = _val;
			children = _children;
		}
	};

	// 提交时不要提交这个类
	public static class TreeNode {
		int val;
		TreeNode left;
		TreeNode right;

		TreeNode(int x) {
			val = x;
		}
	}

	// 只提交这个类即可
	class Codec {
		// Encodes an n-ary tree to a binary tree.
		public TreeNode encode(Node root) {
			if (root == null) {
				return null;
			}
			/*多叉树的头就是二叉树的头*/
			TreeNode head = new TreeNode(root.val);
			/*多叉树的孩子组成右边界，给头节点的左孩子*/
			head.left = en(root.children);
			return head;
		}

		/*
		* 深度优先
		* a有b、c、d三个孩子
		* 处理到b时，先处理b的孩子，假设为e、f、g
		* 再回去处理c
		* */
		private TreeNode en(List<Node> children) {
			TreeNode head = null;
			TreeNode cur = null;
			for (Node child : children) {
				/*1. 拿出所有的孩子*/
				TreeNode tNode = new TreeNode(child.val);
				if (head == null) {
					/*2. 第一个孩子给了head*/
					head = tNode;
				} else {
					/*3. 其他兄弟们一路向右（右边挂着兄弟）*/
					cur.right = tNode;
				}
				/*来到了当前的孩子*/
				cur = tNode;
				/*当前孩子的children，挂在左边。深度优先遍历（左边挂着孩子）*/
				cur.left = en(child.children);
			}
			return head;
		}

		// Decodes your binary tree to an n-ary tree.
		public Node decode(TreeNode root) {
			if (root == null) {
				return null;
			}
			/*root.left代表了一颗数*/
			return new Node(root.val, de(root.left));
		}

		/*
		* 深度优先
		* root左边挂着自己的孩子，右边挂着兄弟们
		* */
		public List<Node> de(TreeNode root) {
			List<Node> children = new ArrayList<>();
			while (root != null) {
				/*root自己建立好节点，root自己的孩子记在左边*/
				Node cur = new Node(root.val, de(root.left));
				/*把root右边的所有兄弟们都加入到children里去*/
				children.add(cur);
				root = root.right;
			}
			return children;
		}

	}

}
