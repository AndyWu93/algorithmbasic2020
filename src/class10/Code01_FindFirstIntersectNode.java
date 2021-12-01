package class10;

/**
 * 两个有/无环lb，找首个相交点（单链表高级难度）
 *
 * 首先需要一个函数返回一个lb的首个入环节点，如果没有环返回null
 * 思路：
 * 使用容器，遍历lb，set记录每个节点，如果遍历到重复的，返回
 * 进阶：不使用容器
 * 使用快慢指针Fast（2步），Slow（1步）
 * 无环：F能走到null
 * 有环：
 * FS会在环上的某个节点（记为x）相遇，
 * 相遇后F来到开头，调整速度为1步，F（从头）S（从x）同时出发，FS再次相遇的节点就是首个入环节点
 *
 * 然后分别找到lb1，lb2的入环节点loop1，loop2
 * 1. loop1==loop2==null 两个无环节点
 * 使用容器：lb1所有的节点放入set中，再遍历lb2，看有没有在set中的
 * 进阶：不使用容器
 * 遍历lb1，得到length1，来到end1
 * 遍历lb2，得到length2，来到end2
 * 如果end1 != end2，没有相交节点
 * 否则一定相交，且成Y的形状。
 * 比对length1，length2，假设length1比length2长20，那lb1先走20步，lb1lb2再一起走，第一个相交的节点就是首个相交节点
 * 2. lb1，lb2，两个lb，一个有环，一个无环
 * 因为是单链表，所以这种情况不可能相交，如果相交了一定是两个有环的lb
 * 3. 都有环
 * a. 不相交（loop1!=loop2）
 * 		6 6
 *
 * b. lb1,lb2的入环节点是1个（loop1==loop2）
 * 		Y V
 * 		O O
 *  把loop1loop2当成end，就变成了求两个无环lb的首个相交节点的问题
 *
 * c. lb1,lb2的入环节点不是1个（loop1!=loop2）
 *     \/
 *     O
 *  a和c都是loop1!=loop2，如何区分？
 *  沿着loop1往后走，如果走了一圈没有遇到loop2，就是a，返回null；
 *  遇到了loop2就是c，返回loop1或者loop2
 */
public class Code01_FindFirstIntersectNode {

	public static class Node {
		public int value;
		public Node next;

		public Node(int data) {
			this.value = data;
		}
	}

	public static Node getIntersectNode(Node head1, Node head2) {
		if (head1 == null || head2 == null) {
			return null;
		}
		Node loop1 = getLoopNode(head1);
		Node loop2 = getLoopNode(head2);
		if (loop1 == null && loop2 == null) {
			/*两个都是无环*/
			return noLoop(head1, head2);
		}
		if (loop1 != null && loop2 != null) {
			/*两个都是有环*/
			return bothLoop(head1, loop1, head2, loop2);
		}
		/*其他情况*/
		return null;
	}

	// 找到链表第一个入环节点，如果无环，返回null
	public static Node getLoopNode(Node head) {
		if (head == null || head.next == null || head.next.next == null) {
			/*因为有快慢指针，所以至少需要3个点*/
			return null;
		}
		// n1 慢  n2 快
		Node slow = head.next; // n1 -> slow
		Node fast = head.next.next; // n2 -> fast
		while (slow != fast) {
			if (fast.next == null || fast.next.next == null) {
				return null;
			}
			fast = fast.next.next;
			slow = slow.next;
		}
		/*出了while，slow fast 追赶相遇*/
		fast = head; // n2 -> walk again from head
		while (slow != fast) {
			/*调整fast步伐*/
			slow = slow.next;
			fast = fast.next;
		}
		/*再次相遇为入环节点*/
		return slow;
	}

	// 如果两个链表都无环，返回第一个相交节点，如果不想交，返回null
	public static Node noLoop(Node head1, Node head2) {
		if (head1 == null || head2 == null) {
			return null;
		}
		Node cur1 = head1;
		Node cur2 = head2;
		int n = 0;
		while (cur1.next != null) {
			n++;
			cur1 = cur1.next;
		}/*cur1来到了end1*/
		while (cur2.next != null) {
			n--;
			cur2 = cur2.next;
		}/*cur2来到了end2，n就是lb1lb2的长度差*/
		if (cur1 != cur2) {
			return null;
		}
		// n  :  链表1长度减去链表2长度的值
		cur1 = n > 0 ? head1 : head2; // 谁长，谁的头变成cur1
		cur2 = cur1 == head1 ? head2 : head1; // 谁短，谁的头变成cur2
		n = Math.abs(n);
		while (n != 0) {
			/*长链表走差值步*/
			n--;
			cur1 = cur1.next;
		}
		while (cur1 != cur2) {
			/*一起走，第一次相遇就是ans*/
			cur1 = cur1.next;
			cur2 = cur2.next;
		}
		return cur1;
	}

	// 两个有环链表，返回第一个相交节点，如果不想交返回null
	public static Node bothLoop(Node head1, Node loop1, Node head2, Node loop2) {
		Node cur1 = null;
		Node cur2 = null;
		if (loop1 == loop2) {
			/*情况b，方法和无环求法一样，只是end不再是null，而是loop1/loop2*/
			cur1 = head1;
			cur2 = head2;
			int n = 0;
			while (cur1 != loop1) {
				/*到loop1*/
				n++;
				cur1 = cur1.next;
			}
			while (cur2 != loop2) {
				/*到loop2*/
				n--;
				cur2 = cur2.next;
			}
			cur1 = n > 0 ? head1 : head2;
			cur2 = cur1 == head1 ? head2 : head1;
			n = Math.abs(n);
			while (n != 0) {
				n--;
				cur1 = cur1.next;
			}
			while (cur1 != cur2) {
				cur1 = cur1.next;
				cur2 = cur2.next;
			}
			return cur1;
		} else {
			/*要么a，要么c*/
			cur1 = loop1.next;
			while (cur1 != loop1) {
				if (cur1 == loop2) {
					/*遇到了loop2，说明是c*/
					return loop1;
				}
				cur1 = cur1.next;
			}
			/*走到自己还没遇到loop2，不相交*/
			return null;
		}
	}

	public static void main(String[] args) {
		// 1->2->3->4->5->6->7->null
		Node head1 = new Node(1);
		head1.next = new Node(2);
		head1.next.next = new Node(3);
		head1.next.next.next = new Node(4);
		head1.next.next.next.next = new Node(5);
		head1.next.next.next.next.next = new Node(6);
		head1.next.next.next.next.next.next = new Node(7);

		// 0->9->8->6->7->null
		Node head2 = new Node(0);
		head2.next = new Node(9);
		head2.next.next = new Node(8);
		head2.next.next.next = head1.next.next.next.next.next; // 8->6
		System.out.println(getIntersectNode(head1, head2).value);

		// 1->2->3->4->5->6->7->4...
		head1 = new Node(1);
		head1.next = new Node(2);
		head1.next.next = new Node(3);
		head1.next.next.next = new Node(4);
		head1.next.next.next.next = new Node(5);
		head1.next.next.next.next.next = new Node(6);
		head1.next.next.next.next.next.next = new Node(7);
		head1.next.next.next.next.next.next = head1.next.next.next; // 7->4

		// 0->9->8->2...
		head2 = new Node(0);
		head2.next = new Node(9);
		head2.next.next = new Node(8);
		head2.next.next.next = head1.next; // 8->2
		System.out.println(getIntersectNode(head1, head2).value);

		// 0->9->8->6->4->5->6..
		head2 = new Node(0);
		head2.next = new Node(9);
		head2.next.next = new Node(8);
		head2.next.next.next = head1.next.next.next.next.next; // 8->6
		System.out.println(getIntersectNode(head1, head2).value);

	}

}
