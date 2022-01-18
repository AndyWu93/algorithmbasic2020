package class35;

/**
 * 有序表之AVL
 *
 * 关于索引
 * 数据库中一切能够建立索引的字段都是可排序的
 * 索引就是将索引字段的值组成搜索二叉树的结构，在查找该字段的时候，能够快速找到，最多遍历一颗树的高度O(logN)
 * 问：为什么不用hash表？
 *     因为不仅需要查一条记录，有时候需要查找一个范围的记录，所以用有序表
 * 有序表就是平衡搜索二叉树，种类：
 *     avl，SizeBalanceTree，红黑树
 * 平衡搜索二叉树如何平衡？
 * 左旋：
 *     以x为头节点的左旋：x下去，x.right(y)上来，x成为y.left,y原来的left成为新的x.right
 * 右旋：
 *     以x为头节点的右旋：x下去，x.left(z)上来，x成为z.right,z原来的right成为新的x.left
 * 左旋右旋不改变树的搜索性，但调整了树的平衡
 *
 * BST的增删查：
 * 增：
 *     从头节点开始，遇到大的往左滑，遇到小的往右滑，直到来到一个null的位置，停
 * 查：
 *     从头节点开始，遇到大的往左滑，遇到小的往右滑，直到来到相等的位置，停
 * 删除节点x：
 *     1）x没有左右子树：直接删掉
 *     2）x仅有左/右一个子树：删掉后，左或者右孩子替代原有位置
 *     3）x既有左子树，又有右子树：
 *         找到x节点右子树最左位置a节点，
 *         用a来替代该节点的环境，
 *         a原来的right替代a原来的环境
 *     为什么这么操作？a节点是x节点右树最小的位置，也就是>x最近的节点
 *     所以也可以用x左树的最右节点y节点来替x的环境
 *
 *
 * AVL树：拥有最严苛的平衡性
 *     任何一个节点的左树和右树的高度差<2
 * avl树增删节点后，平衡性被破坏的4中情况：
 * LL: 左树的左树过长了，解决方案：头节点1次右旋
 * LR: 左树的右树过长了，
 *     解决方案：让左树的右树来到头节点的位置
 *     步骤：（LR->LL->平）
 *         1.针对左树来1次左旋，
 *         2.针对头节点来1次右旋
 * RL: 右树的左树过长了，
 *     解决方案：让右树的左树来到头节点的位置
 *     步骤：（RL->RR->平）
 *         1.针对右树来1次右旋，
 *         2.针对头节点来1次左旋
 * RR: 右树的右树过长了，解决方案：头节点1次左旋
 *
 * LL+LR: 左树的左树、左树的右树都长了，解决方案：按照LL处理
 * RL+RR: 右树的左树、右树的右树都长了，解决方案：按照RR处理
 *
 * AVL树的调整模式：
 *  加入一个节点x
 *     看以x为头的树是否需要调整
 *     再看以x的父节点为头的树是否需要调整
 *     ...
 *     一直到整颗树的头。
 *  调整代价：
 *     加入的节点沿途都需要调整O(logN)
 *     每次调整复杂度O(1)
 *     整体复杂度：O(logN)
 *
 *  删除一个节点x
 *     1）x没有左右子树：删掉后，沿途查看所有父节点看是否需要调整
 *     2）x仅有左/右一个子树：删掉后，沿途查看替代自己环境的节点和所有父节点看是否需要调整
 *     3）x既有左子树，又有右子树：从原右树的最左节点的父节点开始沿途查看是否需要调整
 *
 *
 * 思考：做题时改有序表，一般该的是那部分内容？
 *     avl是有序表的一种实现，有序表的功能就是bst的功能，这些功能不涉及平衡性的变动。
 *     平衡性就像是个补丁，需要再适合的时候调用即可
 */
public class Code01_AVLTreeMap {

	public static class AVLNode<K extends Comparable<K>, V> {
		/*Node中的k、v都是泛型*/
		public K k;
		public V v;
		/*左孩子*/
		public AVLNode<K, V> l;
		/*右孩子*/
		public AVLNode<K, V> r;
		/*以当前节点为头的树高，用于高度调整*/
		public int h;

		public AVLNode(K key, V value) {
			k = key;
			v = value;
			h = 1;
		}
	}

	/**
	 * AVL map
	 */
	public static class AVLTreeMap<K extends Comparable<K>, V> {
		/*整颗树的头节点*/
		private AVLNode<K, V> root;
		/*整棵树大小*/
		private int size;

		public AVLTreeMap() {
			root = null;
			size = 0;
		}

		/**
		 * 以cur为头节点的树右旋
		 * 右旋：
		 *     以x为头节点的右旋：
		 *     	x下去，x.left(z)上来，x成为z.right,z原来的right成为新的x.left
		 *     	高度重新计算，先算下去的节点，再算上来的节点，其他节点高度都没变
		 */
		private AVLNode<K, V> rightRotate(AVLNode<K, V> cur) {
			AVLNode<K, V> left = cur.l;
			/*上来的left的右孩子，给了下去的cur的左孩子*/
			cur.l = left.r;
			/*left上来了，cur下去了*/
			left.r = cur;
			/*上来的left和下去的cur的高度需要重新计算，一定是先计算下去的，再计算上来的*/
			/*计算方式：左右孩子的高度取最大值后+1*/
			cur.h = Math.max((cur.l != null ? cur.l.h : 0), (cur.r != null ? cur.r.h : 0)) + 1;
			left.h = Math.max((left.l != null ? left.l.h : 0), (left.r != null ? left.r.h : 0)) + 1;
			/*返回原cur为头的树的新的头节点*/
			return left;
		}

		/**
		 * 左旋
		 */
		private AVLNode<K, V> leftRotate(AVLNode<K, V> cur) {
			AVLNode<K, V> right = cur.r;
			cur.r = right.l;
			right.l = cur;
			cur.h = Math.max((cur.l != null ? cur.l.h : 0), (cur.r != null ? cur.r.h : 0)) + 1;
			right.h = Math.max((right.l != null ? right.l.h : 0), (right.r != null ? right.r.h : 0)) + 1;
			return right;
		}

		/**
		 * 调整以cur为头的平衡性，并返回新的头
		 */
		private AVLNode<K, V> maintain(AVLNode<K, V> cur) {
			if (cur == null) {
				return null;
			}
			/*左树右树高度都拿出来*/
			int leftHeight = cur.l != null ? cur.l.h : 0;
			int rightHeight = cur.r != null ? cur.r.h : 0;
			/*高度差大于1，需要调整*/
			if (Math.abs(leftHeight - rightHeight) > 1) {
				if (leftHeight > rightHeight) {
					/*L高*/
					int leftLeftHeight = cur.l != null && cur.l.l != null ? cur.l.l.h : 0;
					int leftRightHeight = cur.l != null && cur.l.r != null ? cur.l.r.h : 0;
					if (leftLeftHeight >= leftRightHeight) {
						/*LL(>)或者LL/LR(=)同时,这里开始，每次调用旋转时头部都重新接住了*/
						cur = rightRotate(cur);
					} else {
						/*LR，2次旋转*/
						cur.l = leftRotate(cur.l);
						cur = rightRotate(cur);
					}
				} else {
					/*R高*/
					int rightLeftHeight = cur.r != null && cur.r.l != null ? cur.r.l.h : 0;
					int rightRightHeight = cur.r != null && cur.r.r != null ? cur.r.r.h : 0;
					if (rightRightHeight >= rightLeftHeight) {
						cur = leftRotate(cur);
					} else {
						cur.r = rightRotate(cur.r);
						cur = leftRotate(cur);
					}
				}
			}
			return cur;
		}

		/**
		 * 找到小于等于key最近的节点
		 * bst的查找
		 */
		private AVLNode<K, V> findLastIndex(K key) {
			AVLNode<K, V> pre = root;
			AVLNode<K, V> cur = root;
			while (cur != null) {
				pre = cur;
				if (key.compareTo(cur.k) == 0) {
					break;
				} else if (key.compareTo(cur.k) < 0) {
					cur = cur.l;
				} else {
					cur = cur.r;
				}
			}
			return pre;
		}

		/**
		 * 找到大于等于key最近的节点
		 */
		private AVLNode<K, V> findLastNoSmallIndex(K key) {
			AVLNode<K, V> ans = null;
			AVLNode<K, V> cur = root;
			while (cur != null) {
				if (key.compareTo(cur.k) == 0) {
					ans = cur;
					break;
				} else if (key.compareTo(cur.k) < 0) {
					ans = cur;
					cur = cur.l;
				} else {
					cur = cur.r;
				}
			}
			return ans;
		}

		private AVLNode<K, V> findLastNoBigIndex(K key) {
			AVLNode<K, V> ans = null;
			AVLNode<K, V> cur = root;
			while (cur != null) {
				if (key.compareTo(cur.k) == 0) {
					ans = cur;
					break;
				} else if (key.compareTo(cur.k) < 0) {
					cur = cur.l;
				} else {
					ans = cur;
					cur = cur.r;
				}
			}
			return ans;
		}

		/**
		 * 在以cur为头的树上增加一个节点，并把新的头部返回
		 */
		private AVLNode<K, V> add(AVLNode<K, V> cur, K key, V value) {
			if (cur == null) {
				/*cur是空的，new一个新节点后就返回*/
				return new AVLNode<K, V>(key, value);
			} else {
				if (key.compareTo(cur.k) < 0) {
					/*
					* 如果要加入的key比cur的key小
					* 去cur的左边为头的树增加节点，
					* 增加完后可能返回新的头，所以cur.left把新的头接住
					* */
					cur.l = add(cur.l, key, value);
				} else {
					cur.r = add(cur.r, key, value);
				}
				/*cur树加节点了，高度重新算一下*/
				cur.h = Math.max(cur.l != null ? cur.l.h : 0, cur.r != null ? cur.r.h : 0) + 1;
				/*
				* 整棵树加了节点，需要调平衡
				* 该方法在递归中调用，所以即实现了节点增加完了以后，返回时沿途所有的节点都调整了平衡性
				* */
				return maintain(cur);
			}
		}

		// 在cur这棵树上，删掉key所代表的节点
		// 返回cur这棵树的新头部
		private AVLNode<K, V> delete(AVLNode<K, V> cur, K key) {
			if (key.compareTo(cur.k) > 0) {
				/*去右树删key，删了把新头返回*/
				cur.r = delete(cur.r, key);
			} else if (key.compareTo(cur.k) < 0) {
				/*去左树删key，删了把新头返回*/
				cur.l = delete(cur.l, key);
			} else {
				/*找到了需要删除的key*/
				if (cur.l == null && cur.r == null) {
					/*没有孩子，直接删*/
					cur = null;
				} else if (cur.l == null && cur.r != null) {
					/*只有右孩子，拿右孩子替我*/
					cur = cur.r;
				} else if (cur.l != null && cur.r == null) {
					cur = cur.l;
				} else {
					/*左右孩子都有，先找到右孩子的最左节点des*/
					AVLNode<K, V> des = cur.r;
					while (des.l != null) {
						des = des.l;
					}
					/*把右树的des删掉，且调整了平衡性*/
					cur.r = delete(cur.r, des.k);
					/*des接管原来cur的左右孩子*/
					des.l = cur.l;
					des.r = cur.r;
					cur = des;
				}
			}
			if (cur != null) {
				cur.h = Math.max(cur.l != null ? cur.l.h : 0, cur.r != null ? cur.r.h : 0) + 1;
			}
			/*
			* 为何从cur开始调整？
			* 按照规则，应该从des原来的父节点开始往上调整
			* 但是des在被删除的时候，cur->des之间的所有节点的平衡性已经被调整了（见204行：cur.r = delete(cur.r, des.k);）
			* 所以cur开始调整即可
			* 该方法在递归中调用，所以即实现了节点删除完了以后，返回时沿途所有的节点都调整了平衡性
			* */
			return maintain(cur);
		}

		public int size() {
			return size;
		}

		public boolean containsKey(K key) {
			if (key == null) {
				return false;
			}
			AVLNode<K, V> lastNode = findLastIndex(key);
			return lastNode != null && key.compareTo(lastNode.k) == 0 ? true : false;
		}

		/**
		 * add是私有方法，这个put才是接口方法
		 */
		public void put(K key, V value) {
			if (key == null) {
				return;
			}
			AVLNode<K, V> lastNode = findLastIndex(key);
			if (lastNode != null && key.compareTo(lastNode.k) == 0) {
				/*找到的就更新*/
				lastNode.v = value;
			} else {
				/*没找到就添加，同步一下头节点*/
				size++;
				root = add(root, key, value);
			}
		}

		public void remove(K key) {
			if (key == null) {
				return;
			}
			/*先查一下有没有，有再掉用私有的删除方法*/
			if (containsKey(key)) {
				size--;
				root = delete(root, key);
			}
		}

		public V get(K key) {
			if (key == null) {
				return null;
			}
			AVLNode<K, V> lastNode = findLastIndex(key);
			if (lastNode != null && key.compareTo(lastNode.k) == 0) {
				return lastNode.v;
			}
			return null;
		}

		public K firstKey() {
			if (root == null) {
				return null;
			}
			AVLNode<K, V> cur = root;
			while (cur.l != null) {
				cur = cur.l;
			}
			return cur.k;
		}

		public K lastKey() {
			if (root == null) {
				return null;
			}
			AVLNode<K, V> cur = root;
			while (cur.r != null) {
				cur = cur.r;
			}
			return cur.k;
		}

		public K floorKey(K key) {
			if (key == null) {
				return null;
			}
			AVLNode<K, V> lastNoBigNode = findLastNoBigIndex(key);
			return lastNoBigNode == null ? null : lastNoBigNode.k;
		}

		public K ceilingKey(K key) {
			if (key == null) {
				return null;
			}
			AVLNode<K, V> lastNoSmallNode = findLastNoSmallIndex(key);
			return lastNoSmallNode == null ? null : lastNoSmallNode.k;
		}

	}

}
