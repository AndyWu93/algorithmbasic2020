package class14;

import java.util.HashMap;
import java.util.List;
import java.util.Stack;

/**
 * 并查集
 * 主要功能：
 * boolean isSameSet(a,b)
 * void union(a,b)
 * 在大量调用下并查集以上两个方法均摊下O(1)
 * 实现思路：
 * 给每个元素封装一个指针，该指正一开始指向自己
 * union(a,b)：
 * 获取a、b元素指针指向的最终父节点（以下称元素的代表节点），将一个代表节点的指针指向另一个（小size的指向大size的）
 * isSameSet(a,b)：
 *  获取a、b元素的代表节点，判断他们是不是同一个节点
 *
 *  两个重要的优化：
 *  1.小挂大
 *  2.
 */
public class Code05_UnionFind {

	/*封装的结构*/
	public static class Node<V> {
		V value;

		public Node(V v) {
			value = v;
		}
	}

	public static class UnionFind<V> {
		/*原对象（样本）与封装对象的map*/
		public HashMap<V, Node<V>> nodes;
		/*通过map实现指针功能*/
		public HashMap<Node<V>, Node<V>> parents;
		/*代表节点的size记录维护，只维护代表节点，如果一个代表节点变成了普通节点需要删掉*/
		public HashMap<Node<V>, Integer> sizeMap;

		public UnionFind(List<V> values) {
			nodes = new HashMap<>();
			parents = new HashMap<>();
			sizeMap = new HashMap<>();
			for (V cur : values) {
				Node<V> node = new Node<>(cur);
				nodes.put(cur, node);
				/*一开始，每个节点都是代表节点，size为1*/
				parents.put(node, node);
				sizeMap.put(node, 1);
			}
		}

		/*
		* 给你一个节点，请你往上到不能再往上，把代表返回
		* 该方法调用次数到达/超过N次，决定了复杂度最终O(1)
		* */
		public Node<V> findFather(Node<V> cur) {
			Stack<Node<V>> path = new Stack<>();
			while (cur != parents.get(cur)) {
				/*cur如果没到代表节点，沿途节点加到path里去*/
				path.push(cur);
				cur = parents.get(cur);
			}
			/*此时cur==parent.get(cur)为代表节点*/
			while (!path.isEmpty()) {
				/*返回之前沿途收集的节点（不包括代表节点），都指向代表节点*/
				parents.put(path.pop(), cur);
			}
			return cur;
		}

		/**
		 * isSameSet
		 * @param a
		 * @param b
		 * @return
		 */
		public boolean isSameSet(V a, V b) {
			return findFather(nodes.get(a)) == findFather(nodes.get(b));
		}

		/**
		 * union
		 * @param a
		 * @param b
		 */
		public void union(V a, V b) {
			/*先拿到代表节点*/
			Node<V> aHead = findFather(nodes.get(a));
			Node<V> bHead = findFather(nodes.get(b));
			/*只有在代表节点不一样才合并*/
			if (aHead != bHead) {
				int aSetSize = sizeMap.get(aHead);
				int bSetSize = sizeMap.get(bHead);
				/*确定好大小代表节点*/
				Node<V> big = aSetSize >= bSetSize ? aHead : bHead;
				Node<V> small = big == aHead ? bHead : aHead;
				/*小挂大*/
				parents.put(small, big);
				/*大的size更新*/
				sizeMap.put(big, aSetSize + bSetSize);
				/*删掉小的代表节点*/
				sizeMap.remove(small);
			}
		}

		public int sets() {
			return sizeMap.size();
		}

	}
}
