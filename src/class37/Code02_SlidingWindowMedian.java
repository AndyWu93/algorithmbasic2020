package class37;

/**
 * 有一个数组arr、长度为k的窗口，求窗口右移时每一步窗口内的中位数
 * 窗口内的中位数概念：
 * 	窗口内总共奇数个数：假设排完序后，位于中间位置的数
 * 	窗口内总共偶数数个数：假设排完序后，就是位于中间位置的2个数的平均值
 * 解题：
 * 	该结构需要满足的条件：
 * 		1. 可以加入重复的数
 * 		2. 可以求出位于index位置的数
 * 		3. 可以删除位于index位置的数
 * 	依然使用上一题的结构，在有序表中添加allSize
 * 	这里用了一个更加方便的实现，用经典的有序表，将index也纳入比较，这样当value一样时也可以当成是不一样的数
 */
public class Code02_SlidingWindowMedian {

	public static class SBTNode<K extends Comparable<K>> {
		public K key;
		public SBTNode<K> l;
		public SBTNode<K> r;
		public int size;

		public SBTNode(K k) {
			key = k;
			size = 1;
		}
	}

	public static class SizeBalancedTreeMap<K extends Comparable<K>> {
		private SBTNode<K> root;

		private SBTNode<K> rightRotate(SBTNode<K> cur) {
			SBTNode<K> leftNode = cur.l;
			cur.l = leftNode.r;
			leftNode.r = cur;
			leftNode.size = cur.size;
			cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
			return leftNode;
		}

		private SBTNode<K> leftRotate(SBTNode<K> cur) {
			SBTNode<K> rightNode = cur.r;
			cur.r = rightNode.l;
			rightNode.l = cur;
			rightNode.size = cur.size;
			cur.size = (cur.l != null ? cur.l.size : 0) + (cur.r != null ? cur.r.size : 0) + 1;
			return rightNode;
		}

		private SBTNode<K> maintain(SBTNode<K> cur) {
			if (cur == null) {
				return null;
			}
			int leftSize = cur.l != null ? cur.l.size : 0;
			int leftLeftSize = cur.l != null && cur.l.l != null ? cur.l.l.size : 0;
			int leftRightSize = cur.l != null && cur.l.r != null ? cur.l.r.size : 0;
			int rightSize = cur.r != null ? cur.r.size : 0;
			int rightLeftSize = cur.r != null && cur.r.l != null ? cur.r.l.size : 0;
			int rightRightSize = cur.r != null && cur.r.r != null ? cur.r.r.size : 0;
			if (leftLeftSize > rightSize) {
				cur = rightRotate(cur);
				cur.r = maintain(cur.r);
				cur = maintain(cur);
			} else if (leftRightSize > rightSize) {
				cur.l = leftRotate(cur.l);
				cur = rightRotate(cur);
				cur.l = maintain(cur.l);
				cur.r = maintain(cur.r);
				cur = maintain(cur);
			} else if (rightRightSize > leftSize) {
				cur = leftRotate(cur);
				cur.l = maintain(cur.l);
				cur = maintain(cur);
			} else if (rightLeftSize > leftSize) {
				cur.r = rightRotate(cur.r);
				cur = leftRotate(cur);
				cur.l = maintain(cur.l);
				cur.r = maintain(cur.r);
				cur = maintain(cur);
			}
			return cur;
		}

		private SBTNode<K> findLastIndex(K key) {
			SBTNode<K> pre = root;
			SBTNode<K> cur = root;
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

		private SBTNode<K> add(SBTNode<K> cur, K key) {
			if (cur == null) {
				return new SBTNode<K>(key);
			} else {
				cur.size++;
				if (key.compareTo(cur.key) < 0) {
					cur.l = add(cur.l, key);
				} else {
					cur.r = add(cur.r, key);
				}
				return maintain(cur);
			}
		}

		private SBTNode<K> delete(SBTNode<K> cur, K key) {
			cur.size--;
			if (key.compareTo(cur.key) > 0) {
				cur.r = delete(cur.r, key);
			} else if (key.compareTo(cur.key) < 0) {
				cur.l = delete(cur.l, key);
			} else {
				if (cur.l == null && cur.r == null) {
					// free cur memory -> C++
					cur = null;
				} else if (cur.l == null && cur.r != null) {
					// free cur memory -> C++
					cur = cur.r;
				} else if (cur.l != null && cur.r == null) {
					// free cur memory -> C++
					cur = cur.l;
				} else {
					SBTNode<K> pre = null;
					SBTNode<K> des = cur.r;
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
			return cur;
		}

		private SBTNode<K> getIndex(SBTNode<K> cur, int kth) {
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
			SBTNode<K> lastNode = findLastIndex(key);
			return lastNode != null && key.compareTo(lastNode.key) == 0 ? true : false;
		}

		public void add(K key) {
			if (key == null) {
				throw new RuntimeException("invalid parameter.");
			}
			SBTNode<K> lastNode = findLastIndex(key);
			if (lastNode == null || key.compareTo(lastNode.key) != 0) {
				root = add(root, key);
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

	}

	/**
	 * 可比较的节点：value一样时，按index比较
	 */
	public static class Node implements Comparable<Node> {
		public int index;
		public int value;

		public Node(int i, int v) {
			index = i;
			value = v;
		}

		@Override
		public int compareTo(Node o) {
			return value != o.value ? Integer.valueOf(value).compareTo(o.value)
					: Integer.valueOf(index).compareTo(o.index);
		}
	}

	public static double[] medianSlidingWindow(int[] nums, int k) {
		SizeBalancedTreeMap<Node> map = new SizeBalancedTreeMap<>();
		for (int i = 0; i < k - 1; i++) {
			/*形成大小为K的窗口*/
			map.add(new Node(i, nums[i]));
		}
		double[] ans = new double[nums.length - k + 1];
		int index = 0;
		/*从k-1位置开始，到数组最右边*/
		for (int i = k - 1; i < nums.length; i++) {
			map.add(new Node(i, nums[i]));
			if (map.size() % 2 == 0) {
				/*偶数个，取出中间的两个数*/
				Node upmid = map.getIndexKey(map.size() / 2 - 1);
				Node downmid = map.getIndexKey(map.size() / 2);
				ans[index++] = ((double) upmid.value + (double) downmid.value) / 2;
			} else {
				Node mid = map.getIndexKey(map.size() / 2);
				ans[index++] = (double) mid.value;
			}
			/*移除窗口左端*/
			map.remove(new Node(i - k + 1, nums[i - k + 1]));
		}
		return ans;
	}

}
