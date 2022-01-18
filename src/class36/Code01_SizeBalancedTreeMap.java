package class36;

/**
 * 有序表之sizeBalancedTree（有序表中最推荐的一种结构）
 *
 * 平衡性定义：
 * 	任何节点为头的树的节点总数，一定不少于他侄子节点为头的树的节点总数
 * sb树的平衡性分析：
 * 	        父
 * 	       /  \
 * 	      A   B
 * 	     / \ / \
 *		E  F G H
 *     /\ /\/\/\
 *    ..........
 *  按照sb树平衡性的定义，
 *  A.size>=G.size;A.size>=H.size,
 *  所以B.size<=2*A.size
 *  所以，SB树的左右树高度差不会超过2倍，平衡性虽然不及AVL,但整体高度仍然维持在logN水平
 *
 * SB树的违规类型
 * LL：左树的左树节点数大于右树
 * 	解决方案：当前节点右旋，右旋之后，当前节点以及新的头节点的size发生了变化，需要检查是否违规（递归调用）
 * LR：左树的右树节点数大于右树
 * RL：右树的左树节点数大于左树
 * RR：右树的右树节点数大于左树
 * 以上违规的解决方案整体和AVL树一样，不同点在于需要针对size发生变化的节点递归调整
 * why递归调整？
 * 	因为SB本身调整不是很频繁，但发生调整后，涉及size变化的节点可能也需要调整；不像AVL树，调整非常频繁，不会出现调整以后还有不平的情况
 *
 * SB的调整模式：
 * 	加入一个节点x
 * 		沿途所有size受到影响的节点都需要调整
 *	删除一个节点x
 *		本来删除是需要调整的，但是加入节点时会出现递归调整，所以删除节点时就不调了
 *
 *
 */
public class Code01_SizeBalancedTreeMap {

	public static class SBTNode<K extends Comparable<K>, V> {
		public K key;
		public V value;
		public SBTNode<K, V> l;
		public SBTNode<K, V> r;
		/*这里是size，AVL是高度*/
		public int size; // 不同的key的数量

		public SBTNode(K key, V value) {
			this.key = key;
			this.value = value;
			size = 1;
		}
	}

	public static class SizeBalancedTreeMap<K extends Comparable<K>, V> {
		private SBTNode<K, V> root;

		/**
		 * 左旋和右旋和AVL都差不多，只是这里交换和重新计算的是size，不是高度
		 */
		private SBTNode<K, V> rightRotate(SBTNode<K, V> cur) {
			SBTNode<K, V> leftNode = cur.l;
			cur.l = leftNode.r;
			leftNode.r = cur;
			leftNode.size = cur.size;
			cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
			return leftNode;
		}

		private SBTNode<K, V> leftRotate(SBTNode<K, V> cur) {
			SBTNode<K, V> rightNode = cur.r;
			cur.r = rightNode.l;
			rightNode.l = cur;
			rightNode.size = cur.size;
			cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
			return rightNode;
		}

		/**
		 * 平衡调整的补丁
		 */
		private SBTNode<K, V> maintain(SBTNode<K, V> cur) {
			if (cur == null) {
				return null;
			}
			/*把叔侄的节点数都拿出来*/
			int leftSize = cur.l != null ? cur.l.size : 0;
			int leftLeftSize = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
			int leftRightSize = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
			int rightSize = cur.r != null ? cur.r.size : 0;
			int rightLeftSize = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
			int rightRightSize = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
			if (leftLeftSize > rightSize) {
				/*LL型，一次右旋，2次递归，调整完后可能换头，重新接住头*/
				cur = rightRotate(cur);
				cur.r = maintain(cur.r);
				cur = maintain(cur);
			} else if (leftRightSize > rightSize) {
				/*LR型，一次左旋，3次递归，调整完后可能换头，重新接住头*/
				cur.l = leftRotate(cur.l);
				cur = rightRotate(cur);
				cur.l = maintain(cur.l);
				cur.r = maintain(cur.r);
				cur = maintain(cur);
			} else if (rightRightSize > leftSize) {
				/*RR*/
				cur = leftRotate(cur);
				cur.l = maintain(cur.l);
				cur = maintain(cur);
			} else if (rightLeftSize > leftSize) {
				/*RL*/
				cur.r = rightRotate(cur.r);
				cur = leftRotate(cur);
				cur.l = maintain(cur.l);
				cur.r = maintain(cur.r);
				cur = maintain(cur);
			}
			return cur;
		}

		private SBTNode<K, V> findLastIndex(K key) {
			SBTNode<K, V> pre = root;
			SBTNode<K, V> cur = root;
			while (cur != null) {
				pre = cur;
				if (key.compareTo(cur.key) == 0) {
					break;
				} else if (key.compareTo(cur.key) < 0) {
					cur = cur.l;
				} else {
					cur = cur.r;
				}
			}
			return pre;
		}

		private SBTNode<K, V> findLastNoSmallIndex(K key) {
			SBTNode<K, V> ans = null;
			SBTNode<K, V> cur = root;
			while (cur != null) {
				if (key.compareTo(cur.key) == 0) {
					ans = cur;
					break;
				} else if (key.compareTo(cur.key) < 0) {
					ans = cur;
					cur = cur.l;
				} else {
					cur = cur.r;
				}
			}
			return ans;
		}

		private SBTNode<K, V> findLastNoBigIndex(K key) {
			SBTNode<K, V> ans = null;
			SBTNode<K, V> cur = root;
			while (cur != null) {
				if (key.compareTo(cur.key) == 0) {
					ans = cur;
					break;
				} else if (key.compareTo(cur.key) < 0) {
					cur = cur.l;
				} else {
					ans = cur;
					cur = cur.r;
				}
			}
			return ans;
		}

		// 现在，以cur为头的树上，新增，加(key, value)这样的记录
		// 加完之后，会对cur做检查，该调整调整
		// 返回，调整完之后，整棵树的新头部
		private SBTNode<K, V> add(SBTNode<K, V> cur, K key, V value) {
			if (cur == null) {
				return new SBTNode<K, V>(key, value);
			} else {
				/*在cur为头的节点上加节点，size++*/
				cur.size++;
				if (key.compareTo(cur.key) < 0) {
					cur.l = add(cur.l, key, value);
				} else {
					cur.r = add(cur.r, key, value);
				}
				/*左右加完节点后，本层的cur，平衡一下*/
				return maintain(cur);
			}
		}

		// 在cur这棵树上，删掉key所代表的节点
		// 返回cur这棵树的新头部
		private SBTNode<K, V> delete(SBTNode<K, V> cur, K key) {
			/*cur为头的数size--*/
			cur.size--;
			if (key.compareTo(cur.key) > 0) {
				cur.r = delete(cur.r, key);
			} else if (key.compareTo(cur.key) < 0) {
				cur.l = delete(cur.l, key);
			} else { // 当前要删掉cur
				if (cur.l == null && cur.r == null) {
					// free cur memory -> C++
					cur = null;
				} else if (cur.l == null && cur.r != null) {
					// free cur memory -> C++
					cur = cur.r;
				} else if (cur.l != null && cur.r == null) {
					// free cur memory -> C++
					cur = cur.l;
				} else { // 有左有右
					/*找到cur的后继节点，来替cur*/
					SBTNode<K, V> pre = null;
					SBTNode<K, V> des = cur.r;
					des.size--;
					while (des.l != null) {
						pre = des;
						des = des.l;
						des.size--;
					}
					if (pre != null) {
						pre.l = des.r;
						des.r = cur.r;
					}
					des.l = cur.l;
					des.size = des.l.size + (des.r == null ? 0 : des.r.size) + 1;
					// free cur memory -> C++
					cur = des;
				}
			}
			/*删除的时候，可调可不调，add方法会迅速调平，不影响复杂度*/
			// cur = maintain(cur);
			return cur;
		}

		private SBTNode<K, V> getIndex(SBTNode<K, V> cur, int kth) {
			if (kth == (cur.l != null ? cur.l.size : 0) + 1) {
				return cur;
			} else if (kth <= (cur.l != null ? cur.l.size : 0)) {
				return getIndex(cur.l, kth);
			} else {
				return getIndex(cur.r, kth - (cur.l != null ? cur.l.size : 0) - 1);
			}
		}

		public int size() {
			return root == null ? 0 : root.size;
		}

		public boolean containsKey(K key) {
			if (key == null) {
				throw new RuntimeException("invalid parameter.");
			}
			SBTNode<K, V> lastNode = findLastIndex(key);
			return lastNode != null && key.compareTo(lastNode.key) == 0 ? true : false;
		}

		// （key，value） put -> 有序表 新增、改value
		public void put(K key, V value) {
			if (key == null) {
				throw new RuntimeException("invalid parameter.");
			}
			SBTNode<K, V> lastNode = findLastIndex(key);
			if (lastNode != null && key.compareTo(lastNode.key) == 0) {
				lastNode.value = value;
			} else {
				root = add(root, key, value);
			}
		}

		public void remove(K key) {
			if (key == null) {
				throw new RuntimeException("invalid parameter.");
			}
			if (containsKey(key)) {
				root = delete(root, key);
			}
		}

		public K getIndexKey(int index) {
			if (index < 0 || index >= this.size()) {
				throw new RuntimeException("invalid parameter.");
			}
			return getIndex(root, index + 1).key;
		}

		public V getIndexValue(int index) {
			if (index < 0 || index >= this.size()) {
				throw new RuntimeException("invalid parameter.");
			}
			return getIndex(root, index + 1).value;
		}

		public V get(K key) {
			if (key == null) {
				throw new RuntimeException("invalid parameter.");
			}
			SBTNode<K, V> lastNode = findLastIndex(key);
			if (lastNode != null && key.compareTo(lastNode.key) == 0) {
				return lastNode.value;
			} else {
				return null;
			}
		}

		public K firstKey() {
			if (root == null) {
				return null;
			}
			SBTNode<K, V> cur = root;
			while (cur.l != null) {
				cur = cur.l;
			}
			return cur.key;
		}

		public K lastKey() {
			if (root == null) {
				return null;
			}
			SBTNode<K, V> cur = root;
			while (cur.r != null) {
				cur = cur.r;
			}
			return cur.key;
		}

		public K floorKey(K key) {
			if (key == null) {
				throw new RuntimeException("invalid parameter.");
			}
			SBTNode<K, V> lastNoBigNode = findLastNoBigIndex(key);
			return lastNoBigNode == null ? null : lastNoBigNode.key;
		}

		public K ceilingKey(K key) {
			if (key == null) {
				throw new RuntimeException("invalid parameter.");
			}
			SBTNode<K, V> lastNoSmallNode = findLastNoSmallIndex(key);
			return lastNoSmallNode == null ? null : lastNoSmallNode.key;
		}

	}

	// for test
	public static void printAll(SBTNode<String, Integer> head) {
		System.out.println("Binary Tree:");
		printInOrder(head, 0, "H", 17);
		System.out.println();
	}

	// for test
	public static void printInOrder(SBTNode<String, Integer> head, int height, String to, int len) {
		if (head == null) {
			return;
		}
		printInOrder(head.r, height + 1, "v", len);
		String val = to + "(" + head.key + "," + head.value + ")" + to;
		int lenM = val.length();
		int lenL = (len - lenM) / 2;
		int lenR = len - lenM - lenL;
		val = getSpace(lenL) + val + getSpace(lenR);
		System.out.println(getSpace(height * len) + val);
		printInOrder(head.l, height + 1, "^", len);
	}

	// for test
	public static String getSpace(int num) {
		String space = " ";
		StringBuffer buf = new StringBuffer("");
		for (int i = 0; i < num; i++) {
			buf.append(space);
		}
		return buf.toString();
	}

	public static void main(String[] args) {
		SizeBalancedTreeMap<String, Integer> sbt = new SizeBalancedTreeMap<String, Integer>();
		sbt.put("d", 4);
		sbt.put("c", 3);
		sbt.put("a", 1);
		sbt.put("b", 2);
		// sbt.put("e", 5);
		sbt.put("g", 7);
		sbt.put("f", 6);
		sbt.put("h", 8);
		sbt.put("i", 9);
		sbt.put("a", 111);
		System.out.println(sbt.get("a"));
		sbt.put("a", 1);
		System.out.println(sbt.get("a"));
		for (int i = 0; i < sbt.size(); i++) {
			System.out.println(sbt.getIndexKey(i) + " , " + sbt.getIndexValue(i));
		}
		printAll(sbt.root);
		System.out.println(sbt.firstKey());
		System.out.println(sbt.lastKey());
		System.out.println(sbt.floorKey("g"));
		System.out.println(sbt.ceilingKey("g"));
		System.out.println(sbt.floorKey("e"));
		System.out.println(sbt.ceilingKey("e"));
		System.out.println(sbt.floorKey(""));
		System.out.println(sbt.ceilingKey(""));
		System.out.println(sbt.floorKey("j"));
		System.out.println(sbt.ceilingKey("j"));
		sbt.remove("d");
		printAll(sbt.root);
		sbt.remove("f");
		printAll(sbt.root);

	}

}
