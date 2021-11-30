package class09;

/**
 * 单链表分为左小V中等V右大V
 * 思路1，将链表放入arr中玩partition
 *
 * 进阶：空间复杂度要求O(1)
 * 使用6个变量（一组两个变量，可以维持稳定性，一组一个变量的话会不稳定）
 * 小头，小尾
 * 等头，等尾
 * 大头，大尾
 * 遍历lb
 * 遇到大于V的x，大头、大尾如果是空，直接指向x，如果不空，将现有大尾的节点拿出来，指向x，x成为新的大尾
 * 遇到等于V的x，等头、等尾逻辑同上
 * 遇到小于V的x，小头、小尾逻辑同上
 * 最后将小尾的节点指向等头，等尾的节点指向大头
 */
public class Code03_SmallerEqualBigger {

	public static class Node {
		public int value;
		public Node next;

		public Node(int data) {
			this.value = data;
		}
	}
	/*借助数组*/
	public static Node listPartition1(Node head, int pivot) {
		if (head == null) {
			return head;
		}
		Node cur = head;
		int i = 0;
		while (cur != null) {
			i++;
			cur = cur.next;
		}
		Node[] nodeArr = new Node[i];
		i = 0;
		cur = head;
		for (i = 0; i != nodeArr.length; i++) {
			nodeArr[i] = cur;
			cur = cur.next;
		}
		arrPartition(nodeArr, pivot);
		for (i = 1; i != nodeArr.length; i++) {
			nodeArr[i - 1].next = nodeArr[i];
		}
		nodeArr[i - 1].next = null;
		return nodeArr[0];
	}

	public static void arrPartition(Node[] nodeArr, int pivot) {
		int small = -1;
		int big = nodeArr.length;
		int index = 0;
		while (index != big) {
			if (nodeArr[index].value < pivot) {
				/*与小于区域交换，index移动*/
				swap(nodeArr, ++small, index++);
			} else if (nodeArr[index].value == pivot) {
				index++;
			} else {
				/*与大于区域交换，index不动*/
				swap(nodeArr, --big, index);
			}
		}
	}

	public static void swap(Node[] nodeArr, int a, int b) {
		Node tmp = nodeArr[a];
		nodeArr[a] = nodeArr[b];
		nodeArr[b] = tmp;
	}

	/**
	 * 额外空间复杂度O(1)
	 * @param head
	 * @param pivot
	 * @return
	 */
	public static Node listPartition2(Node head, int pivot) {
		Node sH = null; // small head
		Node sT = null; // small tail
		Node eH = null; // equal head
		Node eT = null; // equal tail
		Node mH = null; // big head
		Node mT = null; // big tail
		Node next = null; // save next node
		// every node distributed to three lists
		while (head != null) {
			next = head.next;
			/*先记住next环境，再断开，后面需要往后遍历*/
			head.next = null;
			if (head.value < pivot) {
				if (sH == null) {
					/*第一个头尾都是你*/
					sH = head;
					sT = head;
				} else {
					/*不是第一个，你放到尾巴后面，你成为新的尾巴*/
					sT.next = head;
					sT = head;
				}
			} else if (head.value == pivot) {
				if (eH == null) {
					eH = head;
					eT = head;
				} else {
					eT.next = head;
					eT = head;
				}
			} else {
				if (mH == null) {
					mH = head;
					mT = head;
				} else {
					mT.next = head;
					mT = head;
				}
			}
			/*往后遍历*/
			head = next;
		}
		//以下动作：小于区域的尾巴，连等于区域的头，等于区域的尾巴连大于区域的头
		if (sT != null) {
			/*如果有小于区域，先把小于区域的尾巴连等于区域的头*/
			sT.next = eH;
			/*
			* 下一步，一定是需要用eT 去接 大于区域的头
			* 所以：
			* 有等于区域，eT = 等于区域的尾结点
			* 无等于区域，eT = 小于区域的尾结点
			* */
			eT = eT == null ? sT : eT; // 下一步，谁去连大于区域的头，谁就变成eT
		}/*如果没有小于区域，et原来是啥还是啥*/

		/*进过上面的过程eT变为尽量不为空的尾巴节点*/
		if (eT != null) {
			/*如果小于区域和等于区域，不是都为空，eT去连大于区域的头*/
			eT.next = mH;
		}
		/*返回头结点，看小等大哪个区域的头不为空就返回哪个*/
		return sH != null ? sH : (eH != null ? eH : mH);
	}

	public static void printLinkedList(Node node) {
		System.out.print("Linked List: ");
		while (node != null) {
			System.out.print(node.value + " ");
			node = node.next;
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Node head1 = new Node(7);
		head1.next = new Node(9);
		head1.next.next = new Node(1);
		head1.next.next.next = new Node(8);
		head1.next.next.next.next = new Node(5);
		head1.next.next.next.next.next = new Node(2);
		head1.next.next.next.next.next.next = new Node(5);
		printLinkedList(head1);
		// head1 = listPartition1(head1, 4);
		head1 = listPartition2(head1, 5);
		printLinkedList(head1);

	}

}
