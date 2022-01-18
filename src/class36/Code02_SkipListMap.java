package class36;

import java.util.ArrayList;

/**
 * 有序表之跳表（新世纪的有序表，常数项比BST高，用途没有BST类的有序表广）
 *
 * 了解积压结构：即容量在某个时刻遇到瓶颈了，会自动扩容，比如ArrayList、HashMap
 * 这些结构，扩容logN次，扩容代价是等比数列，总代价收敛到O(N)，所以当个代价O(1)
 *
 * 在有序表中
 * 积压结构：SBtree，红黑树
 * 非积压结构：AVL
 * 积压接结构减少了数据的调整，如果数据存在物理内存中，积压结构减少了IO
 *
 * 跳表的设计：
 * 核心概念：
 * 	1.头节点是一个值没有意义的节点
 * 	2.除了头节点，每个节点都有i层链表，i的值由0.5的概率决定
 * 		如何0.5？
 * 		i初始值=1，出一个[0,1)的随机数，如果大于等于0.5，i++，直到出的随机数小于0.5，i的值确定
 *  3.头节点i遇到了一个节点的i比它大，头节点的i自动升级到一样大
 *
 * 跳表添加数据流程：
 * 	添加一个值v，通过随机机制得到i
 * 	new出新节点node，node.value=v,node有i层链表，每层链表指向null
 * 	从root的第i层链表开始，找到value刚开始大于v的节点a，将node的第i层指向a节点，root的第i层指向node
 * 	从a的第i-1层链表开始，找到value刚开始大于v的节点b，前一个及节点b'，将node的第i-1层指向b节点，b'的第i-1层指向node
 * 	从b'的第i-2层链表开始，找到value刚开始大于v的节点c，前一个及节点c'，将node的第i-2层指向c节点，c'的第i-2层指向node
 * 	...
 * 	直到第1层链表
 *
 * 添加流程复杂度分析：
 * 	在所有的结节中，i每加一层，数量减半（因为0.5的概率），
 * 	从高层开始找起，找到比它大的位置的前一个节点，跳至下一层，再找，再往下跳
 * 	所以从最高层i到第1层，logN的代价
 *
 * 跳表结构举例
 * v: r		3	9	18	29	39	65
 * 	  6-------------------------6
 * 	  5-------------5-----------5
 * 	  4-------------4---4-------4
 * 	  3-----3-------3---3---3---3
 * 	  2-----2-------2---2---2---2
 * 	  1-----1---1---1---1---1---1
 *
 */
public class Code02_SkipListMap {

	// 跳表的节点定义
	public static class SkipListNode<K extends Comparable<K>, V> {
		public K key;
		public V val;
		/*用list来表示这个节点指向的下一个节点，节点数就是该结点的层数*/
		public ArrayList<SkipListNode<K, V>> nextNodes;

		public SkipListNode(K k, V v) {
			key = k;
			val = v;
			nextNodes = new ArrayList<SkipListNode<K, V>>();
		}

		// 遍历的时候，如果是往右遍历到的null(next == null), 遍历结束
		// 头(null), 头节点的null，认为最小
		// node  -> 头，node(null, "")  node.isKeyLess(!null)  true
		// node里面的key是否比otherKey小，true，不是false
		public boolean isKeyLess(K otherKey) {
			//  otherKey == null -> false 
			return otherKey != null && (key == null || key.compareTo(otherKey) < 0);
		}

		public boolean isKeyEqual(K otherKey) {
			return (key == null && otherKey == null)
					|| (key != null && otherKey != null && key.compareTo(otherKey) == 0);
		}

	}

	public static class SkipListMap<K extends Comparable<K>, V> {
		/*层数随机机制*/
		private static final double PROBABILITY = 0.5; // < 0.5 继续做，>=0.5 停
		/*头节点，一般没有值*/
		private SkipListNode<K, V> head;
		/*跳表size*/
		private int size;
		/*跳表的最大层数*/
		private int maxLevel;

		public SkipListMap() {
			head = new SkipListNode<K, V>(null, null);
			head.nextNodes.add(null); // 0
			size = 0;
			/*跳表的层从0开水算*/
			maxLevel = 0;
		}

		/*
		* 从最高层开始，一路找下去,
		* 最终，找到第0层的<key的最右的节点
		* */
		private SkipListNode<K, V> mostRightLessNodeInTree(K key) {
			if (key == null) {
				return null;
			}
			int level = maxLevel;
			SkipListNode<K, V> cur = head;
			while (level >= 0) { // 从上层跳下层
				//  cur  level  -> level-1
				/*
				* 找到level层小于key的最右节点
				* 找到以后，cur接住，level--，
				* 再次进入while
				* */
				cur = mostRightLessNodeInLevel(key, cur, level--);
			}
			return cur;
		}

		// 在level层里，如何往右移动
		// 现在来到的节点是cur，来到了cur的level层，在level层上，找到<key最后一个节点并返回
		private SkipListNode<K, V> mostRightLessNodeInLevel(K key, 
				SkipListNode<K, V> cur, 
				int level) {
			/*第level层，一直往下找，直到next不比key小了，返回cur*/
			SkipListNode<K, V> next = cur.nextNodes.get(level);
			while (next != null && next.isKeyLess(key)) {
				cur = next;
				next = cur.nextNodes.get(level);
			}
			return cur;
		}

		public boolean containsKey(K key) {
			if (key == null) {
				return false;
			}
			SkipListNode<K, V> less = mostRightLessNodeInTree(key);
			SkipListNode<K, V> next = less.nextNodes.get(0);
			return next != null && next.isKeyEqual(key);
		}

		/**
		 * 核心方法之添加值
		 */
		// 新增、改value
		public void put(K key, V value) {
			if (key == null) {
				return;
			}
			// 0层上，最右一个，< key 的Node -> >key
			SkipListNode<K, V> less = mostRightLessNodeInTree(key);
			/*less是第0层小于key的最右节点，find是less在0层的下一个*/
			SkipListNode<K, V> find = less.nextNodes.get(0);
			if (find != null && find.isKeyEqual(key)) {
				/*key之前加入过，就是find节点*/
				find.val = value;
			} else { // find == null   8   7   9
				/*find不是key，或者find是null，说明需要新增节点key*/
				size++;
				int newNodeLevel = 0;
				while (Math.random() < PROBABILITY) {
					/*level从0开始，随机决定高度*/
					newNodeLevel++;
				}
				// newNodeLevel
				while (newNodeLevel > maxLevel) {
					/*如果level比maxLevel还高，将头节点的level数加到此时的level*/
					head.nextNodes.add(null);
					maxLevel++;
				}
				/*new出新节点，并设定好层*/
				SkipListNode<K, V> newNode = new SkipListNode<K, V>(key, value);
				for (int i = 0; i <= newNodeLevel; i++) {
					newNode.nextNodes.add(null);
				}
				/*下面就是将newNode插入表中*/
				int level = maxLevel;
				SkipListNode<K, V> pre = head;
				/*从最高层开始*/
				while (level >= 0) {
					/*level 层中，找到最右的 < key 的节点*/
					pre = mostRightLessNodeInLevel(key, pre, level);
					/*
					* level大于新节点level时，只找，不挂节点
					* 必须等level到了新节点的最高层时，再把新节点塞进去
					* */
					if (level <= newNodeLevel) {
						/*新节点的level层，指向了pre下一个节点*/
						newNode.nextNodes.set(level, pre.nextNodes.get(level));
						/*pre的level层指向了新节点*/
						pre.nextNodes.set(level, newNode);
					}
					level--;
				}
			}
		}

		public V get(K key) {
			if (key == null) {
				return null;
			}
			SkipListNode<K, V> less = mostRightLessNodeInTree(key);
			SkipListNode<K, V> next = less.nextNodes.get(0);
			return next != null && next.isKeyEqual(key) ? next.val : null;
		}

		/**
		 * remove涉及最高层是否有值了，没有值需要消减层数的情况
		 */
		public void remove(K key) {
			if (containsKey(key)) {
				size--;
				int level = maxLevel;
				SkipListNode<K, V> pre = head;
				while (level >= 0) {
					/*先找到本层中小于key的最右节点*/
					pre = mostRightLessNodeInLevel(key, pre, level);
					SkipListNode<K, V> next = pre.nextNodes.get(level);
					// 1）在这一层中，pre下一个就是key
					// 2）在这一层中，pre的下一个key是>要删除key
					if (next != null && next.isKeyEqual(key)) {
						// free delete node memory -> C++
						// level : pre -> next(key) -> ...
						/*找到了key节点，将节点踢出去，怎么踢，它前面的节点直接连他后面的节点*/
						pre.nextNodes.set(level, next.nextNodes.get(level));
					}
					// 在level层只有一个节点了，就是默认节点head
					/*这里是消减头节点层数，当pre就是头节点，且level层中头节点指向空了，头节点消掉该层*/
					if (level != 0 && pre == head && pre.nextNodes.get(level) == null) {
						head.nextNodes.remove(level);
						maxLevel--;
					}
					level--;
				}
			}
		}

		public K firstKey() {
			return head.nextNodes.get(0) != null ? head.nextNodes.get(0).key : null;
		}

		public K lastKey() {
			int level = maxLevel;
			SkipListNode<K, V> cur = head;
			while (level >= 0) {
				SkipListNode<K, V> next = cur.nextNodes.get(level);
				while (next != null) {
					cur = next;
					next = cur.nextNodes.get(level);
				}
				level--;
			}
			return cur.key;
		}

		public K ceilingKey(K key) {
			if (key == null) {
				return null;
			}
			SkipListNode<K, V> less = mostRightLessNodeInTree(key);
			SkipListNode<K, V> next = less.nextNodes.get(0);
			return next != null ? next.key : null;
		}

		public K floorKey(K key) {
			if (key == null) {
				return null;
			}
			SkipListNode<K, V> less = mostRightLessNodeInTree(key);
			SkipListNode<K, V> next = less.nextNodes.get(0);
			return next != null && next.isKeyEqual(key) ? next.key : less.key;
		}

		public int size() {
			return size;
		}

	}

	// for test
	public static void printAll(SkipListMap<String, String> obj) {
		for (int i = obj.maxLevel; i >= 0; i--) {
			System.out.print("Level " + i + " : ");
			SkipListNode<String, String> cur = obj.head;
			while (cur.nextNodes.get(i) != null) {
				SkipListNode<String, String> next = cur.nextNodes.get(i);
				System.out.print("(" + next.key + " , " + next.val + ") ");
				cur = next;
			}
			System.out.println();
		}
	}

	public static void main(String[] args) {
		SkipListMap<String, String> test = new SkipListMap<>();
		printAll(test);
		System.out.println("======================");
		test.put("A", "10");
		printAll(test);
		System.out.println("======================");
		test.remove("A");
		printAll(test);
		System.out.println("======================");
		test.put("E", "E");
		test.put("B", "B");
		test.put("A", "A");
		test.put("F", "F");
		test.put("C", "C");
		test.put("D", "D");
		printAll(test);
		System.out.println("======================");
		System.out.println(test.containsKey("B"));
		System.out.println(test.containsKey("Z"));
		System.out.println(test.firstKey());
		System.out.println(test.lastKey());
		System.out.println(test.floorKey("D"));
		System.out.println(test.ceilingKey("D"));
		System.out.println("======================");
		test.remove("D");
		printAll(test);
		System.out.println("======================");
		System.out.println(test.floorKey("D"));
		System.out.println(test.ceilingKey("D"));
		

	}

}
