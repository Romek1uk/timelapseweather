package rk476.timelapseweather.dataharvester.imagemetrics;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class test {

	public static int search(int[] nums, int target, int low, int high) {
		int mid = low + (high - low) / 2;
		if (high < low) {
			return -1;
		}
		if (nums[mid] == target) {
			return mid;
		}
		if (target > nums[mid]) {
			return search(nums, target, mid + 1, high);
		}
		return search(nums, target, low, mid - 1);
	}
	
	public static int search2(int[] nums, int target) {
		int low = 0;
		int high = nums.length - 1;
		
		while (low <= high) {
			int mid = low + (high - low) / 2;
			if (target > nums[mid]) {
				low = mid + 1;
			}
			else if (target < nums[mid]) {
				high = mid - 1;
			}
			else {
				return mid;
			}
		}
		
		return -1;
	}

	//  {0, 1, 2, 3}, target 3
	// hi = 3
	// low = 0
	// mid = 1
	
	// low = 2, hi = 3 = target
	
	// mid = 2
	// low = 3, hi = 3 = target
	
	// if target is in [3]
	// mid = 2;
	// target too high, low becomes 3
	
	public static void main(String[] args) {
		int[] test = new int[]{0, 1, 2, 3};
		Arrays.sort(test);
		
		System.out.println("--");
		
		System.out.println(search2(test, 3));

	}
}
