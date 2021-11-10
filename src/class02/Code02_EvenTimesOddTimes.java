package class02;

public class Code02_EvenTimesOddTimes {

	/**
	 * 将所有的数都异或起来，就能找到出现奇数次的数
	 * 因为出现偶数次的数跟自己异或都变成了0，0^x=x
	 * @param arr
	 */
	// arr中，只有一种数，出现奇数次
	public static void printOddTimesNum1(int[] arr) {
		int eor = 0;
		for (int i = 0; i < arr.length; i++) {
			eor ^= arr[i];
		}
		System.out.println(eor);
	}

	/**
	 * 1. 先把所有的数异或起来得到eor，eor一定等于a^b，其他的偶数个的数都异或变成0
	 * 2. 找到eor最右侧的1，表明在这个位上a!=b，记为rightOne；x&-x就能得到最右侧的1
	 * 3. arr中所有的数可以分为两类，一类是和rightOne一样，在那个位上是1，这类数包含一个a（或者b），其他的数都是偶数个；另一类在那个位上是0
	 * 4. 将其中的一类数异或起来，就能得到a（或者b），再将其与eor异或，得到另一个奇数
	 * @param arr
	 */
	// arr中，有两种数，出现奇数次
	public static void printOddTimesNum2(int[] arr) {
		int eor = 0;
		for (int i = 0; i < arr.length; i++) {
			eor ^= arr[i];
		}
		// a 和 b是两种数
		// eor != 0
		// eor最右侧的1，提取出来
		// eor :     00110010110111000
		// rightOne :00000000000001000
		int rightOne = eor & (-eor); // 提取出最右的1
		
		
		int onlyOne = 0; // eor'
		for (int i = 0 ; i < arr.length;i++) {
			//  arr[1] =  111100011110000
			// rightOne=  000000000010000
			/*这一类数在rightOne上都是1，所以&rightOne一定不等于0*/
			if ((arr[i] & rightOne) != 0) {
				onlyOne ^= arr[i];
			}
		}
		System.out.println(onlyOne + " " + (eor ^ onlyOne));
	}

	
	public static int bit1counts(int N) {
		int count = 0;
		
		//   011011010000
		//   000000010000     1
		
		//   011011000000
		// 
		
		
		
		while(N != 0) {
			int rightOne = N & ((~N) + 1);
			count++;
			N ^= rightOne;
			// N -= rightOne
		}
		
		
		return count;
		
	}
	
	
	public static void main(String[] args) {
		int a = 5;
		int b = 7;

		a = a ^ b;
		b = a ^ b;
		a = a ^ b;

		System.out.println(a);
		System.out.println(b);

		int[] arr1 = { 3, 3, 2, 3, 1, 1, 1, 3, 1, 1, 1 };
		printOddTimesNum1(arr1);

		int[] arr2 = { 4, 3, 4, 2, 2, 2, 4, 1, 1, 1, 3, 3, 1, 1, 1, 4, 2, 2 };
		printOddTimesNum2(arr2);

	}

}
