package class32;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * 建立敏感词库str[],对于文本text，查询text有多少个存在于str[]中的敏感词，并返回个数
 * 解决方案：AC自动机
 * 设计思路：在前缀树的节点中添加fail指针。fail指针的设计思路来源于KMP
 * fail指针逻辑：
 * 1）头节点的fail=null
 * 2）头节点的直接孩子的fail指向头节点
 * 3）对于其他任意节点x
 * 	寻找x的父节点y，假设y节点到x节点的路是1。
 * 	查看y节点的fail，假设其指向z节点，查看z节点有没有指向1的路
 * 		a. 有，通过1方向指向了k节点，那x的fail指向k
 * 		b. 没有，就继续查看z的fail节点，直到找到了某个节点的fail节点，有1方向的路，x的fail直接指过去
 * 		c. 如果最终fail指向null了都没有发现符合条件的节点，那x的fail指向头节点
 * fail指针的实质：
 * 	标记出其他所有的字符串中，哪个字符串的前缀和当前字符串的后缀相同，且匹配的长度最长
 * 提问：为什么fail需要指向匹配长度最长的str？
 * 	因为当该字符串匹配失败后，跳向下一个str，需要确保此时跳向的str拥有最长的已匹配前缀，
 * 	如果该str依旧匹配失败，再跳向str拥有最长的已匹配前缀
 * 	以此来保证，不会错过任何一个可能匹配成功的str
 * 提问：为什么头节点直接孩子的fail指向头？
 *  如果第二个字符匹配失败了，下个字符串匹配只能从头开始
 *
 */
public class Code04_AC2 {

	// 前缀树的节点
	public static class Node {
		// 如果一个node，end为空，不是结尾
		// 如果end不为空，表示这个点是某个字符串的结尾，end的值就是这个字符串
		public String end;
		// 只有在上面的end变量不为空的时候，endUse才有意义
		// 表示，这个字符串之前有没有加入过答案
		public boolean endUse;
		public Node fail;
		public Node[] nexts;

		public Node() {
			endUse = false;
			end = null;
			fail = null;
			nexts = new Node[26];
		}
	}

	public static class ACAutomation {
		/*头节点，前缀树代码*/
		private Node root;

		public ACAutomation() {
			root = new Node();
		}

		/*前缀树建树*/
		public void insert(String s) {
			char[] str = s.toCharArray();
			Node cur = root;
			int index = 0;
			for (int i = 0; i < str.length; i++) {
				index = str[i] - 'a';
				if (cur.nexts[index] == null) {
					cur.nexts[index] = new Node();
				}
				cur = cur.nexts[index];
			}
			cur.end = s;
		}

		/*设置fail指针*/
		public void build() {
			/*用于宽度优先遍历，通过宽度优先来设置*/
			Queue<Node> queue = new LinkedList<>();
			queue.add(root);
			Node cur = null;
			Node cfail = null;
			while (!queue.isEmpty()) {
				/*
				* 此时弹出了某个节点，此时来设置它孩子的fail指针
				* 因为前缀树不能找到父亲，只能找到孩子
				* */
				cur = queue.poll();
				for (int i = 0; i < 26; i++) { // 所有的路
					// cur -> 父亲  i号儿子，必须把i号儿子的fail指针设置好！
					if (cur.nexts[i] != null) {
						/*如果真的有i号儿子，先把儿子的fail指向root，因为如果最终没有地方指了一定会指向root*/
						cur.nexts[i].fail = root;
						/*拿到自己的fail，方便设置儿子的fail*/
						cfail = cur.fail;
						while (cfail != null) {
							if (cfail.nexts[i] != null) {
								/*
								* 自己的fail不为空，且自己fail有i方向的路，那儿子的fail就是自己的fail在i方向的路上的节点
								* 这里，最长前缀是由自己的fail指向的是最长的，所以儿子天然最长
								* */
								cur.nexts[i].fail = cfail.nexts[i];
								/*找到了就结束*/
								break;
							}
							/*没找到？自己的fail再跳一下，看下个fail*/
							cfail = cfail.fail;
						}
						/*继续遍历*/
						queue.add(cur.nexts[i]);
					}
				}
			}
		}

		/*content查询敏感词*/
		public List<String> containWords(String content) {
			char[] str = content.toCharArray();
			Node cur = root;
			Node follow = null;
			int index = 0;
			List<String> ans = new ArrayList<>();
			/*
			* 遍历content
			* 整体逻辑概述，维护一个变量cur，随着content的遍历，
			* cur也尽可能的往下走，只要有路就走。
			* 且每走一步都会看一眼此时匹配的前缀中，有没有已经成串的，收集一下并标记（下次收集的时候看到标记直接跳过）
			* */
			for (int i = 0; i < str.length; i++) {
				index = str[i] - 'a'; // 路
				/*
				* 当前cur所在str已经没有路了，而且cur又不是头节点，那就随着fail方向看能不能继续走下去
				* 跳尽了所有的fail，都没找到index反向的路，此时cur来到root节点，那就不要跳了
				* */
				while (cur.nexts[index] == null && cur != root) {
					cur = cur.fail;
				}
				// 1) 现在来到的路径，是可以继续匹配的
				// 2) 现在来到的节点，就是前缀树的根节点
				/*是否有index方向的路？有的话就过去，没有就直接回到root*/
				cur = cur.nexts[index] != null ? cur.nexts[index] : root;
				/*此时，停下来，沿着cur的fail逛一圈，看有没有已经成串的*/
				follow = cur;
				/*一路fail直到root节点*/
				while (follow != root) {
					/*之前逛过，直接停，可能是中间经历了一次回到root，接下来Content中又出现了同样的敏感词*/
					if (follow.endUse) {
						break;
					}
					// 不同的需求，在这一段之间修改
					if (follow.end != null) {
						/*成串的，收集一下，再打标*/
						ans.add(follow.end);
						follow.endUse = true;
					}
					// 不同的需求，在这一段之间修改
					follow = follow.fail;
				}
			}
			return ans;
		}

	}

	public static void main(String[] args) {
		ACAutomation ac = new ACAutomation();
		ac.insert("dhe");
		ac.insert("he");
		ac.insert("abcdheks");
		// 设置fail指针
		ac.build();

		List<String> contains = ac.containWords("abcdhekskdjfafhasldkflskdjhwqaeruv");
		for (String word : contains) {
			System.out.println(word);
		}
	}

}
