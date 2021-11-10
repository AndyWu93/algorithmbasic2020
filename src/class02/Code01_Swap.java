package class02;

/**
 * 异或运算
 * 1. 满足交换律和结合律
 * a^b=b^a;(a^b)^c=a^(b^c)=(a^c)^b
 * 2. 同样一批数，无论什么顺序异或起来的结果一定一样
 * 3.
 * a^0=a;a^a=0;
 * if a^b=c
 * a=a^b^b=c^b=b^c
 * b=a^b^a=c^a=a^c
 *
 * 交换律的运用（a!=b）
 * a=甲，b=乙
 * a=a^b	a=甲^乙，b=乙
 * b=a^b	a=甲^乙，b=甲^乙^乙=甲^0=甲
 * a=a^b	a=甲^乙^甲=乙^0=乙，b=甲
 */
public class Code01_Swap {
	
	public static void main(String[] args) {

		
		
		
		
		
		int a = 16;
		int b = 603;
		
		System.out.println(a);
		System.out.println(b);
		
		
		a = a ^ b;
		b = a ^ b;
		a = a ^ b;
		
		
		System.out.println(a);
		System.out.println(b);
		
		
		
		
		int[] arr = {3,1,100};
		
		int i = 0;
		int j = 0;
		
		arr[i] = arr[i] ^ arr[j];
		arr[j] = arr[i] ^ arr[j];
		arr[i] = arr[i] ^ arr[j];
		
		System.out.println(arr[i] + " , " + arr[j]);
		
		
		
		
		
		
		
		
		
		System.out.println(arr[0]);
		System.out.println(arr[2]);
		
		swap(arr, 0, 0);
		
		System.out.println(arr[0]);
		System.out.println(arr[2]);
		
		
		
	}
	
	
	public static void swap (int[] arr, int i, int j) {
		// arr[0] = arr[0] ^ arr[0];
		arr[i]  = arr[i] ^ arr[j];
		arr[j]  = arr[i] ^ arr[j];
		arr[i]  = arr[i] ^ arr[j];
	}
	
	

}
